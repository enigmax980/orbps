package com.newcore.orbps.models.cntrprint;

import java.io.Serializable;
import java.util.Date;

public class SgMultiPayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5196581907646440821L;

	private int multiPayTimes; //暂交保费年数
	private double multiPayPrem; //暂交保费金额
	private String cntrExpiryDate; //合同满期日期 X月X日
	private int renewTimes; //可连续投保年数
	
	public int getMultiPayTimes() {
		return multiPayTimes;
	}
	public void setMultiPayTimes(int multiPayTimes) {
		this.multiPayTimes = multiPayTimes;
	}
	public double getMultiPayPrem() {
		return multiPayPrem;
	}
	public void setMultiPayPrem(double multiPayPrem) {
		this.multiPayPrem = multiPayPrem;
	}
	public String getCntrExpiryDate() {
		return cntrExpiryDate;
	}
	public void setCntrExpiryDate(String cntrExpiryDate) {
		this.cntrExpiryDate = cntrExpiryDate;
	}
	public int getRenewTimes() {
		return renewTimes;
	}
	public void setRenewTimes(int renewTimes) {
		this.renewTimes = renewTimes;
	}
	
}
