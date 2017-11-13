package com.newcore.orbps.models.web.vo.contractentry.frequencyeffect;

import java.io.Serializable;
import java.util.Date;

/**
 * 频次生效信息Vo
 * @author jincong
 *
 */
public class FrequencyEfEntryVo  implements Serializable{
	
	private static final long serialVersionUID = 1146040001L;
	/** 投保单号 */
	private String applNo;
	/** 本期生效日 */
	private Date inForceDate;
	/** 本期人数 */
	private Long tNumbers;
	/** 本期保费 */
	private Double tPremium;
	/** 险种名称 */
	private String polName;
	/** 承保人数 */
	private Long insuredNum;
	/** 保额 */
	private Double amount;
	/** 保费 */
	private Double premium;
	/** 健康险标识 */
	private String healthId;
	
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the inForceDate
	 */
	public Date getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}
	/**
	 * @return the tNumbers
	 */
	public Long gettNumbers() {
		return tNumbers;
	}
	/**
	 * @param tNumbers the tNumbers to set
	 */
	public void settNumbers(Long tNumbers) {
		this.tNumbers = tNumbers;
	}
	/**
	 * @return the tPremium
	 */
	public Double gettPremium() {
		return tPremium;
	}
	/**
	 * @param tPremium the tPremium to set
	 */
	public void settPremium(Double tPremium) {
		this.tPremium = tPremium;
	}
	/**
	 * @return the polName
	 */
	public String getPolName() {
		return polName;
	}
	/**
	 * @param polName the polName to set
	 */
	public void setPolName(String polName) {
		this.polName = polName;
	}
	/**
	 * @return the insuredNum
	 */
	public Long getInsuredNum() {
		return insuredNum;
	}
	/**
	 * @param insuredNum the insuredNum to set
	 */
	public void setInsuredNum(Long insuredNum) {
		this.insuredNum = insuredNum;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
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
	 * @return the healthId
	 */
	public String getHealthId() {
		return healthId;
	}
	/**
	 * @param healthId the healthId to set
	 */
	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FrequencyEffectVo [applNo=" + applNo + ", inForceDate=" + inForceDate + ", tNumbers=" + tNumbers
				+ ", tPremium=" + tPremium + ", polName=" + polName + ", insuredNum=" + insuredNum + ", amount="
				+ amount + ", premium=" + premium + ", healthId=" + healthId + "]";
	}
	
}
