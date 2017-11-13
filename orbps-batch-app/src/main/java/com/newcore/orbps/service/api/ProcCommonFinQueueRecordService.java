package com.newcore.orbps.service.api;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 
 * 首席共保产生财务队列数据功能设计
 * @author JCC
 * 2016年11月23日 15:12:42
 */
@FunctionalInterface
public interface ProcCommonFinQueueRecordService {
	/**
	 * 生成财务队列数据
	 * @param 
	 */
	com.newcore.orbps.models.pcms.bo.RetInfo procCommonFinQueueRecord(GrpInsurAppl grpInsurAppl);
}
