package com.newcore.orbpsutils.dao.api;

import java.util.List;

import com.newcore.orbps.models.finance.MioAccInfoLog;

/**
 * 收付账户日志表Dao
 * @author JCC
 * 2017年2月22日 14:37:32
 */
public interface MioAccInfoLogDao {

	/**
	 * 获取自增长序列主键
	 * @return
	 * max accLogId
	 */ 
	long queryAccLogId();

	/**
	 * 批量入库
	 * @param accList 账户日志信息
	 * @return
	 */
	boolean insertMioAccInfoLogList(List<MioAccInfoLog> accList);
	
}
