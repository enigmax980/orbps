package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import java.util.Date;

/**
 * 内容:回执核销类
 */
public class InsurApplCvTaskBo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2651218992188655410L;
	/*
	 * 投保单号
	 */
	private String applNo;
	/*
	 * 合同组号
	 */
	private String cgNo;
	/**
	 * 核销时间
	 */
	private Date respDate;
	/*
	 * 保单类别
	 */
	private String cntrType;
	/*
	 * 管理机构
	 */
	private String mgBranch;
	/*
	 * 受理机构
	 */
	private String accBranch;
	/*
	 * 是否附有清单
	 */
	private String attachedFlag;
	/*
	 * 险种代码
	 */
	private String polCode;


	private String dataSource;
	
    
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public InsurApplCvTaskBo() {
		super();
	}

	public String getCntrType() {
		return cntrType;
	}
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getMgBranch() {
		return mgBranch;
	}
	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	public String getAccBranch() {
		return accBranch;
	}
	public void setAccBranch(String accBranch) {
		this.accBranch = accBranch;
	}
	public Date getRespDate() {
		return respDate;
	}

	public void setRespDate(Date respDate) {
		this.respDate = respDate;
	}

	public String getAttachedFlag() {
		return attachedFlag;
	}

	public void setAttachedFlag(String attachedFlag) {
		this.attachedFlag = attachedFlag;
	}

	
}
