package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.common.util.CommonUtils;
import com.halo.core.common.util.PropertiesUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;

/**
 * 清单导入批作业定时启动。
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年9月12日下午4:17:04
 */

@Schedule(cron = "*/30 * * * * ?") // 每隔三十秒执行一次。
@Service("ScheduleImportIpsnList")
@DisallowConcurrentExecution
public class ScheduleImportIpsnListJob implements Job {

	@Resource(name = "batchJobAsynLauncher")
	JobLauncher batchJobAsynLauncher;// 批作业的异步

	@Resource(name = "batchJobSynLauncher")
	JobLauncher batchJobSynLauncher; // 批作业的同步

	@Resource
	org.springframework.batch.core.Job importIpsnListJob;

	JobParametersBuilder jobParametersBuilder;

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	JdbcOperations jdbcTemplate;

//	@Autowired
//	BatchJobMonitorService batchJobMonitorService;

	private static Logger logger = LoggerFactory.getLogger(ScheduleImportIpsnListJob.class);

	private final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private final static String STATE_N = "N";
	private final static String STATE_E = "E";
	private final static String STATE_K = "K";
	private final static String ASY = "asy";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("清单导入批作业开始。");
		// 如果查询结果为空，则直接完成任务
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao
				.queryTaskPrmyInfoByStatus(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), STATE_N);
		if (taskPrmyInfos == null || taskPrmyInfos.isEmpty()) {
			logger.info("清单导入队列暂无符合条件任务，清单导入批作业结束。");
			return;
		}
		// 获取批作业执行同步、异步开关
		String procSwitch = PropertiesUtils.getProperty("batch.ipsn.import.switch");

		for (int j = 0; j < taskPrmyInfos.size(); j++) {
			TaskPrmyInfo taskPrmyInfo = taskPrmyInfos.get(j);
			logger.info(" line: [" + j + "] " + taskPrmyInfo.toString());

			String run_id = String.valueOf(System.currentTimeMillis());
			String mapName = CommonUtils.uuid();
			jobParametersBuilder = new JobParametersBuilder();
			String applNo = taskPrmyInfo.getApplNo();
			String resourceId = taskPrmyInfo.getListPath();
			String regId = CommonUtils.uuid();
			JobParameters jobParameters = jobParametersBuilder.addString("applNo", applNo)
					.addString("targetResource", resourceId).addLong("taskSeq", taskPrmyInfo.getTaskSeq())
					.addString("run_id", run_id).addString("mapName", mapName).addString("regId", regId).toJobParameters();

			// 改变批作业状态为执行状态
			taskPrmyInfo.setStatus(STATE_K);
			taskPrmyInfo.setStartTime(DateFormatUtils.format(new Date(), PATTERN));
			if (!taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo)) {
				logger.error("applNo:" + taskPrmyInfo.getApplNo() + "; 表:" + QueueType.EV_CUST_SET_TASK_QUEUE.toString()
						+ " 更新失败。");
				continue;
			}
			// 清单导入前，删除错误数据表
			Map<String, Object> map = new HashMap<>();
			map.put("applNo", applNo);
			mongoBaseDao.remove(ErrorGrpInsured.class, map);
			logger.info(applNo + "：清单导入job开始");
			
			//批作业启动
//			batchJobMonitorService.regJobMonitor(regId, "ImportIpsnListJob");
			
			try {
				JobExecution jobExecution = null;
				
				// 异步启动，否则同步启动
				if (StringUtils.equals(procSwitch, ASY)) {
					jobExecution = batchJobAsynLauncher.run(importIpsnListJob, jobParameters);

				} else {
					jobExecution = batchJobSynLauncher.run(importIpsnListJob, jobParameters);
				}
				
				taskPrmyDao.updateExcuIdByTaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo.getTaskSeq(), jobExecution.getId());

			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				//批作业启动失败时
//				batchJobMonitorService.launcherFailure(regId, applNo, "ImportIpsnListJob", e.getMessage());
				// 改变批作业状态为问题状态
				taskPrmyInfo.setStatus(STATE_E);
				taskPrmyInfo.setRemark(e.toString());
				taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), PATTERN)));
				taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
				taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo);
				logger.info(e.getMessage(), e);
			}

		}

	}
}
