package com.newcore.orbps.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.InsurRuleMangerDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.bo.insurrulemanger.InsurRuleManger;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.IpsnState;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.GrpInsuredService;
import com.newcore.orbps.service.business.ValidatorUtils;
import com.newcore.orbpsutils.service.api.PolicyValidationAtomService;
import com.newcore.supports.dicts.APPL_RT_OPSN;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE_01;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_USE;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.IPSN_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PRD_SALES_CHANNEL_01;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 被保人信息Service层接口实现
 * 
 * @author jinmeina
 * @date 2016-10-25 10:25
 */
@Service("grpInsuredService")
public class GrpInsuredServiceImpl implements GrpInsuredService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(GrpInsuredServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	TaskProcessService taskProcessServiceDascClient;
	@Autowired
	InsurRuleMangerDao insurRuleMangerDao;
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	TaskPrmyDao taskPrmyDao;
	/**
	 * 增加被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@Override
	public RetInfo addGrpInsured(GrpInsured grpInsured) {
		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(grpInsured.getApplNo());
		// 根据投保单号和被保险人编号判断数据是否存在
		if (null != queryByApplNoAndIpsnNo(grpInsured)) {
			throw new BusinessException("0003", "被保险人编号重复,添加失败");
		}
		String ipsnName = grpInsured.getIpsnName();
		String ipsnIdType = grpInsured.getIpsnIdType();
		String ipsnIdNo = grpInsured.getIpsnIdNo();
		String ipsnSex = grpInsured.getIpsnSex();
		Date ipsnBirthDate = grpInsured.getIpsnBirthDate();

		Map<String, Object> mapFive = new HashMap<>();
		mapFive.put("applNo", grpInsured.getApplNo());
		mapFive.put("ipsnName", ipsnName);
		mapFive.put("ipsnIdType", ipsnIdType);
		mapFive.put("ipsnIdNo", ipsnIdNo);
		mapFive.put("ipsnSex", ipsnSex);
		mapFive.put("ipsnBirthDate", ipsnBirthDate);
		GrpInsured grpInsuredInfo = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, mapFive);
		// 判断被保险人五要素
		if (null != grpInsuredInfo) {
			throw new BusinessException("0003", "被保险人五要素信息已存在，被保人信息添加失败");
		}
		StringBuilder bnfrErrMsg = checkGrpInsured(grpInsured);
		grpInsured.setProcStat(YES_NO_FLAG.NO.getKey());
		// 处理标记（N"新增）
		grpInsured.setProcFlag(YES_NO_FLAG.NO.getKey());
		// 根据出生日期计算被保人年龄
		grpInsured.setIpsnAge(getAgeByBirthday(grpInsured.getIpsnBirthDate()));
		// 被保人年龄性别校验
		PolicyValidationAtomService policyValidationAtomService = SpringContextHolder
				.getBean("policyValidationAtomService");
		RetInfo retInfoSex = policyValidationAtomService.ValidaPolAndIpsn(grpInsured);
		if (null != retInfoSex && StringUtils.equals("0", retInfoSex.getRetCode())) {
			bnfrErrMsg.append(retInfoSex.getErrMsg());
			bnfrErrMsg.append("|");
		}
		if (!StringUtils.isEmpty(bnfrErrMsg)) {
			retInfo.setErrMsg(bnfrErrMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}

		mongoBaseDao.insert(grpInsured);
		retInfo.setRetCode("1");
		retInfo.setErrMsg("被保人信息添加成功");
		return retInfo;
	}

	private GrpInsured queryByApplNoAndIpsnNo(GrpInsured grpInsured) {
		if (null == grpInsured) {
			throw new BusinessException("0003", "参数被保人信息为空");
		}
		if (StringUtils.isEmpty(grpInsured.getApplNo())) {
			throw new BusinessException("0001", "参数投保单号为空");
		}
		if (null == grpInsured.getIpsnNo()) {
			throw new BusinessException("0002", "被保人编号为空");
		}
		String applNo = grpInsured.getApplNo();
		Long ipsnNo = grpInsured.getIpsnNo();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		map.put("ipsnNo", ipsnNo);
		GrpInsured grpInsuredInfo = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, map);
		return grpInsuredInfo;
	}

	/**
	 * 根据投保单号、被保人序号或被保人客户号，查询被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@Override
	public GrpInsured queryGrpInsured(GrpInsured grpInsured) {
		if (null == grpInsured) {
			throw new BusinessException("0003", "参数被保人信息为空");
		}

		if (StringUtils.isEmpty(grpInsured.getApplNo())) {
			throw new BusinessException("0001", "参数投保单号为空");
		}

		if (null == (grpInsured.getIpsnNo()) && StringUtils.isEmpty(grpInsured.getIpsnCustNo())) {
			throw new BusinessException("0013", "参数被保险人客户号为空");
		}

		Map<String, Object> qureryMap = new HashMap<>();
		qureryMap.put("applNo", grpInsured.getApplNo());

		if (null != grpInsured.getIpsnNo()) {
			qureryMap.put("ipsnNo", grpInsured.getIpsnNo());
		} else {
			qureryMap.put("ipsnCustNo", grpInsured.getIpsnNo());
		}

		GrpInsured grpInsur = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, qureryMap);
		if (grpInsur == null) {
			throw new BusinessException("0003", "被保险人信息为空");
		}
		return grpInsur;
	}

	/**
	 * 根据投保单号、被保人序号、查询标记，查询被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return grpInsured
	 * @throws Exception
	 */
	@Override
	public GrpInsured queryPreOrNextGrpInsured(Map<String,Object> map) {
		String applNo = (String) map.get("applNo");
		Object ipsnNo = map.get("ipsnNo");
		String flag = (String) map.get("flag");
		if(StringUtils.isEmpty(applNo)){
			throw new BusinessException("0001", "参数投保单号为空");
		}
		if(ipsnNo==null) {
			throw new BusinessException("0002", "被保人编号为空");
		}
		if(StringUtils.isEmpty(flag)) {
			throw new BusinessException("0018", "查询标记为空");
		}
		Query query = new Query();
		Criteria criteria = Criteria.where("applNo").is(applNo);
		if(StringUtils.equals(flag, "pre")){
			criteria.and("ipsnNo").lt(ipsnNo);
		}else if(StringUtils.equals(flag, "next")){
			criteria.and("ipsnNo").gt(ipsnNo);
		}else{
			throw new BusinessException("0018", "查询标记值错误");
		}
		query.addCriteria(criteria);
		if(StringUtils.equals(flag, "pre")){
			query.with(new Sort(Direction.DESC, "ipsnNo"));
		}else if(StringUtils.equals(flag, "next")){
			query.with(new Sort(Direction.ASC, "ipsnNo"));
		}else{
			throw new BusinessException("0018", "查询标记值错误");
		}
		GrpInsured grpInsured = mongoTemplate.findOne(query, GrpInsured.class);
		return grpInsured;
	}
	/**
	 * 删除被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@Override
	public RetInfo removeGrpInsured(Map<String, Object> paraMap) {
		RetInfo retInfo = new RetInfo();
		if (null == paraMap) {
			throw new BusinessException("0003", "参数Map集合为空");
		}
		if (null == paraMap.get("applNo")) {
			throw new BusinessException("0001", "投保单号为空");
		}
		if (null == paraMap.get("ipsnNo")) {
			throw new BusinessException("0002", "被保人编号为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", paraMap.get("applNo"));
		map.put("ipsnNo", paraMap.get("ipsnNo"));
		GrpInsured grpInsuredInfo = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, map);
		if (null != grpInsuredInfo) {
			Integer result = mongoBaseDao.remove(grpInsuredInfo.getClass(), map);
			if (result > 0) {
				retInfo.setApplNo(paraMap.get("applNo") + "");
				retInfo.setRetCode("1");
				retInfo.setErrMsg("被保人信息删除成功");
				return retInfo;
			} else {
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("被保人信息删除失败");
				return retInfo;
			}
		}
		retInfo.setApplNo(paraMap.get("applNo") + "");
		retInfo.setRetCode("0");
		retInfo.setErrMsg("被保人信息不存在，删除失败");
		return retInfo;
	}

	/**
	 * 修改被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@Override
	public RetInfo updateGrpInsured(GrpInsured grpInsured) {
		if (null == grpInsured) {
			throw new BusinessException("0003", "参数被保人信息为空");
		}
		if (null == grpInsured.getApplNo()) {
			throw new BusinessException("0001", "参数投保单号为空");
		}
		if (null == grpInsured.getIpsnNo()) {
			throw new BusinessException("0002", "参数客户编号为空");
		}
		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(grpInsured.getApplNo());
		grpInsured.setProcStat(YES_NO_FLAG.NO.getKey());
		// 处理标记（N"新增）
		grpInsured.setProcFlag(YES_NO_FLAG.NO.getKey());
		// 根据出生日期计算被保人年龄
		grpInsured.setIpsnAge(getAgeByBirthday(grpInsured.getIpsnBirthDate()));
		StringBuilder bnfrErrMsg = checkGrpInsured(grpInsured);
		// 被保人年龄性别校验
		PolicyValidationAtomService policyValidationAtomService = SpringContextHolder
				.getBean("policyValidationAtomService");
		RetInfo retInfoSex = policyValidationAtomService.ValidaPolAndIpsn(grpInsured);
		if (null != retInfoSex && StringUtils.equals("0", retInfoSex.getRetCode())) {
			bnfrErrMsg.append(retInfoSex.getErrMsg());
			bnfrErrMsg.append("|");
		}
		if (!StringUtils.isEmpty(bnfrErrMsg)) {
			retInfo.setErrMsg(bnfrErrMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}
		String applNo = grpInsured.getApplNo();
		Long ipsnNo = grpInsured.getIpsnNo();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		map.put("ipsnNo", ipsnNo);
		GrpInsured grpInsuredInfo = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, map);
		if (null != grpInsuredInfo) {
			mongoBaseDao.remove(GrpInsured.class, map);
			mongoBaseDao.insert(grpInsured);
			retInfo.setRetCode("1");
			retInfo.setErrMsg("被保人信息修改成功");

		} else {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("无此被保人，更新失败");
		}
		return retInfo;
	}

	/**
	 * 被保人全部提交完成信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@Override
	@AsynCall
	public RetInfo addAllGrpInsured(TraceNodePra traceNodePra) {
		if (null == traceNodePra.getApplNo() || !isReview(traceNodePra.getTraceNode())) {
			throw new BusinessException("0001", "参数不符合要求");
		}
		if (StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkBranchNo())
				|| StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkNo())
				|| StringUtils.isEmpty(traceNodePra.getTraceNode().getProcStat())) {

			throw new BusinessException("0019", traceNodePra.getApplNo());
		}

		traceNodePra.getTraceNode().setProcDate(new Date());

		// 获取操作轨迹
		Map<String, Object> operTraceMap = new HashMap<>();
		operTraceMap.put("applNo", traceNodePra.getApplNo());
		TraceNode currentTraceNode = mongoBaseDao.getPeekTraceNode(traceNodePra.getApplNo());
		if (null == currentTraceNode) {
			throw new BusinessException("0017", traceNodePra.getApplNo() + "操作轨迹不存在。");
		}
		if (StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())
				|| StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())
				|| StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_SUC.getKey())
				|| StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_SUC.getKey())
				|| StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey())
				|| StringUtils.equals(currentTraceNode.getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())) {
			throw new BusinessException("0017", traceNodePra.getApplNo() + "已提交成功，不能重复提交");
		}

		StringBuilder errMsg = new StringBuilder();
		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(traceNodePra.getApplNo());
		HashMap<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", traceNodePra.getApplNo());
		Object grpObject = mongoBaseDao.findOne(GrpInsured.class, queryMap);
		if (grpObject == null) {
			throw new BusinessException("0003");
		}
		Criteria c = Criteria.where("applNo").is(traceNodePra.getApplNo());
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		if (grpInsurAppl == null) {
			throw new BusinessException("0004");
		}

		// 任务完成参数列表
		Map<String, String> mapVar = new HashMap<>();
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		taskProcessRequestBO.setBusinessKey(grpInsurAppl.getApplNo());
		taskProcessRequestBO.setBranchNo(traceNodePra.getTraceNode().getPclkBranchNo());
		taskProcessRequestBO.setClerkName(traceNodePra.getTraceNode().getPclkName());
		taskProcessRequestBO.setClerkNo(traceNodePra.getTraceNode().getPclkNo());
		taskProcessRequestBO.setTaskId(traceNodePra.getTaskId());// 业务服务参数中获取的任务ID

		// 复核失败，则流程结束
		if (StringUtils.equals(traceNodePra.getTraceNode().getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())
				|| StringUtils.equals(traceNodePra.getTraceNode().getProcStat(),
						NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())) {
			mapVar.put("IS_INFO_CHECK_SUC", "0");

			taskProcessRequestBO.setProcessVar(mapVar);
			taskProcessServiceDascClient.completeTask(taskProcessRequestBO);

			// 保存轨迹
			mongoBaseDao.updateOperTrace(traceNodePra.getApplNo(), traceNodePra.getTraceNode());

			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("1");
			return retInfo;

		}

		/* 被保人清单 与 保单要约 进行校验 */
		/* 对被保总人数人数进行校验 */
		long ipsnNum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(traceNodePra.getApplNo())),
				GrpInsured.class);

		if (null == grpInsurAppl.getApplState()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setErrMsg("投保单[" + grpInsurAppl.getApplNo() + "]投保要约信息为空!");
			retInfo.setRetCode("0");
			return retInfo;
		}
		if (ipsnNum != grpInsurAppl.getApplState().getIpsnNum()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setErrMsg(
					"被保人清单中投保总人数[" + ipsnNum + "]与投保要约中投保总人数[" + grpInsurAppl.getApplState().getIpsnNum() + "]不一致!");
			retInfo.setRetCode("0");
			return retInfo;
		}
		// 被保人序号重复校验
		String ipsnNoErr = checkIpsnNo(traceNodePra.getApplNo()).toString();
		if (!StringUtils.isEmpty(ipsnNoErr)) {
			retInfo.setApplNo(traceNodePra.getApplNo());
			retInfo.setErrMsg(ipsnNoErr);
			retInfo.setRetCode("0");
			return retInfo;
		}
		// 被保人五要素重复校验
		String fiveElementsErr = null;
		try {
			fiveElementsErr = checkFiveElements(traceNodePra.getApplNo()).toString();
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (!StringUtils.isEmpty(fiveElementsErr)) {
			retInfo.setApplNo(traceNodePra.getApplNo());
			retInfo.setErrMsg(fiveElementsErr);
			retInfo.setRetCode("0");
			return retInfo;
		}


		/*对所有各类型保费进行校验*/
		// 保额保费校验
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(c), Aggregation.unwind("subStateList"),
				Aggregation.group("$subStateList.polCode").sum("subStateList.faceAmnt").as("sumFaceAmnt")
				.sum("subStateList.premium").as("sumPremium"));
		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);

		List<JSONObject> jsonObjects = aggregate.getMappedResults();

		List<Policy> policylist = grpInsurAppl.getApplState().getPolicyList();


		Double sumFaceAmnt = grpInsurAppl.getApplState().getSumFaceAmnt();//总保额
		Double sumPremium = grpInsurAppl.getApplState().getSumPremium();//总保费		
		/* 对保单要约险种保费进行处理*/
		Map<String, Double> mapFaceAmnt = new HashMap<>();
		Map<String, Double> mapPremium = new HashMap<>();
		Map<String, Double> mapFaceAmntSub = new HashMap<>();
		Map<String, Double> mapPremiumSub = new HashMap<>();
		Policy policyMain = new Policy();
		for (Policy policy : policylist) {
			mapFaceAmnt.put(policy.getPolCode(), policy.getFaceAmnt());
			mapPremium.put(policy.getPolCode(), policy.getPremium());
			if(StringUtils.equals(MR_CODE.MASTER.getKey(), policy.getMrCode())){
				policyMain = policy;
			}
			if (null != policy.getSubPolicyList() && !policy.getSubPolicyList().isEmpty()) {
				for (SubPolicy subPolicy : policy.getSubPolicyList()) {
					mapFaceAmntSub.put(subPolicy.getSubPolCode(), subPolicy.getSubPolAmnt());
					mapPremiumSub.put(subPolicy.getSubPolCode(), subPolicy.getSubPremium());
				}
			}
		}
		/*增加对健康险保费的处理*/
		if(null != grpInsurAppl.getHealthInsurInfo() && StringUtils.equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(), COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey())){
			int no = 0;
			for (int i=0;null != policyMain.getSubPolicyList() && i<policyMain.getSubPolicyList().size();i++ ) {				
				int temp = Integer.valueOf(policyMain.getSubPolicyList().get(i).getSubPolCode().substring(4));
				if(i == 0){
					no = temp;
				}
				if(temp < no){
					no = temp;
				}
			}
			String polCode = policyMain.getPolCode()+"-"+String.valueOf(no);/*第一子险*/
			double tempSumFixedAmnt = null == grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt()?0.00:grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt();
			double tempComInsrPrem = null == grpInsurAppl.getHealthInsurInfo().getComInsrPrem()?0.00:grpInsurAppl.getHealthInsurInfo().getComInsrPrem();
			/*总保额总保费*/
			sumFaceAmnt -= tempSumFixedAmnt;
			sumPremium -= tempComInsrPrem;
			/*主险保额保费*/
			mapFaceAmnt.put(policyMain.getPolCode(), mapFaceAmnt.get(policyMain.getPolCode()) - tempSumFixedAmnt);
			mapPremium.put(policyMain.getPolCode(), mapPremium.get(policyMain.getPolCode()) - tempComInsrPrem);
			/*对子险保额保费处理*/
			if(mapFaceAmntSub.containsKey(polCode) && mapPremiumSub.containsKey(polCode)){
				mapFaceAmntSub.put(polCode, mapFaceAmntSub.get(polCode) - tempSumFixedAmnt);
				mapPremiumSub.put(polCode,mapPremiumSub.get(polCode) - tempComInsrPrem);					
			}
		}
		if(null != grpInsurAppl.getHealthInsurInfo() && COMLNSUR_AMNT_TYPE.FLOAT_INSURED.getKey().equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())){		
			int no = 0;
			for (int i=0;null != policyMain.getSubPolicyList() && i<policyMain.getSubPolicyList().size();i++ ) {				
				int temp = Integer.valueOf(policyMain.getSubPolicyList().get(i).getSubPolCode().substring(4));
				if(i == 0){
					no = temp;
				}
				if(temp < no){
					no = temp;
				}
			}
			String polCode = policyMain.getPolCode()+"-"+String.valueOf(no);/*第一子险*/
			long pIpsnNum = 0;//主被保人人数
			long dIpsnNum = 0;//副被保人人数
			double tempSumFloatAmnt = 0.00;
			Aggregation aggregationAmnt = Aggregation.newAggregation(
					Aggregation.match(c), 
					Aggregation.group("$ipsnType").count().as("IpsnNum"));
			AggregationResults<JSONObject> aggregateAmnt = mongoTemplate.aggregate(aggregationAmnt, "grpInsured", JSONObject.class);			
			List<JSONObject> jsonObjectsAmnt = aggregateAmnt.getMappedResults();
			
			for(JSONObject jsonObject :jsonObjectsAmnt){
				if(IPSN_TYPE.PRIMARY_INSURED.getKey().equals(jsonObject.getString("_id"))){
					pIpsnNum = jsonObject.getLongValue("IpsnNum");
				}
				if(IPSN_TYPE.DEPUTY_INSURED.getKey().equals(jsonObject.getString("_id"))){
					dIpsnNum = jsonObject.getLongValue("IpsnNum");
				}
			}
			if(COMLNSUR_AMNT_USE.JOINT_INSURED.getKey().equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
				tempSumFloatAmnt  = (pIpsnNum + dIpsnNum) * (null == grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()?0.00:grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt());
			}else if(COMLNSUR_AMNT_USE.NJOINT_INSURED.getKey().equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
				tempSumFloatAmnt  = pIpsnNum  * (null == grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()?0.00:grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt());
			}
			/*总保额*/
			sumFaceAmnt -= tempSumFloatAmnt;
			mapFaceAmnt.put(policyMain.getPolCode(), mapFaceAmnt.get(policyMain.getPolCode()) - tempSumFloatAmnt);
			if(mapFaceAmntSub.containsKey(polCode)){
				mapFaceAmntSub.put(polCode, mapFaceAmntSub.get(polCode) - tempSumFloatAmnt);
			}
		}
		/* 对被保人清单保费进行处理 */
		double SumIpsnFaceAmnt = 0.00;
		double SumIpsnPremium = 0.00;
		Map<String, Double> mapIpsnFaceAmnt = new HashMap<>();
		Map<String, Double> mapIpsnPremium = new HashMap<>();
		Map<String, Double> mapIpsnFaceAmntSub = new HashMap<>();
		Map<String, Double> mapIpsnPremiumSub = new HashMap<>();
		for (JSONObject jsonObject : jsonObjects) {
			String polCode = jsonObject.getString("_id").trim();

			SumIpsnFaceAmnt += jsonObject.getDouble("sumFaceAmnt");
			SumIpsnPremium += jsonObject.getDouble("sumPremium");

			if (polCode.length() > 3) {
				/* 对子险种保额 */
				if (mapIpsnFaceAmntSub.containsKey(polCode)) {
					mapIpsnFaceAmntSub.put(polCode,
							mapIpsnFaceAmntSub.get(polCode) + jsonObject.getDouble("sumFaceAmnt"));
				} else {
					mapIpsnFaceAmntSub.put(polCode, jsonObject.getDouble("sumFaceAmnt"));
				}
				/* 对子险种保费 */
				if (mapIpsnPremiumSub.containsKey(polCode)) {
					mapIpsnPremiumSub.put(polCode, mapIpsnPremiumSub.get(polCode) + jsonObject.getDouble("sumPremium"));
				} else {
					mapIpsnPremiumSub.put(polCode, jsonObject.getDouble("sumPremium"));
				}
				/* 险种保额 */
				if (mapIpsnFaceAmnt.containsKey(polCode.substring(0, 3))) {
					mapIpsnFaceAmnt.put(polCode.substring(0, 3),
							mapIpsnFaceAmnt.get(polCode.substring(0, 3)) + jsonObject.getDouble("sumFaceAmnt"));
				} else {
					mapIpsnFaceAmnt.put(polCode.substring(0, 3), jsonObject.getDouble("sumFaceAmnt"));
				}
				/* 险种保费 */
				if (mapIpsnPremium.containsKey(polCode.substring(0, 3))) {
					mapIpsnPremium.put(polCode.substring(0, 3),
							mapIpsnPremium.get(polCode.substring(0, 3)) + jsonObject.getDouble("sumPremium"));
				} else {
					mapIpsnPremium.put(polCode.substring(0, 3), jsonObject.getDouble("sumPremium"));
				}

			} else {
				/* 险种保额 */
				if (mapIpsnFaceAmnt.containsKey(polCode)) {
					mapIpsnFaceAmnt.put(polCode, mapIpsnFaceAmnt.get(polCode) + jsonObject.getDouble("sumFaceAmnt"));
				} else {
					mapIpsnFaceAmnt.put(polCode, jsonObject.getDouble("sumFaceAmnt"));
				}
				/* 险种保费 */
				if (mapIpsnPremium.containsKey(polCode)) {
					mapIpsnPremium.put(polCode, mapIpsnPremium.get(polCode) + jsonObject.getDouble("sumPremium"));
				} else {
					mapIpsnPremium.put(polCode, jsonObject.getDouble("sumPremium"));
				}
			}
		}
		if(null != grpInsurAppl.getFundInsurInfo()){
			if (Math.abs(SumIpsnFaceAmnt - grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt()) >= 0.01) {
				errMsg.append("记入个人账户总金额[" + grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt() + "]跟被保人累计总保额[" + SumIpsnFaceAmnt+ "]不一致");
				retInfo.setRetCode("0");
			}
			if (Math.abs(SumIpsnPremium - grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()) > 0.001) {
				errMsg.append("个人账户总保费[" + grpInsurAppl.getFundInsurInfo().getIpsnFundPremium() + "]跟被保人累计总保费[" + SumIpsnPremium + "]不一致");
				retInfo.setRetCode("0");
			}
		}else{
			/* 对险种保额保费校验 */
			for (Map.Entry<String, Double> entry : mapFaceAmnt.entrySet()) {
				if (mapIpsnFaceAmnt.get(entry.getKey()) == null) {
					mapIpsnFaceAmnt.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnFaceAmnt.get(entry.getKey())) > 0.001) {
					errMsg.append("险种[" + entry.getKey() + "]总保额[" + entry.getValue() + "]跟被保人险种累计总保额["
							+ mapIpsnFaceAmnt.get(entry.getKey()) + "]不一致");
					retInfo.setRetCode("0");
				}
			}
			for (Map.Entry<String, Double> entry : mapPremium.entrySet()) {
				if (mapIpsnPremium.get(entry.getKey()) == null) {
					mapIpsnPremium.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnPremium.get(entry.getKey())) > 0.001) {
					errMsg.append("险种[" + entry.getKey() + "]总保费" + entry.getValue() + "跟被保人险种累计总保费["
							+ mapIpsnPremium.get(entry.getKey()) + "]不一致");
					retInfo.setRetCode("0");
				}
			}
			/* 对子险种保额进行校验 */
			for (Map.Entry<String, Double> entry : mapFaceAmntSub.entrySet()) {
				if (mapIpsnFaceAmntSub.get(entry.getKey()) == null) {
					mapIpsnFaceAmntSub.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnFaceAmntSub.get(entry.getKey())) > 0.001) {
					errMsg.append("子险种[" + entry.getKey() + "]总保额[" + entry.getValue() + "]跟被保人子险种累计总保额["
							+ mapIpsnFaceAmntSub.get(entry.getKey()) + "]不一致");
					retInfo.setRetCode("0");
				}
			}
			for (Map.Entry<String, Double> entry : mapPremiumSub.entrySet()) {
				if (mapIpsnPremiumSub.get(entry.getKey()) == null) {
					mapIpsnPremiumSub.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnPremiumSub.get(entry.getKey())) > 0.001) {
					errMsg.append("子险种[" + entry.getKey() + "]总保费[" + entry.getValue() + "]跟被保人子险种累计总保费["
							+ mapIpsnPremiumSub.get(entry.getKey()) + "]不一致");
					retInfo.setRetCode("0");
				}
			}
			
			if (Math.abs(SumIpsnFaceAmnt - sumFaceAmnt) > 0.001) {
				errMsg.append("保单要约总保额[" + sumFaceAmnt + "]跟被保人累计总保额[" + SumIpsnFaceAmnt+ "]不一致");
				retInfo.setRetCode("0");
			}
			if (Math.abs(SumIpsnPremium - sumPremium) > 0.001) {
				errMsg.append("保单要约总保费[" + sumPremium + "]跟被保人累计总保费[" + SumIpsnPremium + "]不一致");
				retInfo.setRetCode("0");
			}
		}

		if ("0".equals(retInfo.getRetCode())) {
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}

		/* 组织架构树 节点交费金额校验 */
		List<OrgTree> orgTrees = grpInsurAppl.getOrgTreeList();
		checkOrgTrees(orgTrees, traceNodePra.getApplNo(), retInfo, errMsg);

		// 分组属组人数校验
		if (grpInsurAppl.getIpsnStateGrpList() != null && !grpInsurAppl.getIpsnStateGrpList().isEmpty()) {
			for (IpsnStateGrp ipsnStateGrp : grpInsurAppl.getIpsnStateGrpList()) {
				Aggregation aggregation1 = Aggregation
						.newAggregation(Aggregation.match(Criteria.where("applNo").is(traceNodePra.getApplNo())),
								Aggregation.project("subStateList"), Aggregation.unwind("subStateList"),
								Aggregation.match(
										Criteria.where("subStateList.claimIpsnGrpNo").is(ipsnStateGrp.getIpsnGrpNo())), // 属组号
								Aggregation.group("subStateList.polCode").count().as("num"));
				AggregationResults<JSONObject> result = mongoTemplate.aggregate(aggregation1, "grpInsured",
						JSONObject.class);
				List<JSONObject> mappedResults = result.getMappedResults();
				checkIpsnStateGrpList(ipsnStateGrp, errMsg, mappedResults);
			}
		}
		// 收费分组属组人数校验
		if (grpInsurAppl.getIpsnPayGrpList() != null && !grpInsurAppl.getIpsnPayGrpList().isEmpty()) {
			for (IpsnPayGrp ipsnPayGrp : grpInsurAppl.getIpsnPayGrpList()) {
				Criteria criteria = Criteria.where("applNo").is(traceNodePra.getApplNo()).and("feeGrpNo").is(ipsnPayGrp.getFeeGrpNo());
				long i = mongoTemplate.count(new Query(criteria), GrpInsured.class);
				if(ipsnPayGrp.getIpsnGrpNum()-i!=0){
					errMsg.append("|团单收费分组");
					errMsg.append(ipsnPayGrp.getFeeGrpNo());
					errMsg.append("人数：");
					errMsg.append(ipsnPayGrp.getIpsnGrpNum());
					errMsg.append("，清单收费组人数：");
					errMsg.append(i);
					errMsg.append("|");
				}
			}
		}
		// 校验所有被保人的个人缴费金额总计+单位交费金额的总计=投保要约的总保费
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_INDIVI_COPAY.getKey())) {
			//非基金险
			if(null == grpInsurAppl.getFundInsurInfo()){
				Aggregation aggregation2 = Aggregation.newAggregation(Aggregation.match(c),
					Aggregation.project("applNo").andExpression("ipsnPayAmount + grpPayAmount").as("num"),
					Aggregation.group("applNo").sum("num").as("sum"));
				AggregationResults<Object> result = mongoTemplate.aggregate(aggregation2, "grpInsured", Object.class);
				List<Object> resultList = result.getMappedResults();
				HashMap<String, Object> payAmountMap = (HashMap<String, Object>) resultList.get(0);
				Double sumPayAmount = (Double) payAmountMap.get("sum");
				if (sumPremium.compareTo(sumPayAmount) > 0.0001
						|| sumPremium.compareTo(sumPayAmount) < -0.0001) {
					errMsg.append("所有被保人的个人缴费金额总计与单位交费金额的总计之和:");
					errMsg.append(sumPayAmount);
					errMsg.append("投保要约的总保费:");
					errMsg.append(sumPremium);
					errMsg.append(";");
					errMsg.append("|被保人的个人缴费金额总计加单位交费金额的总计不等于投保要约的总保费|");
				}
			}
		}
		//主被保险人数
		long masterIpsnNum = mongoTemplate.count(new Query(Criteria.where("applNo").is(traceNodePra.getApplNo()).and("ipsnType").is("I")), GrpInsured.class);
		if (masterIpsnNum==0){
			errMsg.append("被保人清单中必须有一个是主被保险人|");
		}
		if (!StringUtils.isEmpty(errMsg)) {
			retInfo.setErrMsg(errMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}
		retInfo.setRetCode("1");
		retInfo.setErrMsg("全部添加成功");

		//如果是档案清单，插入开户队列
		if(StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())){			
			List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), grpInsurAppl.getApplNo(), null);
			if(null != taskPrmyInfos && !taskPrmyInfos.isEmpty()){
				retInfo.setApplNo(grpInsurAppl.getApplNo());
				retInfo.setErrMsg("档案清单补导入清单已经导入过，请不要重复导入");
				retInfo.setRetCode("0");
				return retInfo;
			}
			TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
			String applNo = grpInsurAppl.getApplNo();
			taskPrmyInfo.setApplNo(applNo);
			String sql = "SELECT S_EV_CUST_SET_TASK_QUEUE.NEXTVAL FROM DUAL";
			Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
			taskPrmyInfo.setTaskSeq(taskSeq);
			taskPrmyInfo.setStatus("N");
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			taskPrmyInfo.setBusinessKey(applNo);
			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), taskPrmyInfo);
			// 保存轨迹
			mongoBaseDao.updateOperTrace(traceNodePra.getApplNo(), traceNodePra.getTraceNode());
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("1");
			return retInfo;
		}

		// 如果是团单被保人录入或者清汇被保人录入，则完成流程
		if (StringUtils.equals(traceNodePra.getTraceNode().getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())
				|| StringUtils.equals(traceNodePra.getTraceNode().getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey())) {

			taskProcessRequestBO.setProcessVar(mapVar);
			taskProcessServiceDascClient.completeTask(taskProcessRequestBO);
			// 保存轨迹
			mongoBaseDao.updateOperTrace(traceNodePra.getApplNo(), traceNodePra.getTraceNode());

			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("1");
			return retInfo;

		}

		// 添加复合成功
		mapVar.put("IS_INFO_CHECK_SUC", "1");
		mapVar.put("IS_INSURED_LIST", "1");

		// 判断人工审批
		// 销售渠道
		String salesChannel = "";
		// 规则类型
		String ruleType = "1";// 暂时同一为1
		// 契约形式
		String cntrForm = grpInsurAppl.getCntrType();
		for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
			// 取主销售员销售机构和销售渠道
			if (StringUtils.equals(salesInfo.getDevelMainFlag(), "1")) {
				salesChannel = salesInfo.getSalesChannel();
			}
		}
		// 如果没主销售员标记取第一个销售员的销售机构和销售渠道
		if (StringUtils.isEmpty(salesChannel)) {
			salesChannel = grpInsurAppl.getSalesInfoList().get(0).getSalesChannel();
		}
		// 通过管理机构号，销售渠道，销售机构，契约形式查询审批方案
		// flag 为false 不进人工审批，true进人工审批
		boolean flag = false;
		InsurRuleManger insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(), salesChannel,
				ruleType, cntrForm);
		if (null == insurRuleManger) {
			insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),
					PRD_SALES_CHANNEL_01.ALL.getKey(), ruleType, cntrForm);
		}
		if(null == insurRuleManger){
			insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,CNTR_TYPE_01.ALL.getKey());					
		}
		if(null == insurRuleManger){
			insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),salesChannel,ruleType,CNTR_TYPE_01.ALL.getKey());					
		}
		if (null == insurRuleManger) {
			flag = false;
		} else {
			// 人工审批规则判断 兼容多多个审批规则
			List<InsurRuleManger> insurRuleMangers = new ArrayList<>();
			insurRuleMangers.add(insurRuleManger);
			flag = grpInsurAppl.isManApprove(insurRuleMangers);
		}
		if (flag) {
			mapVar.put("IS_MAN_APPROVE", "1");// 人工审批
			Map<String, Object> map = new HashMap<>();
			map.put("applNo", grpInsurAppl.getApplNo());
			Update update = new Update();
			update.addToSet("conventions.inputConv", grpInsurAppl.getConventions().getInputConv());
			mongoBaseDao.update(GrpInsurAppl.class, map, update);
		} else {
			mapVar.put("IS_MAN_APPROVE", "0");//
		}

		taskProcessRequestBO.setProcessVar(mapVar);
		taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作

		mongoBaseDao.updateOperTrace(traceNodePra.getApplNo(), traceNodePra.getTraceNode());

		retInfo.setApplNo(grpInsurAppl.getApplNo());
		retInfo.setRetCode("1");
		return retInfo;
	}

	private void checkOrgTrees(List<OrgTree> orgTrees, String applNo, RetInfo retInfo, StringBuilder errMsg) {

		if (null == orgTrees || orgTrees.isEmpty()) {
			return;
		}
		orgTrees.removeAll(Collections.singleton(null));

		/*
		 * 组织架构树目前所采用校验规则 1、所有节点 即可以是缴费节点又可以是非缴费节点 (不用校验) 2、不是所有节点都挂人 (不用校验)
		 * 3、如果有一个被保人挂在节点上，所有被保人都必须挂在节点上 (校验) 4、根节点如果挂人，就必须是缴费节点，其他节点不限制 (校验)
		 * 5、如果是缴费节点，必须挂人 (校验) 6、如果该节点是缴费节点，挂在该节点下的被保人及单位保费金额就保存在该节点上 (校验)
		 * 7、如果该节点是非缴费节点 ，挂在该节点下的被保人单位保费金额挂在最近的缴费父节点上 (校验)
		 */
		Map<String, Double> mapLevel = new HashMap<>();/* 层级代码 */
		Map<String, String> mapNoPaid = new HashMap<>();/* 非缴费节点 */
		Map<String, String> mapIsPaid = payNode(orgTrees);/* 缴费节点及其对应非缴费节点 */
		String rootLevelCode = "";/* 跟节点层级代码 */
		for (OrgTree orgTree : orgTrees) {
			mapLevel.put(String.valueOf(orgTree.getLevelCode()),
					null == orgTree.getNodePayAmnt() ? 0.00 : orgTree.getNodePayAmnt());
			if ("N".equals(orgTree.getIsPaid())) {
				mapNoPaid.put(String.valueOf(orgTree.getLevelCode()), orgTree.getIsPaid());
			}
			if ("Y".equals(orgTree.getIsRoot())) {
				rootLevelCode = orgTree.getLevelCode();
			}
		}
		/* 按照组织层次代码 对被保人清单进行统计单位交费金额 */
		Aggregation aggregationOrg = Aggregation.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo)),
				Aggregation.group("levelCode").sum("grpPayAmount").as("sumGrpPayAmount").push("ipsnNo")
				.as("ipsnNoList"));
		AggregationResults<JSONObject> aggregationResults = mongoTemplate.aggregate(aggregationOrg, GrpInsured.class,
				JSONObject.class);
		List<JSONObject> mappedResults = aggregationResults.getMappedResults();
		/* 对统计数据进行处理 */
		Map<String, Double> mapStat = new HashMap<>();/* 层级代码 */
		for (JSONObject jsonObject : mappedResults) {
			mapStat.put(jsonObject.getString("_id"),
					null == jsonObject.getDouble("sumGrpPayAmount") ? 0.00 : jsonObject.getDouble("sumGrpPayAmount"));
		}
		/* 第四校验规则 根节点如果挂人，就必须是缴费节点，其他节点不限制 */
		if (mapNoPaid.containsKey(rootLevelCode) && mapStat.containsKey(rootLevelCode)) {
			errMsg.append("层级代码[" + rootLevelCode + "]为根节点,且在被保人清单中有被保人在该根节点下,但组织架构树中该根节点并不是缴费节点|");
			retInfo.setRetCode("0");
			return;
		}
		/* 第五校验规则 如果是缴费节点，必须有相应的被保人存在该缴费节点下 */
		for (Map.Entry<String, String> entry : mapIsPaid.entrySet()) {
			if (!mapStat.containsKey(entry.getKey())) {
				errMsg.append("层级代码[" + entry.getKey() + "]是缴费节点,但在被保人清单中没有被保人在该节点下|");
				retInfo.setRetCode("0");
				return;
			}
		}
		/* 校验被保人清单中组织层级代码是否存在组织架构树中，如果存在校验其金额 */
		for (JSONObject jsonObject : mappedResults) {
			/* 第三校验规则 如果有一个被保人挂在节点上，所有被保人都必须挂在节点上 */
			if (StringUtils.isEmpty(jsonObject.getString("_id")) && mapStat.size() > 1) {
				errMsg.append("被保人清单中有被保人" + jsonObject.get("ipsnNoList") + "不存在组织层次代码|");
				retInfo.setRetCode("0");
				continue;
			}
			if (!StringUtils.isEmpty(jsonObject.getString("_id")) && !mapLevel.containsKey(jsonObject.get("_id"))) {
				errMsg.append("组织架构树中不存在该[" + jsonObject.getString("_id") + "]组织层次代码|");
				retInfo.setRetCode("0");
				continue;
			}
			/* 第六校验规则 如果该节点是缴费节点，挂在该节点下的被保人及单位保费金额就保存在该节点上 */
			if (mapIsPaid.containsKey(jsonObject.getString("_id"))) {
				if (null == jsonObject.get("sumGrpPayAmount")) {
					errMsg.append("该被保人清单中,层次代码为[" + jsonObject.getString("_id") + "]的清单无单位交费金额元素(grpPayAmount)|");
					retInfo.setRetCode("0");
					continue;
				}
				String levelCode[] = StringUtils.isEmpty(mapIsPaid.get(jsonObject.getString("_id"))) ? null
						: mapIsPaid.get(jsonObject.getString("_id")).split("\\|");
				double sum = mapLevel.get(jsonObject.getString("_id"));
				for (int i = 0; null != levelCode && i < levelCode.length; i++) {
					sum -= null == mapStat.get(levelCode[i]) ? 0.00 : mapStat.get(levelCode[i]);
				}
				if (Math.abs(sum - mapStat.get(jsonObject.getString("_id"))) > 0.001) {
					errMsg.append("层级代码[" + jsonObject.getString("_id") + "]为缴费节点,该节点所占金额[" + sum
							+ "]跟被保人清单中,该层级代码及其非缴费子节点累计单位交费金额[" + mapStat.get(jsonObject.getString("_id")) + "]不一致|");
					retInfo.setRetCode("0");
					continue;
				}
			}
			/* 第七校验规则 如果该节点是非缴费节点 ，挂在该节点下的被保人单位保费金额挂在最近的缴费父节点上 */
			if (mapNoPaid.containsKey(jsonObject.getString("_id"))) {
				/* 查找其父节点是否是缴费节点 */
				OrgTree orgTree = findPrioLevelCode(orgTrees, jsonObject.getString("_id"));
				if (null == orgTree || StringUtils.equals(orgTree.getLevelCode(), jsonObject.getString("_id"))) {
					errMsg.append("层级代码[" + jsonObject.getString("_id") + "]为非缴费节点,但其上级层级代码都不为缴费节点|");
					retInfo.setRetCode("0");
					continue;
				}
				String levelCode[] = StringUtils.isEmpty(mapIsPaid.get(orgTree.getLevelCode())) ? null
						: mapIsPaid.get(orgTree.getLevelCode()).split("\\|");
				double surplus = mapLevel.get(orgTree.getLevelCode());
				/* 将其他非缴费子节点在被保人清单累计金额给去掉，留下该校验的 */
				for (int i = 0; null != levelCode && i < levelCode.length; i++) {
					double sub = null == mapStat.get(levelCode[i]) ? 0.00 : mapStat.get(levelCode[i]);
					surplus -= StringUtils.equals(jsonObject.getString("_id"), levelCode[i]) ? 0.00 : sub;
				}
				surplus -= mapStat.get(orgTree.getLevelCode());
				if (Math.abs(surplus - mapStat.get(jsonObject.getString("_id"))) > 0.0001) {
					errMsg.append("层次代码[" + jsonObject.getString("_id") + "]为非缴费节点,其在被保人清单中累计单位交费金额["
							+ mapStat.get(jsonObject.getString("_id")) + "]跟组织架构树中该节点所占金额[" + surplus + "]不一致|");
					retInfo.setRetCode("0");
					continue;
				}
			}
		}
	}

	private OrgTree findPrioLevelCode(List<OrgTree> orgTrees, String levelCode) {
		if (null == orgTrees || orgTrees.isEmpty() || null == levelCode) {
			return null;
		}
		for (OrgTree orgTree2 : orgTrees) {
			if (StringUtils.equals(orgTree2.getLevelCode(), levelCode)) {
				if (StringUtils.equals(orgTree2.getIsPaid(), "N")) {
					return findPrioLevelCode(orgTrees, orgTree2.getPrioLevelCode());
				} else {
					return orgTree2;
				}
			}
		}
		return null;
	}

	private Map<String, String> payNode(List<OrgTree> orgTrees) {

		Map<String, String> mapPayType = new HashMap<>();/* 是否缴费 */
		for (OrgTree orgTree : orgTrees) {
			mapPayType.put(String.valueOf(orgTree.getLevelCode()), orgTree.getIsPaid());
		}
		Map<String, String> lMaps = new HashMap<>();/* 统计缴费节点下非缴费节点 */
		for (Map.Entry<String, String> entry : mapPayType.entrySet()) {
			if (StringUtils.equals("Y", entry.getValue())) {
				if (lMaps.containsKey(entry.getKey())) {
					continue;
				}
				lMaps.put(entry.getKey(), "");
			} else {
				OrgTree orgTree = findPrioLevelCode(orgTrees, entry.getKey());
				if (null == orgTree) {
					continue;
				} else if (lMaps.containsKey(orgTree.getLevelCode())) {
					lMaps.put(orgTree.getLevelCode(), (StringUtils.isEmpty(lMaps.get(orgTree.getLevelCode()))
							? entry.getKey() : (lMaps.get(orgTree.getLevelCode()) + entry.getKey())) + "|");
				} else {
					lMaps.put(orgTree.getLevelCode(), entry.getKey() + "|");
				}
			}
		}
		return lMaps;
	}

	private void checkIpsnStateGrpList(IpsnStateGrp ipsnStateGrp, StringBuilder ecseptionStr,
			List<JSONObject> mappedResults) {
		List<Long> ipsnGrpNos = new ArrayList<>();
		for (JSONObject jsonObj : mappedResults) {
			long policyNum = jsonObj.getLong("num");
			if (Long.compare(policyNum, ipsnStateGrp.getIpsnGrpNum()) != 0 && !ipsnGrpNos.contains(ipsnStateGrp.getIpsnGrpNo())) {
				ecseptionStr.append("要约分组分组：" + ipsnStateGrp.getIpsnGrpNo() + " 团单要约属组人数：" + ipsnStateGrp.getIpsnGrpNum()
				+ " 清单累计属组人数：" + policyNum + ";");
				ipsnGrpNos.add(ipsnStateGrp.getIpsnGrpNo());
			}
		}
	}

	private boolean isReview(TraceNode traceNode) {
		if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_SUC.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_SUC.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据用户生日计算年龄
	 */
	private Long getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}
		return (long) age;
	}

	// 被保人序号重复校验
	private StringBuilder checkIpsnNo(String applNo) {
		StringBuilder ecseptionStr = new StringBuilder();
		Criteria c = Criteria.where("applNo").is(applNo);
		Aggregation ipsnAggregation = Aggregation.newAggregation(Aggregation.match(c),
				Aggregation.group("ipsnNo").count().as("sum"), Aggregation.match(new Criteria("sum").gte(2)));
		AggregationResults<JSONObject> ipsnResult = mongoTemplate.aggregate(ipsnAggregation, "grpInsured",
				JSONObject.class);
		List<JSONObject> results = ipsnResult.getMappedResults();
		for (JSONObject result : results) {
			ecseptionStr.append("|ipsnNo=");
			ecseptionStr.append(result.getString("_id"));
			ecseptionStr.append("被保人序号重复|");
		}
		return ecseptionStr;
	}

	// 被保人五要素校验
	private StringBuilder checkFiveElements(String applNo) throws ParseException {
		StringBuilder err = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		long num = mongoBaseDao.count(GrpInsured.class, map);
		if (num <= 10000) {
			Criteria criteria = new Criteria();
			criteria.and("applNo").is(applNo);
			Aggregation aggregationSub = Aggregation
					.newAggregation(Aggregation.match(criteria),
							Aggregation
							.project("ipsnBirthDate", "ipsnNo", "ipsnName", "ipsnSex", "ipsnIdType",
									"ipsnIdNo"),
							Aggregation.group("ipsnIdNo", "ipsnName", "ipsnSex", "ipsnIdType", "ipsnBirthDate").count()
							.as("Sum").push("ipsnNo").as("ipsnNo"),
							Aggregation.match(Criteria.where("Sum").gte(2)));
			AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub,
					GrpInsured.class, JSONObject.class);
			for (JSONObject jsonObj : jsAggregationResultsSub.getMappedResults()) {
				err.append("|ipsnNo=");
				err.append(jsonObj.getString("ipsnNo"));
				err.append(",ipsnName =");
				err.append(jsonObj.getString("ipsnName"));
				err.append(",ipsnSex =");
				err.append(jsonObj.getString("ipsnSex"));
				err.append(",ipsnBirthDate =");
				err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"), "yyyy-MM-dd"));
				err.append(",ipsnIdType =");
				err.append(jsonObj.getString("ipsnIdType"));
				err.append(",ipsnIdNo =");
				err.append(jsonObj.getString("ipsnIdNo"));
				err.append(",被保人五要素重复|");
			}
			return err;
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo)),
				Aggregation.project("ipsnSex").and("ipsnBirthDate").extractDayOfMonth().as("day").and("ipsnBirthDate")
				.extractYear().as("year").and("ipsnBirthDate").extractMonth().as("month"),
				Aggregation.group("year", "month", "day", "ipsnSex").count().as("Sum"),
				Aggregation.sort(Direction.ASC, "year", "month"));
		AggregationResults<JSONObject> jsAggregationResults = mongoTemplate.aggregate(aggregation, GrpInsured.class,
				JSONObject.class);
		long sum = 0;
		String beginDate = "1900-01-01";
		String endDate;
		for (int i = 0; i < jsAggregationResults.getMappedResults().size(); i++) {
			sum += jsAggregationResults.getMappedResults().get(i).getLong("Sum");
			if (sum >= 100000) {
				JSONObject jsonObject = jsAggregationResults.getMappedResults().get(i - 1);
				Criteria criteria = new Criteria();
				criteria.and("applNo").is(applNo);
				endDate = String.valueOf(jsonObject.getLong("year")) + "-" + String.valueOf(jsonObject.getLong("month"))
				+ "-" + String.valueOf(jsonObject.getLong("day"));
				criteria.and("ipsnBirthDate").gte(sDateFormat.parseObject(beginDate))
				.lte(sDateFormat.parseObject(endDate));
				Aggregation aggregationSub = Aggregation.newAggregation(Aggregation.match(criteria),
						Aggregation.project("ipsnBirthDate", "ipsnNo", "ipsnName", "ipsnSex", "ipsnIdType", "ipsnIdNo"),
						Aggregation.group("ipsnIdNo", "ipsnName", "ipsnSex", "ipsnIdType", "ipsnBirthDate").count()
						.as("Sum").push("ipsnNo").as("ipsnNo"),
						Aggregation.match(Criteria.where("Sum").gte(2)));
				AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub,
						GrpInsured.class, JSONObject.class);
				System.err.println(jsAggregationResultsSub.getMappedResults());
				for (JSONObject jsonObj : jsAggregationResultsSub.getMappedResults()) {
					err.append("|ipsnNo=");
					err.append(jsonObj.getString("ipsnNo"));
					err.append(",ipsnName =");
					err.append(jsonObj.getString("ipsnName"));
					err.append(",ipsnSex =");
					err.append(jsonObj.getString("ipsnSex"));
					err.append(",ipsnBirthDate =");
					err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"), "yyyy-MM-dd"));
					err.append(",ipsnIdType =");
					err.append(jsonObj.getString("ipsnIdType"));
					err.append(",ipsnIdNo =");
					err.append(jsonObj.getString("ipsnIdNo"));
					err.append(",被保人五要素重复|");
				}
				beginDate = endDate;
				sum = jsAggregationResults.getMappedResults().get(i).getLong("Sum");
			}
		}
		if (sum > 0) {
			JSONObject jsonObject = jsAggregationResults.getMappedResults()
					.get(jsAggregationResults.getMappedResults().size() - 1);
			Criteria criteria = new Criteria();
			criteria.and("applNo").is(applNo);
			endDate = String.valueOf(jsonObject.getLong("year")) + "-" + String.valueOf(jsonObject.getLong("month"))
			+ "-" + String.valueOf(jsonObject.getLong("day"));
			criteria.and("ipsnBirthDate").gte(sDateFormat.parseObject(beginDate)).lte(sDateFormat.parseObject(endDate));
			Aggregation aggregationSub = Aggregation
					.newAggregation(Aggregation.match(criteria),
							Aggregation
							.project("ipsnBirthDate", "ipsnNo", "ipsnName", "ipsnSex", "ipsnIdType",
									"ipsnIdNo"),
							Aggregation.group("ipsnIdNo", "ipsnName", "ipsnSex", "ipsnIdType", "ipsnBirthDate").count()
							.as("Sum").push("ipsnNo").as("ipsnNo"),
							Aggregation.match(Criteria.where("Sum").gte(2)));
			AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub,
					GrpInsured.class, JSONObject.class);
			for (JSONObject jsonObj : jsAggregationResultsSub.getMappedResults()) {
				err.append("|ipsnNo=");
				err.append(jsonObj.getString("ipsnNo"));
				err.append(",ipsnName =");
				err.append(jsonObj.getString("ipsnName"));
				err.append(",ipsnSex =");
				err.append(jsonObj.getString("ipsnSex"));
				err.append(",ipsnBirthDate =");
				err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"), "yyyy-MM-dd"));
				err.append(",ipsnIdType =");
				err.append(jsonObj.getString("ipsnIdType"));
				err.append(",ipsnIdNo =");
				err.append(jsonObj.getString("ipsnIdNo"));
				err.append(",被保人五要素重复|");
			}
		}
		return err;
	}

	// 投保人信息校验
	private void checkHldrInfo(GrpInsured grpInsured, StringBuilder bnfrErrMsg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (grpInsured.getHldrInfo() == null) {
			bnfrErrMsg.append("|投保人信息为空.|");
			return;
		}
		if (StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrName())) {
			bnfrErrMsg.append("|投保人姓名为空|");
		}
		if (ValidatorUtils.testSex(grpInsured.getHldrInfo().getHldrSex())) {
			bnfrErrMsg.append("|投保人性别不符合字典检查|");
		}
		if (grpInsured.getHldrInfo().getHldrBirthDate() == null) {
			bnfrErrMsg.append("|投保人出生日期为空|");
		} else {
			String str = sdf.format(grpInsured.getHldrInfo().getHldrBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				bnfrErrMsg.append("|投保人出生日期错误|");
			}
		}
		if (ValidatorUtils.testIdType(grpInsured.getHldrInfo().getHldrIdType())) {
			bnfrErrMsg.append("|投保人证件类型不符合字典检查|");
		}
		if (StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrIdNo())) {
			bnfrErrMsg.append("|投保人证件号码为空|");
		}
		if (StringUtils.equals(ID_TYPE.ID.getKey(), grpInsured.getHldrInfo().getHldrIdType())) {
			String validRet = ValidatorUtils.validIdNo(grpInsured.getHldrInfo().getHldrIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				bnfrErrMsg.append("|");
				bnfrErrMsg.append(validRet);
				bnfrErrMsg.append("|");
				return;
			}else{
				grpInsured.getHldrInfo().setHldrIdNo(grpInsured.getHldrInfo().getHldrIdNo().toUpperCase());
			}
			if (grpInsured.getHldrInfo().getHldrBirthDate() != null && !StringUtils.equals(
					DateFormatUtils.format(grpInsured.getHldrInfo().getHldrBirthDate(), "yyyyMMdd"),
					grpInsured.getHldrInfo().getHldrIdNo().substring(6, 14))) {
				bnfrErrMsg.append("|投保人出生日期与身份证不符|");
			}
			if (Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 0
					&& StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(), SEX.MALE.getKey())) {
				bnfrErrMsg.append("|投保人性别与身份证不符|");
			}
			if (Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 1
					&& StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(), SEX.FEMALE.getKey())) {
				bnfrErrMsg.append("|投保人性别与身份证不符|");
			}
		}
		if (StringUtils.equals(APPL_RT_OPSN.ME.getKey(), grpInsured.getRelToHldr())) {
			if (!StringUtils.equals(grpInsured.getHldrInfo().getHldrName(), grpInsured.getIpsnName())) {
				bnfrErrMsg.append("|投被保人关系为本人时，投被保人姓名不相同|");
			}
			if (!StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(), grpInsured.getIpsnSex())) {
				bnfrErrMsg.append("|投被保人关系为本人时，投被保人性别不相同|");
			}
			if (grpInsured.getHldrInfo().getHldrBirthDate() != null && grpInsured.getIpsnBirthDate() != null
					&& !DateUtils.isSameDay(grpInsured.getHldrInfo().getHldrBirthDate(),
							grpInsured.getIpsnBirthDate())) {
				bnfrErrMsg.append("|投被保人关系为本人时，投被保人出生日期不相同|");
			}
			if (!StringUtils.equals(grpInsured.getHldrInfo().getHldrIdType(), grpInsured.getIpsnIdType())) {
				bnfrErrMsg.append("|投被保人关系为本人时，投被保人证件类型不相同|");
			}
			if (!StringUtils.equals(grpInsured.getHldrInfo().getHldrIdNo(), grpInsured.getIpsnIdNo())) {
				bnfrErrMsg.append("|投被保人关系为本人时，投被保人证件号码不相同|");
			}
		}
	}

	// 受益人信息校验
	private void checkBnfrInfo(BnfrInfo bnfrInfo, StringBuilder bnfrErrMsg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (StringUtils.isEmpty(bnfrInfo.getBnfrName())) {
			bnfrErrMsg.append("|受益人姓名为空.|");
		}
		if (bnfrInfo.getBnfrLevel() == null) {
			bnfrErrMsg.append("|受益人顺序为空.|");
		}
		if (bnfrInfo.getBnfrProfit() == null) {
			bnfrErrMsg.append("|受益人份额为空.|");
		}
		if (ValidatorUtils.testSex(bnfrInfo.getBnfrSex())) {
			bnfrErrMsg.append("|受益人性别不符合字典检查.|");
		}
		if (bnfrInfo.getBnfrBirthDate() != null) {
			String str = sdf.format(bnfrInfo.getBnfrBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				bnfrErrMsg.append("|受益人出生日期错误|");
			}
		}
		if (StringUtils.equals(bnfrInfo.getBnfrIdType(), ID_TYPE.ID.getKey())
				&& !StringUtils.isEmpty(bnfrInfo.getBnfrIdNo())) {
			String validRet = ValidatorUtils.validIdNo(bnfrInfo.getBnfrIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				bnfrErrMsg.append("|");
				bnfrErrMsg.append(validRet);
				bnfrErrMsg.append("|");
			} else {
				bnfrInfo.setBnfrIdNo(bnfrInfo.getBnfrIdNo().toUpperCase());
				if (bnfrInfo.getBnfrBirthDate() != null
						&& !StringUtils.equals(DateFormatUtils.format(bnfrInfo.getBnfrBirthDate(), "yyyyMMdd"),
								bnfrInfo.getBnfrIdNo().substring(6, 14))) {
					bnfrErrMsg.append("|受益人出生日期与身份证不符|");
				}
				if (Integer.parseInt(bnfrInfo.getBnfrIdNo().substring(16, 17)) % 2 == 0
						&& StringUtils.equals(bnfrInfo.getBnfrSex(), SEX.MALE.getKey())) {
					bnfrErrMsg.append("|受益人性别与身份证不符|");
				}
				if (Integer.parseInt(bnfrInfo.getBnfrIdNo().substring(16, 17)) % 2 == 1
						&& StringUtils.equals(bnfrInfo.getBnfrSex(), SEX.FEMALE.getKey())) {
					bnfrErrMsg.append("|受益人性别与身份证不符|");
				}
			}
		}
	}

	private StringBuilder checkGrpInsured(GrpInsured grpInsured) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuilder bnfrErrMsg = new StringBuilder();
		// 判断被保人信息若为空返回失败信息
		if (null == grpInsured) {
			throw new BusinessException("0003");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", grpInsured.getApplNo());
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if (null == grpInsurAppl) {
			throw new BusinessException("0004", "该团单不存在");
		}
		/*
		 * 与主被保险人关系,被保险人类型,主被保险人编号联合校验
		 */
		if (ValidatorUtils.testIpsnType(grpInsured.getIpsnType())) {
			bnfrErrMsg.append("|被保险人类型不符合字典检查|");
		} else if (StringUtils.equals(grpInsured.getIpsnType(), IPSN_TYPE.PRIMARY_INSURED.getKey())) {
			if (!StringUtils.isEmpty(grpInsured.getIpsnRtMstIpsn())
					&& !StringUtils.equals(grpInsured.getIpsnRtMstIpsn(), APPL_RT_OPSN.ME.getKey())) {
				bnfrErrMsg.append("|被保险人为主被保险人时，与主被保险人关系必须是本人.|");
			} else if (StringUtils.isEmpty(grpInsured.getIpsnRtMstIpsn())) {
				grpInsured.setIpsnRtMstIpsn(APPL_RT_OPSN.ME.getKey());
			}
			// 主被保险人编号
			if (grpInsured.getMasterIpsnNo() == null) {
				grpInsured.setMasterIpsnNo(grpInsured.getIpsnNo());
			} else if (Long.compare(grpInsured.getMasterIpsnNo(), grpInsured.getIpsnNo()) != 0) {
				bnfrErrMsg.append("|被保险人为主被保险人时，主被保险人编号应等于被保险人编号.|");
			}
		} else {
			if (ValidatorUtils.testApplRtOpsn(grpInsured.getIpsnRtMstIpsn())) {
				bnfrErrMsg.append("|与主被保险人关系不符合字典检查.|");
			}
			// 主被保险人编号
			if (grpInsured.getMasterIpsnNo() == null) {
				bnfrErrMsg.append("|主被保险人编号为空.|");

			} else {// 如果主被保险人编号非空，则该被保险人必须存在
				Map<String, Object> mapMaster = new HashMap<>();
				mapMaster.put("applNo", grpInsured.getApplNo());
				mapMaster.put("ipsnNo", grpInsured.getMasterIpsnNo());
				GrpInsured grpInsuredInfo = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, mapMaster);
				// 如果查不到该被保险人
				if (null == grpInsuredInfo) {
					bnfrErrMsg.append("|根据此主被保险人编号查无此人.|");
				}

			}
		}
		// 被保人姓名
		if (StringUtils.isEmpty(grpInsured.getIpsnName())) {
			bnfrErrMsg.append("|被保险人姓名为空|");
		}
		// 被保险人类型
		if (ValidatorUtils.testIpsnType(grpInsured.getIpsnType())) {
			bnfrErrMsg.append("|被保险人类型不符合字典检查|");
		}
		// 被保险人性别
		if (ValidatorUtils.testSex(grpInsured.getIpsnSex())) {
			bnfrErrMsg.append("|被保险人性别不符合字典检查|");
		}
		// 被保人出生日期
		if (null == grpInsured.getIpsnBirthDate()) {
			bnfrErrMsg.append("|被保人出生日期为空或格式有误|");
		} else {
			String str = sdf.format(grpInsured.getIpsnBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				bnfrErrMsg.append("|投保人出生日期错误|");
			}
		}
		// 证件类别不能为空
		if (ValidatorUtils.testIdType(grpInsured.getIpsnIdType())) {
			bnfrErrMsg.append("|被保人证件类别不符合字典检查|");
		}
		// 如果是身份证，则对证件号码进行校验
		if (StringUtils.equals(grpInsured.getIpsnIdType(), IPSN_TYPE.PRIMARY_INSURED.getKey())
				&& !StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			String validRet = ValidatorUtils.validIdNo(grpInsured.getIpsnIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				bnfrErrMsg.append("|" + validRet + "|");
			} else {
				grpInsured.setIpsnIdNo(grpInsured.getIpsnIdNo().toUpperCase());
				if (grpInsured.getIpsnBirthDate() != null
						&& !StringUtils.equals(DateFormatUtils.format(grpInsured.getIpsnBirthDate(), "yyyyMMdd"),
								grpInsured.getIpsnIdNo().substring(6, 14))) {
					bnfrErrMsg.append("|被保人出生日期与身份证不符|");
				}
				if (Integer.parseInt(grpInsured.getIpsnIdNo().substring(16, 17)) % 2 == 0
						&& StringUtils.equals(grpInsured.getIpsnSex(), SEX.MALE.getKey())) {
					bnfrErrMsg.append("|被保人性别与身份证不符|");
				}
				if (Integer.parseInt(grpInsured.getIpsnIdNo().substring(16, 17)) % 2 == 1
						&& StringUtils.equals(grpInsured.getIpsnSex(), SEX.FEMALE.getKey())) {
					bnfrErrMsg.append("|被保人性别与身份证不符|");
				}
			}
		}
		// 证件号码不能为空
		if (StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			bnfrErrMsg.append("|被保人证件号码为空|");
			// 证件类别长度为8-18
		} else if (grpInsured.getIpsnIdNo().length() < 8 || grpInsured.getIpsnIdNo().length() > 18) {
			bnfrErrMsg.append("|被保人证件号码长度有误|");
		}
		// 职业代码
		if (StringUtils.isEmpty(grpInsured.getIpsnOccCode())) {
			bnfrErrMsg.append("|职业代码为空|");
			// 职业代码长度为6
		} else if (6 != grpInsured.getIpsnOccCode().length()) {
			bnfrErrMsg.append("|职业代码长度有误|");
		}
		// 职业风险等级为空校验
		if (StringUtils.isEmpty(grpInsured.getIpsnOccClassLevel())) {
			bnfrErrMsg.append("|职业风险等级为空|");
		}
		// 组织层次代码
		if (grpInsurAppl.getOrgTreeList() != null && !grpInsurAppl.getOrgTreeList().isEmpty()
				&& StringUtils.isEmpty(grpInsured.getLevelCode())) {
			bnfrErrMsg.append("|组织层次代码为空|");
		}
		// 在职标记
		if (!StringUtils.isEmpty(grpInsured.getInServiceFlag())
				&& ValidatorUtils.testYesNo(grpInsured.getInServiceFlag())) {
			bnfrErrMsg.append("|在职标记不符合字典检查|");
		}
		// 医保标记
		if (!StringUtils.isEmpty(grpInsured.getIpsnSss()) && ValidatorUtils.testIpsnSss(grpInsured.getIpsnSss())) {
			bnfrErrMsg.append("|医保标记不符合字典检查|");
		}
		// 是否异常告知
		if (ValidatorUtils.testYesNo(grpInsured.getNotificaStat())) {
			bnfrErrMsg.append("|异常告知不符合字典检查|");
		}
		// 子要约的险种保费累计金额
		// 子要约险种是否跟基本信息中匹配校验
		Double sumPremium = checkSubStateList(grpInsurAppl, grpInsured.getSubStateList(), bnfrErrMsg);
		// 个人、单位缴费金额校验|个人付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.PSN_ACC_PAY.getKey())) {
			if (grpInsured.getIpsnPayAmount() == null || grpInsured.getIpsnPayAmount() < 0.001) {
				grpInsured.setIpsnPayAmount(sumPremium);
			} else if (Math.abs(grpInsured.getIpsnPayAmount() - sumPremium) >= 0.001) {
				bnfrErrMsg.append("|保费来源为个人付款时，个人缴费金额与子要约的保费累计金额不相等|");
			}
			if (grpInsured.getGrpPayAmount() != null && Math.abs(grpInsured.getGrpPayAmount()) > 0) {
				bnfrErrMsg.append("|保费来源为个人付款时，团体缴费金额应为空或为0|");
			}
		}
		// 个人、单位缴费金额校验|团体付款
		if (grpInsurAppl.getFundInsurInfo() == null
			&& StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
			if (grpInsured.getGrpPayAmount() == null || grpInsured.getGrpPayAmount() < 0.001) {
				grpInsured.setGrpPayAmount(sumPremium);
			} else if (Math.abs(grpInsured.getGrpPayAmount() - sumPremium) >= 0.001) {
				bnfrErrMsg.append("|保费来源为团体付款时，单位缴费金额与子要约的保费累计金额不相等|");
			}
			if (grpInsured.getIpsnPayAmount() != null && Math.abs(grpInsured.getIpsnPayAmount()) > 0) {
				bnfrErrMsg.append("|保费来源为团体付款时，个人缴费金额应为空或为0|");
			}
		}
		// 个人缴费金额,团体缴费金额校验|团体个人共同付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_INDIVI_COPAY.getKey())) {
			if (grpInsured.getIpsnPayAmount() == null || grpInsured.getIpsnPayAmount() < 0.001) {
				grpInsured.setIpsnPayAmount(0.0);
			}
			if (grpInsured.getGrpPayAmount() == null || grpInsured.getGrpPayAmount() < 0.001) {
				grpInsured.setGrpPayAmount(0.0);
			}
			if (grpInsurAppl.getFundInsurInfo() == null
				&& Math.abs(grpInsured.getIpsnPayAmount() + grpInsured.getGrpPayAmount() - sumPremium) >= 0.01) {
				bnfrErrMsg.append("“保费来源为团个共同付款时，非基金险个人缴费金额+团体缴费金额!=子要约的保费累计金额”");
			}
			if (grpInsurAppl.getFundInsurInfo() != null
				&& Math.abs(grpInsured.getIpsnPayAmount() - sumPremium) >= 0.01) {
				bnfrErrMsg.append("“保费来源为团个共同付款时，基金险个人缴费金额!=子要约的保费累计金额”");
			}
				
		}
		// 收费属组,交费账户校验 | 团体付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
			if (grpInsured.getFeeGrpNo() != null
					|| (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty())) {
				bnfrErrMsg.append("|保费来源为团体付款时，收费属组号，缴费账户应为空|");
			}
		}
		// 收费属组，交费账户校验 | 个人付款，团个共同付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.PSN_ACC_PAY.getKey())
				|| StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),
						PREM_SOURCE.GRP_INDIVI_COPAY.getKey())) {
			if (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty()) {
				// 校验交费账户
				checkAccInfos(grpInsured, bnfrErrMsg);
			}
			// 收费属组号，交费帐号不能同时为空
			if (!checkFeeGrpNo(grpInsurAppl, grpInsured)) {
				if (grpInsured.getAccInfoList() == null || grpInsured.getAccInfoList().isEmpty()) {
					bnfrErrMsg.append("|收费属组号与缴费账户不能同时为空|");
				}
			}
		}
		// 投保人校验
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())
				&& grpInsured.getHldrInfo() != null) {
			bnfrErrMsg.append("|契约类型为团单时投保人信息必须为空|");
		}
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			checkHldrInfo(grpInsured, bnfrErrMsg);
		}
		// 受益人信息校验
		if (null != grpInsured.getBnfrInfoList() && !grpInsured.getBnfrInfoList().isEmpty()) {
			for (BnfrInfo bnfrInfo : grpInsured.getBnfrInfoList()) {
				checkBnfrInfo(bnfrInfo, bnfrErrMsg);
				if (bnfrInfo.getBnfrProfit() != null && bnfrInfo.getBnfrProfit() > 1.0) {
					bnfrErrMsg.append("|受益人受益份额应小于等于1|");
				}
			}
		}
		setIpsnStateList(grpInsured,grpInsurAppl);
		return bnfrErrMsg;
	}

	private void checkAccInfos(GrpInsured grpInsured, StringBuilder errors) {
		List<AccInfo> accInfos = grpInsured.getAccInfoList();
		if (accInfos == null || accInfos.isEmpty()) {
			return;
		}
		// 账户顺序集合
		List<Long> seqNos = new ArrayList<>();
		// 个人账户扣款金额总和
		double sumIpsnPayAmnt = 0.0;
		// 个人账户交费比例总和
		double sumIpsnPayPct = 0.0;
		for (int i = 0; i < accInfos.size(); i++) {
			AccInfo accInfo = accInfos.get(i);
			if (accInfo == null) {
				seqNos.add(-1l);
				continue;
			}
			seqNos.add(accInfo.getSeqNo());
			// 账户顺序校验
			if (accInfo.getSeqNo() == null || accInfo.getSeqNo() > 1000 || accInfo.getSeqNo() <= 0) {
				errors.append("|账户顺序为空或数值错误.|");
			} else if (seqNos.indexOf(accInfo.getSeqNo()) != i) {
				errors.append("|账户顺序重复.|");
			}
			// 个人扣款金额校验
			if (accInfo.getIpsnPayAmnt() == null || accInfo.getIpsnPayAmnt() < 0.01) {
				errors.append("|个人账户扣款金额不能为空或为0|");
				accInfo.setIpsnPayAmnt(0.0);
			} else {
				sumIpsnPayAmnt += accInfo.getIpsnPayAmnt();
			}
			// 个人账户交费比例
			if (accInfo.getIpsnPayPct() == null || accInfo.getIpsnPayPct().compareTo(0.0) == 0) {
				accInfo.setIpsnPayPct((int) (accInfo.getIpsnPayAmnt() * 1000 / grpInsured.getIpsnPayAmount()) / 1000.0);
			}
			sumIpsnPayPct += accInfo.getIpsnPayPct();
			if (!Pattern.matches("[0-9]{4}", accInfo.getBankCode())) {
				errors.append("|交费银行必须为4位的数字代码|");
			} else {
				String s = ValidatorUtils.testBankAccNo(accInfo.getBankAccNo());
				if (!StringUtils.isEmpty(s)) {
					errors.append("|");
					errors.append(s);
					errors.append("|");
				}
			}
		}
		if (Math.abs(sumIpsnPayAmnt - grpInsured.getIpsnPayAmount()) >= 0.001) {
			errors.append("|个人账户扣款金额总和不等于个人交费金额|");
		}
		if (Math.abs(sumIpsnPayPct - 1) >= 0.01) {
			errors.append("|所有账户交费比例之和不为1.|");
		}
	}

	private double checkSubStateList(GrpInsurAppl grpInsurAppl, List<SubState> subStates, StringBuilder errors) {
		Double sumPremium = 0.0;
		// 获取基本信息中的险种，子险种代码
		if (subStates == null || subStates.isEmpty()) {
			errors.append("|子要约列表为空.|");
			return sumPremium;
		}
		List<String> polCodeList = new ArrayList<>();
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			if (policy.getSubPolicyList() == null || policy.getSubPolicyList().isEmpty()) {
				polCodeList.add(policy.getPolCode());
			} else {
				for (SubPolicy subPolicy : policy.getSubPolicyList()) {
					polCodeList.add(subPolicy.getSubPolCode());
				}
			}
		}
		// 子要约列表中险种校验
		for (SubState subState : subStates) {
			if (!polCodeList.contains(subState.getPolCode())) {
				errors.append("|子要约列表中险种[" + subState.getPolCode() + "]不包含于团单基本信息中.|");
			}
			// 获取子要约险种名称 2016-12-16 11:20
			for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
				if (!StringUtils.isEmpty(subState.getPolCode())
					&& StringUtils.equals(subState.getPolCode(), policy.getPolCode())) {
					subState.setPolNameChn(policy.getPolNameChn());
				}
				if(policy.getSubPolicyList()==null){
					continue;
				}
				for(SubPolicy subPolicy:policy.getSubPolicyList()){
					if(!StringUtils.isEmpty(subState.getPolCode()) && StringUtils.equals(subState.getPolCode(), subPolicy.getSubPolCode())){
						subState.setPolNameChn(subPolicy.getSubPolName());
					}
				}
			}
			// 获取子要约的险种保费累计金额
			if (subState.getPremium() != null) {
				subState.setSumPremium(subState.getPremium());
				sumPremium += subState.getPremium();
			}
		}
		return sumPremium;
	}

	// 收费属组号校验
	private boolean checkFeeGrpNo(GrpInsurAppl grpInsurAppl, GrpInsured grpInsured) {
		if (grpInsured.getFeeGrpNo() == null || grpInsured.getFeeGrpNo() < 1) {
			return false;
		}
		if (grpInsurAppl.getIpsnPayGrpList() == null || grpInsurAppl.getIpsnPayGrpList().isEmpty()) {
			return false;
		}
		for (IpsnPayGrp ipsnPayGrp : grpInsurAppl.getIpsnPayGrpList()) {
			if (Long.compare(ipsnPayGrp.getFeeGrpNo(), grpInsured.getFeeGrpNo()) == 0) {
				return true;
			}
			;
		}
		return false;
	}

	// 添加打印用要约列表
	private void setIpsnStateList(GrpInsured grpInsured,GrpInsurAppl grpInsurAppl) {
		if (grpInsured.getSubStateList() == null) {
			return;
		}
		Map<String, IpsnState> ipsnStateMap = new HashMap<>();
		for (SubState subState : grpInsured.getSubStateList()) {
			if(StringUtils.isEmpty(subState.getPolCode())||subState.getPolCode().length()<3){
				continue;
			}
			if (ipsnStateMap.containsKey(subState.getPolCode().substring(0, 3))) {
				IpsnState ipsnState = ipsnStateMap.get(subState.getPolCode().substring(0, 3));
				ipsnState.setPremium(ipsnState.getPremium() + subState.getPremium());
				ipsnState.setFaceAmnt(ipsnState.getFaceAmnt() + subState.getFaceAmnt());
				ipsnState.setSumPremium(ipsnState.getSumPremium() + subState.getSumPremium());
			} else {
				IpsnState ipsnState = new IpsnState();
				ipsnState.setPolCode(subState.getPolCode().substring(0, 3));
				ipsnState.setPolNameChn(subState.getPolNameChn());
				ipsnState.setPremium(subState.getPremium());
				ipsnState.setFaceAmnt(subState.getFaceAmnt());
				ipsnState.setSumPremium(subState.getSumPremium());
				ipsnState.setClaimIpsnGrpNo(subState.getClaimIpsnGrpNo());
				ipsnStateMap.put(subState.getPolCode().substring(0, 3), ipsnState);
			}
		}
		List<IpsnState> ipsnStates = new ArrayList<>();
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			if(ipsnStateMap.get(policy.getPolCode())!=null){
				ipsnStateMap.get(policy.getPolCode()).setInsurDur(policy.getInsurDur());
				ipsnStateMap.get(policy.getPolCode()).setInsurDurUnit(policy.getInsurDurUnit());
				ipsnStateMap.get(policy.getPolCode()).setPolNameChn(policy.getPolNameChn());
				ipsnStates.add(ipsnStateMap.get(policy.getPolCode()));
			}
		}
		grpInsured.setIpsnStateList(ipsnStates);
	}
}
