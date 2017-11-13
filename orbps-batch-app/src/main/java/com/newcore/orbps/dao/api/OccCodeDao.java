package com.newcore.orbps.dao.api;

/**
 * @author wangxiao
 * 创建时间：2016年9月8日下午3:40:48
 */
@FunctionalInterface
public interface OccCodeDao {
	//查询职业风险等级
	public String searchIpsnOccClassLevel(String ipsnOccClassCode);
}
