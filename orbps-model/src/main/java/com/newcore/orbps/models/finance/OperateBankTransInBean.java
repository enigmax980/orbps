package com.newcore.orbps.models.finance;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * 银行转账数据
 * 
 * @author lijifei 2017年3月3日 17:20:45
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_DETAILQUERY_SCXE")
public class OperateBankTransInBean implements Serializable {

	/**
	 * 银行代码
	 */
	@XmlElement(name = "BANK_CODE")
	private String bankCode;

	/**
	 * 操作项目
	 */
	@XmlElement(name = "OP_FLAG")
	private String opFlag;

	/**
	 * 待转帐数据生成起始日期
	 */
	@XmlElement(name = "GEN_SDATE")
	private String genSdate;

	/**
	 * 待转帐数据生成截至日期
	 */
	@XmlElement(name = "GEN_EDATE")
	private String genEdate;

	/**
	 * 管理机构
	 */
	@XmlElement(name = "MGR_BRANCH_NO")
	private String mgrBranchNo;


	/**
	 * 是否包含下级机构
	 */
	@XmlElement(name = "BR_DOWN")
	private String brDown;

	/**
	 * 收付费标识
	 */
	@XmlElement(name = "MIO_CLASS")
	private String mioClass;

	/**
	 * 系统代码
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;

	/**
	 * 文本批次号
	 */
	@XmlElement(name = "TRANS_BAT_SEQ")
	private String transBatSeq;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOpFlag() {
		return opFlag;
	}

	public void setOpFlag(String opFlag) {
		this.opFlag = opFlag;
	}

	public String getGenSdate() {
		return genSdate;
	}

	public void setGenSdate(String genSdate) {
		this.genSdate = genSdate;
	}

	public String getGenEdate() {
		return genEdate;
	}

	public void setGenEdate(String genEdate) {
		this.genEdate = genEdate;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getBrDown() {
		return brDown;
	}

	public void setBrDown(String brDown) {
		this.brDown = brDown;
	}

	public String getMioClass() {
		return mioClass;
	}

	public void setMioClass(String mioClass) {
		this.mioClass = mioClass;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getTransBatSeq() {
		return transBatSeq;
	}

	public void setTransBatSeq(String transBatSeq) {
		this.transBatSeq = transBatSeq;
	}

	public OperateBankTransInBean() {
		super();
		// TODO Auto-generated constructor stub
	}





}
