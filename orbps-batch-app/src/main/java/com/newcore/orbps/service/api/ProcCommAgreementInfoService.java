package com.newcore.orbps.service.api;

import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 共保协议落地服务
 * @author JCC
 * 2016年12月14日 16:52:01
 */
public interface ProcCommAgreementInfoService {
	/**
	 * 处理共保协议落地方法
	 * @param grpInsurAppl
	 * @return
	 */
	RetInfo commonAgreement(GrpInsurAppl grpInsurAppl);
}
