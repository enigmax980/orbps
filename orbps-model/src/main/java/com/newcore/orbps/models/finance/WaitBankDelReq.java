package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huanglong
 * @date 2017年2月27日
 * @content 待转账数据删除请求
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_WAITBANKDEL_SCXE")
public class WaitBankDelReq implements Serializable{

	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -2258085681995266749L;
	/*字段名称:系统号,长度:,是否必填:否*/
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/*字段名称:银行代码,长度:,是否必填:否*/
	@XmlElement(name = "BANK_CODE")
	private String bankCode;
	/*字段名称:序号,长度:,是否必填:否*/
	@XmlElement(name = "TRANS_CODE")
	private String transCode;
	public WaitBankDelReq() {
		super();
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
	 * @return the transCode
	 */
	public String getTransCode() {
		return transCode;
	}
	/**
	 * @param transCode the transCode to set
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
}
