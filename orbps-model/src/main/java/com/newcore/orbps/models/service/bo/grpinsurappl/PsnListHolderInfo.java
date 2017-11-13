package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人汇交信息
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午10:50:35
 */
public class PsnListHolderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1588276161465039291L;
	
	/*汇交人cmds客户号,长度：25,是否必填：否*/
	private String sgPartyId;
	/* 字段名：汇交人客户号 */
	private String sgCustNo;

	/* 字段名：汇交人姓名，长度：200，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sgName;

	/* 字段名：汇交人性别，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sgSex;

	/* 字段名：汇交人出生日期，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private Date sgBirthDate;

	/* 字段名：汇交人证件类型，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sgIdType;

	/* 字段名：汇交人证件号码，长度：18，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sgIdNo;

	/* 字段名：汇交人移动电话，长度：32，是否必填：IF telephone == NULL THEN 必填 */
	private String sgMobile;

	/* 字段名：汇交人邮箱，长度：64，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sgEmail;

	/* 字段名：汇交人固定电话，长度：30，是否必填：IF mobile == NULL THEN 必填 */
	private String sgTelephone;

	/* 字段名：传真，长度：15，是否必填：否 */
	private String fax;
	
	// 地址
	private Address address;

	/**
	 * @return the sgPartyId
	 */
	public String getSgPartyId() {
		return sgPartyId;
	}

	/**
	 * @param sgPartyId the sgPartyId to set
	 */
	public void setSgPartyId(String sgPartyId) {
		this.sgPartyId = sgPartyId;
	}

	/**
	 * @return the sgCustNo
	 */
	public String getSgCustNo() {
		return sgCustNo;
	}

	/**
	 * @param sgCustNo the sgCustNo to set
	 */
	public void setSgCustNo(String sgCustNo) {
		this.sgCustNo = sgCustNo;
	}

	/**
	 * @return the sgName
	 */
	public String getSgName() {
		return sgName;
	}

	/**
	 * @param sgName the sgName to set
	 */
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}

	/**
	 * @return the sgSex
	 */
	public String getSgSex() {
		return sgSex;
	}

	/**
	 * @param sgSex the sgSex to set
	 */
	public void setSgSex(String sgSex) {
		this.sgSex = sgSex;
	}

	/**
	 * @return the sgBirthDate
	 */
	public Date getSgBirthDate() {
		return sgBirthDate;
	}

	/**
	 * @param sgBirthDate the sgBirthDate to set
	 */
	public void setSgBirthDate(Date sgBirthDate) {
		this.sgBirthDate = sgBirthDate;
	}

	/**
	 * @return the sgIdType
	 */
	public String getSgIdType() {
		return sgIdType;
	}

	/**
	 * @param sgIdType the sgIdType to set
	 */
	public void setSgIdType(String sgIdType) {
		this.sgIdType = sgIdType;
	}

	/**
	 * @return the sgIdNo
	 */
	public String getSgIdNo() {
		return sgIdNo;
	}

	/**
	 * @param sgIdNo the sgIdNo to set
	 */
	public void setSgIdNo(String sgIdNo) {
		this.sgIdNo = sgIdNo;
	}

	/**
	 * @return the sgMobile
	 */
	public String getSgMobile() {
		return sgMobile;
	}

	/**
	 * @param sgMobile the sgMobile to set
	 */
	public void setSgMobile(String sgMobile) {
		this.sgMobile = sgMobile;
	}

	/**
	 * @return the sgEmail
	 */
	public String getSgEmail() {
		return sgEmail;
	}

	/**
	 * @param sgEmail the sgEmail to set
	 */
	public void setSgEmail(String sgEmail) {
		this.sgEmail = sgEmail;
	}

	/**
	 * @return the sgTelephone
	 */
	public String getSgTelephone() {
		return sgTelephone;
	}

	/**
	 * @param sgTelephone the sgTelephone to set
	 */
	public void setSgTelephone(String sgTelephone) {
		this.sgTelephone = sgTelephone;
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

}
