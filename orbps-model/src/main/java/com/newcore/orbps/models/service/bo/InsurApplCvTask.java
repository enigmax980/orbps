package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

import java.util.Date;



/**
 * @author huanglong
 * @date 2016年8月19日
 * 内容:回执核销类
 */
public class InsurApplCvTask implements Serializable {
	
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
	/*
	 * 合同号
	 */
	private String cntrNo;
	/*
	 * 汇缴件号
	 */
	private String sgNo;
	/*字段名称:批次号,用于批次回执核销,长度26:,是否必填:否*/
	private String batNo;
	/**
	 * 核销状态
	 */
	private String status;
	/*字段名称:核销状态描述,用于批次回执核销,长度:255,是否必填:否*/
	private String description;
	/**
	 * 核销时间
	 */
	private Date respDate;
	/*
	 * 保单类别
	 */
	private String cntrType;
	/*
	 * 管理机构号
	 */
	private String mgBranch;
	/*
	 * 管理机构名称
	 */
	private String mgBranchName;
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
	/*
	 * 操作员机构号
	 */
	private String oclkBranchNo;
	/*
	 * 操作员工号
	 */
	private String oclkClerkNo;
	/*
	 * 操作员姓名
	 */
	private String oclkClerkName;
	/*
	 * 销售机构号
	 */
	private String salesBranchNo;
	/*
	 * 销售员号
	 */
	private String salesNo;
	/*
	 * 销售员姓名
	 */
	private String salesName;
	/*
	 * 签收日期
	 */
    private  Date signDate;
    /*
     * 签收方式
     */
    private String signType;
    /*
     * 合同生效日期
     */
    private Date inForceDate;
    /*
     * 合同预计满期日期
     */
    private Date cntrExpiryDate;
    /*
     * 建议回访时间
     */
    private String returnVisitType;
    
    /*
     * 是否超时
     */
    private String timeOut;
    /*
     * 超时天数
     */
    private long timeOutDay;
    /*
     * 允许超时天数
     */
    private long timeOutDayLimit;
   
    public InsurApplCvTask() {
		super();
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


	public String getCntrNo() {
		return cntrNo;
	}


	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}


	public String getSgNo() {
		return sgNo;
	}


	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}

	/**
	 * @return the batNo
	 */
	public String getBatNo() {
		return batNo;
	}

	/**
	 * @param batNo the batNo to set
	 */
	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	public Date getRespDate() {
		return respDate;
	}


	public void setRespDate(Date respDate) {
		this.respDate = respDate;
	}


	public String getCntrType() {
		return cntrType;
	}


	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public String getMgBranch() {
		return mgBranch;
	}


	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}


	public String getMgBranchName() {
		return mgBranchName;
	}


	public void setMgBranchName(String mgBranchName) {
		this.mgBranchName = mgBranchName;
	}


	public String getAccBranch() {
		return accBranch;
	}


	public void setAccBranch(String accBranch) {
		this.accBranch = accBranch;
	}


	public String getAttachedFlag() {
		return attachedFlag;
	}


	public void setAttachedFlag(String attachedFlag) {
		this.attachedFlag = attachedFlag;
	}


	public String getPolCode() {
		return polCode;
	}


	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public String getOclkBranchNo() {
		return oclkBranchNo;
	}


	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}


	public String getOclkClerkNo() {
		return oclkClerkNo;
	}


	public void setOclkClerkNo(String oclkClerkNo) {
		this.oclkClerkNo = oclkClerkNo;
	}


	public String getOclkClerkName() {
		return oclkClerkName;
	}


	public void setOclkClerkName(String oclkClerkName) {
		this.oclkClerkName = oclkClerkName;
	}

	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}

	public String getSalesNo() {
		return salesNo;
	}


	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	
	public String getSalesName() {
		return salesName;
	}


	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}


	public Date getSignDate() {
		return signDate;
	}


	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}


	public String getSignType() {
		return signType;
	}


	public void setSignType(String signType) {
		this.signType = signType;
	}


	public Date getInForceDate() {
		return inForceDate;
	}


	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}


	public Date getCntrExpiryDate() {
		return cntrExpiryDate;
	}


	public void setCntrExpiryDate(Date cntrExpiryDate) {
		this.cntrExpiryDate = cntrExpiryDate;
	}


	public String getReturnVisitType() {
		return returnVisitType;
	}


	public void setReturnVisitType(String returnVisitType) {
		this.returnVisitType = returnVisitType;
	}


	public String getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}


	public long getTimeOutDay() {
		return timeOutDay;
	}


	public void setTimeOutDay(long timeOutDay) {
		this.timeOutDay = timeOutDay;
	}


	public long getTimeOutDayLimit() {
		return timeOutDayLimit;
	}


	public void setTimeOutDayLimit(long timeOutDayLimit) {
		this.timeOutDayLimit = timeOutDayLimit;
	}
	
}
