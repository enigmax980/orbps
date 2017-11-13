package com.newcore.orbps.service.sff.api;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 暂停送划登记查询服务接口
 * @author LiSK
 *
 */

@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface QueryPauseTransInfoService {
	
	@RequestWrapper(localName = "queryPauseTrans", targetNamespace = "http://www.e-chinalife.com/soa/")
    @ResponseWrapper(localName = "queryResponseData", targetNamespace = "http://www.e-chinalife.com/soa/")
    @WebResult(name = "return")
	public String queryPauseTrans(String message);
}
