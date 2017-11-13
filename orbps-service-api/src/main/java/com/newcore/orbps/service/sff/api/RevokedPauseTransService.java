package com.newcore.orbps.service.sff.api;


import javax.jws.WebService;

/**
 * @author huanglong
 * @date 2017年2月28日
 * @content 撤销暂停送划登记提交
 */
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
@FunctionalInterface
public interface RevokedPauseTransService {
	public String repeal(String xml);
}
