package com.newcore.orbps.models.finance;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行转账数据
 * 
 * @author jiachenchen 2016年8月12日 14:20:45
 */
public class BankTrans implements Serializable {
	private static final long serialVersionUID = 1L;
	private long transCode;  //序号
	private String transStat; //处理状态(保险公司标准)	
	private long plnmioRecId; //应收付记录标识
	private String plnmioRecIdList;	//
	private long transBatSeq; //转帐批号
	private String bankCode; //银行代码
	private String branchBankAccNo;	//保险公司银行帐号
	private String accCustName;	//帐户所有人名称
	private String accCustIdType;	//帐户所有人证件类别
	private String accCustIdNo;		//帐户所有人证件号
	private String bankAccNo;		//银行帐号
	private String mgrBranchNo;  //管理机构
	private String cntrNo;  //合同号
	private long ipsnNo; //被保人顺序号
	private String transClass;		//转帐类型
	private int mioClass;		//收付类型
	private String mioItemCode;		//收付项目代码
	private Date plnmioDate;		//应收/付日期
	private Date mioDate;			//实收/付日期
	private BigDecimal transAmnt;		//金额
	private String custNo;			//客户号
	private Date createDate;		//生成日期	
	private Long btMioTxNo;			//转账交易号
	
	public long getTransCode() {
		return transCode;
	}
	public void setTransCode(long transCode) {
		this.transCode = transCode;
	}
	public String getTransStat() {
		return transStat;
	}
	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}
	public long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	public String getPlnmioRecIdList() {
		return plnmioRecIdList;
	}
	public void setPlnmioRecIdList(String plnmioRecIdList) {
		this.plnmioRecIdList = plnmioRecIdList;
	}
	public long getTransBatSeq() {
		return transBatSeq;
	}
	public void setTransBatSeq(long transBatSeq) {
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
	public String getAccCustName() {
		return accCustName;
	}
	public void setAccCustName(String accCustName) {
		this.accCustName = accCustName;
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
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
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
	public long getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	public String getTransClass() {
		return transClass;
	}
	public void setTransClass(String transClass) {
		this.transClass = transClass;
	}
	public int getMioClass() {
		return mioClass;
	}
	public void setMioClass(int mioClass) {
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
	public BigDecimal getTransAmnt() {
		return transAmnt;
	}
	public void setTransAmnt(BigDecimal transAmnt) {
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
	
}