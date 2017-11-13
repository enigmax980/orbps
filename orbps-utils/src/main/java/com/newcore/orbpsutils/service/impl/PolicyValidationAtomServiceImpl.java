package com.newcore.orbpsutils.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.ipms.api.InsurSummaryInfoService;
import com.newcore.orbps.service.ipms.api.MoneyinInfoQueryService;
import com.newcore.orbps.service.ipms.api.PolicyCatStdService;
import com.newcore.orbps.service.ipms.api.PolicyDurService;
import com.newcore.orbps.service.ipms.api.PsnInfoService;
import com.newcore.orbps.service.ipms.api.ShortInurDurService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.orbpsutils.service.api.PolicyValidationAtomService;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.DUR_UNIT;
import com.newcore.supports.dicts.SALES_TAGET;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author huanglong
 * @date 2017年1月9日
 * @content policy险种校验原子服务
 */
@Service("policyValidationAtomService")
public class PolicyValidationAtomServiceImpl implements PolicyValidationAtomService {

	@Autowired
	PolicyDurService resulpolicyDurService;
	@Autowired
	MoneyinInfoQueryService resulmoneyinInfoQueryService;
	@Autowired
	PsnInfoService resulPsnInfoService;
	@Autowired
	SubPolicyService resulsubPolicyService;
	@Autowired
	PolicyCatStdService resulPolicyCatStdService;
	@Autowired
	InsurSummaryInfoService resulInsurSummaryInfoService;
	@Autowired
	ShortInurDurService resulshortInurDurService;
	@Autowired
	ConfigDao configDao;

	private static final String PROPERTIE_TYPE = "POL_PRINT_CONTROL";
	private final Logger logger = LoggerFactory.getLogger(PolicyValidationAtomServiceImpl.class);

	@Override
	public int ValidaPolPeriod(Policy policy) {

		logger.error("入参:" + JSON.toJSONString(policy));
		if (null == policy) {
			return -1;
		}
		if (StringUtils.isEmpty(policy.getPolCode())) {
			logger.error("入参险种为空!");
			return -1;
		}
		if (null == policy.getInsurDur()) {
			logger.error("保险期间为空!");
			return -1;
		}
		if (StringUtils.isEmpty(policy.getInsurDurUnit())) {
			logger.error("保险期类型!");
			return -1;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("polCode", policy.getPolCode());
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		map = resulpolicyDurService.excute(map);
		if (null == map || (map.containsKey("errorCode") && "8888".equals(map.get("errorCode")))) {
			logger.error("查询不到数据!");
			return -1;
		}
		int flag = -1;
		JSONArray jsonArray = (JSONArray) map.get("policyDurBos");
		for (int i = 0; null != jsonArray && i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			if (StringUtils.equals(object.getString("durUnit"), DUR_UNIT.CONVENTION.getKey())) {
				CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo1);
				Map<String, Object> map1 = new HashMap<>();
				map1.put("polCode", policy.getPolCode());
				map1 = resulshortInurDurService.excute(map1);
				if(null == map1 || (map1.containsKey("errorCode") && "8888".equals(map1.get("errorCode")))){
					logger.error("查询不到数据!");
				}
				JSONObject jsonObject = (JSONObject) map1.get("policyDurBo");
				int insurFrom = jsonObject.getIntValue("insurDurFrom");
				int insurDurTo = jsonObject.getIntValue("insurDurTo");
				String insurDurUnit = jsonObject.getString("insurDurUnit");
				if(!StringUtils.equals(policy.getInsurDurUnit(), insurDurUnit)){
					logger.error("保险期间类型不符");
					return -1;
				}
				if(policy.getInsurDur() < insurFrom || policy.getInsurDur() > insurDurTo){
					logger.error("保险期间不符");
					return -1;			
				}
				return 1;
			}
			if (StringUtils.equals(policy.getInsurDurUnit(), object.getString("durUnit"))
					&& policy.getInsurDur() == Integer.valueOf(object.getString("durAmnt"))) {
				flag = 0;
			}
			if(StringUtils.equals(object.getString("durUnit"), "B")){
				return 2;
			}
		}			
		return flag;
	}

	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return boolean
	 * @content 校验险种是否包含现有缴费方式
	 */
	@Override
	public boolean ValidaPayType(String polCode, String moneyinItrvl) {
		if (StringUtils.isEmpty(polCode)) {
			logger.error("入参险种为空!");
			return false;
		}
		if (StringUtils.isEmpty(moneyinItrvl)) {
			logger.error("缴费方式!");
			return false;
		}
		Map<String, Object> mapQuery = new HashMap<>();
		mapQuery.put("polCode", polCode);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		mapQuery = resulmoneyinInfoQueryService.excute(mapQuery);
		if (null == mapQuery || (mapQuery.containsKey("errorCode") && "8888".equals(mapQuery.get("errorCode")))) {
			logger.error("查询不到数据!");
			return false;
		}
		JSONArray jsonArray = (JSONArray) mapQuery.get("moneyinInfoBos");
		for (int i = 0; null != jsonArray && i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (StringUtils.equals(moneyinItrvl, jsonObject.getString("payItrvlCode"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return Map<String,String>
	 * @content 查询险种下子险的互斥子险种
	 */
	@Override
	public Map<String, String> ValidaMutex(String polSubCode) {
		Map<String, String> mapMutex = new HashMap<>();
		// if(StringUtils.isEmpty(polSubCode)){
		// logger.error("入参polCode为空!");
		// return mapMutex;
		// }
		// Map<String, Object> mapQuery = new HashMap<>();
		// mapQuery.put("polCode", polSubCode.substring(0, 3));
		// CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		// HeaderInfoHolder.setOutboundHeader(headerInfo);
		// mapQuery = resulsubPolicyService.excute(mapQuery);
		// if(null == mapQuery || (mapQuery.containsKey("errorCode") &&
		// "8888".equals(mapQuery.get("errorCode")))){
		// logger.error("查询不到数据!");
		// return mapMutex;
		// }
		// /*此子险互斥校验需遵循以下条件 sMCode="2" 是多选，sMCode="1"单选 ，sMGroupId组号
		// * 1 多选 ：组内共享，组外互斥
		// * 2 单选 ：组内互斥，组外共享
		// * 3 一个子险 只存在一个组中
		// * 4 一个组多选 单选属性 只有其一
		// * 5 多选：子险种分成 可选 、必选 两个大类组，同一个大类组内不是一个组号的都是互斥。
		// * */
		// Map<String, String> mapSMGroupId = new HashMap<>();
		// Map<String, String> mapChoose = new HashMap<>();//多选可选组
		// Map<String, String> mapMust = new HashMap<>();//多选必须组
		// String sMGroupIdSelf = "";/*组*/
		// String sMCodeSelf ="";/*单选多选属性*/
		// String saleServeOptionSelf="";
		// JSONArray jsonArray = (JSONArray)mapQuery.get("subPolicyBos");
		// /*对互斥数据处理*/
		// for(int i=0;null != jsonArray && i< jsonArray.size();i++){
		// JSONObject jsonObject = jsonArray.getJSONObject(i);
		// JSONObject subPolicyRelation =
		// jsonObject.getJSONObject("subPolicyRelation");
		// if(null == subPolicyRelation){
		// continue;
		// }
		// if(StringUtils.equals(jsonObject.getString("saleServeOption"),SALE_SERVE_OPTION.SELECT_SUB_POL.getKey())
		// && StringUtils.equals(subPolicyRelation.getString("sMCode"), "2")){
		// mapChoose.put(jsonObject.getString("subPolCode"),
		// subPolicyRelation.getString("sMGroupId"));
		// }
		// if(StringUtils.equals(jsonObject.getString("saleServeOption"),SALE_SERVE_OPTION.MUST_SUB_POL.getKey())
		// && StringUtils.equals(subPolicyRelation.getString("sMCode"), "2")){
		// mapMust.put(jsonObject.getString("subPolCode"),
		// subPolicyRelation.getString("sMGroupId"));
		// }
		// if(mapSMGroupId.containsKey(subPolicyRelation.getString("sMGroupId"))){
		// mapSMGroupId.put(subPolicyRelation.getString("sMGroupId"),
		// mapSMGroupId.get(subPolicyRelation.getString("sMGroupId"))
		// +jsonObject.getString("subPolCode") +"|");
		// }else{
		// mapSMGroupId.put(subPolicyRelation.getString("sMGroupId"),jsonObject.getString("subPolCode")
		// +"|");
		// }
		// if(StringUtils.equals(jsonObject.getString("subPolCode"),
		// polSubCode)){
		// sMGroupIdSelf = subPolicyRelation.getString("sMGroupId");
		// sMCodeSelf = subPolicyRelation.getString("sMCode");
		// saleServeOptionSelf = jsonObject.getString("saleServeOption");
		// }
		// }
		// if(StringUtils.equals("1", sMCodeSelf)){
		// /*查找组内互斥的险种*/
		// String [] str = mapSMGroupId.get(sMGroupIdSelf).split("\\|");
		// for(int i = 0;i< str.length;i++){
		// if(!StringUtils.equals(polSubCode, str[i])){
		// mapMutex.put(str[i], "");
		// }
		// }
		// }else if(StringUtils.equals(saleServeOptionSelf,
		// SALE_SERVE_OPTION.MUST_SUB_POL.getKey())){
		// for(Map.Entry<String, String> entry:mapMust.entrySet()){
		// if(!StringUtils.equals(entry.getValue(), sMGroupIdSelf)){
		// mapMutex.put(entry.getKey(), "");
		// }
		// }
		// }else if (StringUtils.equals(saleServeOptionSelf,
		// SALE_SERVE_OPTION.SELECT_SUB_POL.getKey())) {
		// for(Map.Entry<String, String> entry:mapChoose.entrySet()){
		// if(!StringUtils.equals(entry.getValue(), sMGroupIdSelf)){
		// mapMutex.put(entry.getKey(), "");
		// }
		// }
		// }
		return mapMutex;
	}

	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return RetInfo
	 * @content 用于被保人年龄性别 是否符合险种要求的校验
	 */
	@Override
	public RetInfo ValidaPolAndIpsn(GrpInsured grpInsured) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuilder errMsg = new StringBuilder();
		if (null == grpInsured) {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("被保人信息为空!");
			return retInfo;
		}
		if (grpInsured.getSubStateList() == null) {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("对应被保人信息中要约分组号不正确或子要约不存在!");
			return retInfo;
		}
		Map<String, String> mapPolCode = new HashMap<>();
		for (SubState subState : grpInsured.getSubStateList()) {
			mapPolCode.put(subState.getPolCode().substring(0, 3), "");
		}
		for (Map.Entry<String, String> entry : mapPolCode.entrySet()) {
			RetInfo retInfoRet = ValidaSinglePolIpsn(entry.getKey(), grpInsured);
			if (null != retInfoRet && StringUtils.equals("0", retInfoRet.getRetCode())) {
				errMsg.append(retInfoRet.getErrMsg());
				errMsg.append("|");
				retInfo.setRetCode("0");
			}
		}
		if (StringUtils.equals("0", retInfo.getRetCode())) {
			retInfo.setErrMsg(errMsg.toString());
		} else {
			retInfo.setErrMsg("被保人性别符合所有险种要求");
		}
		return retInfo;
	}

	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return Map<String,String>
	 * @content 一级分类-保险101 二类分类-长期201 短期201 三级分类-团体301 个人302 四级分类-寿险401 养老年金402
	 *          健康险403 意外险404 五级分类-定期寿险501 终身险502 年金503 两全504 医疗险 505 重大疾病险506
	 *          失能险507 护理险508 六级分类-费用报销601 定额给付602 附加分类-普通型901 投连型902 分红型903
	 *          万能型904 变额年金905 详见 CAT_CODE枚举类
	 */
	@Override
	public Map<String, String> ValidaPolType(String polCode) {
		Map<String, String> mapPolType = new HashMap<>();
		if (StringUtils.isEmpty(polCode)) {
			return mapPolType;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("polCode", polCode.substring(0, 3));
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		map = resulPolicyCatStdService.excute(map);
		if (null == map || (map.containsKey("errorCode") && "8888".equals(map.get("errorCode")))) {
			logger.error("查询不到数据!");
			return mapPolType;
		}
		JSONArray jsonArray = (JSONArray) map.get("policyCatStdBos");
		for (int i = 0; null != jsonArray && i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			mapPolType.put(jsonObject.getString("catLvl"), jsonObject.getString("catCode"));
		}
		return mapPolType;
	}

	/**
	 * @author huanglong
	 * @date 2017年1月12日
	 * @param PolicyValidationAtomService
	 * @return Map<String,String>
	 * @content 险种承保约束信息 可查询: 险种承保约束信息 可查询: 销售对象salesTaget 被保人数量 ipsnCount
	 *          是否允许单独投保applSingleFlag 承保来源insurSource 是否存在人员约束ipsnLimitFlag
	 *          及其产品该服务所有返回值
	 */
	@Override
	public Map<String, Object> ValidaSalesLimit(String polCode) {
		Map<String, Object> mapSalesLimit = new HashMap<>();
		if (StringUtils.isEmpty(polCode)) {
			return mapSalesLimit;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("polCode", polCode.substring(0, 3));
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		map = resulInsurSummaryInfoService.excute(map);
		if (null == map || (map.containsKey("errorCode") && "8888".equals(map.get("errorCode")))) {
			logger.error("查询不到数据!");
			return mapSalesLimit;
		}
		JSONObject insurSummaryInfoBo = (JSONObject) map.get("insurSummaryInfoBo");
		insurSummaryInfoBo.keySet();
		// 遍历jsonObject数据，添加到Map对象
		for (String key : insurSummaryInfoBo.keySet()) {
			mapSalesLimit.put(key, insurSummaryInfoBo.get(key));
		}
		return mapSalesLimit;
	}

	@Override
	public RetInfo ValidaApplState(GrpInsurAppl grpInsurAppl) {
		/*--1.所有险种的保险期单位必须一样；
		  --2.所有险种的保险期间必须小于等于第一主险；
		  --3.对所有险种的保险期间、单位的合法性校验； （包含约定）
		  --4.含有子险种的，对子险种的互斥做校验；
		  --5.对交费方式校验，所有险种都必须包含所选择的交费方式，例如：选月交的必须都含有月交；
		 */
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		if (null == grpInsurAppl) {
			retInfo.setErrMsg("入参为空");
			retInfo.setRetCode("0");
			return retInfo;
		}
		if (null == grpInsurAppl.getPaymentInfo()) {
			retInfo.setErrMsg("缴费相关paymentInfo为空");
			retInfo.setRetCode("0");
			return retInfo;
		}
		/* 对要约数据进行处理 */
		String moneyinItrvl = grpInsurAppl.getPaymentInfo().getMoneyinItrvl();
		ApplState applState = grpInsurAppl.getApplState();
		if (null == applState) {
			retInfo.setErrMsg("要约为空");
			retInfo.setRetCode("0");
			return retInfo;
		}

		String insurDurUnit = "";
		int insurDur = 0;
		int flag = 0;
		StringBuilder errMsg = new StringBuilder();
		List<Policy> policies = applState.getPolicyList();
		for (int i = 0; null != policies && i < policies.size(); i++) {
			Policy policy = policies.get(i);
			errMsg.append("险种" + policy.getPolCode() + ":");
			if (policy.getPolIpsnNum() > applState.getIpsnNum()) {
				errMsg.append("险种承保人数" + policy.getPolIpsnNum() + "大于要约承保人数" + applState.getIpsnNum() + "|");
				retInfo.setRetCode("0");
			}
			if (i == 0) {
				insurDurUnit = policy.getInsurDurUnit();
				insurDur = policy.getInsurDur();
				flag = ValidaPolPeriod(policy);
				if (-1 == flag) {
					errMsg.append("不支持该保险期类型:" + insurDurUnit + "与保险期间:" + insurDur + "|");
					retInfo.setErrMsg(errMsg.toString());
					retInfo.setRetCode("0");
					return retInfo;
				}
				if (!ValidaPayType(policy.getPolCode(), moneyinItrvl)) {
					errMsg.append("不支持该缴费方式:" + moneyinItrvl + "|");
					retInfo.setErrMsg(errMsg.toString());
					retInfo.setRetCode("0");
					return retInfo;
				}
			}
			String polType = StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())
					? CNTR_TYPE.PERSON_INSUR.getKey() : grpInsurAppl.getCntrType();
			Map<String, Object> mapSalesLimit = ValidaSalesLimit(policy.getPolCode());
			if (!StringUtils.equals(polType, (String) mapSalesLimit.get("salesTaget"))
					&& !StringUtils.equals(SALES_TAGET.BOTH_SALE.getKey(), (String) mapSalesLimit.get("salesTaget"))) {
				errMsg.append("该险种销售对象为" + mapSalesLimit.get("salesTagetDesc") + ",保单不支持"
						+ mapSalesLimit.get("salesTagetDesc") + "|");
				retInfo.setRetCode("0");
			}
			if (0 == flag && !StringUtils.equals(policy.getInsurDurUnit(), insurDurUnit)) {
				errMsg.append("保险期类型与主险不一致|");
				retInfo.setRetCode("0");
			}
			if(2 == flag  && !StringUtils.equals(policy.getInsurDurUnit(), insurDurUnit)){
				errMsg.append("保险期类型与主险不一致|");
				retInfo.setRetCode("0");
			}
			if(0 == flag && policy.getInsurDur() > insurDur){
				errMsg.append("保险期期间大于主险期间|");
				retInfo.setRetCode("0");
			}
			if(2 == flag && (policy.getInsurDur() > insurDur || policy.getInsurDur() < insurDur)){
				errMsg.append("保险期期间不等于主险期间|");
				retInfo.setRetCode("0");				
			}
			if(!ValidaPayType(policy.getPolCode(), moneyinItrvl)){
				errMsg.append("不存在该缴费方式:"+moneyinItrvl+"|");
				retInfo.setErrMsg(errMsg.toString());
				retInfo.setRetCode("0");
			}
			/* 子险种互斥校验 */
			Map<String, String> mapSubPol = new HashMap<>();
			Map<String, String> mapSubPolResult = new HashMap<>();
			List<SubPolicy> subPolicies = policy.getSubPolicyList();
			for (int j = 0; null != subPolicies && j < subPolicies.size(); j++) {
				mapSubPol.put(subPolicies.get(j).getSubPolCode(), "");
			}

			for (Map.Entry<String, String> entry : mapSubPol.entrySet()) {
				Map<String, String> mapMutex = ValidaMutex(entry.getKey());
				for (Map.Entry<String, String> entryMutex : mapMutex.entrySet()) {
					if (mapSubPol.containsKey(entryMutex.getKey())) {
						mapSubPolResult.put(entry.getKey(), StringUtils.isEmpty(entry.getValue()) ? entryMutex.getKey()
								: (entry.getValue() + entryMutex.getKey() + "|"));
					}
				}
			}
			if (!mapSubPolResult.isEmpty()) {
				for (Map.Entry<String, String> entry : mapSubPolResult.entrySet()) {
					errMsg.append("子险种 " + entry.getKey() + "存在互斥子险种:" + entry.getValue() + "|");
					retInfo.setRetCode("0");
				}
			}
			if (StringUtils.equals("1", retInfo.getRetCode())) {
				errMsg.append("检验成功!");
			}
		}
		if (StringUtils.equals(retInfo.getRetCode(), "0")) {
			retInfo.setErrMsg(errMsg.toString());
		} else {
			retInfo.setErrMsg("要约险种校验通过!");
		}
		return retInfo;
	}

	@SuppressWarnings("deprecation")
	private RetInfo ValidaSinglePolIpsn(String polCode, GrpInsured grpInsured) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuilder errMsg = new StringBuilder();
		errMsg.append("被保人序号" + grpInsured.getIpsnNo() + "与险种" + polCode + "的校验结果:");
		Map<String, Object> mapQuery = new HashMap<>();
		mapQuery.put("polCode", polCode);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		mapQuery = resulPsnInfoService.excute(mapQuery);
		if (null == mapQuery || (mapQuery.containsKey("errorCode") && "8888".equals(mapQuery.get("errorCode")))) {
			errMsg.append("查询不到险种数据。");
			retInfo.setRetCode("0");
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}
		JSONArray jsonArray = (JSONArray) JSONObject.parseObject(JSONObject.toJSONString(mapQuery.get("psnInfoBo")))
				.get("psnCtrlList");
		if (null == jsonArray) {
			errMsg.append("查询不到险种数据。");
			retInfo.setRetCode("0");
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}
		boolean sexFlag = false;
		for (int i = 0; null != jsonArray && i < jsonArray.size(); i++) {
			if (!StringUtils.equals("M", jsonArray.getJSONObject(i).getString("psnTypeCode").substring(0, 1))) {
				continue;
			}
			if (StringUtils.equals(grpInsured.getIpsnSex(), jsonArray.getJSONObject(i).getString("sex"))
					|| StringUtils.equals(SEX.BOTH.getKey(), jsonArray.getJSONObject(i).getString("sex"))) {
				sexFlag = true;
			}
			// 被保人年龄校验
			Date sysDate = new Date();
			String maxAgeUnit = jsonArray.getJSONObject(i).getString("maxAgeUnit");
			Integer maxAge = jsonArray.getJSONObject(i).getIntValue("maxAge");
			int temp = 0;
			switch (StringUtils.isEmpty(maxAgeUnit) ? "A" : maxAgeUnit) {
			case "A":
				temp = sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear();
				if (temp > 0) {
					int add = sysDate.getMonth() > grpInsured.getIpsnBirthDate().getMonth() ? 0
							: (sysDate.getMonth() < grpInsured.getIpsnBirthDate().getMonth() ? -1
									: (sysDate.getDate() >= grpInsured.getIpsnBirthDate().getDate() ? 0 : -1));
					temp += add;
				}
				break;
			case "M":
				temp = (sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear()) * 12
						+ (sysDate.getMonth() - grpInsured.getIpsnBirthDate().getMonth());
				break;
			case "D":
				temp = (int) (sysDate.getTime() - grpInsured.getIpsnBirthDate().getTime()) / (3600 * 24);
				break;

			default:
				break;
			}
			if (temp > (null == maxAge ? 99 : maxAge)) {
				errMsg.append("被保人年龄大于险种允许最大年龄限制,");
			}

			String minAgeUnit = jsonArray.getJSONObject(i).getString("minAgeUnit");
			Integer minAge = jsonArray.getJSONObject(i).getIntValue("minAge");
			temp = 0;
			switch (StringUtils.isEmpty(minAgeUnit) ? "A" : minAgeUnit) {
			case "A":
				int add = sysDate.getMonth() > grpInsured.getIpsnBirthDate().getMonth() ? 0
						: (sysDate.getMonth() < grpInsured.getIpsnBirthDate().getMonth() ? -1
								: (sysDate.getDate() >= grpInsured.getIpsnBirthDate().getDate() ? 0 : -1));
				temp = sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear();
				temp = temp > 0 ? temp + add : temp;
				break;
			case "M":
				temp = (sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear()) * 12
						+ (sysDate.getMonth() - grpInsured.getIpsnBirthDate().getMonth());
				break;
			case "D":
				temp = (int) (sysDate.getTime() - grpInsured.getIpsnBirthDate().getTime()) / (3600 * 24);
				break;
			default:
				break;
			}
			if (temp < (null == minAge ? 0 : minAge)) {
				errMsg.append("被保人年龄小于险种允许最小年龄限制,");
			}
		}
		/* 年龄不做限制 */
		if (sexFlag) {
			retInfo.setErrMsg("被保人性别符合险种要求!");
			retInfo.setRetCode("1");
		} else {
			retInfo.setRetCode("0");
			errMsg.append("被保人性别不符合险种要求!");
			retInfo.setErrMsg(errMsg.toString());
		}
		return retInfo;
	}

	@Override
	public String ValidaPolConfig(List<Policy> policyList) {
		StringBuilder errMsg = new StringBuilder();
		// 查询规则是否打开 如果规则 configState = Y 则进行险种校验
		String configState = configDao.queryPropertiesConfigure(PROPERTIE_TYPE, null, null);
		if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), configState)) {
			for (Policy policy : policyList) {
				String policyState = configDao.queryPropertiesConfigure(PROPERTIE_TYPE, policy.getPolCode(), null);
				// 检测所有险种是否存在险种表中 如果返回等于Y 则跳过 否则组成错误提示
				if (!StringUtils.equals(YES_NO_FLAG.YES.getKey(), policyState)) {
					if (StringUtils.isEmpty(errMsg)) {
						errMsg.append("险种" + policy.getPolCode());
					} else {
						errMsg.append("," + policy.getPolCode());
					}
				}
			}
			if (!StringUtils.isEmpty(errMsg)) {
				errMsg.append(",暂不支持纸质打印");
			}
		}
		return errMsg.toString();
	}

	public static void main(String[] args) {
		StringBuilder errMsg = new StringBuilder();
		errMsg.append("dd");
		System.out.println(!StringUtils.isEmpty(errMsg.toString()));
	}
}
