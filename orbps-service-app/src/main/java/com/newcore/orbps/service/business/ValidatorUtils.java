package com.newcore.orbps.service.business;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.model.service.dict.POST_CODE;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.supports.dicts.ADMIN_FEE_COPU_TYPE;
import com.newcore.supports.dicts.APPL_BUSI_TYPE;
import com.newcore.supports.dicts.APPL_RT_OPSN;
import com.newcore.supports.dicts.ARGUE_TYPE;
import com.newcore.supports.dicts.CNTR_PRINT_TYPE;
import com.newcore.supports.dicts.CNTR_SEND_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_USE;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.DUR_UNIT;
import com.newcore.supports.dicts.GIFT_FLAG;
import com.newcore.supports.dicts.GRP_BUSI_TYPE;
import com.newcore.supports.dicts.GRP_ID_TYPE;
import com.newcore.supports.dicts.GRP_PSN_DEPT_TYPE;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.INFORCE_DATE_TYPE;
import com.newcore.supports.dicts.INSUR_PROPERTY;
import com.newcore.supports.dicts.IPSN_GRP_TYPE;
import com.newcore.supports.dicts.IPSN_SI_HEALTH_TYPE;
import com.newcore.supports.dicts.IPSN_TYPE;
import com.newcore.supports.dicts.LEGAL_CODE;
import com.newcore.supports.dicts.LIST_PRINT_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.NATIONALITY_CODE;
import com.newcore.supports.dicts.NATURE_CODE;
import com.newcore.supports.dicts.OCC_CLASS_CODE;
import com.newcore.supports.dicts.PREM_CAL_TYPE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.SPECI_BUSINESS_LOGO;
import com.newcore.supports.dicts.SRND_AMNT_CMPT_TYPE;
import com.newcore.supports.dicts.STL_TYPE;
import com.newcore.supports.dicts.VOUCHER_PRINT_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;

/**
 * 校验工具类
 * @author liushuaifeng
 *
 * 创建时间：2016年7月26日下午3:52:36
 */
public class ValidatorUtils {
	private final static Logger logger = LoggerFactory.getLogger(ValidatorUtils.class);

	/**
	 * 身份证号校验
	 * */
	public static String validIdNo(String sfz){
		if(StringUtils.isEmpty(sfz)){
			return "身份证号码为空";
		}
		char ss[] = sfz.trim().toCharArray();
		if (ss[0] < '1' || ss[0] > '9'){
			logger.error("必须是数字开头的身份证号");
			return "必须是数字开头的身份证号";
		}

		if (ss.length != 18){
			logger.error("中国公民身份证号必须是18位");
			return "中国公民身份证号必须是18位";// 不是18位
		}	
		int iS = 0;
		int iW[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char dd[] = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		for (int i = 0; i < 17; i++)
			iS += (int) (ss[i] - '0') * iW[i];
		if (ss[17] == 'x'){
			ss[17] = 'X';
		}
		if (dd[iS % 11] != ss[17])
			return "身份证号检验位不符合校验规则";
		String rq = sfz.trim().substring(6, 14);
		if (!testDate(rq).equals(rq))
			return "身份证号中的出生日期不符合日期规则";
		logger.info("身份证校验通过");

		return "OK";
	}

	/**
	 * 日期校验
	 * */
	public static String testDate(String date){
		String re = "";
		if (date.equals(""))
			return ".";
		long d0 = StringUtils.stringToInteger(date), d1 = d0 / 10000, d2 = d0 % 10000 / 100, d3 = d0 % 100;
		String in = DateFormatUtils.format(new Date(), "yyyyMMdd");
		long d4 = StringUtils.stringToInteger(in);
		if (d4 < d0){
			return re;
		}
		if (d1 < 1800 || d1 > 9999 || d2 < 1 || d2 > 12 || d3 < 1 || d3 > 31)
			return re;
		if (d2 == 4 || d2 == 6 || d2 == 9 || d2 == 11)
			if (d3 > 30)
				return re;
		if (d2 == 2) {
			if (d1 % 400 == 0 || d1 % 4 == 0) {
				if (d3 > 29)
					return re;
			} else {
				if (d3 > 28)
					return re;
			}
		}
		return "" + (d1 * 10000 + d2 * 100 + d3);
	}
	//校验团体清单标记
	public static boolean testLstProcType(String lstProcType){
		boolean b = true;
		LST_PROC_TYPE[] array= LST_PROC_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),lstProcType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验保单性质
	public static boolean testInsurProperty(String insurProperty){
		boolean b = true;
		INSUR_PROPERTY[] array= INSUR_PROPERTY.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),insurProperty)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验团体业务分类（来源）
	public static boolean testGrpBusiType(String grpBusiType){
		boolean b = true;
		GRP_BUSI_TYPE[] array= GRP_BUSI_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),grpBusiType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验契约业务类型
	public static boolean testApplBusiType(String applBusiType){
		boolean b = true;
		APPL_BUSI_TYPE[] array= APPL_BUSI_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),applBusiType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验合同打印方式
	public static boolean testCntrPrintType(String cntrPrintType){
		boolean b = true;
		CNTR_PRINT_TYPE[] array= CNTR_PRINT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),cntrPrintType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验清单打印方式
	public static boolean testListPrintType(String listPrintType){
		boolean b = true;
		LIST_PRINT_TYPE[] array= LIST_PRINT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),listPrintType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验个人凭证类型
	public static boolean testVoucherPrintType(String voucherPrintType){
		boolean b = true;
		VOUCHER_PRINT_TYPE[] array= VOUCHER_PRINT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),voucherPrintType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验是否
	public static boolean testYesNo(String s){
		boolean b = true;
		YES_NO_FLAG[] array= YES_NO_FLAG.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),s)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验契约形式
	public static boolean testCntrType(String cntrType){
		boolean b = true;
		CNTR_TYPE[] array= CNTR_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),cntrType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验合同送达形式
	public static boolean testCntrSendType(String cntrSendType){
		boolean b = true;
		CNTR_SEND_TYPE[] array= CNTR_SEND_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),cntrSendType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验汇交人类型
	public static boolean testListType(String listType){
		boolean b = true;
		LIST_TYPE[] array= LIST_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),listType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验争议处理方式
	public static boolean testArgueType(String argueType){
		boolean b = true;
		ARGUE_TYPE[] array= ARGUE_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),argueType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验赠送保险标志
	public static boolean testGiftFlag(String giftFlag){
		boolean b = true;
		GIFT_FLAG[] array= GIFT_FLAG.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),giftFlag)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验指定退保公式
	public static boolean testSrndAmntCmptType(String srndAmntCmptType){
		boolean b = true;
		SRND_AMNT_CMPT_TYPE[] array= SRND_AMNT_CMPT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),srndAmntCmptType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验性别
	public static boolean testSex(String sex){
		boolean b = true;
		SEX[] array= SEX.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),sex)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验证件类型
	public static boolean testIdType(String idType){
		boolean b = true;
		ID_TYPE[] array= ID_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),idType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验团体客户证件类型
	public static boolean testGrpIdType(String grpIdType){
		boolean b = true;
		GRP_ID_TYPE[] array= GRP_ID_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),grpIdType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验企业注册地国籍
	public static boolean testGrpCountryCode(String grpCountryCode){
		boolean b = true;
		NATIONALITY_CODE[] array= NATIONALITY_CODE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),grpCountryCode)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验外管局部门类型
	public static boolean testGrpPsnDeptType(String grpPsnDeptType){
		boolean b = true;
		GRP_PSN_DEPT_TYPE[] array= GRP_PSN_DEPT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),grpPsnDeptType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验行业类别
	public static boolean testOccClassCode(String occClassCode){
		boolean b = true;
		OCC_CLASS_CODE[] array= OCC_CLASS_CODE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),occClassCode)){
				b = false;
				break;
			}

		}
		return b;
	}
	//校验单位性质（经济分类）
	public static boolean testNatureCode(String natureCode){
		boolean b = true;
		NATURE_CODE[] array= NATURE_CODE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),natureCode)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验单位性质（法律分类）
	public static boolean testLegalCode(String legalCode){
		boolean b = true;
		LEGAL_CODE[] array= LEGAL_CODE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),legalCode)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验共同展业主副标志
	public static boolean testDevelMainFlag(String develMainFlag){
		boolean b = true;
		DEVEL_MAIN_FLAG[] array= DEVEL_MAIN_FLAG.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),develMainFlag)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验团单保费来源
	public static boolean testPremSource(String premSource){
		boolean b = true;
		PREM_SOURCE[] array= PREM_SOURCE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),premSource)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验缴费方式
	public static boolean testMoneyinItrvl(String moneyinItrvl){
		boolean b = true;
		MONEYIN_ITRVL[] array= MONEYIN_ITRVL.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),moneyinItrvl)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验缴费形式
	public static boolean testMoneyinType(String moneyinType){
		boolean b = true;
		MONEYIN_TYPE[] array= MONEYIN_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),moneyinType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验结算方式
	public static boolean testStlType(String stlType){
		boolean b = true;
		STL_TYPE[] array= STL_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),stlType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验币种
	public static boolean testCurrencyCode(String currencyCode){
		boolean b = true;
		CURRENCY_CODE[] array= CURRENCY_CODE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),currencyCode)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验生效日类型
	public static boolean testInforceDateType(String inforceDateType){
		boolean b = true;
		INFORCE_DATE_TYPE[] array= INFORCE_DATE_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),inforceDateType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验保险期类型
	public static boolean testInsurDurUnit(String insurDurUnit){
		boolean b = true;
		DUR_UNIT[] array = DUR_UNIT.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),insurDurUnit)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验专项业务标识
	public static boolean testSpeciBusinessLogo(String speciBusinessLogo){
		boolean b = true;
		SPECI_BUSINESS_LOGO[] array = SPECI_BUSINESS_LOGO.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),speciBusinessLogo)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验分组类型
	public static boolean testIpsnGrpType(String ipsnGrpType){
		boolean b = true;
		IPSN_GRP_TYPE[] array = IPSN_GRP_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),ipsnGrpType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验与被保险人关系
	public static boolean testApplRtOpsn(String applRtOpsn){
		boolean b = true;
		APPL_RT_OPSN[] array= APPL_RT_OPSN.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),applRtOpsn)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验被保险人类型
	public static boolean testIpsnType(String ipsnType){
		boolean b = true;
		IPSN_TYPE[] array= IPSN_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),ipsnType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验医保标记
	public static boolean testIpsnSss(String ipsnSss){
		boolean b = true;
		IPSN_SI_HEALTH_TYPE[] array= IPSN_SI_HEALTH_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),ipsnSss)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验建工险.保费计算方式
	public static boolean testPremCalType(String premCalType){
		boolean b = true;
		PREM_CAL_TYPE[] array= PREM_CAL_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),premCalType)){
				b = false;
				break;
			}
		}
		return b;
	}
	
	/*校验邮编*/
	public static RetInfo checkPostCode(String postCode,String province){
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		boolean b = false;
		if(StringUtils.isEmpty(postCode) || postCode.length()<2){
			retInfo.setRetCode("0");
			retInfo.setErrMsg("邮编"+postCode+"不正确.");
			return retInfo;
		}
		for(POST_CODE postcode:POST_CODE.values()){
			if(StringUtils.equals(postcode.getKey(),postCode.substring(0, 2))){
				b = true;
				break;
			}
		}
		if(!b){
			retInfo.setRetCode("0");
			retInfo.setErrMsg("邮编"+postCode+"不正确.");
			return retInfo;
		}
		if(!StringUtils.isEmpty(postCode) && !StringUtils.isEmpty(province)){
			if(StringUtils.equals(POST_CODE.HONGKONG.getKey(), postCode) && (-1 == province.indexOf(POST_CODE.HONGKONG.getDescription()))){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("邮编"+postCode+" 为香港邮编不属于 "+ province +".");
			}else if(StringUtils.equals(POST_CODE.MACAO.getKey(), postCode) &&
					(-1 == province.indexOf(POST_CODE.MACAO.getDescription()))){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("邮编"+postCode+" 为澳门邮编不属于 "+ province +".");
			}else if(-1 == province.indexOf(POST_CODE.valueOfKey(postCode.substring(0, 2)).getDescription())){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("邮编"+postCode+"属于"+POST_CODE.valueOfKey(postCode.substring(0, 2)).getDescription()+" 省/市,不属于"+ province +".");				
			}
		}
		return retInfo;
	}	
	
	//校验健康险.公共保额使用范围
	public static boolean testComlnsurAmntUse(String comlnsurAmntUse){
		boolean b = true;
		COMLNSUR_AMNT_USE [] array= COMLNSUR_AMNT_USE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),comlnsurAmntUse)){
				b = false;
				break;
			}
		}
		return b;
	}

	//校验健康险.公共保额使用范围
	public static boolean testComlnsurAmntType(String comlnsurAmntType){
		boolean b = true;
		COMLNSUR_AMNT_TYPE [] array= COMLNSUR_AMNT_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),comlnsurAmntType)){
				b = false;
				break;
			}
		}
		return b;
	}
	//校验基金险.管理费提取方式
	public static boolean testAdminFeeCopuType(String adminFeeCopuType){
		boolean b = true;
		ADMIN_FEE_COPU_TYPE [] array= ADMIN_FEE_COPU_TYPE.values();
		for(int i = 0;i<array.length;i++){
			if(StringUtils.equals(array[i].getKey(),adminFeeCopuType)){
				b = false;
				break;
			}
		}
		return b;
	}
	
	//经与业管讨论暂时不做银行帐号校验，校验银行帐号
	public static String testBankAccNo(String bankAccNo){
		return "";
//		if (!Pattern.matches("[0-9]{16,19}", bankAccNo)) {
//			return "|银行卡号长度必须在16到19之间的数字|";
//		}
//		String strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";
//		if (strBin.indexOf(bankAccNo.substring(0,2))==-1){
//			return "|银行卡号开头两位不正确|";
//		}
//	    int sumOdd = 0;
//	    int sumEven = 0;
//	    int length = bankAccNo.length();
//	    int[] wei = new int[length];
//	    for (int i = 0; i < length; i++) {
//	    	wei[i] = Integer.parseInt(bankAccNo.substring(length - i - 1, length - i));// 从最末一位开始提取，每一位上的数值
//	    }
//	    for (int i = 0; i < length / 2; i++) {
//	    	sumOdd += wei[2 * i];
//	    	if ((wei[2 * i + 1] * 2) > 9){
//	    		wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
//	    	}else{
//	    		wei[2 * i + 1] *= 2;
//	    	}
//	    	sumEven += wei[2 * i + 1];
//	    }
//	    if (length % 2 == 1){
//	    	sumOdd += wei[length-1]; 
//	    }
//	    if ((sumOdd + sumEven) % 10 == 0){
//	    	return "";
//	    }else{
//	    	return "|银行帐号有误|";
//	    }
	}
}
