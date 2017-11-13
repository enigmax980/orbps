package com.newcore.orbps.models.service.bo.udw;

import java.io.Serializable;

/**
 * @author huanglong
 * @date 2016年9月5日
 * 内容:被保人险种核保结论
 */
public class UdwIpsnPolResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5971369370183605426L;
	/*
	 * 险种代码
	 */
	private String polCode;
	/*
	 * 核保结论
	 */
	private String udwResult;
	/*
	 * 加费金额
	 */
	private double addFeeAmnt;
	/*
	 * 加费期
	 */
	private Integer addFeeYear;
	/*
	 * 特约内容
	 */
	private String convention;
		
	public UdwIpsnPolResult() {
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
	public double getAddFeeAmnt() {
		return addFeeAmnt;
	}
	public void setAddFeeAmnt(double addFeeAmnt) {
		this.addFeeAmnt = addFeeAmnt;
	}
	public Integer getAddFeeYear() {
		return addFeeYear;
	}
	public void setAddFeeYear(Integer addFeeYear) {
		this.addFeeYear = addFeeYear;
	}
	public String getConvention() {
		return convention;
	}
	public void setConvention(String convention) {
		this.convention = convention;
	}
	
	
}
