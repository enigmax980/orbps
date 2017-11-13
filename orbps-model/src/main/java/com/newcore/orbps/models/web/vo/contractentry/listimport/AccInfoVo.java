package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;

/**
 * 缴费账户
 * @author 王鸿林
 *
 * @date 2016年11月23日 下午4:47:35
 * @TODO
 */
public class AccInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5339211441157066737L;

	/* 字段名：账户顺序，是否必填：IF 团单销售员共同展业 THEN 必填 */
	private Long seqNo;

	/* 字段名：个人扣款金额，是否必填：否 */
	private Double ipsnPayAmnt;

	/* 字段名：个人交费比例 */
	private Double ipsnPayPct;

	/* 字段名：缴费账户，长度：200，是否必填：否 */
	private String bankAccName;

	/* 字段名：缴费银行，长度：8，是否必填：否 */
	private String bankCode;

	/* 字段名：缴费账号，长度：48，是否必填：否 */
	private String bankAccNo;

	/**
	 * @return the seqNo
	 */
	public Long getSeqNo() {
		return seqNo;
	}

	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * @return the ipsnPayAmnt
	 */
	public Double getIpsnPayAmnt() {
		return ipsnPayAmnt;
	}

	/**
	 * @param ipsnPayAmnt the ipsnPayAmnt to set
	 */
	public void setIpsnPayAmnt(Double ipsnPayAmnt) {
		this.ipsnPayAmnt = ipsnPayAmnt;
	}

	/**
	 * @return the ipsnPayPct
	 */
	public Double getIpsnPayPct() {
		return ipsnPayPct;
	}

	/**
	 * @param ipsnPayPct the ipsnPayPct to set
	 */
	public void setIpsnPayPct(Double ipsnPayPct) {
		this.ipsnPayPct = ipsnPayPct;
	}

	/**
	 * @return the bankAccName
	 */
	public String getBankAccName() {
		return bankAccName;
	}

	/**
	 * @param bankAccName the bankAccName to set
	 */
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
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
	 * @return the bankAccNo
	 */
	public String getBankAccNo() {
		return bankAccNo;
	}

	/**
	 * @param bankAccNo the bankAccNo to set
	 */
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccInfo [seqNo=" + seqNo + ", ipsnPayAmnt=" + ipsnPayAmnt + ", ipsnPayPct=" + ipsnPayPct
				+ ", bankAccName=" + bankAccName + ", bankCode=" + bankCode + ", bankAccNo=" + bankAccNo + "]";
	}
	
}
