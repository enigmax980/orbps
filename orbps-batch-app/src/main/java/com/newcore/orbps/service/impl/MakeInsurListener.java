package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.TransactionCodeHolder;
import com.halo.core.common.util.CommonUtils;
import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.EarnestAccInfoDao;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.util.Date;

/**
 * 保单生效批作业：1.批作业启动时处理，2.批作业处理完毕后处理
 * Created by liushuaifeng on 2017/2/21 0021.
 */
public class MakeInsurListener implements JobExecutionListener {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Resource(name = "batchJobSynLauncher")
	JobLauncher batchJobSynLauncher; // 批作业的同步

	@Resource(name = "cntrDataLandingJob")
	Job cntrDataLandingJob;

	@Resource(name = "taskProcessServiceDascClient")
	TaskProcessService taskProcessService;

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Autowired
	EarnestAccInfoDao earnestAccInfoDao;

	@Autowired
	BatchJobMonitorService batchJobMonitorService;

	private static Logger logger = LoggerFactory.getLogger(IpsnListImportListener.class);
	private final static String pattern = "yyyy-MM-dd HH:mm:ss";
	private final static String STATE_C = "C";
	private final static String STATE_E = "E";

	@Override
	public void beforeJob(JobExecution jobExecution) {
		//更新成当前任务的执行id
		Long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
        taskPrmyDao.updateExcuIdByTaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), taskSeq, jobExecution.getId());

		String regId = CommonUtils.uuid();
		//批作业启动
		batchJobMonitorService.regJobMonitor(regId, "makeInsurJob");
		jobExecution.getExecutionContext().putString("regId", regId);
		//批作业开始执行前
		batchJobMonitorService.beginExcution(jobExecution);
	}

	//@Transaction
	@AsynCall
	@Override
	public synchronized void afterJob(JobExecution jobExecution) {

		if (jobExecution.getStatus() == BatchStatus.STOPPED || jobExecution.getStatus() == BatchStatus.STOPPING) {
			return;
		}

		long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
		String applNo = jobExecution.getJobParameters().getString("applNo");
		//String isMultiPay = jobExecution.getJobParameters().getString("isMultiPay");

		TaskPrmyInfo taskPrmyInfo = taskPrmyDao.queryTaskPrmyInfoByTaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(),
				taskSeq);
		if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C)
				|| StringUtils.equals(taskPrmyInfo.getStatus(), STATE_E)) {
			return;
		}

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			taskPrmyInfo.setStatus(STATE_C);

			//任务完成操作
			logger.info("通知任务平台完成任务：applNo: " + taskPrmyInfo.getApplNo());
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			taskProcessRequestBO.setTaskId(taskPrmyInfo.getTaskId());// 业务服务参数中获取的任务ID
			taskProcessRequestBO.setBusinessKey(taskPrmyInfo.getApplNo());
			taskProcessService.completeTask(taskProcessRequestBO);//
			/*插入保单生效轨迹*/
			TraceNode traceNode = new TraceNode();
			traceNode.setProcDate(new Date());
			traceNode.setProcStat(NEW_APPL_STATE.POLICY_INFORCE.getKey());
			mongoBaseDao.updateOperTrace(applNo, traceNode);
		}else {
			taskPrmyInfo.setStatus(STATE_E);
			String err = jobExecution.getAllFailureExceptions().get(0).getCause().getMessage();
			if (err.length() >= 512) {
				err = "transNo:" + TransactionCodeHolder.get() + "," + err.substring(0, 480);
			}
			taskPrmyInfo.setRemark(err);
		}

		//批作业执行结束后
		if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C)) {
			batchJobMonitorService.afterExcution(true, jobExecution);
		} else {
			batchJobMonitorService.afterExcution(false, jobExecution);
		}

		//完成任务
		taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), pattern)));
		taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
		taskPrmyInfo.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
		taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), taskPrmyInfo);

	}



}
