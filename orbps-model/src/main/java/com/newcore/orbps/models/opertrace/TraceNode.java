package com.newcore.orbps.models.opertrace;

import java.io.Serializable;
import java.util.Date;

public class TraceNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pclkBranchNo;
	
	private String pclkNo;
	
	private String pclkName;
	
	private Date procDate;
	
	private String procStat;
	
	private String description;

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
	 * @return the procDate
	 */
	public Date getProcDate() {
		return procDate;
	}

	/**
	 * @param procDate the procDate to set
	 */
	public void setProcDate(Date procDate) {
		this.procDate = procDate;
	}

	/**
	 * @return the procStat
	 */
	public String getProcStat() {
		return procStat;
	}

	/**
	 * @param procStat the procStat to set
	 */
	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
