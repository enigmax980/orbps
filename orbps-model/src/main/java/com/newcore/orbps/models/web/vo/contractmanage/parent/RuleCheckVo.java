package com.newcore.orbps.models.web.vo.contractmanage.parent;

import java.io.Serializable;

/**
 * 审核查询信息
 * @author xiaoye
 *
 */
public class RuleCheckVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445432L;
	/** 管理机构号 */
	private String mgBranchNo;
	/** 规则类型 */
	private String ruleType;
	/** 审核状态 */
	private String approvalState;
	/** 序号*/
	private String applId;
	/** 规则名称 */
	private String ruleName;
	/** 提交人员机构 */
	private String submitBranch;
	/** 提交人员姓名 */
	private String submitName;
	/** 提交人员工号 */
	private String submitNo;
	/** 提交时间 */
	private String submitDate;
	/**
	 * @return the mgBranchNo
	 */
	public String getMgBranchNo() {
		return mgBranchNo;
	}
	/**
	 * @param mgBranchNo the mgBranchNo to set
	 */
	public void setMgBranchNo(String mgBranchNo) {
		this.mgBranchNo = mgBranchNo;
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
	 * @return the approvalState
	 */
	public String getApprovalState() {
		return approvalState;
	}
	/**
	 * @param approvalState the approvalState to set
	 */
	public void setApprovalState(String approvalState) {
		this.approvalState = approvalState;
	}
	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}
	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
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
	 * @return the submitBranch
	 */
	public String getSubmitBranch() {
		return submitBranch;
	}
	/**
	 * @param submitBranch the submitBranch to set
	 */
	public void setSubmitBranch(String submitBranch) {
		this.submitBranch = submitBranch;
	}
	/**
	 * @return the submitName
	 */
	public String getSubmitName() {
		return submitName;
	}
	/**
	 * @param submitName the submitName to set
	 */
	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}
	/**
	 * @return the submitNo
	 */
	public String getSubmitNo() {
		return submitNo;
	}
	/**
	 * @param submitNo the submitNo to set
	 */
	public void setSubmitNo(String submitNo) {
		this.submitNo = submitNo;
	}
	/**
	 * @return the submitDate
	 */
	public String getSubmitDate() {
		return submitDate;
	}
	/**
	 * @param submitDate the submitDate to set
	 */
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApprovalQueryVo [mgBranchNo=" + mgBranchNo + ", ruleType=" + ruleType + ", approvalState="
				+ approvalState + ", applId=" + applId + ", ruleName=" + ruleName + ", submitBranch=" + submitBranch
				+ ", submitName=" + submitName + ", submitNo=" + submitNo + ", submitDate=" + submitDate + "]";
	}


	
}
