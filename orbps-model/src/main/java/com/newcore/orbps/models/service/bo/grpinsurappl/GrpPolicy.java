package com.newcore.orbps.models.service.bo.grpinsurappl;
import java.io.Serializable;

/**
 * 险种
 * @author wangxiao
 * 创建时间：2016年9月1日下午3:03:08
 */
public class GrpPolicy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2482694296308592183L;

	/* 字段名：险种，长度：8，是否必填：是 */
	private String polCode;

	/* 字段名：险种，长度：2，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private String mrCode;
	
	/* 字段名：险种保额，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private Double faceAmnt;

	/* 字段名：实际保费，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private Double premium;
	
	/* 字段名：标准保费，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private Double stdPremium;
	
	/* 字段名：承保费率，是否必填：否 */
	private Double premRate;
	
	/* 字段名：费率浮动幅度，是否必填：否 */
	private Double premDiscount;

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
	 * @return the premRate
	 */
	public Double getPremRate() {
		return premRate;
	}

	/**
	 * @param premRate the premRate to set
	 */
	public void setPremRate(Double premRate) {
		this.premRate = premRate;
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

}
