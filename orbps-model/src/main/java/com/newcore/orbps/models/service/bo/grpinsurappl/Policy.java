package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.List;

/**
 * 险种
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:21:09
 */
public class Policy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8257245341511149531L;

	/* 字段名：险种，长度：8，是否必填：是 */
	private String polCode;

	/* 字段名：险种名称，长度：30，是否必填：是 */
	private String polNameChn;

	/* 字段名：保险期间，是否必填：是 */
	private Integer insurDur;

	/* 字段名：保险期类型，长度：2，是否必填：否 */
	private String insurDurUnit;

	/* 字段名：险种保额，是否必填：是 */
	private Double faceAmnt;

	/* 字段名：实际保费，是否必填：是 */
	private Double premium;

	/* 字段名：标准保费，是否必填：是 */
	private Double stdPremium;

	/* 字段名：险种投保人数，是否必填：是 */
	private Long polIpsnNum;

	/*
	 * 字段名：分保单号，长度：25，是否必填：是
	 */
	private String cntrNo;

	/*
	 * 字段名：主附险性质，长度：2，是否必填：是
	 * M 主险、R 附险(被保人)、E 附险(投保人)
	 */
	private String mrCode;
	
	/*
	 * 字段名：缴费间隔月，是否必填：是
	 */
	private Integer moneyinItrvlMon;

	/*
	 * 字段名：专项业务标识，长度：2，是否必填：否
	 */
	private String speciBusinessLogo;

	/*
	 * 字段名：保费折扣，是否必填：否
	 */
	private Double premDiscount;
	
	//核保结论
	private TuPolResult tuPolResult;

	// 子险种 存在子险种必填
	private List<SubPolicy> subPolicyList;

	/**
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}

	/**
	 * @param polCode the polCode to set
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
	 * @return the insurDur
	 */
	public Integer getInsurDur() {
		return insurDur;
	}

	/**
	 * @param insurDur the insurDur to set
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
	 * @param insurDurUnit the insurDurUnit to set
	 */
	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}

	/**
	 * @return the faceAmnt
	 */
	public Double getFaceAmnt() {
		return faceAmnt;
	}

	/**
	 * @param faceAmnt the faceAmnt to set
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
	 * @param premium the premium to set
	 */
	public void setPremium(Double premium) {
		this.premium = premium;
	}

	/**
	 * @return the stdPremium
	 */
	public Double getStdPremium() {
		return stdPremium;
	}

	/**
	 * @param stdPremium the stdPremium to set
	 */
	public void setStdPremium(Double stdPremium) {
		this.stdPremium = stdPremium;
	}

	/**
	 * @return the polIpsnNum
	 */
	public Long getPolIpsnNum() {
		return polIpsnNum;
	}

	/**
	 * @param polIpsnNum the polIpsnNum to set
	 */
	public void setPolIpsnNum(Long polIpsnNum) {
		this.polIpsnNum = polIpsnNum;
	}

	/**
	 * @return the cntrNo
	 */
	public String getCntrNo() {
		return cntrNo;
	}

	/**
	 * @param cntrNo the cntrNo to set
	 */
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	/**
	 * @return the mrCode
	 */
	public String getMrCode() {
		return mrCode;
	}

	/**
	 * @param mrCode the mrCode to set
	 */
	public void setMrCode(String mrCode) {
		this.mrCode = mrCode;
	}

	/**
	 * @return the moneyinItrvlMon
	 */
	public Integer getMoneyinItrvlMon() {
		return moneyinItrvlMon;
	}

	/**
	 * @param moneyinItrvlMon the moneyinItrvlMon to set
	 */
	public void setMoneyinItrvlMon(Integer moneyinItrvlMon) {
		this.moneyinItrvlMon = moneyinItrvlMon;
	}

	/**
	 * @return the speciBusinessLogo
	 */
	public String getSpeciBusinessLogo() {
		return speciBusinessLogo;
	}

	/**
	 * @param speciBusinessLogo the speciBusinessLogo to set
	 */
	public void setSpeciBusinessLogo(String speciBusinessLogo) {
		this.speciBusinessLogo = speciBusinessLogo;
	}

	/**
	 * @return the premDiscount
	 */
	public Double getPremDiscount() {
		return premDiscount;
	}

	/**
	 * @param premDiscount the premDiscount to set
	 */
	public void setPremDiscount(Double premDiscount) {
		this.premDiscount = premDiscount;
	}

	/**
	 * @return the tuPolResult
	 */
	public TuPolResult getTuPolResult() {
		return tuPolResult;
	}

	/**
	 * @param tuPolResult the tuPolResult to set
	 */
	public void setTuPolResult(TuPolResult tuPolResult) {
		this.tuPolResult = tuPolResult;
	}

	/**
	 * @return the subPolicyList
	 */
	public List<SubPolicy> getSubPolicyList() {
		return subPolicyList;
	}

	/**
	 * @param subPolicyList the subPolicyList to set
	 */
	public void setSubPolicyList(List<SubPolicy> subPolicyList) {
		this.subPolicyList = subPolicyList;
	}

}
