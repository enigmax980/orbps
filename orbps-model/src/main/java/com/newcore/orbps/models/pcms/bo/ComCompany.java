package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ComCompany implements Serializable {

	private static final long serialVersionUID = -1371177223245466783L;
	/*
	 * 公司名称
	 */
	@NotNull(message="[公司名称不能为空]")
	@Length(max=200,message="[公司名称长度不能大于200位]")
	private String companyName;
	private String companyCode;
	/*
	 * 共保类型
	 */
	@NotNull(message="[共保类型不能为空]")
	@Length(max=1,message="[共保类型长度不能大于1位]")
	private String coinsurType;
	/*
	 * 是否本公司
	 */
	@NotNull(message="[是否本公司不能为空]")
	@Length(max=1,message="[是否本公司长度不能大于1位]")
	private String companyFlag;
	private Double coinsurAmntPct;
	/*
	 * 共保责任份额比例
	 */
	@NotNull(message="[共保责任份额比例不能为空]")
	private Double coinsurResponsePct;
	private String bankCode;
	private String bankAccName;
	private String bankAccNo;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCoinsurType() {
		return coinsurType;
	}
	public void setCoinsurType(String coinsurType) {
		this.coinsurType = coinsurType;
	}
	public String getCompanyFlag() {
		return companyFlag;
	}
	public void setCompanyFlag(String companyFlag) {
		this.companyFlag = companyFlag;
	}
	public Double getCoinsurAmntPct() {
		return coinsurAmntPct;
	}
	public void setCoinsurAmntPct(Double coinsurAmntPct) {
		this.coinsurAmntPct = coinsurAmntPct;
	}
	public Double getCoinsurResponsePct() {
		return coinsurResponsePct;
	}
	public void setCoinsurResponsePct(Double coinsurResponsePct) {
		this.coinsurResponsePct = coinsurResponsePct;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankAccName() {
		return bankAccName;
	}
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
	@Override
	public String toString() {
		return "ComCompany [companyName=" + companyName + ", companyCode=" + companyCode + ", coinsurType="
				+ coinsurType + ", companyFlag=" + companyFlag + ", coinsurAmntPct=" + coinsurAmntPct
				+ ", coinsurResponsePct=" + coinsurResponsePct + ", bankCode=" + bankCode + ", bankAccName="
				+ bankAccName + ", bankAccNo=" + bankAccNo + "]";
	}

}
