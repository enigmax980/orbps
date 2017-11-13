package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;

/**
 * 增值税信息
 * @author xiaoye
 *
 */
public class GrpVatInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1145469328L;
	/** 纳税人名称 */
	private String taxpayer;
	/** 纳税人识别号 */
	private String taxpayerCode;
	/** 开户银行 */
	private String companyBankCode;
	/** 开户名称 */
	private String companyBankName;
	/** 银行账号 */
	private String companyBankAccNo;

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
	public String getCompanyBankAccNo() {
		return companyBankAccNo;
	}

	/**
	 * @param companyBankAccNo the companyBankAccNo to set
	 */
	public void setCompanyBankAccNo(String companyBankAccNo) {
		this.companyBankAccNo = companyBankAccNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VatInfoVo [taxpayer=" + taxpayer + ", taxpayerCode=" + taxpayerCode + ", companyBankCode="
				+ companyBankCode + ", companyBankName=" + companyBankName + ", companyBankAccNo=" + companyBankAccNo
				+ "]";
	}

	
}
