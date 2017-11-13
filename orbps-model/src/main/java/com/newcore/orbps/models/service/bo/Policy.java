package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

public class Policy implements Serializable{
    
	private static final long serialVersionUID = 2480800739758914311L;
	
	/** 管理机构代码 */
	private String branchNo;  
	/** 销售渠道 */
	private String polCode;
	/** 销售机构号 */
    private String chiefPolCode;	           
    /** 业务场景 */
    private String polNameEng;                
    /** 契约形式 */
    private String polNameChn;
    /** 产品类型 */
    private Date polEffDate;
    /** 是否自动审批 */
    private Date polExpDate;
    /** 人工自动审批 */
    private String businessType;
    /** 1、生效日往前追溯天数 */
    private String insurDurType;
    /** 2、生效日往后指定天数 */
    private String yieldType;
    /** 3、生效日往前追溯跨越日期 */
    private String  premiumType;
    /** 规则类型 */
    private String botAgeLmt;
    /** 规则变化原因 */
    private String topAgeLmt;
    /** 规则状态 */
    private String sexSaleTo;
    /** 规则名称 */
    private String moneyinItrvl;
    /** 启用日期 */
    private String moneyinType;
    /** 停止日期 */
    private String mrType;
    
    private String uwRiskFact;
    private String siRiskFact;
    /** 审核状态 */
    private int premAlgo;
    /** 审核意见 */
    private int ageAlgo;
    
    /** 当前操作员机构 */
    private int cashAlgo;   
    /** 当前操作员工号 */
	private String subpolOptionFlag;   
	/** 当前操作员姓名 */
	private String  coverOptionFlag;  
	/** 当前操作时间 */
	private String  faceAmntType;        
	/** 操作类型 */
	private int  orderSeq;
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getChiefPolCode() {
		return chiefPolCode;
	}
	public void setChiefPolCode(String chiefPolCode) {
		this.chiefPolCode = chiefPolCode;
	}
	public String getPolNameEng() {
		return polNameEng;
	}
	public void setPolNameEng(String polNameEng) {
		this.polNameEng = polNameEng;
	}
	public String getPolNameChn() {
		return polNameChn;
	}
	public void setPolNameChn(String polNameChn) {
		this.polNameChn = polNameChn;
	}
	public Date getPolEffDate() {
		return polEffDate;
	}
	public void setPolEffDate(Date polEffDate) {
		this.polEffDate = polEffDate;
	}
	public Date getPolExpDate() {
		return polExpDate;
	}
	public void setPolExpDate(Date polExpDate) {
		this.polExpDate = polExpDate;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getInsurDurType() {
		return insurDurType;
	}
	public void setInsurDurType(String insurDurType) {
		this.insurDurType = insurDurType;
	}
	public String getYieldType() {
		return yieldType;
	}
	public void setYieldType(String yieldType) {
		this.yieldType = yieldType;
	}
	public String getPremiumType() {
		return premiumType;
	}
	public void setPremiumType(String premiumType) {
		this.premiumType = premiumType;
	}
	public String getBotAgeLmt() {
		return botAgeLmt;
	}
	public void setBotAgeLmt(String botAgeLmt) {
		this.botAgeLmt = botAgeLmt;
	}
	public String getTopAgeLmt() {
		return topAgeLmt;
	}
	public void setTopAgeLmt(String topAgeLmt) {
		this.topAgeLmt = topAgeLmt;
	}
	public String getSexSaleTo() {
		return sexSaleTo;
	}
	public void setSexSaleTo(String sexSaleTo) {
		this.sexSaleTo = sexSaleTo;
	}
	public String getMoneyinItrvl() {
		return moneyinItrvl;
	}
	public void setMoneyinItrvl(String moneyinItrvl) {
		this.moneyinItrvl = moneyinItrvl;
	}
	public String getMoneyinType() {
		return moneyinType;
	}
	public void setMoneyinType(String moneyinType) {
		this.moneyinType = moneyinType;
	}
	public String getMrType() {
		return mrType;
	}
	public void setMrType(String mrType) {
		this.mrType = mrType;
	}
	public String getUwRiskFact() {
		return uwRiskFact;
	}
	public void setUwRiskFact(String uwRiskFact) {
		this.uwRiskFact = uwRiskFact;
	}
	public String getSiRiskFact() {
		return siRiskFact;
	}
	public void setSiRiskFact(String siRiskFact) {
		this.siRiskFact = siRiskFact;
	}
	public int getPremAlgo() {
		return premAlgo;
	}
	public void setPremAlgo(int premAlgo) {
		this.premAlgo = premAlgo;
	}
	public int getAgeAlgo() {
		return ageAlgo;
	}
	public void setAgeAlgo(int ageAlgo) {
		this.ageAlgo = ageAlgo;
	}
	public int getCashAlgo() {
		return cashAlgo;
	}
	public void setCashAlgo(int cashAlgo) {
		this.cashAlgo = cashAlgo;
	}
	public String getSubpolOptionFlag() {
		return subpolOptionFlag;
	}
	public void setSubpolOptionFlag(String subpolOptionFlag) {
		this.subpolOptionFlag = subpolOptionFlag;
	}
	public String getCoverOptionFlag() {
		return coverOptionFlag;
	}
	public void setCoverOptionFlag(String coverOptionFlag) {
		this.coverOptionFlag = coverOptionFlag;
	}
	public String getFaceAmntType() {
		return faceAmntType;
	}
	public void setFaceAmntType(String faceAmntType) {
		this.faceAmntType = faceAmntType;
	}
	public int getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}
	@Override
	public String toString() {
		return "Policy [branchNo=" + branchNo + ", polCode=" + polCode + ", chiefPolCode=" + chiefPolCode
				+ ", polNameEng=" + polNameEng + ", polNameChn=" + polNameChn + ", polEffDate=" + polEffDate
				+ ", polExpDate=" + polExpDate + ", businessType=" + businessType + ", insurDurType=" + insurDurType
				+ ", yieldType=" + yieldType + ", premiumType=" + premiumType + ", botAgeLmt=" + botAgeLmt
				+ ", topAgeLmt=" + topAgeLmt + ", sexSaleTo=" + sexSaleTo + ", moneyinItrvl=" + moneyinItrvl
				+ ", moneyinType=" + moneyinType + ", mrType=" + mrType + ", uwRiskFact=" + uwRiskFact + ", siRiskFact="
				+ siRiskFact + ", premAlgo=" + premAlgo + ", ageAlgo=" + ageAlgo + ", cashAlgo=" + cashAlgo
				+ ", subpolOptionFlag=" + subpolOptionFlag + ", coverOptionFlag=" + coverOptionFlag + ", faceAmntType="
				+ faceAmntType + ", orderSeq=" + orderSeq + "]";
	}      
    
   
}
