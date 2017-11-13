package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 建工险-落地前加工数据
 * 
 * @author lijifei 
 * 创建时间：2016年10月14日上午10:10:25
 */
public class ConstructInsurInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2953318873599893450L;

	/* 字段名：工程名称，长度：128，是否必填：建工险必填，团单特有 */
	private String iobjName;

	/* 字段名：工程地址，长度：128，是否必填：建工险必填，团单特有 */
	private String projLoc;

	/* 字段名：标的编号，是否必填：建工险必填 */
	private long iobjNo;

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

	public ConstructInsurInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIobjName() {
		return iobjName;
	}

	public void setIobjName(String iobjName) {
		this.iobjName = iobjName;
	}

	public String getProjLoc() {
		return projLoc;
	}

	public void setProjLoc(String projLoc) {
		this.projLoc = projLoc;
	}

	public long getIobjNo() {
		return iobjNo;
	}

	public void setIobjNo(long iobjNo) {
		this.iobjNo = iobjNo;
	}

	public String getProjType() {
		return projType;
	}

	public void setProjType(String projType) {
		this.projType = projType;
	}

	public String getProjLocType() {
		return projLocType;
	}

	public void setProjLocType(String projLocType) {
		this.projLocType = projLocType;
	}

	public Double getIobjCost() {
		return iobjCost;
	}

	public void setIobjCost(Double iobjCost) {
		this.iobjCost = iobjCost;
	}

	public Double getIobjSize() {
		return iobjSize;
	}

	public void setIobjSize(Double iobjSize) {
		this.iobjSize = iobjSize;
	}

	public String getPremCalType() {
		return premCalType;
	}

	public void setPremCalType(String premCalType) {
		this.premCalType = premCalType;
	}

	public Date getConstructFrom() {
		return constructFrom;
	}

	public void setConstructFrom(Date constructFrom) {
		this.constructFrom = constructFrom;
	}

	public Date getConstructTo() {
		return constructTo;
	}

	public void setConstructTo(Date constructTo) {
		this.constructTo = constructTo;
	}

	public Double getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(Double floorHeight) {
		this.floorHeight = floorHeight;
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



}
