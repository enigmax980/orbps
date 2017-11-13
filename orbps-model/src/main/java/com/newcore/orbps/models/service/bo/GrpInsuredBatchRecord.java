package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 清单批次信息记录
 * 
 * @author lijifei 
 * 创建时间：2016年8月18日上午11:07:11
 */
public class GrpInsuredBatchRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -866716026447070348L;

	//投保单号
	private String 	applNo;
	//组合保单号 
	private String cgNo;
	//批次号
	private String batchNo ;
	//批次总人数  
	private String batchIpsnNum;
	//批次总保额  
	private Double batchSumFaceAmnt;
	//批次总保费    
	private Double batchSumPremium;
	//成功笔数   
	private String succNum;
	//失败笔数   
	private String failNum;
	//处理时间    
	private Date createTime;

	public GrpInsuredBatchRecord() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchIpsnNum() {
		return batchIpsnNum;
	}

	public void setBatchIpsnNum(String batchIpsnNum) {
		this.batchIpsnNum = batchIpsnNum;
	}

	public Double getBatchSumFaceAmnt() {
		return batchSumFaceAmnt;
	}

	public void setBatchSumFaceAmnt(Double batchSumFaceAmnt) {
		this.batchSumFaceAmnt = batchSumFaceAmnt;
	}

	public Double getBatchSumPremium() {
		return batchSumPremium;
	}

	public void setBatchSumPremium(Double batchSumPremium) {
		this.batchSumPremium = batchSumPremium;
	}

	public String getSuccNum() {
		return succNum;
	}

	public void setSuccNum(String succNum) {
		this.succNum = succNum;
	}

	public String getFailNum() {
		return failNum;
	}

	public void setFailNum(String failNum) {
		this.failNum = failNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
