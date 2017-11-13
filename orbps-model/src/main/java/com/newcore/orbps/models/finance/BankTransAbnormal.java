package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行转账异常入账实体类
 * 
 * @author 李四魁
 *
 */
public class BankTransAbnormal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 应收付标识
	 */
	private long plnmioRecId;
	/**
	 * 保单号码
	 */
	private String cntrNo;
	/**
	 * 入账日期
	 */
	private Date actDate;
	/**
	 * 入账项目
	 */
	private String actItemCode;
	/**
	 * 入账金额
	 */
	private BigDecimal actAmnt;
	/**
	 * 收付费类别
	 */
	private int mioClass;
	/**
	 * 原应收付费项目
	 */
	private String mioItemCode;
	/**
	 * 原应收日期
	 */
	private Date plnmioDate;
	/**
	 * 原应收金额
	 */
	private BigDecimal amnt;
	/**
	 * 客户名
	 */
	private String mioCustName;
	/**
	 * 转账表流水号
	 */
	private long transCode;
	/**
	 * 说明
	 */
	private String remark;
	public long getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	public String getCntrNo() {
		return cntrNo;
	}
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	public Date getActDate() {
		return actDate;
	}
	public void setActDate(Date actDate) {
		this.actDate = actDate;
	}
	public String getActItemCode() {
		return actItemCode;
	}
	public void setActItemCode(String actItemCode) {
		this.actItemCode = actItemCode;
	}
	public BigDecimal getActAmnt() {
		return actAmnt;
	}
	public void setActAmnt(BigDecimal actAmnt) {
		this.actAmnt = actAmnt;
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
	public Date getPlnmioDate() {
		return plnmioDate;
	}
	public void setPlnmioDate(Date plnmioDate) {
		this.plnmioDate = plnmioDate;
	}
	public BigDecimal getAmnt() {
		return amnt;
	}
	public void setAmnt(BigDecimal amnt) {
		this.amnt = amnt;
	}
	public String getMioCustName() {
		return mioCustName;
	}
	public void setMioCustName(String mioCustName) {
		this.mioCustName = mioCustName;
	}
	public long getTransCode() {
		return transCode;
	}
	public void setTransCode(long transCode) {
		this.transCode = transCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
