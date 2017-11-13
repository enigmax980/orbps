package com.newcore.orbpsutils.dao.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.newcore.orbps.models.finance.MioLog;


/**
 * 实收付流水表MioLog对应DAO接口
 * 2017-02-15
 * @author 李四魁
 *
 */
public interface MioLogDao {


	/**
	 * 批量插入实收付数据
	 * @param mlList 要插入的实收付数据
	 * @return true--插入成功   false--插入失败
	 */
	public boolean insertMioLogList(List<MioLog> mlList);

	/**
	 * 插入一条实收付数据
	 * @param ml 要插入的实收付数据
	 * @return true--插入成功   false--插入失败
	 */
	public boolean insertOneMioLog(MioLog ml);

	/**
	 * 获取mio_log_id自增长序列最新值加1
	 * @return
	 */
	public long getMioLogIdSeq();

	/**
	 * 获取bat_no自增长序列最新值加1
	 * @return
	 */
	public long getBatNo();

	/**
	 * 根据批次号更新实收付流水中的实收付流水落地状态
	 * @param batNo 需要更新的实收付流水落地批次号
	 * @param coreStat 要更新成的状态
	 * @return
	 */
	public boolean updateMioLogStat(long batNo, String coreStat);

	/**
	 * 获取实收数据集合
	 * @param map 查询条件集合
	 * key = 表字段
	 * val = 字段内容
	 * @return
	 */
	public List<MioLog> queryMioLogList(Map<String, String> param);

	/**
	 * 根据实收数据ID更新实收付流水(回退冲正时调用)
	 * @param mioLogId 实收ID(唯一)
	 * @return
	 */
	boolean updateMioLog(long mioLogId , Integer mioTxClass);

	/**
	 * 获得转保费总保费(回退冲正时调用)
	 * @param mioClass 收付类型
	 * @param mioItemCode 收付项目代码
	 * @param mioType 收付方式代码
	 * @param applNo 投保单号
	 * @param mioTxClass 收付交易类型
	 * @return
	 */
	Double getMioLogSumAmnt(Integer mioClass , String mioItemCode , String mioType , String applNo , Integer mioTxClass);

	/**
	 * 获取实收数据集合
	 * @param cgNo 合同组号
	 * @return
	 */
	public List<MioLog> queryMioLogList(String cgNo);

	/**
	 * 获取写流水日期
	 * @param no 投保单号/组合保单号
	 * @param type 查询类别
	 * APPL-投保单号，CGNO-组合保单号
	 * @return
	 */
	public Date getMioLogUpdTime(String applNo, String type);
}
