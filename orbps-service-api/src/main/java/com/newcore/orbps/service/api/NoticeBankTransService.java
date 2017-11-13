package com.newcore.orbps.service.api;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.ResponseWrapper;

import com.newcore.orbps.models.banktrans.RequestBody;
import com.newcore.orbps.models.banktrans.ResponseBody;

/**
 * 银行转账通知服务
 * @author jiachenchen
 * @date 2016年8月12日 10:24:25
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface NoticeBankTransService {
	/**
	 * FTP传输接口方法
	 */
	@ResponseWrapper(localName = "Response", targetNamespace = "http://www.e-chinalife.com/soa/")
	@WebResult(name = "BODY")
	ResponseBody noticeBankTransService(@WebParam(name = "BODY") RequestBody requestBody);
}
