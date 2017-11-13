package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.common.util.CommonUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.YES_NO_FLAG;

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

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by liushuaifeng on 2017/2/28 0028.
 */

@Schedule(cron = "0 0/1 * * * ?")
@Service("ScheduleMakeInsur")
@DisallowConcurrentExecution
public class ScheduleMakeInsurJob implements Job {
	@Resource(name = "batchJobAsynLauncher")
	JobLauncher batchJobAsynLauncher;// 批作业的异步

	@Resource(name = "batchJobSynLauncher")
	JobLauncher batchJobSynLauncher; // 批作业的同步

	@Resource(name = "makeInsurJob")
	org.springframework.batch.core.Job makeInsurJob;

	JobParametersBuilder jobParametersBuilder;

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Autowired
	ConfigDao configDao;

	@Autowired
	MongoBaseDao mongoBaseDao;

	private static final String PROPERTIE_TYPE = "IMAGE_APPROVAL"; // 配置类型

	@Autowired
	BatchJobMonitorService batchJobMonitorService;

	private static Logger logger = LoggerFactory.getLogger(ScheduleImportIpsnListJob.class);

	private final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private final static String STATE_N = "N";
	private final static String STATE_E = "E";
	private final static String STATE_K = "K";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("生效批作业开始....");
		// 如果查询结果为空，则直接完成任务
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao
				.queryTaskPrmyInfoByStatus(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), STATE_N);
		if (taskPrmyInfos == null || taskPrmyInfos.isEmpty()) {
			logger.info("生效队列暂无符合条件任务，清单导入批作业结束。");
			return;
		}
		String str = "N";
		for (int j = 0; j < taskPrmyInfos.size(); j++) {
			TaskPrmyInfo taskPrmyInfo = taskPrmyInfos.get(j);
			//查询规则表根据规则类型,机构号,系统来源进行查询
			str = configDao.queryPropertiesConfigure(PROPERTIE_TYPE, taskPrmyInfo.getExtKey0(),
					taskPrmyInfo.getExtKey1());
			// 规则存在并且为Y 进行审批判断 
			if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), str)) {
				//查询影像资料是否审批
				TraceNode traceNode = mongoBaseDao.getPeekImgApprovalTrace(taskPrmyInfo.getApplNo());
				//如果影像审批通过就走生效流程 否则进行下一条带生效流程
 				if (null == traceNode  || !StringUtils.equals(traceNode.getProcStat(), YES_NO_FLAG.YES.getKey())) {
					continue;
				}
			}
			logger.info(" line: [" + j + "] " + taskPrmyInfo.toString());
			jobParametersBuilder = new JobParametersBuilder();
			String applNo = taskPrmyInfo.getApplNo();
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("WHERE ACC_NO LIKE '");
			whereBuffer.append(taskPrmyInfo.getApplNo() + "%");
			whereBuffer.append("'");
			JobParameters jobParameters = jobParametersBuilder.addString("applNo", taskPrmyInfo.getApplNo())
					.addString("query.where", whereBuffer.toString()).addLong("taskSeq", taskPrmyInfo.getTaskSeq())
					.toJobParameters();

			// 改变批作业状态为执行状态
			taskPrmyInfo.setStatus(STATE_K);
			taskPrmyInfo.setStartTime(DateFormatUtils.format(new Date(), PATTERN));
			if (!taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), taskPrmyInfo)) {
				logger.error("applNo:" + taskPrmyInfo.getApplNo() + "; 表:"
						+ QueueType.CNTR_INFORCE_TASK_QUEUE.toString() + " 更新失败。");
				continue;
			}

			try {

				JobExecution jobExecution = batchJobSynLauncher.run(makeInsurJob, jobParameters);

				taskPrmyDao.updateExcuIdByTaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(),
						taskPrmyInfo.getTaskSeq(), jobExecution.getId());

			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				String regId = CommonUtils.uuid();
				// 批作业启动失败时
				batchJobMonitorService.launcherFailure(regId, applNo, "makeInsurJob", e.getMessage());

				// 改变批作业状态为问题状态
				taskPrmyInfo.setStatus(STATE_E);
				taskPrmyInfo.setRemark(e.toString());
				taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), PATTERN)));
				taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
				taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), taskPrmyInfo);
				logger.info(e.getMessage(), e);
			}

		}
	}
}
