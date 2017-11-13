package com.newcore.orbps.models.finance;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 实收付流水类
 * 
 * 2017-02-15
 * @author 李四魁
 *
 */
public class MioLog implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 收付流水标识
	 */
	private long mioLogId;
	/**
	 * 应收付记录标识
	 */
	private long plnmioRecId;
	/**
	 * 险种代码
	 */
	private String polCode;
	/**
	 * 合同类型
	 */
	private String cntrType;
	/**
	 * 合同组号
	 */
	private String cgNo;
	/**
	 * 汇缴事件号
	 */
	private String sgNo;
	/**
	 * 保单号/投保单号
	 */
	private String cntrNo;
	/**
	 * 币种
	 */
	private String currencyCode;
	/**
	 * 保全批改流水号/赔案号
	 */
	private long mtnId;
	/**
	 * 批改保全项目
	 */
	private String mtnItemCode;
	/**
	 * 被保人序号
	 */
	private long ipsnNo;
	/***
	 * 组织层次代码
	 */
	private String levelCode;
	/***
	 * 收费组属组编号
	 */
	private long feeGrpNo;
	/**
	 * 领款人/交款人客户号
	 */
	private String mioCustNo;
	/**
	 * 领款人/交款人姓名
	 */
	private String mioCustName;
	/**
	 * 应收付日期
	 */
	private Date plnmioDate;
	/**
	 * 实收付日期
	 */
	private Date mioDate;
	/**
	 * 写流水日期
	 */
	private Date mioLogUpdTime;
	/**
	 * 保费缴纳宽限截止日期
	 */
	private Date premDeadlineDate;
	/**
	 * 收付项目代码
	 */
	private String mioItemCode;
	/**
	 * 收付方式代码
	 */
	private String mioType;
	/**
	 * 管理机构
	 */
	private String mgrBranchNo;
	/**
	 * 操作员分支机构号
	 */
	private String pclkBranchNo;
	/**
	 * 操作员代码
	 */
	private String pclkNo;
	/**
	 * 销售机构号
	 */
	private String salesBranchNo;
	/**
	 * 销售渠道
	 */
	private String salesChannel;
	/**
	 * 销售员代码
	 */
	private String salesNo;
	/**
	 * 收付类型
	 */
	private int mioClass;
	/**
	 * 金额
	 */
	private BigDecimal amnt;
	/**
	 * 帐户所有人证件类别
	 */
	private String accCustIdType;
	/**
	 * 帐户所有人证件号
	 */
	private String accCustIdNo;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 帐户所有人名称
	 */
	private String bankAccName;
	/**
	 * 银行账号
	 */
	private String bankAccNo;

	
	/**
	 * 收付交易类型
	 */
	private int mioTxClass;
	/**
	 * 收付交易号
	 */
	private long mioTxNo;
	/**
	 * 冲正业务日期
	 */
	private Date corrMioDate;
	/**
	 * 冲正收付交易号
	 */
	private long corrMioTxNo;
	/**
	 * 打印发票号
	 */
	private String receiptNo;
	/**
	 * 核销凭证号
	 */
	private String voucherNo;
	/**
	 * 财务应收付日期
	 */
	private Date finPlnmioDate;
	/**
	 * 清算交易流水号
	 */
	private String clearingMioTxNo;
	/**
	 * 是否收付处理标记
	 */
	private String mioProcFlag;
	/**
	 * 路由号
	 */
	private String routerNo;
	/**
	 * 关联帐户标识
	 */
	private long accId;
	/**
	 * 实收数据入账状态
	 */
	private String coreStat;
	/**
	 * 批次号
	 */
	private long batNo;
	/**
	 * 转账编号
	 */
	private long transCode;
	/**
	 * 转账交易号
	 */
	private long btMioTxNo;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 生成应收记录时间
	 */
	private Date plnmioCreateTime;
	/**
	 * 净收入
	 */
	private Double netIncome;
	/**
	 * 税
	 */
	private Double vat;
	/**
	 * ID号，对应vat_flow
	 */
	private String vatId;
	/**
	 * 税率
	 */
	private Double vatRate;

	/**
	 * 营改增拆分结构
	 */
	private List<VatSplitInfo> vatSplitInfoList;
	
	
	public Long getMioLogId() {
		return mioLogId;
	}
	public void setMioLogId(Long mioLogId) {
		this.mioLogId = mioLogId;
	}
	public Long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(Long plnmioRecId) {
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
	public Long getMtnId() {
		return mtnId;
	}
	public void setMtnId(Long mtnId) {
		this.mtnId = mtnId;
	}
	public String getMtnItemCode() {
		return mtnItemCode;
	}
	public void setMtnItemCode(String mtnItemCode) {
		this.mtnItemCode = mtnItemCode;
	}
	public Long getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(Long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public Long getFeeGrpNo() {
		return feeGrpNo;
	}
	public void setFeeGrpNo(Long feeGrpNo) {
		this.feeGrpNo = feeGrpNo;
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
	public Date getPremDeadlineDate() {
		return premDeadlineDate;
	}
	public void setPremDeadlineDate(Date premDeadlineDate) {
		this.premDeadlineDate = premDeadlineDate;
	}
	public String getMioItemCode() {
		return mioItemCode;
	}
	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}
	public String getMioType() {
		return mioType;
	}
	public void setMioType(String mioType) {
		this.mioType = mioType;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	public String getPclkNo() {
		return pclkNo;
	}
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
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
	public BigDecimal getAmnt() {
		return amnt;
	}
	public void setAmnt(BigDecimal amnt) {
		this.amnt = amnt;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
	public Long getBtMioTxNo() {
		return btMioTxNo;
	}
	public void setBtMioTxNo(Long btMioTxNo) {
		this.btMioTxNo = btMioTxNo;
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
	public String getClearingMioTxNo() {
		return clearingMioTxNo;
	}
	public void setClearingMioTxNo(String clearingMioTxNo) {
		this.clearingMioTxNo = clearingMioTxNo;
	}
	public Date getFinPlnmioDate() {
		return finPlnmioDate;
	}
	public void setFinPlnmioDate(Date finPlnmioDate) {
		this.finPlnmioDate = finPlnmioDate;
	}
	public String getCoreStat() {
		return coreStat;
	}
	public void setCoreStat(String coreStat) {
		this.coreStat = coreStat;
	}
	public Long getBatNo() {
		return batNo;
	}
	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}
	public Long getTransCode() {
		return transCode;
	}
	public void setTransCode(Long transCode) {
		this.transCode = transCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getBankAccName() {
		return bankAccName;
	}
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
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
	public long getAccId() {
		return accId;
	}
	public void setAccId(long accId) {
		this.accId = accId;
	}
	public Date getPlnmioCreateTime() {
		return plnmioCreateTime;
	}
	public void setPlnmioCreateTime(Date plnmioCreateTime) {
		this.plnmioCreateTime = plnmioCreateTime;
	}
	public Double getNetIncome() {
		return netIncome;
	}
	public void setNetIncome(Double netIncome) {
		this.netIncome = netIncome;
	}
	public Double getVat() {
		return vat;
	}
	public void setVat(Double vat) {
		this.vat = vat;
	}
	public String getVatId() {
		return vatId;
	}
	public void setVatId(String vatId) {
		this.vatId = vatId;
	}
	public Double getVatRate() {
		return vatRate;
	}
	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

    public List<VatSplitInfo> getVatSplitInfoList() {
        return vatSplitInfoList;
    }

    public void setVatSplitInfoList(List<VatSplitInfo> vatSplitInfoList) {
        this.vatSplitInfoList = vatSplitInfoList;
    }
}