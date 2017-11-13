package com.newcore.orbps.service.api;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.ResponseWrapper;

import com.newcore.orbps.models.banktrans.RequestBody;
import com.newcore.orbps.models.banktrans.ResponseBody;

/**
 * TODO FTP传输服务端接口
 * 
 * @author jcc 2016年8月16日 14:27:04
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface ProcScInterFace {
	/**
	 * FTP传输接口方法
	 */
	@ResponseWrapper(localName = "Response", targetNamespace = "http://www.e-chinalife.com/soa/")
	@WebResult(name = "BODY")
	ResponseBody procScInterFace(@WebParam(name = "BODY") RequestBody requestBody);
}
