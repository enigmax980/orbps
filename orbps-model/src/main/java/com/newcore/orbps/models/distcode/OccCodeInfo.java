package com.newcore.orbps.models.distcode;

import java.io.Serializable;

public class OccCodeInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 职业大类代码
	 */
	private String occClassCode;
	/**
	 * 职业子类代码
	 */
	private String occSubclsCode;
	/**
	 * 职业细类代码
	 */
	private String occCode;
	/**
	 * 职业英文名
	 */
	private String occNameEng;
	/**
	 * 职业中文名
	 */
	private String occNameChn;
	/**
	 * 职业危险系数(意外伤害职业类别)
	 */
	private String occDangerFactor;
	public String getOccClassCode() {
		return occClassCode;
	}
	public void setOccClassCode(String occClassCode) {
		this.occClassCode = occClassCode;
	}
	public String getOccSubclsCode() {
		return occSubclsCode;
	}
	public void setOccSubclsCode(String occSubclsCode) {
		this.occSubclsCode = occSubclsCode;
	}
	public String getOccCode() {
		return occCode;
	}
	public void setOccCode(String occCode) {
		this.occCode = occCode;
	}
	public String getOccNameEng() {
		return occNameEng;
	}
	public void setOccNameEng(String occNameEng) {
		this.occNameEng = occNameEng;
	}
	public String getOccNameChn() {
		return occNameChn;
	}
	public void setOccNameChn(String occNameChn) {
		this.occNameChn = occNameChn;
	}
	public String getOccDangerFactor() {
		return occDangerFactor;
	}
	public void setOccDangerFactor(String occDangerFactor) {
		this.occDangerFactor = occDangerFactor;
	}
}
