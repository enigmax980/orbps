package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 特殊险种附加信息
 * @author xiaoye
 *
 */
public class GrpSpecialInsurAddInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1441465343L;
	/** 公共保额使用范围 */
	private String comInsurAmntUse;
	/** 公共保额类型 */
	private String comInsurAmntType;
	/** 公共保费 */
	private Double commPremium;
	/** 固定公共保额 */
	private Double fixedComAmnt;
	/** 人均浮动公共保额 */
	private Double ipsnFloatAmnt;
	/** 人均浮动比例*/
	private Double ipsnFloatPct;
	
	/** 管理费计提方式 */
	private String adminCalType;
	/** 管理费比例 */
	private Double adminPcent;
	/** 个人账户金额 */
	private Double ipsnAccAmnt;
	/** 计入个人账户金额 */
	private Double inclIpsnAccAmnt;
	/** 公共账户交费金额 */
	private Double sumPubAccAmnt;
	/** 计入公共缴费账户金额 */
	private Double inclSumPubAccAmnt;
	/* 字段名：基金险收到保费时间，是否必填：基金险必填，团单特有 */
	private Date preMioDate;
	/* 字段名：账户余额，是否必填：基金险必填，团单特有 */
	private Double accBalance;
	/* 字段名：账户管理费金额，是否必填：基金险必填，团单特有 */
	private Double accAdminBalance;
	/* 字段名：首期管理费总金额，是否必填：基金险必填，团单特有 */
	private Double accSumAdminBalance;
	/** 工程名称 */
	private String projectName;
	/** 工程地址 */
	private String projectAddr;
	/** 工程类型 */
	private String projectType;
	/** 保费收取方式 */
	private String cpnstMioType;
	/** 总造价 */
	private Double totalCost;
	/** 总面积 */
	private Double totalArea;
	/** 施工开始日期 */
	private Date constructionDur;
	/** 施工结束日期 */
	private Date until;
	/** 企业资质 */
	private String enterpriseLicence;
	/** 获奖情况 */
	private String awardGrade;
	/** 是否有安防措施 */
	private String safetyFlag;
	/** 疾病死亡人数 */
	private Long diseaDeathNums;
	/** 疾病伤残人数 */
	private Long diseaDisableNums;
	/** 意外死亡人数 */
	private Long acdntDeathNums;
	/** 意外伤残人数 */
	private Long acdntDisableNums;
	/** 过去二年内是否有四级以上安全事故 */
	private String saftyAcdntFlag;
	/** 层高(米) */
	private Double floorHeight;
	/** 工程位置类别 */
	private String projLocType;
	
	/**
	 * @return the comInsurAmntUse
	 */
	public String getComInsurAmntUse() {
		return comInsurAmntUse;
	}
	/**
	 * @param comInsurAmntUse the comInsurAmntUse to set
	 */
	public void setComInsurAmntUse(String comInsurAmntUse) {
		this.comInsurAmntUse = comInsurAmntUse;
	}
	/**
	 * @return the comInsurAmntType
	 */
	public String getComInsurAmntType() {
		return comInsurAmntType;
	}
	/**
	 * @param comInsurAmntType the comInsurAmntType to set
	 */
	public void setComInsurAmntType(String comInsurAmntType) {
		this.comInsurAmntType = comInsurAmntType;
	}
	/**
	 * @return the commPremium
	 */
	public Double getCommPremium() {
		return commPremium;
	}
	/**
	 * @param commPremium the commPremium to set
	 */
	public void setCommPremium(Double commPremium) {
		this.commPremium = commPremium;
	}
	/**
	 * @return the fixedComAmnt
	 */
	public Double getFixedComAmnt() {
		return fixedComAmnt;
	}
	/**
	 * @param fixedComAmnt the fixedComAmnt to set
	 */
	public void setFixedComAmnt(Double fixedComAmnt) {
		this.fixedComAmnt = fixedComAmnt;
	}
	/**
	 * @return the ipsnFloatAmnt
	 */
	public Double getIpsnFloatAmnt() {
		return ipsnFloatAmnt;
	}
	/**
	 * @param ipsnFloatAmnt the ipsnFloatAmnt to set
	 */
	public void setIpsnFloatAmnt(Double ipsnFloatAmnt) {
		this.ipsnFloatAmnt = ipsnFloatAmnt;
	}
	/**
	 * @return the ipsnFloatPct
	 */
	public Double getIpsnFloatPct() {
		return ipsnFloatPct;
	}
	/**
	 * @param ipsnFloatPct the ipsnFloatPct to set
	 */
	public void setIpsnFloatPct(Double ipsnFloatPct) {
		this.ipsnFloatPct = ipsnFloatPct;
	}
	/**
	 * @return the adminCalType
	 */
	public String getAdminCalType() {
		return adminCalType;
	}
	/**
	 * @param adminCalType the adminCalType to set
	 */
	public void setAdminCalType(String adminCalType) {
		this.adminCalType = adminCalType;
	}
	/**
	 * @return the adminPcent
	 */
	public Double getAdminPcent() {
		return adminPcent;
	}
	/**
	 * @param adminPcent the adminPcent to set
	 */
	public void setAdminPcent(Double adminPcent) {
		this.adminPcent = adminPcent;
	}
	/**
	 * @return the ipsnAccAmnt
	 */
	public Double getIpsnAccAmnt() {
		return ipsnAccAmnt;
	}
	/**
	 * @param ipsnAccAmnt the ipsnAccAmnt to set
	 */
	public void setIpsnAccAmnt(Double ipsnAccAmnt) {
		this.ipsnAccAmnt = ipsnAccAmnt;
	}
	/**
	 * @return the inclIpsnAccAmnt
	 */
	public Double getInclIpsnAccAmnt() {
		return inclIpsnAccAmnt;
	}
	/**
	 * @param inclIpsnAccAmnt the inclIpsnAccAmnt to set
	 */
	public void setInclIpsnAccAmnt(Double inclIpsnAccAmnt) {
		this.inclIpsnAccAmnt = inclIpsnAccAmnt;
	}
	/**
	 * @return the sumPubAccAmnt
	 */
	public Double getSumPubAccAmnt() {
		return sumPubAccAmnt;
	}
	/**
	 * @param sumPubAccAmnt the sumPubAccAmnt to set
	 */
	public void setSumPubAccAmnt(Double sumPubAccAmnt) {
		this.sumPubAccAmnt = sumPubAccAmnt;
	}
	/**
	 * @return the inclSumPubAccAmnt
	 */
	public Double getInclSumPubAccAmnt() {
		return inclSumPubAccAmnt;
	}
	/**
	 * @param inclSumPubAccAmnt the inclSumPubAccAmnt to set
	 */
	public void setInclSumPubAccAmnt(Double inclSumPubAccAmnt) {
		this.inclSumPubAccAmnt = inclSumPubAccAmnt;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the projectAddr
	 */
	public String getProjectAddr() {
		return projectAddr;
	}
	/**
	 * @param projectAddr the projectAddr to set
	 */
	public void setProjectAddr(String projectAddr) {
		this.projectAddr = projectAddr;
	}
	/**
	 * @return the projectType
	 */
	public String getProjectType() {
		return projectType;
	}
	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	/**
	 * @return the cpnstMioType
	 */
	public String getCpnstMioType() {
		return cpnstMioType;
	}
	/**
	 * @param cpnstMioType the cpnstMioType to set
	 */
	public void setCpnstMioType(String cpnstMioType) {
		this.cpnstMioType = cpnstMioType;
	}
	/**
	 * @return the totalCost
	 */
	public Double getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	/**
	 * @return the totalArea
	 */
	public Double getTotalArea() {
		return totalArea;
	}
	/**
	 * @param totalArea the totalArea to set
	 */
	public void setTotalArea(Double totalArea) {
		this.totalArea = totalArea;
	}
	/**
	 * @return the constructionDur
	 */
	public Date getConstructionDur() {
		return constructionDur;
	}
	/**
	 * @param constructionDur the constructionDur to set
	 */
	public void setConstructionDur(Date constructionDur) {
		this.constructionDur = constructionDur;
	}
	/**
	 * @return the until
	 */
	public Date getUntil() {
		return until;
	}
	/**
	 * @param until the until to set
	 */
	public void setUntil(Date until) {
		this.until = until;
	}
	public String getEnterpriseLicence() {
		return enterpriseLicence;
	}
	public void setEnterpriseLicence(String enterpriseLicence) {
		this.enterpriseLicence = enterpriseLicence;
	}
	public String getAwardGrade() {
		return awardGrade;
	}
	public void setAwardGrade(String awardGrade) {
		this.awardGrade = awardGrade;
	}
	public String getSafetyFlag() {
		return safetyFlag;
	}
	public void setSafetyFlag(String safetyFlag) {
		this.safetyFlag = safetyFlag;
	}
	public Long getDiseaDeathNums() {
		return diseaDeathNums;
	}
	public void setDiseaDeathNums(Long diseaDeathNums) {
		this.diseaDeathNums = diseaDeathNums;
	}
	public Long getDiseaDisableNums() {
		return diseaDisableNums;
	}
	public void setDiseaDisableNums(Long diseaDisableNums) {
		this.diseaDisableNums = diseaDisableNums;
	}
	public Long getAcdntDeathNums() {
		return acdntDeathNums;
	}
	public void setAcdntDeathNums(Long acdntDeathNums) {
		this.acdntDeathNums = acdntDeathNums;
	}
	public Long getAcdntDisableNums() {
		return acdntDisableNums;
	}
	public void setAcdntDisableNums(Long acdntDisableNums) {
		this.acdntDisableNums = acdntDisableNums;
	}
	public String getSaftyAcdntFlag() {
		return saftyAcdntFlag;
	}
	public void setSaftyAcdntFlag(String saftyAcdntFlag) {
		this.saftyAcdntFlag = saftyAcdntFlag;
	}
	public Double getFloorHeight() {
		return floorHeight;
	}
	public void setFloorHeight(Double floorHeight) {
		this.floorHeight = floorHeight;
	}
	
	public String getProjLocType() {
		return projLocType;
	}
	public void setProjLocType(String projLocType) {
		this.projLocType = projLocType;
	}
	public Date getPreMioDate() {
		return preMioDate;
	}
	public void setPreMioDate(Date preMioDate) {
		this.preMioDate = preMioDate;
	}
	public Double getAccBalance() {
		return accBalance;
	}
	public void setAccBalance(Double accBalance) {
		this.accBalance = accBalance;
	}
	public Double getAccAdminBalance() {
		return accAdminBalance;
	}
	public void setAccAdminBalance(Double accAdminBalance) {
		this.accAdminBalance = accAdminBalance;
	}
	public Double getAccSumAdminBalance() {
		return accSumAdminBalance;
	}
	public void setAccSumAdminBalance(Double accSumAdminBalance) {
		this.accSumAdminBalance = accSumAdminBalance;
	}
	@Override
	public String toString() {
		return "GrpSpecialInsurAddInfoVo [comInsurAmntUse=" + comInsurAmntUse + ", comInsurAmntType=" + comInsurAmntType
				+ ", commPremium=" + commPremium + ", fixedComAmnt=" + fixedComAmnt + ", ipsnFloatAmnt=" + ipsnFloatAmnt
				+ ", ipsnFloatPct=" + ipsnFloatPct + ", adminCalType=" + adminCalType + ", adminPcent=" + adminPcent
				+ ", ipsnAccAmnt=" + ipsnAccAmnt + ", inclIpsnAccAmnt=" + inclIpsnAccAmnt + ", sumPubAccAmnt="
				+ sumPubAccAmnt + ", inclSumPubAccAmnt=" + inclSumPubAccAmnt + ", preMioDate=" + preMioDate
				+ ", accBalance=" + accBalance + ", accAdminBalance=" + accAdminBalance + ", accSumAdminBalance="
				+ accSumAdminBalance + ", projectName=" + projectName + ", projectAddr=" + projectAddr
				+ ", projectType=" + projectType + ", cpnstMioType=" + cpnstMioType + ", totalCost=" + totalCost
				+ ", totalArea=" + totalArea + ", constructionDur=" + constructionDur + ", until=" + until
				+ ", enterpriseLicence=" + enterpriseLicence + ", awardGrade=" + awardGrade + ", safetyFlag="
				+ safetyFlag + ", diseaDeathNums=" + diseaDeathNums + ", diseaDisableNums=" + diseaDisableNums
				+ ", acdntDeathNums=" + acdntDeathNums + ", acdntDisableNums=" + acdntDisableNums + ", saftyAcdntFlag="
				+ saftyAcdntFlag + ", floorHeight=" + floorHeight + ", projLocType=" + projLocType + "]";
	}
	
	
}
