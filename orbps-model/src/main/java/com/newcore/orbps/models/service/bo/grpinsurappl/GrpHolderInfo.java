package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 团体客户信息
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:19:42
 */
public class GrpHolderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7776359775966954347L;
	
	/*投保单位cmds客户号,长度：25，是否必填：否*/
	private String partyId;

	/* 字段名：团体法人客户号 */
	private String grpCustNo;

	/* 字段名：单位名称，长度：200，是否必填：是 */
	private String grpName;

	/* 字段名：曾用名，长度：200，是否必填：是 */
	private String formerGrpName;

	/* 字段名：团体客户证件类别，长度：1，是否必填：是 */
	private String grpIdType;

	/* 字段名：团体客户证件号码，长度：18，是否必填：是 */
	private String grpIdNo;

	/* 字段名：企业注册地国籍，长度：3，是否必填：是 */
	private String grpCountryCode;

	/* 字段名：外管局部门类型，长度：2，是否必填：是 */
	private String grpPsnDeptType;

	/* 字段名：行业类别，长度：2，是否必填：是 */
	private String occClassCode;

	/* 字段名：单位性质(经济分类)，长度：2，是否必填：否 */
	private String natureCode;

	/* 字段名：单位性质（法律分类），长度：2，是否必填：否 */
	private String legalCode;

	/* 字段名：法人代表，，是否必填：否 */
	private String corpRep;

	/* 字段名：传真，长度：15，是否必填：否 */
	private String fax;

	/* 字段名：员工总数，是否必填：IF 团单 THEN 必填 */
	private Long numOfEnterprise;

	/* 字段名：在职人数，是否必填：IF 团单 THEN 必填 */
	private Long onjobStaffNum;

	/* 字段名：投保人数，是否必填：IF 团单 THEN 必填 */
	private Long ipsnNum;

	/* 字段名：联系人姓名，长度：200，是否必填：是 */
	private String contactName;

	/* 字段名：联系人证件类别，长度：1，是否必填：否 */
	private String contactIdType;

	/* 字段名：联系人证件号码，长度：18，是否必填：否 */
	private String contactIdNo;

	/* 字段名：联系人移动电话，长度：32，是否必填：IF contractTelephone == NULL THEN 必填 */
	private String contactMobile;

	/* 字段名：联系人固定电话，长度：30，是否必填：IF contractMobile == NULL THEN 必填 */
	private String contactTelephone;

	/* 字段名：联系人电子邮件，长度：64，是否必填：否 */
	private String contactEmail;

	// 地址
	private Address address;

	/*
	 * 字段名：纳税人识别号，长度：15，是否必填：否
	 */
	private String taxpayerId;
	
	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	/**
	 * @return the grpCustNo
	 */
	public String getGrpCustNo() {
		return grpCustNo;
	}

	/**
	 * @param grpCustNo the grpCustNo to set
	 */
	public void setGrpCustNo(String grpCustNo) {
		this.grpCustNo = grpCustNo;
	}

	/**
	 * @return the grpName
	 */
	public String getGrpName() {
		return grpName;
	}

	/**
	 * @param grpName the grpName to set
	 */
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	/**
	 * @return the formerGrpName
	 */
	public String getFormerGrpName() {
		return formerGrpName;
	}

	/**
	 * @param formerGrpName the formerGrpName to set
	 */
	public void setFormerGrpName(String formerGrpName) {
		this.formerGrpName = formerGrpName;
	}

	/**
	 * @return the grpIdType
	 */
	public String getGrpIdType() {
		return grpIdType;
	}

	/**
	 * @param grpIdType the grpIdType to set
	 */
	public void setGrpIdType(String grpIdType) {
		this.grpIdType = grpIdType;
	}

	/**
	 * @return the grpIdNo
	 */
	public String getGrpIdNo() {
		return grpIdNo;
	}

	/**
	 * @param grpIdNo the grpIdNo to set
	 */
	public void setGrpIdNo(String grpIdNo) {
		this.grpIdNo = grpIdNo;
	}

	/**
	 * @return the grpCountryCode
	 */
	public String getGrpCountryCode() {
		return grpCountryCode;
	}

	/**
	 * @param grpCountryCode the grpCountryCode to set
	 */
	public void setGrpCountryCode(String grpCountryCode) {
		this.grpCountryCode = grpCountryCode;
	}

	/**
	 * @return the grpPsnDeptType
	 */
	public String getGrpPsnDeptType() {
		return grpPsnDeptType;
	}

	/**
	 * @param grpPsnDeptType the grpPsnDeptType to set
	 */
	public void setGrpPsnDeptType(String grpPsnDeptType) {
		this.grpPsnDeptType = grpPsnDeptType;
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
	 * @return the natureCode
	 */
	public String getNatureCode() {
		return natureCode;
	}

	/**
	 * @param natureCode the natureCode to set
	 */
	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
	}

	/**
	 * @return the legalCode
	 */
	public String getLegalCode() {
		return legalCode;
	}

	/**
	 * @param legalCode the legalCode to set
	 */
	public void setLegalCode(String legalCode) {
		this.legalCode = legalCode;
	}

	/**
	 * @return the corpRep
	 */
	public String getCorpRep() {
		return corpRep;
	}

	/**
	 * @param corpRep the corpRep to set
	 */
	public void setCorpRep(String corpRep) {
		this.corpRep = corpRep;
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
	 * @return the numOfEnterprise
	 */
	public Long getNumOfEnterprise() {
		return numOfEnterprise;
	}

	/**
	 * @param numOfEnterprise the numOfEnterprise to set
	 */
	public void setNumOfEnterprise(Long numOfEnterprise) {
		this.numOfEnterprise = numOfEnterprise;
	}

	/**
	 * @return the onjobStaffNum
	 */
	public Long getOnjobStaffNum() {
		return onjobStaffNum;
	}

	/**
	 * @param onjobStaffNum the onjobStaffNum to set
	 */
	public void setOnjobStaffNum(Long onjobStaffNum) {
		this.onjobStaffNum = onjobStaffNum;
	}

	/**
	 * @return the ipsnNum
	 */
	public Long getIpsnNum() {
		return ipsnNum;
	}

	/**
	 * @param ipsnNum the ipsnNum to set
	 */
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactIdType
	 */
	public String getContactIdType() {
		return contactIdType;
	}

	/**
	 * @param contactIdType the contactIdType to set
	 */
	public void setContactIdType(String contactIdType) {
		this.contactIdType = contactIdType;
	}

	/**
	 * @return the contactIdNo
	 */
	public String getContactIdNo() {
		return contactIdNo;
	}

	/**
	 * @param contactIdNo the contactIdNo to set
	 */
	public void setContactIdNo(String contactIdNo) {
		this.contactIdNo = contactIdNo;
	}

	/**
	 * @return the contactMobile
	 */
	public String getContactMobile() {
		return contactMobile;
	}

	/**
	 * @param contactMobile the contactMobile to set
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	/**
	 * @return the contactTelephone
	 */
	public String getContactTelephone() {
		return contactTelephone;
	}

	/**
	 * @param contactTelephone the contactTelephone to set
	 */
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	public String getTaxpayerId() {
		return taxpayerId;
	}

	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

}