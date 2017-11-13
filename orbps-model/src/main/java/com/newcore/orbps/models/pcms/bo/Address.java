package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
	@NotNull(message="[省/自治州不能为空]")
	@Length(max=80,message="[省/自治州长度不能超过80位]")
	private String province;

	/* 字段名：市/州，长度：80，是否必填：是 */
	@NotNull(message="[市/州不能为空]")
	@Length(max=80,message="[市/州长度不能超过80位]")
	private String city;

	/* 字段名：区/县，长度：80，是否必填：是 */
	@NotNull(message="[区/县不能为空]")
	@Length(max=80,message="[区/县长度不能超过80位]")
	private String county;

	/* 字段名：镇/乡，长度：80，是否必填：是 */
	@Length(max=80,message="[镇/乡长度不能超过80位]")
	private String town;

	/* 字段名：村/社区，长度：160，是否必填：是 */
	@Length(max=160,message="[村/社区长度不能超过160位]")
	private String village;

	/* 字段名：地址明细，长度：200，是否必填：是 */
	@NotNull(message="[地址明细不能为空]")
	@Length(max=200,message="[地址明细长度不能超过200位]")
	private String homeAddress;

	/* 字段名：邮政编码，长度：6，是否必填：是 */
	@NotNull(message="[邮政编码不能为空]")
	@Length(max=6,message="[邮政编码长度不能超过6位]")
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


	public String getHomeAddress() {
		return homeAddress;
	}

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
