package com.newcore.orbps.models.service.bo.grpinsured;

import java.io.Serializable;

/**
 * 子要约
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日下午2:31:55
 */
public class SubState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6679734584444079521L;

	/* 字段名：险种代码，长度：8，是否必填：是 */
	private String polCode;
	
	/* 字段名：险种名称，长度：30，是否必填：是*/
	private String polNameChn;

	/* 字段名：险种保额，是否必填：是 */
	private Double faceAmnt;

	/* 字段名：险种保费，是否必填：是 */
	private Double premium;

	/* 字段名：险种保费，是否必填：是 */
	private Double sumPremium;

	/* 字段名：要约属组号，是否必填：否 */
	private Long claimIpsnGrpNo;

	/**
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}

	/**
	 * @param polCode
	 *            the polCode to set
	 */
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	/**
	 * @return the polNameChn
	 */
	public String getPolNameChn() {
		return polNameChn;
	}

	/**
	 * @param polNameChn the polNameChn to set
	 */
	public void setPolNameChn(String polNameChn) {
		this.polNameChn = polNameChn;
	}

	/**
	 * @return the faceAmnt
	 */
	public Double getFaceAmnt() {
		return faceAmnt;
	}

	/**
	 * @param faceAmnt
	 *            the faceAmnt to set
	 */
	public void setFaceAmnt(Double faceAmnt) {
		this.faceAmnt = faceAmnt;
	}

	/**
	 * @return the sumPremium
	 */
	public Double getSumPremium() {
		return sumPremium;
	}

	/**
	 * @param sumPremium the sumPremium to set
	 */
	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}

	/**
	 * @return the premium
	 */
	public Double getPremium() {
		return premium;
	}

	/**
	 * @param premium
	 *            the premium to set
	 */
	public void setPremium(Double premium) {
		this.premium = premium;
	}

	/**
	 * @return the claimIpsnGrpNo
	 */
	public Long getClaimIpsnGrpNo() {
		return claimIpsnGrpNo;
	}

	/**
	 * @param claimIpsnGrpNo
	 *            the claimIpsnGrpNo to set
	 */
	public void setClaimIpsnGrpNo(Long claimIpsnGrpNo) {
		this.claimIpsnGrpNo = claimIpsnGrpNo;
	}

}
