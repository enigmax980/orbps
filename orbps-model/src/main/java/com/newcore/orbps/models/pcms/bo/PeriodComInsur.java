package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class PeriodComInsur implements Serializable {

	private static final long serialVersionUID = -5930800610811574291L;
	
	private String cntrNo;
	private String custName;
	/*
	 * 单件保单人数
	 */
	@NotNull(message="[单件保单人数不能为空]")
	private Long num;
	private String remark;
	public String getCntrNo() {
		return cntrNo;
	}
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "PeriodComInsur [cntrNo=" + cntrNo + ", custName=" + custName + ", num=" + num + ", remark=" + remark
				+ "]";
	}
}
