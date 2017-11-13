package com.newcore.orbps.dao.api;

import java.util.List;

import com.newcore.orbps.models.distcode.OccCodeInfo;

@FunctionalInterface
public interface DictInitDao {
	/**
	 * 查询字典集合Dao接口
	 * @param occClassCode
	 * @param occSubclsCode	
	 * @param occCode	
	 * @return
	 */
	public List<OccCodeInfo> queryDict(); 
}
