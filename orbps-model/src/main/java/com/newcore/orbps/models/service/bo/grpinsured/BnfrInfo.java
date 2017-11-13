package com.newcore.orbps.models.service.bo.grpinsured;

import java.io.Serializable;
import java.util.Date;

/**
 * 受益人信息
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日下午2:42:13
 */
public class BnfrInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5841059434982055776L;

	/* 字段名：受益人cmds客户号，长度：25，是否必录：否 */
	private String bnfrPartyId;

	/* 字段名：受益人客户号，长度：25，是否必录：否 */
	private String bnfrCustNo;

	/* 字段名：受益人姓名，长度：200，是否必录：存在受益人时，必录*/
	private String bnfrName;

	/* 字段名：受益人受益顺序，是否必录：存在受益人时，必录 */
	private Long bnfrLevel;

	/* 字段名：受益人受益份额，是否必录：存在受益人时，必录 */
	private Double bnfrProfit;

	/* 字段名：受益人性别，长度：1，是否必录：存在受益人时，必录 */
	private String bnfrSex;

	/* 字段名：受益人出生日期，是否必录：否 */
	private Date bnfrBirthDate;

	/* 字段名：受益人证件类别，长度：2，是否必录：否 */
	private String bnfrIdType;

	/* 字段名：受益人证件号码，长度：18，是否必录：否 */
	private String bnfrIdNo;

	/* 字段名：与被保人关系，长度：2，是否必录：存在受益人时，必录 */
	private String bnfrRtIpsn;
	
	/* 字段名：受益人手机号，长度：32 */
	private String bnfrMobilePhone;

	/**
	 * @return the bnfrPartyId
	 */
	public String getBnfrPartyId() {
		return bnfrPartyId;
	}

	/**
	 * @param bnfrPartyId the bnfrPartyId to set
	 */
	public void setBnfrPartyId(String bnfrPartyId) {
		this.bnfrPartyId = bnfrPartyId;
	}

	/**
	 * @return the bnfrCustNo
	 */
	public String getBnfrCustNo() {
		return bnfrCustNo;
	}

	/**
	 * @param bnfrCustNo
	 *            the bnfrCustNo to set
	 */
	public void setBnfrCustNo(String bnfrCustNo) {
		this.bnfrCustNo = bnfrCustNo;
	}

	/**
	 * @return the bnfrName
	 */
	public String getBnfrName() {
		return bnfrName;
	}

	/**
	 * @param bnfrName
	 *            the bnfrName to set
	 */
	public void setBnfrName(String bnfrName) {
		this.bnfrName = bnfrName;
	}

	/**
	 * @return the bnfrLevel
	 */
	public Long getBnfrLevel() {
		return bnfrLevel;
	}

	/**
	 * @param bnfrLevel the bnfrLevel to set
	 */
	public void setBnfrLevel(Long bnfrLevel) {
		this.bnfrLevel = bnfrLevel;
	}

	/**
	 * @return the bnfrProfit
	 */
	public Double getBnfrProfit() {
		return bnfrProfit;
	}

	/**
	 * @param bnfrProfit
	 *            the bnfrProfit to set
	 */
	public void setBnfrProfit(Double bnfrProfit) {
		this.bnfrProfit = bnfrProfit;
	}

	/**
	 * @return the bnfrSex
	 */
	public String getBnfrSex() {
		return bnfrSex;
	}

	/**
	 * @param bnfrSex
	 *            the bnfrSex to set
	 */
	public void setBnfrSex(String bnfrSex) {
		this.bnfrSex = bnfrSex;
	}

	/**
	 * @return the bnfrBirthDate
	 */
	public Date getBnfrBirthDate() {
		return bnfrBirthDate;
	}

	/**
	 * @param bnfrBirthDate
	 *            the bnfrBirthDate to set
	 */
	public void setBnfrBirthDate(Date bnfrBirthDate) {
		this.bnfrBirthDate = bnfrBirthDate;
	}

	/**
	 * @return the bnfrIdType
	 */
	public String getBnfrIdType() {
		return bnfrIdType;
	}

	/**
	 * @param bnfrIdType
	 *            the bnfrIdType to set
	 */
	public void setBnfrIdType(String bnfrIdType) {
		this.bnfrIdType = bnfrIdType;
	}

	/**
	 * @return the bnfrIdNo
	 */
	public String getBnfrIdNo() {
		return bnfrIdNo;
	}

	/**
	 * @param bnfrIdNo
	 *            the bnfrIdNo to set
	 */
	public void setBnfrIdNo(String bnfrIdNo) {
		this.bnfrIdNo = bnfrIdNo;
	}

	/**
	 * @return the bnfrRtIpsn
	 */
	public String getBnfrRtIpsn() {
		return bnfrRtIpsn;
	}

	/**
	 * @param bnfrRtIpsn
	 *            the bnfrRtIpsn to set
	 */
	public void setBnfrRtIpsn(String bnfrRtIpsn) {
		this.bnfrRtIpsn = bnfrRtIpsn;
	}

	/**
	 * @return the bnfrMoblePhone
	 */
	public String getBnfrMobilePhone() {
		return bnfrMobilePhone;
	}

	/**
	 * @param bnfrMoblePhone the bnfrMoblePhone to set
	 */
	public void setBnfrMobilePhone(String bnfrMobilePhone) {
		this.bnfrMobilePhone = bnfrMobilePhone;
	}

}
