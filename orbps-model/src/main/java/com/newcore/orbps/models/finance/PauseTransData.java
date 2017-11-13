package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author LiSK
 *2017-02-22
 */
@SuppressWarnings(value = "serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BANK_NOT2TRANS_SCXE")
public class PauseTransData implements Serializable {
	/**
	 * 系统号
	 */
	@XmlElement(name = "SYS_NO")
	String sysNo;
	/**
	 * 合同号
	 */
	@XmlElement(name = "CNTR_NO")
	String cntrNo;
	/**
	 * 客户号
	 */
	@XmlElement(name = "CUST_NO")
	String custNo;
	/**
	 * 应收付日期
	 */
	@XmlElement(name = "PLNMIO_DATE")
	String plnmioDate;
	/**
	 * 收付标志
	 */
	@XmlElement(name = "MIO_CLASS")
	int mioClass;
	/**
	 * 收付费项目
	 */
	@XmlElement(name = "MIO_ITEM_CODE")
	String mioItemCode;
	/**
	 * 银行代码
	 */
	@XmlElement(name = "BANK_CODE")
	String bankCode;
	/**
	 * 帐号
	 */
	@XmlElement(name = "BANK_ACC_NO")
	String bankAccNo;
	/**
	 * 金额
	 */
	@XmlElement(name = "AMNT")
	BigDecimal amnt;
	/**
	 * 管理机构
	 */
	@XmlElement(name = "MGR_BRANCH_NO")
	String mgrBranchNo;
	/**
	 * 应收付标识id
	 */
	@XmlElement(name = "PLNMIO_REC_ID")
	long plnmioRecId;
	/**
	 * 款项所有人
	 */
	@XmlElement(name = "MIO_CUST_NAME")
	String mioCustName;
	/**
	 * 生成应收付操作员机构
	 */
	@XmlElement(name = "GCLK_BRANCH_NO")
	String gclkBranchNo;
	/**
	 * 生成应收付操作员代码
	 */
	@XmlElement(name = "GCLK_CLERK_NO")
	String gclkClerkNo;
	/**
	 * 不可送划原因
	 */
	@XmlElement(name = "STOP_TRANS_REASON")
	String stopTransReason;
	/**
	 * 不可送划起日期
	 */
	@XmlElement(name = "STOP_TRANS_DATE")
	String stopTransDate;
	/**
	 * 恢复送划日期
	 */
	@XmlElement(name = "RE_TRANS_DATE")
	String reTransDate;
	/**
	 * 是否冻结应收付表  0-不冻结   1-冻结
	 */
	@XmlElement(name = "LOCK_FLAG")
	String lockFlag;
	/**
	 * 是否送划  0-不送   1-要送划
	 */
	@XmlElement(name = "TRANS_FLAG")
	String transFlag;
	/**
	 * 录入时间
	 */
	@XmlElement(name = "ENTER_TIME")
	String enterTime;
	/**
	 * 录入机构
	 */
	@XmlElement(name = "ENTER_BRANCH_NO")
	String enterBranchNo;
	/**
	 * 录入工号
	 */
	@XmlElement(name = "ENTER_CLERK_NO")
	String enterClerkNo;
	/**
	 * 是否撤消
	 */
	@XmlElement(name = "CANCEL_FLAG")
	String cancelFlag;
	/**
	 * 撤消时间
	 */
	@XmlElement(name = "CANCEL_TIME")
	String cancelTime;
	/**
	 * 撤消机构
	 */
	@XmlElement(name = "CANCEL_BRANCH_NO")
	String cancelBranchNo;
	/**
	 * 撤消工号
	 */
	@XmlElement(name = "CANCEL_CLERK_NO")
	String cancelClerkNo;
	/**
	 * 撤消理由
	 */
	@XmlElement(name = "CANCEL_REASON")
	String cancelReason;
	/**
	 * 失效标志
	 */
	@XmlElement(name = "INVALID_STAT")
	String invalidStat;
	/**
	 * 是否转账途中
	 */
	@XmlElement(name = "HOLD_FLAG")
	String holdFlag;
	/**
	 * 收付费形式
	 */
	@XmlElement(name = "MIO_TYPE")
	String mioType;
	/**
	 * 选择标志
	 */
	@XmlElement(name = "SELFLAG")
	String selflag;
	/**
	 * 处理标志
	 */
	@XmlElement(name = "BANKREG_FLAG")
	String bankRegFlag;
	/**
	 * 备注说明
	 */
	@XmlElement(name = "REMARK")
	String remark;
	
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public String getCntrNo() {
		return cntrNo;
	}
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getPlnmioDate() {
		return plnmioDate;
	}
	public void setPlnmioDate(String plnmioDate) {
		this.plnmioDate = plnmioDate;
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
	public BigDecimal getAmnt() {
		return amnt;
	}
	public void setAmnt(BigDecimal amnt) {
		this.amnt = amnt;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	public String getMioCustName() {
		return mioCustName;
	}
	public void setMioCustName(String mioCustName) {
		this.mioCustName = mioCustName;
	}
	public String getGclkBranchNo() {
		return gclkBranchNo;
	}
	public void setGclkBranchNo(String gclkBranchNo) {
		this.gclkBranchNo = gclkBranchNo;
	}
	public String getGclkClerkNo() {
		return gclkClerkNo;
	}
	public void setGclkClerkNo(String gclkClerkNo) {
		this.gclkClerkNo = gclkClerkNo;
	}
	public String getStopTransReason() {
		return stopTransReason;
	}
	public void setStopTransReason(String stopTransReason) {
		this.stopTransReason = stopTransReason;
	}
	
	public String getStopTransDate() {
		return stopTransDate;
	}
	public void setStopTransDate(String stopTransDate) {
		this.stopTransDate = stopTransDate;
	}
	public String getReTransDate() {
		return reTransDate;
	}
	public void setReTransDate(String reTransDate) {
		this.reTransDate = reTransDate;
	}
	public String getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}
	public String getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
	public String getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}
	public String getEnterBranchNo() {
		return enterBranchNo;
	}
	public void setEnterBranchNo(String enterBranchNo) {
		this.enterBranchNo = enterBranchNo;
	}
	public String getEnterClerkNo() {
		return enterClerkNo;
	}
	public void setEnterClerkNo(String enterClerkNo) {
		this.enterClerkNo = enterClerkNo;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getCancelBranchNo() {
		return cancelBranchNo;
	}
	public void setCancelBranchNo(String cancelBranchNo) {
		this.cancelBranchNo = cancelBranchNo;
	}
	public String getCancelClerkNo() {
		return cancelClerkNo;
	}
	public void setCancelClerkNo(String cancelClerkNo) {
		this.cancelClerkNo = cancelClerkNo;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getInvalidStat() {
		return invalidStat;
	}
	public void setInvalidStat(String invalidStat) {
		this.invalidStat = invalidStat;
	}
	public String getHoldFlag() {
		return holdFlag;
	}
	public void setHoldFlag(String holdFlag) {
		this.holdFlag = holdFlag;
	}
	public String getMioType() {
		return mioType;
	}
	public void setMioType(String mioType) {
		this.mioType = mioType;
	}
	public String getSelflag() {
		return selflag;
	}
	public void setSelflag(String selflag) {
		this.selflag = selflag;
	}
	public String getBankRegFlag() {
		return bankRegFlag;
	}
	public void setBankRegFlag(String bankRegFlag) {
		this.bankRegFlag = bankRegFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}






