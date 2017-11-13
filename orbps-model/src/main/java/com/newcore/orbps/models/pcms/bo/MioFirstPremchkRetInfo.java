package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;
/**
 * 契约保费检查返回信息
 * @author huangwei
 * @date 2016-08-30
 */
public class MioFirstPremchkRetInfo implements Serializable {

	
	private static final long serialVersionUID = -2121892628319119402L;

	//所有有收费完成标志
	private String allMiFinMark;
	
	//投保单号
	private String applNo;
	
	//字段名：执行结果代码，长度：1，说明：0：执行失败；1：执行成功；
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;
	
	//保费检查的具体返回结果
	private List<MioFirstPremchkRetInfoList> RetInfo;
	
	public String getAllMiFinMark() {
		return allMiFinMark;
	}
	public void setAllMiFinMark(String allMiFinMark) {
		this.allMiFinMark = allMiFinMark;
	}
	public List<MioFirstPremchkRetInfoList> getRetInfo() {
		return RetInfo;
	}
	public void setRetInfo(List<MioFirstPremchkRetInfoList> retInfo) {
		RetInfo = retInfo;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
