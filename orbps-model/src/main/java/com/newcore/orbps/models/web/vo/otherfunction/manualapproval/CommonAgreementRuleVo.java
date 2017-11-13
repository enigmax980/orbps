package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;
/***
 * 共保协议查询入参
 * 
 * @author wanghonglin
 *
 * @date 2017年2月20日 下午4:13:01
 * @TODO
 */
public class CommonAgreementRuleVo implements Serializable{
	
	private static final long serialVersionUID = 416461785889120315L;
	/**投保单号*/
	private String applNo;
	/**协议号*/
	private String agreementNo;
	/**协议号名称*/
	private String agreementName;
	/**协议创建起期*/
	private String inForceDate;
	/**协议创建止期*/
	private String termDate;
	/** 销售机构代码 */
    private String salesBranchNo;
    /** 是否包含下级机构 */
    private String findSubBranchNoFlag;
	
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
	 * @return the agreementNo
	 */
	public String getAgreementNo() {
		return agreementNo;
	}
	/**
	 * @param agreementNo the agreementNo to set
	 */
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	/**
	 * @return the agreementName
	 */
	public String getAgreementName() {
		return agreementName;
	}
	/**
	 * @param agreementName the agreementName to set
	 */
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
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
	 * @return the termDate
	 */
	public String getTermDate() {
		return termDate;
	}
	/**
	 * @param termDate the termDate to set
	 */
	public void setTermDate(String termDate) {
		this.termDate = termDate;
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
        return "CommonAgreementRuleVo [applNo=" + applNo + ", agreementNo=" + agreementNo + ", agreementName="
                + agreementName + ", inForceDate=" + inForceDate + ", termDate=" + termDate + ", salesBranchNo="
                + salesBranchNo + ", findSubBranchNoFlag=" + findSubBranchNoFlag + "]";
    }
	
	
}
