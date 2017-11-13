package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.GcLppPremRateBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.TuPolResult;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.web.vo.sendgrpinsurappl.GrpInsurApplSendVo;
import com.newcore.orbps.models.web.vo.sendgrpinsurappl.HealthInsurInfoVo;
import com.newcore.orbps.models.web.vo.sendgrpinsurappl.PlnmioRecVo;
import com.newcore.orbps.service.pcms.api.MissionLandingService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.orbpsutils.math.DateUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import javax.annotation.Resource;
import java.util.*;

/**
 * 保单基本信息落地
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class CntrBasicDataLandingTasklet implements Tasklet {

	private int maxAttempts;

	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Resource(name = "missionLandingServiceClient")
	MissionLandingService missionLandingService; //保单落地服务

	//保单辅助需要的默认值
	private static final String CLDC_TYPE = "M";
	//转保费记录
	private static final String MIO_ITEM_CODE_PS = "PS";

	private static final String MTN_ITEM_CODE_29 = "29";
	//保单基本信息落地完成标记
	private static final String COMPELETE_INSUR_APPL_LAND_FLAG = "3";

	//保单基本信息落地失败标记
	private static final String FAILURE_INSUR_APPL_LAND_FLAG = "4";

	private static final Long IOBJ_NO = 1L;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CntrBasicDataLandingTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long taskSeq = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("taskSeq");

		logger.info("保单帽子落地开始，" + "taskSeq = " + taskSeq);

		String applNo;
		GrpInsurApplSendVo grpInsurApplSendVo = new GrpInsurApplSendVo();

		//获取监控表数据
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByTaskSeq(taskSeq);
		if (StringUtils.equals(COMPELETE_INSUR_APPL_LAND_FLAG, taskCntrDataLandingQueue.getInsurApplLandFlag())) {
			return RepeatStatus.FINISHED;
		}

		applNo = taskCntrDataLandingQueue.getApplNo();

		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("查询保单基本信息失败：GrpInsurAppl"));
		}
		//bo to vo
		grpInsurApplSendVo.transGrpInsurBoToVo(grpInsurAppl);
		//健康险赋值
		HealthInsurInfoVo healthInsurInfoVo=null;
		if(null!=grpInsurAppl.getHealthInsurInfo()){
			healthInsurInfoVo =new HealthInsurInfoVo();
			healthInsurInfoVo.setComInsrPrem(null==grpInsurAppl.getHealthInsurInfo().getComInsrPrem()?0.00:grpInsurAppl.getHealthInsurInfo().getComInsrPrem());
			healthInsurInfoVo.setComInsurAmntType(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType());
			healthInsurInfoVo.setComInsurAmntUse(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse());
			healthInsurInfoVo.setFloatInverse(null == grpInsurAppl.getHealthInsurInfo().getFloatInverse()?0.00:grpInsurAppl.getHealthInsurInfo().getFloatInverse());
			healthInsurInfoVo.setIpsnFloatAmnt(null == grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()?0.00:grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt());
			healthInsurInfoVo.setSumFixedAmnt(null == grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt()?0.00:grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt());
			//公共保额使用形式（comInsurAmntType）值为0（浮动公共保额），需计算需要对合计浮动保额赋值
			//● 2 包括连带被保人 合计浮动保额 = 人均保额（ipsnFloatAmnt） * 有效被保人数（GrpInsured.procStat=E）
		    //● 1 不包括连带被保险人	合计浮动保额 = 人均保额 * （有效被保人数 - 连带有效被保人数ipsnType=A：副被保险人）
			if(org.apache.commons.lang3.StringUtils.equals("0", grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())){
				//有效被保人数procStat=E
				long countSum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E")),GrpInsured.class);
				//连带有效被保人数ipsnType=A 
				long ipsnTypeCountSum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("ipsnType").is("A")),GrpInsured.class);
				//人均保额
				Double ipsnFloatAmnt = grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt();
				//公共总保额
				Double sumFloatAmnt=null;
				if(org.apache.commons.lang3.StringUtils.equals("1",grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
					sumFloatAmnt=ipsnFloatAmnt * (countSum-ipsnTypeCountSum);
				}
				else if(org.apache.commons.lang3.StringUtils.equals("2",grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
					sumFloatAmnt=ipsnFloatAmnt * countSum;
				}
				sumFloatAmnt= null == sumFloatAmnt ? null : ProcMioInfoUtils.keepPrecision(sumFloatAmnt, 2);
				healthInsurInfoVo.setSumFloatAmnt(sumFloatAmnt);
			}
		}
		grpInsurApplSendVo.setHealthInsurInfo(healthInsurInfoVo);

		//理赔录入类型-写死
		grpInsurApplSendVo.getApplState().setClDcType(CLDC_TYPE);
		if (null != grpInsurAppl.getConstructInsurInfo()) {
			grpInsurApplSendVo.getConstructInsurInfo().setIobjNo(IOBJ_NO);
		}
		//清汇多期暂交数据组织
		if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())
				&& StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getPaymentInfo().getIsMultiPay())) {
			List<GcLppPremRateBo> gcLppPremRateBoList = getGcLppPremRateBo(grpInsurAppl);
			grpInsurApplSendVo.setGcLppPremRateBoList(gcLppPremRateBoList);
		}
		//组织核保结论
		Map<String, Object> maps = new HashMap<>();
		maps.put("businessKey", applNo);
		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, maps);
		if (null == applUdwResult) {
			throw new BusinessException(new Throwable("获取核保结论失败：ApplUdwResult"));
		}
		List<UdwPolResult> udwPolResultList = applUdwResult.getUdwPolResults();
		List<Policy> policyList = grpInsurApplSendVo.getApplState().getPolicyList();
		for (UdwPolResult udwPolResult : udwPolResultList) {
			for (Policy policy : policyList) {
				if (udwPolResult.getPolCode().equals(policy.getPolCode())) {
					TuPolResult tuPolResult = new TuPolResult();
					tuPolResult.setAddFeeAmnt(udwPolResult.getAddFeeAmnt());
					tuPolResult.setAddFeeYear(udwPolResult.getAddFeeYear());
					tuPolResult.setUdwOpinion(udwPolResult.getUdwOpinion());
					tuPolResult.setUdwResult(udwPolResult.getUdwResult());
					policy.setTuPolResult(tuPolResult);
				}
			}
		}

		//基金险
		if(grpInsurAppl.getFundInsurInfo()!=null){
			//帽子落地中新增管理费比例、个人与公共账户总保费以及总保额、收到保费时间、管理费总金额等字段的落地。
			grpInsurApplSendVo.getFundInsurInfo().setAdminFeeCopuType(grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType()); //管理费计提方式
			grpInsurApplSendVo.getFundInsurInfo().setAdminFeePct(grpInsurAppl.getFundInsurInfo().getAdminFeePct());			//管理费比例
			grpInsurApplSendVo.getFundInsurInfo().setIpsnFundPremium(grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()); //个人账户缴费金额
			grpInsurApplSendVo.getFundInsurInfo().setIpsnFundAmnt(grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt()); 		//计入个人账户金额
			grpInsurApplSendVo.getFundInsurInfo().setSumFundPremium(grpInsurAppl.getFundInsurInfo().getSumFundPremium()); 	//公共账户缴费金额
			grpInsurApplSendVo.getFundInsurInfo().setSumFundAmnt(grpInsurAppl.getFundInsurInfo().getSumFundAmnt()); 		//计入公共账户金额
			if(grpInsurAppl.getFundInsurInfo().getPreMioDate() != null){
				grpInsurApplSendVo.getFundInsurInfo().setPreMioDate(grpInsurAppl.getFundInsurInfo().getPreMioDate()); 			//基金险收到保费时间
			}
			grpInsurApplSendVo.getFundInsurInfo().setAccBalance(grpInsurAppl.getFundInsurInfo().getAccBalance());			//账户余额
			grpInsurApplSendVo.getFundInsurInfo().setAccAdminBalance(grpInsurAppl.getFundInsurInfo().getAccAdminBalance()); //账户管理费金额
			grpInsurApplSendVo.getFundInsurInfo().setAccSumAdminBalance(grpInsurAppl.getFundInsurInfo().getAccSumAdminBalance()); //首期管理费总金额
		}

		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		RetInfo retInfo = new RetInfo();

		retryPolicy.setMaxAttempts(maxAttempts); //设置重试次数
        retryTemplate.setRetryPolicy(retryPolicy);
		retInfo = retryTemplate.execute(
				new RetryCallback<RetInfo, Exception>() {
					public RetInfo doWithRetry(RetryContext context) throws Exception {
						//调用保单落地服务
						CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
						HeaderInfoHolder.setOutboundHeader(headerInfo);
						return missionLandingService.missionLandingCreate(grpInsurApplSendVo);
					}
				}
				);

		if (StringUtils.equals(retInfo.getRetCode(), "1")) {
			//如果调用成功
			taskCntrDataLandingQueue.setInsurApplLandFlag(COMPELETE_INSUR_APPL_LAND_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);

			return RepeatStatus.FINISHED;
		} else {
			//如果落地失败
			taskCntrDataLandingQueue.setInsurApplLandFlag(FAILURE_INSUR_APPL_LAND_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);
			throw new BusinessException(new Throwable("帽子落地失败:"+retInfo.getErrMsg()));
		}

	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}



	/**
	 * 生成多期暂交数据
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	public List<GcLppPremRateBo> getGcLppPremRateBo(GrpInsurAppl grpInsurAppl) {
		List<GcLppPremRateBo> gcLppPremRateBoList = new ArrayList<>();
		//根据多期暂缴年数 赋值
		for (int i = 0; i < grpInsurAppl.getPaymentInfo().getMutipayTimes(); i++) {
			//团体保单一次性缴费多期缴费标准
			GcLppPremRateBo gcLppPremRateBo = new GcLppPremRateBo();

			Date inForcedate = DateUtils.computeDate(grpInsurAppl.getInForceDate(), i, "Y");
			//获得指定保单生效日期
			gcLppPremRateBo.setInForceDate(inForcedate);
			//取保险期间
			gcLppPremRateBo.setInsurDur(grpInsurAppl.getApplState().getInsurDur());
			//取保险期间单位
			gcLppPremRateBo.setInsurDurUnit(grpInsurAppl.getApplState().getInsurDurUnit());
			//取第一险种或主险
			gcLppPremRateBo.setPolCode(grpInsurAppl.getFirPolCode());
			//取总保费
			gcLppPremRateBo.setPremium(grpInsurAppl.getApplState().getSumPremium());
			//放入list集合
			gcLppPremRateBoList.add(gcLppPremRateBo);
		}
		return gcLppPremRateBoList;
	}


	public List<PlnmioRecVo> getPlnmioRecList(List<PlnmioRec> plnmioRecs) {
		List<PlnmioRecVo> plnmioRecVoList = new ArrayList<>();
		logger.info("保单基本信息落地，生成分期应收……");
		//遍历应收
		for (PlnmioRec plnmioRec : plnmioRecs) {
			//筛选分期应收数据
			if (StringUtils.equals(MTN_ITEM_CODE_29, plnmioRec.getMtnItemCode())
					&& StringUtils.equals(MIO_ITEM_CODE_PS, plnmioRec.getMioItemCode())) {
				PlnmioRecVo plnmioRecVo = new PlnmioRecVo();
				plnmioRecVo.transBoToVo(plnmioRec);
				plnmioRecVoList.add(plnmioRecVo);
			}
		}
		return plnmioRecVoList;

	}


}
