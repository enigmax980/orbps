package com.newcore.orbps.models.web.vo.contractmanage.accountconfig;

import java.io.Serializable;
import java.util.Date;

/**
 * 团单管理
 * @author xiaoye
 *
 */
public class AccountOwnerVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445431L;
	/** 业务类型 */
	private String serviceType;
	/** 投保人 */
	private String applHolders;
	/** 被保人 */
	private String insureders;
	/** 交费模式 */
	private String paymentMode;
	/** 投保人 */
	private String applHolder;
	/** 被保人 */
	private String insureder;
	/** 家庭联系人 */
	private String homeLinker;
	/** 汇交人 */
	private String exchanger;
	/** 所有情况 */
	private String allSituation;
	/** 启用日期 */
	private Date enableDate;
	/** 停用日期 */
	private Date stopDate;
	/** 规则状态  */
	private Date regularState;
	/** 规则变化情况 */
	private Date ruleChange;
	/** 规则变化说明 */
	private Date ruleChangeDes;
	/** 审核状态 */
	private Date auditStatus;
	/** 审核意见(原) */
	private Date checkAdviceOld;
	/** 审核意见(新) */
	private Date checkAdvice;
	/** 审核通过 */
	private Date checkPass;
	/** 审核不通过 */
	private Date checkNotPass;
	
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
	 * @return the applHolders
	 */
	public String getApplHolders() {
		return applHolders;
	}
	/**
	 * @param applHolders the applHolders to set
	 */
	public void setApplHolders(String applHolders) {
		this.applHolders = applHolders;
	}
	/**
	 * @return the insureders
	 */
	public String getInsureders() {
		return insureders;
	}
	/**
	 * @param insureders the insureders to set
	 */
	public void setInsureders(String insureders) {
		this.insureders = insureders;
	}
	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	/**
	 * @return the applHolder
	 */
	public String getApplHolder() {
		return applHolder;
	}
	/**
	 * @param applHolder the applHolder to set
	 */
	public void setApplHolder(String applHolder) {
		this.applHolder = applHolder;
	}
	/**
	 * @return the insureder
	 */
	public String getInsureder() {
		return insureder;
	}
	/**
	 * @param insureder the insureder to set
	 */
	public void setInsureder(String insureder) {
		this.insureder = insureder;
	}
	/**
	 * @return the homeLinker
	 */
	public String getHomeLinker() {
		return homeLinker;
	}
	/**
	 * @param homeLinker the homeLinker to set
	 */
	public void setHomeLinker(String homeLinker) {
		this.homeLinker = homeLinker;
	}
	/**
	 * @return the exchanger
	 */
	public String getExchanger() {
		return exchanger;
	}
	/**
	 * @param exchanger the exchanger to set
	 */
	public void setExchanger(String exchanger) {
		this.exchanger = exchanger;
	}
	/**
	 * @return the allSituation
	 */
	public String getAllSituation() {
		return allSituation;
	}
	/**
	 * @param allSituation the allSituation to set
	 */
	public void setAllSituation(String allSituation) {
		this.allSituation = allSituation;
	}
	/**
	 * @return the enableDate
	 */
	public Date getEnableDate() {
		return enableDate;
	}
	/**
	 * @param enableDate the enableDate to set
	 */
	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}
	/**
	 * @return the stopDate
	 */
	public Date getStopDate() {
		return stopDate;
	}
	/**
	 * @param stopDate the stopDate to set
	 */
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	/**
	 * @return the regularState
	 */
	public Date getRegularState() {
		return regularState;
	}
	/**
	 * @param regularState the regularState to set
	 */
	public void setRegularState(Date regularState) {
		this.regularState = regularState;
	}
	/**
	 * @return the ruleChange
	 */
	public Date getRuleChange() {
		return ruleChange;
	}
	/**
	 * @param ruleChange the ruleChange to set
	 */
	public void setRuleChange(Date ruleChange) {
		this.ruleChange = ruleChange;
	}
	/**
	 * @return the ruleChangeDes
	 */
	public Date getRuleChangeDes() {
		return ruleChangeDes;
	}
	/**
	 * @param ruleChangeDes the ruleChangeDes to set
	 */
	public void setRuleChangeDes(Date ruleChangeDes) {
		this.ruleChangeDes = ruleChangeDes;
	}
	/**
	 * @return the auditStatus
	 */
	public Date getAuditStatus() {
		return auditStatus;
	}
	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(Date auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * @return the checkAdviceOld
	 */
	public Date getCheckAdviceOld() {
		return checkAdviceOld;
	}
	/**
	 * @param checkAdviceOld the checkAdviceOld to set
	 */
	public void setCheckAdviceOld(Date checkAdviceOld) {
		this.checkAdviceOld = checkAdviceOld;
	}
	/**
	 * @return the checkAdvice
	 */
	public Date getCheckAdvice() {
		return checkAdvice;
	}
	/**
	 * @param checkAdvice the checkAdvice to set
	 */
	public void setCheckAdvice(Date checkAdvice) {
		this.checkAdvice = checkAdvice;
	}
	/**
	 * @return the checkPass
	 */
	public Date getCheckPass() {
		return checkPass;
	}
	/**
	 * @param checkPass the checkPass to set
	 */
	public void setCheckPass(Date checkPass) {
		this.checkPass = checkPass;
	}
	/**
	 * @return the checkNotPass
	 */
	public Date getCheckNotPass() {
		return checkNotPass;
	}
	/**
	 * @param checkNotPass the checkNotPass to set
	 */
	public void setCheckNotPass(Date checkNotPass) {
		this.checkNotPass = checkNotPass;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountOwnerVo [serviceType=" + serviceType + ", applHolders=" + applHolders + ", insureders="
				+ insureders + ", paymentMode=" + paymentMode + ", applHolder=" + applHolder + ", insureder="
				+ insureder + ", homeLinker=" + homeLinker + ", exchanger=" + exchanger + ", allSituation="
				+ allSituation + ", enableDate=" + enableDate + ", stopDate=" + stopDate + ", regularState="
				+ regularState + ", ruleChange=" + ruleChange + ", ruleChangeDes=" + ruleChangeDes + ", auditStatus="
				+ auditStatus + ", checkAdviceOld=" + checkAdviceOld + ", checkAdvice=" + checkAdvice + ", checkPass="
				+ checkPass + ", checkNotPass=" + checkNotPass + "]";
	}


	
}
