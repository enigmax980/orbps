package com.newcore.orbps.service.api;

/**
 * 账户余额冻结功能
 * @author JCC
 * 2016年11月1日 15:25:45
 */
@FunctionalInterface
public interface ProcNewApplPSInfoService {
	/**
	 * 账户余额冻结功能方法
	 * @param applNo 投保单号
	 */
	public boolean procPSInfo(String applNo);
}
