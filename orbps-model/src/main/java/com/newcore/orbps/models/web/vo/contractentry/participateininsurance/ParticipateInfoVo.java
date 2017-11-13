package com.newcore.orbps.models.web.vo.contractentry.participateininsurance;

import java.io.Serializable;


/**
 * 保单信息    
 * @author wangyanjie
 *
 */

public class ParticipateInfoVo implements Serializable {

	private static final long serialVersionUID = 6876010335278397088L;

	/** 保单号 */
	private String cntrNo;
	/** 单位名称 */
	private String companyName;
	/** 单件保单人数 */
	private Integer perPolicyNum;
	/** 备注 */
	private String remark;
	/**
	 * @return the cntrNo
	 */
	public String getCntrNo() {
		return cntrNo;
	}
	/**
	 * @param cntrNo the cntrNo to set
	 */
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the perPolicyNum
	 */
	
	public Integer getPerPolicyNum() {
		return perPolicyNum;
	}
	/**
	 * @param perPolicyNum  the perPolicyNum  to set
	 */
	
	public void setPerPolicyNum(Integer perPolicyNum) {
		this.perPolicyNum = perPolicyNum;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParticipateInfo [cntrNo=" + cntrNo + ", companyName=" + companyName + ", perPolicyNum =" + perPolicyNum 
				+ ", remark=" + remark + "]";
	}
	
	
}
