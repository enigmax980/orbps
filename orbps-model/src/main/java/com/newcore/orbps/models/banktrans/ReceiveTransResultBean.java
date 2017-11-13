package com.newcore.orbps.models.banktrans;

public class ReceiveTransResultBean {
	/**
	 * 收付费唯一标识
	 */
	private String miosTransCode;
	/**
	 * 转账序号
	 */
	private Long transCode;
	/**
	 * 转账状态
	 */
	private String transStat;

	public ReceiveTransResultBean(String miosTransCode, Long transCode,
			String transStat) {
		this.miosTransCode = miosTransCode;
		this.transCode = transCode;
		this.transStat = transStat;
	}

	public String getMiosTransCode() {
		return miosTransCode;
	}

	public Long getTransCode() {
		return transCode;
	}

	public String getTransStat() {
		return transStat;
	}

	public void setMiosTransCode(String miosTransCode) {
		this.miosTransCode = miosTransCode;
	}

	public void setTransCode(Long transCode) {
		this.transCode = transCode;
	}

	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}
}