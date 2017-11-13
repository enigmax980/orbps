package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;

public class SalesInfoVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6435410555436381022L;

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
	 * 管理机构名称
	 */
	private String mgBranchName;
	
	/**
	 * 销售机构名称
	 */
	private String salesBranchName;
	
	/**
	 * 归档机构号
	 */
	private String archiveBranchNo;
	
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
	 * 网点是否失效
	 */
	private String deptState;
	
	/**
	 * 网点名称
	 */
	private String deptName;

	/**
	 * @return the saleState
	 */
	public String getSaleState() {
		return saleState;
	}

	/**
	 * @param saleState the saleState to set
	 */
	public void setSaleState(String saleState) {
		this.saleState = saleState;
	}

	/**
	 * @return the provBranchNo
	 */
	public String getProvBranchNo() {
		return provBranchNo;
	}

	/**
	 * @param provBranchNo the provBranchNo to set
	 */
	public void setProvBranchNo(String provBranchNo) {
		this.provBranchNo = provBranchNo;
	}

	/**
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	/**
	 * @return the mgBranchName
	 */
	public String getMgBranchName() {
		return mgBranchName;
	}

	/**
	 * @param mgBranchName the mgBranchName to set
	 */
	public void setMgBranchName(String mgBranchName) {
		this.mgBranchName = mgBranchName;
	}

	/**
	 * @return the salesBranchName
	 */
	public String getSalesBranchName() {
		return salesBranchName;
	}

	/**
	 * @param salesBranchName the salesBranchName to set
	 */
	public void setSalesBranchName(String salesBranchName) {
		this.salesBranchName = salesBranchName;
	}

	/**
	 * @return the archiveBranchNo
	 */
	public String getArchiveBranchNo() {
		return archiveBranchNo;
	}

	/**
	 * @param archiveBranchNo the archiveBranchNo to set
	 */
	public void setArchiveBranchNo(String archiveBranchNo) {
		this.archiveBranchNo = archiveBranchNo;
	}

	/**
	 * @return the salesName
	 */
	public String getSalesName() {
		return salesName;
	}

	/**
	 * @param salesName the salesName to set
	 */
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	/**
	 * @return the salesBranchAddr
	 */
	public String getSalesBranchAddr() {
		return salesBranchAddr;
	}

	/**
	 * @param salesBranchAddr the salesBranchAddr to set
	 */
	public void setSalesBranchAddr(String salesBranchAddr) {
		this.salesBranchAddr = salesBranchAddr;
	}

	/**
	 * @return the salesBranchPostCode
	 */
	public String getSalesBranchPostCode() {
		return salesBranchPostCode;
	}

	/**
	 * @param salesBranchPostCode the salesBranchPostCode to set
	 */
	public void setSalesBranchPostCode(String salesBranchPostCode) {
		this.salesBranchPostCode = salesBranchPostCode;
	}

	/**
	 * @return the centerCode
	 */
	public String getCenterCode() {
		return centerCode;
	}

	/**
	 * @param centerCode the centerCode to set
	 */
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	/**
	 * @return the deptState
	 */
	public String getDeptState() {
		return deptState;
	}

	/**
	 * @param deptState the deptState to set
	 */
	public void setDeptState(String deptState) {
		this.deptState = deptState;
	}

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
