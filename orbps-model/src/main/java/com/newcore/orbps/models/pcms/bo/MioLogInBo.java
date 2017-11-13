package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
 
/**
 * 实收付流水表Model.
 */
public class MioLogInBo implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6395630955360998984L;

	/**
	 * 收付流水标识
	 */
//	@NotEmpty(message="[收付流水标识不能为空]")
//	@Length(max=10,message="[收付流水标识长度不能超过10位]") 
	private String mioLogId;
	
	/**
	 * 应收付记录标识
	 */
	@NotEmpty(message="[应收付记录标识不能为空]")
	@Length(max=10,message="[应收付记录标识长度不能超过10位]") 
	private String plnmioRecId;
	
	/**
	 * 险种代码
	 */
	@Length(max=8,message="[险种代码长度不能超过8位]") 
	private String polCode;
	
	/**
	 * 合同类型
	 */
	@Length(max=1,message="[合同类型长度不能超过1位]") 
	private String cntrType;
	
	/**
	 * 合同组号
	 */
	@Length(max=25,message="[合同类型长度不能超过25位]") 
	private String cgNo;
	
	/**
	 * 汇缴事件号
	 */
	@Length(max=25,message="[汇缴事件号长度不能超过25位]") 
	private String sgNo;
	
	/**
	 * 保单号/账号/投保单号
	 */
	@NotEmpty(message="[保单号/账号/投保单号不能为空]")
	@Length(max=25,message="[保单号/账号/投保单号长度不能超过25位]") 
	private String cntrNo;
	
	/**
	 * 保单币种
	 */
	@NotEmpty(message="[保单币种不能为空]")
	@Length(max=3,message="[保单币种长度不能超过3位]") 
	private String currencyCode;
	
	/**
	 * 保全批改流水号/附险标识
	 */
	@Length(max=10,message="[保全批改流水号/附险标识长度不能超过10位]") 
	private String mtnId;
	
	/**
	 * 批改保全项目
	 */
	@Length(max=2,message="[批改保全项目长度不能超过2位]") 
	private String mtnItemCode;
	
	/**
	 * 被保人序号/子账号
	 */
	@NotEmpty(message="[被保人序号/子账号不能为空]")
	@Length(max=8,message="[被保人序号/子账号长度不能超过8位]") 
	private String ipsnNo;
	
	/**
	 * 领款人/交款人客户号
	 */
	@Length(max=25,message="[领款人/交款人客户号长度不能超过25位]") 
	private String mioCustNo;
	
	/**
	 * 领款人/交款人姓名
	 */
	@Length(max=200,message="[领款人/交款人姓名长度不能超过200位]") 
	private String mioCustName;
	
	/**
	 * 应收付日期（暂收费可为销售员收款日期）
	 */
	@NotNull(message="[应收付日期（暂收费可为销售员收款日期）不能为空]")
	private Date plnmioDate;
	
	/**
	 * 实收付日期
	 */
	@NotNull(message="[实收付日期不能为空]")
	private Date mioDate;
	
	/**
	 * 写流水时间
	 */
	@NotNull(message="[写流水时间不能为空]")
	private Date mioLogUpdTime;
	
	/**
	 * 保费缴费宽限截止日期
	 */
	private Date premDeadline;
	
	/**
	 * 收付项目代码
	 */
	@NotEmpty(message="[收付项目代码不能为空]")
	@Length(max=2,message="[收付项目代码长度不能超过2位]") 
	private String mioItemCode;
	
	/**
	 * 收付款方式代码
	 */
	@NotEmpty(message="[收付款方式代码不能为空]")
	@Length(max=1,message="[收付款方式代码长度不能超过1位]") 
	private String mioTypeCode;
	
	/**
	 * 管理机构
	 */
	@NotEmpty(message="[管理机构不能为空]")
	@Length(max=6,message="[管理机构长度不能超过6位]") 
	private String mgrBranchNo;
	
	/**
	 * 操作员分支机构号
	 */
	@NotEmpty(message="[操作员分支机构号不能为空]")
	@Length(max=6,message="[操作员分支机构号长度不能超过6位]") 
	private String oclkBranchNo;
	
	/**
	 * 操作员代码
	 */
	@NotEmpty(message="[操作员代码不能为空]")
	@Length(max=8,message="[操作员代码长度不能超过8位]") 
	private String oclkClerkNo;
	
	/**
	 * 销售机构号
	 */
	@Length(max=6,message="[销售机构号长度不能超过6位]") 
	private String salesBranchNo;
	
	/**
	 * 销售渠道
	 */
	@Length(max=2,message="[销售机构号长度不能超过2位]") 
	private String salesChannel;
	
	/**
	 * 销售员号
	 */
	@Length(max=2,message="[销售机构号长度不能超过2位]") 
	private String salesNo;
	
	/**
	 * 收付类型
	 */
	private Integer mioClass;
	
	/**
	 * 金额
	 */
	@NotNull(message="[金额不能为空]")
	private String amnt;
	
	
	/**
	 * 账户所有人证件类别
	 */
	@Length(max=1,message="[账户所有人证件类别长度不能超过1位]") 
	private String bankaccIdType;
	
	/**
	 * 账户所有人证件号
	 */
	@Length(max=18,message="[账户所有人证件号长度不能超过18位]") 
	private String bankaccIdNo;
	
	/**
	 * 银行代码
	 */
	@Length(max=4,message="[银行代码长度不能超过4位]") 
	private String bankCode;
	
	/**
	 * 账户所有人名称
	 */
	@Length(max=200,message="[账户所有人名称长度不能超过200位]") 
	private String bankaccName;
	
	/**
	 * 银行账号
	 */
	@Length(max=48,message="[银行账号长度不能超过48位]") 
	private String bankAccNo;
	
	/**
	 * 收付交易类型
	 */
	
	@NotNull(message="[收付交易类型不能为空]")
	private Integer mioTxClass;
	
	/**
	 *收付交易号(7版收据号)
	 */
	@NotNull(message="[收付交易号(7版收据号)不能为空]")
	private Long mioTxNo;
	
	/**
	 * 冲正业务日期
	 */
	private Date corrMioDate;
	
	/**
	 * 冲正收付交易号
	 */
	private Long corrMioTxNo;
	
	/**
	 * 打印发票号
	 */
	@Length(max=30,message="[打印发票号长度不能超过30位]") 
	private String receiptNo;
	
	/**
	 * 核销凭证号
	 */
	@Length(max=30,message="[核销凭证号长度不能超过30位]") 
	private String voucherNo;
	
	/**
	 * 财务应收付日期
	 */
	private Date finPlnmioDate;
	
	/**
	 * 清算交易流水号
	 */
	@Length(max=20,message="[清算交易流水号长度不能超过20位]") 
	private String clearingMioTxNo;
	
	/**
	 * 是否收付处理标记
	 */
	@NotEmpty(message="[是否收付处理标记不能为空]")
	@Length(max=1,message="[清算交易流水号长度不能超过1位]") 
	private String mioProcFlag;
	
	/**
	 * 路由号
	 */
	@NotEmpty(message="[路由号不能为空]")
	@Length(max=1,message="[路由号长度不能超过1位]") 
	private String routerNo;
	
	/**
	 * 关联账户标识
	 */
	private Long accId;
	
	/**
	 * 备注
	 */
	@Length(max=1,message="[路由号长度不能超过255位]") 
	private String remark;
	
	/**
	 * 生成应收记录时间
	 */
	private Date plnmioCreateTime;
	
	/**
	 * 净收入
	 */
	private String netIncome;
	
	/**
	 * 税
	 */
	private String vat;
	
	/**
	 * ID号，对应vat_flow
	 */
	private String vatId;
	
	/**
	 * 税率
	 */
	private String vatRate;
	
	private String dataSource;
	
	private Long batNo;
	
	public Long getBatNo() {
		return batNo;
	}

	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getMioLogId() {
		return mioLogId;
	}

	public void setMioLogId(String mioLogId) {
		this.mioLogId = mioLogId;
	}

	public String getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(String plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public String getCntrType() {
		return cntrType;
	}

	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getSgNo() {
		return sgNo;
	}

	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getMtnId() {
		return mtnId;
	}

	public void setMtnId(String mtnId) {
		this.mtnId = mtnId;
	}

	public String getMtnItemCode() {
		return mtnItemCode;
	}

	public void setMtnItemCode(String mtnItemCode) {
		this.mtnItemCode = mtnItemCode;
	}

	public String getIpsnNo() {
		return ipsnNo;
	}

	public void setIpsnNo(String ipsnNo) {
		this.ipsnNo = ipsnNo;
	}

	public String getMioCustNo() {
		return mioCustNo;
	}

	public void setMioCustNo(String mioCustNo) {
		this.mioCustNo = mioCustNo;
	}

	public String getMioCustName() {
		return mioCustName;
	}

	public void setMioCustName(String mioCustName) {
		this.mioCustName = mioCustName;
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

	public Date getMioLogUpdTime() {
		return mioLogUpdTime;
	}

	public void setMioLogUpdTime(Date mioLogUpdTime) {
		this.mioLogUpdTime = mioLogUpdTime;
	}

	public Date getPremDeadline() {
		return premDeadline;
	}

	public void setPremDeadline(Date premDeadline) {
		this.premDeadline = premDeadline;
	}

	public String getMioItemCode() {
		return mioItemCode;
	}

	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}

	public String getMioTypeCode() {
		return mioTypeCode;
	}

	public void setMioTypeCode(String mioTypeCode) {
		this.mioTypeCode = mioTypeCode;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
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

	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	public Integer getMioClass() {
		return mioClass;
	}

	public void setMioClass(Integer mioClass) {
		this.mioClass = mioClass;
	}

	public String getAmnt() {
		return amnt;
	}

	public void setAmnt(String amnt) {
		this.amnt = amnt;
	}

	public String getBankaccIdType() {
		return bankaccIdType;
	}

	public void setBankaccIdType(String bankaccIdType) {
		this.bankaccIdType = bankaccIdType;
	}

	public String getBankaccIdNo() {
		return bankaccIdNo;
	}

	public void setBankaccIdNo(String bankaccIdNo) {
		this.bankaccIdNo = bankaccIdNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankaccName() {
		return bankaccName;
	}

	public void setBankaccName(String bankaccName) {
		this.bankaccName = bankaccName;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public Integer getMioTxClass() {
		return mioTxClass;
	}

	public void setMioTxClass(Integer mioTxClass) {
		this.mioTxClass = mioTxClass;
	}

	public Long getMioTxNo() {
		return mioTxNo;
	}

	public void setMioTxNo(Long mioTxNo) {
		this.mioTxNo = mioTxNo;
	}

	public Date getCorrMioDate() {
		return corrMioDate;
	}

	public void setCorrMioDate(Date corrMioDate) {
		this.corrMioDate = corrMioDate;
	}

	public Long getCorrMioTxNo() {
		return corrMioTxNo;
	}

	public void setCorrMioTxNo(Long corrMioTxNo) {
		this.corrMioTxNo = corrMioTxNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getFinPlnmioDate() {
		return finPlnmioDate;
	}

	public void setFinPlnmioDate(Date finPlnmioDate) {
		this.finPlnmioDate = finPlnmioDate;
	}

	public String getClearingMioTxNo() {
		return clearingMioTxNo;
	}

	public void setClearingMioTxNo(String clearingMioTxNo) {
		this.clearingMioTxNo = clearingMioTxNo;
	}

	public String getMioProcFlag() {
		return mioProcFlag;
	}

	public void setMioProcFlag(String mioProcFlag) {
		this.mioProcFlag = mioProcFlag;
	}

	public String getRouterNo() {
		return routerNo;
	}

	public void setRouterNo(String routerNo) {
		this.routerNo = routerNo;
	}

	public Long getAccId() {
		return accId;
	}

	public void setAccId(Long accId) {
		this.accId = accId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPlnmioCreateTime() {
		return plnmioCreateTime;
	}

	public void setPlnmioCreateTime(Date plnmioCreateTime) {
		this.plnmioCreateTime = plnmioCreateTime;
	}

	public String getNetIncome() {
		return netIncome;
	}

	public void setNetIncome(String netIncome) {
		this.netIncome = netIncome;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getVatId() {
		return vatId;
	}

	public void setVatId(String vatId) {
		this.vatId = vatId;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}


    
}
