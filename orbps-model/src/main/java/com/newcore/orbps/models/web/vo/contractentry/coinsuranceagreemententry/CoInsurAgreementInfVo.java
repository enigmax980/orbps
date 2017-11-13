package com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry;

import java.io.Serializable;
import java.util.Date;

/**
 * 共保协议信息  
 * @author wangyanjie
 *
 */

public class CoInsurAgreementInfVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000000L;
	/** 交易标志 */
    private String transFlag;
	/** 共保协议号 */
	private String agreementNo;
	/** 协议名称  */
	private String agreementName;
	/** 管理机构*/
	private String mgrBranchNo;
	/** 是否包含下级机构 */
    private String findSubBranchNoFlag;
	/** 协议签署日期   */
	private Date applDate;
	/** 协议有效开始时间 */
	private Date inforceDate;
	/** 协议有效终止时间 */
	private Date termDate;
	
	
	/**
     * @return the transFlag
     */
    public String getTransFlag() {
        return transFlag;
    }
    /**
     * @param transFlag the transFlag to set
     */
    public void setTransFlag(String transFlag) {
        this.transFlag = transFlag;
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
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	/**
	 * @return the applDate
	 */
	public Date getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}
	/**
	 * @return the inforceDate
	 */
	public Date getInforceDate() {
		return inforceDate;
	}
	/**
	 * @param inforceDate the inforceDate to set
	 */
	public void setInforceDate(Date inforceDate) {
		this.inforceDate = inforceDate;
	}
	/**
	 * @return the termDate
	 */
	public Date getTermDate() {
		return termDate;
	}
	/**
	 * @param termDate the termDate to set
	 */
	public void setTermDate(Date termDate) {
		this.termDate = termDate;
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
        return "CoInsurAgreementInfVo [transFlag=" + transFlag + ", agreementNo=" + agreementNo + ", agreementName="
                + agreementName + ", mgrBranchNo=" + mgrBranchNo + ", findSubBranchNoFlag=" + findSubBranchNoFlag
                + ", applDate=" + applDate + ", inforceDate=" + inforceDate + ", termDate=" + termDate + "]";
    }
	
}
