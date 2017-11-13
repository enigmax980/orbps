package com.newcore.orbps.models.pcms.vo;

import java.io.Serializable;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

public class GrpInsurApplVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1700685984107557444L;
	
	private String errCode;  //错误提示 1 成功 0失败
	
	private String errMsg;  //错误提示信息
	
	private String applNo;	//投保单号
	
	private GrpInsurAppl grpInsurAppl;    //团单基本信息
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public GrpInsurAppl getGrpInsurAppl() {
		return grpInsurAppl;
	}
	public void setGrpInsurAppl(GrpInsurAppl grpInsurAppl) {
		this.grpInsurAppl = grpInsurAppl;
	}
	
}
