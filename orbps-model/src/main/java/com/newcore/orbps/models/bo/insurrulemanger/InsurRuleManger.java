package com.newcore.orbps.models.bo.insurrulemanger;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2016年12月17日上午8:59:15
 */
public class InsurRuleManger implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8809620554057346210L;
	/** 序号 */
	private int id;
	/** 管理机构号 */
	private String mgrBranchNo;
	/** 销售渠道 */
	private String salesChannel;
	//销售机构号
	private String salesBranchNo;
	/** 业务场景 */
	private String scene;
	/** 契约形式 */
	private String cntrForm;
	/** 产品类型 */
	private String productType;
	/** 是否自动审批 */
	private String autoApproveFlag;
	/** 人工自动审批 */
	private String artificialApproveFlag;
	/** 1、生效日往前追溯天数 */
	private Long beforeEffectiveDate;
	/** 2、生效日往后指定天数 */
	private Long afterEffectiveDate;
	/** 3、生效日往前追溯跨越日期 */
	private String effectiveDateBackAcross;
	/** 规则类型*/
	private String ruleType;
	/** 规则变化原因 */
	private String ruleChangeReason;
	/** 规则状态 */
	private String ruleState;
	/** 规则名称 */
	private String ruleName;
	/** 启用日期 */
	private Date startDate;
	/** 停止日期 */
	private Date endDate;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}
	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	/**
	 * @return the scene
	 */
	public String getScene() {
		return scene;
	}
	/**
	 * @param scene the scene to set
	 */
	public void setScene(String scene) {
		this.scene = scene;
	}
	/**
	 * @return the cntrForm
	 */
	public String getCntrForm() {
		return cntrForm;
	}
	/**
	 * @param cntrForm the cntrForm to set
	 */
	public void setCntrForm(String cntrForm) {
		this.cntrForm = cntrForm;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the autoApproveFlag
	 */
	public String getAutoApproveFlag() {
		return autoApproveFlag;
	}
	/**
	 * @param autoApproveFlag the autoApproveFlag to set
	 */
	public void setAutoApproveFlag(String autoApproveFlag) {
		this.autoApproveFlag = autoApproveFlag;
	}
	/**
	 * @return the artificialApproveFlag
	 */
	public String getArtificialApproveFlag() {
		return artificialApproveFlag;
	}
	/**
	 * @param artificialApproveFlag the artificialApproveFlag to set
	 */
	public void setArtificialApproveFlag(String artificialApproveFlag) {
		this.artificialApproveFlag = artificialApproveFlag;
	}
	/**
	 * @return the beforeEffectiveDate
	 */
	public Long getBeforeEffectiveDate() {
		return beforeEffectiveDate;
	}
	/**
	 * @param beforeEffectiveDate the beforeEffectiveDate to set
	 */
	public void setBeforeEffectiveDate(Long beforeEffectiveDate) {
		this.beforeEffectiveDate = beforeEffectiveDate;
	}
	/**
	 * @return the afterEffectiveDate
	 */
	public Long getAfterEffectiveDate() {
		return afterEffectiveDate;
	}
	/**
	 * @param afterEffectiveDate the afterEffectiveDate to set
	 */
	public void setAfterEffectiveDate(Long afterEffectiveDate) {
		this.afterEffectiveDate = afterEffectiveDate;
	}
	/**
	 * @return the effectiveDateBackAcross
	 */
	public String getEffectiveDateBackAcross() {
		return effectiveDateBackAcross;
	}
	/**
	 * @param effectiveDateBackAcross the effectiveDateBackAcross to set
	 */
	public void setEffectiveDateBackAcross(String effectiveDateBackAcross) {
		this.effectiveDateBackAcross = effectiveDateBackAcross;
	}
	/**
	 * @return the ruleType
	 */
	public String getRuleType() {
		return ruleType;
	}
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	/**
	 * @return the ruleChangeReason
	 */
	public String getRuleChangeReason() {
		return ruleChangeReason;
	}
	/**
	 * @param ruleChangeReason the ruleChangeReason to set
	 */
	public void setRuleChangeReason(String ruleChangeReason) {
		this.ruleChangeReason = ruleChangeReason;
	}
	/**
	 * @return the ruleState
	 */
	public String getRuleState() {
		return ruleState;
	}
	/**
	 * @param ruleState the ruleState to set
	 */
	public void setRuleState(String ruleState) {
		this.ruleState = ruleState;
	}
	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}
	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
