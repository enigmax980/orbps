package com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry;

import java.io.Serializable;

/**
 * 客户信息 
 * @author wangyanjie 
 *
 */
public class CoInsurCustomerVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000002L;
	/** 客户名称 */
	private String name;
	/**  证件类别  */
	private String idType;
	/** 证件号码*/
	private String idNo;
	/** 行业性质   */
	private String enterpriseNature;
	/** 职业类别   */
	private String occClassCode;
	/** 传真*/
	private String fax;
	/** 邮政编码 */
	private String zipCode;
	/** 联系人姓名 */
	private String contactPsn;
	/** 联系人电话 */
	private String contactTel;
	/** 联系人邮箱 */
	private String email;
	/** 省*/
	private String province;
	/** 市 */
	private String city;
	/** 县 */
	private String county;
	/** 乡镇 */
	private String town;
	/** 村 */
	private String village;
	/** 详细地址 */
	private String homeAddress;
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
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}
	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}
	/**
	 * @return the village
	 */
	public String getVillage() {
		return village;
	}
	/**
	 * @param village the village to set
	 */
	public void setVillage(String village) {
		this.village = village;
	}
	/**
	 * @return the homeAddress
	 */
	public String getHomeAddress() {
		return homeAddress;
	}
	/**
	 * @param homeAddress the homeAddress to set
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoInsurCustomerVo [name=" + name + ", idType=" + idType + ", idNo=" + idNo + ", enterpriseNature="
				+ enterpriseNature + ", occClassCode=" + occClassCode + ", fax=" + fax + ", zipCode=" + zipCode
				+ ", contactPsn=" + contactPsn + ", contactTel=" + contactTel + ", email=" + email + ", province="
				+ province + ", city=" + city + ", county=" + county + ", town=" + town + ", village=" + village
				+ ", homeAddress=" + homeAddress + "]";
	}
}
