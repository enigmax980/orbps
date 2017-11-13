package com.newcore.orbps.service.api;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 抽取银行转账数据接口
 * @author 王鸿林
 * 2016年11月15日 17:29:11
 */
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface ExtractBankTransService {

	/**
	 * 抽取银行转账数据
	 * 
	 * @param message
	 * @return
	 */
	@RequestWrapper(localName = "extract", targetNamespace = "http://www.e-chinalife.com/soa/")
	@ResponseWrapper(localName = "extractResponse", targetNamespace = "http://www.e-chinalife.com/soa/")
	@WebResult(name = "return",targetNamespace = "http://www.e-chinalife.com/soa/")
	String extract(@WebParam(name = "msg", targetNamespace = "http://www.e-chinalife.com/soa/") String message);
}
