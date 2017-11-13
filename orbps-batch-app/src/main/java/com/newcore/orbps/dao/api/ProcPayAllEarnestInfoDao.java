package com.newcore.orbps.dao.api;

import java.math.BigDecimal;
import java.util.List;

import com.newcore.orbps.models.service.bo.ProcEarnestPayTask;

/**
 * 暂缴费支取全部支取
 * @author LJF
 * 2017年3月8日 11:19:55
 */
public interface ProcPayAllEarnestInfoDao {


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
	 * 查询实收数据
	 * @param cntrNo 投保单号
	 * @param bankAccNo 银行账号
	 * @param bankCode 银行代码
	 * @param element 元素：被保人序号、层级代码、属组号
	 * @param elementFlag 元素标志：判断是那种元素
	 * @return 
	 */
	BigDecimal getSumAmntFromMioLog(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag);
	
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
}
