package com.newcore.orbps.service.api;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 产生分期应收数据服务
 * @author JCC
 * 2016年9月5日 10:28:12
 */
@FunctionalInterface
public interface ProPlnmioRecStepService {
	/**
	 * 产生分期应收数据方法
	 * @param grpInsurAppl
	 */
	public boolean proPlnmioRecStep(GrpInsurAppl grpInsurAppl);
}
