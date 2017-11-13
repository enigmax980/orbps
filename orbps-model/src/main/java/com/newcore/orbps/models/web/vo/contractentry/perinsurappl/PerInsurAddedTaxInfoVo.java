package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
/**
 * 增值税信息
 * @author wangyupeng
 *
 */
public class PerInsurAddedTaxInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 2149444545L;
	
	/** 纳税人名称 */
	private String taxpayerName;
	/**纳税识别号**/
	private String taxpayNo;
	/**开户银行代码**/
	private String accountCode;
	/**开户名称**/
	private String accountName;
	/**银行帐号**/
	private Long bankNo;
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
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}
	/**
	 * @param accountCode the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
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
	 * @return the bankNo
	 */
	public Long getBankNo() {
		return bankNo;
	}
	/**
	 * @param bankNo the bankNo to set
	 */
	public void setBankNo(Long bankNo) {
		this.bankNo = bankNo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddedTaxInfo [taxpayerName=" + taxpayerName + ", taxpayNo=" + taxpayNo + ", accountCode=" + accountCode
				+ ", accountName=" + accountName + ", bankNo=" + bankNo + "]";
	}


}
