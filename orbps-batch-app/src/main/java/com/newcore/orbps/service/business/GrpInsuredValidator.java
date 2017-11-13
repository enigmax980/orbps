package com.newcore.orbps.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.HldrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.IpsnState;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbpsutils.service.api.PolicyValidationAtomService;
import com.newcore.supports.dicts.APPL_RT_OPSN;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.IPSN_TYPE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.SEX;

public class GrpInsuredValidator{

	private Logger logger = LoggerFactory.getLogger(GrpInsuredValidator.class);
	
	private GrpInsurAppl grpInsurAppl;
	
	private final String NAME_PATTERN = "[0-9a-zA-Z\u4E00-\u9FA5.() （）·-]+";
	
	private Map<String,String> polMinAgeMap;
	
	private Map<String,String> polMaxAgeMap;
	
	private Map<String,String> polSexMap;
	
	@Autowired
	PolicyValidationAtomService policyValidationAtomService;
	
	public String validate(GrpInsured grpInsured) {
		StringBuilder errors = new StringBuilder();
		if (grpInsurAppl == null) {
			errors.append("[投保单号对应的团单基本信息不存在]");
			return errors.toString();
		}
		// boolean
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		/*
		 * 被保人编号不能为空
		 */
		if (grpInsured.getIpsnNo() == null) {
			errors.append("“被保险人编号为空”");
		}
		/*
		 * 与主被保险人关系,被保险人类型,主被保险人编号联合校验
		 */
		if(ValidatorUtils.testIpsnType(grpInsured.getIpsnType())){
			errors.append("“被保险人类型错误”");
		}else if (StringUtils.equals(grpInsured.getIpsnType(),IPSN_TYPE.PRIMARY_INSURED.getKey())) {
			if (!StringUtils.isEmpty(grpInsured.getIpsnRtMstIpsn())
				&& !StringUtils.equals(grpInsured.getIpsnRtMstIpsn(),APPL_RT_OPSN.ME.getKey())) {
				errors.append("“被保险人为主被保险人时，与主被保险人关系必须是本人”");
			} else if (StringUtils.isEmpty(grpInsured.getIpsnRtMstIpsn())) {
				grpInsured.setIpsnRtMstIpsn(APPL_RT_OPSN.ME.getKey());
			}
			// 主被保险人编号
			if (grpInsured.getMasterIpsnNo() == null) {
				grpInsured.setMasterIpsnNo(grpInsured.getIpsnNo());
			}else if(grpInsured.getIpsnNo()!=null && Long.compare(grpInsured.getMasterIpsnNo(), grpInsured.getIpsnNo())!=0){
				errors.append("“被保险人为主被保险人时，主被保险人编号应等于被保险人编号”");
			}
		}else{
			if (ValidatorUtils.testApplRtOpsn(grpInsured.getIpsnRtMstIpsn())) {
				errors.append("“与主被保险人关系错误”");
			}
			// 主被保险人编号
			if (grpInsured.getMasterIpsnNo() == null) {
				errors.append("“主被保险人编号为空”");
			}else if(grpInsured.getIpsnNo()!=null && Long.compare(grpInsured.getMasterIpsnNo(), grpInsured.getIpsnNo())==0){
				errors.append("“被保险人不为主被保险人时，主被保险人编号应不等于被保险人编号”");
			}
		}
		/*
		 * 被保人姓名不能为空
		 */
		if (StringUtils.isEmpty(grpInsured.getIpsnName())) {
			errors.append("“被保险人姓名为空”");
		}else{
			String name = grpInsured.getIpsnName();
			name = name.trim();
			name = name.replaceAll(" +", " ");
			if(name.matches(NAME_PATTERN)){
				grpInsured.setIpsnName(name);
			}else{
				errors.append("“被保险人姓名格式不正确”");
			}
		}

		/*
		 * 校验与投保人关系
		 */
		if (!StringUtils.isEmpty(grpInsured.getRelToHldr())
				&& ValidatorUtils.testApplRtOpsn(grpInsured.getRelToHldr())) {
			errors.append("“与投保人关系错误”");
		}

		/*
		 * 证件类别不能为空
		 */
		if (ValidatorUtils.testIdType(grpInsured.getIpsnIdType())) {
			errors.append("“被保人证件类别错误”");
		}

		// 如果是身份证，则对证件号码进行校验
		if (StringUtils.equals(grpInsured.getIpsnIdType(), ID_TYPE.ID.getKey())
				&& !StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			String validRet = ValidatorUtils.validIdNo(grpInsured.getIpsnIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				errors.append("“");
				errors.append(validRet);
				errors.append("”");
			} else {
				grpInsured.setIpsnIdNo(grpInsured.getIpsnIdNo().toUpperCase());
				// 如果被保人性别为空，则取身份证里的性别和出生日期
				if (StringUtils.isEmpty(grpInsured.getIpsnSex())) {
					if (Integer.parseInt(grpInsured.getIpsnIdNo().substring(16, 17)) % 2 == 0) {
						grpInsured.setIpsnSex(SEX.FEMALE.getKey());
					} else {
						grpInsured.setIpsnSex(SEX.MALE.getKey());
					}
				} else {

					if (Integer.parseInt(grpInsured.getIpsnIdNo().substring(16, 17)) % 2 == 0
							&& StringUtils.equals(grpInsured.getIpsnSex(), SEX.MALE.getKey())) {
						errors.append("“被保人性别与身份证不符”");
					}
					if (Integer.parseInt(grpInsured.getIpsnIdNo().substring(16, 17)) % 2 == 1
							&& StringUtils.equals(grpInsured.getIpsnSex(), SEX.FEMALE.getKey())) {
						errors.append("“被保人性别与身份证不符”");
					}
				}

				if (grpInsured.getIpsnBirthDate() == null) {
					try {
						grpInsured.setIpsnBirthDate(
								new SimpleDateFormat("yyyyMMdd").parse(grpInsured.getIpsnIdNo().substring(6, 14)));
					} catch (ParseException e) {
						logger.error(e.getMessage(), e);
					}
					grpInsured.setIpsnAge(getAgeByBirthday(grpInsured.getIpsnBirthDate()));
				}

				if (grpInsured.getIpsnBirthDate() != null
						&& !StringUtils.equals(DateFormatUtils.format(grpInsured.getIpsnBirthDate(), "yyyyMMdd"),
								grpInsured.getIpsnIdNo().substring(6, 14))) {
					errors.append("“被保人出生日期与身份证不符”");
				}

			}
		}

		/*
		 * 性别不能为空
		 */
		if (ValidatorUtils.testSex(grpInsured.getIpsnSex())) {
			errors.append("“被保险人性别不符”");
		}

		/*
		 * 出生日期不能为空
		 */
		if (grpInsured.getIpsnBirthDate() == null) {
			errors.append("“被保人出生日期为空或格式有误”");
		} else {
			String str = sdf.format(grpInsured.getIpsnBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				errors.append("“被保人出生日期错误”");
			}
		}

		/*
		 * 证件号码不能为空
		 */
		if (StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			errors.append("“被保人证件号码为空”");
			// 证件类别长度为8-18
		} else if (grpInsured.getIpsnIdNo().length() < 8 || grpInsured.getIpsnIdNo().length() > 18) {
			errors.append("“被保人证件号码长度有误”");
		}

		/*
		 * 职业代码不能为空
		 */
		if (StringUtils.isEmpty(grpInsured.getIpsnOccCode())) {
			errors.append("“职业代码为空”");
			// 职业代码长度为6
		} else if (6 != grpInsured.getIpsnOccCode().length()) {
			errors.append("“职业代码长度有误”");
		}
		if (StringUtils.isEmpty(grpInsured.getIpsnOccClassLevel())) {
			errors.append("“职业代码录入不正确”");
		}
		if ((grpInsurAppl.getOrgTreeList()==null || grpInsurAppl.getOrgTreeList().isEmpty())
			&& !StringUtils.isEmpty(grpInsured.getLevelCode())){
			errors.append("“基本信息不存在组织架构树时组织层次代码应为空”");
		}
		/*
		 * 校验在职标志
		 */
		if (!StringUtils.isEmpty(grpInsured.getInServiceFlag())
				&& ValidatorUtils.testYesNo(grpInsured.getInServiceFlag())) {
			errors.append("“在职标志错误”");
		}
		/*
		 * 校验医保标志
		 */
		if (!StringUtils.isEmpty(grpInsured.getIpsnSss()) && ValidatorUtils.testIpsnSss(grpInsured.getIpsnSss())) {
			errors.append("“医保标志错误”");
		}
		/*
		 * 异常告知不能为空
		 */
		if (ValidatorUtils.testYesNo(grpInsured.getNotificaStat())) {
			errors.append("“异常告知错误”");
		}
		// 子要约的险种保费累计金额
		// 子要约险种是否跟基本信息中匹配校验
		double sumPremium = checkSubStateList(grpInsured.getSubStateList(), errors);
		// 个人、单位缴费金额校验|个人付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.PSN_ACC_PAY.getKey())) {
			if (grpInsured.getIpsnPayAmount() == null || grpInsured.getIpsnPayAmount() < 0.001) {
				grpInsured.setIpsnPayAmount(sumPremium);
			} else if (Math.abs(grpInsured.getIpsnPayAmount() - sumPremium) >= 0.001) {
				errors.append("“保费来源为个人付款时，个人缴费金额与子要约的保费累计金额不相等”");
			}
			if (grpInsured.getGrpPayAmount() != null && Math.abs(grpInsured.getGrpPayAmount()) > 0) {
				errors.append("“保费来源为个人付款时，团体缴费金额应为空或为0”");
			}
		}
		// 个人、单位缴费金额校验|非基金险、团体付款
		if (grpInsurAppl.getFundInsurInfo() == null
			&& StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
			if (grpInsured.getGrpPayAmount() == null || grpInsured.getGrpPayAmount() < 0.001) {
				grpInsured.setGrpPayAmount(sumPremium);
			} else if (Math.abs(grpInsured.getGrpPayAmount() - sumPremium) >= 0.001) {
				errors.append("“保费来源为团体付款时，单位缴费金额与子要约的保费累计金额不相等”");
			}
			if (grpInsured.getIpsnPayAmount() != null && Math.abs(grpInsured.getIpsnPayAmount()) > 0) {
				errors.append("“保费来源为团体付款时，个人缴费金额应为空或为0”");
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
				errors.append("“保费来源为团个共同付款时，非基金险个人缴费金额+团体缴费金额!=子要约的保费累计金额”");
			}
			if (grpInsurAppl.getFundInsurInfo() != null
					&& Math.abs(grpInsured.getIpsnPayAmount() - sumPremium) >= 0.01) {
				errors.append("“保费来源为团个共同付款时，基金险个人缴费金额!=子要约的保费累计金额”");
			}
		}
		// 收费属组,交费账户校验 | 团体付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
			if (grpInsured.getFeeGrpNo() != null
					|| (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty())) {
				errors.append("“保费来源为团体付款时，收费属组号，缴费账户应为空”");
			}
		}
		// 收费属组，交费账户校验 | 个人付款，团个共同付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.PSN_ACC_PAY.getKey())
				|| StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),
						PREM_SOURCE.GRP_INDIVI_COPAY.getKey())) {
			if (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty()) {
				// 校验交费账户
				checkAccInfos(grpInsured, errors);
			}
			// 收费属组号，交费帐号不能同时为空
			if (!checkFeeGrpNo(grpInsurAppl, grpInsured)) {
				if (grpInsured.getAccInfoList() == null || grpInsured.getAccInfoList().isEmpty()) {
					errors.append("“收费属组号与缴费账户不能同时为空”");
				}
			}
			// 收费属组号，交费帐号不能同时存在
			if (checkFeeGrpNo(grpInsurAppl, grpInsured)) {
				if (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty()) {
					errors.append("“收费属组号与缴费账户不能同时存在”");
				}
			}
		}
		// 添加打印用的要约分组赋值
		setIpsnStateList(grpInsured);
		// 投保人信息校验
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			checkHldrInfo(grpInsured, errors);
		}
		/*
		 * 受益人信息校验
		 */
		if (grpInsured.getBnfrInfoList() != null && grpInsured.getBnfrInfoList().size() > 0) {
			for (BnfrInfo bnfrInfo : grpInsured.getBnfrInfoList()) {
				checkBnfrInfo(bnfrInfo, errors);
			}
		}
		//被保人年龄性别校验
		RetInfo retInfo = null;
		if(StringUtils.isEmpty(errors)){
			retInfo = ValidaPolAndIpsn(grpInsured);
		}
//		policyValidationAtomService=SpringContextHolder.getBean("policyValidationAtomService");
//		RetInfo retInfo = policyValidationAtomService.ValidaPolAndIpsn(grpInsured);
		if(null !=retInfo && StringUtils.equals("0", retInfo.getRetCode())){
			errors.append("“"+retInfo.getErrMsg()+"”");
		}
		return errors.toString();
	}

	/**
	 * @param grpInsurAppl
	 */
	public GrpInsuredValidator(GrpInsurAppl grpInsurAppl) {
		super();
		this.grpInsurAppl = grpInsurAppl;
	}

	// 投保人信息校验
	private void checkHldrInfo(GrpInsured grpInsured, StringBuilder errors) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		HldrInfo hderInfo = new HldrInfo();
		if (grpInsured.getIpsnAge()==null){
			return;
		}
		if (grpInsured.getHldrInfo() == null && grpInsured.getIpsnAge()<18) {
			errors.append("“投保人信息为空”");
			return;
		}else if(grpInsured.getHldrInfo() == null){
			hderInfo.setHldrName(grpInsured.getIpsnName());
			hderInfo.setHldrSex(grpInsured.getIpsnSex());
			hderInfo.setHldrBirthDate(grpInsured.getIpsnBirthDate());
			hderInfo.setHldrIdType(grpInsured.getIpsnIdType());
			hderInfo.setHldrIdNo(grpInsured.getIpsnIdNo());
			grpInsured.setHldrInfo(hderInfo);
			grpInsured.setRelToHldr(APPL_RT_OPSN.ME.getKey());
			return;
		}
		// 如果投保人五要素同时为空，则表示投被保人为同一个人，把被保人信息赋给投保人
		if (grpInsured.getIpsnAge()!=null
			&& grpInsured.getIpsnAge()>=18
			&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrName())
			&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrSex())
			&& (grpInsured.getHldrInfo().getHldrBirthDate() == null)
			&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrIdType())
			&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrIdNo())) {

			hderInfo.setHldrName(grpInsured.getIpsnName());
			hderInfo.setHldrSex(grpInsured.getIpsnSex());
			hderInfo.setHldrBirthDate(grpInsured.getIpsnBirthDate());
			hderInfo.setHldrIdType(grpInsured.getIpsnIdType());
			hderInfo.setHldrIdNo(grpInsured.getIpsnIdNo());
			grpInsured.setHldrInfo(hderInfo);
			grpInsured.setRelToHldr(APPL_RT_OPSN.ME.getKey());

			return;
		}
		if(StringUtils.equals(APPL_RT_OPSN.ME.getKey(), grpInsured.getRelToHldr())){
			if(!StringUtils.equals(grpInsured.getHldrInfo().getHldrName(),grpInsured.getIpsnName())){
				errors.append("“投被保人关系为本人时，投被保人姓名不相同”");
			}
			if(!StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(),grpInsured.getIpsnSex())){
				errors.append("“投被保人关系为本人时，投被保人性别不相同”");
			}
			if(grpInsured.getHldrInfo().getHldrBirthDate()!=null
				&& grpInsured.getIpsnBirthDate()!=null
				&& !DateUtils.isSameDay(grpInsured.getHldrInfo().getHldrBirthDate(),grpInsured.getIpsnBirthDate())){
				errors.append("“投被保人关系为本人时，投被保人出生日期不相同”");
			}
			if(!StringUtils.equals(grpInsured.getHldrInfo().getHldrIdType(),grpInsured.getIpsnIdType())){
				errors.append("“投被保人关系为本人时，投被保人证件类型不相同”");
			}
			if(!StringUtils.equals(grpInsured.getHldrInfo().getHldrIdNo(),grpInsured.getIpsnIdNo())){
				errors.append("“投被保人关系为本人时，投被保人证件号码不相同”");
			}
		}
		if (StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrName())) {
			errors.append("“投保人姓名为空”");
		}else{
			String name = grpInsured.getHldrInfo().getHldrName();
			name = name.trim();
			name = name.replaceAll(" +", " ");
			if(name.matches(NAME_PATTERN)){
				grpInsured.getHldrInfo().setHldrName(name);
			}else{
				errors.append("“投保人姓名格式不正确”");
			}
		}
		if (ValidatorUtils.testIdType(grpInsured.getHldrInfo().getHldrIdType())) {
			errors.append("“投保人证件类型错误”");
		}
		if (StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrIdNo())) {
			errors.append("“投保人证件号码为空”");
		}
		if (StringUtils.equals(ID_TYPE.ID.getKey(), grpInsured.getHldrInfo().getHldrIdType())) {
			String validRet = ValidatorUtils.validIdNo(grpInsured.getHldrInfo().getHldrIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				errors.append("“"+validRet+"”");
				return;
			}else{
				grpInsured.getHldrInfo().setHldrIdNo(grpInsured.getHldrInfo().getHldrIdNo().toUpperCase());
			}
			if(grpInsured.getHldrInfo().getHldrBirthDate() == null){
				try {
					grpInsured.getHldrInfo().setHldrBirthDate(DateUtils.parseDate(grpInsured.getHldrInfo().getHldrIdNo().substring(6, 14), "yyyyMMdd"));
				} catch (ParseException e) {
					logger.error("日期转换失败",e);
				}
			}else if (grpInsured.getHldrInfo().getHldrBirthDate() != null && !StringUtils.equals(
					DateFormatUtils.format(grpInsured.getHldrInfo().getHldrBirthDate(), "yyyyMMdd"),
					grpInsured.getHldrInfo().getHldrIdNo().substring(6, 14))) {
				errors.append("“投保人出生日期与身份证不符”");
			}
			if(Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 0
				&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrSex())){
				grpInsured.getHldrInfo().setHldrSex(SEX.FEMALE.getKey());
			}else if(Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 1
				&& StringUtils.isEmpty(grpInsured.getHldrInfo().getHldrSex())){
				grpInsured.getHldrInfo().setHldrSex(SEX.MALE.getKey());
			}else if (Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 0
				&& StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(), SEX.MALE.getKey())) {
				errors.append("“投保人性别与身份证不符”");
			}else if (Integer.parseInt(grpInsured.getHldrInfo().getHldrIdNo().substring(16, 17)) % 2 == 1
				&& StringUtils.equals(grpInsured.getHldrInfo().getHldrSex(), SEX.FEMALE.getKey())) {
				errors.append("“投保人性别与身份证不符”");
			}
		}
		if (ValidatorUtils.testSex(grpInsured.getHldrInfo().getHldrSex())) {
			errors.append("“投保人性别错误”");
		}
		if (grpInsured.getHldrInfo().getHldrBirthDate() == null) {
			errors.append("“投保人出生日期为空”");
		} else {
			String str = sdf.format(grpInsured.getHldrInfo().getHldrBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				errors.append("“投保人出生日期错误”");
			}else{
				long hldrAge = getAgeByBirthday(grpInsured.getHldrInfo().getHldrBirthDate());
				if(hldrAge < 18){
					errors.append("“投保人年龄不能小于18岁”");
				}
			}
		}
	}

	// 受益人信息校验
	private void checkBnfrInfo(BnfrInfo bnfrInfo, StringBuilder errors) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (StringUtils.isEmpty(bnfrInfo.getBnfrName())) {
			errors.append("“受益人姓名为空”");
		}else{
			String name = bnfrInfo.getBnfrName();
			name = name.trim();
			name = name.replaceAll(" +", " ");
			if(name.matches(NAME_PATTERN)){
				bnfrInfo.setBnfrName(name);
			}else{
				errors.append("“受益人姓名格式不正确”");
			}
		}
		if (bnfrInfo.getBnfrLevel() == null) {
			errors.append("“受益人顺序为空”");
		}
		if (bnfrInfo.getBnfrProfit() == null) {
			errors.append("“受益人份额为空”");
		}
		if (ValidatorUtils.testSex(bnfrInfo.getBnfrSex())) {
			errors.append("“受益人性别错误”");
		}
		if (bnfrInfo.getBnfrBirthDate() != null) {
			String str = sdf.format(bnfrInfo.getBnfrBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (!StringUtils.equals(testDate, str)) {
				errors.append("“受益人出生日期错误”");
			}
		}
		if (StringUtils.equals(bnfrInfo.getBnfrIdType(), ID_TYPE.ID.getKey())
				&& !StringUtils.isEmpty(bnfrInfo.getBnfrIdNo())) {
			String validRet = ValidatorUtils.validIdNo(bnfrInfo.getBnfrIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				errors.append("“"+validRet+"”");
			} else {
				bnfrInfo.setBnfrIdNo(bnfrInfo.getBnfrIdNo().toUpperCase());
				if (bnfrInfo.getBnfrBirthDate() != null
						&& !StringUtils.equals(DateFormatUtils.format(bnfrInfo.getBnfrBirthDate(), "yyyyMMdd"),
								bnfrInfo.getBnfrIdNo().substring(6, 14))) {
					errors.append("“受益人出生日期与身份证不符”");
				}
				if (Integer.parseInt(bnfrInfo.getBnfrIdNo().substring(16, 17)) % 2 == 0
						&& StringUtils.equals(bnfrInfo.getBnfrSex(), SEX.MALE.getKey())) {
					errors.append("“受益人性别与身份证不符”");
				}
				if (Integer.parseInt(bnfrInfo.getBnfrIdNo().substring(16, 17)) % 2 == 1
						&& StringUtils.equals(bnfrInfo.getBnfrSex(), SEX.FEMALE.getKey())) {
					errors.append("“受益人性别与身份证不符”");
				}
			}
		}
		if (ValidatorUtils.testApplRtOpsn(bnfrInfo.getBnfrRtIpsn())) {
			errors.append("“受益人与被保险人关系错误”");
		}
		if (bnfrInfo.getBnfrProfit() != null && bnfrInfo.getBnfrProfit() > 1.0) {
			errors.append("“受益人受益份额应小于等于1”");
		}
	}

	private void checkAccInfos(GrpInsured grpInsured, StringBuilder errors) {
		List<AccInfo> accInfos = grpInsured.getAccInfoList();
		if (accInfos == null || accInfos.isEmpty() || grpInsured.getIpsnPayAmount() == null) {
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
				errors.append("“账户顺序为空或数值错误”");
			} else if (seqNos.indexOf(accInfo.getSeqNo()) != i) {
				errors.append("“账户顺序重复”");
			}
			// 个人扣款金额与个人扣款比例不能同时为空
			if ((accInfo.getIpsnPayAmnt() == null || accInfo.getIpsnPayAmnt() < 0.01)
				&& (accInfo.getIpsnPayPct() == null || accInfo.getIpsnPayPct().compareTo(0.0) == 0)) {
				errors.append("“个人扣款金额与个人扣款比例不能同时为空”");
				continue;
			}
			// 个人扣款金额校验
			if (accInfo.getIpsnPayAmnt() == null || accInfo.getIpsnPayAmnt() < 0.01) {
				accInfo.setIpsnPayAmnt(accInfo.getIpsnPayPct()*grpInsured.getIpsnPayAmount());
			}
			sumIpsnPayAmnt += accInfo.getIpsnPayAmnt();
			// 个人账户交费比例
			if (accInfo.getIpsnPayPct() == null || accInfo.getIpsnPayPct().compareTo(0.0) == 0) {
				accInfo.setIpsnPayPct((int) (accInfo.getIpsnPayAmnt() * 1000 / grpInsured.getIpsnPayAmount()) / 1000.0);
			}
			sumIpsnPayPct += accInfo.getIpsnPayPct();
			if (accInfo.getBankCode()==null){
				errors.append("“交费银行代码为空”");
			}else if (!Pattern.matches("[0-9]{4}", accInfo.getBankCode())) {
				errors.append("“交费银行必须为4位的数字代码”");
			} else {
				String s = ValidatorUtils.testBankAccNo(accInfo.getBankAccNo());
				if (!StringUtils.isEmpty(s)) {
					errors.append("“"+s+"”");
				}
			}
			if (StringUtils.isEmpty(accInfo.getBankAccNo())){
				errors.append("“缴费账号为空”");
			}
		}
		if (Math.abs(sumIpsnPayAmnt - grpInsured.getIpsnPayAmount()) >= 0.001) {
			errors.append("“个人账户扣款金额总和不等于个人交费金额”");
		}
		if (Math.abs(sumIpsnPayPct - 1) >= 0.01) {
			errors.append("“所有账户交费比例之和不等于1”");
		}
	}

	private double checkSubStateList(List<SubState> subStates, StringBuilder errors) {
		double sumPremium = 0.0;
		if (subStates == null || subStates.isEmpty()) {
			errors.append("“险种属组号不存在或子险种列表为空”");
			return sumPremium;
		}
		// 基本信息中有要约分组时，要约分组与被保人险种的校验
		if(grpInsurAppl.getIpsnStateGrpList()!=null && !grpInsurAppl.getIpsnStateGrpList().isEmpty()){
			if(subStates.get(0).getClaimIpsnGrpNo()==null){
				errors.append("“基本信息中有要约分组时,被保人险种属组号必填”");
				return sumPremium;
			}
			for(IpsnStateGrp ipsnStateGrp:grpInsurAppl.getIpsnStateGrpList()){
				if(Long.compare(ipsnStateGrp.getIpsnGrpNo(), subStates.get(0).getClaimIpsnGrpNo())!=0){
					continue;
				}
				if(ipsnStateGrp.getGrpPolicyList().size()!=subStates.size()){
					errors.append("“该被保人险种属组下险种个数与团单基本信息中该分组中险种个数不同”");
					return sumPremium;
				}
				subCheckSubStateList(ipsnStateGrp, subStates, errors);
			}
		}else{
			if(subStates.get(0).getClaimIpsnGrpNo()!=null){
				errors.append("“基本信息中无要约分组时,被保人险种属组号必须不填”");
				return sumPremium;
			}
		}
		
		// 获取基本信息中的险种，子险种名称
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
				errors.append("“子要约列表中险种[" + subState.getPolCode() + "]不包含于团单基本信息中”");
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
		}
		return false;
	}

	// 添加打印用要约列表
	private void setIpsnStateList(GrpInsured grpInsured) {
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
	/**
	 * 根据用户生日计算年龄
	 */
	private Long getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

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
		return (long)age;
	}

	private RetInfo ValidaPolAndIpsn(GrpInsured grpInsured) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuffer errMsg = new StringBuffer();
		if(null == grpInsured){
			retInfo.setRetCode("0");
			retInfo.setErrMsg("被保人信息为空!");
			return retInfo;
		}
		if(grpInsured.getSubStateList()==null){
			retInfo.setRetCode("0");
			retInfo.setErrMsg("对应被保人信息中要约分组号不正确或子要约不存在!");
			return retInfo;
		}
		Map<String, String> mapPolCode = new HashMap<>();
		for(SubState subState:grpInsured.getSubStateList()){
			mapPolCode.put(subState.getPolCode().substring(0, 3), "");
		}
		for(Map.Entry<String, String> entry:mapPolCode.entrySet()){				
			RetInfo retInfoRet = ValidaSinglePolIpsn(entry.getKey(),grpInsured);
			if(null !=retInfoRet && StringUtils.equals("0", retInfoRet.getRetCode())){
				errMsg.append(retInfoRet.getErrMsg());
				errMsg.append("|");
				retInfo.setRetCode("0");
			}
		}
		if(StringUtils.equals("0", retInfo.getRetCode())){
			retInfo.setErrMsg(errMsg.toString());
		}else {
			retInfo.setErrMsg("被保人性别符合所有险种要求");
		}
		return retInfo;
	}
	private RetInfo ValidaSinglePolIpsn(String polCode,GrpInsured grpInsured){
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuffer errMsg = new StringBuffer();
		errMsg.append("被保人序号"+grpInsured.getIpsnNo()+"与险种"+polCode+"的校验结果:");
//		
//		Map<String, Object> mapQuery = new HashMap<>();
//		mapQuery.put("polCode", polCode);
//		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//		HeaderInfoHolder.setOutboundHeader(headerInfo);
//		mapQuery = resulPsnInfoService.excute(mapQuery);
//		if(null == mapQuery || (mapQuery.containsKey("errorCode") && mapQuery.get("errorCode").equals("8888"))){
//			errMsg.append("查询不到险种数据。");
//			retInfo.setRetCode("0");
//			retInfo.setErrMsg(errMsg.toString());
//			return retInfo;
//		}
//		JSONArray jsonArray = (JSONArray)JSONObject.parseObject(JSONObject.toJSONString(mapQuery.get("psnInfoBo"))).get("psnCtrlList");
//		if(null == jsonArray){
//			errMsg.append("查询不到险种数据。");
//			retInfo.setRetCode("0");
//			retInfo.setErrMsg(errMsg.toString());
//			return retInfo;
//		}
		boolean sexFlag = false;
//		for(int i = 0; null != jsonArray && i<jsonArray.size();i++){
//			if(!StringUtils.equals("M", jsonArray.getJSONObject(i).getString("psnTypeCode").substring(0, 1))){
//				continue;
//			}
			
			if(StringUtils.equals(grpInsured.getIpsnSex(), this.polSexMap.get(polCode)) || StringUtils.equals(SEX.BOTH.getKey(), this.polSexMap.get(polCode))){
				sexFlag = true;
			}
			//被保人年龄校验
			Date sysDate = new Date();
			String maxAgeUnit = this.polMaxAgeMap.get(polCode).substring(0, 1);
			int maxAge = Integer.valueOf(this.polMaxAgeMap.get(polCode).substring(1));
			int temp =0;
			switch (maxAgeUnit) {
			case "A":
				temp = sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear();
				break;
			case "M":
				temp = (sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear()) * 12 + (sysDate.getMonth() - grpInsured.getIpsnBirthDate().getMonth());
				break;
			case "D":
				temp= (int)(sysDate.getTime() - grpInsured.getIpsnBirthDate().getTime()) / (3600 * 24);
				break;

			default:
				break;
			}
			if(temp > maxAge){
				errMsg.append("被保人年龄大于险种允许最大年龄限制,");
			}				

			String minAgeUnit = this.polMinAgeMap.get(polCode).substring(0, 1);;
			int minAge =  Integer.valueOf(this.polMinAgeMap.get(polCode).substring(1));
			temp = 0;
			switch (minAgeUnit) {
			case "A":
				temp = sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear();
				break;
			case "M":
				temp = (sysDate.getYear() - grpInsured.getIpsnBirthDate().getYear()) * 12 + (sysDate.getMonth() - grpInsured.getIpsnBirthDate().getMonth());
				break;
			case "D":
				temp = (int)(sysDate.getTime() - grpInsured.getIpsnBirthDate().getTime()) / (3600 * 24);
				break;
			default:
				break;
			}			
			if(temp < minAge){
				errMsg.append("被保人年龄小于险种允许最小年龄限制,");
			}
//		}
		/*年龄不做限制*/
		if(sexFlag){
			retInfo.setErrMsg("被保人性别符合险种要求!");
			retInfo.setRetCode("1");
		}else{
			retInfo.setRetCode("0");
			errMsg.append("被保人性别不符合险种要求!");
			retInfo.setErrMsg(errMsg.toString());
		}		
		return retInfo;
	}
	//基本信息中要约分组与被保人信息中子要约列表的校验
	private void subCheckSubStateList(IpsnStateGrp ipsnStateGrp,List<SubState> subStates,StringBuilder errors){
		//获取基本信息中该分组下险种代码集合
		List<String> grpPolicyCodes = new ArrayList<>();
		for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
			grpPolicyCodes.add(grpPolicy.getPolCode());
		}
		for(SubState subState:subStates){
			if(!grpPolicyCodes.contains(subState.getPolCode())){
				errors.append("“被保人险种属组下险种代码["+subState.getPolCode()+"]不存在于团单分组["+subState.getClaimIpsnGrpNo()+"]中”");
				return;
			}
			//基金险或分组类型为2时，不校验被保人险种属组中的保额保费
			if(grpInsurAppl.getFundInsurInfo()!=null || StringUtils.equals(ipsnStateGrp.getIpsnGrpType(),"2")){
				continue;
			}
			//分组保额保费一致时，校验输入的保额保费与基本信息中是否一致
			for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
				if(!StringUtils.equals(grpPolicy.getPolCode(),subState.getPolCode())){
					continue;
				}
				if(Double.compare(grpPolicy.getFaceAmnt(),subState.getFaceAmnt())!=0){
					errors.append("“被保人险种属组中险种["+grpPolicy.getPolCode()+"]的保额与基本信息中要约分组中保额不一致”");
				}
				if(Double.compare(grpPolicy.getPremium(),subState.getPremium())!=0){
					errors.append("“被保人险种属组中险种["+grpPolicy.getPolCode()+"]的保费与基本信息中要约分组中保费不一致”");
				}
			}
		}
	}
	public Map<String, String> getPolMinAgeMap() {
		return polMinAgeMap;
	}

	public void setPolMinAgeMap(Map<String, String> polMinAgeMap) {
		this.polMinAgeMap = polMinAgeMap;
	}

	public Map<String, String> getPolMaxAgeMap() {
		return polMaxAgeMap;
	}

	public void setPolMaxAgeMap(Map<String, String> polMaxAgeMap) {
		this.polMaxAgeMap = polMaxAgeMap;
	}

	public Map<String, String> getPolSexMap() {
		return polSexMap;
	}

	public void setPolSexMap(Map<String, String> polSexMap) {
		this.polSexMap = polSexMap;
	}
}
