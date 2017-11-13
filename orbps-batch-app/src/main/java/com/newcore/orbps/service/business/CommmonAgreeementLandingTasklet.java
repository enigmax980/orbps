package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.pcms.api.SetComAgreeService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 共保协议落地
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class CommmonAgreeementLandingTasklet implements Tasklet {

	private int maxAttempts;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Resource(name = "setComAgreeServiceClient")
	SetComAgreeService setComAgreeService;  //共保协议落地服务

	//共保协议落地完成标记
	private static final String COMPELETE_INSUR_APPL_LAND_FLAG = "3";

	//共保协议落地失败标记
	private static final String FAILURE_INSUR_APPL_LAND_FLAG = "4";


	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CommmonAgreeementLandingTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Long taskSeq = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("taskSeq");
		String isCommonAgreement = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString("isCommonAgreement");

		logger.info("共保协议落地-isCommonAgreement：" + isCommonAgreement);
		//如果非共保，则跳过此阶段
		if (StringUtils.equals(YES_NO_FLAG.NO.getKey(), isCommonAgreement)) {
			return RepeatStatus.FINISHED;
		}

		//获取监控表数据
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByTaskSeq(taskSeq);
		//如果共保协议已经落地，则跳过此阶段
		if (StringUtils.equals(COMPELETE_INSUR_APPL_LAND_FLAG, taskCntrDataLandingQueue.getCommonAgreementLandFlag())) {
			return RepeatStatus.FINISHED;
		}

		String applNo = taskCntrDataLandingQueue.getApplNo();
		RetInfo retInfo = new RetInfo();//落地服务返回值
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("查询保单基本信息失败：GrpInsurAppl"));
		}
		//获取共保协议
		Map<String, Object> comMap = new HashMap<>();
		comMap.put("agreementNo", grpInsurAppl.getAgreementNo());
		CommonAgreement commonAgreement = (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, comMap);
		if (null == commonAgreement) {
			throw new BusinessException(new Throwable("查询共保协议信息失败：commonAgreement"));
		}
		//组织共保协议落地报文
		com.newcore.orbps.models.pcms.bo.CommonAgreement commonAgreementVo = new com.newcore.orbps.models.pcms.bo.CommonAgreement();
		commonAgreementVo.setApplNo(applNo);
		commonAgreementVo.transCommonAgreementBoToVo(commonAgreement);

		//共保协议落地
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts); //设置重试次数
		retryTemplate.setRetryPolicy(retryPolicy);
		retInfo = retryTemplate.execute(
				new RetryCallback<RetInfo, Exception>() {
					public RetInfo doWithRetry(RetryContext context) throws Exception {
						//调用共保落地
						CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
						HeaderInfoHolder.setOutboundHeader(headerInfo);
						return setComAgreeService.addCommonAgreement(commonAgreementVo);
					}
				}
				);
		if (StringUtils.equals(retInfo.getRetCode(), "1")) {
			//如果调用成功
			taskCntrDataLandingQueue.setCommonAgreementLandFlag(COMPELETE_INSUR_APPL_LAND_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);

			return RepeatStatus.FINISHED;
		} else {
			//如果落地失败
			taskCntrDataLandingQueue.setCommonAgreementLandFlag(FAILURE_INSUR_APPL_LAND_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);
			return RepeatStatus.FINISHED;
		}

	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
}
