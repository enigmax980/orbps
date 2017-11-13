package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huanglong
 * @date 2017年2月20日
 * @content 回执核销查询返回类，用于回执核销查询
 */
public class QueryReceipt implements Serializable{

	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -1840564723205466590L;

	
	/*字段名称:销售机构号,长度:,是否必填:否*/
	private String salesBranchNo;
	/*字段名称:投保单号,长度:,是否必填:否*/
	private String applNo;
	/*字段名称:投保人/汇交人姓名,长度:,是否必填:否*/
	private String sgName;
	/*字段名称:投保日期,长度:,是否必填:否*/
	private Date applDate;
	/*字段名称:打印日期,长度:,是否必填:否*/
	private Date printDate;
	/*字段名称:合同送达日期,长度:,是否必填:否*/
	private Date signDate;
	/*字段名称:,长度:,是否必填:否*/
	private String cntrSendType;
	/*字段名称:回执核销操作时间,长度:,是否必填:否*/
	private Date respDate;
	/*字段名称:回执操作人员机构号,长度:,是否必填:否*/
	private String oclkBranchNo;
	/*字段名称:回执核销操作人员工号,长度:,是否必填:否*/
	private String oclkClerkNo;
	/*字段名称:回执核销操作人员姓名,长度:,是否必填:否*/
	private String oclkClerkName;
	/*字段名称:核销状态描述,长度:,是否必填:否*/
	private String description;
	public QueryReceipt() {
		super();
		// TODO Auto-generated constructor stub
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
	public Date getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}
	/**
	 * @return the printDate
	 */
	public Date getPrintDate() {
		return printDate;
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
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
	 * @return the respDate
	 */
	public Date getRespDate() {
		return respDate;
	}
	/**
	 * @param respDate the respDate to set
	 */
	public void setRespDate(Date respDate) {
		this.respDate = respDate;
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
}
