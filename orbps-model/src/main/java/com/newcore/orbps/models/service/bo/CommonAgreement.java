package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;

/**
 * @author huanglong
 * @date 2016年10月24日
 * 内容:共保基本信息
 */
public class CommonAgreement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4164617855889120315L;
	/*
	 * 协议号
	 */
	private String agreementNo;
	/*
	 * 协议名称
	 */
	private String agreementName;
	/*
	 * 协议签署日期
	 */
	private Date signDate;
	/*
	 * 协议申请日期
	 */
	private Date applDate;
	/*
	 * 协议生效日期
	 */
	private Date inForceDate;
	/*
	 * 协议终止日期
	 */
	private Date termDate;
	/*字段名称:协议创建日期 记录审核时间,长度:,是否必填:否*/
	private Date createDate;
	/*
	 * 管理机构
	 */
	private String mgrBranchNo;
	/*
	 * 操作员机构
	 */
	private String pclkBranchNo;
	/*
	 * 操作员工号
	 */
	private String clerkNo;
	/*
	 * 审核员机构
	 */
	private String vclkBranchNo;
	/*
	 * 审核员工号
	 */
	private String vclkNo;
	/*
	 * 协议约定
	 */
	private String convention;
	/*
	 * 协议状态
	 */
	private String agreementStat;
	/*
	 * 是否扫描 默认N
	 */
	private String isScan;
	
	/*字段名称:审核备注,长度:,是否必填:否*/
	private String remark;
	/*
	 * 交易标志A--共保新增,M-共保修改，I--参与共保
	 */
	private String transFlag;
	/*字段名称:共保协议公司基本信息组 ,长度:,是否必填:否*/
	private List<ComCompany> comCompanies;
	/*字段名称:共保协议客户基本信息 使用团体客户信息,长度:,是否必填:否*/
	private GrpHolderInfo comCustomer;
	/*字段名称:共保险种信息组,长度:,是否必填:否*/
	private List<Policy> policies;
	/*字段名称参与共保信息组:,长度:,是否必填:否*/
	private List<PeriodComRec> periodComRecs;
	public CommonAgreement() {
		super();
	}
	/**
	 * @return agreementNo
	 */
	public String getAgreementNo() {
		return agreementNo;
	}
	/**
	 * @param agreementNo 要设置的 agreementNo
	 */
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	/**
	 * @return agreementName
	 */
	public String getAgreementName() {
		return agreementName;
	}
	/**
	 * @param agreementName 要设置的 agreementName
	 */
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	/**
	 * @return signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate 要设置的 signDate
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	/**
	 * @return inForceDate
	 */
	public Date getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate 要设置的 inForceDate
	 */
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}
	/**
	 * @return termDate
	 */
	public Date getTermDate() {
		return termDate;
	}
	/**
	 * @param termDate 要设置的 termDate
	 */
	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	/**
	 * @param mgrBranchNo 要设置的 mgrBranchNo
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	/**
	 * @return the pclkBranchNo
	 */
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	/**
	 * @param pclkBranchNo the pclkBranchNo to set
	 */
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	/**
	 * @return clerkNo
	 */
	public String getClerkNo() {
		return clerkNo;
	}
	/**
	 * @param clerkNo 要设置的 clerkNo
	 */
	public void setClerkNo(String clerkNo) {
		this.clerkNo = clerkNo;
	}
	/**
	 * @return vclkBranchNo
	 */
	public String getVclkBranchNo() {
		return vclkBranchNo;
	}
	/**
	 * @param vclkBranchNo 要设置的 vclkBranchNo
	 */
	public void setVclkBranchNo(String vclkBranchNo) {
		this.vclkBranchNo = vclkBranchNo;
	}

	/**
	 * @return vclkNo
	 */
	public String getVclkNo() {
		return vclkNo;
	}
	/**
	 * @param vclkNo 要设置的 vclkNo
	 */
	public void setVclkNo(String vclkNo) {
		this.vclkNo = vclkNo;
	}
	/**
	 * @return convention
	 */
	public String getConvention() {
		return convention;
	}
	/**
	 * @param convention 要设置的 convention
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}	
	/**
	 * @return the periodComRecs
	 */
	public List<PeriodComRec> getPeriodComRecs() {
		return periodComRecs;
	}
	/**
	 * @param periodComRecs the periodComRecs to set
	 */
	public void setPeriodComRecs(List<PeriodComRec> periodComRecs) {
		this.periodComRecs = periodComRecs;
	}
	/**
	 * @return comCompanies
	 */
	public List<ComCompany> getComCompanies() {
		return comCompanies;
	}
	/**
	 * @param comCompanies 要设置的 comCompanies
	 */
	public void setComCompanies(List<ComCompany> comCompanies) {
		this.comCompanies = comCompanies;
	}

	/**
	 * @return the comCustomer
	 */
	public GrpHolderInfo getComCustomer() {
		return comCustomer;
	}
	/**
	 * @param comCustomer the comCustomer to set
	 */
	public void setComCustomer(GrpHolderInfo comCustomer) {
		this.comCustomer = comCustomer;
	}
	/**
	 * @return the policies
	 */
	public List<Policy> getPolicies() {
		return policies;
	}
	/**
	 * @param policies the policies to set
	 */
	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
	}
	/**
	 * @return applDate
	 */
	public Date getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate 要设置的 applDate
	 */
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}	
	/**
	 * @return agreementStat
	 */
	public String getAgreementStat() {
		return agreementStat;
	}
	/**
	 * @param agreementStat 要设置的 agreementStat
	 */
	public void setAgreementStat(String agreementStat) {
		this.agreementStat = agreementStat;
	}

	/**
	 * @return isScan
	 */
	public String getIsScan() {
		return isScan;
	}
	/**
	 * @param isScan 要设置的 isScan
	 */
	public void setIsScan(String isScan) {
		this.isScan = isScan;
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
	 * @return transFlag
	 */
	public String getTransFlag() {
		return transFlag;
	}
	/**
	 * @param transFlag 要设置的 transFlag
	 */
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

}
