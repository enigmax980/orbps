package com.newcore.orbpsutils.dao.api;

/**
 * 系统参数配置表
 * @author JCC
 * 2017年5月17日 14:22:49
 */
public interface BizParameterDao {
	/**
	 * 是否调用老打印系统开关
	 * @param bizParamCode 参数编码
	 * @return
	 */
	String findBizParam(String bizParamCode);
}
