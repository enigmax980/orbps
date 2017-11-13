package com.newcore.orbps.models.web.vo.sendgrpinsurappl;

import java.io.Serializable;
/**
 * 团体收费组
 * @author LJF 
 * 创建时间：2017年5月5日下午5:17:53
 */
public class IpsnPayGrpVo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5330253770883460337L;

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
	private String bankAccName;

	/* 字段名：银行账号，长度：25，是否必填：有分组且缴费方式为银行转账必填，团单特有 */
	private String bankAccNo;

	/* 字段名：支票号，长度：25，是否必填：有分组且缴费方式为支票必填，团单特有 */
	private String bankChequeNo;








	public IpsnPayGrpVo() {
		super();
		// TODO Auto-generated constructor stub
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
