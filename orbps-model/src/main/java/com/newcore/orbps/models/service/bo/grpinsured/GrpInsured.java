package com.newcore.orbps.models.service.bo.grpinsured;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 被保人信息
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午11:30:38
 */
public class GrpInsured implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1855001849295227805L;

	/* 字段名：投保单号，长度：16，是否必填：是 */
	private String applNo;
	
	//组合保单号，是否必填：是 
	private String cgNo;
	
	//批次号，是否必填：是 
	private String batNo;
	
	//状态
	private String procStat;
	
	//被保人cmds客户号,长度：25，是否必填：否
	private String ipsnPartyId;
	
	/* 字段名：被保人客户号 ，长度：25，是否必填：否 */
	private String ipsnCustNo;
	
	/* 字段名：处理标记 ，长度：2，是否必填：否    预处理平台自己使用。I-导入完成，E-导入存在问题，C-开户完成 */
	private String procFlag;

	/* 字段名：主被保险人编号，是否必填：否 */
	private Long masterIpsnNo;

	/* 字段名：与主被保人关系，长度：2，是否必填：是*/
	private String ipsnRtMstIpsn;

	/* 字段名：被保险人编号，是否必填：是 */
	private Long ipsnNo;

	/* 字段名：被保险人姓名，长度：2-32，是否必填：是 */
	private String ipsnName;

	/* 字段名：与投保人关系，长度：2，是否必填：是 */
	private String relToHldr;

	/* 字段名：被保险人类型，长度：2，是否必填：是 */
	private String ipsnType;

	/* 字段名：被保险人性别，长度：2，是否必填：是 */
	private String ipsnSex;

	/* 字段名：被保人出生日期，是否必填：是 */
	private Date ipsnBirthDate;

	/* 字段名：被保人年龄，是否必填：否 */
	private Long ipsnAge;

	/* 字段名：被保人证件类别，长度：1，是否必填：是 */
	private String ipsnIdType;

	/* 字段名：被保人证件号码，长度：8-18，是否必填：是 */
	private String ipsnIdNo;

	/* 字段名：职业代码，长度：6，是否必填：是 */
	private String ipsnOccCode;

	/* 字段名：职业风险等级，长度：2，是否必填：否 */
	private String ipsnOccClassLevel;

	/* 字段名：组织层次代码，长度：6，是否必填：否 */
	private String levelCode;

	/* 字段名：被保人工作地点，长度：4-200，是否必填：否 */
	private String ipsnCompanyLoc;

	/* 字段名：被保人工号，长度：16，是否必填：否 */
	private String ipsnRefNo;

	/* 字段名：在职标记，长度：2，是否必填：否 */
	private String inServiceFlag;

	/* 字段名：医保标记，长度：2，是否必填：否 */
	private String ipsnSss;

	/* 字段名：医保代码，长度：2，是否必填：否 */
	private String ipsnSsc;

	/* 字段名：医保编号，长度：18，是否必填：否 */
	private String ipsnSsn;

	/* 字段名：是否异常告知，长度：2，是否必填：是 */
	private String notificaStat;

	/* 字段名：被保人手机号码，长度：32，是否必填：否 */
	private String ipsnMobilePhone;

	/* 字段名：被保人电子邮箱地址，长度：64，是否必填：否 */
	private String ipsnEmail;

	/* 字段名：收费属组号，是否必填：否 */
	private Long feeGrpNo;

	/* 字段名：同业公司人身保险保额合计，是否必填：否 */
	private Double tyNetPayAmnt;

	/* 字段名：个人缴费金额，是否必填：否 */
	private Double ipsnPayAmount;

	/* 字段名：单位交费金额，是否必填：否 */
	private Double grpPayAmount;

	/* 字段名：备注，是否必填：契约平台自己使用，记录处理过程中对应状态的问题 */
	private String remark;
	
	// 投保人信息
	private HldrInfo hldrInfo;

	// 缴费账号
	private List<AccInfo> accInfoList;

	// 子要约
	private List<SubState> subStateList;

	// 要约列表
	private List<IpsnState> ipsnStateList;
	
	// 受益人信息
	private List<BnfrInfo> bnfrInfoList;

	/* 字段名：账户余额 */
	private Double accBalance;

	/* 字段名：账户管理费金额*/
	private Double accAdminBalance;
	
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
	 * @return the cgNo
	 */
	public String getCgNo() {
		return cgNo;
	}

	/**
	 * @param cgNo the cgNo to set
	 */
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	/**
	 * @return the batNo
	 */
	public String getBatNo() {
		return batNo;
	}

	/**
	 * @param batNo the batNo to set
	 */
	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}

	/**
	 * @return the procStat
	 */
	public String getProcStat() {
		return procStat;
	}

	/**
	 * @param procStat the procStat to set
	 */
	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}

	/**
	 * @return the ipsnPartyId
	 */
	public String getIpsnPartyId() {
		return ipsnPartyId;
	}

	/**
	 * @param ipsnPartyId the ipsnPartyId to set
	 */
	public void setIpsnPartyId(String ipsnPartyId) {
		this.ipsnPartyId = ipsnPartyId;
	}

	/**
	 * @return the ipsnCustNo
	 */
	public String getIpsnCustNo() {
		return ipsnCustNo;
	}

	/**
	 * @param ipsnCustNo the ipsnCustNo to set
	 */
	public void setIpsnCustNo(String ipsnCustNo) {
		this.ipsnCustNo = ipsnCustNo;
	}

	/**
	 * @return the procFlag
	 */
	public String getProcFlag() {
		return procFlag;
	}

	/**
	 * @param procFlag the procFlag to set
	 */
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}

	/**
	 * @return the masterIpsnNo
	 */
	public Long getMasterIpsnNo() {
		return masterIpsnNo;
	}

	/**
	 * @param masterIpsnNo the masterIpsnNo to set
	 */
	public void setMasterIpsnNo(Long masterIpsnNo) {
		this.masterIpsnNo = masterIpsnNo;
	}

	/**
	 * @return the ipsnRtMstIpsn
	 */
	public String getIpsnRtMstIpsn() {
		return ipsnRtMstIpsn;
	}

	/**
	 * @param ipsnRtMstIpsn the ipsnRtMstIpsn to set
	 */
	public void setIpsnRtMstIpsn(String ipsnRtMstIpsn) {
		this.ipsnRtMstIpsn = ipsnRtMstIpsn;
	}

	/**
	 * @return the ipsnNo
	 */
	public Long getIpsnNo() {
		return ipsnNo;
	}

	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(Long ipsnNo) {
		this.ipsnNo = ipsnNo;
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
	 * @return the relToHldr
	 */
	public String getRelToHldr() {
		return relToHldr;
	}

	/**
	 * @param relToHldr the relToHldr to set
	 */
	public void setRelToHldr(String relToHldr) {
		this.relToHldr = relToHldr;
	}

	/**
	 * @return the ipsnType
	 */
	public String getIpsnType() {
		return ipsnType;
	}

	/**
	 * @param ipsnType the ipsnType to set
	 */
	public void setIpsnType(String ipsnType) {
		this.ipsnType = ipsnType;
	}

	/**
	 * @return the ipsnSex
	 */
	public String getIpsnSex() {
		return ipsnSex;
	}

	/**
	 * @param ipsnSex the ipsnSex to set
	 */
	public void setIpsnSex(String ipsnSex) {
		this.ipsnSex = ipsnSex;
	}

	/**
	 * @return the ipsnBirthDate
	 */
	public Date getIpsnBirthDate() {
		return ipsnBirthDate;
	}

	/**
	 * @param ipsnBirthDate the ipsnBirthDate to set
	 */
	public void setIpsnBirthDate(Date ipsnBirthDate) {
		this.ipsnBirthDate = ipsnBirthDate;
	}

	public Long getIpsnAge() {
		return ipsnAge;
	}

	public void setIpsnAge(Long ipsnAge) {
		this.ipsnAge = ipsnAge;
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
	 * @return the ipsnOccCode
	 */
	public String getIpsnOccCode() {
		return ipsnOccCode;
	}

	/**
	 * @param ipsnOccCode the ipsnOccCode to set
	 */
	public void setIpsnOccCode(String ipsnOccCode) {
		this.ipsnOccCode = ipsnOccCode;
	}

	/**
	 * @return the ipsnOccClassLevel
	 */
	public String getIpsnOccClassLevel() {
		return ipsnOccClassLevel;
	}

	/**
	 * @param ipsnOccClassLevel the ipsnOccClassLevel to set
	 */
	public void setIpsnOccClassLevel(String ipsnOccClassLevel) {
		this.ipsnOccClassLevel = ipsnOccClassLevel;
	}

	/**
	 * @return the levelCode
	 */
	public String getLevelCode() {
		return levelCode;
	}

	/**
	 * @param levelCode the levelCode to set
	 */
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	/**
	 * @return the ipsnCompanyLoc
	 */
	public String getIpsnCompanyLoc() {
		return ipsnCompanyLoc;
	}

	/**
	 * @param ipsnCompanyLoc the ipsnCompanyLoc to set
	 */
	public void setIpsnCompanyLoc(String ipsnCompanyLoc) {
		this.ipsnCompanyLoc = ipsnCompanyLoc;
	}

	/**
	 * @return the ipsnRefNo
	 */
	public String getIpsnRefNo() {
		return ipsnRefNo;
	}

	/**
	 * @param ipsnRefNo the ipsnRefNo to set
	 */
	public void setIpsnRefNo(String ipsnRefNo) {
		this.ipsnRefNo = ipsnRefNo;
	}

	/**
	 * @return the inServiceFlag
	 */
	public String getInServiceFlag() {
		return inServiceFlag;
	}

	/**
	 * @param inServiceFlag the inServiceFlag to set
	 */
	public void setInServiceFlag(String inServiceFlag) {
		this.inServiceFlag = inServiceFlag;
	}

	/**
	 * @return the ipsnSss
	 */
	public String getIpsnSss() {
		return ipsnSss;
	}

	/**
	 * @param ipsnSss the ipsnSss to set
	 */
	public void setIpsnSss(String ipsnSss) {
		this.ipsnSss = ipsnSss;
	}

	/**
	 * @return the ipsnSsc
	 */
	public String getIpsnSsc() {
		return ipsnSsc;
	}

	/**
	 * @param ipsnSsc the ipsnSsc to set
	 */
	public void setIpsnSsc(String ipsnSsc) {
		this.ipsnSsc = ipsnSsc;
	}

	/**
	 * @return the ipsnSsn
	 */
	public String getIpsnSsn() {
		return ipsnSsn;
	}

	/**
	 * @param ipsnSsn the ipsnSsn to set
	 */
	public void setIpsnSsn(String ipsnSsn) {
		this.ipsnSsn = ipsnSsn;
	}

	/**
	 * @return the notificaStat
	 */
	public String getNotificaStat() {
		return notificaStat;
	}

	/**
	 * @param notificaStat the notificaStat to set
	 */
	public void setNotificaStat(String notificaStat) {
		this.notificaStat = notificaStat;
	}

	/**
	 * @return the ipsnMoblePhone
	 */
	public String getIpsnMobilePhone() {
		return ipsnMobilePhone;
	}

	/**
	 * @param ipsnMoblePhone the ipsnMoblePhone to set
	 */
	public void setIpsnMobilePhone(String ipsnMobilePhone) {
		this.ipsnMobilePhone = ipsnMobilePhone;
	}

	/**
	 * @return the ipsnEmail
	 */
	public String getIpsnEmail() {
		return ipsnEmail;
	}

	/**
	 * @param ipsnEmail the ipsnEmail to set
	 */
	public void setIpsnEmail(String ipsnEmail) {
		this.ipsnEmail = ipsnEmail;
	}

	/**
	 * @return the feeGrpNo
	 */
	public Long getFeeGrpNo() {
		return feeGrpNo;
	}

	/**
	 * @param feeGrpNo the feeGrpNo to set
	 */
	public void setFeeGrpNo(Long feeGrpNo) {
		this.feeGrpNo = feeGrpNo;
	}

	/**
	 * @return the tyNetPayAmnt
	 */
	public Double getTyNetPayAmnt() {
		return tyNetPayAmnt;
	}

	/**
	 * @param tyNetPayAmnt the tyNetPayAmnt to set
	 */
	public void setTyNetPayAmnt(Double tyNetPayAmnt) {
		this.tyNetPayAmnt = tyNetPayAmnt;
	}

	/**
	 * @return the ipsnPayAmount
	 */
	public Double getIpsnPayAmount() {
		return ipsnPayAmount;
	}

	/**
	 * @param ipsnPayAmount the ipsnPayAmount to set
	 */
	public void setIpsnPayAmount(Double ipsnPayAmount) {
		this.ipsnPayAmount = ipsnPayAmount;
	}

	/**
	 * @return the grpPayAmount
	 */
	public Double getGrpPayAmount() {
		return grpPayAmount;
	}

	/**
	 * @param grpPayAmount the grpPayAmount to set
	 */
	public void setGrpPayAmount(Double grpPayAmount) {
		this.grpPayAmount = grpPayAmount;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the hldrInfo
	 */
	public HldrInfo getHldrInfo() {
		return hldrInfo;
	}

	/**
	 * @param hldrInfo the hldrInfo to set
	 */
	public void setHldrInfo(HldrInfo hldrInfo) {
		this.hldrInfo = hldrInfo;
	}

	/**
	 * @return the accInfoList
	 */
	public List<AccInfo> getAccInfoList() {
		return accInfoList;
	}

	/**
	 * @param accInfoList the accInfoList to set
	 */
	public void setAccInfoList(List<AccInfo> accInfoList) {
		this.accInfoList = accInfoList;
	}

	/**
	 * @return the subStateList
	 */
	public List<SubState> getSubStateList() {
		return subStateList;
	}

	/**
	 * @param subStateList the subStateList to set
	 */
	public void setSubStateList(List<SubState> subStateList) {
		this.subStateList = subStateList;
	}

	/**
	 * @return the ipsnStateList
	 */
	public List<IpsnState> getIpsnStateList() {
		return ipsnStateList;
	}

	/**
	 * @param ipsnStateList the ipsnStateList to set
	 */
	public void setIpsnStateList(List<IpsnState> ipsnStateList) {
		this.ipsnStateList = ipsnStateList;
	}

	/**
	 * @return the bnfrInfoList
	 */
	public List<BnfrInfo> getBnfrInfoList() {
		return bnfrInfoList;
	}

	/**
	 * @param bnfrInfoList the bnfrInfoList to set
	 */
	public void setBnfrInfoList(List<BnfrInfo> bnfrInfoList) {
		this.bnfrInfoList = bnfrInfoList;
	}

	/**
	 * @return the accBalance
	 */
	public Double getAccBalance() {
		return accBalance;
	}

	/**
	 * @param accBalance the accBalance to set
	 */
	public void setAccBalance(Double accBalance) {
		this.accBalance = accBalance;
	}

	/**
	 * @return the accAdminBalance
	 */
	public Double getAccAdminBalance() {
		return accAdminBalance;
	}

	/**
	 * @param accAdminBalance the accAdminBalance to set
	 */
	public void setAccAdminBalance(Double accAdminBalance) {
		this.accAdminBalance = accAdminBalance;
	}

}
