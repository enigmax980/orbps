package com.newcore.orbps.models.service.bo.udw;

import java.io.Serializable;

/**
 * @author huanglong
 * @date 2016年9月7日
 * 内容:险种核保结论
 */
public class UdwPolResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5230722236739033095L;
	/*
	 * 字段名：险种代码
	 */
	private String polCode;
	/*
	 * 字段名：核保结论，长度：2，是否必填：否
	 */
	private String udwResult;	
	/*
	 * 字段名：加费金额，是否必填：否
	 */
	private Double addFeeAmnt;

	/*
	 * 字段名：加费期，是否必填：否
	 */
	private Integer addFeeYear;

	/*
	 * 字段名：核保意见，长度：2000，是否必填：否
	 */
	private String udwOpinion;

	public UdwPolResult() {
		super();
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public String getUdwResult() {
		return udwResult;
	}

	public void setUdwResult(String udwResult) {
		this.udwResult = udwResult;
	}

	public Double getAddFeeAmnt() {
		return addFeeAmnt;
	}

	public void setAddFeeAmnt(Double addFeeAmnt) {
		this.addFeeAmnt = addFeeAmnt;
	}

	public Integer getAddFeeYear() {
		return addFeeYear;
	}

	public void setAddFeeYear(Integer addFeeYear) {
		this.addFeeYear = addFeeYear;
	}

	/**
	 * @return the udwOpinion
	 */
	public String getUdwOpinion() {
		return udwOpinion;
	}

	/**
	 * @param udwOpinion the udwOpinion to set
	 */
	public void setUdwOpinion(String udwOpinion) {
		this.udwOpinion = udwOpinion;
	}

}
