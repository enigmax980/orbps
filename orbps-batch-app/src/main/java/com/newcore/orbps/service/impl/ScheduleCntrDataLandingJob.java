package com.newcore.orbps.service.impl;

import com.halo.core.batch.annotation.Schedule;
import com.halo.core.common.util.CommonUtils;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
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
 * 保单落地定时任务
 * Created by liushuaifeng on 2017/3/2 0002.
 */
@Schedule(cron = "0 0/1 * * * ?")
@Service("ScheduleCntrDataLandingJob")
@DisallowConcurrentExecution
public class ScheduleCntrDataLandingJob implements Job {

    @Resource(name = "batchJobAsynLauncher")
    JobLauncher batchJobAsynLauncher;// 批作业的异步

    @Resource(name = "batchJobSynLauncher")
    JobLauncher batchJobSynLauncher; // 批作业的同步

    @Resource(name = "cntrDataLandingJob")
    org.springframework.batch.core.Job cntrDataLandingJob; //保单落地job

    @Autowired
    TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Autowired
	BatchJobMonitorService batchJobMonitorService;

    @Autowired
    TaskPrmyDao taskPrmyDao;
    
    private JobParametersBuilder jobParametersBuilder;

    private static Logger logger = LoggerFactory.getLogger(ScheduleCntrDataLandingJob.class);


    private final static String STATE_N = "N"; //默认是N
    private final static String STATE_E = "E";
    private final static String STATE_K = "K";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // 如果查询结果为空，则直接完成任务
        List<TaskCntrDataLandingQueue> taskCntrDataLandingQueueList = taskCntrDataLandingQueueDao.searchByStatus(STATE_N);

        for (TaskCntrDataLandingQueue taskCntrDataLandingQueue : taskCntrDataLandingQueueList){
            logger.info("落地启动开始：" + taskCntrDataLandingQueue.toString());
            String run_id = String.valueOf(System.currentTimeMillis());
			String applNo = taskCntrDataLandingQueue.getApplNo();
            jobParametersBuilder = new JobParametersBuilder();
            JobParameters jobParameters = jobParametersBuilder.addString("applNo", applNo)
                    .addLong("taskSeq", taskCntrDataLandingQueue.getTaskSeq()).addString("isCommonAgreement", taskCntrDataLandingQueue.getIsCommonAgreement())
                    .addString("lstProcType", taskCntrDataLandingQueue.getLstProcType()).addLong("batNo", taskCntrDataLandingQueue.getFinLandBatNo())
                    .addLong("plnBatNo", taskCntrDataLandingQueue.getPlnLandBatNo())
                    .addString("runid",run_id).toJobParameters();
            taskCntrDataLandingQueue.setStatus(STATE_K);
            taskCntrDataLandingQueue.setStartTime(new Date());
            taskCntrDataLandingQueueDao.updateByTaskSeq(taskCntrDataLandingQueue.getTaskSeq(), taskCntrDataLandingQueue);
			
            try {
            	JobExecution jobExecution = batchJobAsynLauncher.run(cntrDataLandingJob, jobParameters);
                taskPrmyDao.updateExcuIdByTaskSeq("TASK_CNTR_DATA_LANDING_QUEUE", taskCntrDataLandingQueue.getTaskSeq(), jobExecution.getId());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                    | JobParametersInvalidException e){

    			String regId = CommonUtils.uuid();
				//批作业启动失败时
				batchJobMonitorService.launcherFailure(regId, applNo, "cntrDataLandingJob", e.getMessage());
				
                taskCntrDataLandingQueue.setStatus(STATE_E);
                taskCntrDataLandingQueue.setRemark(e.toString());
                taskCntrDataLandingQueue.setEndTime(new Date());
                taskCntrDataLandingQueue.setAskTimes(taskCntrDataLandingQueue.getAskTimes() + 1);
                //改变批作业状态为问题状态
                taskCntrDataLandingQueueDao.updateByTaskSeq(taskCntrDataLandingQueue.getTaskSeq(), taskCntrDataLandingQueue);
                logger.info(e.getMessage(), e);
            }

        }

    }
}
