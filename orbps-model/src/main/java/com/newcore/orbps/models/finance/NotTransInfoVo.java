package com.newcore.orbps.models.finance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangxiao
 * 创建时间：2017年2月27日上午10:02:45
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_NOTBANKMC_SCXE")
public class NotTransInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314105384264384986L;
	/**
	 * 保单号
	 */
	@XmlElement(name = "CNTR_NO")
	private String cntrNo;
	/**
	 * 应收付起始时间
	 */
	@XmlElement(name = "PLNMIO_SDATE")
	private String plnmioSdate;
	/**
	 * 应收付截至时间
	 */
	@XmlElement(name = "PLNMIO_EDATE")
	private String plnmioEdate;
	/**
	 * 系统代码：空为未选
	 */
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/**
	 * 收付费标识：
	 */
	@XmlElement(name = "MIO_CLASS")
	private String mioClass;
	/**
	 * 首续期标识：
	 */
	@XmlElement(name = "FA_FLAG")
	private String faFlag;
	/**
	 * 收付项目
	 */
	@XmlElement(name = "MIO_ITEM_CODE")
	private String mioItemCode;
	/**
	 * 查询起讫日期
	 */
	@XmlElement(name = "BEGIN_DATE")
	private String beginDate;
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
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	
}
