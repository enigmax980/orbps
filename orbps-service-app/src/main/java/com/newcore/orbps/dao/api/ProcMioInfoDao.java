package com.newcore.orbps.dao.api;

import java.util.List;

import com.newcore.orbps.models.banktrans.EarnestAccInfo;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 处理收付费公共服务
 * @author JCC
 * 2016年11月30日 11:19:55
 */
public interface ProcMioInfoDao {
	/**
	 * 获取团体出单基本信息
	 * @param applNo 投保单号
	 * @return GrpInsurAppl
	 */
	GrpInsurAppl getGrpInsurAppl(String applNo);

	/**
	 * 获取收付费数据
	 * @param applNo 投保单号
	 * @return
	 * 	MioPlnmioInfo
	 */
	MioPlnmioInfo getMioPlnmioInfo(String applNo);

	/**
	 * 根据投保单删除数据
	 * @param applNo 投保单号
	 * @return 
	 * 	删除的条数
	 */
	int removeMioPlnmioByApplNo(String applNo);

	/**
	 * 获取应收数据中最大值
	 * @return
	 *  max plnmioRecId
	 */
	Long getMaxPlnMioRecId();


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
	 * 获取被保人加费金额 
	 * @param 
	 * 	applNo 投保单号     必填
	 * @param 
	 * 	ipsnId 被保人序号  可填 
	 * @return
	 * 被保人加费金额
	 */
	double getPersonAddMoney(String applNo,String ipsnId);

	/**
	 * 获取操作轨迹数据
	 * @param applNo 投保单
	 * @return
	 */
	InsurApplOperTrace getInsurApplOperTrace(String applNo);

	/**
	 * 获取银行转账数据中最大ID
	 * @return
	 *  max transBatSeq
	 */
	long getMaxBuildBankId();

	/**
	 * 获取实收数据中最大ID
	 * @return
	 *  max mioLogId
	 */
	Long getMixMioLogId();

	/**
	 * 获取帐号信息
	 * @param accNo  帐号ID
	 * @return
	 * 	EarnestAccInfo
	 */
	EarnestAccInfo getEarnestAccInfo(String accNo);

	/**
	 * 删除帐号信息
	 * @param accNo 帐号ID
	 * @return
	 *  删除条数 num
	 */
	int removeEarnestAccInfo(String accNo);

	/**
	 * 获取被保人加费
	 * @param applNo 投保单号
	 * @param polCode 险种代码
	 * @return
	 * 险种对应的被保人加费
	 */
	double getPersonAddMoneyByPolCode(String applNo, String polCode);

	/**
	 * 获取被保人加费金额
	 * @param applNo 投保单号
	 * @param polCode 险种代码
	 * @param ipsnNo 被保人序号
	 * @return
	 * 被保人在该险种加费
	 */
	double getPersonAddMoney(String applNo,String polCode,long ipsnNo);

	/**
	 * 修改被保人状态
	 * @param PlnmioRec 应收数据
	 * @param procStat 状态值： O-作废，E-成功
	 * @return int 修改条数
	 */
	int changeGrpInsuredState(PlnmioRec plnmioRec, String procStat);


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


}
