package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

/**
 * 审批表格Vo
 * @author xiaoYe
 *
 */
public class ApprovalListVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544566L;
	/*字段名称:销售机构代码,长度:,是否必填:否*/
	private String salesBranchNo;
	/*字段名称:投保单号,长度:,是否必填:否*/
	private String applNo;
	/*字段名称:首个主险种代码,长度:,是否必填:否*/
	private String polCode;
	/*字段名称:新单受理时间,长度:,是否必填:否*/
	private String acceptDate;
	/*字段名称:生效日,长度:,是否必填:否*/
	private String inForceDate;
	/*字段名称:审批类型,长度:,是否必填:否*/
	private String ruleTye;
	/*字段名称:进入人工审批原因,长度:,是否必填:否*/
	private String reason;
	/*字段名称:审批结果,长度:,是否必填:否*/
	private String result;
	/*字段名称:审批操作人员机构号,长度:,是否必填:否*/
	private String pclkBranchNo;
	/*字段名称:审批操作人员工号,长度:,是否必填:否*/
	private String pclkNo;
	/*字段名称:审批操作人员姓名,长度:,是否必填:否*/
	private String pclkName;
	/*字段名称:审批操作时间,长度:,是否必填:否*/
	private String procDate;
	/*投保人，姓名*/
	private String sgName;
	/*字段名称:生效时间，,用于生效时间,长度:,是否必填:否*/
	private String signDate;
	/*字段名：备注*/
	private String note;
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
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}
	/**
	 * @param polCode the polCode to set
	 */
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	/**
	 * @return the acceptDate
	 */
	public String getAcceptDate() {
		return acceptDate;
	}
	/**
	 * @param acceptDate the acceptDate to set
	 */
	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}
	/**
	 * @return the inForceDate
	 */
	public String getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(String inForceDate) {
		this.inForceDate = inForceDate;
	}
	/**
	 * @return the ruleTye
	 */
	public String getRuleTye() {
		return ruleTye;
	}
	/**
	 * @param ruleTye the ruleTye to set
	 */
	public void setRuleTye(String ruleTye) {
		this.ruleTye = ruleTye;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
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
	 * @return the procDate
	 */
	public String getProcDate() {
		return procDate;
	}
	/**
	 * @param procDate the procDate to set
	 */
	public void setProcDate(String procDate) {
		this.procDate = procDate;
	}
	
	/**
	 * @return the sgName
	 */
	public String getSgName() {
		return sgName;
	}
	/**
	 * @param sgName the sgName to set
	 */
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}
	/**
	 * @return the signDate
	 */
	public String getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApprovalListVo [salesBranchNo=" + salesBranchNo + ", applNo=" + applNo + ", polCode=" + polCode
				+ ", acceptDate=" + acceptDate + ", inForceDate=" + inForceDate + ", ruleTye=" + ruleTye + ", reason="
				+ reason + ", result=" + result + ", pclkBranchNo=" + pclkBranchNo + ", pclkNo=" + pclkNo
				+ ", pclkName=" + pclkName + ", procDate=" + procDate + ", sgName=" + sgName + ", signDate=" + signDate
				+ "]";
	}
	
	
}
