package com.newcore.orbps.dao.api;

import java.util.List;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.OperateBankTransInBean;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 处理收付费公共服务
 * @author LJF
 * 2017年3月2日 20:19:55
 */
public interface ProcPSInfoDao {

	/**
	 * 获取险种加费金额
	 * @param 
	 * 		applNo 投保单号   必填
	 * @param 
	 * 		polCode 险种代码 可填 
	 * @return
	 * 	险种加费金额总计
	 */
	double getPolicyAddMoney(String applNo,String polCode);


	/**
	 * 获取有效被保人数据
	 * @param applNo 投保单号
	 * @param levelList 多个层次代码
	 * @return
	 */
	List<GrpInsured> getGrpInsuredList(String applNo, List<String> levelList);

	/**
	 * 获取被保人加费金额 
	 * @param 
	 * 	applNo 投保单号     必填
	 * @param 
	 * 	ipsnNoList 被保人序号  可填 
	 * @return
	 * 被保人加费金额
	 */
	double getPersonAddMoney(String applNo,List<Long> ipsnNoList);

	/**
	 * 获取无效被保人的险种保费金额
	 * @param applNo 投保单号
	 * @param polCode 险种代码
	 * @return
	 */
	double getInvalidPolicyMoney(String applNo, String polCode);

	/**
	 * 获取无效被保人单位缴费总计金额
	 * @param applNo 投保单号
	 * @param levelCode 组织层次代码
	 * @return
	 */
	double getInvalidIpsnMoney(String applNo,List<String> levelCode);

	/**
	 * 获取单笔银行转账的应收金额
	 * @param applNo 投保单号
	 * @return
	 */
	double getPlnmioRecAmntBankTrans(String applNo);

	/**
	 * 银行转账操作查询
	 * @param operateBankTransInBean 银行转账数据
	 * @return
	 */
	List<BankTrans> getListBankTrans(OperateBankTransInBean operateBankTransInBean);

	/**
	 * 银行转账操作总金额查询
	 * @param operateBankTransInBean 银行转账数据
	 * @return
	 */
	Double getListBankTransAmnt(OperateBankTransInBean operateBankTransInBean);

	/**
	 * 银行转账操作最大时间查询
	 * @param operateBankTransInBean 银行转账数据
	 * @return
	 */
	String getListBankTransMaxCreateDate(OperateBankTransInBean operateBankTransInBean);


	/**
	 * 银行转账操作最小时间查询
	 * @param operateBankTransInBean 银行转账数据
	 * @return
	 */
	String getListBankTransMinCreateDate(OperateBankTransInBean operateBankTransInBean);

}
