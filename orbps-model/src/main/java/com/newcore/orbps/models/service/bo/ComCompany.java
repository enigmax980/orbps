package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

/**
 * @author huanglong
 * @date 2016年10月24日
 * 内容:共保公司基本信息
 */
public class ComCompany implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2117098759179656901L;
	/*
	 * 公司代码
	 */
	private String companyCode;
	/*
	 * 公司名称
	 */
	private String companyName;
	/*
	 * 共保类型  M 主承保方    N 参与承保方
	 */
	private String coinsurType;
	/*
	 * 是否本公司
	 */
	private String companyFlag;
	/*
	 * 共保保费份额比例
	 */
	private double coinsurAmntPct;
	/*
	 * 共保责任份额比例
	 */
	private double coinsurResponsePct;
	/*
	 * 开户行
	 */
	private String bankCode;
	/*
	 * 开户行名称
	 */
	private String bankName;
	/*
	 * 银行帐号
	 */
	private String bankAccNo;
	/*
	 * 户名
	 */
	private String bankAccName;
	public ComCompany() {
		super();
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName 要设置的 companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return coinsurType
	 */
	public String getCoinsurType() {
		return coinsurType;
	}

	/**
	 * @param coinsurType 要设置的 coinsurType
	 */
	public void setCoinsurType(String coinsurType) {
		this.coinsurType = coinsurType;
	}

	/**
	 * @return companyFlag
	 */
	public String getCompanyFlag() {
		return companyFlag;
	}

	/**
	 * @param companyFlag 要设置的 companyFlag
	 */
	public void setCompanyFlag(String companyFlag) {
		this.companyFlag = companyFlag;
	}

	/**
	 * @return coinsurAmntPct
	 */
	public double getCoinsurAmntPct() {
		return coinsurAmntPct;
	}

	/**
	 * @param coinsurAmntPct 要设置的 coinsurAmntPct
	 */
	public void setCoinsurAmntPct(double coinsurAmntPct) {
		this.coinsurAmntPct = coinsurAmntPct;
	}

	/**
	 * @return coinsurResponsePct
	 */
	public double getCoinsurResponsePct() {
		return coinsurResponsePct;
	}

	/**
	 * @param coinsurResponsePct 要设置的 coinsurResponsePct
	 */
	public void setCoinsurResponsePct(double coinsurResponsePct) {
		this.coinsurResponsePct = coinsurResponsePct;
	}

	/**
	 * @return bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode 要设置的 bankCode
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName 要设置的 bankName
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return bankAccNo
	 */
	public String getBankAccNo() {
		return bankAccNo;
	}

	/**
	 * @param bankAccNo 要设置的 bankAccNo
	 */
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	/**
	 * @return bankAccName
	 */
	public String getBankAccName() {
		return bankAccName;
	}

	/**
	 * @param bankAccName 要设置的 bankAccName
	 */
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}

}
