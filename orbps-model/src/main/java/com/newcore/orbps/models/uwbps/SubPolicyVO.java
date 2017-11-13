package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

/**
 * 子险种
 * @author huanghaiyang
 * 创建时间：2016年7月29日
 */
public class SubPolicyVO implements Serializable{
	private static final long serialVersionUID = 4184688965581852407L;
	/**
	 * 
	 */

	/* 字段名：子险种，长度：8，是否必填：存在子险种必填 */
	private String subPolCode;

	/* 字段名：险种保额，是否必填：存在子险种必填 */
	private Double subPolAmnt;

	/* 字段名：险种名称，是否必填：存在子险种必填 */
	private String subPolName;

	/* 字段名：险种实际保费，是否必填：存在子险种必填 */
	private Double subPremium;

	/* 字段名：险种标准保费，是否必填：存在子险种必填 */
	private Double subStdPremium;

	/**
	 * 
	 */
	public SubPolicyVO() {
		super();
	}

	/**
	 * @param subPolCode
	 * @param subPremium
	 * @param subStdPremium
	 */
	public SubPolicyVO(String subPolCode,String subPolName, Double subPolAmnt, Double subPremium, Double subStdPremium) {
		super();
		this.subPolCode = subPolCode;
		this.subPolAmnt = subPolAmnt;
		this.subPolName = subPolName;
		this.subPremium = subPremium;
		this.subStdPremium = subStdPremium;
	}

	public String getSubPolCode() {
		return subPolCode;
	}

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
	 *            the subPolName to set
	 */
	public void setSubPolName(String subPolName) {
		this.subPolName = subPolName;
	}

	/**
	 * @return the subPremium
	 */
	public Double getSubPremium() {
		return subPremium;
	}

	/**
	 * @param subPremium
	 *            the subPremium to set
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
	 * @param subStdPremium
	 *            the subStdPremium to set
	 */
	public void setSubStdPremium(Double subStdPremium) {
		this.subStdPremium = subStdPremium;
	}

	public Double getSubPolAmnt() {
		return subPolAmnt;
	}

	public void setSubPolAmnt(Double subPolAmnt) {
		this.subPolAmnt = subPolAmnt;
	}
}
