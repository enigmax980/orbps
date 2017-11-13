package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;

/**
 * 审批查询条件Vo
 * @author wanghonglin
 *
 */
public class ApprovalVo  implements Serializable{
	private static final long serialVersionUID = 14344544561L;
	/** 投保单号 */
	private String applNo;
	/** 销售机构代码 */
	private String salesBranchNo;
	/**是否包含下级机构*/
	private String levelFlag;
	/**审批操作人员机构号*/
	private String pclkBranchNo;
	/**审批操作员工号*/
	private String pclkNo;
	/**审批类型*/
	private String ruleTye;
	/**审批操作起期*/
	private String startDate;
	/**审批操作止期*/
	private String endDate;
	/**是否审批通过*/
	private String result;
	/**审批信息*/
	private List<ApprovalListVo> queryApproval = new ArrayList<ApprovalListVo>();
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
	 * @return the levelFlag
	 */
	public String getLevelFlag() {
		return levelFlag;
	}
	/**
	 * @param levelFlag the levelFlag to set
	 */
	public void setLevelFlag(String levelFlag) {
		this.levelFlag = levelFlag;
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
	 * @return the queryApproval
	 */
	public List<ApprovalListVo> getQueryApproval() {
		return queryApproval;
	}
	/**
	 * @param queryApproval the queryApproval to set
	 */
	public void setQueryApproval(List<ApprovalListVo> queryApproval) {
		this.queryApproval = queryApproval;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApprovalVo [applNo=" + applNo + ", salesBranchNo=" + salesBranchNo + ", levelFlag=" + levelFlag
				+ ", pclkBranchNo=" + pclkBranchNo + ", pclkNo=" + pclkNo + ", ruleTye=" + ruleTye + ", startDate="
				+ startDate + ", endDate=" + endDate + ", result=" + result + ", queryApproval=" + queryApproval + "]";
	}
	
}
