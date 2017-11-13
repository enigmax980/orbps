package com.newcore.orbps.models.web.vo.contractentry.combinedpolicy;

import java.io.Serializable;
import java.util.Date;
/**
 * 缴费信息  
 * @author wangyanjie
 *
 */
public class ComBinedPayVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000007L;
	/** 保险费合计 */
	private Double sumPrem;
	/** 交费方式 */
	private String moneyinType;
	/** 首期交费形式 */
	private String ernstMoneyinType;
	/** 实收日期 */
	private Date receiptDate;
	/** 连续投保标识 */	
	private String continuousInsurance;
	/** 币种 */
	private String currencyCode;
	/** 续期交费形式 */
	private String renewalFeeForm;
	/** 银行代码*/
	private String payBankCode;
	/** 缴费账号 */
	private String bankaccNo;
	/** 账户形式 */
	private String accountForm;
	/** 账户持有人 */
	private String accountHolder;
	/** 是投保人的 */
	private String ipsnRelationCode;
	/** 身份证号 */
	private String ipsnIdNo;
	/** 签单日期 */
	private Date signDate;
	/** 指定生效日 */
	private Date inforceDate;
	/** 保单性质*/
	private String policyNature;
	/** 送达类型 */
	private String serviceType;
	/** 同业公司人身保险保额合计 */
	private Double appOfficeTel;
	/** 合同争议处理方式 */
	private String disputeResolution;
	/** 仲裁委员会名称*/
	private String arbitrationCommissionName;
	
	/**
	 * @return the moneyinType
	 */
	public String getMoneyinType() {
		return moneyinType;
	}
	/**
	 * @param moneyinType the moneyinType to set
	 */
	public void setMoneyinType(String moneyinType) {
		this.moneyinType = moneyinType;
	}
	/**
	 * @return the ernstMoneyinType
	 */
	public String getErnstMoneyinType() {
		return ernstMoneyinType;
	}
	/**
	 * @param ernstMoneyinType the ernstMoneyinType to set
	 */
	public void setErnstMoneyinType(String ernstMoneyinType) {
		this.ernstMoneyinType = ernstMoneyinType;
	}
	
	/**
	 * @return the continuousInsurance
	 */
	public String getContinuousInsurance() {
		return continuousInsurance;
	}
	/**
	 * @param continuousInsurance the continuousInsurance to set
	 */
	public void setContinuousInsurance(String continuousInsurance) {
		this.continuousInsurance = continuousInsurance;
	}
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the renewalFeeForm
	 */
	public String getRenewalFeeForm() {
		return renewalFeeForm;
	}
	/**
	 * @param renewalFeeForm the renewalFeeForm to set
	 */
	public void setRenewalFeeForm(String renewalFeeForm) {
		this.renewalFeeForm = renewalFeeForm;
	}
	/**
	 * @return the payBankCode
	 */
	public String getPayBankCode() {
		return payBankCode;
	}
	/**
	 * @param payBankCode the payBankCode to set
	 */
	public void setPayBankCode(String payBankCode) {
		this.payBankCode = payBankCode;
	}

	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the inforceDate
	 */
	public Date getInforceDate() {
		return inforceDate;
	}
	/**
	 * @param inforceDate the inforceDate to set
	 */
	public void setInforceDate(Date inforceDate) {
		this.inforceDate = inforceDate;
	}

	/**
	 * @return the sumPrem
	 */
	public Double getSumPrem() {
		return sumPrem;
	}
	/**
	 * @param sumPrem the sumPrem to set
	 */
	public void setSumPrem(Double sumPrem) {
		this.sumPrem = sumPrem;
	}
	/**
	 * @return the receiptDate
	 */
	public Date getReceiptDate() {
		return receiptDate;
	}
	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	/**
	 * @return the bankaccNo
	 */
	public String getBankaccNo() {
		return bankaccNo;
	}
	/**
	 * @param bankaccNo the bankaccNo to set
	 */
	public void setBankaccNo(String bankaccNo) {
		this.bankaccNo = bankaccNo;
	}
	/**
	 * @return the accountForm
	 */
	public String getAccountForm() {
		return accountForm;
	}
	/**
	 * @param accountForm the accountForm to set
	 */
	public void setAccountForm(String accountForm) {
		this.accountForm = accountForm;
	}
	/**
	 * @return the accountHolder
	 */
	public String getAccountHolder() {
		return accountHolder;
	}
	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	/**
	 * @return the ipsnRelationCode
	 */
	public String getIpsnRelationCode() {
		return ipsnRelationCode;
	}
	/**
	 * @param ipsnRelationCode the ipsnRelationCode to set
	 */
	public void setIpsnRelationCode(String ipsnRelationCode) {
		this.ipsnRelationCode = ipsnRelationCode;
	}
	/**
	 * @return the ipsnIdNo
	 */
	public String getIpsnIdNo() {
		return ipsnIdNo;
	}
	/**
	 * @param ipsnIdNo the ipsnIdNo to set
	 */
	public void setIpsnIdNo(String ipsnIdNo) {
		this.ipsnIdNo = ipsnIdNo;
	}
	/**
	 * @return the policyNature
	 */
	public String getPolicyNature() {
		return policyNature;
	}
	/**
	 * @param policyNature the policyNature to set
	 */
	public void setPolicyNature(String policyNature) {
		this.policyNature = policyNature;
	}
	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * @return the appOfficeTel
	 */
	public Double getAppOfficeTel() {
		return appOfficeTel;
	}
	/**
	 * @param appOfficeTel the appOfficeTel to set
	 */
	public void setAppOfficeTel(Double appOfficeTel) {
		this.appOfficeTel = appOfficeTel;
	}
	/**
	 * @return the disputeResolution
	 */
	public String getDisputeResolution() {
		return disputeResolution;
	}
	/**
	 * @param disputeResolution the disputeResolution to set
	 */
	public void setDisputeResolution(String disputeResolution) {
		this.disputeResolution = disputeResolution;
	}
	/**
	 * @return the arbitrationCommissionName
	 */
	public String getArbitrationCommissionName() {
		return arbitrationCommissionName;
	}
	/**
	 * @param arbitrationCommissionName the arbitrationCommissionName to set
	 */
	public void setArbitrationCommissionName(String arbitrationCommissionName) {
		this.arbitrationCommissionName = arbitrationCommissionName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PayInfoVo [sumPrem=" + sumPrem + ", moneyinType=" + moneyinType + ", ernstMoneyinType="
				+ ernstMoneyinType + ", receiptDate=" + receiptDate + ", continuousInsurance=" + continuousInsurance
				+ ", currencyCode=" + currencyCode + ", renewalFeeForm=" + renewalFeeForm + ", payBankCode="
				+ payBankCode + ", bankaccNo=" + bankaccNo + ", accountForm=" + accountForm + ", accountHolder="
				+ accountHolder + ", ipsnRelationCode=" + ipsnRelationCode + ", ipsnIdNo=" + ipsnIdNo + ", signDate="
				+ signDate + ", inforceDate=" + inforceDate + ", policyNature=" + policyNature + ", serviceType="
				+ serviceType + ", appOfficeTel=" + appOfficeTel + ", disputeResolution=" + disputeResolution
				+ ", arbitrationCommissionName=" + arbitrationCommissionName + "]";
	}
	

}
