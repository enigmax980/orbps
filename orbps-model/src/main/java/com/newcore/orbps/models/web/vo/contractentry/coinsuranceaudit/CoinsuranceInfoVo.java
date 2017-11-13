package com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit;

import java.io.Serializable;
import java.util.Date;

/**
 * 共保审批基本信息
 * @author wangyanjie
 */
public class CoinsuranceInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146444543L;
	/** 协议号 */
	private String agreementNo;
	/** 协议名称 */
	private String agreementName;
	/** 协议签署日期 */
	private Date applDate;
	/** 协议有效期开始时间 */
	private Date inforceDate;
	/** 协议有效期结束时间 */
	private Date termDate;
	/** 管理机构 */
	private String mgrBranchNo;
	/** 客户名称 */
	private String customerName;
    /** 证件类型 */
    private String idType;
    /** 证件号码 */
    private String idNo;
    /** 行业性质   */
	private String enterpriseNature;
	/** 行业类别 */
	private String occClassCode;
    /** 联系人 */
    private String contactPsn;
    /** 电话号码 */
    private String contactTel;
    /** 邮编 */
    private String zipCode;
    /** 电子邮件 */
    private String email;
    /** 联系地址 */
    private String address;
    /** 传真 */
    private String fax;
    /** 协议约定 */
    private String agreement;
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
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}
	/**
	 * @param idType the idType to set
	 */
	public void setIdType(String idType) {
		this.idType = idType;
	}
	/**
	 * @return the idNo
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * @param idNo the idNo to set
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/**
	 * @return the enterpriseNature
	 */
	public String getEnterpriseNature() {
		return enterpriseNature;
	}
	/**
	 * @param enterpriseNature the enterpriseNature to set
	 */
	public void setEnterpriseNature(String enterpriseNature) {
		this.enterpriseNature = enterpriseNature;
	}
	/**
	 * @return the occClassCode
	 */
	public String getOccClassCode() {
		return occClassCode;
	}
	/**
	 * @param occClassCode the occClassCode to set
	 */
	public void setOccClassCode(String occClassCode) {
		this.occClassCode = occClassCode;
	}
	/**
	 * @return the contactPsn
	 */
	public String getContactPsn() {
		return contactPsn;
	}
	/**
	 * @param contactPsn the contactPsn to set
	 */
	public void setContactPsn(String contactPsn) {
		this.contactPsn = contactPsn;
	}
	/**
	 * @return the contactTel
	 */
	public String getContactTel() {
		return contactTel;
	}
	/**
	 * @param contactTel the contactTel to set
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the agreement
	 */
	public String getAgreement() {
		return agreement;
	}
	/**
	 * @param agreement the agreement to set
	 */
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoinsuranceInfoVo [agreementNo=" + agreementNo + ", agreementName=" + agreementName + ", applDate="
				+ applDate + ", inforceDate=" + inforceDate + ", termDate=" + termDate + ", mgrBranchNo=" + mgrBranchNo
				+ ", customerName=" + customerName + ", idType=" + idType + ", idNo=" + idNo + ", enterpriseNature="
				+ enterpriseNature + ", occClassCode=" + occClassCode + ", contactPsn=" + contactPsn + ", contactTel="
				+ contactTel + ", zipCode=" + zipCode + ", email=" + email + ", address=" + address + ", fax=" + fax
				+ ", agreement=" + agreement + "]";
	}
	
}
