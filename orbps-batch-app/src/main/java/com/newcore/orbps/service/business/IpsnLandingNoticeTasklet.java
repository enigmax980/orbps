package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.mongodb.BasicDBList;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskBo;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskRetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.pcms.api.GrpListCgTaskService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.*;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import org.slf4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.slf4j.LoggerFactory.*;

/**
 * 被保人落地告知
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class IpsnLandingNoticeTasklet implements Tasklet {

	private int maxAttempts; //调用服务尝试次数

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	ProcMioInfoDao procMioInfoDao;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Resource(name = "restfulgrpListCgTaskServiceClient")
	GrpListCgTaskService grpListCgTaskService;

	//被保人落地未通知
	private static final String UN_IPSN_LAND_NOTIFY_FLAG = "0";

	//被保人落地通知失败标记
	private static final String FAILURE_IPSN_LAND_NOTIFY_FLAG = "2";

	//被保人落地通知成功
	private static final String SUCESS_IPSN_LAND_NOTIFY_FLAG = "1";
	//被保人落地成功
	private static final String SUCESS_IPSN_LAND_FLAG = "3";

	//被保人落地批次号，
	private static final Long INSUR_APPL_LAND_BATCH_NO = 0L;

	private static final Integer PRECISION = 2;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = getLogger(IpsnLandingNoticeTasklet.class);

	@SuppressWarnings({ "static-access", "unused" })
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Long taskSeq = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("taskSeq");
		String lstProcType = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString("lstProcType");

		//如果非普通清单，则无需落地，跳过该step
		if (!StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), lstProcType)) {
			return RepeatStatus.FINISHED;
		}

		//获取监控表数据
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByTaskSeq(taskSeq);
		if (StringUtils.equals(SUCESS_IPSN_LAND_NOTIFY_FLAG, taskCntrDataLandingQueue.getIpsnLandFlag())
				|| StringUtils.equals(SUCESS_IPSN_LAND_FLAG, taskCntrDataLandingQueue.getIpsnLandFlag())) {
			return RepeatStatus.FINISHED;
		}

		//此处重新更新一次被保人表，避免档案清单补导入数据缺失
		GrpInsurAppl grpInsurAppl = procMioInfoDao.getGrpInsurAppl(taskCntrDataLandingQueue.getApplNo());
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("根据投保单号查询保单基本信息失败."));
		}
		BasicDBList dbd = new BasicDBList();
		dbd.add("N");
		dbd.add("E");
		Query queryGrpInsured = new Query();
		queryGrpInsured.addCriteria(Criteria.where("applNo").is(grpInsurAppl.getApplNo()));
		queryGrpInsured.addCriteria(Criteria.where("procStat").in(dbd));
		Update updateGrpInsured = new Update();
		updateGrpInsured.set("ipsnInForceDate", grpInsurAppl.getInForceDate());
		updateGrpInsured.set("procStat", "E");
		updateGrpInsured.set("cgNo", grpInsurAppl.getCgNo());
		updateGrpInsured.set("batNo", "0");
		mongoTemplate.updateMulti(queryGrpInsured, updateGrpInsured, GrpInsured.class);

		//对作废的被保人操作，
		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("applNo", grpInsurAppl.getApplNo());
		updateMap.put("procStat", "O");
		Update updateInvalidGrpInsured = new Update();
		updateInvalidGrpInsured.set("batNo", "-1");
		mongoBaseDao.updateAll(GrpInsured.class, updateMap, updateInvalidGrpInsured);

		//组织发送报文
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", taskCntrDataLandingQueue.getApplNo());
		queryMap.put("procStat", "E");
		Long ipsnLandNum = mongoBaseDao.count(GrpInsured.class, queryMap);
		if (null == ipsnLandNum) {
			throw new BusinessException(new Throwable("查询被保人数量失败：GrpInsured"));
		}

		//计算被保人总保额
		double sumFaceAmnt = getGrpInsuredSumFaceAmnt(taskCntrDataLandingQueue.getApplNo());
		//根据投保单号、收费方式计算实际保费
		double sumPremium = Double.valueOf(taskCntrDataLandingQueue.getSumPremium());

		//组织被保人落地告知Vo
		GrpListCgTaskBo grpListCgTaskVo = new GrpListCgTaskBo();
		grpListCgTaskVo.setCgNo(grpInsurAppl.getCgNo());                            // 组合保单号
		grpListCgTaskVo.setBatNo(INSUR_APPL_LAND_BATCH_NO);        // 批次号
		grpListCgTaskVo.setBatchIpsnNum(ipsnLandNum);        // 批次总人数
		grpListCgTaskVo.setBatchSumFaceAmnt(BigDecimalUtils.keepPrecision(sumFaceAmnt, PRECISION).doubleValue());            // 批次总保额
		grpListCgTaskVo.setBatchSumPremium(BigDecimalUtils.keepPrecision(sumPremium, PRECISION).doubleValue());                // 批次总保费
		grpListCgTaskVo.setApplNo(grpInsurAppl.getApplNo());                            // 投保单号
		grpListCgTaskVo.setMgBranch(grpInsurAppl.getMgrBranchNo());                    // 管理机构号
		grpListCgTaskVo.setPolCode(grpInsurAppl.getFirPolCode());



		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts); //设置重试次数
		retryTemplate.setRetryPolicy(retryPolicy);
		GrpListCgTaskRetInfo grpListCgTaskRetInfo = retryTemplate.execute(
				new RetryCallback<GrpListCgTaskRetInfo, Exception>() {
					public GrpListCgTaskRetInfo doWithRetry(RetryContext context) throws Exception {
						//调用保单落地服务
						CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
						HeaderInfoHolder.setOutboundHeader(headerInfo);
						return grpListCgTaskService.grpListCgTaskService(grpListCgTaskVo);
					}
				}
				);
		if (StringUtils.equals(grpListCgTaskRetInfo.getRetCode(), "1")) {
			//如果调用成功
			taskCntrDataLandingQueue.setIpsnLandFlag(SUCESS_IPSN_LAND_NOTIFY_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);

			return RepeatStatus.FINISHED;
		} else {
			//如果落地失败
			taskCntrDataLandingQueue.setIpsnLandFlag(FAILURE_IPSN_LAND_NOTIFY_FLAG);
			taskCntrDataLandingQueueDao.updateByTaskSeq(taskSeq, taskCntrDataLandingQueue);
			return null;
		}

	}

	/**
	 * 根据投保单获取总保额--从被保人累计中
	 * @param applNo
	 * @return
	 */
	public double getGrpInsuredSumFaceAmnt(String applNo) {

		double sumFaceAmnt = 0.0;

		Criteria criteria = Criteria.where("applNo").is(applNo).and("procStat").is("E");
		// 保额保费校验
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.unwind("subStateList"),
				Aggregation.group("$subStateList.polCode").sum("$subStateList.faceAmnt").as("sumPolFaceAmnt"));

		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();

		for (JSONObject jsonObject : jsonObjects) {

			sumFaceAmnt += jsonObject.getDouble("sumPolFaceAmnt");
		}

		return sumFaceAmnt;

	}

	/**
	 * 从应收中获取总保费
	 *
	 * @param cntrNo
	 * @return
	 */
	public double getSumPremiumByApplNo(String cntrNo) {

		Map<String, Object> criteriaMap = new HashMap<>();
		criteriaMap.put("cntrNo", cntrNo);
		criteriaMap.put("mioItemCode", "FA");
		criteriaMap.put("mioClass", -1);
		criteriaMap.put("mioType", "S");

		List<MioLog> mioLogs = mongoBaseDao.find(MioLog.class, criteriaMap);
		if (null == mioLogs) {
			throw new BusinessException(new Throwable("查询流水表失败"));
		}

		return mioLogs.get(0).getAmnt().doubleValue();
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}


}
