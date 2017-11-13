package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;

/**
 * 缴费信息Vo
 * @author jincong
 *
 */
public class LoanAddChargeInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030008L;
	
	/** 保费合计 */
	private Double totalPremium;
	/** 交费方式 */
	private String moneyinItrvl;
	/** 特别约定 */
	private String speAgreement;
	
	/**
	 * @return the totalPremium
	 */
	public Double getTotalPremium() {
		return totalPremium;
	}
	/**
	 * @param totalPremium the totalPremium to set
	 */
	public void setTotalPremium(Double totalPremium) {
		this.totalPremium = totalPremium;
	}
	/**
	 * @return the moneyinItrvl
	 */
	public String getMoneyinItrvl() {
		return moneyinItrvl;
	}
	/**
	 * @param moneyinItrvl the moneyinItrvl to set
	 */
	public void setMoneyinItrvl(String moneyinItrvl) {
		this.moneyinItrvl = moneyinItrvl;
	}
	/**
	 * @return the speAgreement
	 */
	public String getSpeAgreement() {
		return speAgreement;
	}
	/**
	 * @param speAgreement the speAgreement to set
	 */
	public void setSpeAgreement(String speAgreement) {
		this.speAgreement = speAgreement;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChargeInfoVo [totalPremium=" + totalPremium + ", moneyinItrvl=" + moneyinItrvl + ", speAgreement="
				+ speAgreement + "]";
	}
}
