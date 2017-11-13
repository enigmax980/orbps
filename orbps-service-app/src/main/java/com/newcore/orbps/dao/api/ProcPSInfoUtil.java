package com.newcore.orbps.dao.api;

import java.util.Map;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 回退冲正-获取账户信息与对应的金额
 * @author LJF
 * 2017年3月2日 20:19:55
 */
public interface ProcPSInfoUtil {

	
	/**
	 * 获取险种加费金额
	 * @param 
	 * 		grpInsurAppl 保单基本信息
	 * @return
	 * 	账户信息与对应的金额
	 */
	Map<String,Double> procCorrPSData(GrpInsurAppl grpInsurAppl);

	 /**
     * 根据投保单号更新更新监控表记录中的status和帽子落地状态
     * @param applNo
     * @param status
     * @param statusGrp
     * @return
     */
    public Boolean updateStatusAndApplLandFlagByApplNo(String applNo, String status , String statusGrp);
}
