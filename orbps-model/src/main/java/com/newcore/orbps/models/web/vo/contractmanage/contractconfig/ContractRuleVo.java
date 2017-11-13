package com.newcore.orbps.models.web.vo.contractmanage.contractconfig;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则管理信息
 * @author xiaoye
 *
 */
public class ContractRuleVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445435L;
	/** 机构代码 */
	private String branchCode;
	/** 销售渠道 */
	private String salesChannel;
	/** 业务场景 */
	private String bsScenario;
	/** 险种代码 */
	private String busiPrdCode;
	/** 合同类型 */
	private String cntrType;
	/** 构件信息 */
	private ContractComponentListVo cntrComponentListVo;
	/** 启用日期 */
	private Date startDate;
	/** 停用日期 */
	private Date endDate;
	/** 规则状态 */
	private String ruleState;
	/** 规则变化情况 */
	private String changeState;
	/** 规则变化说明 */
	private String changeReason;
	/** 审核状态 */
	private String approvalState;
	/** 审核意见 */
	private String approvalInfo;
	/** 审核结果 */
	private String approvalResult;
	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}
	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
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
	 * @return the bsScenario
	 */
	public String getBsScenario() {
		return bsScenario;
	}
	/**
	 * @param bsScenario the bsScenario to set
	 */
	public void setBsScenario(String bsScenario) {
		this.bsScenario = bsScenario;
	}
	/**
	 * @return the busiPrdCode
	 */
	public String getBusiPrdCode() {
		return busiPrdCode;
	}
	/**
	 * @param busiPrdCode the busiPrdCode to set
	 */
	public void setBusiPrdCode(String busiPrdCode) {
		this.busiPrdCode = busiPrdCode;
	}
	/**
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}
	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	/**
	 * @return the cntrComponentListVo
	 */
	public ContractComponentListVo getCntrComponentListVo() {
		return cntrComponentListVo;
	}
	/**
	 * @param cntrComponentListVo the cntrComponentListVo to set
	 */
	public void setCntrComponentListVo(ContractComponentListVo cntrComponentListVo) {
		this.cntrComponentListVo = cntrComponentListVo;
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
	 * @return the changeState
	 */
	public String getChangeState() {
		return changeState;
	}
	/**
	 * @param changeState the changeState to set
	 */
	public void setChangeState(String changeState) {
		this.changeState = changeState;
	}
	/**
	 * @return the changeReason
	 */
	public String getChangeReason() {
		return changeReason;
	}
	/**
	 * @param changeReason the changeReason to set
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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
	 * @return the approvalInfo
	 */
	public String getApprovalInfo() {
		return approvalInfo;
	}
	/**
	 * @param approvalInfo the approvalInfo to set
	 */
	public void setApprovalInfo(String approvalInfo) {
		this.approvalInfo = approvalInfo;
	}
	/**
	 * @return the approvalResult
	 */
	public String getApprovalResult() {
		return approvalResult;
	}
	/**
	 * @param approvalResult the approvalResult to set
	 */
	public void setApprovalResult(String approvalResult) {
		this.approvalResult = approvalResult;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContractRuleVo [branchCode=" + branchCode + ", salesChannel=" + salesChannel + ", bsScenario="
				+ bsScenario + ", busiPrdCode=" + busiPrdCode + ", cntrType=" + cntrType + ", cntrComponentListVo="
				+ cntrComponentListVo + ", startDate=" + startDate + ", endDate=" + endDate + ", ruleState=" + ruleState
				+ ", changeState=" + changeState + ", changeReason=" + changeReason + ", approvalState=" + approvalState
				+ ", approvalInfo=" + approvalInfo + ", approvalResult=" + approvalResult + "]";
	}
	
	
}
