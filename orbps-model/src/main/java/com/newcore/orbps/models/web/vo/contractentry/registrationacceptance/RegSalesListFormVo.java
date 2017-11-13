package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;

/**
 * 展业信息
 * @author jincong
 *
 */
public class RegSalesListFormVo implements Serializable{

	private static final long serialVersionUID = 13242344403L;
	
	/**字段名：主副标记*/
	private String jointFieldWorkFlag;
	
	/**字段名：销售渠道*/
	private String salesChannel;
	
	/**字段名：销售机构代码*/
	private String salesBranchNo;
	
	/**字段名：销售人员代码*/
	private String salesNo;
	
	/**字段名：代理网点号*/
	private String worksiteNo;
	
	/**字段名：网点名称*/
	private String worksiteName;
	
	/**字段名：展业比例 */
	private Double businessPct;
	
	/**字段名：销售人员姓名 */
	private String salesName;
	
	/**字段名：是否有中介机构 */
	private String salesAgencyFlag;
	
	/**字段名：中介机构销售渠道 */
	private String salesChannelAgency;
	
	/**字段名：中介机构机构号 */
	private String branchNoAgency;
	
	/**字段名：中介机构网点号 */
	private String workSiteNoAgency;
	
	/**字段名：中介机构网点名称 */
	private String workSiteNameAgency;
	
	/**字段名：中介机构销售机构号 */
	private String salesBranchNoAgency;
	
	/**字段名：中介机构销售机构员工号 */
	private String salesCodeAgency;
	
	/**字段名：中介机构销售机构员工姓名 */
	private String salesNameAgency;
	
	/**字段名：代理员工号 */
	private String agencyNo;
	
	/**字段名：代理员工姓名 */
	private String agencyName;

	/**
	 * @return the jointFieldWorkFlag
	 */
	public String getJointFieldWorkFlag() {
		return jointFieldWorkFlag;
	}

	/**
	 * @param jointFieldWorkFlag the jointFieldWorkFlag to set
	 */
	public void setJointFieldWorkFlag(String jointFieldWorkFlag) {
		this.jointFieldWorkFlag = jointFieldWorkFlag;
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
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
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}

	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	/**
	 * @return the worksiteNo
	 */
	public String getWorksiteNo() {
		return worksiteNo;
	}

	/**
	 * @param worksiteNo the worksiteNo to set
	 */
	public void setWorksiteNo(String worksiteNo) {
		this.worksiteNo = worksiteNo;
	}

	/**
	 * @return the worksiteName
	 */
	public String getWorksiteName() {
		return worksiteName;
	}

	/**
	 * @param worksiteName the worksiteName to set
	 */
	public void setWorksiteName(String worksiteName) {
		this.worksiteName = worksiteName;
	}

	/**
	 * @return the businessPct
	 */
	public Double getBusinessPct() {
		return businessPct;
	}

	/**
	 * @param businessPct the businessPct to set
	 */
	public void setBusinessPct(Double businessPct) {
		this.businessPct = businessPct;
	}

	/**
	 * @return the salesName
	 */
	public String getSalesName() {
		return salesName;
	}

	/**
	 * @param salesName the salesName to set
	 */
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	/**
	 * @return the salesAgencyFlag
	 */
	public String getSalesAgencyFlag() {
		return salesAgencyFlag;
	}

	/**
	 * @param salesAgencyFlag the salesAgencyFlag to set
	 */
	public void setSalesAgencyFlag(String salesAgencyFlag) {
		this.salesAgencyFlag = salesAgencyFlag;
	}

	/**
	 * @return the salesChannelAgency
	 */
	public String getSalesChannelAgency() {
		return salesChannelAgency;
	}

	/**
	 * @param salesChannelAgency the salesChannelAgency to set
	 */
	public void setSalesChannelAgency(String salesChannelAgency) {
		this.salesChannelAgency = salesChannelAgency;
	}

	/**
	 * @return the branchNoAgency
	 */
	public String getBranchNoAgency() {
		return branchNoAgency;
	}

	/**
	 * @param branchNoAgency the branchNoAgency to set
	 */
	public void setBranchNoAgency(String branchNoAgency) {
		this.branchNoAgency = branchNoAgency;
	}

	/**
	 * @return the workSiteNoAgency
	 */
	public String getWorkSiteNoAgency() {
		return workSiteNoAgency;
	}

	/**
	 * @param workSiteNoAgency the workSiteNoAgency to set
	 */
	public void setWorkSiteNoAgency(String workSiteNoAgency) {
		this.workSiteNoAgency = workSiteNoAgency;
	}

	/**
	 * @return the workSiteNameAgency
	 */
	public String getWorkSiteNameAgency() {
		return workSiteNameAgency;
	}

	/**
	 * @param workSiteNameAgency the workSiteNameAgency to set
	 */
	public void setWorkSiteNameAgency(String workSiteNameAgency) {
		this.workSiteNameAgency = workSiteNameAgency;
	}

	/**
	 * @return the salesBranchNoAgency
	 */
	public String getSalesBranchNoAgency() {
		return salesBranchNoAgency;
	}

	/**
	 * @param salesBranchNoAgency the salesBranchNoAgency to set
	 */
	public void setSalesBranchNoAgency(String salesBranchNoAgency) {
		this.salesBranchNoAgency = salesBranchNoAgency;
	}

	/**
	 * @return the salesCodeAgency
	 */
	public String getSalesCodeAgency() {
		return salesCodeAgency;
	}

	/**
	 * @param salesCodeAgency the salesCodeAgency to set
	 */
	public void setSalesCodeAgency(String salesCodeAgency) {
		this.salesCodeAgency = salesCodeAgency;
	}

	/**
	 * @return the salesNameAgency
	 */
	public String getSalesNameAgency() {
		return salesNameAgency;
	}

	/**
	 * @param salesNameAgency the salesNameAgency to set
	 */
	public void setSalesNameAgency(String salesNameAgency) {
		this.salesNameAgency = salesNameAgency;
	}

	/**
	 * @return the agencyNo
	 */
	public String getAgencyNo() {
		return agencyNo;
	}

	/**
	 * @param agencyNo the agencyNo to set
	 */
	public void setAgencyNo(String agencyNo) {
		this.agencyNo = agencyNo;
	}

	/**
	 * @return the agencyName
	 */
	public String getAgencyName() {
		return agencyName;
	}

	/**
	 * @param agencyName the agencyName to set
	 */
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegSalesListFormVo [jointFieldWorkFlag=" + jointFieldWorkFlag + ", salesChannel=" + salesChannel
				+ ", salesBranchNo=" + salesBranchNo + ", salesNo=" + salesNo + ", worksiteNo=" + worksiteNo
				+ ", worksiteName=" + worksiteName + ", businessPct=" + businessPct + ", salesName=" + salesName
				+ ", salesAgencyFlag=" + salesAgencyFlag + ", salesChannelAgency=" + salesChannelAgency
				+ ", branchNoAgency=" + branchNoAgency + ", workSiteNoAgency=" + workSiteNoAgency
				+ ", workSiteNameAgency=" + workSiteNameAgency + ", salesBranchNoAgency=" + salesBranchNoAgency
				+ ", salesCodeAgency=" + salesCodeAgency + ", salesNameAgency=" + salesNameAgency + ", agencyNo="
				+ agencyNo + ", agencyName=" + agencyName + "]";
	}
}
