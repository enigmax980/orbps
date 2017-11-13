package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 子险种
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:26:19
 */
public class SubPolicy implements Serializable{
	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = 3516713830521275965L;

	/* 字段名：子险种，长度：8，是否必填：存在子险种必填 */
	@NotNull(message="[子险种不能为空]")
	@Length(max=8,message="[子险种长度不能大于8位]")
	private String subPolCode;

	/* 字段名：险种保额，是否必填：存在子险种必填 */
	@NotNull(message="[险种保额不能为空]")
	private Double subPolAmnt;

	/* 字段名：险种实际保费，是否必填：存在子险种必填 */
	@NotNull(message="[险种实际保费不能为空]")
	private Double subPremium;

	/* 字段名：险种标准保费，是否必填：存在子险种必填 */
	@NotNull(message="[险种标准保费不能为空]")
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

	public Double getSubPolAmnt() {
		return subPolAmnt;
	}

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
