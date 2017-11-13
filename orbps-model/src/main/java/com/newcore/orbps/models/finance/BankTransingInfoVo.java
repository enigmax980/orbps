package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangxiao
 * 创建时间：2017年2月27日下午3:25:42
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_SLEDET_SCXE")
public class BankTransingInfoVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4440670905033043355L;
	/**
	 *保单号码
	 */
	@XmlElement(name = "CNTR_NO")
	private String cntrNo;
	/**
	 *账户户名
	 */
	@XmlElement(name = "ACC_CUST_NAME")
	private String accCustName;
	/**
	 *证件号码
	 */
	@XmlElement(name = "BANKACC_ID_NO")
	private String bankaccIdNo;
	/**
	 *银行帐号
	 */
	@XmlElement(name = "BANK_ACC_NO")
	private String bankAccNo;
	/**
	 *银行代码
	 */
	@XmlElement(name = "BANK_CODE")
	private String bankCode;
	/**
	 *收付费项目
	 */
	@XmlElement(name = "MIO_ITEM_CODE")
	private String mioItemCode;
	/**
	 *应收付日期起讫日期YYYY-MM-DD
	 */
	@XmlElement(name = "PLNMIO_SDATE")
	private String plnmioSdate;
	/**
	 *应收付日期截至日期YYYY-MM-DD
	 */
	@XmlElement(name = "PLNMIO_EDATE")
	private String plnmioEdate;
	/**
	 *转账起始日期YYYY-MM-DD
	 */
	@XmlElement(name = "MIO_SDATE")
	private String mioSdate;
	/**
	 *转账截至日期YYYY-MM-DD
	 */
	@XmlElement(name = "MIO_EDATE")
	private String mioEdate;
	/**
	 *系统代码--必输（前台校验）
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/**
	 *收付费标识
	 */
	@XmlElement(name = "MIO_CLASS")
	private String mioClass;
	/**
	 *首期续期标识
	 */
	@XmlElement(name = "FA_FLAG")
	private String faFlag;
	/**
	 *文本批次号
	 */
	@XmlElement(name = "TRANS_BAT_SEQ")
	private String transBatSeq;
	/**
	 *是否入账--必输（前台校验）
	 */
	@XmlElement(name = "ACT_FLAG")
	private String actFlag;
	/**
	 *成功标识
	 */
	@XmlElement(name = "SUCCESS_STAT")
	private String successStat;
	/**
	 * @return the cntrNo
	 */
	public String getCntrNo() {
		return cntrNo;
	}
	/**
	 * @param cntrNo the cntrNo to set
	 */
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	/**
	 * @return the accCustName
	 */
	public String getAccCustName() {
		return accCustName;
	}
	/**
	 * @param accCustName the accCustName to set
	 */
	public void setAccCustName(String accCustName) {
		this.accCustName = accCustName;
	}
	/**
	 * @return the bankaccIdNo
	 */
	public String getBankaccIdNo() {
		return bankaccIdNo;
	}
	/**
	 * @param bankaccIdNo the bankaccIdNo to set
	 */
	public void setBankaccIdNo(String bankaccIdNo) {
		this.bankaccIdNo = bankaccIdNo;
	}
	/**
	 * @return the bankAccNo
	 */
	public String getBankAccNo() {
		return bankAccNo;
	}
	/**
	 * @param bankAccNo the bankAccNo to set
	 */
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the mioItemCode
	 */
	public String getMioItemCode() {
		return mioItemCode;
	}
	/**
	 * @param mioItemCode the mioItemCode to set
	 */
	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}
	/**
	 * @return the plnmioSdate
	 */
	public String getPlnmioSdate() {
		return plnmioSdate;
	}
	/**
	 * @param plnmioSdate the plnmioSdate to set
	 */
	public void setPlnmioSdate(String plnmioSdate) {
		this.plnmioSdate = plnmioSdate;
	}
	/**
	 * @return the plnmioEdate
	 */
	public String getPlnmioEdate() {
		return plnmioEdate;
	}
	/**
	 * @param plnmioEdate the plnmioEdate to set
	 */
	public void setPlnmioEdate(String plnmioEdate) {
		this.plnmioEdate = plnmioEdate;
	}
	/**
	 * @return the mioSdate
	 */
	public String getMioSdate() {
		return mioSdate;
	}
	/**
	 * @param mioSdate the mioSdate to set
	 */
	public void setMioSdate(String mioSdate) {
		this.mioSdate = mioSdate;
	}
	/**
	 * @return the mioEdate
	 */
	public String getMioEdate() {
		return mioEdate;
	}
	/**
	 * @param mioEdate the mioEdate to set
	 */
	public void setMioEdate(String mioEdate) {
		this.mioEdate = mioEdate;
	}
	/**
	 * @return the sysNo
	 */
	public String getSysNo() {
		return sysNo;
	}
	/**
	 * @param sysNo the sysNo to set
	 */
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	/**
	 * @return the mioClass
	 */
	public String getMioClass() {
		return mioClass;
	}
	/**
	 * @param mioClass the mioClass to set
	 */
	public void setMioClass(String mioClass) {
		this.mioClass = mioClass;
	}
	/**
	 * @return the faFlag
	 */
	public String getFaFlag() {
		return faFlag;
	}
	/**
	 * @param faFlag the faFlag to set
	 */
	public void setFaFlag(String faFlag) {
		this.faFlag = faFlag;
	}
	/**
	 * @return the transBatSeq
	 */
	public String getTransBatSeq() {
		return transBatSeq;
	}
	/**
	 * @param transBatSeq the transBatSeq to set
	 */
	public void setTransBatSeq(String transBatSeq) {
		this.transBatSeq = transBatSeq;
	}
	/**
	 * @return the actFlag
	 */
	public String getActFlag() {
		return actFlag;
	}
	/**
	 * @param actFlag the actFlag to set
	 */
	public void setActFlag(String actFlag) {
		this.actFlag = actFlag;
	}
	/**
	 * @return the successStat
	 */
	public String getSuccessStat() {
		return successStat;
	}
	/**
	 * @param successStat the successStat to set
	 */
	public void setSuccessStat(String successStat) {
		this.successStat = successStat;
	}

}
