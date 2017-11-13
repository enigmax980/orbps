package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * @author wangxiao
 * 创建时间：2016年9月3日上午9:42:23
 */
public class Conventions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5251009999536344995L;
	
	/* 字段名：录入特约 */
	private String inputConv;

	/* 字段名：契约设定特约 */
	private String orSetConv;
	
	/* 字段名：核保特约 */
	private String udwConv;
	
	/* 字段名：未成人年人特约 */
	private String minorConv;
	
	/* 字段名：险种责任特约 */
	private String polConv;

	/**
	 * @return the inputConv
	 */
	public String getInputConv() {
		return inputConv;
	}

	/**
	 * @param inputConv the inputConv to set
	 */
	public void setInputConv(String inputConv) {
		this.inputConv = inputConv;
	}

	/**
	 * @return the orSetConv
	 */
	public String getOrSetConv() {
		return orSetConv;
	}

	/**
	 * @param orSetConv the orSetConv to set
	 */
	public void setOrSetConv(String orSetConv) {
		this.orSetConv = orSetConv;
	}

	/**
	 * @return the udwConv
	 */
	public String getUdwConv() {
		return udwConv;
	}

	/**
	 * @param udwConv the udwConv to set
	 */
	public void setUdwConv(String udwConv) {
		this.udwConv = udwConv;
	}

	/**
	 * @return the minorConv
	 */
	public String getMinorConv() {
		return minorConv;
	}

	/**
	 * @param minorConv the minorConv to set
	 */
	public void setMinorConv(String minorConv) {
		this.minorConv = minorConv;
	}

	/**
	 * @return the polConv
	 */
	public String getPolConv() {
		return polConv;
	}

	/**
	 * @param polConv the polConv to set
	 */
	public void setPolConv(String polConv) {
		this.polConv = polConv;
	}

}
