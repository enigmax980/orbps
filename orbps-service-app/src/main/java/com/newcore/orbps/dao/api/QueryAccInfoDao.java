package com.newcore.orbps.dao.api;

import java.math.BigDecimal;
import java.util.List;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.service.bo.QueryEarnestAccInfoBean;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 查询账户信息
 * @author LJF
 * 2017年2月22日 20:19:55
 */
public interface QueryAccInfoDao {

	/**
	 * 查询被保人序号信息
	 * @param queryEarnestAccInfoBean 投保单号
	 * @return Long
	 */
	Long getIpsnNoList(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean);

	/**
	 * 查询被保人序号信息
	 * @param queryEarnestAccInfoBean 投保单号 
	 * @param  levelCodeList  组织层次代码
	 * @return Long
	 */
	Long getIpsnNoListBy(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean,List<String> levelCodeList);

	/**
	 * 查询被保人序号信息
	 * @param queryEarnestAccInfoBean 投保单号
	 * @return Long
	 */
	List<Long> getIpsnNoListByQueryEarnestAccInfoBean(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean);

	/**
	 * 查询账户信息
	 * @param accNoList 账户号集合
	 * @param pageSize 查询条数
	 * @param pageStartNo 起始数
	 * @return EarnestAccInfo
	 */
	List<EarnestAccInfo> queryEarnestAccInfo(List<String> accNoList ,Integer pageSize , long pageStartNo);

	/**
	 * 查询账户信息
	 * @param applNo 投保单号
	 * @param pageSize 查询条数
	 * @param pageStartNo 起始数
	 * @return EarnestAccInfo
	 */
	List<EarnestAccInfo> queryEarnestAccInfoByApplNo(String applNo ,Integer pageSize , long pageStartNo);

	/**
	 * 查询账户信息
	 * @param accNo 账户号
	 * @return EarnestAccInfo
	 */
	EarnestAccInfo queryOneEarnestAccInfoByApplNo(String accNo);

	/**
	 * 查询账户信息总余额
	 * @param applNo 投保单号
	 * @return EarnestAccInfo
	 */
	BigDecimal querySumBalanceEarnestAccInfoByApplNo(String applNo);

	/**
	 * 查询账户信息冻结总余额
	 * @param applNo 投保单号
	 * @return EarnestAccInfo
	 */
	BigDecimal querySumFrozenBalanceEarnestAccInfoByApplNo(String applNo);


	/**
	 * 查询账户信息
	 * @param cntrNo 保单号
	 * @param lockFlag 锁标志
	 * @param procStat 送达状态
	 * @return EarnestAccInfo
	 */
	boolean getProcStat(String cntrNo , String lockFlag , String procStat);

	/**
	 * 修改账户信息
	 * @param accNo 账户号
	 * @param amnt 支取金额
	 * @return boolean
	 */
	boolean updateEarnestAccInfo(String accNo , Double amnt);

	/**
	 * 添加账户轨迹信息
	 * @param mioAccInfoLog 账户轨迹信息
	 * @return boolean
	 */
	boolean insertEarnestAccInfo(MioAccInfoLog	mioAccInfoLog);

	/**
	 * 查询实收数据
	 * @param cntrNo 投保单号
	 * @param bankAccNo 银行账号
	 * @param bankCode 银行代码
	 * @param element 元素：被保人序号、层级代码、属组号
	 * @param elementFlag 元素标志：判断是那种元素
	 * @return boolean
	 */
	BigDecimal getMioLog(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag);
	
	
	/**
	 * 查询该银行账号已支取金额
	 * @param cntrNo 投保单号
	 * @param bankAccNo 银行账号
	 * @param bankCode 银行代码
	 * @param element 元素：被保人序号、层级代码、属组号
	 * @param elementFlag 元素标志：判断是那种元素
	 * @return boolean
	 */
	BigDecimal getPlnmioRec(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag);

	/**
	 * 查询账户信息轨迹表序列号 MIO_ACC_INFO_LOG
	 * @return long
	 */
	long selectMioAccInfoLog();

	/**
	 * 暂缴费支取-全部支取时，向产生应付控制表中插入任务
	 * @param applNo 投保单号
	 * @return boolean
	 */
	boolean insertProcEarnestPayTask(String applNo); 

	/**
	 * 暂缴费支取-全部支取时，查询产生应付控制表任务
	 * @param applNo 投保单号
	 * @return ProcEarnestPayTask
	 */
	int findProcEarnestPayTask(String applNo); 

	/**
	 * 查询账户信息
	 * @param applNo 投保单号
	 * @return int
	 */
	int queryCountEarnestAccInfoByApplNo(String applNo);

	/**
	 * 查询账户信息总数
	 * QueryAccInfoDao
	 * BigDecimal
	 */
	BigDecimal queryNumAccInfoByApplNo(String applNo);
}
