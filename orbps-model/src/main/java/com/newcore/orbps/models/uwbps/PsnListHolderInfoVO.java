package com.newcore.orbps.models.uwbps;

/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 个人汇交信息
 * @author huanghaiyang
 * 创建时间：2016年7月29日
 */
public class PsnListHolderInfoVO implements Serializable{
	private static final long serialVersionUID = 2512090873429332438L;
	/**
	 * 
	 */

	/* 字段名：汇交人客户号 */
	private String sgCustNo;

	//汇交人客户号
	@NotNull(message = "汇交人客户号不能为空")
	private long sgPartyId;
 

	/* 字段名：汇交人姓名，长度：200，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人姓名不能为空")
	private String sgName;//

	/* 字段名：汇交人性别，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人性别不能为空")
	private String sgSex;

	/* 字段名：汇交人出生日期，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人出生日期不能为空")
	private Date sgBirthDate;

	/* 字段名：汇交人证件类型，长度：1，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人证件类型不能为空")
	private String sgIdType;

	/* 字段名：汇交人证件号码，长度：18，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人证件类型不能为空")
	private String sgIdNo;

	//汇交人邮政编码
	@NotNull(message = "汇交人邮政编码不能为空")
	private String postCode;

	/* 字段名：汇交人移动电话，长度：32，是否必填：IF telephone == NULL THEN 必填 */
	private String sgMobile;

	/* 字段名：汇交人邮箱，长度：64，是否必填：IF 清汇 && 个人汇交人 THEN 必填 */
	@NotNull(message = "汇交人邮箱不能为空")
	private String sgEmail;

	/* 字段名：汇交人固定电话，长度：30，是否必填：IF mobile == NULL THEN 必填 */
	private String sgTelephone;

	/* 字段名：传真，长度：15，是否必填：否 */
	private String fax;

	//省/自治州
	private String province;

	//市/州
	@NotNull(message = "市/州不能为空")
	private String city;

	//  区/县
	@NotNull(message = "区/县不能为空")
	private String county;

	//镇/乡
	private String town;

	//村/社区
	private String village;

	//地址明细
	@NotNull(message = "汇交人地址明细必填")
	private String homeAddress;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSgCustNo() {
		return sgCustNo;
	}

	public void setSgCustNo(String psnCustNo) {
		this.sgCustNo = psnCustNo;
	}
 
	public long getSgPartyId() {
		return sgPartyId;
	}

	public void setSgPartyId(long psnPartyId) {
		this.sgPartyId = psnPartyId;
	}

 
	public String getSgName() {
		return sgName;
	}

	public void setSgName(String psnName) {
		this.sgName = psnName;
	}

	public String getSgSex() {
		return sgSex;
	}

	public void setSgSex(String psnSex) {
		this.sgSex = psnSex;
	}

	public Date getSgBirthDate() {
		return sgBirthDate;
	}

	public void setSgBirthDate(Date psnBirthDate) {
		this.sgBirthDate = psnBirthDate;
	}

	public String getSgIdType() {
		return sgIdType;
	}

	public void setSgIdType(String psnIdType) {
		this.sgIdType = psnIdType;
	}

	public String getSgIdNo() {
		return sgIdNo;
	}

	public void setSgIdNo(String psnIdNo) {
		this.sgIdNo = psnIdNo;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getSgMobile() {
		return sgMobile;
	}

	public void setSgMobile(String psnMobile) {
		this.sgMobile = psnMobile;
	}

	public String getSgEmail() {
		return sgEmail;
	}

	public void setSgEmail(String psnEmail) {
		this.sgEmail = psnEmail;
	}

	public String getSgTelephone() {
		return sgTelephone;
	}

	public void setSgTelephone(String psnTelephone) {
		this.sgTelephone = psnTelephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
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

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String home) {
		this.homeAddress = home;
	}
}
