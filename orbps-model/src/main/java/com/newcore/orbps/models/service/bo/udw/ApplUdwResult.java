package com.newcore.orbps.models.service.bo.udw;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author huanglong
 * @date 2016年9月5日
 * 内容: 核保信息
 */
public class ApplUdwResult implements Serializable{

	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -597950842769412590L;
	/*
	 * 投保单号== 流水号 使用时需要进行转换
	 */	
	private String businessKey;	
	/*
	 * 核保操作员机构
	 */
	private String pclkBranchNo;
	/*
	 * 核保操作员代码
	 */
	private String pclkNo;
	/*
	 * 核保操作员姓名
	 */
	private String pclkName;
	/*
	 * 核保完成日期
	 */
	private Date udwFinishDate;	
	/*
	 * 核保特约
	 */	
	private String udwConv;
	/*
	 * 整单核保结论
	 */
	private String udwResult;
	/*
	 * 险种核保结论
	 */
	private List<UdwPolResult> udwPolResults;
	/*
	 * 被保人核保信息
	 */
	private List<UdwIpsnResult> udwIpsnResults;
	
	public ApplUdwResult() {
		super();
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	/**
	 * @return the pclkNo
	 */
	public String getPclkNo() {
		return pclkNo;
	}
	/**
	 * @param pclkNo the pclkNo to set
	 */
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
	}
	public String getPclkName() {
		return pclkName;
	}
	public void setPclkName(String pclkName) {
		this.pclkName = pclkName;
	}
	public Date getUdwFinishDate() {
		return udwFinishDate;
	}
	public void setUdwFinishDate(Date udwFinishDate) {
		this.udwFinishDate = udwFinishDate;
	}

	public List<UdwPolResult> getUdwPolResults() {
		return udwPolResults;
	}

	public void setUdwPolResults(List<UdwPolResult> udwPolResults) {
		this.udwPolResults = udwPolResults;
	}



	public String getUdwConv() {
		return udwConv;
	}

	public void setUdwConv(String udwConv) {
		this.udwConv = udwConv;
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
	public List<UdwIpsnResult> getUdwIpsnResults() {
		return udwIpsnResults;
	}
	public void setUdwIpsnResults(List<UdwIpsnResult> udwIpsnResults) {
		this.udwIpsnResults = udwIpsnResults;
	}	
	
}
