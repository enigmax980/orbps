package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
/***
 * 共保协议查询返回Vo
 * @author 王鸿林
 *
 * @date 2017年2月20日 下午4:06:11
 * @TODO
 */
public class CommonAgreementVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4164617855889120815L;
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
	private String signDate;
	/*
	 * 协议申请日期
	 */
	private String applDate;
	/*
	 * 协议生效日期
	 */
	private String inForceDate;
	/*
	 * 协议终止日期
	 */
	private String termDate;
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
	/*
	 * 本公司公司名字
	 */
	private String companyName;
	/*
	 * 本公司共保身份
	 */
	private String coinsurType;
	/*
	 *本公司共保份额 
	 */
	private Double coinsurResponsePct;
	/*字段名称:共保协议公司基本信息组 ,长度:,是否必填:否*/
	private List<ComCompany> comCompanies;
	/*字段名称:共保协议客户基本信息 使用团体客户信息,长度:,是否必填:否*/
	private GrpHolderInfo comCustomer;
//	/*字段名称:共保险种信息组,长度:,是否必填:否*/
//	private List<Policy> policies;
//	/*字段名称参与共保信息组:,长度:,是否必填:否*/
//	private List<PeriodComRec> periodComRecs;
	/**
	 * @return the agreementNo
	 */
	public String getAgreementNo() {
		return agreementNo;
	}
	/**
	 * @param agreementNo the agreementNo to set
	 */
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	/**
	 * @return the agreementName
	 */
	public String getAgreementName() {
		return agreementName;
	}
	/**
	 * @param agreementName the agreementName to set
	 */
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	/**
	 * @return the signDate
	 */
	public String getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the applDate
	 */
	public String getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(String applDate) {
		this.applDate = applDate;
	}
	/**
	 * @return the inForceDate
	 */
	public String getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(String inForceDate) {
		this.inForceDate = inForceDate;
	}
	/**
	 * @return the termDate
	 */
	public String getTermDate() {
		return termDate;
	}
	/**
	 * @param termDate the termDate to set
	 */
	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}
	/**
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	/**
	 * @param mgrBranchNo the mgrBranchNo to set
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
	 * @return the clerkNo
	 */
	public String getClerkNo() {
		return clerkNo;
	}
	/**
	 * @param clerkNo the clerkNo to set
	 */
	public void setClerkNo(String clerkNo) {
		this.clerkNo = clerkNo;
	}
	/**
	 * @return the vclkBranchNo
	 */
	public String getVclkBranchNo() {
		return vclkBranchNo;
	}
	/**
	 * @param vclkBranchNo the vclkBranchNo to set
	 */
	public void setVclkBranchNo(String vclkBranchNo) {
		this.vclkBranchNo = vclkBranchNo;
	}
	/**
	 * @return the vclkNo
	 */
	public String getVclkNo() {
		return vclkNo;
	}
	/**
	 * @param vclkNo the vclkNo to set
	 */
	public void setVclkNo(String vclkNo) {
		this.vclkNo = vclkNo;
	}
	/**
	 * @return the convention
	 */
	public String getConvention() {
		return convention;
	}
	/**
	 * @param convention the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}
	/**
	 * @return the agreementStat
	 */
	public String getAgreementStat() {
		return agreementStat;
	}
	/**
	 * @param agreementStat the agreementStat to set
	 */
	public void setAgreementStat(String agreementStat) {
		this.agreementStat = agreementStat;
	}
	/**
	 * @return the isScan
	 */
	public String getIsScan() {
		return isScan;
	}
	/**
	 * @param isScan the isScan to set
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
	 * @return the transFlag
	 */
	public String getTransFlag() {
		return transFlag;
	}
	/**
	 * @param transFlag the transFlag to set
	 */
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the coinsurType
	 */
	public String getCoinsurType() {
		return coinsurType;
	}
	/**
	 * @param coinsurType the coinsurType to set
	 */
	public void setCoinsurType(String coinsurType) {
		this.coinsurType = coinsurType;
	}
	/**
	 * @return the coinsurResponsePct
	 */
	public Double getCoinsurResponsePct() {
		return coinsurResponsePct;
	}
	/**
	 * @param coinsurResponsePct the coinsurResponsePct to set
	 */
	public void setCoinsurResponsePct(Double coinsurResponsePct) {
		this.coinsurResponsePct = coinsurResponsePct;
	}
	/**
	 * @return the comCompanies
	 */
	public List<ComCompany> getComCompanies() {
		return comCompanies;
	}
	/**
	 * @param comCompanies the comCompanies to set
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommonAgreementVo [agreementNo=" + agreementNo + ", agreementName=" + agreementName + ", signDate="
				+ signDate + ", applDate=" + applDate + ", inForceDate=" + inForceDate + ", termDate=" + termDate
				+ ", mgrBranchNo=" + mgrBranchNo + ", pclkBranchNo=" + pclkBranchNo + ", clerkNo=" + clerkNo
				+ ", vclkBranchNo=" + vclkBranchNo + ", vclkNo=" + vclkNo + ", convention=" + convention
				+ ", agreementStat=" + agreementStat + ", isScan=" + isScan + ", remark=" + remark + ", transFlag="
				+ transFlag + ", companyName=" + companyName + ", coinsurType=" + coinsurType + ", coinsurResponsePct="
				+ coinsurResponsePct + ", comCompanies=" + comCompanies + ", comCustomer=" + comCustomer + "]";
	}
	
}
