package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.halo.core.common.util.CommonUtils;
import com.newcore.orbps.service.api.BatchJobMonitorService;
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

import com.alibaba.druid.util.StringUtils;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.common.util.PropertiesUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;

/**
 * 被保人开户批作业定时启动
 *
 * @author liushuaifeng
 *         <p>
 *         创建时间：2016年9月23日下午3:42:31
 */

@Schedule(cron = "*/30 * * * * ?") // 每隔一分钟执行一次。
@Service("ScheduleIpsnOpenCust")
@DisallowConcurrentExecution
public class ScheduleIpsnOpenCustJob implements Job {

    @Resource(name = "batchJobSynLauncher")
    JobLauncher batchJobSynLauncher; // 批作业同步启动器

    @Resource(name = "batchJobAsynLauncher")
    JobLauncher batchJobAsynLauncher; // 批作业异步启动器

    @Resource(name = "ipsnListOpenCustJob")
    org.springframework.batch.core.Job ipsnListOpenCustJob;

    JobParametersBuilder jobParametersBuilder;

    @Autowired
    TaskPrmyDao taskPrmyDao;

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Autowired
    private BatchJobMonitorService batchJobMonitorService;//批作业监控服务

    private static Logger logger = LoggerFactory.getLogger(ScheduleIpsnOpenCustJob.class);

    private final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String STATE_N = "N";
    private final static String STATE_E = "E";
    private final static String STATE_K = "K";
    private final static String ASY = "asy";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("开户批作业开始");

        // 如果查询结果为空，则直接完成任务
        List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao
                .queryTaskPrmyInfoByStatus(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), STATE_N);
        if (taskPrmyInfos == null || taskPrmyInfos.isEmpty()) {

            logger.info("开户队列暂无符合条件任务，开户批作业结束");
            return;
        }
        String procSwitch = PropertiesUtils.getProperty("batch.ipsn.opencust.switch");

        for (int i = 0; i < taskPrmyInfos.size(); i++) {

            TaskPrmyInfo taskPrmyInfo = taskPrmyInfos.get(i);
            logger.info(" line: [" + i + "] " + taskPrmyInfo.toString());

            String run_id = String.valueOf(System.currentTimeMillis());
            jobParametersBuilder = new JobParametersBuilder();
            String applNo = taskPrmyInfo.getApplNo();
            JobParameters jobParameters = jobParametersBuilder.addString("applNo", applNo).addLong("size", 100L)
                    .addString("run_id", run_id).addLong("taskSeq", taskPrmyInfo.getTaskSeq()).toJobParameters();

            //改变批作业状态为执行状态
            taskPrmyInfo.setStatus(STATE_K);
            taskPrmyInfo.setStartTime((DateFormatUtils.format(new Date(), PATTERN)));
            if(!taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskPrmyInfo)){
                logger.error("表:" + QueueType.EV_CUST_SET_TASK_QUEUE.toString() +  " 更新失败。");
                continue;
            }

            logger.info(applNo + "开户job开始");
            try {
                JobExecution jobExecution = null;

                //异步启动，否则同步启动
                if (StringUtils.equals(procSwitch, ASY)) {

                    jobExecution = batchJobAsynLauncher.run(ipsnListOpenCustJob, jobParameters);
                    taskPrmyInfo.setJobInstanceId(jobExecution.getId());//赋值批作业执行ID
                } else {
                    jobExecution = batchJobSynLauncher.run(ipsnListOpenCustJob, jobParameters);
                }
                logger.info("applNo=[{}], jexecution_id=[{}],批作业已启动", applNo, jobExecution.getId());
                taskPrmyDao.updateExcuIdByTaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskPrmyInfo.getTaskSeq(), jobExecution.getId());

            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                    | JobParametersInvalidException e) {

                String regId = CommonUtils.uuid();
                batchJobMonitorService.launcherFailure(regId, applNo, "ipsnListOpenCustJob", e.getMessage());
                taskPrmyInfo.setStatus(STATE_E);
                taskPrmyInfo.setRemark(e.toString());
                taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), PATTERN)));
                taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
                //改变批作业状态为问题状态
                taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskPrmyInfo);
                logger.info(e.getMessage(), e);

            }
        }
        logger.info("开户批作业结束");
    }

}
