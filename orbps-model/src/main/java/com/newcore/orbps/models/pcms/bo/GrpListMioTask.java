package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;
/**
 * 实收付流水任务 BO
 * @author lijifei
 *
 */
public class GrpListMioTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2085517350985652679L;

	private String applNo;
	private String mgrBranchNo;
	private String pclkNo;
	private String pclkBranchNo;
	private long minMioID;
	private long maxMioID;
	private long batNo;
	private String mioType;
	private List<PolCode> polCodeList;
	private long plnBatNo;
	
	public GrpListMioTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public long getPlnBatNo() {
		return plnBatNo;
	}

	public void setPlnBatNo(long plnBatNo) {
		this.plnBatNo = plnBatNo;
	}

	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getPclkNo() {
		return pclkNo;
	}
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
	}
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	public long getMinMioID() {
		return minMioID;
	}
	public void setMinMioID(long minMioID) {
		this.minMioID = minMioID;
	}
	public long getMaxMioID() {
		return maxMioID;
	}
	public void setMaxMioID(long maxMioID) {
		this.maxMioID = maxMioID;
	}
	public long getBatNo() {
		return batNo;
	}
	public void setBatNo(long batNo) {
		this.batNo = batNo;
	}
	public String getMioType() {
		return mioType;
	}
	public void setMioType(String mioType) {
		this.mioType = mioType;
	}
	public List<PolCode> getPolCodeList() {
		return polCodeList;
	}
	public void setPolCodeList(List<PolCode> polCodeList) {
		this.polCodeList = polCodeList;
	}








}
