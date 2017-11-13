package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 暂停送划登记查询请求报文类
 * @author LiSK
 *
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_NOTTOBANKSEL_SCXE")
public class QueryPauseTransInfoBean implements Serializable {
	/**
	 * 系统号 
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/**
	 * 合同号
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
	 * 收付类型
	 */
	@XmlElement(name = "MIO_CLASS")
	private int mioClass;
	/**
	 * 收付费项目
	 */
	@XmlElement(name = "MIO_ITEM_CODE")
	private String mioItemCode;
	/**
	 * 银行代码
	 */
	@XmlElement(name = "BANK_CODE")
	private String bankCode;
	/**
	 * 银行账号
	 */
	@XmlElement(name = "BANK_ACC_NO")
	private String bankAccNo;
	/**
	 * 金额
	 */
	@XmlElement(name = "AMNT")
	private Double amnt;
	/**
	 * 管理机构
	 */
	@XmlElement(name = "MGR_BRANCH_NO")
	private String mgrBranchNo;
	/**
	 * 录入起始日期
	 */
	@XmlElement(name = "ENTER_SDATE")
	private String enterSdate;
	/**
	 * 录入截止日期
	 */
	@XmlElement(name = "ENTER_EDATE")
	private String enterEdate;
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
	 * 撤消起始日期
	 */
	@XmlElement(name = "CANCEL_SDATE")
	private String cancelSdate;
	/**
	 * 撤消截止日期
	 */
	@XmlElement(name = "CANCEL_EDATE")
	private String cancelEdate;
	/**
	 * 撤消机构
	 */
	@XmlElement(name = "CANCEL_BRANCH_NO")
	private String cancelBranchNo;
	/**
	 * 撤消工号
	 */
	@XmlElement(name = "CANCEL_CLERK_NO")
	private String cancelClerkNo;
	/**
	 * 处理标志
	 */
	@XmlElement(name = "BANKREG_FLAG")
	private int bankRegFlag;
	/**
	 * 不转账类别
	 */
	@XmlElement(name = "TRANS_FLAG")
	private int transFlag;
	/**
	 * 翻页标志:单次查询起始条数
	 */
	@XmlElement(name = "MAX_ID")
	private long maxId;
	
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
	public Double getAmnt() {
		return amnt;
	}
	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getEnterSdate() {
		return enterSdate;
	}
	public void setEnterSdate(String enterSdate) {
		this.enterSdate = enterSdate;
	}
	public String getEnterEdate() {
		return enterEdate;
	}
	public void setEnterEdate(String enterEdate) {
		this.enterEdate = enterEdate;
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
	public String getCancelSdate() {
		return cancelSdate;
	}
	public void setCancelSdate(String cancelSdate) {
		this.cancelSdate = cancelSdate;
	}
	public String getCancelEdate() {
		return cancelEdate;
	}
	public void setCancelEdate(String cancelEdate) {
		this.cancelEdate = cancelEdate;
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
	public int getBankRegFlag() {
		return bankRegFlag;
	}
	public void setBankRegFlag(int bankRegFlag) {
		this.bankRegFlag = bankRegFlag;
	}
	public int getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(int transFlag) {
		this.transFlag = transFlag;
	}
	public long getMaxId() {
		return maxId;
	}
	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}
	
	
	
}




