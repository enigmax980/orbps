package com.newcore.orbps.service.sff.api;

import javax.jws.WebService;

/**
 * 银行转账操作查询
 * @author lijifei
 * 2017年3月3日 19:43:37
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface QueryOperateBankTransInfo {

	/**
	 * 生成不转账数据
	 * @return
	 */
	public String queryOperateBankTrans(String message);
}
