package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.models.taskprmy.TaskType;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.GrpInsurAppl;
import com.newcore.orbps.service.api.TaskCntrInForceService;

/**
 * @author niuzhihao
 * @ClassName: TaskCntrInForceServiceImpl
 * @Description: 保单生效
 * @updater lijifei
 * @date 2016年9月1日 上午11:06:50
 */
@Service("taskCntrInForceService")
public class TaskCntrInForceServiceImpl implements TaskCntrInForceService {

    @Autowired
    MongoBaseDao mongoBaseDao;


    @Autowired
    TaskPrmyDao taskPrmyDao;

    private final static String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(TaskCntrInForceServiceImpl.class);


    @Override
    @Transaction
    @AsynCall
    public void cntrInForce(String args) {

        String[] argsArr = args.split("_");
        // 业务逻辑开始
        logger.info("执行方法:保单生效" + argsArr[0] + "_" + argsArr[1]);
        //通过投保单号applNo查询mongodb获取管理机构号
        Map<String, Object> map = new HashMap<>();
        map.put("applNo", argsArr[0]);
        GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
        if (null == grpInsurAppl) {
            throw new BusinessException(new Throwable("查询保单基本信息失败appNO=" + argsArr[0]));
        }
        TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
        taskPrmyInfo.setTaskSeq(taskPrmyDao.getTaskSeq(TaskType.CNTR_INFORCE.getTaskType()));
        taskPrmyInfo.setTaskId(argsArr[1]);
        taskPrmyInfo.setStatus("N");
        taskPrmyInfo.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
        taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), DATA_PATTERN));
        taskPrmyInfo.setAskTimes(0);
        taskPrmyInfo.setApplNo(grpInsurAppl.getApplNo());
        taskPrmyInfo.setBusinessKey(grpInsurAppl.getApplNo());
        //新增来源系统，增加到扩展键EXT_KEY0
        taskPrmyInfo.setExtKey0(grpInsurAppl.getAccessSource());
        taskPrmyInfo.setExtKey1(grpInsurAppl.getProvBranchNo());
        taskPrmyDao.insertTaskPrmyInfoByTaskSeq(TaskType.CNTR_INFORCE.getTaskType(), taskPrmyInfo);
      
    }


}
