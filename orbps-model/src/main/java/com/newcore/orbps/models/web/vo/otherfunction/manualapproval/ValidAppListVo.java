package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

public class ValidAppListVo implements Serializable{
	
private static final long serialVersionUID = 14344544563L;
	
	/** 序号 */
	private String applId;
	/** 险种名称 */
	private String busiPrdName;
	/** 超出天数 */
	private String overDays;
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
	 * @return the busiPrdName
	 */
	public String getBusiPrdName() {
		return busiPrdName;
	}
	/**
	 * @param busiPrdName the busiPrdName to set
	 */
	public void setBusiPrdName(String busiPrdName) {
		this.busiPrdName = busiPrdName;
	}
	/**
	 * @return the overDays
	 */
	public String getOverDays() {
		return overDays;
	}
	/**
	 * @param overDays the overDays to set
	 */
	public void setOverDays(String overDays) {
		this.overDays = overDays;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidAppListVo [applId=" + applId + ", busiPrdName=" + busiPrdName + ", overDays=" + overDays + "]";
	}

	
}
