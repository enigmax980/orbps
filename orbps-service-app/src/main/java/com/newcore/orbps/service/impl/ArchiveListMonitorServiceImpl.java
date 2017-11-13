package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.ArchiveListMonitor;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.dicts.LST_PROC_TYPE;


@Service("archiveListMonitor")
public class ArchiveListMonitorServiceImpl implements ArchiveListMonitor {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    TaskPrmyDao taskPrmyDao;
    @Autowired
    InsurApplOperUtils insurApplLandUtils;
    @Autowired
    TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;
    @Autowired
    JdbcOperations jdbcTemplate;
    @Autowired
    MongoBaseDao mongoBaseDao;


    private static Logger logger = LoggerFactory.getLogger(ArchiveListMonitorServiceImpl.class);

    private static String LANDED = "3";/*已落地*/
    private static String SUCC_FIRST = "A";/*第一次落地完毕时*/
    private static String SUCC_SEC = "S";/*二次落地成功*/
    private static String FAIL_SEC = "F";/*二次落地失败*/
    private static String NO_LIST = "N";/*无清单导入*/
    private static String OPENING = "K";/*第一次落地完毕时*/
    private static String LAND_EX = "E";/*落地异常*/


    @Override
    public void send(String applNo) {
        if (StringUtils.isEmpty(applNo)) {
            logger.error("投保单号为空");
            return;
        }
        GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)), GrpInsurAppl.class);
        if (null == grpInsurAppl) {
            return;
        }
        if (LST_PROC_TYPE.ARCHIVES_LIST.getKey().equals(grpInsurAppl.getLstProcType())) {
            if (SUCC_FIRST.equals(query(applNo))) {
                taskCntrDataLandingQueueDao.updateArchiveList(applNo);
                mongoTemplate.updateFirst(Query.query(Criteria.where("applNo").is(applNo)), Update.update("lstProcType", "L"), GrpInsurAppl.class);
            }
        }
    }

    /**
     * @param ArchiveListMonitor
     * @return String  档案清单已开户，第一次落地完毕时-A,二次落地成功-S,二次落地失败-F,无二次落地-N,正在开户中-K,落地异常-"E"
     * @author huanglong
     * @date 2017年4月6日
     * @content 查询档案清单状态
     */
    @Override
    public String query(String applNo) {
        if (StringUtils.isEmpty(applNo)) {
            return null;
        }
        RetInfo retInfo = new RetInfo();
        List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo, null);
        /*无二次落地*/
        if (null == taskPrmyInfos || taskPrmyInfos.isEmpty()) {
            return NO_LIST;
        }

        Map<String, Object> mongoMap = new HashMap<>();
        mongoMap.put("applNo", applNo);
        mongoMap.put("procFlag", "N");
        Long count = mongoBaseDao.count(GrpInsured.class, mongoMap);
//        List<TaskPrmyInfo> taskPrmyInfos1 = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), applNo, "C");
        //所有被保人开户完毕
        if (count == 0L) {
            retInfo = insurApplLandUtils.getIsInsurApplLanding(applNo);
            TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByApplNo(applNo);
            if (null != retInfo
                    && LANDED.equals(retInfo.getRetCode())
                    && null != taskCntrDataLandingQueue
                    && LST_PROC_TYPE.ARCHIVES_LIST.getKey().equals(taskCntrDataLandingQueue.getLstProcType())) {
                return SUCC_FIRST;
            }
            if (null != taskCntrDataLandingQueue
                    && LST_PROC_TYPE.ORDINARY_LIST.getKey().equals(taskCntrDataLandingQueue.getLstProcType())
                    && LST_PROC_TYPE.ARCHIVES_LIST.getKey().equals(taskCntrDataLandingQueue.getExtKey1())) {
                if (null != retInfo
                        && LANDED.equals(retInfo.getRetCode())) {
                    return SUCC_SEC;
                } else {
                    return FAIL_SEC;
                }
            }
            return NO_LIST; //说明是普通清单流程
        } else {
            return OPENING;
        }

    }
}
