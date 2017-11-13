package com.newcore.orbps.models.banktrans;

import java.util.Date;
import java.io.Serializable;

/**
 * 应收付记录
 * 
 * @author jiachenchen 2016年8月12日 14:04:04
 */
public class PlnmioRec implements Serializable {
	private static final long serialVersionUID = 1L;

	// 应收付记录标识
	private Long plnmioRecId;
	// 被投保人序号
	private Long ipsnNo;
	// 组织层次代码
	private String levelCode;
	// 收费组属组编号
	private Long feeGrpNo;
	// 合同类型
	private String cntrType;
	// 汇缴事件号
	private String sgNo;
	// 合同组号
	private String cgNo;
	// 归档机构
	private String arcBranchNo;
	// 签单年度
	private Integer signYear;
	// 险种代码
	private String polCode;
	// 保单号
	private String cntrNo;
	// 保全批改流水号
	private Long mtnId;
	// 批改保全项目
	private String mtnItemCode;
	// 领款人/交款人客户号
	private String mioCustNo;
	// 领款人/交款人姓名
	private String mioCustName;
	// 收付类型
	private Integer mioClass;
	// 应收付日期
	private Date plnmioDate;
	// 保费缴费宽限截止日期
	private Date premDeadlineDate;
	// 收付项目代码
	private String mioItemCode;
	// 收付款形式代码
	private String mioType;
	// 管理机构
	private String mgrBranchNo;
	// 销售渠道
	private String salesChannel;
	// 销售机构号
	private String salesBranchNo;
	// 销售员
	private String salesNo;
	// 金额
	private Double amnt;
	// 锁标志
	private Integer lockFlag;
	// 银行代码
	private String bankCode;
	// 银行帐号
	private String bankAccNo;
	// 帐户所有人名称
	private String bankAccName;
	// 待转账标识
	private Integer holdFlag;
	// 核销凭证号
	private String voucherNo;
	// 财务应收付日期
	private Date finPlnmioDate;
	// 清算交易号（收据号）
	private String clearingMioTxNo;
	// 是否收付处理标记
	private String mioProcFlag;
	// 关联账户标识
	private Long accId;
	// 生成应收记录时间
	private Date plnmioCreateTime;
	// 应收状态 N未收 D作废 S实收，S成功，F失败
	private String procStat;
	// 转账状态
	private String transStat;
	// 备注
	private String remark;
	// 币种
	private String currencyCode;
	// 帐户所有人证件类别
	private String accCustIdType;
	// 帐户所有人证件号
	private String bankaccIdNo;
	// 路由号
	private String routerNo;
	// 账户所属类别【O:组织架构树应收,I:被保人应收,P:收费组产生应收】
	private String multiPayAccType;

	public Long getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(Long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
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

	public String getCntrType() {
		return cntrType;
	}

	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public String getSgNo() {
		return sgNo;
	}

	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getArcBranchNo() {
		return arcBranchNo;
	}

	public void setArcBranchNo(String arcBranchNo) {
		this.arcBranchNo = arcBranchNo;
	}

	public Integer getSignYear() {
		return signYear;
	}

	public void setSignYear(Integer signYear) {
		this.signYear = signYear;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
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

	public Integer getMioClass() {
		return mioClass;
	}

	public void setMioClass(Integer mioClass) {
		this.mioClass = mioClass;
	}

	public Date getPlnmioDate() {
		return plnmioDate;
	}

	public void setPlnmioDate(Date plnmioDate) {
		this.plnmioDate = plnmioDate;
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

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}

	public String getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	public Double getAmnt() {
		return amnt;
	}

	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
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

	public String getBankAccName() {
		return bankAccName;
	}

	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}

	public Integer getHoldFlag() {
		return holdFlag;
	}

	public void setHoldFlag(Integer holdFlag) {
		this.holdFlag = holdFlag;
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

	public Long getAccId() {
		return accId;
	}

	public void setAccId(Long accId) {
		this.accId = accId;
	}

	public Date getPlnmioCreateTime() {
		return plnmioCreateTime;
	}

	public void setPlnmioCreateTime(Date plnmioCreateTime) {
		this.plnmioCreateTime = plnmioCreateTime;
	}

	public String getProcStat() {
		return procStat;
	}

	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}

	public String getTransStat() {
		return transStat;
	}

	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAccCustIdType() {
		return accCustIdType;
	}

	public void setAccCustIdType(String accCustIdType) {
		this.accCustIdType = accCustIdType;
	}

	public String getBankaccIdNo() {
		return bankaccIdNo;
	}

	public void setBankaccIdNo(String bankaccIdNo) {
		this.bankaccIdNo = bankaccIdNo;
	}

	public String getRouterNo() {
		return routerNo;
	}

	public void setRouterNo(String routerNo) {
		this.routerNo = routerNo;
	}

	public String getMultiPayAccType() {
		return multiPayAccType;
	}

	public void setMultiPayAccType(String multiPayAccType) {
		this.multiPayAccType = multiPayAccType;
	}

}