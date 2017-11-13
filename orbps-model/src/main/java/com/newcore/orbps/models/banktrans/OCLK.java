package com.newcore.orbps.models.banktrans;

public class OCLK {
	/**
	 * 操作员机构号
	 */
	private String oclkBranchNo;
	/**
	 * 操作员代码
	 */
	private String oclkClerkNo;

	public OCLK() {

	}

	public OCLK(String oclkBranchNo, String oclkClerkNo) {
		this.oclkBranchNo = oclkBranchNo;
		this.oclkClerkNo = oclkClerkNo;
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
}