package com.newcore.orbps.models.web.vo.contractmanage.accountconfig;

import java.io.Serializable;

/**
 * 操作轨迹查询信息
 * @author xiaoye
 *
 */
public class AccountOperatingTrackVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445433L;
	/** 操作序号 */
	private String applId;
	/** 操作时间 */
	private String operaDate;
	/** 操作类型 */
	private String operaType;
	/** 操作机构 */
	private String operaBranch;
	/** 操作人员工号 */
	private String operaNo;
	/** 操作人员姓名 */
	private String operaName;
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
	 * @return the operaDate
	 */
	public String getOperaDate() {
		return operaDate;
	}
	/**
	 * @param operaDate the operaDate to set
	 */
	public void setOperaDate(String operaDate) {
		this.operaDate = operaDate;
	}
	/**
	 * @return the operaType
	 */
	public String getOperaType() {
		return operaType;
	}
	/**
	 * @param operaType the operaType to set
	 */
	public void setOperaType(String operaType) {
		this.operaType = operaType;
	}
	/**
	 * @return the operaBranch
	 */
	public String getOperaBranch() {
		return operaBranch;
	}
	/**
	 * @param operaBranch the operaBranch to set
	 */
	public void setOperaBranch(String operaBranch) {
		this.operaBranch = operaBranch;
	}
	/**
	 * @return the operaNo
	 */
	public String getOperaNo() {
		return operaNo;
	}
	/**
	 * @param operaNo the operaNo to set
	 */
	public void setOperaNo(String operaNo) {
		this.operaNo = operaNo;
	}
	/**
	 * @return the operaName
	 */
	public String getOperaName() {
		return operaName;
	}
	/**
	 * @param operaName the operaName to set
	 */
	public void setOperaName(String operaName) {
		this.operaName = operaName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OperatingTrackVo [applId=" + applId + ", operaDate=" + operaDate + ", operaType=" + operaType
				+ ", operaBranch=" + operaBranch + ", operaNo=" + operaNo + ", operaName=" + operaName + "]";
	}


	
}
