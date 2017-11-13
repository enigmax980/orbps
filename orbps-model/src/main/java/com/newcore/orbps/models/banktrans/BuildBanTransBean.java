package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IO_MIOS_GEN_BANK_TRANS_SCXE")
public class BuildBanTransBean implements Serializable{
	/**
	 * 系统号 [9: 四川小额]
	 */
	private String sysNo;
	/**
	 * 机构号
	 */
	private String branchNo;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 收付类型 [1收] [-1付]
	 */
	private Integer rpFlag;
	/**
	 * 首期续期表记 [0首期] [1续期]
	 */
	private Integer fcFlag;
	/**
	 * 是否包含下级机构 [0不包含] [1包含]
	 */
	private Integer sbFlag;
	/**
	 * 操作日期 [对应应收付日期]
	 */
	private String procDate;
	/**
	 * 转帐来源 [只八版使用]
	 */
	private String transSource;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 开始执行时间
	 */
	private String startExecTime;
	
	public BuildBanTransBean() {
		////Add a nested comment explaining why this method is empty, throw an UnsupportedOperationException or complete the implementation
	}

	public BuildBanTransBean(String sysNo, String branchNo, String bankCode,
			Integer rpFlag, Integer fcFlag, Integer sbFlag, String procDate,
			String transSource, String taskId, String startExecTime) {
		this.sysNo = sysNo;
		this.branchNo = branchNo;
		this.bankCode = bankCode;
		this.rpFlag = rpFlag;
		this.fcFlag = fcFlag;
		this.sbFlag = sbFlag;
		this.procDate = procDate;
		this.transSource = transSource;
		this.taskId = taskId;
		this.startExecTime = startExecTime;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getRpFlag() {
		return rpFlag;
	}

	public void setRpFlag(Integer rpFlag) {
		this.rpFlag = rpFlag;
	}

	public Integer getFcFlag() {
		return fcFlag;
	}

	public void setFcFlag(Integer fcFlag) {
		this.fcFlag = fcFlag;
	}

	public Integer getSbFlag() {
		return sbFlag;
	}

	public void setSbFlag(Integer sbFlag) {
		this.sbFlag = sbFlag;
	}

	public String getProcDate() {
		return procDate;
	}

	public void setProcDate(String procDate) {
		this.procDate = procDate;
	}

	public String getTransSource() {
		return transSource;
	}

	public void setTransSource(String transSource) {
		this.transSource = transSource;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStartExecTime() {
		return startExecTime;
	}

	public void setStartExecTime(String startExecTime) {
		this.startExecTime = startExecTime;
	}
}