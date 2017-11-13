package com.newcore.orbps.service.api;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 产生应收数据服务
 * @author JCC
 * 2016-08-23 16:29:19
 */
@FunctionalInterface
public interface ProcPlnmioRecRecordService {
	
	/**
	 * 产生应收数据操作
	 * @param 
	 * 	grpInsurAppl 保单信息
	 * @param
	 *  taskId 任务流ID
	 * @return 
	 */
	public boolean procPlnmioRecRecord(GrpInsurAppl grpInsurAppl,String taskId);
}
