package com.newcore.orbpsutils.dao.api;

import java.util.List;

import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.PauseTransData;
import com.newcore.orbps.models.finance.QueryPauseTransInfoBean;
import com.newcore.supports.models.service.bo.NullValue;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 暂停送划表DAO
 * @author JCC
 * 2017年2月27日 10:11:05
 */
public interface MioNot2bankDao {
	
	/**
	 * 统计暂停送划表中满足条件的数据条数
	 * @param 
	 * 	plnmioRecId 应收付标识
	 * @return
	 *  ROWNUM
	 */
	int queryMioNot2bankRow(MiosNotToBank mioToBank);

	/**
	 * 新增暂停送划数据
	 * @param 
	 * 	mioToBank 暂停送划数据
	 * @return
	 *  成功：true 失败：false
	 */
	boolean insertMioNot2bank(MiosNotToBank mioToBank);

	/**
	 * 修改暂停送划数据
	 * @param mioToBank
	 * @return
	 */
	boolean updateMioNot2bank(MiosNotToBank mioToBank);
	
	/**
	 * 根据应收付信息查询暂停送划数据
	 * @param pauseTransData
	 * @return
	 */
	MiosNotToBank queryMioNot2bankInfo(PauseTransData pauseTransData);
	
	/**
	 * 查询暂停送划历史信息
	 * @param bean  查询条件
	 * @param page  分页条件
	 * @return 
	 */
	List<MiosNotToBank> queryHistoryNotTransInfo(QueryPauseTransInfoBean bean, PageQuery<NullValue> page);

}
