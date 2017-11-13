package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import org.hibernate.validator.constraints.Length;

public class CommonAgreement implements Serializable {

	
	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = 5312009911278256631L;

	private String applNo;	
	/*
	 * 协议号
	 */
	@NotNull(message="[协议号不能为空]")
	@Length(max=20,message="[协议号长度不能大于20位]")
	private String agreementNo;
	
	/*
	 * 协议名称
	 */
	@NotNull(message="[协议名称不能为空]")
	@Length(max=255,message="[协议名称长度不能大于255位]")
	private String agreementName;
	private Date applDate;
	private Date inForceDate;
	private Date termDate;
	/*
	 * 管理机构
	 */
	@NotNull(message="[管理机构不能为空]")
	@Length(max=6,message="[管理机构长度不能大于6位]")
	private String mgrBranchNo;
	/**
	 * oclkBranchNo改为pclkBranchNo update by zy 2016-11-29
	 * 
	 */
	private String pclkBranchNo;
	private String clerkNo;
	private String vclkBranchNo;
	private String vclkNo;
	private String convention;
	private String agreementStat;
	private String remark;
	private String chkOpinon;
	/*
	 * 是否扫描
	 */
	@NotNull(message="[是否扫描不能为空]")
	@Length(max=1,message="[是否扫描长度不能大于1位]")
	private String isScan;
	private PeriodComRec periodComRec;
	private List<ComCompany> comCompanyList;
	private ComGrpHolderInfo grpHolderInfo;
	private List<ComPolicy> comPolList;
	private List<PeriodComInsur> periodComInsurList; 
	private String dataSource;
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	public String getAgreementName() {
		return agreementName;
	}
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	public Date getApplDate() {
		return applDate;
	}
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}
	public Date getInForceDate() {
		return inForceDate;
	}
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}
	public Date getTermDate() {
		return termDate;
	}
	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	public String getClerkNo() {
		return clerkNo;
	}
	public void setClerkNo(String clerkNo) {
		this.clerkNo = clerkNo;
	}
	public String getVclkBranchNo() {
		return vclkBranchNo;
	}
	public void setVclkBranchNo(String vclkBranchNo) {
		this.vclkBranchNo = vclkBranchNo;
	}
	public String getVclkNo() {
		return vclkNo;
	}
	public void setVclkNo(String vclkNo) {
		this.vclkNo = vclkNo;
	}
	public String getConvention() {
		return convention;
	}
	public void setConvention(String convention) {
		this.convention = convention;
	}
	public String getAgreementStat() {
		return agreementStat;
	}
	public void setAgreementStat(String agreementStat) {
		this.agreementStat = agreementStat;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChkOpinon() {
		return chkOpinon;
	}
	public void setChkOpinon(String chkOpinon) {
		this.chkOpinon = chkOpinon;
	}
	public String getIsScan() {
		return isScan;
	}
	public void setIsScan(String isScan) {
		this.isScan = isScan;
	}
	public PeriodComRec getPeriodComRec() {
		return periodComRec;
	}
	public void setPeriodComRec(PeriodComRec periodComRec) {
		this.periodComRec = periodComRec;
	}
	public List<ComCompany> getComCompanyList() {
		return comCompanyList;
	}
	public void setComCompanyList(List<ComCompany> comCompanyList) {
		this.comCompanyList = comCompanyList;
	}
	public ComGrpHolderInfo getGrpHolderInfo() {
		return grpHolderInfo;
	}
	public void setGrpHolderInfo(ComGrpHolderInfo grpHolderInfo) {
		this.grpHolderInfo = grpHolderInfo;
	}
	public List<ComPolicy> getComPolList() {
		return comPolList;
	}
	public void setComPolList(List<ComPolicy> comPolList) {
		this.comPolList = comPolList;
	}
	public List<PeriodComInsur> getPeriodComInsurList() {
		return periodComInsurList;
	}
	public void setPeriodComInsurList(List<PeriodComInsur> periodComInsurList) {
		this.periodComInsurList = periodComInsurList;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public void transCommonAgreementBoToVo(com.newcore.orbps.models.service.bo.CommonAgreement commonAgreement){
		//this.setApplNo(grpInsurAppl.getApplNo());
		this.setAgreementName(commonAgreement.getAgreementName());
		this.setAgreementNo(commonAgreement.getAgreementNo());
		this.setApplDate(commonAgreement.getApplDate());
		this.setClerkNo(commonAgreement.getClerkNo());
		this.setAgreementStat(commonAgreement.getAgreementStat());
		List<com.newcore.orbps.models.pcms.bo.ComCompany> comCompanyList = new ArrayList<>();
		for (com.newcore.orbps.models.service.bo.ComCompany comCompany2 : commonAgreement.getComCompanies()) {
			com.newcore.orbps.models.pcms.bo.ComCompany comCompanyPcms = new com.newcore.orbps.models.pcms.bo.ComCompany();
			comCompanyPcms.setBankAccName(comCompany2.getBankAccName());
			comCompanyPcms.setBankAccNo(comCompany2.getBankAccNo());
			comCompanyPcms.setBankCode(comCompany2.getBankCode());
			comCompanyPcms.setCoinsurAmntPct(comCompany2.getCoinsurAmntPct());
			comCompanyPcms.setCoinsurResponsePct(comCompany2.getCoinsurResponsePct());
			comCompanyPcms.setCoinsurType(comCompany2.getCoinsurType());
			comCompanyPcms.setCompanyCode(comCompany2.getCompanyCode());
			comCompanyPcms.setCompanyFlag(comCompany2.getCompanyFlag());
			comCompanyPcms.setCompanyName(comCompany2.getCompanyName());
			comCompanyList.add(comCompanyPcms);
		}
		this.setComCompanyList(comCompanyList);

		this.setConvention(commonAgreement.getConvention());
		this.setDataSource(null);

		com.newcore.orbps.models.pcms.bo.ComGrpHolderInfo comGrpHolderInfo = new com.newcore.orbps.models.pcms.bo.ComGrpHolderInfo();
		com.newcore.orbps.models.pcms.bo.Address address = new com.newcore.orbps.models.pcms.bo.Address();
		GrpHolderInfo grpHolderInfo  = commonAgreement.getComCustomer();
		address.setCity(grpHolderInfo.getAddress().getCity());
		address.setCounty(grpHolderInfo.getAddress().getCounty());
		address.setHomeAddress(grpHolderInfo.getAddress().getHomeAddress());
		address.setPostCode(grpHolderInfo.getAddress().getPostCode());
		address.setProvince(grpHolderInfo.getAddress().getProvince());
		address.setTown(grpHolderInfo.getAddress().getTown());
		address.setVillage(grpHolderInfo.getAddress().getVillage());

		comGrpHolderInfo.setAddress(address);
		comGrpHolderInfo.setContactEmail(grpHolderInfo.getContactEmail());
		comGrpHolderInfo.setContactMobile(grpHolderInfo.getContactMobile());
		comGrpHolderInfo.setContactName(grpHolderInfo.getContactName());
		comGrpHolderInfo.setContactTelephone(grpHolderInfo.getContactTelephone());
		comGrpHolderInfo.setCorpRep(grpHolderInfo.getCorpRep());
		comGrpHolderInfo.setFax(grpHolderInfo.getFax());
		comGrpHolderInfo.setGrpCustNo(grpHolderInfo.getGrpCustNo());
		comGrpHolderInfo.setGrpIdNo(grpHolderInfo.getGrpIdNo());
		comGrpHolderInfo.setGrpIdType(grpHolderInfo.getGrpIdType());

		comGrpHolderInfo.setGrpName(grpHolderInfo.getGrpName());
		comGrpHolderInfo.setNatureCode(grpHolderInfo.getNatureCode());
		comGrpHolderInfo.setOccClassCode(grpHolderInfo.getOccClassCode());

		this.setGrpHolderInfo(comGrpHolderInfo);

		this.setInForceDate(commonAgreement.getInForceDate());
		this.setIsScan(commonAgreement.getIsScan());
		this.setMgrBranchNo(commonAgreement.getMgrBranchNo());
		this.setPclkBranchNo(commonAgreement.getPclkBranchNo());


		List<ComPolicy> comPolList = new ArrayList<>();
		List<Policy> ListPolicy = commonAgreement.getPolicies();
		if(ListPolicy.size()>0){
			for (Policy policy : ListPolicy) {
				ComPolicy comPolicy =new ComPolicy();
				comPolicy.setFaceAmnt(policy.getFaceAmnt());
				comPolicy.setMrCode(policy.getMrCode());
				comPolicy.setPolCode(policy.getPolCode());
				comPolicy.setPremium(policy.getPremium());
				comPolList.add(comPolicy);
			}
		}
		this.setComPolList(comPolList);
		this.setTermDate(commonAgreement.getTermDate());
		this.setVclkBranchNo(commonAgreement.getVclkBranchNo());
		this.setVclkNo(commonAgreement.getVclkNo());

	}
}
