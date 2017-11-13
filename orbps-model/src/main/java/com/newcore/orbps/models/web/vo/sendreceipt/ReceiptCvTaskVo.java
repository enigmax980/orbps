package com.newcore.orbps.models.web.vo.sendreceipt;

import java.io.Serializable;
import java.util.Date;

public class ReceiptCvTaskVo implements Serializable{
	
	private static final long serialVersionUID = 14344366551L;
	/** 汇交申请号 */
	private String sgApplNo;
	/** 投保单号 */
	private String applNo;
	/** 合同组号 */
	private String cgNo;
	/** 合同号 */
	private String cntrNo;
	/** 汇缴事件号 */
	private String sgNo;
	/** 核销状态 */
	private String status;
	/** 回执核销操作时间 */
	private String respDate;
	/** 保单类别 */
	private String cntrType;
	/** 系统类型 */
	private String sysType;
	/** 管理机构号 */
	private String mgBranchNo;
	/** 管理机构名称 */
	private String mgBranchName;
	/** 受理机构 */
	private String accBranch;
	/** 是否附有清单 */
	private String attachedFlag;
	/** 险种代码 */
	private String polCode;
	/** 受理网点 */
	private long accDept;
	/** 回执核销操作员机构号*/
	private String oclkBranchNo;
	/** 回执核销操作员工号 */
	private String oclkClerkNo;
	/** 回执核销操作员姓名 */
	private String oclkClerkName;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构号 */
	private String salesBranchNo;
	/** 销售员号 */
	private String salesNo;
	/** 销售员姓名 */
	private String salesName;
	/** 合同送达日期 */
    private  String signDate;
    /** 签收方式 */
    private String signType;
    /** 合同生效日期 */
    private String inForceDate;
    /** 合同预计满期日期 */
    private Date cntrExpiryDate;
    /** 建议回访时间 W-工作日，S-非工作日 */
    private String returnVisitType;
    /** 是否超时 */
    private String timeOut;
    /** 超时天数 */
    private long timeOutDay;
    /** 允许超时天数 */
    private long timeOutDayLimit;
    /** 回执核销起期*/
    private Date bgForceDate;
    /** 回执核销止期	 */
    private Date edForceDate;
    /** 批次号 */
	private String batchNo;
    /**担保人（汇交人）*/
    private String sgName;
    /**投保申请日期*/
    private String applDate;
    /**打印时间*/
    private String printDate;
    /**合同送达方式 */
    private String cntrSendType;
	/**
	 * @return the sgApplNo
	 */
	public String getSgApplNo() {
		return sgApplNo;
	}
	/**
	 * @param sgApplNo the sgApplNo to set
	 */
	public void setSgApplNo(String sgApplNo) {
		this.sgApplNo = sgApplNo;
	}
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
	 * @return the cgNo
	 */
	public String getCgNo() {
		return cgNo;
	}
	/**
	 * @param cgNo the cgNo to set
	 */
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	/**
	 * @return the cntrNo
	 */
	public String getCntrNo() {
		return cntrNo;
	}
	/**
	 * @param cntrNo the cntrNo to set
	 */
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	/**
	 * @return the sgNo
	 */
	public String getSgNo() {
		return sgNo;
	}
	/**
	 * @param sgNo the sgNo to set
	 */
	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the respDate
	 */
	public String getRespDate() {
		return respDate;
	}
	/**
	 * @param respDate the respDate to set
	 */
	public void setRespDate(String respDate) {
		this.respDate = respDate;
	}
	/**
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}
	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	/**
	 * @return the sysType
	 */
	public String getSysType() {
		return sysType;
	}
	/**
	 * @param sysType the sysType to set
	 */
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	/**
	 * @return the mgBranchNo
	 */
	public String getMgBranchNo() {
		return mgBranchNo;
	}
	/**
	 * @param mgBranchNo the mgBranchNo to set
	 */
	public void setMgBranchNo(String mgBranchNo) {
		this.mgBranchNo = mgBranchNo;
	}
	/**
	 * @return the mgBranchName
	 */
	public String getMgBranchName() {
		return mgBranchName;
	}
	/**
	 * @param mgBranchName the mgBranchName to set
	 */
	public void setMgBranchName(String mgBranchName) {
		this.mgBranchName = mgBranchName;
	}
	/**
	 * @return the accBranch
	 */
	public String getAccBranch() {
		return accBranch;
	}
	/**
	 * @param accBranch the accBranch to set
	 */
	public void setAccBranch(String accBranch) {
		this.accBranch = accBranch;
	}
	/**
	 * @return the attachedFlag
	 */
	public String getAttachedFlag() {
		return attachedFlag;
	}
	/**
	 * @param attachedFlag the attachedFlag to set
	 */
	public void setAttachedFlag(String attachedFlag) {
		this.attachedFlag = attachedFlag;
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
	 * @return the accDept
	 */
	public long getAccDept() {
		return accDept;
	}
	/**
	 * @param accDept the accDept to set
	 */
	public void setAccDept(long accDept) {
		this.accDept = accDept;
	}
	/**
	 * @return the oclkBranchNo
	 */
	public String getOclkBranchNo() {
		return oclkBranchNo;
	}
	/**
	 * @param oclkBranchNo the oclkBranchNo to set
	 */
	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}
	/**
	 * @return the oclkClerkNo
	 */
	public String getOclkClerkNo() {
		return oclkClerkNo;
	}
	/**
	 * @param oclkClerkNo the oclkClerkNo to set
	 */
	public void setOclkClerkNo(String oclkClerkNo) {
		this.oclkClerkNo = oclkClerkNo;
	}
	/**
	 * @return the oclkClerkName
	 */
	public String getOclkClerkName() {
		return oclkClerkName;
	}
	/**
	 * @param oclkClerkName the oclkClerkName to set
	 */
	public void setOclkClerkName(String oclkClerkName) {
		this.oclkClerkName = oclkClerkName;
	}
	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}
	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
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
	/**
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}
	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}
	/**
	 * @return the salesName
	 */
	public String getSalesName() {
		return salesName;
	}
	/**
	 * @param salesName the salesName to set
	 */
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	/**
	 * @return the signDate
	 */
	public String getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the signType
	 */
	public String getSignType() {
		return signType;
	}
	/**
	 * @param signType the signType to set
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}
	/**
	 * @return the inForceDate
	 */
	public String getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(String inForceDate) {
		this.inForceDate = inForceDate;
	}
	/**
	 * @return the cntrExpiryDate
	 */
	public Date getCntrExpiryDate() {
		return cntrExpiryDate;
	}
	/**
	 * @param cntrExpiryDate the cntrExpiryDate to set
	 */
	public void setCntrExpiryDate(Date cntrExpiryDate) {
		this.cntrExpiryDate = cntrExpiryDate;
	}
	/**
	 * @return the returnVisitType
	 */
	public String getReturnVisitType() {
		return returnVisitType;
	}
	/**
	 * @param returnVisitType the returnVisitType to set
	 */
	public void setReturnVisitType(String returnVisitType) {
		this.returnVisitType = returnVisitType;
	}
	/**
	 * @return the timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}
	/**
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	/**
	 * @return the timeOutDay
	 */
	public long getTimeOutDay() {
		return timeOutDay;
	}
	/**
	 * @param timeOutDay the timeOutDay to set
	 */
	public void setTimeOutDay(long timeOutDay) {
		this.timeOutDay = timeOutDay;
	}
	/**
	 * @return the timeOutDayLimit
	 */
	public long getTimeOutDayLimit() {
		return timeOutDayLimit;
	}
	/**
	 * @param timeOutDayLimit the timeOutDayLimit to set
	 */
	public void setTimeOutDayLimit(long timeOutDayLimit) {
		this.timeOutDayLimit = timeOutDayLimit;
	}
	/**
	 * @return the bgForceDate
	 */
	public Date getBgForceDate() {
		return bgForceDate;
	}
	/**
	 * @param bgForceDate the bgForceDate to set
	 */
	public void setBgForceDate(Date bgForceDate) {
		this.bgForceDate = bgForceDate;
	}
	/**
	 * @return the edForceDate
	 */
	public Date getEdForceDate() {
		return edForceDate;
	}
	/**
	 * @param edForceDate the edForceDate to set
	 */
	public void setEdForceDate(Date edForceDate) {
		this.edForceDate = edForceDate;
	}
	/**
	 * @return the sgName
	 */
	public String getSgName() {
		return sgName;
	}
	/**
	 * @param sgName the sgName to set
	 */
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}
	/**
	 * @return the applDate
	 */
	public String getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(String applDate) {
		this.applDate = applDate;
	}
	/**
	 * @return the printDate
	 */
	public String getPrintDate() {
		return printDate;
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	/**
	 * @return the cntrSendType
	 */
	public String getCntrSendType() {
		return cntrSendType;
	}
	/**
	 * @param cntrSendType the cntrSendType to set
	 */
	public void setCntrSendType(String cntrSendType) {
		this.cntrSendType = cntrSendType;
	}
    
	/**
	 * @return the batchNo
	 */
	public String getBatchNo() {
		return batchNo;
	}
	/**
	 * @param batchNo the batchNo to set
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReceiptCvTaskVo [sgApplNo=" + sgApplNo + ", applNo=" + applNo + ", cgNo=" + cgNo + ", cntrNo=" + cntrNo
				+ ", sgNo=" + sgNo + ", status=" + status + ", respDate=" + respDate + ", cntrType=" + cntrType
				+ ", sysType=" + sysType + ", mgBranchNo=" + mgBranchNo + ", mgBranchName=" + mgBranchName
				+ ", accBranch=" + accBranch + ", attachedFlag=" + attachedFlag + ", polCode=" + polCode + ", accDept="
				+ accDept + ", oclkBranchNo=" + oclkBranchNo + ", oclkClerkNo=" + oclkClerkNo + ", oclkClerkName="
				+ oclkClerkName + ", salesChannel=" + salesChannel + ", salesBranchNo=" + salesBranchNo + ", salesNo="
				+ salesNo + ", salesName=" + salesName + ", signDate=" + signDate + ", signType=" + signType
				+ ", inForceDate=" + inForceDate + ", cntrExpiryDate=" + cntrExpiryDate + ", returnVisitType="
				+ returnVisitType + ", timeOut=" + timeOut + ", timeOutDay=" + timeOutDay + ", timeOutDayLimit="
				+ timeOutDayLimit + ", bgForceDate=" + bgForceDate + ", edForceDate=" + edForceDate + ", batchNo="
				+ batchNo + ", sgName=" + sgName + ", applDate=" + applDate + ", printDate=" + printDate
				+ ", cntrSendType=" + cntrSendType + "]";
	}
}
