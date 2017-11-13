package com.newcore.orbpsutils.dao.api;

import java.util.List;

import com.newcore.orbps.models.banktrans.ExtractBankTransBean;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.BankTransInfoDetailsVo;
import com.newcore.orbps.models.finance.BankTransingInfoVo;

public interface BankTransDao {

	/**
	 * 批量保存银行转账数据
	 * @param 
	 * 	btList 银行转账数据
	 * @return
	 */
	boolean insertBankTrans(List<BankTrans> btList);

	/**
	 * 修改银行转账数据的转账状态
	 * @param 
	 * 	transCodes 主键  id1，id2
	 * @param 
	 * 	tranStat 转账状态
	 * @param
	 * 	transBatSeq 转账批次
	 * @return
	 */
	boolean updateBankTransTransStat(String transCodes,String tranStat,long transBatSeq);

	/**
	 * 获取满足条件的银行转账数据
	 * @param bean 抽取条件
	 * @return
	 * 银行转账数据集合
	 */
	List<BankTrans> queryBankTransList(ExtractBankTransBean bean);

	/**
	 * 根据序号获取银行转账数据
	 * @param transCode 主键
	 * @return
	 * BankTrans
	 */
	BankTrans queryBankTransByTransCode(Long transCode);

	/**
	 * 删除序号对应的银行转账数据
	 * @param transCode 主键
	 */
	boolean deleteBankTrans(Long transCode);
	/**
	 * 待转账数据查询
	 * @param bankTransingInfoVo
	 * @return List<BankTrans>
	 */
	public List<BankTrans> queryBankTransingInfo(BankTransingInfoVo bankTransingInfoVo);
	/**
	 * 银行转账操作明细查询
	 * @param bankTransingInfoVo
	 * @return List<BankTrans>
	 */
	public List<BankTrans> queryOperateBankTransInfoDetails(BankTransInfoDetailsVo bankTransInfoDetailsVo);

	/**
	 * 获取转账批次号序列值
	 * @return
	 */
	long getTransBatSeq();
}
