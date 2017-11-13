package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 保单基本信息
 * @author xiaoye
 *
 */
public class GrpApplBaseInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 132423443L;
	/** 业务标志 */
	private String businessFlag;
	/** 单位/团体名称 */
	private String companyName;
	/** partyId*/
	private String partyId;
	/** 团体客户号*/
	private String grpCustNo;
	/** 职业类别 */
	private String occDangerFactor;
	/** 企业注册地 */
	private String registerArea;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNo;
	/** 部门类型 */
	private String deptType;
	/** 员工总数 */
    private Long numOfEmp;
    /** 在职人数 */
    private Long ojEmpNum;
	/** 投保人数 */
	private Long applNum;
	/** 省/直辖市 */
	private String appAddrProv;
	/** 市/城区 */
	private String appAddrCity;
	/** 县/地级市 */
	private String appAddrTown;
	/** 乡镇 */
	private String appAddrCountry;
	/** 村/社区 */
	private String appAddrValige;
	/** 详细地址 */
    private String appAddrHome;
    /** 邮编 */
	private String appPost;
	/** 联系人姓名 */
	private String connName;
	/** 联系人证件类型 */
	private String connIdType;
	/** 联系人证件号码 */
	private String connIdNo;
	/** 性别 */
	private String appGender;
	/** 出生日期 */
	private Date appBirthday;
	/** 联系人移动电话 */
	private String connPhone;
	/** 联系人邮箱 */
	private String connPostcode;
	/** 固定电话 */
	private String appHomeTel;
	/** 传真号码 */
	private String appHomeFax;
	/** 争议处理方式 */
	private String disputePorcWay;
	/** 仲裁机构名称 */
	private String arbOrgName;
    /**
     * @return the businessFlag
     */
    public String getBusinessFlag() {
        return businessFlag;
    }
    /**
     * @param businessFlag the businessFlag to set
     */
    public void setBusinessFlag(String businessFlag) {
        this.businessFlag = businessFlag;
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
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}
	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	/**
	 * @return the grpCustNo
	 */
	public String getGrpCustNo() {
		return grpCustNo;
	}
	/**
	 * @param grpCustNo the grpCustNo to set
	 */
	public void setGrpCustNo(String grpCustNo) {
		this.grpCustNo = grpCustNo;
	}
	/**
     * @return the occDangerFactor
     */
    public String getOccDangerFactor() {
        return occDangerFactor;
    }
    /**
     * @param occDangerFactor the occDangerFactor to set
     */
    public void setOccDangerFactor(String occDangerFactor) {
        this.occDangerFactor = occDangerFactor;
    }
    /**
     * @return the registerArea
     */
    public String getRegisterArea() {
        return registerArea;
    }
    /**
     * @param registerArea the registerArea to set
     */
    public void setRegisterArea(String registerArea) {
        this.registerArea = registerArea;
    }
    /**
     * @return the idType
     */
    public String getIdType() {
        return idType;
    }
    /**
     * @param idType the idType to set
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }
    /**
     * @return the idNo
     */
    public String getIdNo() {
        return idNo;
    }
    /**
     * @param idNo the idNo to set
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    /**
     * @return the deptType
     */
    public String getDeptType() {
        return deptType;
    }
    /**
     * @param deptType the deptType to set
     */
    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
    /**
     * @return the applNum
     */
    public Long getApplNum() {
        return applNum;
    }
    /**
     * @param applNum the applNum to set
     */
    public void setApplNum(Long applNum) {
        this.applNum = applNum;
    }
    /**
     * @return the appAddrProv
     */
    public String getAppAddrProv() {
        return appAddrProv;
    }
    /**
     * @param appAddrProv the appAddrProv to set
     */
    public void setAppAddrProv(String appAddrProv) {
        this.appAddrProv = appAddrProv;
    }
    /**
     * @return the appAddrCity
     */
    public String getAppAddrCity() {
        return appAddrCity;
    }
    /**
     * @param appAddrCity the appAddrCity to set
     */
    public void setAppAddrCity(String appAddrCity) {
        this.appAddrCity = appAddrCity;
    }
    /**
     * @return the appAddrTown
     */
    public String getAppAddrTown() {
        return appAddrTown;
    }
    /**
     * @param appAddrTown the appAddrTown to set
     */
    public void setAppAddrTown(String appAddrTown) {
        this.appAddrTown = appAddrTown;
    }
    /**
     * @return the appAddrCountry
     */
    public String getAppAddrCountry() {
        return appAddrCountry;
    }
    /**
     * @param appAddrCountry the appAddrCountry to set
     */
    public void setAppAddrCountry(String appAddrCountry) {
        this.appAddrCountry = appAddrCountry;
    }
    /**
     * @return the appAddrValige
     */
    public String getAppAddrValige() {
        return appAddrValige;
    }
    /**
     * @param appAddrValige the appAddrValige to set
     */
    public void setAppAddrValige(String appAddrValige) {
        this.appAddrValige = appAddrValige;
    }
    /**
     * @return the appPost
     */
    public String getAppPost() {
        return appPost;
    }
    /**
     * @param appPost the appPost to set
     */
    public void setAppPost(String appPost) {
        this.appPost = appPost;
    }
    /**
     * @return the connName
     */
    public String getConnName() {
        return connName;
    }
    /**
     * @param connName the connName to set
     */
    public void setConnName(String connName) {
        this.connName = connName;
    }
    /**
     * @return the connIdType
     */
    public String getConnIdType() {
        return connIdType;
    }
    /**
     * @param connIdType the connIdType to set
     */
    public void setConnIdType(String connIdType) {
        this.connIdType = connIdType;
    }
    /**
     * @return the connIdNo
     */
    public String getConnIdNo() {
        return connIdNo;
    }
    /**
     * @param connIdNo the connIdNo to set
     */
    public void setConnIdNo(String connIdNo) {
        this.connIdNo = connIdNo;
    }
    /**
     * @return the appGender
     */
    public String getAppGender() {
        return appGender;
    }
    /**
     * @param appGender the appGender to set
     */
    public void setAppGender(String appGender) {
        this.appGender = appGender;
    }
    /**
     * @return the appBirthday
     */
    public Date getAppBirthday() {
        return appBirthday;
    }
    /**
     * @param appBirthday the appBirthday to set
     */
    public void setAppBirthday(Date appBirthday) {
        this.appBirthday = appBirthday;
    }
    /**
     * @return the connPhone
     */
    public String getConnPhone() {
        return connPhone;
    }
    /**
     * @param connPhone the connPhone to set
     */
    public void setConnPhone(String connPhone) {
        this.connPhone = connPhone;
    }
    /**
     * @return the connPostcode
     */
    public String getConnPostcode() {
        return connPostcode;
    }
    /**
     * @param connPostcode the connPostcode to set
     */
    public void setConnPostcode(String connPostcode) {
        this.connPostcode = connPostcode;
    }
    /**
     * @return the appHomeTel
     */
    public String getAppHomeTel() {
        return appHomeTel;
    }
    /**
     * @param appHomeTel the appHomeTel to set
     */
    public void setAppHomeTel(String appHomeTel) {
        this.appHomeTel = appHomeTel;
    }
    /**
     * @return the appHomeFax
     */
    public String getAppHomeFax() {
        return appHomeFax;
    }
    /**
     * @param appHomeFax the appHomeFax to set
     */
    public void setAppHomeFax(String appHomeFax) {
        this.appHomeFax = appHomeFax;
    }
    /**
     * @return the disputePorcWay
     */
    public String getDisputePorcWay() {
        return disputePorcWay;
    }
    /**
     * @param disputePorcWay the disputePorcWay to set
     */
    public void setDisputePorcWay(String disputePorcWay) {
        this.disputePorcWay = disputePorcWay;
    }
    /**
     * @return the arbOrgName
     */
    public String getArbOrgName() {
        return arbOrgName;
    }
    /**
     * @param arbOrgName the arbOrgName to set
     */
    public void setArbOrgName(String arbOrgName) {
        this.arbOrgName = arbOrgName;
    }
    /**
     * @return the appAddrHome
     */
    public String getAppAddrHome() {
        return appAddrHome;
    }
    /**
     * @param appAddrHome the appAddrHome to set
     */
    public void setAppAddrHome(String appAddrHome) {
        this.appAddrHome = appAddrHome;
    }
    /**
     * @return the numOfEmp
     */
    public Long getNumOfEmp() {
        return numOfEmp;
    }
    /**
     * @param numOfEmp the numOfEmp to set
     */
    public void setNumOfEmp(Long numOfEmp) {
        this.numOfEmp = numOfEmp;
    }
    /**
     * @return the ojEmpNum
     */
    public Long getOjEmpNum() {
        return ojEmpNum;
    }
    /**
     * @param ojEmpNum the ojEmpNum to set
     */
    public void setOjEmpNum(Long ojEmpNum) {
        this.ojEmpNum = ojEmpNum;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GrpApplBaseInfoVo [businessFlag=" + businessFlag + ", companyName=" + companyName + ", occDangerFactor="
                + occDangerFactor + ", registerArea=" + registerArea + ", idType=" + idType + ", idNo=" + idNo
                + ", deptType=" + deptType + ", numOfEmp=" + numOfEmp + ", ojEmpNum=" + ojEmpNum + ", applNum="
                + applNum + ", appAddrProv=" + appAddrProv + ", appAddrCity=" + appAddrCity + ", appAddrTown="
                + appAddrTown + ", appAddrCountry=" + appAddrCountry + ", appAddrValige=" + appAddrValige
                + ", appAddrHome=" + appAddrHome + ", appPost=" + appPost + ", connName=" + connName + ", connIdType="
                + connIdType + ", connIdNo=" + connIdNo + ", appGender=" + appGender + ", appBirthday=" + appBirthday
                + ", connPhone=" + connPhone + ", connPostcode=" + connPostcode + ", appHomeTel=" + appHomeTel
                + ", appHomeFax=" + appHomeFax + ", disputePorcWay=" + disputePorcWay + ", arbOrgName=" + arbOrgName
                + "]";
    }
    
}
