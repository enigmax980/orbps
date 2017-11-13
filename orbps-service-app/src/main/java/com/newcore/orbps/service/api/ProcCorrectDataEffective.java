package com.newcore.orbps.service.api;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 订正
 *
 * @author lijifei
 * 创建时间：2016年12月14日13:24:11
 */
@FunctionalInterface
public interface ProcCorrectDataEffective {

	RetInfo correctGrpInsurAppl(GrpInsurAppl grpInsurAppl);
	
}
