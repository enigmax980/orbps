package com.newcore.orbps.service.api;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 产生首席共保应付功能服务
 * @author JCC
 * 2016年11月19日 14:20:08
 */
@FunctionalInterface
public interface ProcCommonPlnmioRecRecordService {
	
  /**
   * 产生首席共保应付功能
   * @param grpInsurAppl
   */
  boolean procCommonPlnmioRecRecord(GrpInsurAppl grpInsurAppl);
}
