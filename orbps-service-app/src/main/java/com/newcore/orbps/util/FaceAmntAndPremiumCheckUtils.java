package com.newcore.orbps.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * @author wangxiao
 * 创建时间：2016年10月31日下午5:04:08
 */
public class FaceAmntAndPremiumCheckUtils {
	public RetInfo check(String applNo,MongoBaseDao mongoBaseDao,MongoTemplate mongoTemplate){
		RetInfo reInfo = new RetInfo();
		HashMap<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		
		//清单导入错误数据查询，有错误数据不进行校验
		List<ErrorGrpInsured> errorGrpInsureds = (List<ErrorGrpInsured>) mongoBaseDao.find(ErrorGrpInsured.class,map);
		if(null != errorGrpInsureds && !errorGrpInsureds.isEmpty()){
			reInfo.setApplNo(applNo);
			reInfo.setErrMsg("清单导入存在错误信息，请导出错误信息");
			reInfo.setRetCode("0");
			return reInfo;
		}
		
		Map<String, Double> polPremMap = new HashMap<>();
		Map<String, Double> polFaceMap = new HashMap<>();
		
		Criteria c=Criteria.where("applNo").is(applNo);
		GrpInsurAppl grpInsurAppl=(GrpInsurAppl)mongoBaseDao.findOne(GrpInsurAppl.class, map);
		StringBuilder ecseptionStr = new StringBuilder();
		//被保人序号重复校验
		Aggregation ipsnAggregation = Aggregation.newAggregation(
				Aggregation.match(c),
				Aggregation.group("ipsnNo").count().as("sum")
				);
		AggregationResults<JSONObject> ipsnResult = mongoTemplate.aggregate(ipsnAggregation, "grpInsured", JSONObject.class);
		List<JSONObject> results = ipsnResult.getMappedResults();
		for(JSONObject result:results){
			int sum = result.getIntValue("sum");
			if(sum>=2){
				ecseptionStr.append("ipsnNo=");
				ecseptionStr.append(result.getString("_id"));
				ecseptionStr.append("|");
			}
		}
		if(!StringUtils.isEmpty(ecseptionStr.toString())){
			ecseptionStr.append("被保人序号重复");
		}
		
		//被保人五要素校验
		StringBuilder elementsError = new StringBuilder();
		Aggregation elementsAggregation = Aggregation.newAggregation(
				Aggregation.match(c),
				Aggregation.group("ipsnName","ipsnSex","ipsnBirthDate","ipsnIdType","ipsnIdCode").count().as("sum")
				);
		AggregationResults<JSONObject> elementsResult = mongoTemplate.aggregate(elementsAggregation, "grpInsured", JSONObject.class);
		List<JSONObject> eleResults = elementsResult.getMappedResults();
		for(JSONObject result:eleResults){
			int sum = result.getIntValue("sum");
			if(sum>=2){
				elementsError.append("ipsnIdCode=");
				elementsError.append(result.getString("ipsnIdCode"));
				elementsError.append("|");
			}
		}
		if(!StringUtils.isEmpty(elementsError.toString())){
			ecseptionStr.append(elementsError);
			ecseptionStr.append("被保人五要素重复");
		}
		
		//保额保费校验
		List<Policy> policylist  = grpInsurAppl.getApplState().getPolicyList();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(c),
				Aggregation.unwind("subStateList"),
				Aggregation.group("$subStateList.polCode").sum("subStateList.faceAmnt").as("sumFaceAmnt").sum("subStateList.premium").as("sumPremium")
				);
		AggregationResults<GrpInsured> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", GrpInsured.class);
		DBObject rawResults = aggregate.getRawResults();
		Object object = rawResults.get("result");
		String string =object.toString();
		JSONArray array = JSON.parseArray(string);
		JSONObject jsonObject;
		for (Object object2 : array) {
			jsonObject= JSON.parseObject(object2.toString());
			polPremMap.put(jsonObject.get("_id").toString(), Double.parseDouble(jsonObject.get("sumPremium").toString()));
			polFaceMap.put(jsonObject.get("_id").toString(), Double.parseDouble(jsonObject.get("sumFaceAmnt").toString()));
		}
		for (Policy policy : policylist) {
			if (null!=policy.getSubPolicyList() && !policy.getSubPolicyList().isEmpty()) {
				List<SubPolicy> subPolicyList = policy.getSubPolicyList();
				checkPremiumFaceAmnt(subPolicyList, ecseptionStr, polPremMap, polFaceMap);
			}else{
				if(polPremMap.get(policy.getPolCode())-policy.getPremium()>0.0001
					||polPremMap.get(policy.getPolCode())-policy.getPremium()<-0.0001){
					ecseptionStr.append("险种："+policy.getPolCode()+"累计保费"+polPremMap.get(policy.getPolCode())+";");
					ecseptionStr.append("保单基本信息："+policy.getPolCode()+"保费"+policy.getPremium()+";");
				}
				if(polFaceMap.get(policy.getPolCode())-policy.getFaceAmnt()>0.0001
					||polFaceMap.get(policy.getPolCode())-policy.getFaceAmnt()<-0.0001){
					ecseptionStr.append("险种："+policy.getPolCode()+"累计保额"+polFaceMap.get(policy.getPolCode())+";");
					ecseptionStr.append("保单基本信息："+policy.getPolCode()+"保额"+policy.getFaceAmnt()+";");
				}
			}
		}
		
		//分组属组人数校验
		if(grpInsurAppl.getIpsnStateGrpList()!=null
			&& !grpInsurAppl.getIpsnStateGrpList().isEmpty()){
			for(IpsnStateGrp ipsnStateGrp:grpInsurAppl.getIpsnStateGrpList()){
				if(StringUtils.equals(ipsnStateGrp.getIpsnGrpType(),"1")){
					Aggregation aggregation1 = Aggregation.newAggregation(
						Aggregation.match(Criteria.where("applNo").is(applNo)),
						Aggregation.project("subStateList"),
						Aggregation.unwind("subStateList"),
						Aggregation.match(Criteria.where("subStateList.claimIpsnGrpNo").is(ipsnStateGrp.getIpsnGrpNo())),//属组号
						Aggregation.group("subStateList.polCode").sum("subStateList.faceAmnt").as("sumfaceAmnt").
						sum("subStateList.premium").as("sumpremium").count().as("num")			
					);
					AggregationResults<JSONObject> result = mongoTemplate.aggregate(aggregation1, "grpInsured", JSONObject.class);
					List<JSONObject> mappedResults = result.getMappedResults();
					checkIpsnStateGrpList(ipsnStateGrp, ecseptionStr, mappedResults);
				}
			}
		}
		//校验所有被保人的个人缴费金额总计+单位交费金额的总计=投保要约的总保费
		if(StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),"3")){
			Aggregation aggregation2 = Aggregation.newAggregation(
					Aggregation.match(c),
					Aggregation.project("applNo").andExpression("ipsnPayAmount + grpPayAmount").as("num"),
					Aggregation.group("applNo").sum("num").as("sum")
					);	
			AggregationResults<Object> result = mongoTemplate.aggregate(aggregation2, "grpInsured", Object.class);
			List<Object> resultList = result.getMappedResults();
			HashMap<String, Object> payAmountMap = (HashMap<String, Object>) resultList.get(0);
			Double sumPayAmount = (Double) payAmountMap.get("sum");
			if(grpInsurAppl.getApplState().getSumPremium().compareTo(sumPayAmount)>0.0001
				||grpInsurAppl.getApplState().getSumPremium().compareTo(sumPayAmount)<-0.0001){
				ecseptionStr.append("所有被保人的个人缴费金额总计与单位交费金额的总计之和:");
				ecseptionStr.append(sumPayAmount);
				ecseptionStr.append("投保要约的总保费:");
				ecseptionStr.append(grpInsurAppl.getApplState().getSumPremium());
				ecseptionStr.append(";");
			}
		}
		
		if(!StringUtils.isEmpty(ecseptionStr.toString())){
			reInfo.setErrMsg(ecseptionStr.toString()+"|请重新导入当前投保单号下所有清单数据");
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			return reInfo;
		}
		reInfo.setApplNo(applNo);
		reInfo.setRetCode("1");
		return reInfo;
	}
	private void checkPremiumFaceAmnt(List<SubPolicy> subPolicyList,StringBuilder ecseptionStr,Map<String, Double> polPremMap,Map<String, Double> polFaceMap){
		for (SubPolicy subPolicy : subPolicyList) {
			if(polPremMap.get(subPolicy.getSubPolCode())-subPolicy.getSubPremium()>0.0001
				|| polPremMap.get(subPolicy.getSubPolCode())-subPolicy.getSubPremium()<-0.0001){
				ecseptionStr.append("子险种："+subPolicy.getSubPolCode()+"累计保费"+polPremMap.get(subPolicy.getSubPolCode())+";");
				ecseptionStr.append("保单基本信息："+subPolicy.getSubPolCode()+"保费"+subPolicy.getSubPremium()+";");
			}
			if(polFaceMap.get(subPolicy.getSubPolCode())-subPolicy.getSubPolAmnt()>0.0001
				|| polFaceMap.get(subPolicy.getSubPolCode())-subPolicy.getSubPolAmnt()<-0.0001){
				ecseptionStr.append("子险种："+subPolicy.getSubPolCode()+"累计保额"+polFaceMap.get(subPolicy.getSubPolCode())+";");
				ecseptionStr.append("保单基本信息："+subPolicy.getSubPolCode()+"保额"+subPolicy.getSubPolAmnt()+";");
			}
		}
	}
	private void checkIpsnStateGrpList(IpsnStateGrp ipsnStateGrp,StringBuilder ecseptionStr,List<JSONObject> mappedResults){
		for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
			for(JSONObject jsonObj:mappedResults){
				String polCode = jsonObj.getString("_id");
				double sumfaceAmnt = jsonObj.getDouble("sumfaceAmnt");
				double sumpremium = jsonObj.getDouble("sumpremium");
				long policyNum = jsonObj.getLong("num");
				if(
					StringUtils.equals(polCode, grpPolicy.getPolCode())
					&& (sumfaceAmnt-grpPolicy.getFaceAmnt()>0.0001
						||sumfaceAmnt-grpPolicy.getFaceAmnt()<-0.0001)
				){
					ecseptionStr.append("分组："+ipsnStateGrp.getIpsnGrpNo()+" 险种："+polCode+" 团单险种保额："+grpPolicy.getFaceAmnt()+" 清单累计保额："+sumfaceAmnt+";");
				}
				else if(
					StringUtils.equals(polCode, grpPolicy.getPolCode())
					&& (sumpremium-grpPolicy.getPremium()>0.0001
						||sumpremium-grpPolicy.getPremium()<-0.0001)
				){
					ecseptionStr.append("分组："+ipsnStateGrp.getIpsnGrpNo()+" 险种："+polCode+" 团单实际保费："+grpPolicy.getPremium()+" 清单累计保费："+sumpremium+";");
				}
				if(Long.compare(policyNum, ipsnStateGrp.getIpsnGrpNum())!=0){
					ecseptionStr.append("分组："+ipsnStateGrp.getIpsnGrpNo()+" 团单要约属组人数："+ipsnStateGrp.getIpsnGrpNum()+" 清单累计属组人数："+policyNum+";");
				}
			}
		}
	}
}
