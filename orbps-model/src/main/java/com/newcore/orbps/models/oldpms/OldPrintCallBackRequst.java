package com.newcore.orbps.models.oldpms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 保单辅助打印数据落地反馈服务入参
 * 
 * @author JCC 2017年5月17日 09:25:59
 */
public class OldPrintCallBackRequst implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "投保单号为空")
	@Size(min = 16,max = 16 ,message = "投保单号长度必须是16位.")
	private String applNo; 		//投保单号
	
	@Size(min = 22,max = 25 ,message = "组合保单号长度必须是22-25位.")
	private String cgNo; 		//组合保单号
	
	@NotNull(message = "管理机构号为空")
	@Size(min = 6,max = 6 ,message = "管理机构号长度必须是6位.")
	private String mgrBranchNo; //管理机构号
	
	@NotNull(message = "落地结果代码为空")
	@Size(min = 1,max = 1 ,message = "落地结果代码长度必须是1位.")
	private String retCode; 	//落地结果代码
	
	@NotNull(message = "落地结果失败错误信息为空")
	@Size(min = 1,max = 500 ,message = "落地结果失败错误信息长度必须是1-500位.")
	private String errMsg; 		//落地结果失败错误信息

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
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
