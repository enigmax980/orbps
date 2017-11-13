package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 地址
 * @author wangxiao
 * 创建时间：2016年7月20日下午11:49:18
 */
public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5259992066092424513L;

	/* 字段名：省/自治州，长度：80，是否必填：是 */
	private String province;

	/* 字段名：市/州，长度：80，是否必填：是 */
	private String city;

	/* 字段名：区/县，长度：80，是否必填：是 */
	private String county;

	/* 字段名：镇/乡，长度：80，是否必填：是 */
	private String town;

	/* 字段名：村/社区，长度：160，是否必填：是 */
	private String village;

	/* 字段名：地址明细，长度：200，是否必填：是 */
	private String homeAddress;

	/* 字段名：邮政编码，长度：6，是否必填：是 */
	private String postCode;

	/**
	 * 
	 */
	public Address() {
		super();
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

	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

}
