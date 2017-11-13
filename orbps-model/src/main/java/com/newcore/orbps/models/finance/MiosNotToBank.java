package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 银行转账暂停送划实体类
 * 
 * @author 李四魁
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_NotToBankTe_SCXE")
public class MiosNotToBank implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 应收付标识
	 */
	private long plnmioRecId;
	/**
	 * 系统号
	 */
	private String sysNo;
	/**
	 * 保单号码
	 */
	private String cntrNo;
	/**
	 * 客户号
	 */
	private String custNo;
	/**
	 * 应收付日期
	 */
	private Date plnmioDate;
	/**
	 * 收付费类别
	 */
	private int mioClass;
	/**
	 * 收付费项目
	 */
	private String mioItemCode;
	/**
	 * 银行代号
	 */
	private String bankCode;
	/**
	 * 帐号
	 */
	private String bankAccNo;
	/**
	 * 金额
	 */
	private BigDecimal amnt;
	/**
	 * 管理机构号
	 */
	private String mgrBranchNo;
	/**
	 * 款项所有人
	 */
	private String mioCustName;
	/**
	 * 生成应收付操作员机构
	 */
	private String gclkBranchNo;
	/**
	 * 生成应收付操作员代码
	 */
	private String gclkClerkNo;
	/**
	 * 不可送划原因
	 */
	private String stopTransReason;
	/**
	 * 不可送划起日期
	 */
	private Date stopTransDate;
	/**
	 * 恢复送划日期
	 */
	private Date reTransDate;
	/**
	 * 是否冻结应收付表标识
	 */
	private int lockFlag;
	/**
	 * 是否送划标记
	 */
	private int transFlag;
	/**
	 * 录入时间
	 */
	private Date enterTime;
	/**
	 * 录入机构
	 */
	private String enterBranchNo;
	/**
	 * 录入工号
	 */
	private String enterClerkNo;
	/**
	 * 是否撤消标记
	 */
	private int cancelFlag;
	/**
	 * 撤消时间
	 */
	private Date cancelTime;
	/**
	 * 撤消机构号
	 */
	private String cancelBranchNo;
	/**
	 * 撤消工号
	 */
	private String cancelClerkNo;
	/**
	 * 撤消理由
	 */
	private String cancelReason;
	/**
	 * 失效标志
	 */
	private int invalidStat;
	public long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
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
	public Date getPlnmioDate() {
		return plnmioDate;
	}
	public void setPlnmioDate(Date plnmioDate) {
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
	public Date getStopTransDate() {
		return stopTransDate;
	}
	public void setStopTransDate(Date stopTransDate) {
		this.stopTransDate = stopTransDate;
	}
	public Date getReTransDate() {
		return reTransDate;
	}
	public void setReTransDate(Date reTransDate) {
		this.reTransDate = reTransDate;
	}
	public int getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(int lockFlag) {
		this.lockFlag = lockFlag;
	}
	public int getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(int transFlag) {
		this.transFlag = transFlag;
	}
	public Date getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Date enterTime) {
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
	public int getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(int cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
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
	public int getInvalidStat() {
		return invalidStat;
	}
	public void setInvalidStat(int invalidStat) {
		this.invalidStat = invalidStat;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
