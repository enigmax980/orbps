package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;

/**
 * 组织关系树
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:52:56
 */
public class OrgTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6970078440845162134L;

	/* 字段名：层级代码，是否必填：是 */
	private String levelCode;
	
	/* 字段名：上级层级代码，是否必填：是 */
	private String prioLevelCode;

	/* 字段名：是否缴费，长度：2，是否必填：是	1：是；0：否。*/
	private String isPaid;
	
	/* 字段名：保全选项，长度：2，是否必填：是	1：是；0：否。*/
	private String mtnOpt;
	
	/* 字段名：服务指派，长度：2，是否必填：是	1：是；0：否。*/
	private String serviceOpt;
	
	/* 字段名：发票选项，长度：2，是否必填：是	1：是；0：否。*/
	private String receiptOpt;
	
	/* 字段名：纳税人识别号，长度：15，是否必填：否*/
	private String taxpayerId;
	
	/* 字段名：节点类型，长度：15，是否必填：是	0:企业法人；1：部门；3：虚拟节点*/
	private String nodeType;
	
	/* 字段名：是否根节点，长度：2，是否必填：是	1：是；1：否。*/
	private String isRoot;
	
	/* 字段名：缴费形式，长度：2，是否必填：IF isPaid == 1 THEN*/
	private String moneyinType;
	
	/* 字段名：节点交费金额，是否必填：IF isPaid == 1 THEN*/
	private Double nodePayAmnt;
	
	/* 字段名：开户银行，长度：4，是否必填：IF isPaid == 1 THEN*/
	private String bankCode;
	
	/* 字段名：开户名称，长度：48，是否必填：IF isPaid == 1 THEN*/
	private String bankaccName;
	
	/* 字段名：银行账号，长度：25，是否必填：IF isPaid == 1 THEN*/
	private String bankaccNo;
	
	/* 字段名：支票号，长度：25，是否必填：IF isPaid == 1 THEN*/
	private String bankChequeNo;

	/* 字段名：团体客户信息*/
	private GrpHolderInfo grpHolderInfo;

	/**
	 * @return the levelCode
	 */
	public String getLevelCode() {
		return levelCode;
	}

	/**
	 * @param levelCode the levelCode to set
	 */
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	/**
	 * @return the prioLevelCode
	 */
	public String getPrioLevelCode() {
		return prioLevelCode;
	}

	/**
	 * @param prioLevelCode the prioLevelCode to set
	 */
	public void setPrioLevelCode(String prioLevelCode) {
		this.prioLevelCode = prioLevelCode;
	}

	/**
	 * @return the isPaid
	 */
	public String getIsPaid() {
		return isPaid;
	}

	/**
	 * @param isPaid the isPaid to set
	 */
	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}

	/**
	 * @return the mtnOpt
	 */
	public String getMtnOpt() {
		return mtnOpt;
	}

	/**
	 * @param mtnOpt the mtnOpt to set
	 */
	public void setMtnOpt(String mtnOpt) {
		this.mtnOpt = mtnOpt;
	}

	/**
	 * @return the serviceOpt
	 */
	public String getServiceOpt() {
		return serviceOpt;
	}

	/**
	 * @param serviceOpt the serviceOpt to set
	 */
	public void setServiceOpt(String serviceOpt) {
		this.serviceOpt = serviceOpt;
	}

	/**
	 * @return the receiptOpt
	 */
	public String getReceiptOpt() {
		return receiptOpt;
	}

	/**
	 * @param receiptOpt the receiptOpt to set
	 */
	public void setReceiptOpt(String receiptOpt) {
		this.receiptOpt = receiptOpt;
	}

	/**
	 * @return the taxpayerId
	 */
	public String getTaxpayerId() {
		return taxpayerId;
	}

	/**
	 * @param taxpayerId the taxpayerId to set
	 */
	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * @return the isRoot
	 */
	public String getIsRoot() {
		return isRoot;
	}

	/**
	 * @param isRoot the isRoot to set
	 */
	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}

	/**
	 * @return the nodePayAmnt
	 */
	public Double getNodePayAmnt() {
		return nodePayAmnt;
	}

	/**
	 * @param nodePayAmnt the nodePayAmnt to set
	 */
	public void setNodePayAmnt(Double nodePayAmnt) {
		this.nodePayAmnt = nodePayAmnt;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the bankaccName
	 */
	public String getBankaccName() {
		return bankaccName;
	}

	/**
	 * @param bankaccName the bankaccName to set
	 */
	public void setBankaccName(String bankaccName) {
		this.bankaccName = bankaccName;
	}

	/**
	 * @return the bankaccNo
	 */
	public String getBankaccNo() {
		return bankaccNo;
	}

	/**
	 * @param bankaccNo the bankaccNo to set
	 */
	public void setBankaccNo(String bankaccNo) {
		this.bankaccNo = bankaccNo;
	}

	/**
	 * @return the bankChequeNo
	 */
	public String getBankChequeNo() {
		return bankChequeNo;
	}

	/**
	 * @param bankChequeNo the bankChequeNo to set
	 */
	public void setBankChequeNo(String bankChequeNo) {
		this.bankChequeNo = bankChequeNo;
	}

	/**
	 * @return the grpHolderInfo
	 */
	public GrpHolderInfo getGrpHolderInfo() {
		return grpHolderInfo;
	}

	/**
	 * @param grpHolderInfo the grpHolderInfo to set
	 */
	public void setGrpHolderInfo(GrpHolderInfo grpHolderInfo) {
		this.grpHolderInfo = grpHolderInfo;
	}

	public String getMoneyinType() {
		return moneyinType;
	}

	public void setMoneyinType(String moneyinType) {
		this.moneyinType = moneyinType;
	}

}
