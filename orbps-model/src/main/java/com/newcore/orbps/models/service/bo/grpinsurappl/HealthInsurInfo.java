package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 健康险
 * @author wangxiao
 * 创建时间：2016年7月21日上午10:20:15
 */
public class HealthInsurInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 209224723099906454L;

	/*字段名：公共保额使用范围，长度：2，是否必填：健康险必填，团单特有*/
	private String comInsurAmntUse;

	/*字段名：公共保额使用形式，长度：2，是否必填：健康险必填，团单特有*/
	private String comInsurAmntType;

	/*字段名：固定公共保额，是否必填：健康险必填，团单特有*/
	private Double sumFixedAmnt;

	/*字段名：人均浮动公共保额，是否必填：健康险必填，团单特有*/
	private Double ipsnFloatAmnt;

	/*字段名：公共保费，是否必填：健康险必填，团单特有*/
	private Double comInsrPrem;

	/*字段名：人均浮动比例，是否必填：健康险必填，团单特有*/
	private Double floatInverse;

	/**
	 * 
	 */
	public HealthInsurInfo() {
		super();
	}

	/**
	 * @return the comInsurAmntUse
	 */
	public String getComInsurAmntUse() {
		return comInsurAmntUse;
	}



	/**
	 * @param comInsurAmntUse the comInsurAmntUse to set
	 */
	public void setComInsurAmntUse(String comInsurAmntUse) {
		this.comInsurAmntUse = comInsurAmntUse;
	}



	/**
	 * @return the comInsurAmntType
	 */
	public String getComInsurAmntType() {
		return comInsurAmntType;
	}



	/**
	 * @param comInsurAmntType the comInsurAmntType to set
	 */
	public void setComInsurAmntType(String comInsurAmntType) {
		this.comInsurAmntType = comInsurAmntType;
	}



	/**
	 * @return the sumFixedAmnt
	 */
	public Double getSumFixedAmnt() {
		return sumFixedAmnt;
	}



	/**
	 * @param sumFixedAmnt the sumFixedAmnt to set
	 */
	public void setSumFixedAmnt(Double sumFixedAmnt) {
		this.sumFixedAmnt = sumFixedAmnt;
	}



	/**
	 * @return the ipsnFloatAmnt
	 */
	public Double getIpsnFloatAmnt() {
		return ipsnFloatAmnt;
	}



	/**
	 * @param ipsnFloatAmnt the ipsnFloatAmnt to set
	 */
	public void setIpsnFloatAmnt(Double ipsnFloatAmnt) {
		this.ipsnFloatAmnt = ipsnFloatAmnt;
	}



	/**
	 * @return the comInsrPrem
	 */
	public Double getComInsrPrem() {
		return comInsrPrem;
	}



	/**
	 * @param comInsrPrem the comInsrPrem to set
	 */
	public void setComInsrPrem(Double comInsrPrem) {
		this.comInsrPrem = comInsrPrem;
	}



	/**
	 * @return the floatInverse
	 */
	public Double getFloatInverse() {
		return floatInverse;
	}



	/**
	 * @param floatInverse the floatInverse to set
	 */
	public void setFloatInverse(Double floatInverse) {
		this.floatInverse = floatInverse;
	}

}
