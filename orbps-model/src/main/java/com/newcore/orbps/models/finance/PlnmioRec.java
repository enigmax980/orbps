package com.newcore.orbps.models.finance;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 应收付记录
 * @author jiachenchen 2016年8月12日 14:04:04
 */
public class PlnmioRec implements Serializable {
	private static final long serialVersionUID = 1L;

	//应收付记录标识
	private long plnmioRecId; //应收付记录标识
	private String cntrType;  //合同类型
	private String sgNo;      //汇缴事件号
	private String cgNo;	  //合同组号
	private String arcBranchNo;	//	(路由)归档机构号
	private int signYear;			//(路由)签单年度
	private String polCode;			//(路由)险种代码(如果是附险放附险险种代码)
	private String cntrNo;			//保单号/投保单号/帐号
	private String currencyCode;	//保单币种 'CNY'
	private long mtnId;			    //"保全批改流水号/附险合同号(附加险续保用)"
	private String mtnItemCode;		//	批改保全项目
	private long ipsnNo;			//被保人序号
	private String mioCustNo;	    //领款人/交款人客户号
	private String mioCustName;		//领款人/交款人姓名
	private int mioClass;			//收付类型
	private Date plnmioDate;		//应收付日期
	private Date premDeadlineDate;		//保费缴费宽限截止日期
	private String mioItemCode;		//收付项目代码
	private String mioType;			//收付款形式代码
	private String mgrBranchNo;		//管理机构
	private String salesChannel;	//销售渠道
	private String salesBranchNo;	//销售机构号
	private String salesNo;			//销售员号
	private BigDecimal amnt;		//金额
	private String lockFlag;		//锁标志
	private String bankCode;		//银行代码
	private String bankAccName;		//帐户所有人名称
	private String accCustIdType;	//帐户所有人证件类别
	private String accCustIdNo;		//帐户所有人证件号
	private String bankAccNo;		//银行帐号
	private String holdFlag;		//待转帐标志
	private String voucherNo;		//核销凭证号
	private Date finPlnmioDate;		//财务应收付日期
	private String clearingMioTxNo;	//清算交易号(收据号)
	private String mioProcFlag;		//是否收付处理标记
	private String routerNo;		//路由号
	private long accId;	   			//关联帐户标识
	private String ipsnName;		//被保人姓名
	private String ipsnSex;			//被保人性别
	private Date ipsnBirthDate;		//被保人出生日期
	private String ipsnIdType;		//被保人证件类别
	private String ipsnIdNo;		//被保人证件号
	private String remark;			//备注
	private Date plnmioCreateTime;	//生成应收记录时间
	private String transStat;		//转账状态
	private String procStat;		//应收状态
	private String multiPayAccType;	//账号所属人类别
	private String levelCode;		//组织层次代码
	private long feeGrpNo;	//收费组属组编号
	private String extKey1;	//扩展健1
	private String extKey2;	//扩展健2
	private String extKey3;	//扩展健3
	private String extKey4;	//扩展健4
	private String extKey5;	//扩展健5
	private Long batNo;     //批次号
	
	
	public Long getBatNo() {
		return batNo;
	}
	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}
	public long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
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
	public int getSignYear() {
		return signYear;
	}
	public void setSignYear(int signYear) {
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
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public long getMtnId() {
		return mtnId;
	}
	public void setMtnId(long mtnId) {
		this.mtnId = mtnId;
	}
	public String getMtnItemCode() {
		return mtnItemCode;
	}
	public void setMtnItemCode(String mtnItemCode) {
		this.mtnItemCode = mtnItemCode;
	}
	public long getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(long ipsnNo) {
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
	public int getMioClass() {
		return mioClass;
	}
	public void setMioClass(int mioClass) {
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
	public BigDecimal getAmnt() {
		return amnt;
	}
	public void setAmnt(BigDecimal amnt) {
		this.amnt = amnt;
	}
	public String getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankAccName() {
		return bankAccName;
	}
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
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
	public String getHoldFlag() {
		return holdFlag;
	}
	public void setHoldFlag(String holdFlag) {
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
	public String getIpsnName() {
		return ipsnName;
	}
	public void setIpsnName(String ipsnName) {
		this.ipsnName = ipsnName;
	}
	public String getIpsnSex() {
		return ipsnSex;
	}
	public void setIpsnSex(String ipsnSex) {
		this.ipsnSex = ipsnSex;
	}
	public Date getIpsnBirthDate() {
		return ipsnBirthDate;
	}
	public void setIpsnBirthDate(Date ipsnBirthDate) {
		this.ipsnBirthDate = ipsnBirthDate;
	}
	public String getIpsnIdType() {
		return ipsnIdType;
	}
	public void setIpsnIdType(String ipsnIdType) {
		this.ipsnIdType = ipsnIdType;
	}
	public String getIpsnIdNo() {
		return ipsnIdNo;
	}
	public void setIpsnIdNo(String ipsnIdNo) {
		this.ipsnIdNo = ipsnIdNo;
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
	public String getTransStat() {
		return transStat;
	}
	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}
	public String getProcStat() {
		return procStat;
	}
	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}
	public String getMultiPayAccType() {
		return multiPayAccType;
	}
	public void setMultiPayAccType(String multiPayAccType) {
		this.multiPayAccType = multiPayAccType;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public long getFeeGrpNo() {
		return feeGrpNo;
	}
	public void setFeeGrpNo(long feeGrpNo) {
		this.feeGrpNo = feeGrpNo;
	}
	public String getExtKey1() {
		return extKey1;
	}
	public void setExtKey1(String extKey1) {
		this.extKey1 = extKey1;
	}
	public String getExtKey2() {
		return extKey2;
	}
	public void setExtKey2(String extKey2) {
		this.extKey2 = extKey2;
	}
	public String getExtKey3() {
		return extKey3;
	}
	public void setExtKey3(String extKey3) {
		this.extKey3 = extKey3;
	}
	public String getExtKey4() {
		return extKey4;
	}
	public void setExtKey4(String extKey4) {
		this.extKey4 = extKey4;
	}
	public String getExtKey5() {
		return extKey5;
	}
	public void setExtKey5(String extKey5) {
		this.extKey5 = extKey5;
	}
	public String getArcBranchNo() {
		return arcBranchNo;
	}
	public void setArcBranchNo(String arcBranchNo) {
		this.arcBranchNo = arcBranchNo;
	}
}