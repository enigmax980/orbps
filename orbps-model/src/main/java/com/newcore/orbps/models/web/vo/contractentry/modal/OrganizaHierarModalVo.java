package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;

/**
 * 组织层次信息
 * @author xiaoYe
 *
 */
public class OrganizaHierarModalVo  implements Serializable{
	
	private static final long serialVersionUID = 5467453243L;
    /** 层级代码 */
    private String levelCode;
    /** 上级层级代码 */
    private String prioLevelCode;
    /** 节点类型 */
    private String nodeType;
    /** 是否根节点 */
    private String isRoot;
    /** 节点交费金额 */
    private Double nodePayAmnt;
    /** partyId*/
    private String partyId;
	/** 客户号 */
	private String custNo;
	/** 单位/团体名称 */
	private String companyName;
	/** 部门名称 */
    private String groupDep;
	/** 曾用名 */
	private String oldName;
	/** 单位性质 */
	private String unitCharacter;
	/** 行业性质细分  法律*/
	private String legalCode;
	/** 单位性质 经济分类 */
    private String natureCode;
	/** 行业类型 */
	private String industryClassification;
	/** 证件类型 */
    private String deptType;
    /** 成员总数 */
    private Long totalMembers;
    /** 证件号码 */
    private String idCardNo;
    /** 在职人数 */
    private Long ojEmpNum;
    /** 投保人数 */
    private Long applNum;
    /** 省/自治州 */
    private String province;
    /** 市/州 */
    private String city;
    /** 区/县 */
    private String county;
    /** 镇/乡 */
    private String town;
    /** 村/社区 */
    private String village;
    /** 地址明细 */
    private String detailAddress;
    /** 邮政编码 */
    private String postCode;
    /** 企业注册国籍 */
    private String registeredNationality;
    /** 联系人姓名 */
    private String contactName;
    /** 手机号码 */
    private String phoneNum;
    /** 电子邮件 */
    private String email;
    /** 固定电话 */
    private String fixedPhones;
    /** 是否缴费 */
    private String pay;
    /** 保全选项 */
    private String securityOptions;
    /** 服务指派 */
    private String serviceAssignment;
    /** 发票选项 */
    private String invoiceOption;
    /** 增值税号 */
    private Integer vatNum;
    /** 开户银行 */
    private String bankCode;
    /** 开户名称 */
    private String bankaccName;
    /** 银行账号 */
    private String bankaccNo;
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
     * @return the custNo
     */
    public String getCustNo() {
        return custNo;
    }
    /**
     * @param custNo the custNo to set
     */
    public void setCustNo(String custNo) {
        this.custNo = custNo;
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
     * @return the oldName
     */
    public String getOldName() {
        return oldName;
    }
    /**
     * @param oldName the oldName to set
     */
    public void setOldName(String oldName) {
        this.oldName = oldName;
    }
    /**
     * @return the unitCharacter
     */
    public String getUnitCharacter() {
        return unitCharacter;
    }
    /**
     * @param unitCharacter the unitCharacter to set
     */
    public void setUnitCharacter(String unitCharacter) {
        this.unitCharacter = unitCharacter;
    }
    /**
     * @return the industryClassification
     */
    public String getIndustryClassification() {
        return industryClassification;
    }
    /**
     * @param industryClassification the industryClassification to set
     */
    public void setIndustryClassification(String industryClassification) {
        this.industryClassification = industryClassification;
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
     * @return the totalMembers
     */
    public Long getTotalMembers() {
        return totalMembers;
    }
    /**
     * @param totalMembers the totalMembers to set
     */
    public void setTotalMembers(Long totalMembers) {
        this.totalMembers = totalMembers;
    }
    /**
     * @return the idCardNo
     */
    public String getIdCardNo() {
        return idCardNo;
    }
    /**
     * @param idCardNo the idCardNo to set
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
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
     * @return the province
     */
    public String getProvince() {
        return province;
    }
    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }
    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @return the county
     */
    public String getCounty() {
        return county;
    }
    /**
     * @param county the county to set
     */
    public void setCounty(String county) {
        this.county = county;
    }
    /**
     * @return the town
     */
    public String getTown() {
        return town;
    }
    /**
     * @param town the town to set
     */
    public void setTown(String town) {
        this.town = town;
    }
    /**
     * @return the village
     */
    public String getVillage() {
        return village;
    }
    /**
     * @param village the village to set
     */
    public void setVillage(String village) {
        this.village = village;
    }
    /**
     * @return the detailAddress
     */
    public String getDetailAddress() {
        return detailAddress;
    }
    /**
     * @param detailAddress the detailAddress to set
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
    /**
     * @return the registeredNationality
     */
    public String getRegisteredNationality() {
        return registeredNationality;
    }
    /**
     * @param registeredNationality the registeredNationality to set
     */
    public void setRegisteredNationality(String registeredNationality) {
        this.registeredNationality = registeredNationality;
    }
    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * @return the phoneNum
     */
    public String getPhoneNum() {
        return phoneNum;
    }
    /**
     * @param phoneNum the phoneNum to set
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the fixedPhones
     */
    public String getFixedPhones() {
        return fixedPhones;
    }
    /**
     * @param fixedPhones the fixedPhones to set
     */
    public void setFixedPhones(String fixedPhones) {
        this.fixedPhones = fixedPhones;
    }
    /**
     * @return the pay
     */
    public String getPay() {
        return pay;
    }
    /**
     * @param pay the pay to set
     */
    public void setPay(String pay) {
        this.pay = pay;
    }
    /**
     * @return the securityOptions
     */
    public String getSecurityOptions() {
        return securityOptions;
    }
    /**
     * @param securityOptions the securityOptions to set
     */
    public void setSecurityOptions(String securityOptions) {
        this.securityOptions = securityOptions;
    }
    /**
     * @return the serviceAssignment
     */
    public String getServiceAssignment() {
        return serviceAssignment;
    }
    /**
     * @param serviceAssignment the serviceAssignment to set
     */
    public void setServiceAssignment(String serviceAssignment) {
        this.serviceAssignment = serviceAssignment;
    }
    /**
     * @return the invoiceOption
     */
    public String getInvoiceOption() {
        return invoiceOption;
    }
    /**
     * @param invoiceOption the invoiceOption to set
     */
    public void setInvoiceOption(String invoiceOption) {
        this.invoiceOption = invoiceOption;
    }
    /**
     * @return the vatNum
     */
    public Integer getVatNum() {
        return vatNum;
    }
    /**
     * @param vatNum the vatNum to set
     */
    public void setVatNum(Integer vatNum) {
        this.vatNum = vatNum;
    }
    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }
    /**
     * @param postCode the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    /**
     * @return the levelCode
     */
    public String getLevelCode() {
        return levelCode;
    }
    /**
     * @param levelCode the levelCode to set
     */
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
    /**
     * @return the prioLevelCode
     */
    public String getPrioLevelCode() {
        return prioLevelCode;
    }
    /**
     * @param prioLevelCode the prioLevelCode to set
     */
    public void setPrioLevelCode(String prioLevelCode) {
        this.prioLevelCode = prioLevelCode;
    }
    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }
    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
    /**
     * @return the isRoot
     */
    public String getIsRoot() {
        return isRoot;
    }
    /**
     * @param isRoot the isRoot to set
     */
    public void setIsRoot(String isRoot) {
        this.isRoot = isRoot;
    }
    /**
     * @return the nodePayAmnt
     */
    public Double getNodePayAmnt() {
        return nodePayAmnt;
    }
    /**
     * @param nodePayAmnt the nodePayAmnt to set
     */
    public void setNodePayAmnt(Double nodePayAmnt) {
        this.nodePayAmnt = nodePayAmnt;
    }
    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }
    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * @return the bankaccName
     */
    public String getBankaccName() {
        return bankaccName;
    }
    /**
     * @param bankaccName the bankaccName to set
     */
    public void setBankaccName(String bankaccName) {
        this.bankaccName = bankaccName;
    }
    /**
     * @return the bankaccNo
     */
    public String getBankaccNo() {
        return bankaccNo;
    }
    /**
     * @param bankaccNo the bankaccNo to set
     */
    public void setBankaccNo(String bankaccNo) {
        this.bankaccNo = bankaccNo;
    }
    /**
     * @return the legalCode
     */
    public String getLegalCode() {
        return legalCode;
    }
    /**
     * @param legalCode the legalCode to set
     */
    public void setLegalCode(String legalCode) {
        this.legalCode = legalCode;
    }
    /**
     * @return the natureCode
     */
    public String getNatureCode() {
        return natureCode;
    }
    /**
     * @param natureCode the natureCode to set
     */
    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }
    /**
     * @return the groupDep
     */
    public String getGroupDep() {
        return groupDep;
    }
    /**
     * @param groupDep the groupDep to set
     */
    public void setGroupDep(String groupDep) {
        this.groupDep = groupDep;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OrganizaHierarModalVo [levelCode=" + levelCode + ", prioLevelCode=" + prioLevelCode + ", nodeType="
                + nodeType + ", isRoot=" + isRoot + ", nodePayAmnt=" + nodePayAmnt + ", partyId=" + partyId
                + ", custNo=" + custNo + ", companyName=" + companyName + ", groupDep=" + groupDep + ", oldName="
                + oldName + ", unitCharacter=" + unitCharacter + ", legalCode=" + legalCode + ", natureCode="
                + natureCode + ", industryClassification=" + industryClassification + ", deptType=" + deptType
                + ", totalMembers=" + totalMembers + ", idCardNo=" + idCardNo + ", ojEmpNum=" + ojEmpNum + ", applNum="
                + applNum + ", province=" + province + ", city=" + city + ", county=" + county + ", town=" + town
                + ", village=" + village + ", detailAddress=" + detailAddress + ", postCode=" + postCode
                + ", registeredNationality=" + registeredNationality + ", contactName=" + contactName + ", phoneNum="
                + phoneNum + ", email=" + email + ", fixedPhones=" + fixedPhones + ", pay=" + pay + ", securityOptions="
                + securityOptions + ", serviceAssignment=" + serviceAssignment + ", invoiceOption=" + invoiceOption
                + ", vatNum=" + vatNum + ", bankCode=" + bankCode + ", bankaccName=" + bankaccName + ", bankaccNo="
                + bankaccNo + "]";
    }
    
}
