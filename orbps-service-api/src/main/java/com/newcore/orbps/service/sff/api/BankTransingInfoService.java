package com.newcore.orbps.service.sff.api;

import javax.jws.WebService;

/**
 * @author wangxiao
 * 创建时间：2017年3月2日上午9:00:05
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface BankTransingInfoService {
	/**
	 * 待转账数据查询
	 * BankTransingInfoService
	 * @param String
	 * @return String
	 */
	public String queryBankTransingInfo(String message);
}
