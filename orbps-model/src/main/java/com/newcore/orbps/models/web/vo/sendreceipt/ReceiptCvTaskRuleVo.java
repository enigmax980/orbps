package com.newcore.orbps.models.web.vo.sendreceipt;

import java.io.Serializable;

public class ReceiptCvTaskRuleVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2102754671585320724L;
	//销售机构号
	private String salesBranchNo;
	//所属下级机构
	private String findSubBranchNoFlag;
	//投保单号
	private String applNo;
	//回执核销机构号
	private String oclkBranchNo;
	//回执核销员工号
	private String oclkClerkNo;
	//回执核销起期endDate
	private String startDate;
	//回执核销止期
	private String endDate;
	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
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
	 * @return the oclkBranchNo
	 */
	public String getOclkBranchNo() {
		return oclkBranchNo;
	}
	/**
	 * @param oclkBranchNo the oclkBranchNo to set
	 */
	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}
	/**
	 * @return the oclkClerkNo
	 */
	public String getOclkClerkNo() {
		return oclkClerkNo;
	}
	/**
	 * @param oclkClerkNo the oclkClerkNo to set
	 */
	public void setOclkClerkNo(String oclkClerkNo) {
		this.oclkClerkNo = oclkClerkNo;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    /**
     * @return the findSubBranchNoFlag
     */
    public String getFindSubBranchNoFlag() {
        return findSubBranchNoFlag;
    }
    /**
     * @param findSubBranchNoFlag the findSubBranchNoFlag to set
     */
    public void setFindSubBranchNoFlag(String findSubBranchNoFlag) {
        this.findSubBranchNoFlag = findSubBranchNoFlag;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReceiptCvTaskRuleVo [salesBranchNo=" + salesBranchNo + ", findSubBranchNoFlag=" + findSubBranchNoFlag
                + ", applNo=" + applNo + ", oclkBranchNo=" + oclkBranchNo + ", oclkClerkNo=" + oclkClerkNo
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
	
	
}
