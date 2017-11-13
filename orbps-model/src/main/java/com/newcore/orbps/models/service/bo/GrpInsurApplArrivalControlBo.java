package com.newcore.orbps.models.service.bo;

import java.io.Serializable;


/**
 * 保单落地信息控制
 * 
 * @author lijifei 
 * 创建时间：2016年8月17日上午11:07:11
 */
public class GrpInsurApplArrivalControlBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -866716026447070348L;

	/*
	 * 保单落地信息控制表（CNTR_EFFECT_CTRL）
	 */
	//组合保单号 
	private String cgNo;

	//团体清单标志
	private String lstProcType;

	//批次号
	private String batchNo ;

	//总人数  
	private int ipsnNum;

	//总保额  
	private Double sumFaceAmnt;
	// 总保费    
	private Double sumPremium;

	// 处理状态 
	private String procStat;

	//状态原因说明 
	private String procCauseDesc;

	//保单类型
	private String cntrType;

	//投保单号
	private String applNo;

	//险种代码
	private String polCode;

	//管理机构号
	private String mgBranch;

	//任务ID（保单生效时插入表，在清单落地成功后，用于调用任务流触发“打印”）
	private long id;

	//实收数据（在[无清单保单落地成功后]或[有清单被保人清单导入成功后]触发“实收付流水落地清单告知”服务）
	private String miologDate;



	public GrpInsurApplArrivalControlBo() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getCgNo() {
		return cgNo;
	}



	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}



	public String getLstProcType() {
		return lstProcType;
	}



	public void setLstProcType(String lstProcType) {
		this.lstProcType = lstProcType;
	}



	public String getBatchNo() {
		return batchNo;
	}



	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}



	public int getIpsnNum() {
		return ipsnNum;
	}



	public void setIpsnNum(int ipsnNum) {
		this.ipsnNum = ipsnNum;
	}



	public Double getSumFaceAmnt() {
		return sumFaceAmnt;
	}



	public void setSumFaceAmnt(Double sumFaceAmnt) {
		this.sumFaceAmnt = sumFaceAmnt;
	}



	public Double getSumPremium() {
		return sumPremium;
	}



	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}



	public String getProcStat() {
		return procStat;
	}



	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}



	public String getProcCauseDesc() {
		return procCauseDesc;
	}



	public void setProcCauseDesc(String procCauseDesc) {
		this.procCauseDesc = procCauseDesc;
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



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getMiologDate() {
		return miologDate;
	}



	public void setMiologDate(String miologDate) {
		this.miologDate = miologDate;
	}





}
