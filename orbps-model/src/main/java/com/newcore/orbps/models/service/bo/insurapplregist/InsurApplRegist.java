package com.newcore.orbps.models.service.bo.insurapplregist;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;

/**
 * @author wangxiao
 * 创建时间：2016年10月24日下午1:44:03
 */
public class InsurApplRegist implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7226778595136318551L;
	/* 字段名：单证号码，长度：16，是否必填：是 */
	private String billNo;
	/* 字段名：申请日期，长度：16，是否必填：是 */
	private Date acceptDate;
	/* 字段名：团险方案审批号，长度：22，是否必填：否 */
	private String approNo;
	/* 字段名：是否共保，长度：2，是否必填：是 */
	private String isCommonAgreement;
	/* 字段名：共保协议号，长度：20，是否必填：如果共保，非空 */
	private String agreementNo;
	/* 字段名：契约形式，长度：2，是否必填：是 */
	private String cntrType;
	/* 字段名：险种组合代码，长度：4，是否必填：if cntyType = P then 必填 */
	private String comPolCode;
	/* 字段名：险种代码，长度：8，是否必填：是 */
	private String polCode;
	/* 字段名：险种名称，长度：30，是否必填：是 */
	private String polNameChn;
	/* 字段名：被保险人数，是否必填：是 */
	private Long ipsnNum;
	/* 字段名：币种，长度：3，是否必填：是 */
	private String currencyCode;
	/* 字段名：保费合计，是否必填：是 */
	private Double sumPremium;
	/* 字段名：投保人/汇交人名称，长度：200，是否必填：是 */
	private String hldrName;
	/* 字段名：投保单数量，是否必填：是 */
	private Integer billNumber;
	/* 字段名：共享客户信息，长度：2，是否必填：是 */
	private String shareCustFlag;
	//是否提供电子清单			
	/* 字段名：外包录入标记，长度：2，是否必填：是 */
	private String entChanelFlag;
	/* 字段名：个汇投保单号列表，长度：2，是否必填：IF cntrType == M THEN  列表非空； ELSE 为空；END IF */
	private List<String> applNoList;
	/* 字段名：销售人员是否共同展业标识，长度：2，是否必填：否*/
	private String salesDevelopFlag;
	//投保资料
	private List<ApplInfo> applInfos;
	//销售信息
	private List<SalesInfo> salesInfos;
	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}
	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	/**
	 * @return the acceptDate
	 */
	public Date getAcceptDate() {
		return acceptDate;
	}
	/**
	 * @param acceptDate the acceptDate to set
	 */
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	/**
	 * @return the approNo
	 */
	public String getApproNo() {
		return approNo;
	}
	/**
	 * @param approNo the approNo to set
	 */
	public void setApproNo(String approNo) {
		this.approNo = approNo;
	}
	/**
	 * @return the isCommonAgreement
	 */
	public String getIsCommonAgreement() {
		return isCommonAgreement;
	}
	/**
	 * @param isCommonAgreement the isCommonAgreement to set
	 */
	public void setIsCommonAgreement(String isCommonAgreement) {
		this.isCommonAgreement = isCommonAgreement;
	}
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
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}
	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	/**
	 * @return the comPolCode
	 */
	public String getComPolCode() {
		return comPolCode;
	}
	/**
	 * @param comPolCode the comPolCode to set
	 */
	public void setComPolCode(String comPolCode) {
		this.comPolCode = comPolCode;
	}
	/**
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}
	/**
	 * @param polCode the polCode to set
	 */
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	/**
	 * @return the polNameChn
	 */
	public String getPolNameChn() {
		return polNameChn;
	}
	/**
	 * @param polNameChn the polNameChn to set
	 */
	public void setPolNameChn(String polNameChn) {
		this.polNameChn = polNameChn;
	}
	/**
	 * @return the ipsnNum
	 */
	public Long getIpsnNum() {
		return ipsnNum;
	}
	/**
	 * @param ipsnNum the ipsnNum to set
	 */
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the sumPremium
	 */
	public Double getSumPremium() {
		return sumPremium;
	}
	/**
	 * @param sumPremium the sumPremium to set
	 */
	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}
	/**
	 * @return the hldrName
	 */
	public String getHldrName() {
		return hldrName;
	}
	/**
	 * @param hldrName the hldrName to set
	 */
	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}
	/**
	 * @return the billNumber
	 */
	public Integer getBillNumber() {
		return billNumber;
	}
	/**
	 * @param billNumber the billNumber to set
	 */
	public void setBillNumber(Integer billNumber) {
		this.billNumber = billNumber;
	}
	/**
	 * @return the shareCustFlag
	 */
	public String getShareCustFlag() {
		return shareCustFlag;
	}
	/**
	 * @param shareCustFlag the shareCustFlag to set
	 */
	public void setShareCustFlag(String shareCustFlag) {
		this.shareCustFlag = shareCustFlag;
	}
	/**
	 * @return the entChanelFlag
	 */
	public String getEntChanelFlag() {
		return entChanelFlag;
	}
	/**
	 * @param entChanelFlag the entChanelFlag to set
	 */
	public void setEntChanelFlag(String entChanelFlag) {
		this.entChanelFlag = entChanelFlag;
	}
	/**
	 * @return the applNoList
	 */
	public List<String> getApplNoList() {
		return applNoList;
	}
	/**
	 * @param applNoList the applNoList to set
	 */
	public void setApplNoList(List<String> applNoList) {
		this.applNoList = applNoList;
	}
	/**
	 * @return the salesDevelopFlag
	 */
	public String getSalesDevelopFlag() {
		return salesDevelopFlag;
	}
	/**
	 * @param salesDevelopFlag the salesDevelopFlag to set
	 */
	public void setSalesDevelopFlag(String salesDevelopFlag) {
		this.salesDevelopFlag = salesDevelopFlag;
	}
	/**
	 * @return the applInfos
	 */
	public List<ApplInfo> getApplInfos() {
		return applInfos;
	}
	/**
	 * @param applInfos the applInfos to set
	 */
	public void setApplInfos(List<ApplInfo> applInfos) {
		this.applInfos = applInfos;
	}
	/**
	 * @return the salesInfos
	 */
	public List<SalesInfo> getSalesInfos() {
		return salesInfos;
	}
	/**
	 * @param salesInfos the salesInfos to set
	 */
	public void setSalesInfos(List<SalesInfo> salesInfos) {
		this.salesInfos = salesInfos;
	}
	
}
