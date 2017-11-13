package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

public class SalesmanQueryReturnBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3889134069484009052L;

	/**
	 * 执行结果代码
	 */
	private String retCode ;
	
	/**
	 * 错误信息
	 */
	private String errMsg;
	
	/**
	 * 是否可销售
	 */
	private String saleState;
	
	/**
	 * 省级机构号
	 */
	private String provBranchNo;
	
	/**
	 * 管理机构号
	 */
	private String mgrBranchNo;
	
	/**
	 * 销售机构名称
	 */
	private String salesBranchName;
	/**
	 * 归档机构号
	 */
	private String arcBranchNo;
	
	/**
	 * 销售员姓名
	 */
	private String salesName;
	
	/**
	 * 销售机构地址
	 */
	private String salesBranchAddr;
	
	/**
	 * 销售机构邮政编码
	 */
	private String salesBranchPostCode;
	
	/**
	 * 成本中心
	 */
	private String centerCode;
	
	/**
	 * 手机号
	 */
	private String  salesMobile;
	
	public String getSalesMobile() {
		return salesMobile;
	}

	public void setSalesMobile(String salesMobile) {
		this.salesMobile = salesMobile;
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

	public String getSaleState() {
		return saleState;
	}

	public void setSaleState(String saleState) {
		this.saleState = saleState;
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

	public String getSalesBranchName() {
		return salesBranchName;
	}

	public void setSalesBranchName(String salesBranchName) {
		this.salesBranchName = salesBranchName;
	}

	public String getArcBranchNo() {
		return arcBranchNo;
	}

	public void setArcBranchNo(String arcBranchNo) {
		this.arcBranchNo = arcBranchNo;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
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

	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	
	
}
