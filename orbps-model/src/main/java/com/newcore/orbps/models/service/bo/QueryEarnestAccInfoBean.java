package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 暂收费支取功能-查询账户信息入参bo
 * 
 * @author lijifei 
 * 创建时间：2017年2月22日下午16:46:11
 */
public class QueryEarnestAccInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1622401463448515525L;

	/* 字段名：投保单号，长度：16，是否必填：是 */
	private String applNo;

	/* 字段名：组织架构树节点单位名称，长度：200，*/
	private String grpName;

	/* 字段名：收费组属组编号 集合， */
	private List<Long> feeGrpNoList;

	/* 字段名：被保险人姓名，长度：2-32， */
	private String ipsnName;

	/* 字段名：被保险人性别，长度：2， */
	private String ipsnSex;

	/* 字段名：被保人出生日期， */
	private Date ipsnBirthDate;

	/* 字段名：被保人证件类别，长度：1， */
	private String ipsnIdType;

	/* 字段名：被保人证件号码，长度：8-18，： */
	private String ipsnIdNo;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getGrpName() {
		return grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public List<Long> getFeeGrpNoList() {
		return feeGrpNoList;
	}

	public void setFeeGrpNoList(List<Long> feeGrpNoList) {
		this.feeGrpNoList = feeGrpNoList;
	}

	public String getIpsnName() {
		return ipsnName;
	}

	public void setIpsnName(String ipsnName) {
		this.ipsnName = ipsnName;
	}

	public String getIpsnSex() {
		return ipsnSex;
	}

	public void setIpsnSex(String ipsnSex) {
		this.ipsnSex = ipsnSex;
	}

	public Date getIpsnBirthDate() {
		return ipsnBirthDate;
	}

	public void setIpsnBirthDate(Date ipsnBirthDate) {
		this.ipsnBirthDate = ipsnBirthDate;
	}

	public String getIpsnIdType() {
		return ipsnIdType;
	}

	public void setIpsnIdType(String ipsnIdType) {
		this.ipsnIdType = ipsnIdType;
	}

	public String getIpsnIdNo() {
		return ipsnIdNo;
	}

	public void setIpsnIdNo(String ipsnIdNo) {
		this.ipsnIdNo = ipsnIdNo;
	}

	public QueryEarnestAccInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}






}
