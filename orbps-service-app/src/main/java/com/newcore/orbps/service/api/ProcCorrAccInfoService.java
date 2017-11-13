package com.newcore.orbps.service.api;

import java.util.Map;

/**
 * 转保费功能
 * @author JCC
 * 2017年1月12日 15:25:45
 */
@FunctionalInterface
public interface ProcCorrAccInfoService {
	/**
	 * 转保费功能方法
	 * @param applNo 投保单号
	 */
	public Map<String,Double>  procPSInfo(String applNo);
}
