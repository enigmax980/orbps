package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.common.util.CommonUtils;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.BatchJobMonitorService;

/**
 * 暂缴费支取-全部支取定时任务
 * Created by lijifei on 2017/3/7 0002.
 */
@Schedule(cron = "0 0/30 * * * ?")
@Service("ProcPayAllEarnestInfoJob")
@DisallowConcurrentExecution
public class ProcPayAllEarnestInfoJob implements Job {

	@Resource(name = "batchJobAsynLauncher")
	JobLauncher batchJobAsynLauncher;// 批作业的异步

	@Resource(name = "batchJobSynLauncher")
	JobLauncher batchJobSynLauncher; // 批作业的同步

	@Resource(name = "procEarnestPayPlnmioRecInfoJob")
	org.springframework.batch.core.Job procEarnestPayPlnmioRecInfoJob; //暂缴费支取-全部支取job

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Autowired
	BatchJobMonitorService batchJobMonitorService;
	
	private final static String STATE_N = "N";

	private final static String STATE_E = "E";

	private final static String STATE_K = "K";

	private final static String PATTERN = "yyyy-MM-dd HH:mm:ss";

	private JobParametersBuilder jobParametersBuilder;

	private static Logger logger = LoggerFactory.getLogger(ProcPayAllEarnestInfoJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// 如果查询结果为空，则直接完成任务，如果不为空则执行操作
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao
				.queryTaskPrmyInfoByStatus(QueueType.PROC_EARNEST_PAY_TASK.toString(), STATE_N);

		if (taskPrmyInfos == null || taskPrmyInfos.isEmpty()) {
			logger.info("暂缴费全部支取无符合条件任务，暂缴费全部支取批作业结束。");
			return;
		}

		for (int j = 0; j < taskPrmyInfos.size(); j++) {
			TaskPrmyInfo taskPrmyInfo = taskPrmyInfos.get(j);
			logger.info("暂缴费全部支取启动开始：" + taskPrmyInfo.getApplNo());
			String run_id = String.valueOf(System.currentTimeMillis());
			jobParametersBuilder = new JobParametersBuilder();
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("WHERE ACC_NO LIKE '");
			whereBuffer.append(taskPrmyInfo.getApplNo()+"%");
			whereBuffer.append("'");
			whereBuffer.append(" AND BALANCE > 0 ");
			JobParameters jobParameters = jobParametersBuilder.addString("query.where", whereBuffer.toString())
					.addString("runid",run_id).addLong("taskSeq", taskPrmyInfo.getTaskSeq()).toJobParameters();
			//修改控制表状态
			taskPrmyInfo.setStatus(STATE_K);
			taskPrmyInfo.setRemark("暂缴费全部支取处理中");
			taskPrmyInfo.setStartTime(DateFormatUtils.format(new Date(), PATTERN));
            if (!taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.PROC_EARNEST_PAY_TASK.toString(), taskPrmyInfo)) {
                logger.error("applNo:" + taskPrmyInfo.getApplNo() + "; 表:" + QueueType.PROC_EARNEST_PAY_TASK.toString()
                        + " 更新失败。");
                continue;
            }
			
			try {
				JobExecution jobExecution = batchJobSynLauncher.run(procEarnestPayPlnmioRecInfoJob, jobParameters);

				taskPrmyDao.updateExcuIdByTaskSeq(QueueType.PROC_EARNEST_PAY_TASK.toString(), taskPrmyInfo.getTaskSeq(), jobExecution.getId());
				
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e){
				String regId = CommonUtils.uuid();
				//批作业启动失败时
				batchJobMonitorService.launcherFailure(regId, taskPrmyInfo.getApplNo(), "procEarnestPayPlnmioRecInfoJob", e.getMessage());
				
				logger.info(e.getMessage(), e);

				taskPrmyInfo.setStatus(STATE_E);
				taskPrmyInfo.setRemark(e.toString());
				taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), PATTERN)));
                taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
                taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.PROC_EARNEST_PAY_TASK.toString(), taskPrmyInfo);
            }
		}//end for
	}

}
