package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;

/**
 * 展业信息
 * @author jincong
 *
 */
public class GrpSalesListFormVo implements Serializable{

	private static final long serialVersionUID = 13242344403L;
	
	/**字段名：主副标记*/
	private String jointFieldWorkFlag;
	
	/**字段名：销售渠道*/
	private String salesChannel;
	
	/**字段名：销售机构代码*/
	private String salesBranchNo;
	
	/**字段名：销售人员代码*/
	private String saleCode;
	
	/**字段名：代理网点号*/
	private String worksiteNo;
	
	/**字段名：网点名称*/
	private String worksiteName;
	
	/**字段名：展业比例 */
	private Double businessPct;
	
	/**字段名：销售人员姓名 */
	private String saleName;
	
    /**代理员工号*/
    private String agencyNo;
    
    /**代理员姓名*/
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
     * @return the saleCode
     */
    public String getSaleCode() {
        return saleCode;
    }

    /**
     * @param saleCode the saleCode to set
     */
    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    /**
     * @return the saleName
     */
    public String getSaleName() {
        return saleName;
    }

    /**
     * @param saleName the saleName to set
     */
    public void setSaleName(String saleName) {
        this.saleName = saleName;
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
		return "GrpSalesListFormVo [jointFieldWorkFlag=" + jointFieldWorkFlag + ", salesChannel=" + salesChannel
				+ ", salesBranchNo=" + salesBranchNo + ", saleCode=" + saleCode + ", worksiteNo=" + worksiteNo
				+ ", worksiteName=" + worksiteName + ", businessPct=" + businessPct + ", saleName=" + saleName
				+ ", agencyNo=" + agencyNo + ", agencyName=" + agencyName + "]";
	}



}
