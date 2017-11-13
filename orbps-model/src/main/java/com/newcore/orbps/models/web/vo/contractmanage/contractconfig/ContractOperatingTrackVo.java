package com.newcore.orbps.models.web.vo.contractmanage.contractconfig;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作轨迹查询信息
 * @author xiaoye
 *
 */
public class ContractOperatingTrackVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445439L;
	/** 操作序号 */
	private String applId;
	/** 操作时间 */
	private Date operaDate;
	/** 操作类型 */
	private String operaType;
	/** 操作机构号*/
	private String operaBranchNo;
	/** 操作机构名称*/
	private String operaBranchName;
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
	public Date getOperaDate() {
		return operaDate;
	}
	public void setOperaDate(Date operaDate) {
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

	public String getOperaBranchNo() {
		return operaBranchNo;
	}
	public void setOperaBranchNo(String operaBranchNo) {
		this.operaBranchNo = operaBranchNo;
	}
	public String getOperaBranchName() {
		return operaBranchName;
	}
	public void setOperaBranchName(String operaBranchName) {
		this.operaBranchName = operaBranchName;
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
				+ ", operaBranchNo=" + operaBranchNo + ", operaBranchName=" + operaBranchName + ",operaNo=" + operaNo + ", operaName=" + operaName + "]";
	}


	
}
