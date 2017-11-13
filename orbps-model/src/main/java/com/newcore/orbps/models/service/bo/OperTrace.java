package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * 操作轨迹
 * @author lijifei
 * 创建时间：2016年11月03日上午10:51:25
 */
public class OperTrace implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8337815526730615499L;

	/* 字段名：管理机构号，长度：6，是否必填：IF 柜面出单 THEN 必录 */
	@NotNull(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能大于6位]")
	private String mgrBranchNo;

	/* 字段名：受理操作员机构，长度：6，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=6,message="[受理操作员机构长度不能大于6位]")
	private String acceptBranchNo;

	/* 字段名：受理操作员代码，长度：8，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=8,message="[受理操作员代码长度不能大于8位]")
	private String acceptClerkNo;

	/* 字段名：受理操作员姓名，长度：200，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=200,message="[受理操作员姓名长度不能大于200位]")
	private String acceptClerkName;

	/* 字段名：业务受理时间，长度：10，是否必填：IF 柜面出单 THEN 必录 */
	private Date bizAcceptDate;

	/* 字段名：系统受理时间，长度：10，是否必填：IF 柜面出单 THEN 必录 */
	private Date sysAcceptEntDate;

	/* 字段名：录入操作员机构号，长度：6，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=6,message="[录入操作员机构号长度不能大于6位]")
	private String applEntBranchNo;

	/* 字段名：录入操作员代码，长度：8，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=8,message="[录入操作员代码不能大于8位]")
	private String applEntClerkNo;

	/* 字段名：录入操作员姓名，长度：200，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=200,message="[录入操作员姓名长度不能大于200位]")
	private String applEntClerkName;

	/* 字段名：录入时间，长度：10，是否必填：IF 柜面出单 THEN 必录 */
	private Date applEntDate;
	
	/* 字段名：复核操作员机构号，长度：6，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=6,message="[复核操作员机构号长度不能大于6位]")
	private String applChkBranchNo;
	
	/* 字段名：复核操作员代码，长度：8，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=8,message="[复核操作员代码长度不能大于8位]")
	private String applChkClerkNo;
	
	/* 字段名：复核操作员姓名，长度：200，是否必填：IF 柜面出单 THEN 必录 */
	@Length(max=200,message="[复核操作员姓名长度不能大于200位]")
	private String applChkClerkName;

	/* 字段名：复核时间，长度：10，是否必填：IF 柜面出单 THEN 必录 */
	private Date applChkDate;

	/* 字段名：新单状态，长度：2，是否必填：否 */
	@Length(max=2,message="[新单状态长度不能大于2位]")
	private String newApplState;

	/* 字段名：核保操作员机构，长度：6，是否必填：否 */
	@Length(max=6,message="[核保操作员机构长度不能大于6位]")
	private String pclkBranchNo;
	
	/* 字段名：核保操作员代码，长度：8，是否必填：否 */
	@Length(max=8,message="[核保操作员代码长度不能大于8位]")
	private String pclkClerkNo;
	
	/* 字段名：核保操作员姓名，长度：200，是否必填：否 */
	@Length(max=200,message="[核保操作员姓名长度不能大于200位]")
	private String pclkName;
	
	/* 字段名：核保完成日期，长度：200，是否必填：否 */
	private Date udwFinishDate;

	/**
	 * 
	 */
	public OperTrace() {
		super();
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getAcceptBranchNo() {
		return acceptBranchNo;
	}

	public void setAcceptBranchNo(String acceptBranchNo) {
		this.acceptBranchNo = acceptBranchNo;
	}

	public String getAcceptClerkNo() {
		return acceptClerkNo;
	}

	public void setAcceptClerkNo(String acceptClerkNo) {
		this.acceptClerkNo = acceptClerkNo;
	}

	public String getAcceptClerkName() {
		return acceptClerkName;
	}

	public void setAcceptClerkName(String acceptClerkName) {
		this.acceptClerkName = acceptClerkName;
	}

	public Date getBizAcceptDate() {
		return bizAcceptDate;
	}

	public void setBizAcceptDate(Date bizAcceptDate) {
		this.bizAcceptDate = bizAcceptDate;
	}

	public Date getSysAcceptEntDate() {
		return sysAcceptEntDate;
	}

	public void setSysAcceptEntDate(Date sysAcceptEntDate) {
		this.sysAcceptEntDate = sysAcceptEntDate;
	}

	public String getApplEntBranchNo() {
		return applEntBranchNo;
	}

	public void setApplEntBranchNo(String applEntBranchNo) {
		this.applEntBranchNo = applEntBranchNo;
	}

	public String getApplEntClerkNo() {
		return applEntClerkNo;
	}

	public void setApplEntClerkNo(String applEntClerkNo) {
		this.applEntClerkNo = applEntClerkNo;
	}

	public String getApplEntClerkName() {
		return applEntClerkName;
	}

	public void setApplEntClerkName(String applEntClerkName) {
		this.applEntClerkName = applEntClerkName;
	}

	public Date getApplEntDate() {
		return applEntDate;
	}

	public void setApplEntDate(Date applEntDate) {
		this.applEntDate = applEntDate;
	}

	public String getApplChkBranchNo() {
		return applChkBranchNo;
	}

	public void setApplChkBranchNo(String applChkBranchNo) {
		this.applChkBranchNo = applChkBranchNo;
	}

	public String getApplChkClerkNo() {
		return applChkClerkNo;
	}

	public void setApplChkClerkNo(String applChkClerkNo) {
		this.applChkClerkNo = applChkClerkNo;
	}

	public String getApplChkClerkName() {
		return applChkClerkName;
	}

	public void setApplChkClerkName(String applChkClerkName) {
		this.applChkClerkName = applChkClerkName;
	}

	public Date getApplChkDate() {
		return applChkDate;
	}

	public void setApplChkDate(Date applChkDate) {
		this.applChkDate = applChkDate;
	}

	public String getNewApplState() {
		return newApplState;
	}

	public void setNewApplState(String newApplState) {
		this.newApplState = newApplState;
	}

	public String getPclkBranchNo() {
		return pclkBranchNo;
	}

	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}

	public String getPclkClerkNo() {
		return pclkClerkNo;
	}

	public void setPclkClerkNo(String pclkClerkNo) {
		this.pclkClerkNo = pclkClerkNo;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
