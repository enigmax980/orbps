package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收付账户日志表
 * @author JCC 2017年2月14日 13:42:34
 */
public class MioAccInfoLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private long accLogId;	//账户明细标识
	private long accId;		//账户标识
	private Date createTime;//发生日期
	private String mioItemCode;	//收付项目代码
	private int mioClass; 	//收付类型： 1：收，-1：付
	private BigDecimal anmt;//收付金额
	private String remark;	//备注
	public long getAccLogId() {
		return accLogId;
	}
	public void setAccLogId(long accLogId) {
		this.accLogId = accLogId;
	}
	public long getAccId() {
		return accId;
	}
	public void setAccId(long accId) {
		this.accId = accId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMioItemCode() {
		return mioItemCode;
	}
	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}
	public int getMioClass() {
		return mioClass;
	}
	public void setMioClass(int mioClass) {
		this.mioClass = mioClass;
	}
	public BigDecimal getAnmt() {
		return anmt;
	}
	public void setAnmt(BigDecimal anmt) {
		this.anmt = anmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
