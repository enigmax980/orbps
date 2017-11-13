package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

public class InsurApplApprovalRule implements Serializable{
    
	private static final long serialVersionUID = 1434454499L;
	/**序号*/
	private Integer id;	
	/** 管理机构代码 */
	private String mgrBranchNo;  
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构号 */
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
    private String  effectiveDateBackAcross;
    /** 规则类型 */
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
    
    private String retCode;
    private String errMsg;
    /** 审核状态 */
    private String auditStatus;
    /** 审核意见 */
    private String queryAuditOpinion;
    
    /** 当前操作员机构 */
    private String operBranchNo;   
    /** 当前操作员工号 */
	private String operClerkNo;   
	/** 当前操作员姓名 */
	private String  operClerkName;  
	/** 当前操作时间 */
	private Date  operDate;        
	/** 操作类型 */
	private String  operState;      
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getCntrForm() {
		return cntrForm;
	}
	public void setCntrForm(String cntrForm) {
		this.cntrForm = cntrForm;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getAutoApproveFlag() {
		return autoApproveFlag;
	}
	public void setAutoApproveFlag(String autoApproveFlag) {
		this.autoApproveFlag = autoApproveFlag;
	}
	public String getArtificialApproveFlag() {
		return artificialApproveFlag;
	}
	public void setArtificialApproveFlag(String artificialApproveFlag) {
		this.artificialApproveFlag = artificialApproveFlag;
	}
	public Long getBeforeEffectiveDate() {
		return beforeEffectiveDate;
	}
	public void setBeforeEffectiveDate(Long beforeEffectiveDate) {
		this.beforeEffectiveDate = beforeEffectiveDate;
	}
	public Long getAfterEffectiveDate() {
		return afterEffectiveDate;
	}
	public void setAfterEffectiveDate(Long afterEffectiveDate) {
		this.afterEffectiveDate = afterEffectiveDate;
	}

	public String getEffectiveDateBackAcross() {
		return effectiveDateBackAcross;
	}
	public void setEffectiveDateBackAcross(String effectiveDateBackAcross) {
		this.effectiveDateBackAcross = effectiveDateBackAcross;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getRuleChangeReason() {
		return ruleChangeReason;
	}
	public void setRuleChangeReason(String ruleChangeReason) {
		this.ruleChangeReason = ruleChangeReason;
	}
	public String getRuleState() {
		return ruleState;
	}
	public void setRuleState(String ruleState) {
		this.ruleState = ruleState;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}	
	/**
     * @return the queryAuditOpinion
     */
    public String getQueryAuditOpinion() {
        return queryAuditOpinion;
    }
    /**
     * @param queryAuditOpinion the queryAuditOpinion to set
     */
    public void setQueryAuditOpinion(String queryAuditOpinion) {
        this.queryAuditOpinion = queryAuditOpinion;
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
    
    public String getOperBranchNo() {
		return operBranchNo;
	}
	public void setOperBranchNo(String operBranchNo) {
		this.operBranchNo = operBranchNo;
	}
	public String getOperClerkNo() {
		return operClerkNo;
	}
	public void setOperClerkNo(String operClerkNo) {
		this.operClerkNo = operClerkNo;
	}
	public String getOperClerkName() {
		return operClerkName;
	}
	public void setOperClerkName(String operClerkName) {
		this.operClerkName = operClerkName;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOperState() {
		return operState;
	}
	public void setOperState(String operState) {
		this.operState = operState;
	}
	@Override
    public String toString() {
        return "InsurApplApprovalRule [id=" + id + ", mgrBranchNo=" + mgrBranchNo + ", salesChannel=" + salesChannel
                + ", salesBranchNo=" + salesBranchNo + ", scene=" + scene + ", cntrForm=" + cntrForm + ", productType="
                + productType + ", autoApproveFlag=" + autoApproveFlag + ", artificialApproveFlag="
                + artificialApproveFlag + ", beforeEffectiveDate=" + beforeEffectiveDate + ", afterEffectiveDate="
                + afterEffectiveDate + ", effectiveDateBackAcross=" + effectiveDateBackAcross + ", ruleType=" + ruleType
                + ", ruleChangeReason=" + ruleChangeReason + ", ruleState=" + ruleState + ", ruleName=" + ruleName
                + ", startDate=" + startDate + ", endDate=" + endDate + ", retCode=" + retCode + ", errMsg=" + errMsg
                + ", auditStatus=" + auditStatus + ", queryAuditOpinion=" + queryAuditOpinion + "]";
    }
   
}
