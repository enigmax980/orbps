package com.newcore.orbps.service.api;

import org.springframework.batch.core.JobExecution;

/**
 * 批作业监控服务
 * Created by liushuaifeng on 2017/3/8 0008.
 */
public interface BatchJobMonitorService {

    /**
     * 注册批作业监控
     * 批作业注册ID规则，表名_对应业务队列表中业务队列中的Task_id
     *
     * @param taskId  批作业注册ID，
     * @param batTxNo 批作业注册代码
     */
    public void regJobMonitor(String taskId, String batTxNo);

    /**
     * 在批作业JobExecutionListener类中beforeJob函数中植入该函数
     * jobExecution中需含有批作业注册表中的taskId
     *
     * @param jobExecution
     */
    public void beginExcution(JobExecution jobExecution);

    /**
     * 在批作业JobExecutionListener类中afterJob函数中植入该函数；
     * jobExecution中需含有批作业注册表中的taskId，批作业任务执行表中的taskExecutionId
     *
     * @param resultFlag   true : 成功， false：失败
     * @param jobExecution
     */
    public void afterExcution(boolean resultFlag, JobExecution jobExecution);

    /**
     * 批作业启动失败的时候调用此函数
     *
     * @param taskId
     * @param applNo
     * @param jobName
     * @param failReason
     */
    public void launcherFailure(String taskId, String applNo, String jobName, String failReason);


}
