package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 被保险人错误信息修改服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日16:28:11
 */
@FunctionalInterface
public interface InsuredErrModifyService {

	public	int doInsuredErrModifyService(GrpInsured grpInsured);
}
