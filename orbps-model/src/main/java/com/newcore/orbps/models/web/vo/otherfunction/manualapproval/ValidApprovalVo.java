package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

public class ValidApprovalVo implements Serializable{
	
private static final long serialVersionUID = 14344544565L;
	
	/** 投保单号 */
	private String applNo;
	/** 契约形式 */
	private String contractType;
	/** 投保日期 */
	private String applDate;
	/** 生效日期 */
	private String effectDate;
	/** 保单总保额 */
	private String cntrSumAmount;
	/** 保单总保费 */
	private String cntrSumPremium;
	/** 最后交费日期 */
	private String lastChargeDate;
	/** 总交费金额 */
	private String sumPayAmount;
	/** 审批结果 */
	private String approvalDecision;
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
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}
	/**
	 * @param contractType the contractType to set
	 */
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	/**
	 * @return the applDate
	 */
	public String getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(String applDate) {
		this.applDate = applDate;
	}
	/**
	 * @return the effectDate
	 */
	public String getEffectDate() {
		return effectDate;
	}
	/**
	 * @param effectDate the effectDate to set
	 */
	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}
	/**
	 * @return the cntrSumAmount
	 */
	public String getCntrSumAmount() {
		return cntrSumAmount;
	}
	/**
	 * @param cntrSumAmount the cntrSumAmount to set
	 */
	public void setCntrSumAmount(String cntrSumAmount) {
		this.cntrSumAmount = cntrSumAmount;
	}
	/**
	 * @return the cntrSumPremium
	 */
	public String getCntrSumPremium() {
		return cntrSumPremium;
	}
	/**
	 * @param cntrSumPremium the cntrSumPremium to set
	 */
	public void setCntrSumPremium(String cntrSumPremium) {
		this.cntrSumPremium = cntrSumPremium;
	}
	/**
	 * @return the lastChargeDate
	 */
	public String getLastChargeDate() {
		return lastChargeDate;
	}
	/**
	 * @param lastChargeDate the lastChargeDate to set
	 */
	public void setLastChargeDate(String lastChargeDate) {
		this.lastChargeDate = lastChargeDate;
	}
	/**
	 * @return the sumPayAmount
	 */
	public String getSumPayAmount() {
		return sumPayAmount;
	}
	/**
	 * @param sumPayAmount the sumPayAmount to set
	 */
	public void setSumPayAmount(String sumPayAmount) {
		this.sumPayAmount = sumPayAmount;
	}
	/**
	 * @return the approvalDecision
	 */
	public String getApprovalDecision() {
		return approvalDecision;
	}
	/**
	 * @param approvalDecision the approvalDecision to set
	 */
	public void setApprovalDecision(String approvalDecision) {
		this.approvalDecision = approvalDecision;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidApprovalVo [applNo=" + applNo + ", contractType=" + contractType + ", applDate=" + applDate
				+ ", effectDate=" + effectDate + ", cntrSumAmount=" + cntrSumAmount + ", cntrSumPremium="
				+ cntrSumPremium + ", lastChargeDate=" + lastChargeDate + ", sumPayAmount=" + sumPayAmount
				+ ", approvalDecision=" + approvalDecision + "]";
	}

	
}
