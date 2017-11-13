package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2017年2月21日上午10:53:31
 */
public class UndoResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7447490325315974660L;
	//销售机构号
	private String saleBranchNo;
	//投保单号
	private String applNo;
	//投保人、汇交人姓名
	private String hldrName;
	//撤销人员机构号
	private String pclkBranchNo;
	//撤销人员工号
	private String pclkNo;
	//撤销人员姓名
	private String pclkName;
	//撤销时间
	private Date pclkDate;
	//撤销前保单任务环节
	private String preStat;
	/**
	 * @return the saleBranchNo
	 */
	public String getSaleBranchNo() {
		return saleBranchNo;
	}
	/**
	 * @param saleBranchNo the saleBranchNo to set
	 */
	public void setSaleBranchNo(String saleBranchNo) {
		this.saleBranchNo = saleBranchNo;
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
	 * @return the hldrName
	 */
	public String getHldrName() {
		return hldrName;
	}
	/**
	 * @param hldrName the hldrName to set
	 */
	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}
	/**
	 * @return the pclkBranchNo
	 */
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	/**
	 * @param pclkBranchNo the pclkBranchNo to set
	 */
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	/**
	 * @return the pclkNo
	 */
	public String getPclkNo() {
		return pclkNo;
	}
	/**
	 * @param pclkNo the pclkNo to set
	 */
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
	}
	/**
	 * @return the pclkName
	 */
	public String getPclkName() {
		return pclkName;
	}
	/**
	 * @param pclkName the pclkName to set
	 */
	public void setPclkName(String pclkName) {
		this.pclkName = pclkName;
	}
	/**
	 * @return the pclkDate
	 */
	public Date getPclkDate() {
		return pclkDate;
	}
	/**
	 * @param pclkDate the pclkDate to set
	 */
	public void setPclkDate(Date pclkDate) {
		this.pclkDate = pclkDate;
	}
	/**
	 * @return the preStat
	 */
	public String getPreStat() {
		return preStat;
	}
	/**
	 * @param preStat the preStat to set
	 */
	public void setPreStat(String preStat) {
		this.preStat = preStat;
	}
}
