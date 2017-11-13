package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;

/**
 * 投保要约
 * 
 * @author lijifei 
 * 创建时间：2016年10月11日上午14:20:44
 */
public class ApplState implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7693521337947403874L;

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
	//理赔录入类型
	private String clDcType;
	// 险种
	private List<Policy> policyList;
	public Long getIpsnNum() {
		return ipsnNum;
	}
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}
	public Double getSumFaceAmnt() {
		return sumFaceAmnt;
	}
	public void setSumFaceAmnt(Double sumFaceAmnt) {
		this.sumFaceAmnt = sumFaceAmnt;
	}
	public Double getSumPremium() {
		return sumPremium;
	}
	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}
	public Integer getInsurDur() {
		return insurDur;
	}
	public void setInsurDur(Integer insurDur) {
		this.insurDur = insurDur;
	}
	public String getInsurDurUnit() {
		return insurDurUnit;
	}
	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}
	public String getInforceDateType() {
		return inforceDateType;
	}
	public void setInforceDateType(String inforceDateType) {
		this.inforceDateType = inforceDateType;
	}
	public Date getDesignForceDate() {
		return designForceDate;
	}
	public void setDesignForceDate(Date designForceDate) {
		this.designForceDate = designForceDate;
	}
	public String getIsPrePrint() {
		return isPrePrint;
	}
	public void setIsPrePrint(String isPrePrint) {
		this.isPrePrint = isPrePrint;
	}
	public String getIsFreForce() {
		return isFreForce;
	}
	public void setIsFreForce(String isFreForce) {
		this.isFreForce = isFreForce;
	}
	public Integer getForceFre() {
		return forceFre;
	}
	public void setForceFre(Integer forceFre) {
		this.forceFre = forceFre;
	}
	public String getClDcType() {
		return clDcType;
	}
	public void setClDcType(String clDcType) {
		this.clDcType = clDcType;
	}
	public List<Policy> getPolicyList() {
		return policyList;
	}
	public void setPolicyList(List<Policy> policyList) {
		this.policyList = policyList;
	}
	public ApplState() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}