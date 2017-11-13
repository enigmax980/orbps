package com.newcore.orbps.models.banktrans;

import java.util.Date;
import java.io.Serializable;

/**
 * 银行转账数据
 * 
 * @author jiachenchen 2016年8月12日 14:20:45
 */
public class BankTrans implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 序号
	 */
	private Long transCode;
	/**
	 * 转账状态
	 */
	private String transStat;
	/**
	 * 应收付记录标识
	 */
	private Long plnmioRecId;
	/**
	 * 合并的应收ID
	 */
	private String plnmioRecIdList;
	/**
	 * 转账批号
	 */
	private Long transBatSeq;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 保险公司银行帐号
	 */
	private String branchBankAccNo;
	/**
	 * 银行帐号
	 */
	private String bankAccNo;
	/**
	 * 账户所有人名称
	 */
	private String accCustName;
	/**
	 * 管理机构
	 */
	private String mgrBranchNo;
	/**
	 * 合同号
	 */
	private String cntrNo;
	/**
	 * 转账类型
	 */
	private String transClass;
	/**
	 * 收付类型
	 */
	private Integer mioClass;
	/**
	 * 收付项目代码
	 */
	private String mioItemCode;
	/**
	 * 应收付日期
	 */
	private Date plnmioDate;
	/**
	 * 实收付日期
	 */
	private Date mioDate;
	/**
	 * 金额
	 */
	private Double transAmnt;
	/**
	 * 客户号
	 */
	private String custNo;
	/**
	 * 生成日期
	 */
	private Date createDate;
	/**
	 * 收据号
	 */
	private Long btMioTxNo;

	/**
	 * 帐户所有人证件类别
	 */
	private String accCustIdType;
	/**
	 * 帐户所有人证件号
	 */
	private String accCustIdNo;
	/**
	 * 被保人顺序号
	 */
	private long ipsnNo;

	public Long getTransCode() {
		return transCode;
	}

	public void setTransCode(Long transCode) {
		this.transCode = transCode;
	}

	public String getTransStat() {
		return transStat;
	}

	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}

	public Long getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(Long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}

	public String getPlnmioRecIdList() {
		return plnmioRecIdList;
	}

	public void setPlnmioRecIdList(String plnmioRecIdList) {
		this.plnmioRecIdList = plnmioRecIdList;
	}

	public Long getTransBatSeq() {
		return transBatSeq;
	}

	public void setTransBatSeq(Long transBatSeq) {
		this.transBatSeq = transBatSeq;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBranchBankAccNo() {
		return branchBankAccNo;
	}

	public void setBranchBankAccNo(String branchBankAccNo) {
		this.branchBankAccNo = branchBankAccNo;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public String getAccCustName() {
		return accCustName;
	}

	public void setAccCustName(String accCustName) {
		this.accCustName = accCustName;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getTransClass() {
		return transClass;
	}

	public void setTransClass(String transClass) {
		this.transClass = transClass;
	}

	public Integer getMioClass() {
		return mioClass;
	}

	public void setMioClass(Integer mioClass) {
		this.mioClass = mioClass;
	}

	public String getMioItemCode() {
		return mioItemCode;
	}

	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}

	public Date getPlnmioDate() {
		return plnmioDate;
	}

	public void setPlnmioDate(Date plnmioDate) {
		this.plnmioDate = plnmioDate;
	}

	public Date getMioDate() {
		return mioDate;
	}

	public void setMioDate(Date mioDate) {
		this.mioDate = mioDate;
	}

	public Double getTransAmnt() {
		return transAmnt;
	}

	public void setTransAmnt(Double transAmnt) {
		this.transAmnt = transAmnt;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getBtMioTxNo() {
		return btMioTxNo;
	}

	public void setBtMioTxNo(Long btMioTxNo) {
		this.btMioTxNo = btMioTxNo;
	}

	public String getAccCustIdType() {
		return accCustIdType;
	}

	public void setAccCustIdType(String accCustIdType) {
		this.accCustIdType = accCustIdType;
	}

	public String getAccCustIdNo() {
		return accCustIdNo;
	}

	public void setAccCustIdNo(String accCustIdNo) {
		this.accCustIdNo = accCustIdNo;
	}

	public long getIpsnNo() {
		return ipsnNo;
	}

	public void setIpsnNo(long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}	
}