package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

/**
 * @author wangxiao
 * 创建时间：2016年8月24日下午7:47:01
 */
public class TuIpsnResutlt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130061566696291283L;
	
	/*字段名：投保单号，长度：16*/
	private String applNo;
	
	/*字段名：被保人序号，长度：1 ，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String ipsnNo;
	
	/*字段名：险种代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String polCode;

	/*字段名：核保结论，长度：2*/
	private String udwResult;

	/*字段名：加费金额*/
	private Double addFeeAmnt;

	/*字段名：加费期*/
	private Integer addFeeYear;

	/*字段名：特约内容，长度：2000*/
	private String convention;

	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}

	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	/**
	 * @return the ipsnNo
	 */
	public String getIpsnNo() {
		return ipsnNo;
	}

	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(String ipsnNo) {
		this.ipsnNo = ipsnNo;
	}

	/**
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}

	/**
	 * @param polCode the polCode to set
	 */
	public void setPolCode(String polCode) {
		this.polCode = polCode;
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
	 * @return the convention
	 */
	public String getConvention() {
		return convention;
	}

	/**
	 * @param convention the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}

}
