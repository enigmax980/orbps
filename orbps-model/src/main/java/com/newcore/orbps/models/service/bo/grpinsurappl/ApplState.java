package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 投保要约
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:20:44
 */
public class ApplState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5604116825827443667L;

	/* 字段名：投保人数，是否必填：是 */
	private Long ipsnNum;

	/* 字段名：总保额，是否必填：是 */
	private Double sumFaceAmnt;

	/* 字段名：总保费，是否必填：是 */
	private Double sumPremium;

	/* 字段名：保险期间，是否必填：IF 团单 THEN 必填 */
	private Integer insurDur;

	/* 字段名：保险期类型，长度：2，是否必填：IF 团单 THEN 必填 */
	private String insurDurUnit;

	/*
	 * 字段名：生效日类型，长度：2，是否必填：是 1.指定生效日；0.不指定生效日
	 */
	private String inforceDateType;

	/* 字段名：指定生效日，是否可为空：IF inforceDateType == 1 THEN 必填 */
	private Date designForceDate;

	/*
	 * 字段名：是否预打印，长度：1，是否必填：否 0：否；1：是（默认为0）
	 */
	private String isPrePrint;

	/*
	 * 字段名：是否频次生效，长度：1，是否必填：否 0：否；1：是（默认为0）
	 */
	private String isFreForce;

	/* 字段名：生效频率，是否必填：IF isPreForce == 1 THEN 必填 */
	private Integer forceFre;

	// 险种
	private List<Policy> policyList;

	/**
	 * 
	 */
	public ApplState() {
		super();
	}

	/**
	 * @return the ipsnNum
	 */
	public Long getIpsnNum() {
		return ipsnNum;
	}

	/**
	 * @param ipsnNum
	 *            the ipsnNum to set
	 */
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}

	/**
	 * @return the sumFaceAmnt
	 */
	public Double getSumFaceAmnt() {
		return sumFaceAmnt;
	}

	/**
	 * @param sumFaceAmnt
	 *            the sumFaceAmnt to set
	 */
	public void setSumFaceAmnt(Double sumFaceAmnt) {
		this.sumFaceAmnt = sumFaceAmnt;
	}

	/**
	 * @return the sumPremium
	 */
	public Double getSumPremium() {
		return sumPremium;
	}

	/**
	 * @param sumPremium
	 *            the sumPremium to set
	 */
	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}

	/**
	 * @return the insurDur
	 */
	public Integer getInsurDur() {
		return insurDur;
	}

	/**
	 * @param insurDur
	 *            the insurDur to set
	 */
	public void setInsurDur(Integer insurDur) {
		this.insurDur = insurDur;
	}

	/**
	 * @return the insurDurUnit
	 */
	public String getInsurDurUnit() {
		return insurDurUnit;
	}

	/**
	 * @param insurDurUnit
	 *            the insurDurUnit to set
	 */
	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}

	/**
	 * @return the inforceDateType
	 */
	public String getInforceDateType() {
		return inforceDateType;
	}

	/**
	 * @param inforceDateType
	 *            the inforceDateType to set
	 */
	public void setInforceDateType(String inforceDateType) {
		this.inforceDateType = inforceDateType;
	}


	public Date getDesignForceDate() {
		return designForceDate;
	}

	public void setDesignForceDate(Date designForceDate) {
		this.designForceDate = designForceDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the isPrePrint
	 */
	public String getIsPrePrint() {
		return isPrePrint;
	}

	/**
	 * @param isPrePrint
	 *            the isPrePrint to set
	 */
	public void setIsPrePrint(String isPrePrint) {
		this.isPrePrint = isPrePrint;
	}

	/**
	 * @return the isFreForce
	 */
	public String getIsFreForce() {
		return isFreForce;
	}

	/**
	 * @param isFreForce
	 *            the isFreForce to set
	 */
	public void setIsFreForce(String isFreForce) {
		this.isFreForce = isFreForce;
	}

	/**
	 * @return the forceFre
	 */
	public Integer getForceFre() {
		return forceFre;
	}

	/**
	 * @param forceFre
	 *            the forceFre to set
	 */
	public void setForceFre(Integer forceFre) {
		this.forceFre = forceFre;
	}

	/**
	 * @return the policyList
	 */
	public List<Policy> getPolicyList() {
		return policyList;
	}

	/**
	 * @param policyList
	 *            the policyList to set
	 */
	public void setPolicyList(List<Policy> policyList) {
		this.policyList = policyList;
	}

}
