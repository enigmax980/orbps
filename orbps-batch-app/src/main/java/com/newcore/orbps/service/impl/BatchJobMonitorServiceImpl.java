package com.newcore.orbps.service.impl;

import com.halo.core.common.util.CommonUtils;
import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.models.taskprmy.PBatchErrLogNewPO;
import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionDetailPO;
import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionPO;
import com.newcore.orbps.models.taskprmy.PBatchTaskRegPO;
import com.newcore.orbps.service.api.BatchJobMonitorService;
import com.newcore.orbpsutils.dao.api.PBatchErrLogNewDao;
import com.newcore.orbpsutils.dao.api.PBatchTaskExecutionDao;
import com.newcore.orbpsutils.dao.api.PBatchTaskExecutionDetailDao;
import com.newcore.orbpsutils.dao.api.PBatchTaskRegDao;
import com.newcore.supports.dicts.BUSINESS_TYPE;
import com.newcore.supports.dicts.DATA_DEAL_STATUS;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liushuaifeng on 2017/3/8 0008.
 */
@Service("batchJobMonitorService")
public class BatchJobMonitorServiceImpl implements BatchJobMonitorService {

    @Autowired
    PBatchTaskRegDao pBatchTaskRegDao;

    @Autowired
    PBatchTaskExecutionDao pBatchTaskExecutionDao;

    @Autowired
    PBatchTaskExecutionDetailDao pBatchTaskExecutionDetailDao;

    @Autowired
    PBatchErrLogNewDao pBatchErrLogNewDao;

    @Override
    public void regJobMonitor(String taskId, String batTxNo) {
        //插入批作业注册表
        PBatchTaskRegPO pBatchTaskRegPO = new PBatchTaskRegPO();
        pBatchTaskRegPO.setTaskId(taskId);
        pBatchTaskRegPO.setBatTxNo(batTxNo);
        pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PENDING_TREATMENT.getKey());
        pBatchTaskRegDao.addBatchTaskReg(pBatchTaskRegPO);
    }

    //@Transaction
    @Override
    public void beginExcution(JobExecution jobExecution) {
        //批作业任务注册表
        String taskId = jobExecution.getExecutionContext().getString("regId");
        PBatchTaskRegPO pBatchTaskRegPO = new PBatchTaskRegPO();
        pBatchTaskRegPO.setTaskId(taskId);
        pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.IN_TREATMENT.getKey());
        pBatchTaskRegDao.updateBatchTaskReg(pBatchTaskRegPO);

        //更新批作业执行表
        PBatchTaskExecutionPO pBatchTaskExecutionPO = new PBatchTaskExecutionPO();
        pBatchTaskExecutionPO.setTaskExecutionId(CommonUtils.uuid());
        pBatchTaskExecutionPO.setTaskId(taskId);
        pBatchTaskExecutionDao.addBatchTaskExecution(pBatchTaskExecutionPO);

        //批作业执行明细表
        PBatchTaskExecutionDetailPO pBatchTaskExecutionDetailPO = new PBatchTaskExecutionDetailPO();
        pBatchTaskExecutionDetailPO.setPkId(CommonUtils.uuid());
        pBatchTaskExecutionDetailPO.setTaskExecutionId(pBatchTaskExecutionPO.getTaskExecutionId());
        pBatchTaskExecutionDetailPO.setJobExecutionId(jobExecution.getId());
        pBatchTaskExecutionDetailPO.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
        pBatchTaskExecutionDetailPO.setJobName(jobExecution.getJobInstance().getJobName());
        pBatchTaskExecutionDetailDao.addBatchTaskExecutionDetail(pBatchTaskExecutionDetailPO);

        //批次号放入批作业上下文
        ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
        jobExecutionContext.putString("taskExecutionId", pBatchTaskExecutionPO.getTaskExecutionId());

    }

    //@Transaction
    @Override
    public void afterExcution(boolean resultFlag, JobExecution jobExecution) {

        //批作业监控
        String taskId = jobExecution.getExecutionContext().getString("regId");
        String taskExecutionId = jobExecution.getExecutionContext().getString("taskExecutionId");
        String applNo = jobExecution.getJobParameters().getString("applNo");


        //批作业注册表
        PBatchTaskRegPO pBatchTaskRegPO = new PBatchTaskRegPO();
        pBatchTaskRegPO.setTaskId(taskId);
        pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PENDING_TREATMENT.getKey());

        //批作业任务执行表
        PBatchTaskExecutionPO pBatchTaskExecutionPO = new PBatchTaskExecutionPO();
        pBatchTaskExecutionPO.setTaskExecutionId(taskExecutionId);

        //批作业错误日志表
        PBatchErrLogNewPO pBatchErrLogNewPO = new PBatchErrLogNewPO();
        pBatchErrLogNewPO.setPkid(CommonUtils.uuid());
        pBatchErrLogNewPO.setTaskId(taskId);
        pBatchErrLogNewPO.setTaskExecutionId(taskExecutionId);
        pBatchErrLogNewPO.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
        pBatchErrLogNewPO.setJobExecutionId(jobExecution.getId());
        pBatchErrLogNewPO.setBusinessType(BUSINESS_TYPE.NC.getKey());
        pBatchErrLogNewPO.setBusinessKey(applNo);//业务主键为投保单号

        if (resultFlag) {
            pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PROCESS_COMPLETE_SUCCESS.getKey());

        } else {
            pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PROCESS_COMPLETE_FAIL.getKey());
            pBatchErrLogNewPO.setErrorCode("###");
            pBatchErrLogNewPO.setErrorInfo(jobExecution.getAllFailureExceptions().toString());
            //插入错误日志表
            pBatchErrLogNewDao.addBatchErrLogNew(pBatchErrLogNewPO);
        }

        //更新注册表
        pBatchTaskRegDao.updateBatchTaskReg(pBatchTaskRegPO);
        //更新批作业任务执行表
        pBatchTaskExecutionDao.updateBatchTaskExecution(pBatchTaskExecutionPO);


    }

    @Transaction
    @Override
    public void launcherFailure(String taskId, String applNo, String jobName, String failReason) {
        //批作业注册表
        PBatchTaskRegPO pBatchTaskRegPO = new PBatchTaskRegPO();
        pBatchTaskRegPO.setTaskId(taskId);
        pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PENDING_TREATMENT.getKey());
        pBatchTaskRegPO.setDataDealStatus(DATA_DEAL_STATUS.PROCESS_COMPLETE_FAIL.getKey());
        pBatchTaskRegDao.updateBatchTaskReg(pBatchTaskRegPO);

        //更新批作业执行表
        PBatchTaskExecutionPO pBatchTaskExecutionPO = new PBatchTaskExecutionPO();
        pBatchTaskExecutionPO.setTaskExecutionId(CommonUtils.uuid());
        pBatchTaskExecutionPO.setTaskId(taskId);
        pBatchTaskExecutionDao.addBatchTaskExecution(pBatchTaskExecutionPO);
        pBatchTaskExecutionDao.updateBatchTaskExecution(pBatchTaskExecutionPO); //更新批作业结束时间

        //批作业执行明细表
        PBatchTaskExecutionDetailPO pBatchTaskExecutionDetailPO = new PBatchTaskExecutionDetailPO();
        pBatchTaskExecutionDetailPO.setPkId(CommonUtils.uuid());
        pBatchTaskExecutionDetailPO.setTaskExecutionId(pBatchTaskExecutionPO.getTaskExecutionId());
        pBatchTaskExecutionDetailPO.setJobInstanceId(0L);
        pBatchTaskExecutionDetailPO.setJobExecutionId(0L);
        pBatchTaskExecutionDetailPO.setJobName(jobName);
        pBatchTaskExecutionDetailDao.addBatchTaskExecutionDetail(pBatchTaskExecutionDetailPO);


        //批作业错误日志表
        PBatchErrLogNewPO pBatchErrLogNewPO = new PBatchErrLogNewPO();
        pBatchErrLogNewPO.setPkid(CommonUtils.uuid());
        pBatchErrLogNewPO.setTaskId(taskId);
        pBatchErrLogNewPO.setTaskExecutionId(pBatchTaskExecutionPO.getTaskExecutionId());
        pBatchErrLogNewPO.setJobExecutionId(0L);
        pBatchErrLogNewPO.setJobInstanceId(0L);
        pBatchErrLogNewPO.setBusinessType(BUSINESS_TYPE.NC.getKey());
        pBatchErrLogNewPO.setBusinessKey(applNo);//业务主键为投保单号
        pBatchErrLogNewPO.setErrorCode("####");
        pBatchErrLogNewPO.setErrorInfo(failReason);
        //插入错误日志表
        pBatchErrLogNewDao.addBatchErrLogNew(pBatchErrLogNewPO);
    }


}
