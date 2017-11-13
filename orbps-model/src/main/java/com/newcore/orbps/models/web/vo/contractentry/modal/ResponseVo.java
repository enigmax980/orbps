package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;

/**
 * 责任信息
 * @author xiaoye
 *
 */
public class ResponseVo  implements Serializable{
	
	private static final long serialVersionUID = 1145435443L;

	/** 投保单ID */
	private String applId;
	
	/** 责任代码 */
	private String productCode;
	
	/** 险种代码 */
	private String busiPrdCode;
	
	/** 责任号 */
	private String productNo;
	
	/** 责任名称 */
	private String productName;
	
	/** 保额*/
	private Double productNum;

	/** 保费*/
	private Double productPremium;
	
	/** 险种标准保费*/
	private  Double subStdPremium;

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
	 * @return the busiPrdCode
	 */
	public String getBusiPrdCode() {
		return busiPrdCode;
	}

	/**
	 * @param busiPrdCode the busiPrdCode to set
	 */
	public void setBusiPrdCode(String busiPrdCode) {
		this.busiPrdCode = busiPrdCode;
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
	public Double getProductNum() {
		return productNum;
	}

	/**
	 * @param productNum the productNum to set
	 */
	public void setProductNum(Double productNum) {
		this.productNum = productNum;
	}

	/**
	 * @return the productPremium
	 */
	public Double getProductPremium() {
		return productPremium;
	}

	/**
	 * @param productPremium the productPremium to set
	 */
	public void setProductPremium(Double productPremium) {
		this.productPremium = productPremium;
	}

	/**
	 * @return the subStdPremium
	 */
	public Double getSubStdPremium() {
		return subStdPremium;
	}

	/**
	 * @param subStdPremium the subStdPremium to set
	 */
	public void setSubStdPremium(Double subStdPremium) {
		this.subStdPremium = subStdPremium;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseVo [applId=" + applId + ", productCode=" + productCode + ", busiPrdCode=" + busiPrdCode
				+ ", productNo=" + productNo + ", productName=" + productName + ", productNum=" + productNum
				+ ", productPremium=" + productPremium + ", subStdPremium=" + subStdPremium + "]";
	}






	
}
