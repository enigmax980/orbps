package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

/**
 * 账户对应操作轨迹
 * 
 * @author JCC 2016年11月4日 10:26:55
 */
public class MioAccInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String accNo; 	// 帐号
	private String oclkBranchNo;// 操作员机构号
	private String oclkClerkNo; // 操作员工号
	private String createTime; 	// 创建时间
	private int mioClass; 	// 收付类型： 1：收，-1：付
	private Double anmt; 	// 收付金额

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getOclkBranchNo() {
		return oclkBranchNo;
	}

	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}

	public String getOclkClerkNo() {
		return oclkClerkNo;
	}

	public void setOclkClerkNo(String oclkClerkNo) {
		this.oclkClerkNo = oclkClerkNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getMioClass() {
		return mioClass;
	}

	public void setMioClass(int mioClass) {
		this.mioClass = mioClass;
	}

	public Double getAnmt() {
		return anmt;
	}

	public void setAnmt(Double anmt) {
		this.anmt = anmt;
	}

}
