package com.newcore.orbps.service.sff.api;

import javax.jws.WebService;

/**
 * 银行转账操作明细查询
 * @author wangxiao
 * 创建时间：2017年3月4日下午4:57:21
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface OperateBankTransInfoDetailsService {
	/**
     * 生成银行转账操作明细查询
     * OperateBankTransInfoDetailsService
	 * @param String
	 * @return String
     */
	public String queryOperateBankTransInfoDetails(String message);
}
