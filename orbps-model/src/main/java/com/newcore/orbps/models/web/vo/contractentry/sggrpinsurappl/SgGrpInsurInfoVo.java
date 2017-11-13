package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.Date;

import com.newcore.orbps.models.service.bo.grpinsurappl.Address;

/**
 * 团体汇交信息
 * 
 * @author wangyanjie
 *
 */
public class SgGrpInsurInfoVo implements Serializable {

	private static final long serialVersionUID = 132423443L;
	/** 单位/团体名称 */
	private String companyName;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNo;
	/** 企业注册地 */
	private String companyRegist;
	/** 部门类型 */
	private String departmentType;
	/** 职业类别 */
	private String occClassCode;
	/** 投保人数 */
	private Long gInsuredTotalNum;
	/** 邮编 */
	private String zipCode;
	/** 省/直辖市 */
	private String provinceCode;
	/** 市/城区 */
	private String cityCode;
	/** 县/地级市 */
	private String countyCode;
	/** 乡镇 */
	private String townCode;
	/** 村/社区 */
	private String villageCode;
	/** 联系人姓名 */
	private String contactName;
	/** 联系人证件类型 */
	private String contactIdType;
	/** 联系人证件号码 */
    private String contactIdNo;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private Date birthDate;
	/** 联系人移动电话 */
	private String contactMobile;
	/** 联系人邮箱 */
	private String contactEmail;
	/** 固定电话 */
	private String contactTel;
	/** 传真号码 */
	private String faxNo;
	/** 争议处理方式 */
	private String gSettleDispute;
	/**汇交人详细信息**/
	private String home;
	/**仲裁机构名称**/
	private String parbOrgName;
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}

	/**
	 * @param idType
	 *            the idType to set
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
	 * @param idNo
	 *            the idNo to set
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 * @return the companyRegist
	 */
	public String getCompanyRegist() {
		return companyRegist;
	}

	/**
	 * @param companyRegist
	 *            the companyRegist to set
	 */
	public void setCompanyRegist(String companyRegist) {
		this.companyRegist = companyRegist;
	}

	/**
	 * @return the departmentType
	 */
	public String getDepartmentType() {
		return departmentType;
	}

	/**
	 * @param departmentType
	 *            the departmentType to set
	 */
	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}



	/**
     * @return the occClassCode
     */
    public String getOccClassCode() {
        return occClassCode;
    }

    /**
     * @param occClassCode the occClassCode to set
     */
    public void setOccClassCode(String occClassCode) {
        this.occClassCode = occClassCode;
    }

    /**
	 * @return the insuredTotalNum
	 */
	public Long getGInsuredTotalNum() {
		return gInsuredTotalNum;
	}

	/**
	 * @param insuredTotalNum
	 *            the insuredTotalNum to set
	 */
	public void setGInsuredTotalNum(Long gInsuredTotalNum) {
		this.gInsuredTotalNum = gInsuredTotalNum;
	}

	

	/**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
	 * @return the provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}

	/**
	 * @param provinceCode
	 *            the provinceCode to set
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}

	/**
	 * @param cityCode
	 *            the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * @return the countyCode
	 */
	public String getCountyCode() {
		return countyCode;
	}

	/**
	 * @param countyCode
	 *            the countyCode to set
	 */
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 * @return the townCode
	 */
	public String getTownCode() {
		return townCode;
	}

	/**
	 * @param townCode
	 *            the townCode to set
	 */
	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}

	/**
	 * @return the villageCode
	 */
	public String getVillageCode() {
		return villageCode;
	}

	/**
	 * @param villageCode
	 *            the villageCode to set
	 */
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName
	 *            the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactIdType
	 */
	public String getContactIdType() {
		return contactIdType;
	}

	/**
	 * @param contactIdType
	 *            the contactIdType to set
	 */
	public void setContactIdType(String contactIdType) {
		this.contactIdType = contactIdType;
	}

	
	
	
	/**
     * @return the contactIdNo
     */
    public String getContactIdNo() {
        return contactIdNo;
    }

    /**
     * @param contactIdNo the contactIdNo to set
     */
    public void setContactIdNo(String contactIdNo) {
        this.contactIdNo = contactIdNo;
    }

    /**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the contactMobile
	 */
	public String getContactMobile() {
		return contactMobile;
	}

	/**
	 * @param contactMobile
	 *            the contactMobile to set
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @param contactEmail
	 *            the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * @return the contactTel
	 */
	public String getContactTel() {
		return contactTel;
	}

	/**
	 * @param contactTel
	 *            the contactTel to set
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	/**
	 * @return the faxNo
	 */
	public String getFaxNo() {
		return faxNo;
	}

	/**
	 * @param faxNo
	 *            the faxNo to set
	 */
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}



	/**
	 * @return the gSettleDispute
	 */
	public String getgSettleDispute() {
		return gSettleDispute;
	}

	/**
	 * @param gSettleDispute the gSettleDispute to set
	 */
	public void setgSettleDispute(String gSettleDispute) {
		this.gSettleDispute = gSettleDispute;
	}

	/**
	 * @return the home
	 */
	public String getHome() {
		return home;
	}

	/**
	 * @param home the home to set
	 */
	public void setHome(String home) {
		this.home = home;
	}

	/**
	 * @return the parbOrgName
	 */
	public String getParbOrgName() {
		return parbOrgName;
	}

	/**
	 * @param parbOrgName the parbOrgName to set
	 */
	public void setParbOrgName(String parbOrgName) {
		this.parbOrgName = parbOrgName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgGrpInsurInfoVo [companyName=" + companyName + ", idType=" + idType + ", idNo=" + idNo
				+ ", companyRegist=" + companyRegist + ", departmentType=" + departmentType + ", occClassCode="
				+ occClassCode + ", insuredTotalNum=" + gInsuredTotalNum + ", zipCode=" + zipCode + ", provinceCode="
				+ provinceCode + ", cityCode=" + cityCode + ", countyCode=" + countyCode + ", townCode=" + townCode
				+ ", villageCode=" + villageCode + ", contactName=" + contactName + ", contactIdType=" + contactIdType
				+ ", contactIdNo=" + contactIdNo + ", sex=" + sex + ", birthDate=" + birthDate + ", contactMobile="
				+ contactMobile + ", contactEmail=" + contactEmail + ", contactTel=" + contactTel + ", faxNo=" + faxNo
				+ ", gSettleDispute=" + gSettleDispute + ", home=" + home + ", parbOrgName=" + parbOrgName + "]";
	}







	





}
