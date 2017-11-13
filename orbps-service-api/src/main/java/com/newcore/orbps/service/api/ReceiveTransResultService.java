package com.newcore.orbps.service.api;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 处理银行转账结果
 * 
 * @author 王鸿林
 *
 */
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
@FunctionalInterface
public interface ReceiveTransResultService {
	/**
	 * 处理转账结果
	 *
	 * @param bean
	 * @param oclk
	 * @return
	 * @throws Exception
	 */
	@RequestWrapper(localName = "receive", targetNamespace = "http://www.e-chinalife.com/soa/")
	@ResponseWrapper(localName = "receiveResponse", targetNamespace = "http://www.e-chinalife.com/soa/")
	@WebResult(name = "return" ,targetNamespace = "http://www.e-chinalife.com/soa/")
	String receive(@WebParam(name = "msg", targetNamespace = "http://www.e-chinalife.com/soa/") String message);
}
