package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.TransactionCodeHolder;
import com.halo.core.common.util.CommonUtils;
import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by liushuaifeng on 2017/2/13 0013.
 */
public class CntrDataLandingJobListener implements JobExecutionListener {

	private final static String STATE_C = "C";
	private final static String STATE_E = "E";
//	private final static String STATE_K = "K";
	private final static String FAIL_LAND_STATE = "2";

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;
	
	@Autowired
	BatchJobMonitorService batchJobMonitorService;

	@Autowired
	TaskPrmyDao taskPrmyDao;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {

		//更新成当前任务的执行id
		Long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
        taskPrmyDao.updateExcuIdByTaskSeq(QueueType.TASK_CNTR_DATA_LANDING_QUEUE.toString(), taskSeq, jobExecution.getId());

		String regId = CommonUtils.uuid();
		//批作业启动
		batchJobMonitorService.regJobMonitor(regId, "cntrDataLandingJob");
		jobExecution.getExecutionContext().putString("regId", regId);
		
        //批作业开始执行前
		batchJobMonitorService.beginExcution(jobExecution);

	}

	@Transaction
	@Override
	public synchronized void afterJob(JobExecution jobExecution) {
		
		if (jobExecution.getStatus() == BatchStatus.STOPPED || jobExecution.getStatus() == BatchStatus.STOPPING) {
			return;
		}
		
		long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByTaskSeq(taskSeq);
		taskCntrDataLandingQueue.setAskTimes(taskCntrDataLandingQueue.getAskTimes() + 1);
		taskCntrDataLandingQueue.setEndTime(new Date());

		if (jobExecution.getStatus() == BatchStatus.COMPLETED){
			if(StringUtils.equals(FAIL_LAND_STATE, taskCntrDataLandingQueue.getInsurApplLandFlag()) 
					|| StringUtils.equals(FAIL_LAND_STATE, taskCntrDataLandingQueue.getCommonAgreementLandFlag())
					|| StringUtils.equals(FAIL_LAND_STATE, taskCntrDataLandingQueue.getIpsnLandFlag())
					|| StringUtils.equals(FAIL_LAND_STATE, taskCntrDataLandingQueue.getFinLandFlag())){
				taskCntrDataLandingQueue.setStatus(STATE_E);
				taskCntrDataLandingQueue.setRemark("存在落地失败");
			}else{
				taskCntrDataLandingQueue.setStatus(STATE_C);
				taskCntrDataLandingQueue.setRemark("成功");
			}
		}else {
			taskCntrDataLandingQueue.setStatus(STATE_E);
			String err = jobExecution.getAllFailureExceptions().toString();
			if (err.length() >= 512) {
				err = "transNo:" + TransactionCodeHolder.get() + "," + err.substring(0, 480);
			}
			
		    taskCntrDataLandingQueue.setRemark(err);
			
		}

		//批作业执行结束后
		if (StringUtils.equals(taskCntrDataLandingQueue.getStatus(), STATE_C)) {
			batchJobMonitorService.afterExcution(true, jobExecution);
		} else {
			batchJobMonitorService.afterExcution(false, jobExecution);
		}
		
		taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);

	}
}
