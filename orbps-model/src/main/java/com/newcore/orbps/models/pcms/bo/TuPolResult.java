package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
	@Length(max=2,message="[核保结论长度不能大于2位]")
	private String udwResult;
	
	/*字段名：加费金额，是否必填：否*/
	@NotNull(message="[投保人S法健康加费（金额/千分率）不能为空]")
	private Double addFeeAmnt;

	/*字段名：加费期，是否必填：否*/
	@NotNull(message="[投保人S法健康加费年数不能为空]")
	private Double addFeeYear;

	/*字段名：核保意见，长度：2000，是否必填：否*/
	private String udwOption;

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
	public Double getAddFeeYear() {
		return addFeeYear;
	}

	/**
	 * @param addFeeYear the addFeeYear to set
	 */
	public void setAddFeeYear(Double addFeeYear) {
		this.addFeeYear = addFeeYear;
	}

	/**
	 * @return the udwOption
	 */
	public String getUdwOption() {
		return udwOption;
	}

	/**
	 * @param udwOption the udwOption to set
	 */
	public void setUdwOption(String udwOption) {
		this.udwOption = udwOption;
	}

}
