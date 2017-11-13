package com.newcore.orbps.models.cntrprint;

import java.io.Serializable;

public class PmsDataInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5691853237475369271L;

	private SgMultiPayInfo sgMultiPayInfo;
	private SgInstallmentInfo sgInstallmentInfo;
	
	public SgMultiPayInfo getSgMultiPayInfo() {
		return sgMultiPayInfo;
	}
	public void setSgMultiPayInfo(SgMultiPayInfo sgMultiPayInfo) {
		this.sgMultiPayInfo = sgMultiPayInfo;
	}
	public SgInstallmentInfo getSgInstallmentInfo() {
		return sgInstallmentInfo;
	}
	public void setSgInstallmentInfo(SgInstallmentInfo sgInstallmentInfo) {
		this.sgInstallmentInfo = sgInstallmentInfo;
	}
	
}
