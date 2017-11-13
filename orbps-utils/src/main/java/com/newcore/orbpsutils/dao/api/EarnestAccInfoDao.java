package com.newcore.orbpsutils.dao.api;

import java.math.BigDecimal;
import java.util.List;

import com.newcore.orbps.models.finance.EarnestAccInfo;

public interface EarnestAccInfoDao {

	/**
	 * 查询账户主表信息
	 * @param accNo
	 * 	账号
	 * @return
	 * 	EarnestAccInfo
	 */
	EarnestAccInfo queryEarnestAccInfo(String accNo);

	/**
	 * 获取自主增长序列AccID
	 * @return
	 * max accId
	 */
	long queryEarnestAccInfoAccId();

	/**
	 * 新增数据
	 * @param bigEarAcc
	 * @return
	 */
	boolean insertEarnestAccInfo(EarnestAccInfo bigEarAcc);

	/**
	 * 更新账户余额
	 * @param accId 账户ID
	 * @param sumAmnt 金额
	 * @return
	 */
	boolean updateEarnestAccInfo(long accId, BigDecimal sumAmnt);
	
	/**
	 * 根据投保单号把该保单下所有账户数据里面冻结金额清零
	 * @param applNo 投保单号
	 * @return true--更新成功   false--更新失败
	 */
	boolean updateFrozenBalance(String applNo);

	/**
	 * 批量更新数据
	 * @param earList
	 * @return
	 */
	boolean updateEarnestAccInfo(List<EarnestAccInfo> earList);

	/**
	 * 获取账户信息
	 * @param applNo 投保单号
	 * @return
	 * 投保单对应的账户信息
	 */
	List<EarnestAccInfo> queryEarnestAccInfoList(String applNo);
}
