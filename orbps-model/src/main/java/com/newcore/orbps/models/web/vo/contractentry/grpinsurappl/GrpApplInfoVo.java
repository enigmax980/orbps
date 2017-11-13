package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 投保单信息
 * @author xiaoye
 *
 */
public class GrpApplInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146444543L;
	/** 投保单号 */
	private String applNo;
	/** 报价审批号 */
	private String quotaEaNo;
	/** 投保日期 */
	private Date applDate;
	/** 共保协议号 */
	private String agreementNo;
	/** 上期保单号 */
	private String oldApplNo;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构代码 */
	private String salesBranchNo;
	/** 销售机构名称 */
	private String salesBranchName;
	/** 销售员代码 */
	private String saleCode;
	/** 销售员姓名 */
	private String saleName;
	/**字段名：代理网点号*/
	private String worksiteNo;
	/**字段名：网点名称*/
	private String worksiteName;
	/** 合同号 */
	private String polNo;
	/** 新单状态 */
	private String polNoState;
	/** 生效日期 */
	private Date effectDate;
	/** 销售相关信息 */
    List<GrpSalesListFormVo> grpSalesListFormVos;
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
	 * @return the quotaEaNo
	 */
	public String getQuotaEaNo() {
		return quotaEaNo;
	}
	/**
	 * @param quotaEaNo the quotaEaNo to set
	 */
	public void setQuotaEaNo(String quotaEaNo) {
		this.quotaEaNo = quotaEaNo;
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
	 * @return the oldApplNo
	 */
	public String getOldApplNo() {
		return oldApplNo;
	}
	/**
	 * @param oldApplNo the oldApplNo to set
	 */
	public void setOldApplNo(String oldApplNo) {
		this.oldApplNo = oldApplNo;
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
	 * @return the salesBranchName
	 */
	public String getSalesBranchName() {
		return salesBranchName;
	}
	/**
	 * @param salesBranchName the salesBranchName to set
	 */
	public void setSalesBranchName(String salesBranchName) {
		this.salesBranchName = salesBranchName;
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
	 * @return the polNo
	 */
	public String getPolNo() {
		return polNo;
	}
	/**
	 * @param polNo the polNo to set
	 */
	public void setPolNo(String polNo) {
		this.polNo = polNo;
	}
	/**
	 * @return the polNoState
	 */
	public String getPolNoState() {
		return polNoState;
	}
	/**
	 * @param polNoState the polNoState to set
	 */
	public void setPolNoState(String polNoState) {
		this.polNoState = polNoState;
	}
	/**
	 * @return the effectDate
	 */
	public Date getEffectDate() {
		return effectDate;
	}
	/**
	 * @param effectDate the effectDate to set
	 */
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
    /**
     * @return the grpSalesListFormVos
     */
    public List<GrpSalesListFormVo> getGrpSalesListFormVos() {
        return grpSalesListFormVos;
    }
    /**
     * @param grpSalesListFormVos the grpSalesListFormVos to set
     */
    public void setGrpSalesListFormVos(List<GrpSalesListFormVo> grpSalesListFormVos) {
        this.grpSalesListFormVos = grpSalesListFormVos;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GrpApplInfoVo [applNo=" + applNo + ", quotaEaNo=" + quotaEaNo + ", applDate=" + applDate
                + ", agreementNo=" + agreementNo + ", oldApplNo=" + oldApplNo + ", salesChannel=" + salesChannel
                + ", salesBranchNo=" + salesBranchNo + ", salesBranchName=" + salesBranchName + ", saleCode=" + saleCode
                + ", saleName=" + saleName + ", worksiteNo=" + worksiteNo + ", worksiteName=" + worksiteName
                + ", polNo=" + polNo + ", polNoState=" + polNoState + ", effectDate=" + effectDate
                + ", grpSalesListFormVos=" + grpSalesListFormVos + "]";
    }
	


}
