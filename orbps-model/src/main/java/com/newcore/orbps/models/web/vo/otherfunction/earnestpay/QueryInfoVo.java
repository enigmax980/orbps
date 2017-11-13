package com.newcore.orbps.models.web.vo.otherfunction.earnestpay;

import java.io.Serializable;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;


/**
 * 订正界面Vo
 * @author wangyanjie
 *
 */	

public class QueryInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 11114344544566L;

	/** 投保单号 */
	private String applNo;
	/** 被保人姓名 */
    private String ipsnName;
    /** 被保人证件类型 */
    private String ipsnIdType;
    /** 被保人证件号码 */
    private String ipsnIdNo;
    /** 被保人性别 */
    private String ipsnIdSex;
    /** 被保人出生日期 */
    private String birthDate;
    /** 单位名称 */
    private String grpName;
    /** 收费组组号 */
    private String payGrpNos;
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the ipsnName
	 */
	public String getIpsnName() {
		return ipsnName;
	}
	/**
	 * @param ipsnName the ipsnName to set
	 */
	public void setIpsnName(String ipsnName) {
		this.ipsnName = ipsnName;
	}
	/**
	 * @return the ipsnIdType
	 */
	public String getIpsnIdType() {
		return ipsnIdType;
	}
	/**
	 * @param ipsnIdType the ipsnIdType to set
	 */
	public void setIpsnIdType(String ipsnIdType) {
		this.ipsnIdType = ipsnIdType;
	}
	/**
	 * @return the ipsnIdNo
	 */
	public String getIpsnIdNo() {
		return ipsnIdNo;
	}
	/**
	 * @param ipsnIdNo the ipsnIdNo to set
	 */
	public void setIpsnIdNo(String ipsnIdNo) {
		this.ipsnIdNo = ipsnIdNo;
	}
	/**
	 * @return the ipsnIdSex
	 */
	public String getIpsnIdSex() {
		return ipsnIdSex;
	}
	/**
	 * @param ipsnIdSex the ipsnIdSex to set
	 */
	public void setIpsnIdSex(String ipsnIdSex) {
		this.ipsnIdSex = ipsnIdSex;
	}
	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the grpName
	 */
	public String getGrpName() {
		return grpName;
	}
	/**
	 * @param grpName the grpName to set
	 */
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	/**
	 * @return the payGrpNos
	 */
	public String getPayGrpNos() {
		return payGrpNos;
	}
	/**
	 * @param payGrpNos the payGrpNos to set
	 */
	public void setPayGrpNos(String payGrpNos) {
		this.payGrpNos = payGrpNos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryInfoVo [applNo=" + applNo + ", ipsnName=" + ipsnName + ", ipsnIdType=" + ipsnIdType + ", ipsnIdNo="
				+ ipsnIdNo + ", ipsnIdSex=" + ipsnIdSex + ", birthDate=" + birthDate + ", grpName=" + grpName
				+ ", payGrpNos=" + payGrpNos + "]";
	}
}
