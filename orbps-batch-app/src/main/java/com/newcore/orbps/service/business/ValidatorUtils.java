package com.newcore.orbps.service.business;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.newcore.supports.dicts.APPL_RT_OPSN;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.IPSN_SI_HEALTH_TYPE;
import com.newcore.supports.dicts.IPSN_TYPE;
import com.newcore.supports.dicts.SEX;
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
		sfz = sfz.trim();
		char ss[] = sfz.toCharArray();
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
		String rq = sfz.substring(6, 14);
		if (!testDate(rq).equals(rq))
			return "身份证号中的出生日期不符合日期规则";
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
	//校验银行帐号
	public static String testBankAccNo(String bankAccNo) {
		return "";
//		if (!Pattern.matches("[0-9]{16,19}", bankAccNo)) {
//			return "|银行卡号长度必须在16到19之间的数字|";
//		}
//		String strBin = "10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";
//		if (strBin.indexOf(bankAccNo.substring(0, 2)) == -1) {
//			return "|银行卡号开头两位不正确|";
//		}
//		int sumOdd = 0;
//		int sumEven = 0;
//		int length = bankAccNo.length();
//		int[] wei = new int[length];
//		for (int i = 0; i < length; i++) {
//			wei[i] = Integer.parseInt(bankAccNo.substring(length - i - 1, length - i));// 从最末一位开始提取，每一位上的数值
//		}
//		for (int i = 0; i < length / 2; i++) {
//			sumOdd += wei[2 * i];
//			if ((wei[2 * i + 1] * 2) > 9) {
//				wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
//			} else {
//				wei[2 * i + 1] *= 2;
//			}
//			sumEven += wei[2 * i + 1];
//		}
//		if (length % 2 == 1) {
//			sumOdd += wei[length - 1];
//		}
//		if ((sumOdd + sumEven) % 10 == 0) {
//			return "";
//		} else {
//			return "|银行帐号有误|";
//		}
	}
}
