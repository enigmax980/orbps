package com.newcore.orbps.models.uwbps;
import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;


/**
 * 团体客户信息
 *
 * @author huanghaiyang
 *         创建时间：2016年7月29日上午9:19:42
 */
public class GrpHolderInfoVO implements Serializable {
    private static final long serialVersionUID = -152971415700486195L;
    /**
     *
     */

    //投保人客户号
    @NotNull(message = "团单客户信息——投保人客户号不能为空")
    private long grpPartyId;
    /* 字段名：团体法人客户号 */
    private String grpCustNo;

    /* 字段名：单位名称，长度：200，是否必填：是 */
    @NotNull(message = "团单客户信息——单位名称不能为空")
    private String grpName;

    /* 字段名：证件类别，长度：1，是否必填：是    B：营业执照；S：团体负责人有效证件号码；G：组织机构代码证；T：税务登记证*/
    @NotNull(message = "团单客户信息——证件类型不能为空")
    private String grpIdType;

    /* 字段名：证件号码，长度：18，是否必填：是 */
    @NotNull(message = "团单客户信息——证件号码不能为空")
    private String grpIdNo;

    /* 字段名：企业注册地，长度：3，是否必填：是 */
    @NotNull(message = "团单客户信息——企业注册地不能为空")
    private String grpCountryCode;

    /* 字段名：部门类型，长度：2，是否必填：是   1：中央银行/货币当局；2：银行；3：非银行金融机构；3：其他企业和个人；4：国际组织 */
    @NotNull(message = "团单客户信息——部门类型不能为空")
    private String grpPsnDeptType;

    /* 字段名：行业类别，长度：2，是否必填：是
    00：一般职业；01：农牧业；02：渔业；03：木材森林业；
    04：矿业采石业；05：交通运输业；06：餐旅业；
    07：建筑工程业；08：制造业；09：新闻出版广告业；
    10：卫生；11：娱乐业；12：文教；13：宗教；14：公共事业；
    15：商业；16：金融保险业；17：服务业；
    18：家庭管理；19：治安人员；20：教育；21：其他
    */
    @NotNull(message = "团单客户信息——行业类别不能为空")
    private String occClassCode;

    //邮政编码
    @NotNull(message = "团单客户信息——邮政编码不能为空")
    private String postCode;

    /* 字段名：传真，长度：15，是否必填：否 */
    private String fax;

    /* 字段名：员工总数，是否必填：IF 团单 THEN 必填 */
    @NotNull(message = "团单客户信息——员工总数不能为空")
    private Long numOfEnterprise;

    /* 字段名：在职人数，是否必填：IF 团单 THEN 必填 */
    @NotNull(message = "团单客户信息——在职人数不能为空")
    private Long onjobStaffNum;

    /* 字段名：投保人数，是否必填：IF 团单 THEN 必填 */
    @NotNull(message = "团单客户信息——投保人数不能为空")
    private Long ipsnNum;

    /* 字段名：联系人姓名，长度：200，是否必填：是 */
    @NotNull(message = "团单客户信息——联系人姓名不能为空")
    private String contactName;

    /* 字段名：联系人性别，长度：1，是否必填：否 */
    private String contactSex;

    /* 字段名：联系人出生日期，是否必填：否 */
    private Date contactBirthDate;

    /* 字段名：联系人证件类别，长度：1，是否必填：否 */
    private String contactIdType;

    /* 字段名：联系人证件号码，长度：18，是否必填：否 */
    private String contactIdNo;

    /* 字段名：联系人移动电话，长度：32，是否必填：IF contractTelephone == NULL THEN 必填 */
    private String contactMobile;

    /* 字段名：联系人固定电话，长度：30，是否必填：IF contractMobile == NULL THEN 必填 */
    private String contactTelephone;

    /* 字段名：联系人电子邮件，长度：64，是否必填：否 */
    private String contactEmail;

    //省/自治州
    @NotNull(message = "团单客户信息——省/自治州不能为空")
    private String province;

    //市/州
    @NotNull(message = "团单客户信息——市/州不能为空")
    private String city;

    //  区/县
    @NotNull(message = "团单客户信息——区/县不能为空")
    private String county;

    //镇/乡
    private String town;

    //村/社区
    private String village;

    //地址明细
    @NotNull(message = "团单客户信息——")
    private String homeAddress;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getGrpPartyId() {
        return grpPartyId;
    }

    public void setGrpPartyId(long partyId) {
        this.grpPartyId = partyId;
    }

    public String getGrpCustNo() {
        return grpCustNo;
    }

    public void setGrpCustNo(String grpCustNo) {
        this.grpCustNo = grpCustNo;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpIdType() {
        return grpIdType;
    }

    public void setGrpIdType(String grpIdType) {
        this.grpIdType = grpIdType;
    }

    public String getGrpIdNo() {
        return grpIdNo;
    }

    public void setGrpIdNo(String grpIdNo) {
        this.grpIdNo = grpIdNo;
    }

    public String getGrpCountryCode() {
        return grpCountryCode;
    }

    public void setGrpCountryCode(String grpCountryCode) {
        this.grpCountryCode = grpCountryCode;
    }

    public String getGrpPsnDeptType() {
        return grpPsnDeptType;
    }

    public void setGrpPsnDeptType(String grpPsnDeptType) {
        this.grpPsnDeptType = grpPsnDeptType;
    }

    public String getOccClassCode() {
        return occClassCode;
    }

    public void setOccClassCode(String occClassCode) {
        this.occClassCode = occClassCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getNumOfEnterprise() {
        return numOfEnterprise;
    }

    public void setNumOfEnterprise(Long numOfEmp) {
        this.numOfEnterprise = numOfEmp;
    }

    public Long getOnjobStaffNum() {
        return onjobStaffNum;
    }

    public void setOnjobStaffNum(Long ojEmpNum) {
        this.onjobStaffNum = ojEmpNum;
    }

    public Long getIpsnNum() {
        return ipsnNum;
    }

    public void setIpsnNum(Long ipsnNum) {
        this.ipsnNum = ipsnNum;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactSex() {
        return contactSex;
    }

    public void setContactSex(String contactSex) {
        this.contactSex = contactSex;
    }

    public Date getContactBirthDate() {
        return contactBirthDate;
    }

    public void setContactBirthDate(Date contactBirthDate) {
        this.contactBirthDate = contactBirthDate;
    }

    public String getContactIdType() {
        return contactIdType;
    }

    public void setContactIdType(String contactIdType) {
        this.contactIdType = contactIdType;
    }

    public String getContactIdNo() {
        return contactIdNo;
    }

    public void setContactIdNo(String contactIdNo) {
        this.contactIdNo = contactIdNo;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String home) {
        this.homeAddress = home;
    }
}