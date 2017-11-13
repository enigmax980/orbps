package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;

/**
 * 增值税信息
 * @author wangyanjie
 *
 */
public class SgGrpVatInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000015L;
	
	/** 纳税人名称 */
	private String taxpayerName;
	/** 纳税人识别号 */
	private String taxIdNo;
	/** 开户银行 */
	private String bankCode;
	/** 开户名称 */
	private String bankBranchName;
	/** 银行账号 */
	private String bankaccNo;
	/**
	 * @return the taxpayerName
	 */
	public String getTaxpayerName() {
		return taxpayerName;
	}
	/**
	 * @param taxpayerName the taxpayerName to set
	 */
	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}
	/**
	 * @return the taxIdNo
	 */
	public String getTaxIdNo() {
		return taxIdNo;
	}
	/**
	 * @param taxIdNo the taxIdNo to set
	 */
	public void setTaxIdNo(String taxIdNo) {
		this.taxIdNo = taxIdNo;
	}
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
	 * @return the bankBranchName
	 */
	public String getBankBranchName() {
		return bankBranchName;
	}
	/**
	 * @param bankBranchName the bankBranchName to set
	 */
	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	/**
	 * @return the bankaccNo
	 */
	public String getBankaccNo() {
		return bankaccNo;
	}
	/**
	 * @param bankaccNo the bankaccNo to set
	 */
	public void setBankaccNo(String bankaccNo) {
		this.bankaccNo = bankaccNo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VatInfoVo [taxpayerName=" + taxpayerName + ", taxIdNo=" + taxIdNo + ", bankCode=" + bankCode
				+ ", bankBranchName=" + bankBranchName + ", bankaccNo=" + bankaccNo + "]";
	}

}
