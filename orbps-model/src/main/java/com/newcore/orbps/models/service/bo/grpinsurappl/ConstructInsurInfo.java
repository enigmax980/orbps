package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 建工险
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午10:10:25
 */
public class ConstructInsurInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8426695493544813179L;

	/* 字段名：工程名称，长度：128，是否必填：建工险必填，团单特有 */
	private String iobjName;

	/* 字段名：工程地址，长度：128，是否必填：建工险必填，团单特有 */
	private String projLoc;

	/* 字段名：工程类型，长度：2，是否必填：建工险必填，团单特有 */
	private String projType;

	/* 字段名：工程位置类别，长度：2，是否必填：建工险必填，团单特有 */
	private String projLocType;

	/* 字段名：工程总造价，是否必填：建工险必填，团单特有 */
	private Double iobjCost;

	/* 字段名：工程总面积，是否必填：建工险必填，团单特有 */
	private Double iobjSize;

	/* 字段名：保费收取方式，长度：2，是否必填：建工险必填，团单特有 */
	private String premCalType;

	/* 字段名：施工期间始，，是否必填：建工险必填，团单特有 */
	private Date constructFrom;

	/* 字段名：施工期间止，是否必填：建工险必填，团单特有 */
	private Date constructTo;

	/* 字段名：层高(米)，是否必填：建工险必填，团单特有 */
	private Double floorHeight;

	/* 字段名：企业资质，长度：2，是否必填：建工险必填，团单特有 */
	private String enterpriseLicence;

	/* 字段名：获奖情况，长度：2，是否必填：建工险必填，团单特有 */
	private String awardGrade;

	/* 字段名：是否有安防措施，长度：2，是否必填：建工险必填，团单特有 */
	private String safetyFlag;

	/* 字段名：疾病死亡人数，是否必填：建工险必填，团单特有 */
	private Long diseaDeathNums;

	/* 字段名：疾病伤残人数，是否必填：建工险必填，团单特有 */
	private Long diseaDisableNums;

	/* 字段名：意外死亡人数，是否必填：建工险必填，团单特有 */
	private Long acdntDeathNums;

	/* 字段名：意外伤残人数，是否必填：建工险必填，团单特有 */
	private Long acdntDisableNums;

	/* 字段名：过去二年内是否有四级以上安全事故，长度：2，是否必填：建工险必填，团单特有 */
	private String saftyAcdntFlag;

	/**
	 * 
	 */
	public ConstructInsurInfo() {
		super();
	}

	/**
	 * @return the iobjName
	 */
	public String getIobjName() {
		return iobjName;
	}

	/**
	 * @param iobjName
	 *            the iobjName to set
	 */
	public void setIobjName(String iobjName) {
		this.iobjName = iobjName;
	}

	/**
	 * @return the projLoc
	 */
	public String getProjLoc() {
		return projLoc;
	}

	/**
	 * @param projLoc
	 *            the projLoc to set
	 */
	public void setProjLoc(String projLoc) {
		this.projLoc = projLoc;
	}

	/**
	 * @return the projType
	 */
	public String getProjType() {
		return projType;
	}

	/**
	 * @param projType
	 *            the projType to set
	 */
	public void setProjType(String projType) {
		this.projType = projType;
	}

	/**
	 * @return the projLocType
	 */
	public String getProjLocType() {
		return projLocType;
	}

	/**
	 * @param projLocType
	 *            the projLocType to set
	 */
	public void setProjLocType(String projLocType) {
		this.projLocType = projLocType;
	}

	/**
	 * @return the iobjCost
	 */
	public Double getIobjCost() {
		return iobjCost;
	}

	/**
	 * @param iobjCost
	 *            the iobjCost to set
	 */
	public void setIobjCost(Double iobjCost) {
		this.iobjCost = iobjCost;
	}

	/**
	 * @return the iobjSize
	 */
	public Double getIobjSize() {
		return iobjSize;
	}

	/**
	 * @param iobjSize
	 *            the iobjSize to set
	 */
	public void setIobjSize(Double iobjSize) {
		this.iobjSize = iobjSize;
	}

	/**
	 * @return the premCalType
	 */
	public String getPremCalType() {
		return premCalType;
	}

	/**
	 * @param premCalType
	 *            the premCalType to set
	 */
	public void setPremCalType(String premCalType) {
		this.premCalType = premCalType;
	}

	/**
	 * @return the constructFrom
	 */
	public Date getConstructFrom() {
		return constructFrom;
	}

	/**
	 * @param constructFrom
	 *            the constructFrom to set
	 */
	public void setConstructFrom(Date constructFrom) {
		this.constructFrom = constructFrom;
	}

	/**
	 * @return the constructTo
	 */
	public Date getConstructTo() {
		return constructTo;
	}

	/**
	 * @param constructTo
	 *            the constructTo to set
	 */
	public void setConstructTo(Date constructTo) {
		this.constructTo = constructTo;
	}

	/**
	 * @return the floorHeight
	 */
	public Double getFloorHeight() {
		return floorHeight;
	}

	/**
	 * @param floorHeight
	 *            the floorHeight to set
	 */
	public void setFloorHeight(Double floorHeight) {
		this.floorHeight = floorHeight;
	}

	/**
	 * @return the enterpriseLicence
	 */
	public String getEnterpriseLicence() {
		return enterpriseLicence;
	}

	/**
	 * @param enterpriseLicence
	 *            the enterpriseLicence to set
	 */
	public void setEnterpriseLicence(String enterpriseLicence) {
		this.enterpriseLicence = enterpriseLicence;
	}

	/**
	 * @return the awardGrade
	 */
	public String getAwardGrade() {
		return awardGrade;
	}

	/**
	 * @param awardGrade
	 *            the awardGrade to set
	 */
	public void setAwardGrade(String awardGrade) {
		this.awardGrade = awardGrade;
	}

	/**
	 * @return the safetyFlag
	 */
	public String getSafetyFlag() {
		return safetyFlag;
	}

	/**
	 * @param safetyFlag
	 *            the safetyFlag to set
	 */
	public void setSafetyFlag(String safetyFlag) {
		this.safetyFlag = safetyFlag;
	}

	/**
	 * @return the diseaDeathNums
	 */
	public Long getDiseaDeathNums() {
		return diseaDeathNums;
	}

	/**
	 * @param diseaDeathNums
	 *            the diseaDeathNums to set
	 */
	public void setDiseaDeathNums(Long diseaDeathNums) {
		this.diseaDeathNums = diseaDeathNums;
	}

	/**
	 * @return the diseaDisableNums
	 */
	public Long getDiseaDisableNums() {
		return diseaDisableNums;
	}

	/**
	 * @param diseaDisableNums
	 *            the diseaDisableNums to set
	 */
	public void setDiseaDisableNums(Long diseaDisableNums) {
		this.diseaDisableNums = diseaDisableNums;
	}

	/**
	 * @return the acdntDeathNums
	 */
	public Long getAcdntDeathNums() {
		return acdntDeathNums;
	}

	/**
	 * @param acdntDeathNums
	 *            the acdntDeathNums to set
	 */
	public void setAcdntDeathNums(Long acdntDeathNums) {
		this.acdntDeathNums = acdntDeathNums;
	}

	/**
	 * @return the acdntDisableNums
	 */
	public Long getAcdntDisableNums() {
		return acdntDisableNums;
	}

	/**
	 * @param acdntDisableNums
	 *            the acdntDisableNums to set
	 */
	public void setAcdntDisableNums(Long acdntDisableNums) {
		this.acdntDisableNums = acdntDisableNums;
	}

	/**
	 * @return the saftyAcdntFlag
	 */
	public String getSaftyAcdntFlag() {
		return saftyAcdntFlag;
	}

	/**
	 * @param saftyAcdntFlag
	 *            the saftyAcdntFlag to set
	 */
	public void setSaftyAcdntFlag(String saftyAcdntFlag) {
		this.saftyAcdntFlag = saftyAcdntFlag;
	}
}
