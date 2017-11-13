package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.GrpListMioTask;
import com.newcore.orbps.models.pcms.bo.PolCode;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.pcms.api.GrpMioNtTaskService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务落地,只落地转保费产生的Mio_log流水记录
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class MioLogLandingTasklet implements Tasklet {

    private int maxAttempts; //调用服务尝试次数

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Autowired
    TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

    @Resource(name = "grpMioNtTaskServiceClient")
    GrpMioNtTaskService grpMioNtTaskService;

    //被保人落地通知失败标记
    private static final String FAILURE_MIO_LOG_LAND_NOTIFY_FLAG = "2";

    //被保人落地通知成功
    private static final String SUCESS_MIO_LOG_LAND_NOTIFY_FLAG = "1";
    //被保人落地成功
    private static final String SUCESS_MIO_LOG_LAND_FLAG = "3";

    @Transaction
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Long taskSeq = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("taskSeq");
        Long batNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("batNo");
        Long plnBatNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("plnBatNo");
        //获取监控表数据
        TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByTaskSeq(taskSeq);
        //已落地成功、已经告知成功则退出执行
        if (StringUtils.equals(SUCESS_MIO_LOG_LAND_NOTIFY_FLAG, taskCntrDataLandingQueue.getFinLandFlag())
                || StringUtils.equals(SUCESS_MIO_LOG_LAND_FLAG, taskCntrDataLandingQueue.getFinLandFlag())) {
            return RepeatStatus.FINISHED;
        }
        String applNo = taskCntrDataLandingQueue.getApplNo();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("applNo", applNo);
        GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
        if (null == grpInsurAppl) {
            throw new BusinessException(new Throwable("查询保单基本信息失败：GrpInsurAppl"));
        }
//        //非银行转账，由保单辅助进行财务落地，直接返回成功
//        if (!ProcMioInfoUtils.isBankTrans(grpInsurAppl)) {
//            return RepeatStatus.FINISHED;
//        }

        //被保人已经告知成功，则跳过
//        if (!StringUtils.equals(FAILURE_MIO_LOG_LAND_NOTIFY_FLAG, taskCntrDataLandingQueue.getFinLandFlag())
//                || !StringUtils.equals(UN_MIO_LOG_LAND_NOTIFY_FLAG, taskCntrDataLandingQueue.getFinLandFlag())) {
//            return RepeatStatus.FINISHED;
//        }

        //组织发送报文
        List<PolCode> polCodeList = new ArrayList<>();
        for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
            PolCode polCode = new PolCode();
            polCode.setPolCode(policy.getPolCode());
            polCodeList.add(polCode);
        }

        //实收付流水任务 BO
        GrpListMioTask grpListMioTask = new GrpListMioTask();
        grpListMioTask.setApplNo(applNo);
        grpListMioTask.setBatNo(batNo);
        grpListMioTask.setMgrBranchNo(grpInsurAppl.getMgrBranchNo()); 
        grpListMioTask.setMioType("1");
        grpListMioTask.setPolCodeList(polCodeList);
        grpListMioTask.setPlnBatNo(plnBatNo);
        //调用财务落地告知服务
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        RetInfo retInfo = new RetInfo();
        retryPolicy.setMaxAttempts(maxAttempts); //设置重试次数
        retryTemplate.setRetryPolicy(retryPolicy);
        retInfo = retryTemplate.execute(
                new RetryCallback<RetInfo, Exception>() {
                    public RetInfo doWithRetry(RetryContext context) throws Exception {
                        //消息头设置
                        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
                        HeaderInfoHolder.setOutboundHeader(headerInfo);
                        return grpMioNtTaskService.grpListMioTaskCreate(grpListMioTask);
                    }
                }
        );

        if (StringUtils.equals(retInfo.getRetCode(), "1")) {
            //如果调用成功
            taskCntrDataLandingQueue.setFinLandFlag(SUCESS_MIO_LOG_LAND_NOTIFY_FLAG);
            taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);

            return RepeatStatus.FINISHED;
        } else {
            //如果落地失败
            taskCntrDataLandingQueue.setFinLandFlag(FAILURE_MIO_LOG_LAND_NOTIFY_FLAG);
            taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);
            return null;
        }

    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
