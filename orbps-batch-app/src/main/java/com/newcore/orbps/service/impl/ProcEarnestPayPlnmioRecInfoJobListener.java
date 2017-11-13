package com.newcore.orbps.service.impl;

import com.halo.core.common.TransactionCodeHolder;
import com.halo.core.common.util.CommonUtils;
import com.newcore.orbps.dao.api.ProcPayAllEarnestInfoDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.service.api.BatchJobMonitorService;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liushuaifeng on 2017/3/24 0024.
 */
public class ProcEarnestPayPlnmioRecInfoJobListener implements JobExecutionListener {
    @Autowired
    ProcPayAllEarnestInfoDao procPayAllEarnestInfoDao;

	@Autowired
	BatchJobMonitorService batchJobMonitorService;

	@Autowired
	TaskPrmyDao taskPrmyDao;
	
    private final static String STATE_E = "E";

    private final static String STATE_C = "C";

    private final static String TIME_FLAG_END = "E";

    @Override
    public void beforeJob(JobExecution jobExecution) {

		//更新成当前任务的执行id
		Long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
        taskPrmyDao.updateExcuIdByTaskSeq(QueueType.PROC_EARNEST_PAY_TASK.toString(), taskSeq, jobExecution.getId());

		String regId = CommonUtils.uuid();
		//批作业启动
		batchJobMonitorService.regJobMonitor(regId, "procEarnestPayPlnmioRecInfoJob");
		jobExecution.getExecutionContext().putString("regId", regId);
		
		//批作业开始执行前
		batchJobMonitorService.beginExcution(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
    	
		if (jobExecution.getStatus() == BatchStatus.STOPPED || jobExecution.getStatus() == BatchStatus.STOPPING) {
			return;
		}
        long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");

        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
			batchJobMonitorService.afterExcution(true, jobExecution);
			
            procPayAllEarnestInfoDao.updateProcEarnestPayTask(STATE_C, taskSeq, "全部支取成功", TIME_FLAG_END);
        }else {
			batchJobMonitorService.afterExcution(false, jobExecution);
			
            String err = jobExecution.getAllFailureExceptions().toString();
            if (err.length() >= 512) {
                err = "transNo:" + TransactionCodeHolder.get() + "," + err.substring(0, 480);
            }
            procPayAllEarnestInfoDao.updateProcEarnestPayTask(STATE_E, taskSeq, err, TIME_FLAG_END);
        }
    }
}
