package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 银行转账数据
 * 
 * @author lijifei 2017年3月3日 17:27:45
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESPONSE")
public class BankZzczScxe implements Serializable {


	/**
	 * 操作项目
	 */
	@XmlElement(name = "OP_CODE")
	private String opCode;

	/**
	 * 操作类别
	 */
	@XmlElement(name = "OP_CLASS")
	private String opClass;

	/**
	 * 银行代码
	 */
	@XmlElement(name = "BANK_CODE")
	private String bankCode;


	/**
	 * 系统代码
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;

	/**
	 * 收付费标识
	 */
	@XmlElement(name = "MIO_CLASS")
	private String mioClass;

	/**
	 * 文本批次号
	 */
	@XmlElement(name = "TRANS_BAT_SEQ")
	private String transBatSeq;

	/**
	 * 转账结果 
	 */
	@XmlElement(name = "TRANS_STAT_CHN")
	private String transStatChn;

	/**
	 * 笔数
	 */
	@XmlElement(name = "TRANS_COUNT")
	private String transCount;

	/**
	 * 金额  
	 */
	@XmlElement(name = "TRANS_AMNT")
	private String transAmnt;

	/**
	 * 最小生成日期
	 */
	@XmlElement(name = "MIN_GEN_DATE")
	private String minGenDate;

	/**
	 * 最大生成日期
	 */
	@XmlElement(name = "MAX_GEN_DATE")
	private String maxGenDate;

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getOpClass() {
		return opClass;
	}

	public void setOpClass(String opClass) {
		this.opClass = opClass;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getMioClass() {
		return mioClass;
	}

	public void setMioClass(String mioClass) {
		this.mioClass = mioClass;
	}

	public String getTransBatSeq() {
		return transBatSeq;
	}

	public void setTransBatSeq(String transBatSeq) {
		this.transBatSeq = transBatSeq;
	}

	public String getTransStatChn() {
		return transStatChn;
	}

	public void setTransStatChn(String transStatChn) {
		this.transStatChn = transStatChn;
	}

	public String getTransCount() {
		return transCount;
	}

	public void setTransCount(String transCount) {
		this.transCount = transCount;
	}

	public String getTransAmnt() {
		return transAmnt;
	}

	public void setTransAmnt(String transAmnt) {
		this.transAmnt = transAmnt;
	}

	public String getMinGenDate() {
		return minGenDate;
	}

	public void setMinGenDate(String minGenDate) {
		this.minGenDate = minGenDate;
	}

	public String getMaxGenDate() {
		return maxGenDate;
	}

	public void setMaxGenDate(String maxGenDate) {
		this.maxGenDate = maxGenDate;
	}

	public BankZzczScxe() {
		super();
		// TODO Auto-generated constructor stub
	}






}
