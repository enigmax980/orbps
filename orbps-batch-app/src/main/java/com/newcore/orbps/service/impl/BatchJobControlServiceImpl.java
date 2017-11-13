package com.newcore.orbps.service.impl;

import com.halo.core.batch.service.impl.batch.BatchJobLauncher;
import com.halo.core.batch.service.impl.schedule.SchedulerJobLauncher;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.service.api.BatchJobControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台批作业控制服务
 * 1.批作业全量停、重启、是否停止完毕
 * Created by liushuaifeng on 2017/3/20 0020.
 */
@Service("batchJobControlService")
public class BatchJobControlServiceImpl implements BatchJobControlService {

    //springbatch 停止的作业
    private static String[] arrTaskTableStop = { QueueType.LIST_IMPORT_TASK_QUEUE.toString(), QueueType.EV_CUST_SET_TASK_QUEUE.toString(), QueueType.CNTR_INFORCE_TASK_QUEUE.toString(),  QueueType.TASK_CNTR_DATA_LANDING_QUEUE.toString(), QueueType.PROC_EARNEST_PAY_TASK.toString()};
    //springbatch 启动的作业
    private static String[] arrTaskTableStart = { QueueType.EV_CUST_SET_TASK_QUEUE.toString(), QueueType.CNTR_INFORCE_TASK_QUEUE.toString(),  QueueType.TASK_CNTR_DATA_LANDING_QUEUE.toString(), QueueType.PROC_EARNEST_PAY_TASK.toString()};
    //定时任务表
    private static String[] arrScheduleTaskTable = {QueueType.PROC_PLNMIO_TASK_QUEUE.toString(), "MONEY_IN_CHECK_TASK_QUEUE", };

    private static Logger logger = LoggerFactory.getLogger(BatchJobControlServiceImpl.class);

    @Autowired
    SimpleJobOperator batchJobManager;
    @Autowired
    JdbcOperations jdbcOperations;
    @Autowired
    BatchJobLauncher batchJobLauncher;
    @Autowired
    TaskPrmyDao taskPrmyDao;
    @Autowired
    SchedulerJobLauncher schedulerJobLauncher;

    @Override
    public RetInfo stopMultiBatchJob() {
        RetInfo retInfo = new RetInfo();
        //暂停所有定时任务
        schedulerJobLauncher.pauseAllJobs();
        //暂停所有批作业
        for (String taskTable : arrTaskTableStop) {
            List<Long> jobExcutionIdList = taskPrmyDao.queryJobExcutionIdList(taskTable);
            if (jobExcutionIdList.isEmpty()) {
                continue;
            }

            List<Long> runningExecList = taskPrmyDao.selRuningExexuions(jobExcutionIdList); //对执行ID进行过滤
            logger.info("表名：{}， 待停批作业数量：{}", taskTable, runningExecList.size());
            if (runningExecList.isEmpty()) {
                continue;
            }

            for (Long jobExcutionId : runningExecList) {
                logger.info("批作业[id={}]开始停止...", jobExcutionId);
                try {
                    batchJobManager.stop(jobExcutionId);
                } catch (JobExecutionNotRunningException e) {
                    logger.info("批作业任务表[{}], jobExcutionId=[{}],批作业非执行态，无需停止", taskTable, jobExcutionId);
                    logger.info(e.getMessage(), e);
                } catch (NoSuchJobExecutionException e) {
                    logger.info("批作业任务表[{}], jobExcutionId=[{}],无此批作业，无需停止", taskTable, jobExcutionId);
                    logger.info(e.getMessage(), e);
                }
            }
        }

        retInfo.setRetCode("1");
        return retInfo;
    }

    @Override
    public RetInfo stopImportIpsnListBatchJob(String applNo) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        RetInfo retInfo = new RetInfo();

        //暂停清单导入批作业
        List<Long> jobExcutionIdList = taskPrmyDao.queryJobExcutionIdListByApplno(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo);
        for (Long jobExcutionId : jobExcutionIdList) {
            logger.info("批作业[id={}]开始停止...", jobExcutionId);
            batchJobManager.stop(jobExcutionId);
            taskPrmyDao.updateStatusByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo);
        }
        retInfo.setRetCode("1");
        return retInfo;
    }

    @Override
    public void restartStoppedBatJob() throws JobParametersInvalidException {
        //重启所有定时任务
        schedulerJobLauncher.resumeAllJobs();
        //开始所有批作业
        for (String taskTable : arrTaskTableStart) {

            List<Long> jobExcutionIdList = taskPrmyDao.queryJobExcutionIdList(taskTable);
            if (jobExcutionIdList.isEmpty()) {
                continue;
            }

            List<Long> runningExecList = taskPrmyDao.selStoppedExexuions(jobExcutionIdList); //对执行ID进行过滤
            logger.info("表名：{}， 待重启批作业数量：{}", taskTable, runningExecList.size());
            if (runningExecList.isEmpty()) {
                continue;
            }

            for (Long jobExcutionId : runningExecList) {

                try {
                    logger.info("批作业[id={}]开始重启...", jobExcutionId);
                    batchJobManager.restart(jobExcutionId);
                } catch (JobRestartException | JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException e) {
                    logger.info("批作业[id={}]重启有问题", jobExcutionId);
                    logger.info(e.getMessage(), e);
                }

            }
        }
    }

    @Override
    public RetInfo getIsBatchStopped() throws NoSuchJobException {
        RetInfo retInfo = new RetInfo();
        long runningExecSum = 0L;
        StringBuilder msgBuilder = new StringBuilder();
        StringBuilder sumMsgBuilder = new StringBuilder();

        for (String taskTable : arrTaskTableStop) {

            List<Long> jobExcutionIdList = taskPrmyDao.queryJobExcutionIdList(taskTable);
            if (jobExcutionIdList.isEmpty()) {
                continue;
            }
            long runningCount = taskPrmyDao.selStoppingExexuions(jobExcutionIdList); //对执行ID进行过滤
            runningExecSum += runningCount;
            logger.info("表名：{}， 正在停止的作业数量：{}", taskTable, runningCount);
            msgBuilder.append("表名：" + taskTable +  ", 正在停止批作业的数量：" + runningCount + "; ");
        }
        //获取正在执行的定时任务的数量
        for (String taskTable : arrScheduleTaskTable){
            long scheuletaskCount = taskPrmyDao.selRunningScheduleTask(taskTable);
            logger.info("表名：{}， 正在停止的批作业数量：{}", taskTable, scheuletaskCount);
            runningExecSum += scheuletaskCount;
            msgBuilder.append("表名：" + taskTable +  ", 正在停止定时任务的数量：" + scheuletaskCount + "; ");
        }
        sumMsgBuilder.append("正在停止作业总的数量: "+ runningExecSum + "; " + msgBuilder.toString());
        logger.info("正在停止作业总的数量:[{}]]", sumMsgBuilder.toString());
        if (runningExecSum == 0) {
            retInfo.setErrMsg("全部作业停止完毕。");
        } else {
            retInfo.setErrMsg(sumMsgBuilder.toString());
        }
        retInfo.setRetCode("1");
        return retInfo;
    }


}
