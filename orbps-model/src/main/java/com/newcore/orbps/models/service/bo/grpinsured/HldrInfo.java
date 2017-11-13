package com.newcore.orbps.models.service.bo.grpinsured;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保人信息
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午11:30:17
 */
public class HldrInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6923189470194922035L;
	
	//投保人cmds客户号,长度：25，是否必填：否
	private String hldrPartyId;

	/* 字段名：投保人客户号 */
	private String hldrCustNo;

	/* 字段名：投保人姓名，长度：200，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private String hldrName;

	/* 字段名：投保人性别，长度：1，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private String hldrSex;

	/* 字段名：投保人出生日期，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private Date hldrBirthDate;

	/* 字段名：投保人证件类别，长度：1，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private String hldrIdType;

	/* 字段名：投保人证件号码，长度：8-32，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private String hldrIdNo;
	
	/* 字段名：投保人手机号码，长度：32 */
	private String hldrMobilePhone; 
	
	/**
	 * @return the hldrPartyId
	 */
	public String getHldrPartyId() {
		return hldrPartyId;
	}

	/**
	 * @param hldrPartyId the hldrPartyId to set
	 */
	public void setHldrPartyId(String hldrPartyId) {
		this.hldrPartyId = hldrPartyId;
	}

	/**
	 * @return the hldrCustNo
	 */
	public String getHldrCustNo() {
		return hldrCustNo;
	}

	/**
	 * @param hldrCustNo
	 *            the hldrCustNo to set
	 */
	public void setHldrCustNo(String hldrCustNo) {
		this.hldrCustNo = hldrCustNo;
	}

	/**
	 * @return the hldrName
	 */
	public String getHldrName() {
		return hldrName;
	}

	/**
	 * @param hldrName
	 *            the hldrName to set
	 */
	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}

	/**
	 * @return the hldrSex
	 */
	public String getHldrSex() {
		return hldrSex;
	}

	/**
	 * @param hldrSex
	 *            the hldrSex to set
	 */
	public void setHldrSex(String hldrSex) {
		this.hldrSex = hldrSex;
	}

	/**
	 * @return the hldrBirthDate
	 */
	public Date getHldrBirthDate() {
		return hldrBirthDate;
	}

	/**
	 * @param hldrBirthDate
	 *            the hldrBirthDate to set
	 */
	public void setHldrBirthDate(Date hldrBirthDate) {
		this.hldrBirthDate = hldrBirthDate;
	}

	/**
	 * @return the hldrIdType
	 */
	public String getHldrIdType() {
		return hldrIdType;
	}

	/**
	 * @param hldrIdType
	 *            the hldrIdType to set
	 */
	public void setHldrIdType(String hldrIdType) {
		this.hldrIdType = hldrIdType;
	}

	/**
	 * @return the hldrIdNo
	 */
	public String getHldrIdNo() {
		return hldrIdNo;
	}

	/**
	 * @param hldrIdNo
	 *            the hldrIdNo to set
	 */
	public void setHldrIdNo(String hldrIdNo) {
		this.hldrIdNo = hldrIdNo;
	}

	/**
	 * @return the hldrMoblePhone
	 */
	public String getHldrMobilePhone() {
		return hldrMobilePhone;
	}

	/**
	 * @param hldrMoblePhone the hldrMoblePhone to set
	 */
	public void setHldrMobilePhone(String hldrMobilePhone) {
		this.hldrMobilePhone = hldrMobilePhone;
	}

}
