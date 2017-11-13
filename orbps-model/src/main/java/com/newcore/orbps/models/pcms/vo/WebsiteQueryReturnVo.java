package com.newcore.orbps.models.pcms.vo;

import java.io.Serializable;

public class WebsiteQueryReturnVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3094870272426867628L;

	/**
	 * 执行结果代码
	 */
	private String retCode ;
	
	/**
	 * 错误信息
	 */
	private String errMsg;
	
	/**
	 * 网点是否失效
	 */
	private String deptState;
	
	/**
	 * 网点名称
	 */
	private String deptName;
	
	/**
	 * 省级机构号
	 */
	private String provBranchNo;
	
	/**
	 * 管理机构号
	 */
	private String mgrBranchNo;
	
	/**
	 * 管理机构名称
	 */
	private String mgBranchName;
	
	/**
	 * 归档机构号
	 */
	private String archiveBranchNo;
	
	/**
	 * 成本中心
	 */
	private String centerCode;
	
	/**
	 * 销售机构地址
	 */
	private String salesBranchAddr;
	
	/**
	 * 销售机构邮政编码
	 */
	private String salesBranchPostCode;

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

	public String getDeptState() {
		return deptState;
	}

	public void setDeptState(String deptState) {
		this.deptState = deptState;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProvBranchNo() {
		return provBranchNo;
	}

	public void setProvBranchNo(String provBranchNo) {
		this.provBranchNo = provBranchNo;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getMgBranchName() {
		return mgBranchName;
	}

	public void setMgBranchName(String mgBranchName) {
		this.mgBranchName = mgBranchName;
	}

	public String getArchiveBranchNo() {
		return archiveBranchNo;
	}

	public void setArchiveBranchNo(String archiveBranchNo) {
		this.archiveBranchNo = archiveBranchNo;
	}

	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	public String getSalesBranchAddr() {
		return salesBranchAddr;
	}

	public void setSalesBranchAddr(String salesBranchAddr) {
		this.salesBranchAddr = salesBranchAddr;
	}

	public String getSalesBranchPostCode() {
		return salesBranchPostCode;
	}

	public void setSalesBranchPostCode(String salesBranchPostCode) {
		this.salesBranchPostCode = salesBranchPostCode;
	}
	
	
}
