package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.models.taskprmy.TaskType;
import com.newcore.orbps.service.api.InsurApplOperUtilsService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 保单落地查询服务
 * Created by liushuaifeng on 2017/2/24 0024.
 */
@Service("insurApplOperUtilsService")
public class InsurApplOperUtilsServiceImpl implements InsurApplOperUtilsService {

    @Autowired
    TaskPrmyDao taskPrmyDao;

    @Autowired
    TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

    @Autowired
    MongoBaseDao mongoBaseDao;

    private final static String SUCESS_LAND_STATUS = "3";

    /**
     * 是否落地在途，即财务，被保人、保单基本信息、共保协议等是否落地完成
     * 1.未生效
     * 2.生效、落地在途
     * 3.已落地
     * @param applNo
     * @return
     */
    @Override
    public RetInfo getIsInsurApplLanding(String applNo) {
        RetInfo retInfo = new RetInfo();
        retInfo.setApplNo(applNo);

        //获取生效落地队列表
        List<TaskPrmyInfo> taskPrmyInfoList = taskPrmyDao.selectTaskPrmyInfoByApplNo(TaskType.CNTR_INFORCE.getTaskType(), applNo, null);
        if (null == taskPrmyInfoList || taskPrmyInfoList.isEmpty()){
            retInfo.setRetCode("1");
            return retInfo;
        }

        //获取落地监控表
        TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByApplNo(applNo);
        if (null == taskCntrDataLandingQueue) {
            retInfo.setRetCode("2");
            return retInfo;
        }

        if (StringUtils.equals("C", taskCntrDataLandingQueue.getStatus()) //落地批作业已经完成
                && StringUtils.equals(SUCESS_LAND_STATUS, taskCntrDataLandingQueue.getFinLandFlag()) //财务落地完成
                && StringUtils.equals(SUCESS_LAND_STATUS, taskCntrDataLandingQueue.getInsurApplLandFlag())) {//帽子落地完成

            //如果是普通清单
            if (StringUtils.equals(taskCntrDataLandingQueue.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
                //被保人须落地告知
                if (!StringUtils.equals(SUCESS_LAND_STATUS, taskCntrDataLandingQueue.getIpsnLandFlag())) {
                    retInfo.setRetCode("2");
                    return retInfo;
                }
            }
            //如果是共保
            if (StringUtils.equals(taskCntrDataLandingQueue.getIsCommonAgreement(), YES_NO_FLAG.YES.getKey())) {
                if (!StringUtils.equals(SUCESS_LAND_STATUS, taskCntrDataLandingQueue.getCommonAgreementLandFlag())) {
                    retInfo.setRetCode("2");
                    return retInfo;
                }
            }

            retInfo.setRetCode("3");
            return retInfo;
        }

        retInfo.setRetCode("2");
        return retInfo;
    }

}
