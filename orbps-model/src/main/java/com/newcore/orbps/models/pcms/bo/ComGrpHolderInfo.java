package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ComGrpHolderInfo implements Serializable {

		
	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -7354111564023901013L;
	private String grpCustNo;
	/*
	 * 单位名称
	 */
	@NotNull(message="[单位名称不能为空]")
	@Length(max=200,message="[单位名称长度不能大于200位]")
	private String grpName;
	private String grpIdType;
	private String grpIdNo;
	private String occClassCode;
	private String natureCode;
	private String corpRep;
	private String contactName;
	private String contactMobile;
	private String contactTelephone;
	private String contactEmail;
	private String fax;
	private Address address;
	public String getGrpCustNo() {
		return grpCustNo;
	}
	public void setGrpCustNo(String grpCustNo) {
		this.grpCustNo = grpCustNo;
	}
	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	public String getGrpIdType() {
		return grpIdType;
	}
	public void setGrpIdType(String grpIdType) {
		this.grpIdType = grpIdType;
	}
	public String getGrpIdNo() {
		return grpIdNo;
	}
	public void setGrpIdNo(String grpIdNo) {
		this.grpIdNo = grpIdNo;
	}
	public String getOccClassCode() {
		return occClassCode;
	}
	public void setOccClassCode(String occClassCode) {
		this.occClassCode = occClassCode;
	}
	public String getNatureCode() {
		return natureCode;
	}
	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
	}
	public String getCorpRep() {
		return corpRep;
	}
	public void setCorpRep(String corpRep) {
		this.corpRep = corpRep;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getContactTelephone() {
		return contactTelephone;
	}
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "GrpHolderInfo [grpCustNo=" + grpCustNo + ", grpName=" + grpName + ", grpIdType=" + grpIdType
				+ ", grpIdNo=" + grpIdNo + ", occClassCode=" + occClassCode + ", natureCode=" + natureCode
				+ ", corpRep=" + corpRep + ", contactName=" + contactName + ", contactMobile=" + contactMobile
				+ ", contactTelephone=" + contactTelephone + ", contactEmail=" + contactEmail + ", fax=" + fax
				+ ", address=" + address + "]";
	}

}
