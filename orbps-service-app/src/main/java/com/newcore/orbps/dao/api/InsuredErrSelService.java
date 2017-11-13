package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 被保险人错误信息提交服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日20:24:11
 */
@FunctionalInterface
public interface InsuredErrSelService {
	
	public GrpInsured doInsuredErrSelService(String cgNo,int ipsnNo);
}
