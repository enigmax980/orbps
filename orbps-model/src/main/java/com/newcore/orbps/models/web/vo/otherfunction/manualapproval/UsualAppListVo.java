package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

public class UsualAppListVo implements Serializable{
	
private static final long serialVersionUID = 14344544564L;
	
	/** 序号 */
	private String applId;
	/** 内容类型 */
	private String contentType;
	/** 审批决定 */
	private String approvalDecision;
	/** 机构号 */
	private String branchNo;
	/** 报批员工号 */
	private String approvalerNo;
	/** 报批时间 */
	private String approvalDate;
	/** 报批原因 */
	private String approvalReason;
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
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	 * @return the approvalerNo
	 */
	public String getApprovalerNo() {
		return approvalerNo;
	}
	/**
	 * @param approvalerNo the approvalerNo to set
	 */
	public void setApprovalerNo(String approvalerNo) {
		this.approvalerNo = approvalerNo;
	}
	/**
	 * @return the approvalDate
	 */
	public String getApprovalDate() {
		return approvalDate;
	}
	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	/**
	 * @return the approvalReason
	 */
	public String getApprovalReason() {
		return approvalReason;
	}
	/**
	 * @param approvalReason the approvalReason to set
	 */
	public void setApprovalReason(String approvalReason) {
		this.approvalReason = approvalReason;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UsualApprovalListVo [applId=" + applId + ", contentType=" + contentType + ", approvalDecision="
				+ approvalDecision + ", branchNo=" + branchNo + ", approvalerNo=" + approvalerNo + ", approvalDate="
				+ approvalDate + ", approvalReason=" + approvalReason + "]";
	}
	
	

}
