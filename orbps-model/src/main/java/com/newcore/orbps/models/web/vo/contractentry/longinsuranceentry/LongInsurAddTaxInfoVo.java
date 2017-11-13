package com.newcore.orbps.models.web.vo.contractentry.longinsuranceentry;

import java.io.Serializable;

/**
 * 增值税信息
 * @author wangyupeng
 *
 */
public class LongInsurAddTaxInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149544543L;
	
	/** 纳税人名称 */
	private String taxpayer;
	/**纳税识别号**/
	private String taxpayerCode;
	/**开户银行代码**/
	private String companyBankCode;
	/**开户名称**/
	private String companyBankName;
	/**银行帐号**/
	private Long companyBankAccNo;
	/**
	 * @return the taxpayer
	 */
	public String getTaxpayer() {
		return taxpayer;
	}
	/**
	 * @param taxpayer the taxpayer to set
	 */
	public void setTaxpayer(String taxpayer) {
		this.taxpayer = taxpayer;
	}
	/**
	 * @return the taxpayerCode
	 */
	public String getTaxpayerCode() {
		return taxpayerCode;
	}
	/**
	 * @param taxpayerCode the taxpayerCode to set
	 */
	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}
	/**
	 * @return the companyBankCode
	 */
	public String getCompanyBankCode() {
		return companyBankCode;
	}
	/**
	 * @param companyBankCode the companyBankCode to set
	 */
	public void setCompanyBankCode(String companyBankCode) {
		this.companyBankCode = companyBankCode;
	}
	/**
	 * @return the companyBankName
	 */
	public String getCompanyBankName() {
		return companyBankName;
	}
	/**
	 * @param companyBankName the companyBankName to set
	 */
	public void setCompanyBankName(String companyBankName) {
		this.companyBankName = companyBankName;
	}
	/**
	 * @return the companyBankAccNo
	 */
	public Long getCompanyBankAccNo() {
		return companyBankAccNo;
	}
	/**
	 * @param companyBankAccNo the companyBankAccNo to set
	 */
	public void setCompanyBankAccNo(Long companyBankAccNo) {
		this.companyBankAccNo = companyBankAccNo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddTaxInfo [taxpayer=" + taxpayer + ", taxpayerCode=" + taxpayerCode + ", companyBankCode="
				+ companyBankCode + ", companyBankName=" + companyBankName + ", companyBankAccNo=" + companyBankAccNo
				+ "]";
	}
	
}
