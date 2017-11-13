package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.Date;
/**
 * 投保人信息Vo
 * @author jincong
 *
 */
public class ListImportHldrInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146180001L;
	
	/** 投保人姓名 */
	private String hldrName;
	/** 投保人性别 */
    private String hldrSex;
    /** 投保人证件类型 */
    private String hldrIdType;
    /** 投保人证件号码 */
    private String hldrIdNo;
    /** 投保人出生日期 */
    private Date hldrBirth;
    /** 投保人手机号 */
    private String hldrPhone;
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
     * @return the hldrSex
     */
    public String getHldrSex() {
        return hldrSex;
    }
    /**
     * @param hldrSex the hldrSex to set
     */
    public void setHldrSex(String hldrSex) {
        this.hldrSex = hldrSex;
    }
    /**
     * @return the hldrIdType
     */
    public String getHldrIdType() {
        return hldrIdType;
    }
    /**
     * @param hldrIdType the hldrIdType to set
     */
    public void setHldrIdType(String hldrIdType) {
        this.hldrIdType = hldrIdType;
    }
    /**
     * @return the hldrIdNo
     */
    public String getHldrIdNo() {
        return hldrIdNo;
    }
    /**
     * @param hldrIdNo the hldrIdNo to set
     */
    public void setHldrIdNo(String hldrIdNo) {
        this.hldrIdNo = hldrIdNo;
    }
    /**
     * @return the hldrBirth
     */
    public Date getHldrBirth() {
        return hldrBirth;
    }
    /**
     * @param hldrBirth the hldrBirth to set
     */
    public void setHldrBirth(Date hldrBirth) {
        this.hldrBirth = hldrBirth;
    }
    /**
     * @return the hldrPhone
     */
    public String getHldrPhone() {
        return hldrPhone;
    }
    /**
     * @param hldrPhone the hldrPhone to set
     */
    public void setHldrPhone(String hldrPhone) {
        this.hldrPhone = hldrPhone;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ListImportHldrVo [hldrName=" + hldrName + ", hldrSex=" + hldrSex + ", hldrIdType=" + hldrIdType
                + ", hldrIdNo=" + hldrIdNo + ", hldrBirth=" + hldrBirth + ", hldrPhone=" + hldrPhone + "]";
    }
}
