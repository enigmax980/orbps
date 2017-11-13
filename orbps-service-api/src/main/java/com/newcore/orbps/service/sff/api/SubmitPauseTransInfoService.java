package com.newcore.orbps.service.sff.api;

import javax.jws.WebService;

/**
 * 暂停送划登记提交服务
 * @author JCC
 * 2017年2月27日 09:57:23
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface SubmitPauseTransInfoService {

	/**
	 * 校验暂停送划数据是否可入库
	 * @return
	 */
	String checkPauseTransInfo(String message);
}
