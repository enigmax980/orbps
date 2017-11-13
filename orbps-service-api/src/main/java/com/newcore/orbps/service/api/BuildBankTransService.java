package com.newcore.orbps.service.api;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 生成银行转账数据服务
 * @author jcc
 * 2016年10月8日 10:43:37
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface BuildBankTransService {
	/**
     * 生成转账数据
     * @return
     */
    @RequestWrapper(localName = "build", targetNamespace = "http://www.e-chinalife.com/soa/")
    @ResponseWrapper(localName = "buildResponse", targetNamespace = "http://www.e-chinalife.com/soa/")
    @WebResult(name = "return")
    String build(@WebParam(name = "msg", targetNamespace = "http://www.e-chinalife.com/soa/") String message);
}
