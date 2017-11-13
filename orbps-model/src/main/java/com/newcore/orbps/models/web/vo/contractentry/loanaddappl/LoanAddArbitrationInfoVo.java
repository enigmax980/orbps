package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;

/**
 * 仲裁信息Vo
 * @author jincong
 *
 */
public class LoanAddArbitrationInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030006L;
	
	/** 保单性质 */
	private String cntrNature;
	/** 是否人工核保 */
	private String manualUwFlag;
	/** 争议处理方式 */
	private String disputeHandType;
	/** 仲裁委员会名称 */
	private String arbitrationName;
	
	/**
	 * @return the cntrNature
	 */
	public String getCntrNature() {
		return cntrNature;
	}
	/**
	 * @param cntrNature the cntrNature to set
	 */
	public void setCntrNature(String cntrNature) {
		this.cntrNature = cntrNature;
	}
	/**
	 * @return the manualUwFlag
	 */
	public String getManualUwFlag() {
		return manualUwFlag;
	}
	/**
	 * @param manualUwFlag the manualUwFlag to set
	 */
	public void setManualUwFlag(String manualUwFlag) {
		this.manualUwFlag = manualUwFlag;
	}
	/**
	 * @return the disputeHandType
	 */
	public String getDisputeHandType() {
		return disputeHandType;
	}
	/**
	 * @param disputeHandType the disputeHandType to set
	 */
	public void setDisputeHandType(String disputeHandType) {
		this.disputeHandType = disputeHandType;
	}
	/**
	 * @return the arbitrationName
	 */
	public String getArbitrationName() {
		return arbitrationName;
	}
	/**
	 * @param arbitrationName the arbitrationName to set
	 */
	public void setArbitrationName(String arbitrationName) {
		this.arbitrationName = arbitrationName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArbitrationInfoVo [cntrNature=" + cntrNature + ", manualUwFlag=" + manualUwFlag + ", disputeHandType="
				+ disputeHandType + ", arbitrationName=" + arbitrationName + "]";
	}
}
