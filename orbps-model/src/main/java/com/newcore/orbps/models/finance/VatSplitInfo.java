package com.newcore.orbps.models.finance;

import java.io.Serializable;

/**
 * 营改增财务拆分类
 * Created by liushuaifeng on 2017/2/22 0022.
 */
public class VatSplitInfo implements Serializable {
    /**
     * vat流水号: S2+mio_log.mgr_branch_no+mio_log.mio_log_id
     */
    private String vatId;

    /**
     * 投保单号
     */
    private String applNo;

    /**
     *系统来源
     */
    private String sysNo;

    /**
     * 投保人名称
     */
    private String grpName;

    /**
     * 团体客户号
     */
    private String grpCustNo;

    /**
     * 团体证件号码
     */
    private String grpIdNo;

    /**
     * 购方手机号
     */
    private String contactMobile;

    /**
     * 购方电子邮箱
     */
    private String contactEmail;

    /**
     * 含税保费
     */
    private Double premium;

    public String getVatId() {
        return vatId;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public String getApplNo() {
        return applNo;
    }

    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpCustNo() {
        return grpCustNo;
    }

    public void setGrpCustNo(String grpCustNo) {
        this.grpCustNo = grpCustNo;
    }

    public String getGrpIdNo() {
        return grpIdNo;
    }

    public void setGrpIdNo(String grpIdNo) {
        this.grpIdNo = grpIdNo;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }
}
