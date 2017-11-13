package com.newcore.orbps.service.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.service.cmds.api.CreateGrpCstomerAcountService;
import com.newcore.orbps.service.cmds.api.CreatePsnCstomerAcountService;
import com.newcore.supports.dicts.CNTR_PRINT_TYPE;
import com.newcore.supports.dicts.CNTR_SEND_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.GIFT_FLAG;
import com.newcore.supports.dicts.INSUR_PROPERTY;
import com.newcore.supports.dicts.LIST_PRINT_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.STL_TYPE;
import com.newcore.supports.dicts.VOUCHER_PRINT_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 团单数据处理器实现
 * 
 * @author wangxiao 创建时间：2016年7月20日下午3:59:29
 */
public class GrpInsurApplProcessor implements InsurApplProcessor {

	@Autowired
	CreatePsnCstomerAcountService restfulBatchCreatePsn;
	
	@Autowired
	CreateGrpCstomerAcountService restfulCreateGrpCstomerAcountService;
	
	RetInfo retInfo = new RetInfo();
	StringBuilder errMsg = new StringBuilder();

	/**
	 * 插入处理
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public RetInfo addInsurAppl(GrpInsurAppl grpInsurAppleg,
			MongoBaseDao mongoBaseDao) {
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", grpInsurAppleg.getApplNo());
		if (mongoBaseDao.findOne(GrpInsurAppl.class, map)!=null){
			retInfo.setApplNo(grpInsurAppleg.getApplNo());
			retInfo.setErrMsg("投保单号已存在");
			retInfo.setRetCode("0");
		};
		GrpInsurAppl grpInsurAppl = writeDefaultValue(grpInsurAppleg);
		// 接口出单对入参合规性进行校验
		BindException errors = new BindException(grpInsurAppl, "grpInsurAppl");
		GrpInsurApplValidator grpValidator = new GrpInsurApplValidator();
		grpValidator.validate(grpInsurAppl, errors);

		if (errors.hasErrors()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");

			for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
				errMsg.append(error.getField());
				errMsg.append(", ");
				errMsg.append(error.getCode());
				errMsg.append(" | ");
			}
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}
		//个人客户开户
		if(grpInsurAppl.getPsnListHolderInfo()!=null && StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgCustNo())){
			PsnListHolderInfo psnListHolderInfo = getPsnCustNo(grpInsurAppl);
			grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);
		}
		//团体客户开户
		if(grpInsurAppl.getGrpHolderInfo()!=null && StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpCustNo())){
			GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl);
			grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
		}
		InsurApplRegist insurApplRegist = getInsurApplRegist(grpInsurAppl);
		mongoBaseDao.insert(insurApplRegist);
		mongoBaseDao.remove(GrpInsurAppl.class, map);
		mongoBaseDao.insert(grpInsurAppl);
		retInfo.setApplNo(grpInsurAppl.getApplNo());
		retInfo.setRetCode("1");
		return retInfo;

	}

	/**
	 * 查询处理
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public GrpInsurAppl searchInsurAppl(String applNo, MongoBaseDao mongoBaseDao) {
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		return (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
	}

	/**
	 * 修改处理
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public RetInfo modifyInsurAppl(GrpInsurAppl grpInsurAppl,
			MongoBaseDao mongoBaseDao) {
		// 接口出单对入参合规性进行校验
		BindException errors = new BindException(grpInsurAppl, "grpInsurAppl");
		GrpInsurApplValidator grpValidator = new GrpInsurApplValidator();
		grpValidator.validate(grpInsurAppl, errors);

		if (errors.hasErrors()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");

			for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
				errMsg.append(error.getField());
				errMsg.append(", ");
				errMsg.append(error.getCode());
				errMsg.append(" | ");
			}
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}
		//个人客户开户
		if(grpInsurAppl.getPsnListHolderInfo()!=null && StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgCustNo())){
			PsnListHolderInfo psnListHolderInfo = getPsnCustNo(grpInsurAppl);
			grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);
		}
		//团体客户开户
		if(grpInsurAppl.getGrpHolderInfo()!=null && StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpCustNo())){
			GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl);
			grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
		}
		if(StringUtils.equals("GCSS",grpInsurAppl.getAccessSource()) || StringUtils.equals("GCSS_PRE",grpInsurAppl.getAccessSource())){
			InsurApplRegist insurApplRegist = getInsurApplRegist(grpInsurAppl);
			Map<String, Object> registMap = new HashMap<>();
			registMap.put("billNo", grpInsurAppl.getApplNo());
			mongoBaseDao.remove(InsurApplRegist.class, registMap);
			mongoBaseDao.insert(insurApplRegist);
			InsurApplOperTrace insurApplOperTrace = new InsurApplOperTrace();
			insurApplOperTrace.setApplNo(grpInsurAppl.getApplNo());
			TraceNode traceNode = new TraceNode();
			traceNode.setProcDate(new Date());
			if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())){
				traceNode.setProcStat(NEW_APPL_STATE.GRP_INFO_REVIEW.getKey());
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey())){
				traceNode.setProcStat(NEW_APPL_STATE.LIST_INFO_REVIEW.getKey());
			}
			mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), traceNode);
		}
		// 修改数据mongodb
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", grpInsurAppl.getApplNo());
		mongoBaseDao.remove(GrpInsurAppl.class, map);
		mongoBaseDao.insert(grpInsurAppl);
		retInfo.setApplNo(grpInsurAppl.getApplNo());
		retInfo.setRetCode("1");
		return retInfo;
	}
	//团单信息添加默认值
	public GrpInsurAppl writeDefaultValue(GrpInsurAppl grpInsurAppl) {
		// 保单性质
		if (StringUtils.isEmpty(grpInsurAppl.getInsurProperty())) {
			grpInsurAppl.setInsurProperty(INSUR_PROPERTY.NEW_INSUR.getKey());
		}
		// 合同打印方式
		if (StringUtils.isEmpty(grpInsurAppl.getCntrPrintType())) {
			grpInsurAppl.setCntrPrintType(CNTR_PRINT_TYPE.ELEC_INSUR.getKey());
		}
		// 清单打印方式
		if (StringUtils.isEmpty(grpInsurAppl.getListPrintType())) {
			grpInsurAppl.setListPrintType(LIST_PRINT_TYPE.ELEC_LIST.getKey());
		}
		// 个人凭证类型
		if (StringUtils.isEmpty(grpInsurAppl.getVoucherPrintType())) {
			grpInsurAppl.setVoucherPrintType(VOUCHER_PRINT_TYPE.ELEC_CREDENTIALS.getKey());
		}
		// 销售人员是否共同展业标识
		if (StringUtils.isEmpty(grpInsurAppl.getSalesDevelopFlag())) {
			grpInsurAppl.setSalesDevelopFlag(YES_NO_FLAG.NO.getKey());
		}
		// 外包录入标志
		if (StringUtils.isEmpty(grpInsurAppl.getEntChannelFlag())) {
			grpInsurAppl.setEntChannelFlag(YES_NO_FLAG.NO.getKey());
		}
		// 赠送保险标志
		if (StringUtils.isEmpty(grpInsurAppl.getGiftFlag())) {
			grpInsurAppl.setGiftFlag(GIFT_FLAG.NO.getKey());
		}
		// 续保次数
		if (grpInsurAppl.getRenewTimes() == null) {
			grpInsurAppl.setRenewTimes(0l);
		}
		// 保单送达方式
		if (StringUtils.isEmpty(grpInsurAppl.getCntrSendType())) {
			grpInsurAppl.setCntrSendType(CNTR_SEND_TYPE.COMPANY_SERVICE.getKey());
		}
		// 第一主险
		if (grpInsurAppl.getApplState() != null
				&& grpInsurAppl.getApplState().getPolicyList() != null
				&& !grpInsurAppl.getApplState().getPolicyList().isEmpty()) {
			grpInsurAppl.setFirPolCode(grpInsurAppl.getApplState()
					.getPolicyList().get(0).getPolCode());
		}
		// 人工核保标志
		if (StringUtils.isEmpty(grpInsurAppl.getUdwType())) {
			grpInsurAppl.setUdwType(YES_NO_FLAG.NO.getKey());
		}
		PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
		if (paymentInfo == null) {
			paymentInfo = new PaymentInfo();
		}
		if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())) {
			// 汇缴件号
			if (StringUtils.isEmpty(grpInsurAppl.getSgNo())) {
				grpInsurAppl.setSgNo(grpInsurAppl.getApplNo());
			}
			// 缴费相关.是否需要续期扣款
			if (StringUtils.isEmpty(paymentInfo.getIsRenew())) {
				paymentInfo.setIsRenew(YES_NO_FLAG.NO.getKey());
			}
			// 缴费相关.是否多期暂缴
			if (StringUtils.isEmpty(paymentInfo.getIsMultiPay())) {
				paymentInfo.setIsMultiPay(YES_NO_FLAG.NO.getKey());
			}
		}
		// 缴费相关.结算方式
		if (StringUtils.isEmpty(paymentInfo.getStlType())) {
			paymentInfo.setStlType(STL_TYPE.INSTANT_BILL.getKey());
		}
		// 缴费相关.币种
		if (StringUtils.isEmpty(paymentInfo.getCurrencyCode())) {
			paymentInfo.setCurrencyCode(CURRENCY_CODE.CNY.getKey());
		}
		grpInsurAppl.setPaymentInfo(paymentInfo);
		ApplState applState = grpInsurAppl.getApplState();
		if (applState == null) {
			applState = new ApplState();
		}
		if (StringUtils.isEmpty(applState.getIsPrePrint())) {
			applState.setIsPrePrint(YES_NO_FLAG.NO.getKey());
		}
		if (StringUtils.isEmpty(applState.getIsFreForce())) {
			applState.setIsFreForce(YES_NO_FLAG.NO.getKey());
		}
		List<Policy> policyList = applState.getPolicyList();
		if (policyList != null) {
			for (Policy policy : policyList) {
				if (policy.getPremDiscount() == null) {
					policy.setPremDiscount(0.0);
				}
			}
		}
		return grpInsurAppl;
	}
	//个人汇交人开户
	private PsnListHolderInfo getPsnCustNo(GrpInsurAppl grpInsurAppl){
	List<Map<String, Object>> list=new ArrayList<>();
	Map<String, Object> json = new JSONObject();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		PsnListHolderInfo psnListHolderInfo = grpInsurAppl.getPsnListHolderInfo();
		json.put("PROV_BRANCH_NO",provBranchNo);
		json.put("SRC_SYS","ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO",mgrBranchNo);//管理机构
		json.put("APPL_NO", grpInsurAppl.getApplNo());
		json.put("ROLE","1");
		json.put("IPSN_NO","");
		json.put("BNFRLEVEL","");
		json.put("NAME", psnListHolderInfo.getSgName());
		json.put("ID_TYPE",psnListHolderInfo.getSgIdType());
		json.put("ID_NO",psnListHolderInfo.getSgIdNo());
		json.put("SEX",psnListHolderInfo.getSgSex());
		json.put("BIRTH_DATE",DateFormatUtils.format(psnListHolderInfo.getSgBirthDate(),"yyyy-MM-dd"));
		list.add(json);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//		headerInfo.setOrginSystem("ORBPS");					//<ORISYS>SLBPS</ORISYS>
//		headerInfo.getRouteInfo().setBranchNo("120000");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		restfulBatchCreatePsn=SpringContextHolder.getBean("restfulBatchCreatePsn");
		String retjson = restfulBatchCreatePsn.createPsnCstomerAcount(list);
		JSONObject jsonObject = JSON.parseArray(retjson).getJSONObject(0);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		psnListHolderInfo.setSgCustNo(custNo);
		psnListHolderInfo.setSgPartyId(partyId);
		return psnListHolderInfo;
	}
	//法人开户
	private GrpHolderInfo getGrpCustNo(GrpInsurAppl grpInsurAppl){
		Map<String, Object> json = new HashMap<>();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
		json.put("FLAG", "0");
		json.put("CUST_NO", "");
		json.put("PROV_BRANCH_NO",provBranchNo);
		json.put("SRC_SYS","ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO",mgrBranchNo);//管理机构
		json.put("NAME", grpHolderInfo.getGrpName());
		json.put("OLD_NAME", grpHolderInfo.getFormerGrpName()==null?"":grpHolderInfo.getFormerGrpName());
		json.put("ID_TYPE",grpHolderInfo.getGrpIdType());
		json.put("ID_NO",grpHolderInfo.getGrpIdNo());
		json.put("LEGAL_CODE",grpHolderInfo.getLegalCode()==null?"":grpHolderInfo.getLegalCode());
		json.put("NATURE_CODE",grpHolderInfo.getNatureCode()==null?"":grpHolderInfo.getNatureCode());
		json.put("OCC_CLASS_CODE",grpHolderInfo.getOccClassCode());
		json.put("CORP_REP",grpHolderInfo.getCorpRep());
		json.put("CONTACT_PSN", grpHolderInfo.getContactName()==null?"":grpHolderInfo.getContactName());
		json.put("CONTACT_PSN_SEX", "");
		json.put("NUM_OF_EMP", grpHolderInfo.getNumOfEnterprise().toString());
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//		headerInfo.setOrginSystem("ORBPS");					//<ORISYS>SLBPS</ORISYS>
//		headerInfo.getRouteInfo().setBranchNo("120000");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		restfulCreateGrpCstomerAcountService=SpringContextHolder.getBean("restfulCreateGrpCstomerAcountService");
		String retjson = restfulCreateGrpCstomerAcountService.createGrpCstomerAcount(json);
		JSONObject jsonObject = JSON.parseObject(retjson);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		grpHolderInfo.setGrpCustNo(custNo);
		grpHolderInfo.setPartyId(partyId);
		return grpHolderInfo;
	}
	private InsurApplRegist getInsurApplRegist(GrpInsurAppl grpInsurAppl){
		InsurApplRegist insurApplRegist = new InsurApplRegist();
		insurApplRegist.setBillNo(grpInsurAppl.getApplNo());
		insurApplRegist.setAcceptDate(grpInsurAppl.getApplDate());
		insurApplRegist.setApproNo(grpInsurAppl.getApproNo());
		if(!StringUtils.isEmpty(grpInsurAppl.getAgreementNo())){
			insurApplRegist.setIsCommonAgreement(YES_NO_FLAG.YES.getKey());
			insurApplRegist.setAgreementNo(grpInsurAppl.getAgreementNo());
		}else{
			insurApplRegist.setIsCommonAgreement(YES_NO_FLAG.NO.getKey());
		}
		insurApplRegist.setCntrType(grpInsurAppl.getCntrType());
		insurApplRegist.setPolCode(grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());
		insurApplRegist.setPolNameChn(grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn());
		insurApplRegist.setIpsnNum(grpInsurAppl.getApplState().getIpsnNum());
		insurApplRegist.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode());
		insurApplRegist.setSumPremium(grpInsurAppl.getApplState().getSumPremium());
		if(
			StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())
			|| (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
				&& StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.GRP_SG.getKey()))
		){
			insurApplRegist.setHldrName(grpInsurAppl.getGrpHolderInfo().getGrpName());
		}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey())){
			insurApplRegist.setHldrName(grpInsurAppl.getPsnListHolderInfo().getSgName());
		}
		insurApplRegist.setBillNumber(0);
		insurApplRegist.setShareCustFlag("0");
		insurApplRegist.setEntChanelFlag(grpInsurAppl.getEntChannelFlag());
		insurApplRegist.setSalesDevelopFlag(grpInsurAppl.getSalesDevelopFlag());
		insurApplRegist.setSalesInfos(grpInsurAppl.getSalesInfoList());
		return insurApplRegist;
	}
}
