/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.para;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人汇交信息
 * @author huanghaiyang
 * 创建时间：2016年7月29日
 */
public class PsnListHolderInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1588276161465039291L;

	/* 字段名：汇交人客户号 */
	private String custNO;

	/* 字段名：汇交人姓名，长度：200，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String name;

	/* 字段名：汇交人性别，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String sex;

	/* 字段名：汇交人出生日期，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private Date birthDate;

	/* 字段名：汇交人证件类型，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String idType;

	/* 字段名：汇交人证件号码，长度：18，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String idNo;

	//汇交人邮政编码
	private String postCode;

	/* 字段名：汇交人移动电话，长度：32，是否必填：IF telephone == NULL THEN 必填 */
	private String mobile;

	/* 字段名：汇交人邮箱，长度：64，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	private String email;

	/* 字段名：汇交人固定电话，长度：30，是否必填：IF mobile == NULL THEN 必填 */
	private String telephone;

	/* 字段名：传真，长度：15，是否必填：否 */
	private String fax;

	//市/州
	private String city;

	//  区/县
	private String county;

	//镇/乡
	private String town;

	//村/社区
	private String village;

	//地址明细
	private String home;

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	/**
	 * 
	 */
	public PsnListHolderInfoVO() {
		super();
	}


	/**
	 * @return the custNO
	 */
	public String getCustNO() {
		return custNO;
	}

	/**
	 * @param custNO the custNO to set
	 */
	public void setCustNO(String custNO) {
		this.custNO = custNO;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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




}
