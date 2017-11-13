package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsualApprovalVo implements Serializable{
	
private static final long serialVersionUID = 14344544562L;
	
	/** 报批原因 */
	private String approvalReason;
	/** 审批意见 */
	private String approvalOpinion;
	/** 审批决定 */
	private String approvalDecision;
	/** 上报至机构 */
	private String reportToBranch;
	/** 上报至人员 */
	private String reportToWorker;
	/** 报批岗位 */
	private String approvalStation;
	/** 提交审批时间 */
	private String submitCheckDate;
	/** 审批员机构 */
	private String checkBranch;
	/** 审批员号 */
	private String checkNo;
	/** 报批人机构 */
	private String approvalBranch;
	/** 报批人工号 */
	private String approvalNo;
	/** 报批人姓名 */
	private String approvalName;
	
	private List<UsualAppListVo> usualAppListVos = new ArrayList<UsualAppListVo>();
	
	
	/**
	 * @return the usualAppListVos
	 */
	public List<UsualAppListVo> getUsualAppListVos() {
		return usualAppListVos;
	}
	/**
	 * @param usualAppListVos the usualAppListVos to set
	 */
	public void setUsualAppListVos(List<UsualAppListVo> usualAppListVos) {
		this.usualAppListVos = usualAppListVos;
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
	/**
	 * @return the approvalOpinion
	 */
	public String getApprovalOpinion() {
		return approvalOpinion;
	}
	/**
	 * @param approvalOpinion the approvalOpinion to set
	 */
	public void setApprovalOpinion(String approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
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
	 * @return the reportToBranch
	 */
	public String getReportToBranch() {
		return reportToBranch;
	}
	/**
	 * @param reportToBranch the reportToBranch to set
	 */
	public void setReportToBranch(String reportToBranch) {
		this.reportToBranch = reportToBranch;
	}
	/**
	 * @return the reportToWorker
	 */
	public String getReportToWorker() {
		return reportToWorker;
	}
	/**
	 * @param reportToWorker the reportToWorker to set
	 */
	public void setReportToWorker(String reportToWorker) {
		this.reportToWorker = reportToWorker;
	}
	/**
	 * @return the approvalStation
	 */
	public String getApprovalStation() {
		return approvalStation;
	}
	/**
	 * @param approvalStation the approvalStation to set
	 */
	public void setApprovalStation(String approvalStation) {
		this.approvalStation = approvalStation;
	}
	/**
	 * @return the submitCheckDate
	 */
	public String getSubmitCheckDate() {
		return submitCheckDate;
	}
	/**
	 * @param submitCheckDate the submitCheckDate to set
	 */
	public void setSubmitCheckDate(String submitCheckDate) {
		this.submitCheckDate = submitCheckDate;
	}
	/**
	 * @return the checkBranch
	 */
	public String getCheckBranch() {
		return checkBranch;
	}
	/**
	 * @param checkBranch the checkBranch to set
	 */
	public void setCheckBranch(String checkBranch) {
		this.checkBranch = checkBranch;
	}
	/**
	 * @return the checkNo
	 */
	public String getCheckNo() {
		return checkNo;
	}
	/**
	 * @param checkNo the checkNo to set
	 */
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	/**
	 * @return the approvalBranch
	 */
	public String getApprovalBranch() {
		return approvalBranch;
	}
	/**
	 * @param approvalBranch the approvalBranch to set
	 */
	public void setApprovalBranch(String approvalBranch) {
		this.approvalBranch = approvalBranch;
	}
	/**
	 * @return the approvalNo
	 */
	public String getApprovalNo() {
		return approvalNo;
	}
	/**
	 * @param approvalNo the approvalNo to set
	 */
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}
	/**
	 * @return the approvalName
	 */
	public String getApprovalName() {
		return approvalName;
	}
	/**
	 * @param approvalName the approvalName to set
	 */
	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UsualApprovalVo [approvalReason=" + approvalReason + ", approvalOpinion=" + approvalOpinion
				+ ", approvalDecision=" + approvalDecision + ", reportToBranch=" + reportToBranch + ", reportToWorker="
				+ reportToWorker + ", approvalStation=" + approvalStation + ", submitCheckDate=" + submitCheckDate
				+ ", checkBranch=" + checkBranch + ", checkNo=" + checkNo + ", approvalBranch=" + approvalBranch
				+ ", approvalNo=" + approvalNo + ", approvalName=" + approvalName + ", usualAppListVos="
				+ usualAppListVos + "]";
	}

	

}
