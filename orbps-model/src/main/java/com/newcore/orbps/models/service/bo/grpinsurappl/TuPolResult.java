package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 核保结论
 * @author wangxiao
 * 创建时间：2016年7月27日上午9:45:52
 */
public class TuPolResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2626058749026966386L;

	/*字段名：核保结论，长度：2，是否必填：否*/
	private String udwResult;
	
	/*字段名：加费金额，是否必填：否*/
	private Double addFeeAmnt;

	/*字段名：加费期，是否必填：否*/
	private Integer addFeeYear;

	/*字段名：核保意见，长度：2000，是否必填：否*/
	private String udwOpinion;

	/**
	 * 
	 */
	public TuPolResult() {
		super();
	}

	/**
	 * @return the udwResult
	 */
	public String getUdwResult() {
		return udwResult;
	}

	/**
	 * @param udwResult the udwResult to set
	 */
	public void setUdwResult(String udwResult) {
		this.udwResult = udwResult;
	}

	/**
	 * @return the addFeeAmnt
	 */
	public Double getAddFeeAmnt() {
		return addFeeAmnt;
	}

	/**
	 * @param addFeeAmnt the addFeeAmnt to set
	 */
	public void setAddFeeAmnt(Double addFeeAmnt) {
		this.addFeeAmnt = addFeeAmnt;
	}

	/**
	 * @return the addFeeYear
	 */
	public Integer getAddFeeYear() {
		return addFeeYear;
	}

	/**
	 * @param addFeeYear the addFeeYear to set
	 */
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
