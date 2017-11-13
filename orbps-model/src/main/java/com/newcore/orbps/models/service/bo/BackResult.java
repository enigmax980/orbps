package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2017年2月21日下午7:45:34
 */
public class BackResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8573121938087594554L;
	//销售机构号
	private String saleBranchNo;
	//投保单号
	private String applNo;
	//投保人、汇交人姓名
	private String hldrName;
	//回退人员机构号
	private String pclkBranchNo;
	//回退人员工号
	private String pclkNo;
	//回退人员姓名
	private String pclkName;
	//回退时间
	private Date pclkDate;
	//回退前保单任务环节
	private String preStat;
	//当前保单任务环节
	private String Stat;
	//回退原因
	private String backReason;
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
	/**
	 * @return the stat
	 */
	public String getStat() {
		return Stat;
	}
	/**
	 * @param stat the stat to set
	 */
	public void setStat(String stat) {
		Stat = stat;
	}
	/**
	 * @return the backReason
	 */
	public String getBackReason() {
		return backReason;
	}
	/**
	 * @param backReason the backReason to set
	 */
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	
}
