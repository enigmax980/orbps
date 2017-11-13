package com.newcore.orbpsutils.service.api;

import org.springframework.validation.Errors;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 
 *
 * @author zhoushoubo
 * 创建时间：2016年11月03日13:24:11
 */

public interface InsureValidateService {
	public Boolean baseinfoCheck(GrpInsurAppl grpInsurAppl,Errors arg1);

	public Boolean ipsninfoCheck(GrpInsured grpInsured,Errors arg1);
}
