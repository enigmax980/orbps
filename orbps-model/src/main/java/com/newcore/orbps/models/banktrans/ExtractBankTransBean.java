package com.newcore.orbps.models.banktrans;

public class ExtractBankTransBean {
	/**
	 * 机构号
	 */
	private String branchNo;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 收付类型 [1收] [-1付]
	 */
	private Integer mioClass;
	/**
	 * 是否包含下级机构 [0不包含] [1包含]
	 */
	private Integer fcFlag;
	/**
	 * 业务系统批次编号
	 */
	private Long transBatSeq;
	/**
	 * 本次从那条记录记录开始采集<br />
	 * 抽取完成业务系统置为0
	 */
	private Integer transCode;

	public ExtractBankTransBean() {

	}

	public ExtractBankTransBean(String branchNo, String bankCode,
			Integer mioClass, Integer fcFlag, Long transBatSeq, Integer transCode) {
		this.branchNo = branchNo;
		this.bankCode = bankCode;
		this.mioClass = mioClass;
		this.fcFlag = fcFlag;
		this.transBatSeq = transBatSeq;
		this.transCode = transCode;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getMioClass() {
		return mioClass;
	}

	public void setMioClass(Integer mioClass) {
		this.mioClass = mioClass;
	}

	public Integer getFcFlag() {
		return fcFlag;
	}

	public void setFcFlag(Integer fcFlag) {
		this.fcFlag = fcFlag;
	}

	public Long getTransBatSeq() {
		return transBatSeq;
	}

	public void setTransBatSeq(Long transBatSeq) {
		this.transBatSeq = transBatSeq;
	}

	public Integer getTransCode() {
		return transCode;
	}

	public void setTransCode(Integer transCode) {
		this.transCode = transCode;
	}
}