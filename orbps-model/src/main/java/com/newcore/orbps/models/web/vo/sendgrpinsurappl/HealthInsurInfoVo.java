package com.newcore.orbps.models.web.vo.sendgrpinsurappl;

import java.io.Serializable;

public class HealthInsurInfoVo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2346055963719246425L;

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
	
	/*字段名：浮动总保额 ，是否必填：健康险必填，团单特有*/
	private Double sumFloatAmnt;

	public String getComInsurAmntUse() {
		return comInsurAmntUse;
	}

	public void setComInsurAmntUse(String comInsurAmntUse) {
		this.comInsurAmntUse = comInsurAmntUse;
	}

	public String getComInsurAmntType() {
		return comInsurAmntType;
	}

	public void setComInsurAmntType(String comInsurAmntType) {
		this.comInsurAmntType = comInsurAmntType;
	}

	public Double getSumFixedAmnt() {
		return sumFixedAmnt;
	}

	public void setSumFixedAmnt(Double sumFixedAmnt) {
		this.sumFixedAmnt = sumFixedAmnt;
	}

	public Double getIpsnFloatAmnt() {
		return ipsnFloatAmnt;
	}

	public void setIpsnFloatAmnt(Double ipsnFloatAmnt) {
		this.ipsnFloatAmnt = ipsnFloatAmnt;
	}

	public Double getComInsrPrem() {
		return comInsrPrem;
	}

	public void setComInsrPrem(Double comInsrPrem) {
		this.comInsrPrem = comInsrPrem;
	}

	public Double getFloatInverse() {
		return floatInverse;
	}

	public void setFloatInverse(Double floatInverse) {
		this.floatInverse = floatInverse;
	}

	public Double getSumFloatAmnt() {
		return sumFloatAmnt;
	}

	public void setSumFloatAmnt(Double sumFloatAmnt) {
		this.sumFloatAmnt = sumFloatAmnt;
	}
	
}
