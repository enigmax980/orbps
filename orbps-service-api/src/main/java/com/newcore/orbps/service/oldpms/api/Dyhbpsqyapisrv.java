package com.newcore.orbps.service.oldpms.api;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.ResponseWrapper;

/**
 * 短险打印服务接口客户端
 * @author JCC
 * 2017年5月17日 10:49:35
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface Dyhbpsqyapisrv {
	
	@ResponseWrapper(localName = "Response", targetNamespace = "http://www.e-chinalife.com/soa/")
	@WebResult(name = "return")
	public String srvhbpsqyapi(String msg);
}
