package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

public class InsurApplBo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1142312343L;

	/** 投保单ID */
	private String applId;
	
	/** 责任代码 */
	private String productCode;
	
	/** 责任号 */
	private String productNo;
	
	/** 责任名称 */
	private String productName;
	
	/** 保额*/
	private String productNum;

	/** 保费*/
	private String productPremium;

	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}

	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the productNo
	 */
	public String getProductNo() {
		return productNo;
	}

	/**
	 * @param productNo the productNo to set
	 */
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the productNum
	 */
	public String getProductNum() {
		return productNum;
	}

	/**
	 * @param productNum the productNum to set
	 */
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	/**
	 * @return the productPremium
	 */
	public String getProductPremium() {
		return productPremium;
	}

	/**
	 * @param productPremium the productPremium to set
	 */
	public void setProductPremium(String productPremium) {
		this.productPremium = productPremium;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InsurApplVo [applId=" + applId + ", productCode=" + productCode + ", productNo=" + productNo
				+ ", productName=" + productName + ", productNum=" + productNum + ", productPremium=" + productPremium
				+ "]";
	}
	
	
}
