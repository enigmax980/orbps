package com.newcore.orbps.service.api;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 关于订正、撤销以及冲正处理服务
 * @author JCC
 * 2016年10月27日 09:45:22
 */

public interface ProcCorrMioDataService {

	/**
	 * 处理投保单订正过程中实收数据冲正流水
	 * @param applNo 投保单号
	 * @param pclkBranchNo 操作员机构号
	 * @param pclkNo 操作员工号
	 * @param isProcMio 回退撤销标志 （Y 回退、S 要约撤销）
	 * @return
	 */
	RetInfo setCorrInsurInfo(String applNo,String pclkBranchNo,String pclkNo , String isProcMio);
	/**
	 *订正
	 * @param applNo 投保单号
	 * @author LJF
	 * @return
	 */
	public RetInfo correctDate(String applNo);
}
