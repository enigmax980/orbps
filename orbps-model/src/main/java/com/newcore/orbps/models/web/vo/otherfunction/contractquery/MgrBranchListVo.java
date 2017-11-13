package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;
import java.util.List;

/**
 * 审批表格Table
 * @author xiaoYe
 *
 */
public class MgrBranchListVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544572L;
	/** 序号 */
	private String mgrRad;
	/** 保单管理机构号 */
	private String applManageBranchNo;
	/** 保单管理机构名称 */
	private String applManageBranchName;
	/** 投保单号/汇交申请号 */
	private String applNo;
	/** 详细业务情况 */
	private List<String> detailBusiCondition;
	/**
	 * @return the mgrRad
	 */
	public String getMgrRad() {
		return mgrRad;
	}
	/**
	 * @param mgrRad the mgrRad to set
	 */
	public void setMgrRad(String mgrRad) {
		this.mgrRad = mgrRad;
	}
	/**
	 * @return the applManageBranchNo
	 */
	public String getApplManageBranchNo() {
		return applManageBranchNo;
	}
	/**
	 * @param applManageBranchNo the applManageBranchNo to set
	 */
	public void setApplManageBranchNo(String applManageBranchNo) {
		this.applManageBranchNo = applManageBranchNo;
	}
	/**
	 * @return the applManageBranchName
	 */
	public String getApplManageBranchName() {
		return applManageBranchName;
	}
	/**
	 * @param applManageBranchName the applManageBranchName to set
	 */
	public void setApplManageBranchName(String applManageBranchName) {
		this.applManageBranchName = applManageBranchName;
	}
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the detailBusiCondition
	 */
	public List<String> getDetailBusiCondition() {
		return detailBusiCondition;
	}
	/**
	 * @param detailBusiCondition the detailBusiCondition to set
	 */
	public void setDetailBusiCondition(List<String> detailBusiCondition) {
		this.detailBusiCondition = detailBusiCondition;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MgrBranchListVo [mgrRad=" + mgrRad + ", applManageBranchNo=" + applManageBranchNo
				+ ", applManageBranchName=" + applManageBranchName + ", applNo=" + applNo + ", detailBusiCondition="
				+ detailBusiCondition + "]";
	}
	
	
}
