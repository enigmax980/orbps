package com.newcore.orbps.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.ErrFileCreatService;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_USE;
import com.newcore.supports.dicts.IPSN_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MR_CODE;

/**
 * @author wangxiao
 * 创建时间：2016年8月29日上午10:51:21
 */
public class ErrorGrpInsuredPrintTasket implements Tasklet{
	private String applNo;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	ErrFileCreatService errFileCreatService;
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		List<ErrorGrpInsured> errorGrpInsureds = mongoBaseDao.find(ErrorGrpInsured.class, map);
		//判断是否有清单错误信息
		if(errorGrpInsureds != null && !errorGrpInsureds.isEmpty()){
			errFileCreatService.creatCsvFile(applNo);
			return RepeatStatus.FINISHED;
		}
		if(StringUtils.equals(grpInsurAppl.getAccessSource(),"GCSS") && !StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())){
			checkFaceAmntAndPremium();
			List<ErrorGrpInsured> errorGrpInsureds1 = mongoBaseDao.find(ErrorGrpInsured.class, map);
			//再次判断是否有清单错误信息
			if(errorGrpInsureds1 != null && !errorGrpInsureds1.isEmpty()){
				errFileCreatService.creatCsvFile(applNo);
			}
		}
		return RepeatStatus.FINISHED;
	}

	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}

	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	
	private void checkFaceAmntAndPremium() throws ParseException{
		HashMap<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		Criteria c=Criteria.where("applNo").is(applNo);
		GrpInsurAppl grpInsurAppl=(GrpInsurAppl)mongoBaseDao.findOne(GrpInsurAppl.class, map);
		StringBuilder ecseptionStr = new StringBuilder();
		// 被保人序号重复校验
		ecseptionStr.append(checkIpsnNo(applNo));
		// 被保人五要素校验
		ecseptionStr.append(checkFiveElements(applNo));
		// 保额保费校验
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(c), Aggregation.unwind("subStateList"),
				Aggregation.group("$subStateList.polCode").sum("subStateList.faceAmnt").as("sumFaceAmnt")
						.sum("subStateList.premium").as("sumPremium"));
		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();
		List<Policy> policylist = grpInsurAppl.getApplState().getPolicyList();
		
		Double sumFaceAmnt = grpInsurAppl.getApplState().getSumFaceAmnt();//总保额
		Double sumPremium = grpInsurAppl.getApplState().getSumPremium();//总保费	
		/* 对保单要约险种保费进行处理 */
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
				ecseptionStr.append("记入个人账户总金额[" + grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt() + "]跟被保人累计总保额[" + SumIpsnFaceAmnt+ "]不一致");
			}
			if (Math.abs(SumIpsnPremium - grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()) > 0.001) {
				ecseptionStr.append("个人账户总保费[" + grpInsurAppl.getFundInsurInfo().getIpsnFundPremium() + "]跟被保人累计总保费[" + SumIpsnPremium + "]不一致");
			}
		}else{
			/* 对险种保额保费校验 */
			for (Map.Entry<String, Double> entry : mapFaceAmnt.entrySet()) {
				if (mapIpsnFaceAmnt.get(entry.getKey()) == null) {
					mapIpsnFaceAmnt.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnFaceAmnt.get(entry.getKey())) > 0.001) {
					ecseptionStr.append("|险种[" + entry.getKey() + "]总保额[" + entry.getValue() + "]跟被保人险种累计总保额["
						+ mapIpsnFaceAmnt.get(entry.getKey()) + "]不一致|");
				}
			}
			for (Map.Entry<String, Double> entry : mapPremium.entrySet()) {
				if (mapIpsnPremium.get(entry.getKey()) == null) {
					mapIpsnPremium.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnPremium.get(entry.getKey())) > 0.001) {
					ecseptionStr.append("|险种[" + entry.getKey() + "]总保费" + entry.getValue() + "跟被保人险种累计总保费["
						+ mapIpsnPremium.get(entry.getKey()) + "]不一致|");
				}
			}
			/* 对子险种保额进行校验 */
			for (Map.Entry<String, Double> entry : mapFaceAmntSub.entrySet()) {
				if (mapIpsnFaceAmntSub.get(entry.getKey()) == null) {
					mapIpsnFaceAmntSub.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnFaceAmntSub.get(entry.getKey())) > 0.001) {
					ecseptionStr.append("|子险种[" + entry.getKey() + "]总保额[" + entry.getValue() + "]跟被保人子险种累计总保额["
						+ mapIpsnFaceAmntSub.get(entry.getKey()) + "]不一致|");
				}
			}
			for (Map.Entry<String, Double> entry : mapPremiumSub.entrySet()) {
				if (mapIpsnPremiumSub.get(entry.getKey()) == null) {
					mapIpsnPremiumSub.put(entry.getKey(), 0.0);
				}
				if (Math.abs(entry.getValue() - mapIpsnPremiumSub.get(entry.getKey())) > 0.001) {
					ecseptionStr.append("|子险种[" + entry.getKey() + "]总保费[" + entry.getValue() + "]跟被保人子险种累计总保费["
						+ mapIpsnPremiumSub.get(entry.getKey()) + "]不一致|");
				}
			}
			if (Math.abs(SumIpsnFaceAmnt - sumFaceAmnt) > 0.001) {
				ecseptionStr.append("|保单要约总保额[" + sumFaceAmnt + "]跟被保人累计总保额["
					+ SumIpsnFaceAmnt + "]不一致|");
			}
			if (Math.abs(SumIpsnPremium - sumPremium) > 0.001) {
				ecseptionStr.append("|保单要约总保费[" + sumPremium + "]跟被保人累计总保费["
					+ SumIpsnPremium + "]不一致|");
			}
		}

		// 分组属组人数校验
		if (grpInsurAppl.getIpsnStateGrpList() != null && !grpInsurAppl.getIpsnStateGrpList().isEmpty()) {
			for (IpsnStateGrp ipsnStateGrp : grpInsurAppl.getIpsnStateGrpList()) {
				if (StringUtils.equals(ipsnStateGrp.getIpsnGrpType(), "1")) {
					Aggregation aggregation1 = Aggregation
							.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo)),
									Aggregation.project("subStateList"), Aggregation.unwind("subStateList"),
									Aggregation.match(Criteria.where("subStateList.claimIpsnGrpNo")
											.is(ipsnStateGrp.getIpsnGrpNo())), // 属组号
							Aggregation.group("subStateList.polCode").count().as("num"));
					AggregationResults<JSONObject> result = mongoTemplate.aggregate(aggregation1, "grpInsured",
							JSONObject.class);
					List<JSONObject> mappedResults = result.getMappedResults();
					checkIpsnStateGrpList(ipsnStateGrp, ecseptionStr, mappedResults);
				}
			}
		}
		// 校验所有被保人的个人缴费金额总计+单位交费金额的总计=投保要约的总保费
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), "3")) {
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
					ecseptionStr.append("所有被保人的个人缴费金额总计与单位交费金额的总计之和:");
					ecseptionStr.append(sumPayAmount);
					ecseptionStr.append("投保要约的总保费:");
					ecseptionStr.append(sumPremium);
					ecseptionStr.append(";");
				}
			}
		}
		/* 被保人清单 与 保单要约 进行校验 */
		/* 对被保总人数人数进行校验 */
		long ipsnNum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(grpInsurAppl.getApplNo())),
				GrpInsured.class);
		if (ipsnNum != grpInsurAppl.getApplState().getIpsnNum()) {
			ecseptionStr.append(
					"|被保人清单中投保总人数[" + ipsnNum + "]与投保要约中投保总人数[" + grpInsurAppl.getApplState().getIpsnNum() + "]不一致!|");
		}
		/* 组织架构树 节点交费金额校验 */
		List<OrgTree> orgTrees = grpInsurAppl.getOrgTreeList();
		checkOrgTrees(orgTrees, applNo, ecseptionStr);
		if (!StringUtils.isEmpty(ecseptionStr.toString())) {
			ErrorGrpInsured errorGrpInsured = new ErrorGrpInsured();
			errorGrpInsured.setApplNo(applNo);
			errorGrpInsured.setIpsnNo(0l);
			errorGrpInsured.setRemark(ecseptionStr.toString() + "|请重新导入当前投保单号下所有清单数据");
			mongoBaseDao.insert(errorGrpInsured);
			mongoBaseDao.remove(GrpInsured.class, map);
		}
	}
	private void checkIpsnStateGrpList(IpsnStateGrp ipsnStateGrp,StringBuilder ecseptionStr,List<JSONObject> mappedResults){
		for(JSONObject jsonObj:mappedResults){
			long policyNum = jsonObj.getLong("num");
			if(Long.compare(policyNum, ipsnStateGrp.getIpsnGrpNum())!=0){
				ecseptionStr.append("分组："+ipsnStateGrp.getIpsnGrpNo()+" 团单要约属组人数："+ipsnStateGrp.getIpsnGrpNum()+" 清单累计属组人数："+policyNum+";");
			}
		}
	}
	//被保人序号重复校验
	private StringBuilder checkIpsnNo(String applNo){
		StringBuilder ecseptionStr = new StringBuilder();
		Criteria c=Criteria.where("applNo").is(applNo);
		Aggregation ipsnAggregation = Aggregation.newAggregation(
				Aggregation.match(c),
				Aggregation.group("ipsnNo").count().as("sum"),
				Aggregation.match(new Criteria("sum").gte(2))
		);
		AggregationResults<JSONObject> ipsnResult = mongoTemplate.aggregate(ipsnAggregation, "grpInsured", JSONObject.class);
		List<JSONObject> results = ipsnResult.getMappedResults();
		for(JSONObject result:results){
			ecseptionStr.append("|ipsnNo=");
			ecseptionStr.append(result.getString("_id"));
			ecseptionStr.append("被保人序号重复|");
		}
		return ecseptionStr;
	}
	//被保人五要素校验
	private StringBuilder checkFiveElements(String applNo) throws ParseException{
		StringBuilder err = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo",applNo);
		long num = mongoBaseDao.count(GrpInsured.class, map);
		if(num<=10000){
			Criteria criteria = new Criteria();
			criteria.and("applNo").is(applNo);
			Aggregation aggregationSub = Aggregation.newAggregation(
					Aggregation.match(criteria),
					Aggregation.project("ipsnBirthDate","ipsnNo","ipsnName","ipsnSex","ipsnIdType","ipsnIdNo"),
					Aggregation.group("ipsnIdNo","ipsnName","ipsnSex","ipsnIdType","ipsnBirthDate").count().as("Sum").push("ipsnNo").as("ipsnNo"),
					Aggregation.match(Criteria.where("Sum").gte(2))
					);							
			AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub, GrpInsured.class, JSONObject.class);
			for(JSONObject jsonObj:jsAggregationResultsSub.getMappedResults()){
				err.append("|ipsnNo=");
				err.append(jsonObj.getString("ipsnNo"));
				err.append(",ipsnName =");
				err.append(jsonObj.getString("ipsnName"));
				err.append(",ipsnSex =");
				err.append(jsonObj.getString("ipsnSex"));
				err.append(",ipsnBirthDate =");
				err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"),"yyyy-MM-dd"));
				err.append(",ipsnIdType =");
				err.append(jsonObj.getString("ipsnIdType"));
				err.append(",ipsnIdNo =");
				err.append(jsonObj.getString("ipsnIdNo"));
				err.append(",被保人五要素重复|");
			}
			return err;
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo)),
				Aggregation.project("ipsnSex").and("ipsnBirthDate").extractDayOfMonth().as("day").and("ipsnBirthDate").extractYear().as("year").and("ipsnBirthDate").extractMonth().as("month"),
				Aggregation.group("year","month","day","ipsnSex").count().as("Sum"),
				Aggregation.sort(Direction.ASC,"year","month")
				);
		AggregationResults<JSONObject> jsAggregationResults =  mongoTemplate.aggregate(aggregation, GrpInsured.class, JSONObject.class);
		long sum = 0;
		String beginDate = "1900-01-01";
		String endDate;
		for(int i = 0;i < jsAggregationResults.getMappedResults().size();i++){
			sum += jsAggregationResults.getMappedResults().get(i).getLong("Sum");
			if(sum >= 100000){
				JSONObject jsonObject = jsAggregationResults.getMappedResults().get(i-1);
				Criteria criteria = new Criteria();
				criteria.and("applNo").is(applNo);
				endDate = String.valueOf(jsonObject.getLong("year"))+"-"+String.valueOf(jsonObject.getLong("month"))+"-"+String.valueOf(jsonObject.getLong("day"));
				criteria.and("ipsnBirthDate").gte(sDateFormat.parseObject(beginDate)).lte(sDateFormat.parseObject(endDate));
				Aggregation aggregationSub = Aggregation.newAggregation(
						Aggregation.match(criteria),
						Aggregation.project("ipsnBirthDate","ipsnNo","ipsnName","ipsnSex","ipsnIdType","ipsnIdNo"),
						Aggregation.group("ipsnIdNo","ipsnName","ipsnSex","ipsnIdType","ipsnBirthDate").count().as("Sum").push("ipsnNo").as("ipsnNo"),
						Aggregation.match(Criteria.where("Sum").gte(2))
						);							
				AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub, GrpInsured.class, JSONObject.class);
				for(JSONObject jsonObj:jsAggregationResultsSub.getMappedResults()){
					err.append("|ipsnNo=");
					err.append(jsonObj.getString("ipsnNo"));
					err.append(",ipsnName =");
					err.append(jsonObj.getString("ipsnName"));
					err.append(",ipsnSex =");
					err.append(jsonObj.getString("ipsnSex"));
					err.append(",ipsnBirthDate =");
					err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"),"yyyy-MM-dd"));
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
		if(sum > 0 ){
			JSONObject jsonObject = jsAggregationResults.getMappedResults().get(jsAggregationResults.getMappedResults().size() - 1);
			Criteria criteria = new Criteria();
			criteria.and("applNo").is(applNo);
			endDate = String.valueOf(jsonObject.getLong("year"))+"-"+String.valueOf(jsonObject.getLong("month"))+"-"+String.valueOf(jsonObject.getLong("day"));
			criteria.and("ipsnBirthDate").gte(sDateFormat.parseObject(beginDate)).lte(sDateFormat.parseObject(endDate));
			Aggregation aggregationSub = Aggregation.newAggregation(
					Aggregation.match(criteria),
					Aggregation.project("ipsnBirthDate","ipsnNo","ipsnName","ipsnSex","ipsnIdType","ipsnIdNo"),
					Aggregation.group("ipsnIdNo","ipsnName","ipsnSex","ipsnIdType","ipsnBirthDate").count().as("Sum").push("ipsnNo").as("ipsnNo"),
					Aggregation.match(Criteria.where("Sum").gte(2))
					);							
			AggregationResults<JSONObject> jsAggregationResultsSub = mongoTemplate.aggregate(aggregationSub, GrpInsured.class, JSONObject.class);
			for(JSONObject jsonObj:jsAggregationResultsSub.getMappedResults()){
				err.append("|ipsnNo=");
				err.append(jsonObj.getString("ipsnNo"));
				err.append(",ipsnName =");
				err.append(jsonObj.getString("ipsnName"));
				err.append(",ipsnSex =");
				err.append(jsonObj.getString("ipsnSex"));
				err.append(",ipsnBirthDate =");
				err.append(DateFormatUtils.format(jsonObj.getDate("ipsnBirthDate"),"yyyy-MM-dd"));
				err.append(",ipsnIdType =");
				err.append(jsonObj.getString("ipsnIdType"));
				err.append(",ipsnIdNo =");
				err.append(jsonObj.getString("ipsnIdNo"));
				err.append(",被保人五要素重复|");
			}
		}
		return err;
	}
	
	private void checkOrgTrees(List<OrgTree> orgTrees, String applNo,StringBuilder errMsg) {

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
			return;
		}
		/* 第五校验规则 如果是缴费节点，必须有相应的被保人存在该缴费节点下 */
		for (Map.Entry<String, String> entry : mapIsPaid.entrySet()) {
			if (!mapStat.containsKey(entry.getKey())) {
				errMsg.append("层级代码[" + entry.getKey() + "]是缴费节点,但在被保人清单中没有被保人在该节点下|");
				return;
			}
		}
		/* 校验被保人清单中组织层级代码是否存在组织架构树中，如果存在校验其金额 */
		for (JSONObject jsonObject : mappedResults) {
			/* 第三校验规则 如果有一个被保人挂在节点上，所有被保人都必须挂在节点上 */
			if (StringUtils.isEmpty(jsonObject.getString("_id")) && mapStat.size() > 1) {
				errMsg.append("被保人清单中有被保人" + jsonObject.get("ipsnNoList") + "不存在组织层次代码|");
				continue;
			}
			if (!StringUtils.isEmpty(jsonObject.getString("_id")) && !mapLevel.containsKey(jsonObject.get("_id"))) {
				errMsg.append("组织架构树中不存在该[" + jsonObject.getString("_id") + "]组织层次代码|");
				continue;
			}
			/* 第六校验规则 如果该节点是缴费节点，挂在该节点下的被保人及单位保费金额就保存在该节点上 */
			if (mapIsPaid.containsKey(jsonObject.getString("_id"))) {
				if (null == jsonObject.get("sumGrpPayAmount")) {
					errMsg.append("该被保人清单中,层次代码为[" + jsonObject.getString("_id") + "]的清单无单位交费金额元素(grpPayAmount)|");
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
					continue;
				}
			}
			/* 第七校验规则 如果该节点是非缴费节点 ，挂在该节点下的被保人单位保费金额挂在最近的缴费父节点上 */
			if (mapNoPaid.containsKey(jsonObject.getString("_id"))) {
				/* 查找其父节点是否是缴费节点 */
				OrgTree orgTree = findPrioLevelCode(orgTrees, jsonObject.getString("_id"));
				if (null == orgTree || StringUtils.equals(orgTree.getLevelCode(), jsonObject.getString("_id"))) {
					errMsg.append("层级代码[" + jsonObject.getString("_id") + "]为非缴费节点,但其上级层级代码都不为缴费节点|");
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
}
