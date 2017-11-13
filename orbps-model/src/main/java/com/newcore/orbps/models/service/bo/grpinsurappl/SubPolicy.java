package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 子险种
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:26:19
 */
public class SubPolicy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1033648543458257451L;

	/* 字段名：子险种，长度：8，是否必填：存在子险种必填 */
	private String subPolCode;

	/* 字段名：子险种名称，长度：30，是否必填：否 */
	private String subPolName;

	/* 字段名：险种保额，是否必填：存在子险种必填 */
	private Double subPolAmnt;

	/* 字段名：险种实际保费，是否必填：存在子险种必填 */
	private Double subPremium;

	/* 字段名：险种标准保费，是否必填：存在子险种必填 */
	private Double subStdPremium;

	/**
	 * 
	 */
	public SubPolicy() {
		super();
	}

	/**
	 * @return the subPolCode
	 */
	public String getSubPolCode() {
		return subPolCode;
	}

	/**
	 * @param subPolCode the subPolCode to set
	 */
	public void setSubPolCode(String subPolCode) {
		this.subPolCode = subPolCode;
	}

	/**
	 * @return the subPolName
	 */
	public String getSubPolName() {
		return subPolName;
	}

	/**
	 * @param subPolName the subPolName to set
	 */
	public void setSubPolName(String subPolName) {
		this.subPolName = subPolName;
	}

	/**
	 * @return the subPolAmnt
	 */
	public Double getSubPolAmnt() {
		return subPolAmnt;
	}

	/**
	 * @param subPolAmnt the subPolAmnt to set
	 */
	public void setSubPolAmnt(Double subPolAmnt) {
		this.subPolAmnt = subPolAmnt;
	}

	/**
	 * @return the subPremium
	 */
	public Double getSubPremium() {
		return subPremium;
	}

	/**
	 * @param subPremium the subPremium to set
	 */
	public void setSubPremium(Double subPremium) {
		this.subPremium = subPremium;
	}

	/**
	 * @return the subStdPremium
	 */
	public Double getSubStdPremium() {
		return subStdPremium;
	}

	/**
	 * @param subStdPremium the subStdPremium to set
	 */
	public void setSubStdPremium(Double subStdPremium) {
		this.subStdPremium = subStdPremium;
	}
	
}
