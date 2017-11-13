package com.newcore.orbps.service.gcss.api;

import com.newcore.orbps.model.service.gcss.vo.ImagInformVo;
import com.newcore.orbps.model.service.gcss.vo.NotifyApplDataAuditRet;

import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * 团销系统影像审批通知
 * @author wangxiao
 * 2017年5月22日 10:00:00
 */
@FunctionalInterface
@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
public interface NotifyApplDataAudit {

	@WebResult(name = "return", targetNamespace = "http://www.e-chinalife.com/soa/")
	public NotifyApplDataAuditRet notifyApplDataAudit(ImagInformVo msg);
}
