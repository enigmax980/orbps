package com.newcore.orbps.models.web.vo.contractmanage.parent;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则添加信息
 * @author xiaoye
 *
 */
public class RuleAddVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445434L;
	/** 序号 */
    private int applId;
	/** 管理机构号 */
	private String branchNo;
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
    /**
     * @return the applId
     */
    public int getApplId() {
        return applId;
    }
    /**
     * @param applId the applId to set
     */
    public void setApplId(int applId) {
        this.applId = applId;
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
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RuleAddVo [applId=" + applId + ", branchNo=" + branchNo + ", ruleType=" + ruleType
                + ", ruleChangeReason=" + ruleChangeReason + ", ruleState=" + ruleState + ", ruleName=" + ruleName
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
   
    
    
    
    
}