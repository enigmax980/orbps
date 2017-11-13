package com.newcore.orbps.models.banktrans;

public class Branch {
	/**
	 * 省份机构号
	 */
	private String provBranch;
	/**
	 * 省份机构号-分解
	 */
	private String provBranchTrim;
	/**
	 * 省份名称
	 */
	private String provName;
	/**
	 * 排除的机构号
	 */
	private String debarBranch;
	/**
	 * 排除的机构号-分解
	 */
	private String debarBranchTrim;

	/**
	 * 
	 * @param provBranch
	 * @param provBranchTrim
	 * @param provName
	 * @param debarBranch
	 * @param debarBranchTrim
	 */
	public Branch(String provBranch, String provBranchTrim, String provName,
			String debarBranch, String debarBranchTrim) {
		this.provBranch = provBranch;
		this.provBranchTrim = provBranchTrim;
		this.provName = provName;
		this.debarBranch = debarBranch;
		this.debarBranchTrim = debarBranchTrim;
	}

	public String getProvBranch() {
		return provBranch;
	}

	public String getProvBranchTrim() {
		return provBranchTrim;
	}

	public String getProvName() {
		return provName;
	}

	public String getDebarBranch() {
		return debarBranch;
	}

	public String getDebarBranchTrim() {
		return debarBranchTrim;
	}
}