package com.newcore.orbps.service.api;

import com.newcore.orbps.models.distcode.OccCodeInfo;


public interface DictInitService {
	
	public Boolean insertDict() throws RuntimeException;
	
	public String queryRedisOccCodeList();
	/**	
	 * 获取缓存数据
	 * @return	
	 */
	public OccCodeInfo getRedisOccCode(OccCodeInfo occCode);
}
