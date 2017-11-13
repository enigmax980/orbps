package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 销售相关
 * @author wangxiao
 * 创建时间：2016年7月21日上午9:16:23
 */
public class SalesInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -874712796075730195L;

	/* 字段名：销售渠道，长度：2，是否必填：是 */
	private String salesChannel;

	/* 字段名：销售机构，长度：6，是否必填：是 */
	private String salesBranchNo;
	
	/* 字段名：销售机构名称，长度：128，是否必填：是 */
	private String salesBranchName;

	/* 字段名：销售机构地址，长度：680，是否必填：否 */
	private String salesBranchAddr;

	/* 字段名：销售机构邮政编码，长度：6，是否必填：否 */
	private String salesBranchPostCode;

	/* 字段名：销售员工号，长度：8，是否必填：是 */
	private String salesNo;

	/* 字段名：销售员姓名，长度：200，是否必填：是 */
	private String salesName;

	/* 字段名：网点号，长度：8，是否必填：IF 团险渠道OA || 银保渠道OA THEN 非空 */
	private String salesDeptNo;

	/* 字段名：网点名称，长度：64，是否必填：IF 团险渠道OA || 银保渠道OA THEN 非空 */
	private String deptName;

	/* 字段名：网点代理员工号，长度：8，是否必填：IF 北分团险渠道OA THEN 非空 */
	private String commnrPsnNo;

	/* 字段名：网点代理员姓名，长度：200，是否必填：IF 北分团险渠道OA THEN 非空 */
	private String commnrPsnName;

	/* 字段名：成本中心，长度：10，是否必填：否 */
	private String centerCode;

	/*
	 * 字段名：共同展业主副标记，长度：2，是否必填：IF salesDevelopFlag == 1 THEN 非空" 
	 * 1：主销售员；2：附销售员
	 */
	private String develMainFlag;

	/* 字段名：展业比例，是否必填：IF salesDevelopFlag == 1 THEN 非空 */
	private Double developRate;

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
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
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}

	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
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
	 * @return the salesDeptNo
	 */
	public String getSalesDeptNo() {
		return salesDeptNo;
	}

	/**
	 * @param salesDeptNo the salesDeptNo to set
	 */
	public void setSalesDeptNo(String salesDeptNo) {
		this.salesDeptNo = salesDeptNo;
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

	/**
	 * @return the commnrPsnNo
	 */
	public String getCommnrPsnNo() {
		return commnrPsnNo;
	}

	/**
	 * @param commnrPsnNo the commnrPsnNo to set
	 */
	public void setCommnrPsnNo(String commnrPsnNo) {
		this.commnrPsnNo = commnrPsnNo;
	}

	/**
	 * @return the commnrPsnName
	 */
	public String getCommnrPsnName() {
		return commnrPsnName;
	}

	/**
	 * @param commnrPsnName the commnrPsnName to set
	 */
	public void setCommnrPsnName(String commnrPsnName) {
		this.commnrPsnName = commnrPsnName;
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
	 * @return the develMainFlag
	 */
	public String getDevelMainFlag() {
		return develMainFlag;
	}

	/**
	 * @param develMainFlag the develMainFlag to set
	 */
	public void setDevelMainFlag(String develMainFlag) {
		this.develMainFlag = develMainFlag;
	}

	/**
	 * @return the developRate
	 */
	public Double getDevelopRate() {
		return developRate;
	}

	/**
	 * @param developRate the developRate to set
	 */
	public void setDevelopRate(Double developRate) {
		this.developRate = developRate;
	}

}
