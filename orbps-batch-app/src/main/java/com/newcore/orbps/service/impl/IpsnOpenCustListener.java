package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.common.util.CommonUtils;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.ArchiveListMonitor;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.*;

/**
 * 开户批作业执行完毕后，进行收尾工作。
 *
 * @author liushuaifeng
 *         <p>
 *         创建时间：2016年8月19日上午10:40:15
 */

public class IpsnOpenCustListener implements JobExecutionListener {

    private static Logger logger = LoggerFactory.getLogger(IpsnOpenCustListener.class);
    private final static String pattern = "yyyy-MM-dd HH:mm:ss";
    private final static String STATE_C = "C";
    private final static String STATE_E = "E";
    private final static String FLAG_N = "N";
    private final static Integer TRY_TIMES = 10;
    //private final static String FLAG_F = "F";


    @Autowired
    MongoBaseDao mongoBaseDao;


    @Autowired
    JdbcOperations jdbcTemplate;

    @Autowired
    TaskPrmyDao taskPrmyDao;
    
    @Autowired
    ArchiveListMonitor restfulArchiveListMonitorService;

    @Autowired
    BatchJobMonitorService batchJobMonitorService;

    @Autowired
	ConfigDao configDao;
	
	private static final String PROPERTIE_TYPE= "UDW_OFF" ;  //配置类型
    @Override
    public void beforeJob(JobExecution jobExecution) {
        // TODO Auto-generated method stub
        long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
        taskPrmyDao.updateExcuIdByTaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskSeq, jobExecution.getId()); //更新执行id

        String regId = CommonUtils.uuid();
        //批作业启动
        batchJobMonitorService.regJobMonitor(regId, "ipsnListOpenCustJob");
		jobExecution.getExecutionContext().putString("regId", regId);
		//批作业监控注册
        batchJobMonitorService.beginExcution(jobExecution);
        logger.info("taskSeq={}, excutionId={}", taskSeq, jobExecution.getId());

    }

    @AsynCall
    @Transaction
    public synchronized void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.STOPPED || jobExecution.getStatus() == BatchStatus.STOPPING) {
            return;
        }

        long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");

        TaskPrmyInfo taskPrmyInfo = taskPrmyDao.queryTaskPrmyInfoByTaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(),
                taskSeq);
        if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C) || StringUtils.equals(taskPrmyInfo.getStatus(), STATE_E)) {
            return;
        }

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            Map<String, Object> mongoMap = new HashMap<>();
            Map<String, String> mapVar = new HashMap<>();
            mongoMap.put("applNo", taskPrmyInfo.getApplNo());
            mongoMap.put("procFlag", FLAG_N);
            Long count = mongoBaseDao.count(GrpInsured.class, mongoMap);
//			mongoMap.put("procFlag", FLAG_F);
//			Long count1 = mongoBaseDao.count(GrpInsured.class, mongoMap);
            logger.info("applNo: " + taskPrmyInfo.getApplNo() + ", " + "procFlag : N-" + count);

            if (count == 0) {
                taskPrmyInfo.setStatus(STATE_C);
                //添加操作轨迹
                TraceNode traceNode = new TraceNode();
                traceNode.setProcStat(NEW_APPL_STATE.INSURED_ACCOUNT.getKey());
                traceNode.setProcDate(new Date());

                mongoBaseDao.updateOperTrace(taskPrmyInfo.getApplNo(), traceNode);
                //增加是否跳过核保的逻辑,如果不进行核保，则手工插入核保结论2
                Map<String, Object> applNoMap = new HashMap<String, Object>();
    			applNoMap.put("applNo", taskPrmyInfo.getApplNo());
    			applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
    			InsurApplOperTrace applOperTrace=(InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,  applNoMap);
                String procSwitch = configDao.queryPropertiesConfigure(PROPERTIE_TYPE,applOperTrace.getOperTraceDeque().getLast().getPclkNo(),null);
                if (StringUtils.equals(procSwitch, YES_NO_FLAG.YES.getKey())){
                    logger.info("applNo={}, 核保开关关闭，准备写核保结论……", taskPrmyInfo.getApplNo());

                    ApplUdwResult applUdwResult = new ApplUdwResult();
                    applUdwResult.setBusinessKey(taskPrmyInfo.getApplNo());
                    applUdwResult.setUdwResult("0");
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> businessMap = new HashMap<>();
                    map.put("applNo", taskPrmyInfo.getApplNo());
                    businessMap.put("businessKey", taskPrmyInfo.getApplNo());
                    GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
                    List<UdwPolResult> udwPolResults = new ArrayList<>();
                    for (Policy policy : grpInsurAppl.getApplState().getPolicyList()){
                        UdwPolResult udwPolResult = new UdwPolResult();
                        udwPolResult.setPolCode(policy.getPolCode());
                        udwPolResult.setUdwResult("0");
                        udwPolResult.setAddFeeAmnt(0D);
                        udwPolResult.setAddFeeYear(0);
                        udwPolResults.add(udwPolResult);
                    }
                    applUdwResult.setUdwPolResults(udwPolResults);
                    mongoBaseDao.remove(ApplUdwResult.class, businessMap);
                    mongoBaseDao.insert(applUdwResult);
                    //插入核保轨迹
                    TraceNode udwTraceNode = new TraceNode();
                    udwTraceNode.setProcStat(NEW_APPL_STATE.UNDERWRITING.getKey());
                    udwTraceNode.setProcDate(new Date());
                    mongoBaseDao.updateOperTrace(taskPrmyInfo.getApplNo(), udwTraceNode);

                }

                // 进行任务完成操作
                TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
                taskProcessRequestBO.setProcessVar(mapVar);
                TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
                taskProcessRequestBO.setTaskId(taskPrmyInfo.getTaskId());// 业务服务参数中获取的任务ID
                taskProcessRequestBO.setBusinessKey(taskPrmyInfo.getApplNo());
                taskProcessService.completeTask(taskProcessRequestBO);//
            } else {
                taskPrmyInfo.setStatus(STATE_E);
            }
        } else {
            taskPrmyInfo.setStatus(STATE_E);
        }

        //增加批作业如果存在处理失败记录，自行执行十次，超过十次则停止执行
        if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_E)) {
            if (taskPrmyInfo.getAskTimes() < TRY_TIMES + 1) {
                taskPrmyInfo.setStatus(FLAG_N);
            }

        }

        // 任务结束时间。
        taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), pattern)));
        taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
        String err = jobExecution.getAllFailureExceptions().toString();
        if (err.length() >= 512) {
            err = err.substring(0, 512);
        }
        taskPrmyInfo.setRemark(err);
        taskPrmyInfo.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
        logger.info(taskPrmyInfo.toString());
        taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskPrmyInfo);
        logger.info("applNo: " + "[" + taskPrmyInfo.getApplNo() + "]" + "开户结束");
        /*如果是档案清单发起二次落地服务*/
        if (STATE_C.equals(taskPrmyInfo.getStatus())) {
            CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            restfulArchiveListMonitorService.send(taskPrmyInfo.getApplNo());
        }
        //批作业执行结束后
        if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C)) {
            batchJobMonitorService.afterExcution(true, jobExecution);
        } else {
            batchJobMonitorService.afterExcution(false, jobExecution);
        }
        
    }
}
