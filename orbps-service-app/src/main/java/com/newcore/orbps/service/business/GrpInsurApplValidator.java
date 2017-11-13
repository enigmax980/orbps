package com.newcore.orbps.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.service.pcms.api.WebsiteInfoService;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.orbpsutils.service.api.PolicyValidationAtomService;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryReturnBo;
import com.newcore.supports.dicts.ADMIN_FEE_COPU_TYPE;
import com.newcore.supports.dicts.ARGUE_TYPE;
import com.newcore.supports.dicts.CNTR_PRINT_TYPE;
import com.newcore.supports.dicts.CNTR_SEND_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_USE;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.GIFT_FLAG;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.INFORCE_DATE_TYPE;
import com.newcore.supports.dicts.INSUR_PROPERTY;
import com.newcore.supports.dicts.IPSN_GRP_TYPE;
import com.newcore.supports.dicts.LIST_PRINT_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.PREM_CAL_TYPE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.STL_TYPE;
import com.newcore.supports.dicts.VOUCHER_PRINT_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 功能：对团体保单基本信息实体进行校验（数据字典尚未建好，只做非空校验，后续补充数据校验）
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年7月26日下午2:55:50
 */

public class GrpInsurApplValidator implements Validator {

	private static Logger logger = LoggerFactory.getLogger(GrpInsurApplValidator.class);
	
	@Autowired
	SalesmanInfoService resulsalesmanInfoService;
	@Autowired
	WebsiteInfoService resulwebsiteInfoService;
	@Autowired
	PolicyQueryService resulpolicyQueryService;
	@Autowired
	SubPolicyService resulsubPolicyService;
	@Autowired
	PolicyValidationAtomService policyValidationAtomService;
	@Autowired
	PolNatureService polNatureServiceClient;
	
	@Override
	public boolean supports(Class<?> arg0) {
		return GrpInsurAppl.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {

		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) arg0;
		
		/*默认值初始化*/
		writeDefaultValue(grpInsurAppl);

		/* 交易公共信息 */
		validateGrpPubInfo(grpInsurAppl, arg1);

		/**
		 * 校验清汇特殊信息
		 */
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			//清汇只能录普通清单、档案清单
			if(!StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ORDINARY_LIST.getKey())
				&& !StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())){
				arg1.rejectValue("lstProcType", "清汇只能录普通清单、档案清单");
			}
			validateListInfo(grpInsurAppl, arg1);
		}

		/**
		 * 校验团单特殊信息
		 */
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			//团单只能录普通清单、档案清单、无清单
			if(!StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ORDINARY_LIST.getKey())
				&& !StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())
				&& !StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.NONE_LIST.getKey())){
					arg1.rejectValue("lstProcType", "团单只能录普通清单、档案清单、无清单");
			}
			if(grpInsurAppl.getApplState() != null && grpInsurAppl.getApplState().getIpsnNum()!=null && grpInsurAppl.getApplState().getIpsnNum()<3){
				arg1.rejectValue("applState", "团单被保人数不能小于3人");
			}
			validateGrpInfo(grpInsurAppl, arg1);
		}

		/**
		 * 校验销售信息
		 */
		validateSalesInfo(grpInsurAppl, arg1);
		/*
		 * 缴费信息校验
		 */
		validatePaymentInfo(grpInsurAppl, arg1);
		/*
		 * 投保要约校验
		 */
		validateApplState(grpInsurAppl, arg1);
		/*
		 * 校验要约分组(这一块有问题后续完善)
		 */
		validateIpsnStateGrpList(grpInsurAppl, arg1);

		/*
		 * 团体收费组校验
		 */
		validateIpsnPayGrp(grpInsurAppl, arg1);
		//只有普通清单可以录入组织架构树
		/*
		 * 组织架构数校验，只有团单存在组织架构树
		 */
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			if(!StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())
				&& grpInsurAppl.getOrgTreeList()!=null){
				arg1.rejectValue("orgTreeList", "只有普通清单才存在组织架构树");
			}
			validateOrgTree(grpInsurAppl, arg1);
		}else if(grpInsurAppl.getOrgTreeList()!=null){
			arg1.rejectValue("orgTreeList", "只有团单才存在组织架构树");
		}
		//保险期间类型校验
		//缴费方式校验
		policyValidationAtomService=SpringContextHolder.getBean("policyValidationAtomService");
		//检验合同打印方式 如果打印方式为纸质打印 则需要校检规则
		if(StringUtils.equals(grpInsurAppl.getCntrPrintType(), CNTR_PRINT_TYPE.PAPER_INSUR.getKey())){
			String errMsg = policyValidationAtomService.ValidaPolConfig(grpInsurAppl.getApplState().getPolicyList());
			if(!StringUtils.isEmpty(errMsg)){
				arg1.rejectValue("applState",errMsg);
			}
		}
		RetInfo retInfo = policyValidationAtomService.ValidaApplState(grpInsurAppl);
		if(null !=retInfo && StringUtils.equals("0", retInfo.getRetCode())){
			arg1.rejectValue("applState", retInfo.getErrMsg());
		}	
		//校验健康险
		validateHealthInsurInfo(grpInsurAppl, arg1);
		//校验基金险
		validateFundInsurInfo(grpInsurAppl, arg1);
	}


	/**
	 * 校验公共信息
	 * 
	 * @param grpInsurAppl
	 */
	public void validateGrpPubInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {
		/*
		 * 投保单号不能为空
		 */
		if (StringUtils.isEmpty(grpInsurAppl.getApplNo())) {
			arg1.rejectValue("applNo", "投保单号为空.");
		}
		/*
		 * 投保单号长度为16
		 */
		else if (16 != grpInsurAppl.getApplNo().length()) {
			arg1.rejectValue("applNo", "投保单号长度有误.");
		}

		/*
		 * 清单标记校验
		 */
		if (ValidatorUtils.testLstProcType(grpInsurAppl.getLstProcType())) {
			arg1.rejectValue("lstProcType", "清单标记不符合字典检查.");
		}
		// 保单性质校验
		if (ValidatorUtils.testInsurProperty(grpInsurAppl.getInsurProperty())) {
			arg1.rejectValue("insurProperty", "保单性质不符合字典检查.");
		}

		// 团体业务分类（来源）校验
		if (!StringUtils.isEmpty(grpInsurAppl.getGrpBusiType())
				&& ValidatorUtils.testGrpBusiType(grpInsurAppl.getGrpBusiType())) {
			arg1.rejectValue("insurProperty", "团体业务分类（来源）不符合字典检查.");
		}

		// 契约业务类型校验
		if (!StringUtils.isEmpty(grpInsurAppl.getApplBusiType())
				&& ValidatorUtils.testApplBusiType(grpInsurAppl.getApplBusiType())) {
			arg1.rejectValue("applBusiType", "契约业务类型不符合字典检查.");
		}

		/*
		 * 合同打印方式检验
		 */
		if (ValidatorUtils.testCntrPrintType(grpInsurAppl.getCntrPrintType())) {
			arg1.rejectValue("cntrPrintType", "合同打印方式不符合字典检查.");
		}
		/*
		 * 清单打印方式检验
		 */
		if (ValidatorUtils.testListPrintType(grpInsurAppl.getListPrintType())) {
			arg1.rejectValue("listPrintType", "清单打印方式不符合字典检查.");
		}

		/*
		 * 个人凭证类型校验
		 */
		if (ValidatorUtils.testVoucherPrintType(grpInsurAppl.getVoucherPrintType())) {
			arg1.rejectValue("voucherPrintType", "个人凭证类型不符合字典检查.");
		}

		/*
		 * 销售人员是否共同展业标识校验
		 */
		if (ValidatorUtils.testYesNo(grpInsurAppl.getSalesDevelopFlag())) {
			arg1.rejectValue("salesDevelopFlag", "个人凭证类型不符合字典检查.");
		}

		/*
		 * 契约类型校验
		 */
		if (ValidatorUtils.testCntrType(grpInsurAppl.getCntrType())) {
			arg1.rejectValue("cntrType", "契约类型不符合字典检查.");
		}
		/*
		 * 合同送达方式校验
		 */
		if (ValidatorUtils.testCntrSendType(grpInsurAppl.getCntrSendType())) {
			arg1.rejectValue("cntrSendType", "合同送达方式不符合字典检查.");
		}
		/*
		 * 外包录入标志校验
		 */
		if (ValidatorUtils.testYesNo(grpInsurAppl.getEntChannelFlag())) {
			arg1.rejectValue("entChannelFlag", "外包录入标志不符合字典检查.");
		}

		/*
		 * 争议处理方式校验
		 */
		if (ValidatorUtils.testArgueType(grpInsurAppl.getArgueType())) {
			arg1.rejectValue("argueType", "争议处理方式不符合字典检查.");
		}
		/*
		 * 赠送保险标志校验
		 */
		if (ValidatorUtils.testGiftFlag(grpInsurAppl.getGiftFlag())) {
			arg1.rejectValue("giftFlag", "赠送保险标志不符合字典检查.");
		}
		/*
		 * 是否异常告知校验
		 */
		if (ValidatorUtils.testYesNo(grpInsurAppl.getNotificaStat())) {
			arg1.rejectValue("notificaStat", "是否异常告知不符合字典检查.");
		}
		/*
		 * 人工核保标志校验
		 */
		grpInsurAppl.setUdwType(YES_NO_FLAG.NO.getKey());
		/*
		 * 指定退保公式校验
		 */
		if (!StringUtils.isEmpty(grpInsurAppl.getSrndAmntCmptType())
				&& ValidatorUtils.testSrndAmntCmptType(grpInsurAppl.getSrndAmntCmptType())) {
			arg1.rejectValue("srndAmntCmptType", "指定退保公式不符合字典检查.");
		}

		/*
		 * 投保日期非空校验
		 */
		if (grpInsurAppl.getApplDate() == null) {
			arg1.rejectValue("applDate", "投保日期为空.");
		}

		/*
		 * 争议处理方式非空校验
		 */
		if (ValidatorUtils.testArgueType(grpInsurAppl.getArgueType())) {
			arg1.rejectValue("argueType", "争议处理不符合字典检查.");
		}

		/*
		 * 仲裁委员会名称非空校验
		 */
		if (StringUtils.equals(ARGUE_TYPE.ARBITRATION.getKey(), grpInsurAppl.getArgueType()) && StringUtils.isEmpty(grpInsurAppl.getArbitration())) {
			arg1.rejectValue("arbitration", "仲裁委员会名称为空.");
		}

		// 上期保单号校验
		if (StringUtils.equals(INSUR_PROPERTY.RNEW_INSUR.getKey(), grpInsurAppl.getInsurProperty())
				&& StringUtils.isEmpty(grpInsurAppl.getRenewedCgNo())) {
			arg1.rejectValue("renewedCgNo", "保单性质为续保时上期保单号为空");
		}
	}

	/**
	 * 校验清汇特殊信息
	 * 
	 * @param grpInsurAppl
	 */
	public void validateListInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {

		/*
		 * 汇交人类型校验
		 */
		if (!StringUtils.isEmpty(grpInsurAppl.getSgType())
				&& ValidatorUtils.testListType(grpInsurAppl.getSgType())) {
			arg1.rejectValue("sgType", "汇交人类型不符合字典检查.");
		}
		// 汇交人类型不能为空
		if (StringUtils.isEmpty(grpInsurAppl.getSgType())) {
			arg1.rejectValue("sgType", "汇交人类型为空.");
		}
		/**
		 * 如果汇交人为个人
		 */
		if (StringUtils.equals(LIST_TYPE.PSN_SG.getKey(), grpInsurAppl.getSgType())) {
			if (grpInsurAppl.getPsnListHolderInfo() == null) {
				arg1.rejectValue("psnListHolderInfo", "个人汇交人信息为空");
			} else {
				checkPsnListHolderInfo(grpInsurAppl, arg1);
			}
		} else {// 为团体客户
			if (grpInsurAppl.getGrpHolderInfo() == null) {
				arg1.rejectValue("grpHolderInfo", "团体客户信息为空");
			} else {
				checkGrpHolderInfo(grpInsurAppl, arg1);
			}
		}
		if (grpInsurAppl.getOrgTreeList()!=null){
			arg1.rejectValue("orgTreeList", "清汇组织架构树应为空");
		}
	}

	/**
	 * 校验团单特殊信息
	 * 
	 * @param grpInsurAppl
	 */
	public void validateGrpInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {

		// 团单员工总数、在职人数、投保人数必填
		if (StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(), grpInsurAppl.getCntrType())) {

			if (grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise() == null
					|| 0 == grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise()) {
				arg1.rejectValue("grpHolderInfo", "企业员工总数为空.");
			}
			if (grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum() == null
					|| 0 == grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum()) {
				arg1.rejectValue("grpHolderInfo", "企业在职人数为空.");
			}
			if (grpInsurAppl.getGrpHolderInfo().getIpsnNum() == null
					|| 0 == grpInsurAppl.getGrpHolderInfo().getIpsnNum()) {
				arg1.rejectValue("grpHolderInfo", "投保人数为空.");
			}else if(grpInsurAppl.getApplState()!=null 
				&& grpInsurAppl.getApplState().getIpsnNum()!=null
				&& Long.compare(grpInsurAppl.getGrpHolderInfo().getIpsnNum(),grpInsurAppl.getApplState().getIpsnNum())!=0){
				arg1.rejectValue("grpHolderInfo", "团体客户信息中的投保人数不等于要约中的投保人数.");
			}
		}

		if (grpInsurAppl.getGrpHolderInfo() == null) {
			arg1.rejectValue("grpHolderInfo", "团体客户信息为空");

			return;
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpName())) {
			arg1.rejectValue("grpHolderInfo", "单位名称为空.");
		}
		if (ValidatorUtils.testGrpIdType(grpInsurAppl.getGrpHolderInfo().getGrpIdType())) {
			arg1.rejectValue("grpHolderInfo", "团体客户证件类别不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpIdNo())) {
			arg1.rejectValue("grpHolderInfo", "团体客户证件号码为空.");
		}
		if (ValidatorUtils.testGrpCountryCode(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode())) {
			arg1.rejectValue("grpHolderInfo", "企业注册地国籍不符合字典检查.");
		}
		if (ValidatorUtils.testGrpPsnDeptType(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType())) {
			arg1.rejectValue("grpHolderInfo", "外管局部门类型不符合字典检查.");
		}
		if (ValidatorUtils.testOccClassCode(grpInsurAppl.getGrpHolderInfo().getOccClassCode())) {
			arg1.rejectValue("grpHolderInfo", "行业类别不符合字典检查.");
		}
		if (!StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getNatureCode())
				&& ValidatorUtils.testNatureCode(grpInsurAppl.getGrpHolderInfo().getNatureCode())) {
			arg1.rejectValue("grpHolderInfo", "单位性质（经济分类）不符合字典检查.");
		}
		if (!StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getLegalCode())
				&& ValidatorUtils.testLegalCode(grpInsurAppl.getGrpHolderInfo().getLegalCode())) {
			arg1.rejectValue("grpHolderInfo", "单位性质（法律分类）不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactName())) {
			arg1.rejectValue("grpHolderInfo", "联系人姓名为空.");
		}
		if (ValidatorUtils.testIdType(grpInsurAppl.getGrpHolderInfo().getContactIdType())) {
			arg1.rejectValue("grpHolderInfo", "联系人证件类型不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactIdNo())) {
			arg1.rejectValue("grpHolderInfo", "联系人证件号码为空.");
		}
		if (StringUtils.equals(grpInsurAppl.getGrpHolderInfo().getContactIdType(), ID_TYPE.ID.getKey())) {
			String s = ValidatorUtils.validIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo());
			if (!StringUtils.equals(s, "OK")) {
				arg1.rejectValue("grpHolderInfo", s);
			}else{
				grpInsurAppl.getGrpHolderInfo().setContactIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo().toUpperCase());
			}
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactMobile())
				&& StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactTelephone())) {
			arg1.rejectValue("grpHolderInfo", "联系人固定电话和手机号不能同时为空.");
		}
		if (grpInsurAppl.getGrpHolderInfo().getAddress() == null) {
			arg1.rejectValue("grpHolderInfo", "团体客户信息中地址为空.");
			return;
		}

		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getProvince())) {
			arg1.rejectValue("grpHolderInfo", "汇交人地址中省/自治州为空.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getCity())) {
			arg1.rejectValue("grpHolderInfo", "汇交人地址中市/州为空.");
		}

		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getHomeAddress())) {
			arg1.rejectValue("grpHolderInfo", "汇交人地址明细为空.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getCounty())) {
			arg1.rejectValue("grpHolderInfo", "汇交人地区/县细为空.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode())) {
			arg1.rejectValue("grpHolderInfo", "团体客户信息中邮编为空.");
		}		
		RetInfo retInfo =ValidatorUtils.checkPostCode(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode(), grpInsurAppl.getGrpHolderInfo().getAddress().getProvince());
		if(StringUtils.equals(retInfo.getRetCode(), "0")){
			arg1.rejectValue("grpHolderInfo", retInfo.getErrMsg());
		}
	}

	/**
	 * 校验销售信息
	 * 
	 * @param grpInsurAppl
	 */
	public void validateSalesInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {

		if (grpInsurAppl.getSalesInfoList() == null || (grpInsurAppl.getSalesInfoList().size() == 0)) {
			arg1.rejectValue("salesInfoList", "销售信息为空");

			return;
		}

		if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getSalesDevelopFlag())) {
			if ((grpInsurAppl.getSalesInfoList().size() < 2)) {
				arg1.rejectValue("salesInfoList", "共同展业时销售员应为多个.");
			}
		}

		for (int i = 0; i < grpInsurAppl.getSalesInfoList().size(); i++) {
			SalesInfo salesInfo = grpInsurAppl.getSalesInfoList().get(i);
			// 多销售员共同展业，各个销售员需填写主副展业标记
			if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getSalesDevelopFlag())) {

				if (StringUtils.isEmpty(salesInfo.getDevelMainFlag())) {
					arg1.rejectValue("salesInfoList", "多销售员共同展业，各个销售员需填写主副展业标记.");
				}
				if (ValidatorUtils.testDevelMainFlag(salesInfo.getDevelMainFlag())) {
					arg1.rejectValue("salesInfoList", "共同展业主副展业标记不符合字典检查.");
				}
			}else{
				salesInfo.setDevelMainFlag(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey());
			}
		}

		if (null != grpInsurAppl.getSalesInfoList()) {
			for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
				checkSalesman(grpInsurAppl, arg1,salesInfo);

			}

		} else {
			arg1.rejectValue("salesInfoList", "销售信息为空");
		}

	}

	/** 
	* @Title: checkSalesman 
	* @Description: 校检销售信息
	* @param @param grpInsurAppl
	* @param @param arg1
	* @param @param salesInfo    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	private void checkSalesman(GrpInsurAppl grpInsurAppl, Errors arg1,SalesInfo salesInfo) {
		SalesmanQueryInfo salesmanQueryInfo=new SalesmanQueryInfo();
		WebsiteQueryInfo websiteQueryInfo=new WebsiteQueryInfo();
		resulsalesmanInfoService=SpringContextHolder.getBean("resulsalesmanInfoService");
		resulwebsiteInfoService=SpringContextHolder.getBean("resulwebsiteInfoService");
		if (null != salesInfo) {
			if (StringUtils.isEmpty(salesInfo.getSalesBranchNo())) {
				arg1.rejectValue("salesInfoList", "销售机构为空.");
			} else {
				salesmanQueryInfo.setSalesBranchNo(salesInfo.getSalesBranchNo());
				websiteQueryInfo.setSalesBranchNo(salesInfo.getSalesBranchNo());
			}
			if (StringUtils.isEmpty(salesInfo.getSalesChannel())) {
				arg1.rejectValue("salesInfoList", "销售渠道为空.");
			} else {
				salesmanQueryInfo.setSalesChannel(salesInfo.getSalesChannel());
				websiteQueryInfo.setSalesChannel(salesInfo.getSalesChannel());
			}
			// 当销售信息与网点信息同时存在 以销售信息为主 网点信息只要网点name
			if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo()) && !StringUtils.isEmpty(salesInfo.getSalesNo())) {
				salesmanQueryInfo.setSalesChannel("BS");
				salesmanQueryInfo.setSalesNo(salesInfo.getSalesNo());
				websiteQueryInfo.setSalesDeptNo(salesInfo.getSalesDeptNo());
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				SalesmanQueryReturnBo queryReturnBo  = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
				CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo1);	
				WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);

				if (null!=websiteQuery) {// 获得网点信息
					if (StringUtils.equals("1", websiteQuery.getRetCode())) {
						if (StringUtils.equals("1", websiteQuery.getDeptState())) {
							salesInfo.setDeptName(websiteQuery.getDeptName());
						} else {
							arg1.rejectValue("salesInfoList", "查询网点无效");
						}
					} else {
						arg1.rejectValue("salesInfoList", websiteQuery.getErrMsg());
					}

				}
				if (null!=queryReturnBo) {// 销售员工信息
					if (StringUtils.equals("1", queryReturnBo.getSaleState())) { 
						if (StringUtils.equals("1", queryReturnBo.getSaleState())) {
							grpInsurAppl.setProvBranchNo(queryReturnBo.getProvBranchNo());// 省级机构号
							grpInsurAppl.setMgrBranchNo(queryReturnBo.getMgrBranchNo());// 管理机构号
							grpInsurAppl.setArcBranchNo(queryReturnBo.getArcBranchNo());//
							salesInfo.setSalesName(queryReturnBo.getSalesName()); // 销售员姓名(
							salesInfo.setSalesBranchAddr(queryReturnBo.getSalesBranchAddr()); // 销售机构地址
							salesInfo.setSalesBranchPostCode(queryReturnBo.getSalesBranchPostCode()); // 销售机构邮政编码
							salesInfo.setCenterCode(queryReturnBo.getCenterCode()); // 成本中心(
							salesInfo.setSalesBranchName(queryReturnBo.getSalesBranchName());
						} else {
							arg1.rejectValue("salesInfoList", "查询员工已离职");

						}
					} else {
						arg1.rejectValue("salesInfoList", queryReturnBo.getErrMsg());
					}
				}
				// 如果网点不为空销售员工为空 信息以网点为主
			} else if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo())
					&& StringUtils.isEmpty(salesInfo.getSalesNo())) {
				websiteQueryInfo.setSalesDeptNo(salesInfo.getSalesDeptNo());
				CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo1);	
				WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
				if (null!=websiteQuery) {// 获得网点信息
					if (StringUtils.equals("1", websiteQuery.getRetCode())) {
						if (StringUtils.equals("1", websiteQuery.getDeptState())) {
							salesInfo.setDeptName(websiteQuery.getDeptName());
							grpInsurAppl.setProvBranchNo(websiteQuery.getProvBranchNo());// 省级机构号
							grpInsurAppl.setMgrBranchNo(websiteQuery.getMgrBranchNo());// 管理机构号
							salesInfo.setSalesBranchName(websiteQuery.getSalesBranchName());
							salesInfo.setSalesBranchAddr(websiteQuery.getSalesBranchAddr()); // 销售机构地址
							salesInfo.setSalesBranchPostCode(websiteQuery.getSalesBranchPostCode()); // 销售机构邮政编码
							salesInfo.setCenterCode(websiteQuery.getCenterCode()); // 成本中心(
						} else {
							arg1.rejectValue("salesInfoList", "查询网点无效");
						}
					} else {
						arg1.rejectValue("salesInfoList", websiteQuery.getErrMsg());
					}

				}
			} else if (StringUtils.isEmpty(salesInfo.getSalesDeptNo())
					&& !StringUtils.isEmpty(salesInfo.getSalesNo())) {
				salesmanQueryInfo.setSalesNo(salesInfo.getSalesNo());
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				SalesmanQueryReturnBo queryReturnBo  = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
				if (null!=queryReturnBo) {// 销售员工信息
					if (StringUtils.equals("1", queryReturnBo.getSaleState())) {
						if (StringUtils.equals("1", queryReturnBo.getSaleState())) {
							grpInsurAppl.setProvBranchNo(queryReturnBo.getProvBranchNo());// 省级机构号
							salesInfo.setSalesName(queryReturnBo.getSalesName()); // 销售员姓名(
							grpInsurAppl.setMgrBranchNo(queryReturnBo.getMgrBranchNo());// 管理机构号
							grpInsurAppl.setArcBranchNo(queryReturnBo.getArcBranchNo());//
							salesInfo.setSalesBranchAddr(queryReturnBo.getSalesBranchAddr()); // 销售机构地址
							salesInfo.setSalesBranchPostCode(queryReturnBo.getSalesBranchPostCode()); // 销售机构邮政编码
							salesInfo.setCenterCode(queryReturnBo.getCenterCode()); // 成本中心(
							salesInfo.setSalesBranchName(queryReturnBo.getSalesBranchName());
						} else {
							arg1.rejectValue("salesInfoList", "查询员工已离职");
						}
					} else {
						arg1.rejectValue("salesInfoList", queryReturnBo.getErrMsg());

					}
				}
			} else {
				arg1.rejectValue("salesInfoList", "网点号或销售员工号必须存在一个");
			}
		} else {
			arg1.rejectValue("salesInfoList", "销售信息为空");
		}
	}

	/**
	 * 校验交费信息
	 * 
	 * @param grpInsurAppl
	 */
	public void validatePaymentInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if (grpInsurAppl.getPaymentInfo() == null) {
			arg1.rejectValue("paymentInfo", "缴费信息为空");
			return;
		}
		if(grpInsurAppl.getPaymentInfo().getStlDate()!=null && !grpInsurAppl.getPaymentInfo().getStlDate().isEmpty()){
			grpInsurAppl.getPaymentInfo().getStlDate().removeAll(Collections.singleton(null));
		}
		// 团单不支持个人付款
		if (StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())
			&& StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())){
			arg1.rejectValue("paymentInfo", "团单不支持个人付款.");
		}
		//缴费相关添加默认缴费期，缴费单位，添加开户银行，银行帐号校验
		checkPaymentInfo(grpInsurAppl, arg1);
		if (ValidatorUtils.testPremSource(grpInsurAppl.getPaymentInfo().getPremSource())) {
			arg1.rejectValue("paymentInfo", "团单保费来源不符合字典检查.");
		}
		//除普通清单外不能是个人付款、团体个人共同付款
		if(!StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(),grpInsurAppl.getLstProcType())
			&& (StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
				|| StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource()) )){
			arg1.rejectValue("paymentInfo", "团体清单标志不是普通清单时，保费来源不能是个人付款、团体个人共同付款.");	
		}
		// 如果保费来源为团体个人共同扣款，须填写首期扣款截止日期
		if ((StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource()) 
				|| StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource()))   
				&& grpInsurAppl.getPaymentInfo().getForeExpDate() == null) {
			arg1.rejectValue("paymentInfo", "首期扣款截止日期为空.");
		}
		// 缴费方式不为空
		if (ValidatorUtils.testMoneyinItrvl(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())) {
			arg1.rejectValue("paymentInfo", "缴费方式不符合字典检查.");
		}
		// 如果是共保，缴费方式必须为年缴或趸交
		if (!StringUtils.isEmpty(grpInsurAppl.getAgreementNo())
				&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinItrvl(), MONEYIN_ITRVL.YEAR_PAY.getKey())
				&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinItrvl(), MONEYIN_ITRVL.SINGLE_PAY.getKey())){
			arg1.rejectValue("paymentInfo", "如果是共保，缴费方式必须为年缴或趸交.");
		}
		//如果是个人交费,交费方式必须是趸交
		if (StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
			&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinItrvl(), MONEYIN_ITRVL.SINGLE_PAY.getKey())){
			arg1.rejectValue("paymentInfo", "如果是个人付款，缴费方式必须为趸交.");
		}
		//团体交费含组织架构树,交费方式必须是趸交
		if (StringUtils.equals(PREM_SOURCE.GRP_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
			&& isContainsFeeNode(grpInsurAppl.getOrgTreeList())
			&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinItrvl(), MONEYIN_ITRVL.SINGLE_PAY.getKey())){
			arg1.rejectValue("paymentInfo", "如果是团体付款包含组织架构树，缴费方式必须为趸交.");
		}
		//团个共同交费,交费方式必须是趸交
		if (StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
			&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinItrvl(), MONEYIN_ITRVL.SINGLE_PAY.getKey())){
			arg1.rejectValue("paymentInfo", "如果是团体个人共同付款，缴费方式必须为趸交.");
		}
		// 缴费形式不为空
		if (!StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
			&& !StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType()) &&ValidatorUtils.testMoneyinType(grpInsurAppl.getPaymentInfo().getMoneyinType())) {
			arg1.rejectValue("paymentInfo", "缴费形式不符合字典检查.");
		}
		// 如果是团个共同付款
		if (StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())){
			//如果是清汇
			if(StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(),grpInsurAppl.getCntrType())){
				if(!StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinType(),MONEYIN_TYPE.TRANSFER.getKey())){
					arg1.rejectValue("paymentInfo", "团个共同付款、如果是清汇，交费形式必须为银行转账.");
				}
			}
			//如果是团单并且不存在组织架构树
			else if(StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(),grpInsurAppl.getCntrType())
					&& (grpInsurAppl.getOrgTreeList() == null 
						|| grpInsurAppl.getOrgTreeList().isEmpty())){
				if(!StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinType(),MONEYIN_TYPE.TRANSFER.getKey())){
					arg1.rejectValue("paymentInfo", "团个共同付款、如果是团单并且不存在组织架构树，交费形式必须为银行转账.");
				}
			}
			//如果是团单并且存在组织架构树
			else if(StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(),grpInsurAppl.getCntrType())
					&& grpInsurAppl.getOrgTreeList() != null
					&& !grpInsurAppl.getOrgTreeList().isEmpty()){
				//组织架构树中不包含交费节点时
				if(!isContainsFeeNode(grpInsurAppl.getOrgTreeList())
					&& !StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinType(),MONEYIN_TYPE.TRANSFER.getKey())){
					arg1.rejectValue("paymentInfo", "团个共同付款、如果是团单并且存在组织架构树，且不含交费节点时，交费形式必须为银行转账.");
				}
				//组织架构树中包含交费节点时
				if(isContainsFeeNode(grpInsurAppl.getOrgTreeList())
					&& !StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())){
					arg1.rejectValue("paymentInfo", "团个共同付款、如果是团单并且存在组织架构树，包含交费节点时，交费形式应为空.");
				}
			}
		}
		//如果是个人付款
		if(StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
				&& !StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())){
			arg1.rejectValue("paymentInfo", "保费来源为个人账户付款时，缴费形式应为空.");
		}
		//如果是个人付款,不能存在有交费节点的组织架构树
		if(StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())
			&& isContainsFeeNode(grpInsurAppl.getOrgTreeList())){
			arg1.rejectValue("paymentInfo", "如果是个人付款,不能存在有交费节点的组织架构树");
		}
		//如果是团体付款
		if(StringUtils.equals(PREM_SOURCE.GRP_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())){
			if(StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())
				^ isContainsFeeNode(grpInsurAppl.getOrgTreeList())){
				arg1.rejectValue("paymentInfo", "保费来源为团体付款时，缴费形式为空时，组织架构树必须包含交费节点；缴费形式不为空时，组织架构树不能包含交费节点");
			}
			//交费节点不为空时，节点交费金额之和要等于保费
			if(isContainsFeeNode(grpInsurAppl.getOrgTreeList())
				&& grpInsurAppl.getApplState()!=null
				&& Math.abs(grpInsurAppl.getApplState().getSumPremium()-sumNodePayAmnt(grpInsurAppl.getOrgTreeList()))>0.001){
				arg1.rejectValue("paymentInfo", "保费来源为团体付款时，交费节点不为空时，节点交费金额之和要等于保费");
			}
		}
		// 如果缴费形式为银行转账，需填写账户信息
		if (StringUtils.equals(MONEYIN_TYPE.TRANSFER.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinType())
			&& !StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())) {  
			if (StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getBankCode())) {
				arg1.rejectValue("paymentInfo", "开户银行为空.");
			}
			if (StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getBankAccName())) {
				arg1.rejectValue("paymentInfo", "开户名称为空.");
			}
			if (StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getBankAccNo())) {
				arg1.rejectValue("paymentInfo", "银行账号为空.");
			}
		}
		// 结算方式非空
		if (ValidatorUtils.testStlType(grpInsurAppl.getPaymentInfo().getStlType())) {
			arg1.rejectValue("paymentInfo", "结算方式不符合字典检查.");
		}
		// 如果是银行转账，首期扣款截止日期不能为空
		if (StringUtils.equals(MONEYIN_TYPE.TRANSFER.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinType())
			&& null == grpInsurAppl.getPaymentInfo().getForeExpDate()){
			arg1.rejectValue("paymentInfo", "首期扣款截止日期不能为空.");
		}
		// 如果结算方式为组合结算，结算限额、结算日期为必填
		if (StringUtils.equals(STL_TYPE.BILL_COMBIN.getKey(), grpInsurAppl.getPaymentInfo().getStlType())  
				&& (grpInsurAppl.getPaymentInfo().getStlAmnt() == null
						|| 0.0000001 > Math.abs(grpInsurAppl.getPaymentInfo().getStlAmnt()))) {
			arg1.rejectValue("paymentInfo", "结算限额为空.");
		}

		// 如果结算方式为组合结算，结算限额、结算日期为必填
		if (StringUtils.equals(STL_TYPE.BILL_COMBIN.getKey(), grpInsurAppl.getPaymentInfo().getStlType())
			|| StringUtils.equals(STL_TYPE.DESIGNAT_SETTLE.getKey(), grpInsurAppl.getPaymentInfo().getStlType())){
			if(grpInsurAppl.getPaymentInfo().getStlDate() == null
				|| grpInsurAppl.getPaymentInfo().getStlDate().isEmpty()) {
				arg1.rejectValue("paymentInfo", "结算方式为组合结算或指定日期结算，结算日期为空.");
				return;
			}
			checkStlDate(grpInsurAppl, arg1);
		}
		// 比例结算，结算比例必填
		if (StringUtils.equals(STL_TYPE.PROPOR_SETTLE.getKey(), grpInsurAppl.getPaymentInfo().getStlType()) 
				&& (grpInsurAppl.getPaymentInfo().getStlRate() == null
						|| 0.0000001 > Math.abs(grpInsurAppl.getPaymentInfo().getStlRate()))) {
			arg1.rejectValue("paymentInfo", "结算比例为空.");
		}

		if (ValidatorUtils.testYesNo(grpInsurAppl.getPaymentInfo().getIsRenew())) {
			arg1.rejectValue("paymentInfo", "是否需要续期扣款不符合字典检查.");
		}
		if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getPaymentInfo().getIsRenew())  
				&& (grpInsurAppl.getPaymentInfo().getRenewExpDate() == null)) {
			arg1.rejectValue("paymentInfo", "续期扣款截止日期为空.");
		}
		// 清汇，选择续期扣款，需填写扣款截止日期；多期暂缴，需填写多期暂缴年数
		if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())) {

			if (ValidatorUtils.testYesNo(grpInsurAppl.getPaymentInfo().getIsMultiPay())) {
				arg1.rejectValue("paymentInfo", "是否多期暂缴不符合字典检查.");
			}
			if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getPaymentInfo().getIsMultiPay()) &&

					(grpInsurAppl.getPaymentInfo().getMutipayTimes() == null
							|| grpInsurAppl.getPaymentInfo().getMutipayTimes() == 0)) {
				arg1.rejectValue("paymentInfo", "多期暂缴年数为空.");
			}
			if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getIsMultiPay(), YES_NO_FLAG.YES.getKey())
				&& !StringUtils.equals(MONEYIN_ITRVL.YEAR_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl())
				&& !StringUtils.equals(MONEYIN_ITRVL.SINGLE_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl())){
				arg1.rejectValue("paymentInfo", "如果多期暂缴，缴费方式必须是年缴或趸缴");
			}
			if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getIsMultiPay(), YES_NO_FLAG.YES.getKey())
				&& !StringUtils.equals(PREM_SOURCE.GRP_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())){
				arg1.rejectValue("paymentInfo", "如果多期暂缴，团单保费来源必须是团体付款");
			}
		}
		// 如果多方供款
//		if (StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())  
//				&& ((grpInsurAppl.getPaymentInfo().getMultiPartyScale() == null
//						|| 0.0000001 > Math.abs(grpInsurAppl.getPaymentInfo().getMultiPartyScale()))
//						&& (grpInsurAppl.getPaymentInfo().getMultiPartyMoney() == null
//								|| 0.0000001 > Math.abs(grpInsurAppl.getPaymentInfo().getMultiPartyMoney())))) {
//			arg1.rejectValue("paymentInfo", "多方供款供款比例和供款金额不能同时为空.");
//		}
	}

	/**
	 * 校验要约
	 * 
	 * @param grpInsurAppl
	 */
	public void validateApplState(GrpInsurAppl grpInsurAppl, Errors arg1) {

		Double sumPoicyFaceAmnt = 0.00, sumPoicyPremium = 0.00;
		if (grpInsurAppl.getApplState() == null) {
			arg1.rejectValue("applState", "投保要约为空");
			return;
		}

		if (grpInsurAppl.getApplState().getIpsnNum() == null || grpInsurAppl.getApplState().getIpsnNum() < 1) {
			arg1.rejectValue("applState", "投保人数为空或为0.");
		}
		if ((grpInsurAppl.getApplState().getSumPremium() == null
				|| 0.001 > Math.abs(grpInsurAppl.getApplState().getSumPremium()))) {
			arg1.rejectValue("applState", "总保费为空或为0.");
		}
		// 校验指定生效日类型
		if (ValidatorUtils.testInforceDateType(grpInsurAppl.getApplState().getInforceDateType())) {
			arg1.rejectValue("applState", "指定生效日类型不符合字典检查.");
		}
		// 如果指定生效日
		if (StringUtils.equals(INFORCE_DATE_TYPE.SPECIFY_EFFECT.getKey(), grpInsurAppl.getApplState().getInforceDateType()) 
				&& grpInsurAppl.getApplState().getDesignForceDate() == null) {
			arg1.rejectValue("applState", "指定生效日为空.");
		}
		// 校验是否预打印
		if (ValidatorUtils.testYesNo(grpInsurAppl.getApplState().getIsPrePrint())) {
			arg1.rejectValue("applState", "是否预打印不符合字典检查.");
		}
		// 校验是否频次生效
		if (ValidatorUtils.testYesNo(grpInsurAppl.getApplState().getIsFreForce())) {
			arg1.rejectValue("applState", "是否频次生效不符合字典检查.");
		}
		// 如果频次生效
		if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getApplState().getIsFreForce())
				&& (grpInsurAppl.getApplState().getForceFre() == null
						|| grpInsurAppl.getApplState().getForceFre() == 0)) {
			arg1.rejectValue("applState", "生效频次为空.");
		}

		// 校验险种
		if (grpInsurAppl.getApplState().getPolicyList() == null
				|| grpInsurAppl.getApplState().getPolicyList().isEmpty()) {
			arg1.rejectValue("applState", "险种列表为空.");
			return;
		} else {
			//基金险的判断
			boolean isFund = false;
			CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo1);
			polNatureServiceClient = SpringContextHolder.getBean("polNatureServiceClient");
			List<String> polCodes = new ArrayList<>();
			for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
				polCodes.add(policy.getPolCode());
			}
			List<JSONObject> jsonObjects = polNatureServiceClient.getPolNatureInfo(polCodes);
			String fundPolCode = "";
			for(JSONObject jsonObject:jsonObjects){
				if(StringUtils.equals(YES_NO_FLAG.YES.getKey(), jsonObject.getString("isFund"))){
					fundPolCode = jsonObject.getString("polCode");
					isFund = true;
					break;
				}
			}
			//如果为基金险
			if(isFund){
				//基金险不能为空
				if(grpInsurAppl.getFundInsurInfo()==null){
					arg1.rejectValue("applState", "险种："+fundPolCode+"为基金险,基金险信息为空.");
				}
				//健康险专项标识不能为空
				if(StringUtils.isEmpty(grpInsurAppl.getApplState().getPolicyList().get(0).getSpeciBusinessLogo())){
					arg1.rejectValue("applState", "险种："+fundPolCode+"为基金险,健康险专项标识为空.");
				}
				//被保人是否分组的判断，基金险必须做被保人分组
				if(grpInsurAppl.getIpsnStateGrpList()==null || grpInsurAppl.getIpsnStateGrpList().isEmpty()){
					arg1.rejectValue("applState", "险种："+fundPolCode+"为基金险,基金险必须做被保人分组.");
				}
				//基金险不能是档案清单
				if(StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ARCHIVES_LIST.getKey())){
					arg1.rejectValue("applState", "险种："+fundPolCode+"为基金险,基金险不能是档案清单.");
				}
			}
			
			//校验保险期间，保险期间类型
			if (grpInsurAppl.getApplState().getInsurDur() == null
					|| grpInsurAppl.getApplState().getInsurDur() == 0) {
				grpInsurAppl.getApplState().setInsurDur(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur());
			}
			if (StringUtils.isEmpty(grpInsurAppl.getApplState().getInsurDurUnit())) {
				grpInsurAppl.getApplState().setInsurDurUnit(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDurUnit());
			}
			if (grpInsurAppl.getApplState().getInsurDur() == null
				|| grpInsurAppl.getApplState().getInsurDur() == 0) {
				arg1.rejectValue("applState", "要约保险期间为0或为空.");
				return;
			}
			//保险期间应小于1000
			if (grpInsurAppl.getApplState().getInsurDur()>=1000) {
				arg1.rejectValue("applState", "要约保险期间应小于1000.");
				return;
			}
			if(ValidatorUtils.testInsurDurUnit(grpInsurAppl.getApplState().getInsurDurUnit())){
				arg1.rejectValue("applState", "要约保险期间类型不符合字典检查.");
				return;
			}
			//主险与附加险的保险期间的校验
			if (grpInsurAppl.getApplState().getPolicyList().size()>1){
				long mDays=0;
				long mInsurDur=0;
				if(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur()!=null){
					mInsurDur=grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur();
				}
				switch (grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDurUnit()) {
				case "Y":
					mDays=mInsurDur*360;
					break;
				case "M":
					mDays=mInsurDur*30;
					break;
				case "D":
					mDays=mInsurDur;
					break;

				default:
					break;
				}
				for (int i = 1; i < grpInsurAppl.getApplState().getPolicyList().size(); i++) {
					long cDays=0;
					long cInsurDur=0;
					if(grpInsurAppl.getApplState().getPolicyList().get(i).getInsurDur()!=null){
						cInsurDur=grpInsurAppl.getApplState().getPolicyList().get(i).getInsurDur();
					}
					
					switch (grpInsurAppl.getApplState().getPolicyList().get(i).getInsurDurUnit()) {
					case "Y":
						cDays=cInsurDur*360;
						break;
					case "M":
						cDays=cInsurDur*30;
						break;
					case "D":
						cDays=cInsurDur;
						break;

					default:
						break;
					}
					if (cDays>mDays){
						arg1.rejectValue("applState", "附加险"+i+"的保险期间不能大于主险的保险期间");
					}
				}
			}
		}
		List<String> list = new ArrayList<>();
		for (int i = 0; i < grpInsurAppl.getApplState().getPolicyList().size(); i++) {
			Policy policy = grpInsurAppl.getApplState().getPolicyList().get(i);
			// 校验保险期类型
			if (ValidatorUtils.testInsurDurUnit(policy.getInsurDurUnit())) {
				arg1.rejectValue("applState", "保险期类型不符合字典检查.");
			}
			if (StringUtils.isEmpty(policy.getPolCode())) {
				arg1.rejectValue("applState", "险种为空.");
			}
			list.add(policy.getPolCode());
			int j = list.indexOf(policy.getPolCode());
			if (i != j) {
				arg1.rejectValue("applState", "险种代码重复.");
			}
			/* 对险种进行校验 */
			// 保单基本信息提交校验-产品编码联调
			// 获取第一个险种，判断是不是主险，.获取第一个险种，判断是不是主险，
			// 校验险种
			// 产品组查询赋值
			Map<String, Object> polMap=new HashMap<>();
			polMap.put("polCode",policy.getPolCode());
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			resulpolicyQueryService=SpringContextHolder.getBean("resulpolicyQueryService");
			Map<String, Object> polBoMap = resulpolicyQueryService.excute(polMap);
			if (polBoMap.get("policyBo") == null) {
				arg1.rejectValue("applState", "查询险种概要,没有引用的对象或集合为空.");
			} else {
				JSONObject obj = JSONObject.parseObject(JSON.toJSONString(polBoMap));
				String mrCode=obj.getJSONObject("policyBo").getString("mrCode");
				if(i==0 && !StringUtils.equals(mrCode,MR_CODE.MASTER.getKey())){
					arg1.rejectValue("applState","第一险种不是主险种|");
				}
				//不是第一主险的全部赋值为附加险
				if(i==0){
					policy.setMrCode(mrCode);
				}
				if(i!=0){
					policy.setMrCode(MR_CODE.RIDER.getKey());
				}
				// 2.1获取险种名称polNameChn
				String polNameStr = obj.getJSONObject("policyBo").getString("polNameChn");
				if(polNameStr.contains("建筑")){
					validateConstructInsurInfo(grpInsurAppl, arg1);
				}
//				else if(null!=grpInsurAppl.getConstructInsurInfo()){
//					arg1.rejectValue("constructInsurInfo", "险种不为建工险，建工险信息应为空.");
//				}
				policy.setPolNameChn(polNameStr);
				if (grpInsurAppl.getPaymentInfo() == null
						|| ValidatorUtils.testCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode())) {
					arg1.rejectValue("applState", "币种不符合字典检查.");
				} else {
					// 获取币种
					String currencyCodeStr = obj.getJSONObject("policyBo").getString("currencyCode");
					// //判断币种是否一致
					if (!StringUtils.equals(grpInsurAppl.getPaymentInfo().getCurrencyCode(), currencyCodeStr)) {
						arg1.rejectValue("applState", "币种不一致");
					} // end if-判断币种是否一致
				} // end if-判断币种是否为空
					// 判断险种是否可销售，开售日期<=当前的日期<= 停手日期
				String endTimeStr = obj.getJSONObject("policyBo").getString("polEndDate");
				String beginTimeStr = obj.getJSONObject("policyBo").getString("polStartDate");
				SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				Date endDate = null;
				Date beginDate = null;
				try {
					endDate = formate.parse(endTimeStr);
				} catch (ParseException e) {
					logger.error("停售时间解析错误.");
					throw new RuntimeException(e);
				}
				try {
					beginDate = formate.parse(beginTimeStr);
				} catch (ParseException e) {
					logger.error("开办时间解析错误.");
					throw new RuntimeException(e);
				}
				if (!(beginDate.getTime() <= date.getTime() && date.getTime() <= endDate.getTime())) {
					// 停售
					arg1.rejectValue("applState", policy.getPolCode()+"已停售.");
				}
				// //2.2判断子险种，查询“子险种概要”获取子险种名称；
				CxfHeader subheaderInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(subheaderInfo);
				resulsubPolicyService=SpringContextHolder.getBean("resulsubPolicyService");
				Map<String, Object> subPolBoMap = resulsubPolicyService.excute(polMap);
				JSONArray arr = (JSONArray) subPolBoMap.get("subPolicyBos");
				int m = 0;
				for(;m<arr.size();m++){
					if(arr.getJSONObject(m).getString("subPolCode").contains("-"))break;
				}
				if(m<arr.size()&&(policy.getSubPolicyList()==null || policy.getSubPolicyList().isEmpty())){
					arg1.rejectValue("applState", "所选险种中包含子险种时，子险种必录.");
				}
				if (null != policy.getSubPolicyList()) {
					for (SubPolicy sub : policy.getSubPolicyList()) {
						int p = 0;
						for (Object object : arr) {
							JSONObject ob = (JSONObject) object;
							if (StringUtils.equals(ob.getString("subPolCode"),sub.getSubPolCode())) {
								sub.setSubPolName(ob.getString("subPolName"));
								++p;
							}
						}
						if (p == 0) {
							arg1.rejectValue("applState", sub.getSubPolCode() + "子险种不存在");
						}

					}
				}
			}
			// 校验专项业务标识
			if (!StringUtils.isEmpty(policy.getSpeciBusinessLogo())
					&& ValidatorUtils.testSpeciBusinessLogo(policy.getSpeciBusinessLogo())) {
				arg1.rejectValue("applState", "专项业务标识不符合字典检查.");
			}
			// 校验保险期
			if (policy.getInsurDur() == null || policy.getInsurDur() == 0) {
				arg1.rejectValue("applState", "保险期间为空.");
			}
			if (policy.getFaceAmnt() == null || (policy.getFaceAmnt().compareTo(0.0) == 0)) {
				arg1.rejectValue("applState", "险种保额为空或为0.");
			}
			if ((policy.getPremium() == null) || (policy.getPremium().compareTo(0.0) == 0)) {
				arg1.rejectValue("applState", "实际保费为空或为0.");
			}
			if ((policy.getPolIpsnNum() == null) || (policy.getPolIpsnNum() == 0)) {
				arg1.rejectValue("applState", "险种投保人数为空或为0.");
			}
			// 保费、标准保费、保额累计
			if (policy.getFaceAmnt() != null) {
				sumPoicyFaceAmnt += policy.getFaceAmnt();
			} else {
				policy.setFaceAmnt(0.0);
			}
			if (policy.getPremium() != null) {
				sumPoicyPremium += policy.getPremium();
			} else {
				policy.setPremium(0.0);
			}

			if (policy.getSubPolicyList() != null && !policy.getSubPolicyList().isEmpty()) {
				Double sumSubPoicyFaceAmnt = 0.00, sumSubPolicyPremium = 0.00, sumSubPoicyStdPremium = 0.00;
				for (int k = 0; k < policy.getSubPolicyList().size(); k++) {
					SubPolicy subPolicy = policy.getSubPolicyList().get(k);
					if (StringUtils.isEmpty(subPolicy.getSubPolCode())) {
						arg1.rejectValue("applState", "子险种为空.");
					} else {
						if (policy.getSubPolicyList().lastIndexOf(subPolicy) != k) {
							arg1.rejectValue("applState", "子险种重复.");
						}
					}
					if (subPolicy.getSubPolAmnt() == null || (subPolicy.getSubPolAmnt().compareTo(0.0) == 0)) {
						arg1.rejectValue("applState", "子险种保额为空或为零.");
					}
					if (subPolicy.getSubPremium() == null || subPolicy.getSubPremium().compareTo(0.0) == 0) {
						arg1.rejectValue("applState", "子险种实际保费为空或为0.");
					}
					if(null == subPolicy.getSubStdPremium() || subPolicy.getSubStdPremium().compareTo(0.0) == 0){
						subPolicy.setSubStdPremium(subPolicy.getSubPremium());
					}
					// 子险种保费、标准保费、保额累计
					sumSubPoicyFaceAmnt += subPolicy.getSubPolAmnt();
					sumSubPolicyPremium += subPolicy.getSubPremium();
					sumSubPoicyStdPremium += subPolicy.getSubStdPremium();
				}
				if (null==policy.getStdPremium() || Math.abs(policy.getStdPremium())<0.01){
					policy.setStdPremium(sumSubPoicyStdPremium);
				}
				if (Math.abs(policy.getFaceAmnt()-sumSubPoicyFaceAmnt)>0.01
					|| Math.abs(policy.getPremium()-sumSubPolicyPremium)>0.01) {
					arg1.rejectValue("applState",
							"险种保额:" + policy.getFaceAmnt() + "、保费:" + policy.getPremium()
							+ "跟子险种保额:" + sumSubPoicyFaceAmnt + "、保费:" + sumSubPolicyPremium + "不一致.");
				}
			}else{
				if (null==policy.getStdPremium() || Math.abs(policy.getStdPremium())<0.01){
					policy.setStdPremium(policy.getPremium());
				}
			}
			if(Math.abs(policy.getPremDiscount())<0.01){
				policy.setPremDiscount(BigDecimalUtils.keepPrecisionDouble((policy.getStdPremium()-policy.getPremium())*100/policy.getStdPremium(),2));
			}
		}
		if (grpInsurAppl.getApplState().getSumFaceAmnt() == null
				|| 0.01 > Math.abs(grpInsurAppl.getApplState().getSumFaceAmnt())) {
			grpInsurAppl.getApplState().setSumFaceAmnt(sumPoicyFaceAmnt);
		}

		if (null == grpInsurAppl.getApplState().getSumFaceAmnt() || null == grpInsurAppl.getApplState().getSumPremium()
				|| Math.abs(grpInsurAppl.getApplState().getSumFaceAmnt()-sumPoicyFaceAmnt) >= 0.01
				|| Math.abs(grpInsurAppl.getApplState().getSumPremium()-sumPoicyPremium) >= 0.01) {

			arg1.rejectValue("applState",
					"要约保额: " + grpInsurAppl.getApplState().getSumFaceAmnt() + "、保费: "
							+ grpInsurAppl.getApplState().getSumPremium() + "跟险种累计的保额: " + sumPoicyFaceAmnt + "、保费: "
							+ sumPoicyPremium + "不一致.或者保额、保费为空");
		}

	}

	/*
	 * 校验要约分组
	 */
	public void validateIpsnStateGrpList(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if (grpInsurAppl.getIpsnStateGrpList() == null || grpInsurAppl.getIpsnStateGrpList().isEmpty()) {
			return;
		}
		List<Long> ipsnGrpNos = new ArrayList<>();
		for (int i = 0; i < grpInsurAppl.getIpsnStateGrpList().size(); i++) {
			
			IpsnStateGrp ipsnStateGrp = grpInsurAppl.getIpsnStateGrpList().get(i);
			//增加集合内元素属性非空判断
			if (StringUtils.equals("{}",JSON.toJSONString(ipsnStateGrp))){
				grpInsurAppl.getIpsnStateGrpList().remove(i);
				i--;
				continue;
			}
			if (StringUtils.isEmpty(ipsnStateGrp.getIpsnGrpType())) {
				ipsnStateGrp.setIpsnGrpType(IPSN_GRP_TYPE.SAME_AMNT_PREMIUMS.getKey());
			}else if(ValidatorUtils.testIpsnGrpType(ipsnStateGrp.getIpsnGrpType())){
				arg1.rejectValue("ipsnStateGrpList", "要约分组类型不符合字典检查.");
			}
			if (ipsnStateGrp.getIpsnGrpNo() == null || ipsnStateGrp.getIpsnGrpNo() == 0) {
				ipsnGrpNos.add(-1l);
				arg1.rejectValue("ipsnStateGrpList", "要约属组编号为空或为0.");
			} else {
				ipsnGrpNos.add(ipsnStateGrp.getIpsnGrpNo());
				int j = ipsnGrpNos.indexOf(ipsnStateGrp.getIpsnGrpNo());
				if (i != j) {
					arg1.rejectValue("ipsnStateGrpList", "要约属组编号重复.");
				}
			}
			if (StringUtils.isEmpty(ipsnStateGrp.getIpsnGrpName())) {
				arg1.rejectValue("ipsnStateGrpList", "要约属组名称为空.");
			}
			if (ipsnStateGrp.getIpsnGrpNum() == null || ipsnStateGrp.getIpsnGrpNum() == 0) {
				arg1.rejectValue("ipsnStateGrpList", "要约分组分组类型为保额保费一致时要约属组人数为空或为0.");
			}
			if (!StringUtils.isEmpty(ipsnStateGrp.getIpsnOccCode())
				&& !StringUtils.isEmpty(ipsnStateGrp.getOccClassCode())
				&& !StringUtils.equals(ipsnStateGrp.getOccClassCode(), ipsnStateGrp.getIpsnOccCode().substring(0,2))){
				arg1.rejectValue("ipsnStateGrpList", "要约分组职业代码与所选行业不符");
			}
			if (ipsnStateGrp.getGrpPolicyList() == null || ipsnStateGrp.getGrpPolicyList().isEmpty()) {
				arg1.rejectValue("ipsnStateGrpList", "要约分组险种列表为空.");
				return;
			} else {
				for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
					if (StringUtils.isEmpty(grpPolicy.getPolCode())) {
						arg1.rejectValue("ipsnStateGrpList", "要约属组险种中险种代码为空.");
					}
				}
			}
			//如果集合为空则置为null
			if(grpInsurAppl.getIpsnStateGrpList().isEmpty()){
				grpInsurAppl.setIpsnStateGrpList(null);
			}
		}
		
		//保存子险种的信息
		List<SubPolicy> sumSubPolicy=new ArrayList<SubPolicy>();
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			if(policy.getSubPolicyList()==null||policy.getSubPolicyList().size()==0){
				SubPolicy tmp=new SubPolicy();
				tmp.setSubPolCode(policy.getPolCode());
				tmp.setSubPremium(policy.getPremium());
				tmp.setSubPolAmnt(policy.getFaceAmnt());
				tmp.setSubStdPremium(policy.getStdPremium());
				sumSubPolicy.add(tmp);
				continue;
			}
			for(SubPolicy subPolicy:policy.getSubPolicyList()){
				SubPolicy tmp1=new SubPolicy();				
				tmp1.setSubPolCode(subPolicy.getSubPolCode());
				tmp1.setSubPremium(subPolicy.getSubPremium());
				tmp1.setSubPolAmnt(subPolicy.getSubPolAmnt());
				tmp1.setSubStdPremium(subPolicy.getSubStdPremium());
				sumSubPolicy.add(tmp1);
			}			
		}
		//处理公共保额、公共保费
		//公共保额
		Double sumAmnt = 0.0;
		//公共保费
		Double comInsrPrem = 0.0;
		if(grpInsurAppl.getHealthInsurInfo()!=null && grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt()!=null){
			sumAmnt = grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt();
		}else if(grpInsurAppl.getHealthInsurInfo()!=null && grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()!=null){
			sumAmnt = grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt();
		}
		if(grpInsurAppl.getHealthInsurInfo()!=null && grpInsurAppl.getHealthInsurInfo().getComInsrPrem()!=null){
			comInsrPrem = grpInsurAppl.getHealthInsurInfo().getComInsrPrem();
		}
		if(grpInsurAppl.getApplState()==null){
			return;
		}
		if(grpInsurAppl.getApplState().getPolicyList()==null){
			return;
		}
		if(grpInsurAppl.getApplState().getPolicyList().isEmpty()){
			return;
		}
		//获取需添加公共保额，公共保费的险种代码
		String policyNo = grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode();
		if(grpInsurAppl.getApplState().getPolicyList().get(0).getSubPolicyList()!=null){
			List<SubPolicy> subPolicyList = grpInsurAppl.getApplState().getPolicyList().get(0).getSubPolicyList();
			int i = -1;
			for(SubPolicy subPolicy:subPolicyList){
				String subPolicyNo = subPolicy.getSubPolCode();
				if(subPolicyNo.length()>=5 && i==-1){
					i = Integer.valueOf(subPolicyNo.substring(4));
					policyNo = subPolicy.getSubPolCode();
				}
				if(subPolicyNo.length()>=5 && i>Integer.valueOf(subPolicyNo.substring(4))){
					i = Integer.valueOf(subPolicyNo.substring(4));
					policyNo = subPolicy.getSubPolCode();
				}
			}
		}
		//检验要约分组的子险种信息是否一致
		for(SubPolicy subPolicy:sumSubPolicy){
			Double sumFaceAmnt=0.00;
			Double sumPremium=0.00;
			Double sumStdPremium=0.00;
			//添加公共保额、保费
			if(StringUtils.equals(policyNo, subPolicy.getSubPolCode())){
				sumFaceAmnt += sumAmnt;
				sumPremium += comInsrPrem;
				sumStdPremium += comInsrPrem;
			}
			Long ipsnGrpNum=0L;
			for(IpsnStateGrp ipsnStateGrp:grpInsurAppl.getIpsnStateGrpList()){
				if(StringUtils.equals(IPSN_GRP_TYPE.PAY_SAME_PROPORT.getKey(),ipsnStateGrp.getIpsnGrpType())){
					return;
				}
				ipsnGrpNum=ipsnStateGrp.getIpsnGrpNum()==null?0l:ipsnStateGrp.getIpsnGrpNum();
				for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
					if(StringUtils.equals(grpPolicy.getPolCode(), subPolicy.getSubPolCode())){
						 sumFaceAmnt += grpPolicy.getFaceAmnt()*ipsnGrpNum;
						 sumPremium += grpPolicy.getPremium()*ipsnGrpNum;
						 sumStdPremium += grpPolicy.getStdPremium()*ipsnGrpNum;
					}
				}				
			}
			if(Math.abs(subPolicy.getSubPolAmnt()-sumFaceAmnt)>=0.01){				
				arg1.rejectValue("ipsnStateGrpList","险种代码: " + subPolicy.getSubPolCode()  + "的保额不一致");
			}
			if(Math.abs(subPolicy.getSubPremium()-sumPremium)>=0.01){
				arg1.rejectValue("ipsnStateGrpList","险种代码: " + subPolicy.getSubPolCode()  + "的实际保费不一致");
			}
			if(Math.abs(subPolicy.getSubStdPremium()-sumStdPremium)>=0.01){
				arg1.rejectValue("ipsnStateGrpList","险种代码: " + subPolicy.getSubPolCode()  + "的标准保费不一致");
			}
		}
	}

	/**
	 * 校验团体收费
	 * 
	 * @param grpInsurAppl
	 */
	public void validateIpsnPayGrp(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if (grpInsurAppl.getIpsnPayGrpList() == null || grpInsurAppl.getIpsnPayGrpList().isEmpty()) {
			return;
		}else if(grpInsurAppl.getPaymentInfo()!=null && StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())){
			arg1.rejectValue("ipsnPayGrpList", "团单保费来源为团体付款时，收费属组应为空.");
			return;
		}
		List<Long> feeGrpNo = new ArrayList<>();
		for (int i = 0; i < grpInsurAppl.getIpsnPayGrpList().size(); i++) {
			IpsnPayGrp ipsnPayGrp = grpInsurAppl.getIpsnPayGrpList().get(i);
			//校验开户银行
			if (!StringUtils.isEmpty(ipsnPayGrp.getBankCode())
				&& !Pattern.matches("[0-9]{4}",ipsnPayGrp.getBankCode())){
				arg1.rejectValue("ipsnPayGrpList", "开户银行代码应为4位的数字.");
			}
			//校验银行帐号
			if (!StringUtils.isEmpty(ipsnPayGrp.getBankaccNo())){
				String s = ValidatorUtils.testBankAccNo(ipsnPayGrp.getBankaccNo());
				if(!StringUtils.isEmpty(s)){
					arg1.rejectValue("ipsnPayGrpList",s);
				}
			}

			if ((ipsnPayGrp.getFeeGrpNo() == null) || (ipsnPayGrp.getFeeGrpNo() == 0)) {
				feeGrpNo.add(-1l);
				arg1.rejectValue("ipsnPayGrpList", "收费属组编号为空.");
			} else {
				feeGrpNo.add(ipsnPayGrp.getFeeGrpNo());
				int j = feeGrpNo.indexOf(ipsnPayGrp.getFeeGrpNo());
				if (i != j) {
					arg1.rejectValue("ipsnPayGrpList", "收费属组编号重复.");
				}
			}
			if (StringUtils.isEmpty(ipsnPayGrp.getFeeGrpName())) {
				arg1.rejectValue("ipsnPayGrpList", "收费属组名称为空.");
			}
			if (ipsnPayGrp.getIpsnGrpNum() == null) {
				arg1.rejectValue("ipsnPayGrpList", "收费属组人数为空.");
			}
			if (StringUtils.isEmpty(ipsnPayGrp.getMoneyinType())) {
				arg1.rejectValue("ipsnPayGrpList", "缴费方式为空.");
			}
			if (StringUtils.equals(MONEYIN_TYPE.TRANSFER.getKey(), ipsnPayGrp.getMoneyinType())) { 

				if (StringUtils.isEmpty(ipsnPayGrp.getBankCode())) {
					arg1.rejectValue("ipsnPayGrpList", "开户银行为空.");
				}
				if (StringUtils.isEmpty(ipsnPayGrp.getBankaccName())) {
					arg1.rejectValue("ipsnPayGrpList", "开户名称为空.");
				}
				if (StringUtils.isEmpty(ipsnPayGrp.getBankaccNo())) {
					arg1.rejectValue("ipsnPayGrpList", "银行账号为空.");
				}
			}
			if (StringUtils.equals(MONEYIN_TYPE.CHECK.getKey(), ipsnPayGrp.getMoneyinType())) {

				if (StringUtils.isEmpty(ipsnPayGrp.getBankChequeNo())) {
					arg1.rejectValue("ipsnPayGrpList", "支票号为空.");
				}

			}
		}
	}

	/**
	 * 校验组织架构树
	 * 
	 * @param grpInsurAppl
	 */
	public void validateOrgTree(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if (grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()) {
			return;
		}
		Double sumPayAmnt = 0D;
		for (int i = 0; i < grpInsurAppl.getOrgTreeList().size(); i++) {
			OrgTree orgTree = grpInsurAppl.getOrgTreeList().get(i);
			
			if (StringUtils.isEmpty(orgTree.getLevelCode())) {
				arg1.rejectValue("orgTreeList", "层级代码为空.");
			} else {
				int j = grpInsurAppl.getOrgTreeList().lastIndexOf(orgTree);
				if (i != j) {
					arg1.rejectValue("orgTreeList", "层级代码重复.");
				}
			}
			if (StringUtils.equals(orgTree.getIsRoot(),YES_NO_FLAG.YES.getKey())) {
				orgTree.setPrioLevelCode("#");
			}
			if (!StringUtils.equals(orgTree.getIsRoot(),YES_NO_FLAG.YES.getKey()) && StringUtils.isEmpty(orgTree.getPrioLevelCode())) {
				arg1.rejectValue("orgTreeList", "上级层级代码为空.");
			}
			if (ValidatorUtils.testYesNo(orgTree.getIsPaid())) {
				arg1.rejectValue("orgTreeList", "是否交费不符合字典检查.");
			}
			if (StringUtils.isEmpty(orgTree.getMtnOpt())) {
				arg1.rejectValue("orgTreeList", "保全选项为空.");
			}
			if (StringUtils.isEmpty(orgTree.getServiceOpt())) {
				arg1.rejectValue("orgTreeList", "服务指派为空.");
			}
			if (StringUtils.isEmpty(orgTree.getNodeType())) {
				arg1.rejectValue("orgTreeList", "节点类型为空.");
			}
			if (StringUtils.isEmpty(orgTree.getIsRoot())) {
				arg1.rejectValue("orgTreeList", "是否根节点为空.");
			}
			if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())) {
				//校验开户银行
				if (!StringUtils.isEmpty(orgTree.getBankCode())
					&& !Pattern.matches("[0-9]{4}", orgTree.getBankCode())){
					arg1.rejectValue("orgTreeList", "开户银行代码必须为4位的数字");
				}
				//校验银行帐号
				if (!StringUtils.isEmpty(orgTree.getBankaccNo())){
					String s = ValidatorUtils.testBankAccNo(orgTree.getBankaccNo());
					if(!StringUtils.isEmpty(s)){
						arg1.rejectValue("orgTreeList", s);
					}
				}
				if (orgTree.getNodePayAmnt() == null) {
					arg1.rejectValue("orgTreeList", "节点交费金额为空");
					orgTree.setNodePayAmnt(0.0);
				}
				if (StringUtils.isEmpty(orgTree.getBankCode())) {
					arg1.rejectValue("orgTreeList", "开户银行为空");
				}
				if (StringUtils.isEmpty(orgTree.getBankaccName())) {
					arg1.rejectValue("orgTreeList", "开户名称为空");
				}
				if (StringUtils.isEmpty(orgTree.getBankaccNo())) {
					arg1.rejectValue("orgTreeList", "银行账号为空");
				}
				orgTree.setMoneyinType(MONEYIN_TYPE.TRANSFER.getKey());
				//如果存在缴费节点
				if(StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())){
					sumPayAmnt += orgTree.getNodePayAmnt();
				}
			} 
			
			if (StringUtils.equals(orgTree.getNodeType(),"0")) {  //如果节点是企业法人
				if (orgTree.getGrpHolderInfo() == null) {
					arg1.rejectValue("orgTreeList", "组织架构树中团体客户信息为空");
				} else {
					if (StringUtils.isEmpty(orgTree.getGrpHolderInfo().getGrpName())) {
						arg1.rejectValue("orgTreeList", "组织架构树中团体客户信息中单位名称为空.");
					}
					if (ValidatorUtils.testGrpIdType(orgTree.getGrpHolderInfo().getGrpIdType())) {
						arg1.rejectValue("orgTreeList", "组织架构树中团体客户信息中团体客户证件类别不符合字典检查.");
					}
					if (StringUtils.isEmpty(orgTree.getGrpHolderInfo().getGrpIdNo())) {
						arg1.rejectValue("orgTreeList", "组织架构树中团体客户信息中团体客户证件号码为空.");
					}
				}
			}
		} // end for
	}

	/**
	 * 校验建工险
	 * 
	 * @param grpInsurAppl
	 */
	public void validateConstructInsurInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if(grpInsurAppl.getConstructInsurInfo()==null){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，建工险信息为空.");
			return;
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getIobjName())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，工程名称为空.");
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getProjLoc())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，工程地址为空.");
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getProjType())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，工程类型为空.");
		}
		if(ValidatorUtils.testPremCalType(grpInsurAppl.getConstructInsurInfo().getPremCalType())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，保费计算方式不符合字典检查.");
		}
		//按面积计算，总面积为必录，总造价为非必录
		if(StringUtils.equals(grpInsurAppl.getConstructInsurInfo().getPremCalType(), PREM_CAL_TYPE.AREA_CALCULATION.getKey())){
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjSize()
				||Math.abs(grpInsurAppl.getConstructInsurInfo().getIobjSize())<=0.0){
				arg1.rejectValue("constructInsurInfo", "险种为建工险，工程总面积为空或为0.");
			}
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjCost()){
				grpInsurAppl.getConstructInsurInfo().setIobjCost(0.0);
			}
		}
		//按造价计算，总造价为必录，总面积为非必录
		if(StringUtils.equals(grpInsurAppl.getConstructInsurInfo().getPremCalType(), PREM_CAL_TYPE.COST_CALCULATION.getKey())){
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjCost()
				||Math.abs(grpInsurAppl.getConstructInsurInfo().getIobjCost())<=0.0){
				arg1.rejectValue("constructInsurInfo", "险种为建工险，工程总造价为空或为0.");
			}
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjSize()){
				grpInsurAppl.getConstructInsurInfo().setIobjSize(0.0);
			}
		}
		//按人数计算，总面积、总造价为非必录
		if(StringUtils.equals(grpInsurAppl.getConstructInsurInfo().getPremCalType(), PREM_CAL_TYPE.NUMBER_CALCULATION.getKey())){
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjCost()){
				grpInsurAppl.getConstructInsurInfo().setIobjCost(0.0);
			}
			if(null==grpInsurAppl.getConstructInsurInfo().getIobjSize()){
				grpInsurAppl.getConstructInsurInfo().setIobjSize(0.0);
			}
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getProjLocType())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，工程位置类别为空.");
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getConstructFrom()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，施工期间始为空.");
			return;
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getConstructTo()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，施工期间止为空.");
			return;
		}
		if(null==grpInsurAppl.getApplState()){
			return;
		}
		if(null!=grpInsurAppl.getApplState().getDesignForceDate()
			&& grpInsurAppl.getApplState().getDesignForceDate().before(grpInsurAppl.getConstructInsurInfo().getConstructFrom())
			&& grpInsurAppl.getApplState().getDesignForceDate().after(grpInsurAppl.getConstructInsurInfo().getConstructTo())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，指定生效日不在施工期间内.");
			return;
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getFloorHeight()){
			grpInsurAppl.getConstructInsurInfo().setFloorHeight(0.0);
		}else if(grpInsurAppl.getConstructInsurInfo().getFloorHeight()>99.99){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，层高(米)应小于99.99米.");
		}
		
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，企业资质为空.");
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getAwardGrade())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，获奖情况为空.");
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getSafetyFlag())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，是否有安防措施为空.");
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getDiseaDeathNums()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，疾病死亡人数为空.");
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getDiseaDisableNums()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，疾病伤残人数为空.");
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getAcdntDeathNums()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，意外死亡人数为空.");
		}
		if(null==grpInsurAppl.getConstructInsurInfo().getAcdntDisableNums()){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，意外伤残人数为空.");
		}
		if(StringUtils.isEmpty(grpInsurAppl.getConstructInsurInfo().getSaftyAcdntFlag())){
			arg1.rejectValue("constructInsurInfo", "险种为建工险，过去二年内是否有四级以上安全事故为空.");
		}
	}

	/**
	 * 校验健康险
	 * 
	 * @param grpInsurAppl
	 */
	public void validateHealthInsurInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if(null==grpInsurAppl.getHealthInsurInfo()){
			return;
		}
		if(null==grpInsurAppl.getPaymentInfo()){
			return;
		}
		//默认值“不选择公共保额”,如果不为空则进行字典值校验
		if(StringUtils.isEmpty(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
			grpInsurAppl.getHealthInsurInfo().setComInsurAmntUse(COMLNSUR_AMNT_USE.NCHOOS_PUB_COVERAGE.getKey());
		}else if(ValidatorUtils.testComlnsurAmntUse(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse())){
			arg1.rejectValue("healthInsurInfo", "健康险.公共保额使用范围不符合字典检查.");
		}
		//if 公共保额使用范围！=“不选择公共保额”
		if(!StringUtils.equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse(),COMLNSUR_AMNT_USE.NCHOOS_PUB_COVERAGE.getKey())){
			//对公共保额使用形式进行字典值校验
			if(ValidatorUtils.testComlnsurAmntType(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())){
				arg1.rejectValue("healthInsurInfo", "健康险.公共保额使用形式不符合字典检查.");
			}
			//必须是团体交费
			if(!StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())){
				arg1.rejectValue("healthInsurInfo", "健康险.健康险公共保额只支持团体交费.");
			}
			//不支持组织架构树
			if(null!=grpInsurAppl.getOrgTreeList()){
				arg1.rejectValue("healthInsurInfo", "健康险.健康险公共保额不支持组织架构树.");
			}
			//清单标记为档案清单或者无清单时，不允许录入公共保额
			if(!StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ORDINARY_LIST.getKey())){
				arg1.rejectValue("healthInsurInfo", "健康险.清单标记为档案清单或者无清单时，不允许录入公共保额.");
			}
		}
		//if 公共保额使用形式==“固定公共保额”，对公共保费，固定公共保额进行校验
		if(StringUtils.equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(),COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey())){
			if(null==grpInsurAppl.getHealthInsurInfo().getComInsrPrem()
					|| Double.compare(grpInsurAppl.getHealthInsurInfo().getComInsrPrem(),0.0)<0){
				arg1.rejectValue("healthInsurInfo", "健康险.公共保费输入不正确.");
			}
			if(null == grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt()
				|| Double.compare(grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt(),0.0)<0){
				arg1.rejectValue("healthInsurInfo", "健康险.选择固定公共保额，固定公共保额输入不正确.");
			}
		}
		//if 公共保额使用形式==“浮动公共保额”，对公共保费，人均浮动公共保额、人均浮动比例进行校验
		if(StringUtils.equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(),COMLNSUR_AMNT_TYPE.FLOAT_INSURED.getKey())){
			if((null==grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()
				|| Double.compare(grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt(),0.0)<0)
				&& (null==grpInsurAppl.getHealthInsurInfo().getFloatInverse()
					|| Double.compare(grpInsurAppl.getHealthInsurInfo().getFloatInverse(),0.0)<=0
					|| Double.compare(grpInsurAppl.getHealthInsurInfo().getFloatInverse(),1.0)>0)){
				arg1.rejectValue("healthInsurInfo", "健康险.选择浮动公共保额，人均浮动公共保额、人均浮动比例至少要填一项");
			}
		}
	}

	/**
	 * 校验基金险
	 * 
	 * @param grpInsurAppl
	 */
	public void validateFundInsurInfo(GrpInsurAppl grpInsurAppl, Errors arg1) {
		if(null==grpInsurAppl.getFundInsurInfo()){
			return;
		}
		if(null == grpInsurAppl.getApplState()){
			return;
		}
		//基金险组织架构树校验
		if(null != grpInsurAppl.getOrgTreeList() && !grpInsurAppl.getOrgTreeList().isEmpty()){
			arg1.rejectValue("fundInsurInfo", "基金险不能包含组织架构树.");
		}
		//基金险保费来源校验
		if(grpInsurAppl.getPaymentInfo()!=null && !StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),PREM_SOURCE.GRP_ACC_PAY.getKey())){
			arg1.rejectValue("fundInsurInfo", "基金险只能是团体账户付款.");
		}
		//总保费
		double sumPremium = grpInsurAppl.getApplState().getSumPremium()==null?0:grpInsurAppl.getApplState().getSumPremium();
		//总保额
		double sumFaceAmnt = grpInsurAppl.getApplState().getSumFaceAmnt()==null?0:grpInsurAppl.getApplState().getSumFaceAmnt();
		//管理费记提方式
		if(ValidatorUtils.testAdminFeeCopuType(grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType())){
			arg1.rejectValue("fundInsurInfo", "基金险.管理费记提方式不符合字典检查.");
		}
		//管理费比例
		double adminFeePct = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getAdminFeePct()){
			arg1.rejectValue("fundInsurInfo", "基金险.管理费比例为空.");
		}else{
			adminFeePct = grpInsurAppl.getFundInsurInfo().getAdminFeePct();
		}
		//个人账户总保费
		double ipsnFundPremium = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()){
			arg1.rejectValue("fundInsurInfo", "基金险.个人账户缴费金额为空.");
		}else{
			ipsnFundPremium = grpInsurAppl.getFundInsurInfo().getIpsnFundPremium();
		}
		//记入个人账户总金额
		double ipsnFundAmnt = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt()){
			arg1.rejectValue("fundInsurInfo", "基金险.计入个人账户金额为空.");
		}else{
			ipsnFundAmnt = grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt();
		}
		//公共账户总保费
		double sumFundPremium = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getSumFundPremium()){
			arg1.rejectValue("fundInsurInfo", "基金险.公共账户缴费金额为空.");
		}else{
			sumFundPremium = grpInsurAppl.getFundInsurInfo().getSumFundPremium();
		}
		//记入公共账户总金额
		double sumFundAmnt = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getSumFundAmnt()){
			arg1.rejectValue("fundInsurInfo", "基金险.计入公共账户缴费金额为空.");
		}else{
			sumFundAmnt = grpInsurAppl.getFundInsurInfo().getSumFundAmnt();
		}
		//首期管理费总金额
		double accSumAdminBalance = 0.0;
		if(null==grpInsurAppl.getFundInsurInfo().getAccSumAdminBalance()){
			arg1.rejectValue("fundInsurInfo", "基金险.首期管理费总金额为空.");
		}else{
			accSumAdminBalance = grpInsurAppl.getFundInsurInfo().getAccSumAdminBalance();
		}
		/**
		 * 如果管理费提取方式选择的是P，则按照如下来进行计算：
		 * 首期管理费金额=总保费*管理费比例/100
		 * 记入个人账户总金额=个人账户总保费*(1-管理费比例/100)
		 * 记入公共账户总金额=公共账户总保费*(1-管理费比例/100)
		 */
		if(StringUtils.equals(grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType(), ADMIN_FEE_COPU_TYPE.ACCOUNTS_PREMIUM.getKey())){
			if(Math.abs(Double.compare(accSumAdminBalance, sumPremium*adminFeePct/100))>=0.01){
				arg1.rejectValue("fundInsurInfo", "首期管理费金额≠总保费*管理费比例/100.");
			}
			if(Math.abs(Double.compare(ipsnFundAmnt, ipsnFundPremium*(1-adminFeePct/100)))>=0.01){
				arg1.rejectValue("fundInsurInfo", "记入个人账户总金额≠个人账户总保费*(1-管理费比例/100).");
			}
			if(Math.abs(Double.compare(sumFundAmnt, sumFundPremium*(1-adminFeePct/100)))>=0.01){
				arg1.rejectValue("fundInsurInfo", "记入公共账户总金额≠公共账户总保费*(1-管理费比例/100).");
			}
		}
		/**
		 * 如果管理费提取方式选择的是A，则按照如下来进行计算：
		 * 首期管理费金额=总保额*管理费比例/100
		 * 记入个人账户总金额=个人账户总保费/(1+管理费比例/100)
		 * 记入公共账户总金额=公共账户总保费/(1+管理费比例/100)
		 */
		if(StringUtils.equals(grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType(), ADMIN_FEE_COPU_TYPE.AMOUNT_CALCULATED.getKey())){
			if(Math.abs(Double.compare(accSumAdminBalance, sumFaceAmnt*adminFeePct/100))>=0.01){
				arg1.rejectValue("fundInsurInfo", "首期管理费金额≠总保额*管理费比例/100.");
			}
			if(Math.abs(Double.compare(ipsnFundAmnt, ipsnFundPremium*(1-adminFeePct/100)))>=0.01){
				arg1.rejectValue("fundInsurInfo", "记入个人账户总金额≠个人账户总保费*(1-管理费比例/100).");
			}
			if(Math.abs(Double.compare(sumFundAmnt, sumFundPremium*(1-adminFeePct/100)))>=0.01){
				arg1.rejectValue("fundInsurInfo", "记入公共账户总金额≠公共账户总保费*(1-管理费比例/100).");
			}
		}
		/**
		 * 总保额，总保费校验
		 */
		if(Math.abs(Double.compare(sumPremium,ipsnFundPremium+sumFundPremium))>=0.01){
			arg1.reject("fundInsurInfo","总保费≠个人账户总保费+公共账户总保费");
		}
		if(Math.abs(Double.compare(sumFaceAmnt,ipsnFundAmnt+sumFundAmnt))>=0.01){
			arg1.reject("fundInsurInfo","总保额≠记入个人账户总金额+记入公共账户总金额");
		}
	}

	/* 团单信息默认值初始化 */
	// 团单信息添加默认值
	private void writeDefaultValue(GrpInsurAppl grpInsurAppl) {
		//如果内置对象为空对象则置为null
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getApplState()))){
			grpInsurAppl.setApplState(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getConstructInsurInfo()))){
			grpInsurAppl.setConstructInsurInfo(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getConventions()))){
			grpInsurAppl.setConventions(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getFundInsurInfo()))){
			grpInsurAppl.setFundInsurInfo(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getGrpHolderInfo()))){
			grpInsurAppl.setGrpHolderInfo(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getHealthInsurInfo()))){
			grpInsurAppl.setHealthInsurInfo(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getPaymentInfo()))){
			grpInsurAppl.setPaymentInfo(null);
		}
		if (StringUtils.equals("{}", JSON.toJSONString(grpInsurAppl.getPsnListHolderInfo()))){
			grpInsurAppl.setPsnListHolderInfo(null);
		}
		
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
		if (grpInsurAppl.getApplState() != null && grpInsurAppl.getApplState().getPolicyList() != null
				&& !grpInsurAppl.getApplState().getPolicyList().isEmpty()) {
			grpInsurAppl.setFirPolCode(grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());
		}
		// 人工核保标志
		if (StringUtils.isEmpty(grpInsurAppl.getUdwType())) {
			grpInsurAppl.setUdwType(YES_NO_FLAG.NO.getKey());
		}
		PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
		if (paymentInfo == null) {
			paymentInfo = new PaymentInfo();
		}
		// 缴费相关.是否需要续期扣款
		if (StringUtils.isEmpty(paymentInfo.getIsRenew())) {
			paymentInfo.setIsRenew(YES_NO_FLAG.NO.getKey());
		}
		if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())) {
			// 汇缴件号
			if (StringUtils.isEmpty(grpInsurAppl.getSgNo())) {
				grpInsurAppl.setSgNo(grpInsurAppl.getApplNo());
			}
			// 缴费相关.是否多期暂缴
			if (StringUtils.isEmpty(paymentInfo.getIsMultiPay())) {
				paymentInfo.setIsMultiPay(YES_NO_FLAG.NO.getKey());
			}
		}
		// 缴费相关.结算方式
		if (StringUtils.isEmpty(paymentInfo.getStlType())) {
			paymentInfo.setStlType(YES_NO_FLAG.NO.getKey());
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
	}
	private void checkPsnListHolderInfo(GrpInsurAppl grpInsurAppl,Errors arg1){
		if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgName())) {
			arg1.rejectValue("psnListHolderInfo", "汇交人姓名为空.");
		}
		if (ValidatorUtils.testSex(grpInsurAppl.getPsnListHolderInfo().getSgSex())) {
			arg1.rejectValue("psnListHolderInfo", "汇交人性别不符合字典检查.");
		}
		if (grpInsurAppl.getPsnListHolderInfo().getSgBirthDate() == null) {
			arg1.rejectValue("psnListHolderInfo", "汇交人出生日期为空.");
		}
		if (ValidatorUtils.testIdType(grpInsurAppl.getPsnListHolderInfo().getSgIdType())) {
			arg1.rejectValue("psnListHolderInfo", "汇交人证件类型不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgIdNo())) {
			arg1.rejectValue("psnListHolderInfo", "汇交人证件号码为空.");
		}
		// 如果是身份证，则对证件号码进行校验
		if (StringUtils.equals(grpInsurAppl.getPsnListHolderInfo().getSgIdType(), ID_TYPE.ID.getKey())) { 
			String validRet = ValidatorUtils.validIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				arg1.rejectValue("psnListHolderInfo", validRet);
			}else{
				grpInsurAppl.getPsnListHolderInfo().setSgIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo().toUpperCase());
				if (grpInsurAppl.getPsnListHolderInfo().getSgBirthDate()!=null
						&& !StringUtils.equals(DateFormatUtils.format(grpInsurAppl.getPsnListHolderInfo().getSgBirthDate(), "yyyyMMdd"), grpInsurAppl.getPsnListHolderInfo().getSgIdNo().substring(6, 14))){
					arg1.rejectValue("psnListHolderInfo", "汇交人出生日期与身份证不符");
				}
				if (Integer.parseInt(grpInsurAppl.getPsnListHolderInfo().getSgIdNo().substring(16,17))%2 == 0
						&& StringUtils.equals(grpInsurAppl.getPsnListHolderInfo().getSgSex(), SEX.MALE.getKey())){
					arg1.rejectValue("psnListHolderInfo", "汇交人性别与身份证不符");
				}
				if (Integer.parseInt(grpInsurAppl.getPsnListHolderInfo().getSgIdNo().substring(16,17))%2 == 1
						&& StringUtils.equals(grpInsurAppl.getPsnListHolderInfo().getSgSex(), SEX.FEMALE.getKey())){
					arg1.rejectValue("psnListHolderInfo", "汇交人性别与身份证不符");
				}
			}
		}
		if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgMobile())
				&& StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgTelephone())) {
			arg1.rejectValue("psnListHolderInfo", "汇交人固定电话和手机号不能同时为空.");
		}
//		if (StringUtils.equals(grpInsurAppl.getCntrPrintType(),CNTR_PRINT_TYPE.ELEC_INSUR.getKey()) && StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgEmail())) {
//			arg1.rejectValue("psnListHolderInfo", "合同打印方式为电子保单时，汇交人电子邮箱地址为空.");
//		}
		if (grpInsurAppl.getPsnListHolderInfo().getAddress() == null) {
			arg1.rejectValue("psnListHolderInfo", "汇交人地址为空.");
		} else {
			if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getProvince())) {
				arg1.rejectValue("psnListHolderInfo", "汇交人地址中省/自治州为空.");
			}else if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getCity())) {
				arg1.rejectValue("psnListHolderInfo", "汇交人地址中市/州为空.");
			}else if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getHomeAddress())) {
				arg1.rejectValue("psnListHolderInfo", "汇交人地址明细为空.");
			}else if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getCounty())) {
				arg1.rejectValue("psnListHolderInfo", "汇交人地区/县细为空.");
			}else if (StringUtils.isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getPostCode())) {
				arg1.rejectValue("psnListHolderInfo", "汇交人地址中邮编为空.");
			}else{
				RetInfo retInfo =ValidatorUtils.checkPostCode(grpInsurAppl.getPsnListHolderInfo().getAddress().getPostCode(), grpInsurAppl.getPsnListHolderInfo().getAddress().getProvince());
				if(StringUtils.equals(retInfo.getRetCode(), "0")){
					arg1.rejectValue("psnListHolderInfo", retInfo.getErrMsg());
				}
			}
		}
	}
	private void checkGrpHolderInfo(GrpInsurAppl grpInsurAppl,Errors arg1){
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpName())) {
			arg1.rejectValue("grpHolderInfo", "单位名称为空.");
		}else if(grpInsurAppl.getGrpHolderInfo().getGrpName().length()<2){
			arg1.rejectValue("grpHolderInfo", "单位名称最少两个字符.");
		}
		if (ValidatorUtils.testGrpIdType(grpInsurAppl.getGrpHolderInfo().getGrpIdType())) {
			arg1.rejectValue("grpHolderInfo", "团体客户证件类别不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpIdNo())) {
			arg1.rejectValue("grpHolderInfo", "团体客户证件号码为空.");
		}
		if (ValidatorUtils.testGrpCountryCode(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode())) {
			arg1.rejectValue("grpHolderInfo", "企业注册地国籍不符合字典检查.");
		}
		if (ValidatorUtils.testGrpPsnDeptType(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType())) {
			arg1.rejectValue("grpHolderInfo", "外管局部门类型不符合字典检查.");
		}
		if (ValidatorUtils.testOccClassCode(grpInsurAppl.getGrpHolderInfo().getOccClassCode())) {
			arg1.rejectValue("grpHolderInfo", "行业类别不符合字典检查.");
		}
		if (!StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getNatureCode())
				&& ValidatorUtils.testNatureCode(grpInsurAppl.getGrpHolderInfo().getNatureCode())) {
			arg1.rejectValue("grpHolderInfo", "单位性质（经济分类）不符合字典检查.");
		}
		if (!StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getLegalCode())
				&& ValidatorUtils.testLegalCode(grpInsurAppl.getGrpHolderInfo().getLegalCode())) {
			arg1.rejectValue("grpHolderInfo", "单位性质（法律分类）不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactName())) {
			arg1.rejectValue("grpHolderInfo", "联系人姓名为空.");
		}
		if (ValidatorUtils.testIdType(grpInsurAppl.getGrpHolderInfo().getContactIdType())) {
			arg1.rejectValue("grpHolderInfo", "联系人证件类型不符合字典检查.");
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactIdNo())) {
			arg1.rejectValue("grpHolderInfo", "联系人证件号码为空.");
		}
		if (StringUtils.equals(grpInsurAppl.getGrpHolderInfo().getContactIdType(), ID_TYPE.ID.getKey())) { 
			String s = ValidatorUtils.validIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo());
			if (!StringUtils.equals(s, "OK")) {
				arg1.rejectValue("grpHolderInfo", s);
			}else{
				grpInsurAppl.getGrpHolderInfo().setContactIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo().toUpperCase());
			}
		}
		if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactMobile())) {
			arg1.rejectValue("grpHolderInfo", "联系人手机号不能同时为空.");
		}
		if (StringUtils.equals(grpInsurAppl.getCntrPrintType(),CNTR_PRINT_TYPE.ELEC_INSUR.getKey())
			&& StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getContactEmail())) {
			arg1.rejectValue("grpHolderInfo", "联系人电子邮箱为空.");
		}
		if (grpInsurAppl.getGrpHolderInfo().getAddress() == null) {
			arg1.rejectValue("grpHolderInfo", "团体客户信息中地址为空.");
		} else {

			if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getProvince())) {
				arg1.rejectValue("grpHolderInfo", "汇交人地址中省/自治州为空.");
			}
			if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getCity())) {
				arg1.rejectValue("grpHolderInfo", "汇交人地址中市/州为空.");
			}

			if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getHomeAddress())) {
				arg1.rejectValue("grpHolderInfo", "汇交人地址明细为空.");
			}
			if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getCounty())) {
				arg1.rejectValue("grpHolderInfo", "汇交人地区/县细为空.");
			}
			if (StringUtils.isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode())) {
				arg1.rejectValue("grpHolderInfo", "团体客户信息中邮编为空.");
			}
		}
	}
	//缴费相关添加默认缴费期，缴费单位，添加开户银行，银行帐号校验
	private void checkPaymentInfo(GrpInsurAppl grpInsurAppl,Errors arg1){
		if(grpInsurAppl.getApplState()==null){
			return;
		}
		if(grpInsurAppl.getApplState().getPolicyList() == null||grpInsurAppl.getApplState().getPolicyList().isEmpty()){
			return;
		}
		//缴费期默认值
		if (grpInsurAppl.getPaymentInfo().getMoneyinDur()==null || grpInsurAppl.getPaymentInfo().getMoneyinDur() == 0){
			grpInsurAppl.getPaymentInfo().setMoneyinDur(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur());
		}
		//缴费期要小于1000
		if(grpInsurAppl.getPaymentInfo().getMoneyinDur()>=1000){
			arg1.rejectValue("paymentInfo","缴费期应小于1000");
		}
		//缴费期单位默认值
		if (StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())){
			grpInsurAppl.getPaymentInfo().setMoneyinDurUnit(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDurUnit());
		}
		//开户银行校验
		if (!StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getBankCode())
			&& !Pattern.matches("[0-9]{4}", grpInsurAppl.getPaymentInfo().getBankCode())){
			arg1.rejectValue("paymentInfo","银行代码必须为4位的数字");
		}
		//银行帐号校验
		if (!StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getBankAccNo())){
			String s = ValidatorUtils.testBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());
			if(!StringUtils.isEmpty(s)){
				arg1.rejectValue("paymentInfo",s);
			}
		}
	}
	//对组织架构树中是否包含交费节点进行判断
	private boolean isContainsFeeNode(List<OrgTree> orgTrees){
		if(orgTrees==null || orgTrees.isEmpty()){
			return false;
		}
		for(OrgTree orgTree:orgTrees){
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())){
				return true;
			}
		}
		return false;
	}
	//计算组织架构树所有交费节点，交费金额之和
	private double sumNodePayAmnt(List<OrgTree> orgTrees){
		double sumNodePayAmnt = 0.0;
		for(OrgTree orgTree:orgTrees){
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())){
				if(null == orgTree.getNodePayAmnt()){
					orgTree.setNodePayAmnt(0.0);
				}
				sumNodePayAmnt += orgTree.getNodePayAmnt();
			}
		}
		return sumNodePayAmnt;
	}
	//校验结算日期
	private void checkStlDate(GrpInsurAppl grpInsurAppl,Errors arg1){
		if(grpInsurAppl.getApplState()==null){
			return;
		}
		if(grpInsurAppl.getApplState().getPolicyList()==null
			|| grpInsurAppl.getApplState().getPolicyList().isEmpty()){
			return;
		}
		if(grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDurUnit()==null
			|| grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur()==null){
			return;
		}
		if(grpInsurAppl.getApplState().getDesignForceDate()==null){
			for(Date date:grpInsurAppl.getPaymentInfo().getStlDate()){
				if(date.before(grpInsurAppl.getApplDate())){
					arg1.rejectValue("paymentInfo", "结算方式为组合结算或指定日期结算，未指定生效日时，结算日期必须在投保日期之后.");
					return;
				}
			}
		}else{
			//计算生效截止日
			Date designForceExpDate = DateUtils.addDays(grpInsurAppl.getApplState().getDesignForceDate(),-1);
			switch (grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDurUnit()) {
				case "Y":
					designForceExpDate = DateUtils.addYears(designForceExpDate, grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur());
					break;
				case "M":
					designForceExpDate = DateUtils.addMonths(designForceExpDate, grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur());
					break;
				case "D":
					designForceExpDate = DateUtils.addDays(designForceExpDate, grpInsurAppl.getApplState().getPolicyList().get(0).getInsurDur());
					break;
			}
			for(Date date:grpInsurAppl.getPaymentInfo().getStlDate()){
				if(date.before(grpInsurAppl.getApplState().getDesignForceDate())){
					arg1.rejectValue("paymentInfo", "结算方式为组合结算或指定日期结算，指定生效日时，结算日期必须在指定生效日之后.");
					return;
				}
				if(date.after(designForceExpDate)){
					arg1.rejectValue("paymentInfo", "结算方式为组合结算或指定日期结算，指定生效日时，结算日期必须在生效截止日之前.");
					return;
				}
			}
		}
	}
}