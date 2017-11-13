package com.newcore.orbps.service.sff.api;

import javax.jws.WebService;

/**
 * 不转账数据服务
 * @author wangxiao
 * 2016年10月8日 10:43:37
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface NotTransInfoService {
	/**
     * 生成不转账数据
     * @return
     */
    public String queryNotTransInfo(String message);
}
