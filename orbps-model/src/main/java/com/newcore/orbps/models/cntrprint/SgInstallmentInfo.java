package com.newcore.orbps.models.cntrprint;

import java.io.Serializable;

public class SgInstallmentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 832659265149456185L;

	private String moneyinItrvlDesc; //缴费方式描述
	private String premExtUnitDesc; //缴费宽限期单位描述
	private int premExtAmnt; //缴费宽限期数值
	public String getMoneyinItrvlDesc() {
		return moneyinItrvlDesc;
	}
	public void setMoneyinItrvlDesc(String moneyinItrvlDesc) {
		this.moneyinItrvlDesc = moneyinItrvlDesc;
	}
	public String getPremExtUnitDesc() {
		return premExtUnitDesc;
	}
	public void setPremExtUnitDesc(String premExtUnitDesc) {
		this.premExtUnitDesc = premExtUnitDesc;
	}
	public int getPremExtAmnt() {
		return premExtAmnt;
	}
	public void setPremExtAmnt(int premExtAmnt) {
		this.premExtAmnt = premExtAmnt;
	}

}
