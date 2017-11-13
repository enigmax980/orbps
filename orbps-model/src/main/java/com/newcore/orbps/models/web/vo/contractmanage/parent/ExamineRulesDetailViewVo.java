package com.newcore.orbps.models.web.vo.contractmanage.parent;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则添加信息
 * @author xiaoye
 *
 */
public class ExamineRulesDetailViewVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445434L;
	
	/** 序号 */
    private int id;
    /** 管理机构号 */
    private String mgrBranchNo;
    /** 规则类型*/
    private String ruleType;
    /** 规则状态 */
    private String ruleState;
    /** 规则名称 */
    private String ruleName;
    /** 生效日期 */
    private String validDate;
    /** 失效日期 */
    private String invalidDate;
    /** 销售机构号 */
    private String branchNo;
    /** 销售渠道 */
    private String salesChannel;
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
    /** 机构号 */
    private String salesbranchNo;
    /** 规则变化原因 */
    private String ruleChangeReason;
    /** 1、生效日往前追溯天数 */
    private Long beforeEffectiveDate;
    /** 2、生效日往后指定天数 */
    private Long afterEffectiveDate;
    /** 3、生效日往前追溯跨越日期 */
    private String effectiveDateBackAcross;
    /** 审核状态 */
    private String auditStatus ;
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
	 * @return the validDate
	 */
	public String getValidDate() {
		return validDate;
	}
	/**
	 * @param validDate the validDate to set
	 */
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	/**
	 * @return the invalidDate
	 */
	public String getInvalidDate() {
		return invalidDate;
	}
	/**
	 * @param invalidDate the invalidDate to set
	 */
	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}
	/**
	 * @return the branchNo
	 */
	public String getBranchNo() {
		return branchNo;
	}
	/**
	 * @param branchNo the branchNo to set
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
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
	 * @return the salesbranchNo
	 */
	public String getSalesbranchNo() {
		return salesbranchNo;
	}
	/**
	 * @param salesbranchNo the salesbranchNo to set
	 */
	public void setSalesbranchNo(String salesbranchNo) {
		this.salesbranchNo = salesbranchNo;
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
	 * @return the auditStatus
	 */
	public String getAuditStatus() {
		return auditStatus;
	}
	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExamineRulesDetailViewVo [id=" + id + ", mgrBranchNo=" + mgrBranchNo + ", ruleType=" + ruleType
				+ ", ruleState=" + ruleState + ", ruleName=" + ruleName + ", validDate=" + validDate + ", invalidDate="
				+ invalidDate + ", branchNo=" + branchNo + ", salesChannel=" + salesChannel + ", scene=" + scene
				+ ", cntrForm=" + cntrForm + ", productType=" + productType + ", autoApproveFlag=" + autoApproveFlag
				+ ", artificialApproveFlag=" + artificialApproveFlag + ", salesbranchNo=" + salesbranchNo
				+ ", ruleChangeReason=" + ruleChangeReason + ", beforeEffectiveDate=" + beforeEffectiveDate
				+ ", afterEffectiveDate=" + afterEffectiveDate + ", effectiveDateBackAcross=" + effectiveDateBackAcross
				+ ", auditStatus=" + auditStatus + "]";
	}
    
}