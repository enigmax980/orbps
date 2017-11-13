package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 银行转账暂停送划封装解析的XML值
 * @author JCC
 * 2017年3月6日 14:53:05
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_NOTTOBANKTE_SCXE")
public class MiosNotToBankXML implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 系统号
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/**
	 * 保单号码
	 */
	@XmlElement(name = "CNTR_NO")
	private String cntrNo;
	/**
	 * 客户号
	 */
	@XmlElement(name = "CUST_NO")
	private String custNo;
	/**
	 * 应收付日期
	 */
	@XmlElement(name = "PLNMIO_DATE")
	private String plnmioDate;
	/**
	 * 收付费类别
	 */
	@XmlElement(name = "MIO_CLASS")
	private int mioClass;
	/**
	 * 收付费项目
	 */
	@XmlElement(name = "MIO_ITEM_CODE")
	private String mioItemCode;
	/**
	 * 银行代号
	 */
	@XmlElement(name = "BANK_CODE")
	private String bankCode;
	/**
	 * 帐号
	 */
	@XmlElement(name = "BANK_ACC_NO")
	private String bankAccNo;
	/**
	 * 金额
	 */
	@XmlElement(name = "AMNT")
	private String amnt;
	/**
	 * 管理机构号
	 */
	@XmlElement(name = "MGR_BRANCH_NO")
	private String mgrBranchNo;
	/**
	 * 应收付标识
	 */
	@XmlElement(name = "PLNMIO_REC_ID")
	private String plnmioRecId;
	/**
	 * 款项所有人
	 */
	@XmlElement(name = "MIO_CUST_NAME")
	private String mioCustName;
	/**
	 * 生成应收付操作员机构
	 */
	@XmlElement(name = "GCLK_BRANCH_NO")
	private String gclkBranchNo;
	/**
	 * 生成应收付操作员代码
	 */
	@XmlElement(name = "GCLK_CLERK_NO")
	private String gclkClerkNo;
	/**
	 * 不可送划原因
	 */
	@XmlElement(name = "STOP_TRANS_REASON")
	private String stopTransReason;
	/**
	 * 不可送划起日期
	 */
	@XmlElement(name = "STOP_TRANS_DATE")
	private String stopTransDate;
	/**
	 * 恢复送划日期
	 */
	@XmlElement(name = "RE_TRANS_DATE")
	private String reTransDate;
	/**
	 * 是否冻结应收付表标识
	 */
	@XmlElement(name = "LOCK_FLAG")
	private int lockFlag;
	/**
	 * 是否送划标记
	 */
	@XmlElement(name = "TRANS_FLAG")
	private int transFlag;
	/**
	 * 录入时间
	 */
	@XmlElement(name = "ENTER_TIME")
	private String enterTime;
	/**
	 * 录入机构
	 */
	@XmlElement(name = "ENTER_BRANCH_NO")
	private String enterBranchNo;
	/**
	 * 录入工号
	 */
	@XmlElement(name = "ENTER_CLERK_NO")
	private String enterClerkNo;
	/**
	 * 是否撤消标记
	 */
	@XmlElement(name = "CANCEL_FLAG")
	private int cancelFlag;
	/**
	 * 撤消时间
	 */
	@XmlElement(name = "CANCEL_TIME")
	private String cancelTime;
	/**
	 * 撤消机构号
	 */
	@XmlElement(name = "CANCEL_BRANCH_NO")
	private String cancelBranchNo;
	/**
	 * 撤消工号
	 */
	@XmlElement(name = "CANCEL_CLERK_NO")
	private String cancelClerkNo;
	/**
	 * 撤消理由
	 */
	@XmlElement(name = "CANCEL_REASON")
	private String cancelReason;
	/**
	 * 失效标志
	 */
	@XmlElement(name = "INVALID_STAT")
	private int invalIdStat;
	
	/**
	 * 是否转账途中
	 */
	@XmlElement(name = "HOLD_FLAG")
	private String holdFlag;
	
	/**
	 * 收付费形式
	 */
	@XmlElement(name = "MIO_TYPE")
	private String mioType;
	
	/**
	 * 选择标志
	 */
	@XmlElement(name = "SELFLAG")
	private String selflag;
	
	/**
	 * 处理标志
	 */
	@XmlElement(name = "BANKREG_FLAG")
	private String bankregFlag;
	
	/**
	 * 备注说明
	 */
	@XmlElement(name = "REMARK")
	private String remark;
	
	
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
	public String getAmnt() {
		return amnt;
	}
	public void setAmnt(String amnt) {
		this.amnt = amnt;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(String plnmioRecId) {
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
	public int getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(int cancelFlag) {
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
	public int getInvalIdStat() {
		return invalIdStat;
	}
	public void setInvalIdStat(int invalIdStat) {
		this.invalIdStat = invalIdStat;
	}
	
}
