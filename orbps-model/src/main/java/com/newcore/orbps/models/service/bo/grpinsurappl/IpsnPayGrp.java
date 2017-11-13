package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 团体收费组
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:54:34
 */
public class IpsnPayGrp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8066420211755889058L;

	/* 字段名：收费组属组编号，是否必填：有分组必填，团单特有 */
	private Long feeGrpNo;

	/* 字段名：收费组属组名称，长度：32，是否必填：有分组必填，团单特有 */
	private String feeGrpName;
	
	/* 字段名：收费属组人数，，是否必填：有分组必填，团单特有 */
	private Long ipsnGrpNum;

	/* 字段名：缴费形式，长度：2，是否必填：有分组必填，团单特有 */
	private String moneyinType;

	/* 字段名：开户银行，长度：4，是否必填：有分组且缴费方式为银行转账必填，团单特有 */
	private String bankCode;

	/* 字段名：开户名称，长度：48，是否必填：有分组且缴费方式为银行转账必填，团单特有 */
	private String bankaccName;

	/* 字段名：银行账号，长度：25，是否必填：有分组且缴费方式为银行转账必填，团单特有 */
	private String bankaccNo;

	/* 字段名：支票号，长度：25，是否必填：有分组且缴费方式为支票必填，团单特有 */
	private String bankChequeNo;

	/**
	 * 
	 */
	public IpsnPayGrp() {
		super();
	}

	public Long getFeeGrpNo() {
		return feeGrpNo;
	}


	public void setFeeGrpNo(Long feeGrpNo) {
		this.feeGrpNo = feeGrpNo;
	}


	public String getFeeGrpName() {
		return feeGrpName;
	}


	public void setFeeGrpName(String feeGrpName) {
		this.feeGrpName = feeGrpName;
	}


	public Long getIpsnGrpNum() {
		return ipsnGrpNum;
	}


	public void setIpsnGrpNum(Long ipsnGrpNum) {
		this.ipsnGrpNum = ipsnGrpNum;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the bankaccName
	 */
	public String getBankaccName() {
		return bankaccName;
	}

	/**
	 * @param bankaccName the bankaccName to set
	 */
	public void setBankaccName(String bankaccName) {
		this.bankaccName = bankaccName;
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

	/**
	 * @return the bankChequeNo
	 */
	public String getBankChequeNo() {
		return bankChequeNo;
	}

	/**
	 * @param bankChequeNo the bankChequeNo to set
	 */
	public void setBankChequeNo(String bankChequeNo) {
		this.bankChequeNo = bankChequeNo;
	}



	public String getMoneyinType() {
		return moneyinType;
	}



	public void setMoneyinType(String moneyinType) {
		this.moneyinType = moneyinType;
	}	

}
