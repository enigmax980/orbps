package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangxiao
 * 创建时间：2017年3月4日下午4:29:34
 */
public class BankTransInfoDetailsVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8388775256010523198L;
	
	private String bankCode;
	
	private String mioClass;
	
	private String minGenDate;
	
	private String maxGenDate;
	
	private List<String> mgrBranchNo;

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the mioClass
	 */
	public String getMioClass() {
		return mioClass;
	}

	/**
	 * @param mioClass the mioClass to set
	 */
	public void setMioClass(String mioClass) {
		this.mioClass = mioClass;
	}

	/**
	 * @return the minGenDate
	 */
	public String getMinGenDate() {
		return minGenDate;
	}

	/**
	 * @param minGenDate the minGenDate to set
	 */
	public void setMinGenDate(String minGenDate) {
		this.minGenDate = minGenDate;
	}

	/**
	 * @return the maxGenDate
	 */
	public String getMaxGenDate() {
		return maxGenDate;
	}

	/**
	 * @param maxGenDate the maxGenDate to set
	 */
	public void setMaxGenDate(String maxGenDate) {
		this.maxGenDate = maxGenDate;
	}

	/**
	 * @return the mgrBranchNo
	 */
	public List<String> getMgrBranchNo() {
		return mgrBranchNo;
	}

	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(List<String> mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	
}
