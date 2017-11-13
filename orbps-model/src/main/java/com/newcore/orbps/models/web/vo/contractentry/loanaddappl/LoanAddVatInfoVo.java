package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;

/**
 * 增值税信息Vo
 * @author jincong
 *
 */
public class LoanAddVatInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030002L;
	
	/** 纳税人名称 */
	private String taxpayerName;
	/** 纳税识别号 */
	private String taxpayNo;
	/** 银行代码 */
	private String bankCode;
	/** 开户名称 */
	private String accountName;
	/** 银行账号 */
	private String accountNo;
	
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
	 * @return the taxpayNo
	 */
	public String getTaxpayNo() {
		return taxpayNo;
	}
	/**
	 * @param taxpayNo the taxpayNo to set
	 */
	public void setTaxpayNo(String taxpayNo) {
		this.taxpayNo = taxpayNo;
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
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VatInfoVo [taxpayerName=" + taxpayerName + ", taxpayNo=" + taxpayNo + ", bankCode=" + bankCode
				+ ", accountName=" + accountName + ", accountNo=" + accountNo + "]";
	}
}
