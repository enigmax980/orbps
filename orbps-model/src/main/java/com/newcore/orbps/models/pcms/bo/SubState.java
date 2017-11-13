package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
	@NotNull(message="[险种代码不能为空]")
	@Length(max=1,message="投保人证件类别长度不能大于1位]")
	private String polCode;

	/* 字段名：险种保额，是否必填：是 */
	@NotNull(message="[险种保额不能为空]")
	private Double faceAmnt;

	/* 字段名：险种保费，是否必填：是 */
	@NotNull(message="[险种保费不能为空]")
	private Double premium;

	/* 字段名：理赔分组号，是否必填：否 */
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
