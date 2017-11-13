package com.newcore.orbpsutils.dao.api;

import java.math.BigDecimal;
import java.util.List;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.service.bo.ProcEarnestPayTask;

/**
 * 查询账户信息
 * @author LJF
 * 2017年2月22日 20:19:55
 */
public interface QueryAccInfoDao {

	/**
	 * 查询账户信息
	 * @param accNoList 账户号集合
	 * @return 
	 */
	List<EarnestAccInfo> queryEarnestAccInfo(List<String> accNoList);

	/**
	 * 查询账户信息
	 * @param applNo 投保单号
	 * @return 
	 */
	List<EarnestAccInfo> queryEarnestAccInfoByApplNo(String applNo);

	/**
	 * 查询账户信息
	 * @param accNo 缴费账户号
	 * @return 
	 */
	EarnestAccInfo queryOneEarnestAccInfoByApplNo(String accNo);

	/**
	 * 查询账户信息总余额
	 * @param applNo 投保单号
	 * @return 
	 */
	BigDecimal querySumBalanceEarnestAccInfoByApplNo(String applNo);

	/**
	 * 查询账户信息冻结总余额
	 * @param accNo 投保单号
	 * @return 
	 */
	BigDecimal querySumFrozenBalanceEarnestAccInfoByApplNo(String applNo);


	/**
	 * 查询账户信息
	 * @param cntrNo 保单号
	 * @param lockFlag 锁标志
	 * @param procStat 送达状态
	 * @return 
	 */
	boolean getProcStat(String cntrNo , String lockFlag , String procStat);

	/**
	 * 修改账户信息
	 * @param accNo 账户号
	 * @param amnt 支取金额
	 * @return 
	 */
	boolean updateEarnestAccInfo(String accNo , Double amnt);

	/**
	 * 修改账户信息
	 * @param accId 账户标识
	 * @return 
	 */
	boolean updateEarnestAccInfo(long accId);

	/**
	 * 添加账户轨迹信息
	 * @param mioAccInfoLog 账户轨迹信息
	 * @return 
	 */
	boolean insertEarnestAccInfo(MioAccInfoLog	mioAccInfoLog);

	/**
	 * 查询实收数据
	 * @param cntrNo 投保单号
	 * @param bankAccNo 银行账号
	 * @param bankCode 银行代码
	 * @param element 元素：被保人序号、层级代码、属组号
	 * @param elementFlag 元素标志：判断是那种元素
	 * @return 
	 */
	BigDecimal getMioLogSumAmnt(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag);

	/**
	 * 查询账户信息轨迹表序列号 MIO_ACC_INFO_LOG
	 * @return 
	 */
	long selectMioAccInfoLog();


	/**
	 * 查询暂缴费支取-全部支取任务
	 * @return 
	 */
	List<ProcEarnestPayTask> queryProcEarnestPayTask();

	/**
	 * 根据投保单号查询暂缴费支取-全部支取任务的任务ID
	 * @return 
	 */
	long queryProcEarnestPayTaskTaskSql(String applNo);

	/**
	 * 修改账户信息
	 * @param status 任务状态
	 * @param taskSql 任务唯一ID
	 * @param remark 备注
	 * @param timeFlag 时间开始结束标志
	 * @return 
	 */
	boolean updateProcEarnestPayTask(String status , long taskSql  , String remark , String timeFlag);


	/**
	 * 回退时，根据合同组号删除分期应收数据。
	 * @param cgNo 合同组号
	 * @return 
	 */
	boolean removePlnmioRecList(String cgNo); 

	/**
	 * 查询实收数据
	 * @param cntrNo 投保单号
	 * @param bankAccNo 银行账号
	 * @param bankCode 银行代码
	 * @param element 元素：被保人序号、层级代码、属组号
	 * @param elementFlag 元素标志：判断是那种元素
	 * @return 
	 */
	BigDecimal getSumAmntFromMioLog(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag);
}
