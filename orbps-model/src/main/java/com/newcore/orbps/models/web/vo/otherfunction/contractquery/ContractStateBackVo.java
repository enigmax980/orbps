package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;

/**
 * 契约状态回退清单Vo
 * 
 * @author xiaoYe
 *
 */
public class ContractStateBackVo implements Serializable {

    private static final long serialVersionUID = 14344544595L;

    /** 销售机构代码 */
    private String salesBranchNo;
    /** 投保单号 */
    private String applNo;
    /** 投保人（汇交人）姓名 */
    private String applName;
    /** 回退前保单任务环节（保单状态） */
    private String preApplState;
    /** 回退原因 */
    private String cnBackReason;
    /** 目前保单所处任务环节 */
    private String applState;
    /** 契约回退操作时间 */
    private String cnDate;
    /** 回退操作员机构号 */
    private String cnBranchNo;
    /** 回退操作员工号 */
    private String cnNo;
    /** 回退操作员姓名 */
    private String cnName;
    /**
     * @return the salesBranchNo
     */
    public String getSalesBranchNo() {
        return salesBranchNo;
    }
    /**
     * @param salesBranchNo the salesBranchNo to set
     */
    public void setSalesBranchNo(String salesBranchNo) {
        this.salesBranchNo = salesBranchNo;
    }
    /**
     * @return the applNo
     */
    public String getApplNo() {
        return applNo;
    }
    /**
     * @param applNo the applNo to set
     */
    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }
    /**
     * @return the applName
     */
    public String getApplName() {
        return applName;
    }
    /**
     * @param applName the applName to set
     */
    public void setApplName(String applName) {
        this.applName = applName;
    }
    /**
     * @return the preApplState
     */
    public String getPreApplState() {
        return preApplState;
    }
    /**
     * @param preApplState the preApplState to set
     */
    public void setPreApplState(String preApplState) {
        this.preApplState = preApplState;
    }
    /**
     * @return the cnBackReason
     */
    public String getCnBackReason() {
        return cnBackReason;
    }
    /**
     * @param cnBackReason the cnBackReason to set
     */
    public void setCnBackReason(String cnBackReason) {
        this.cnBackReason = cnBackReason;
    }
    /**
     * @return the applState
     */
    public String getApplState() {
        return applState;
    }
    /**
     * @param applState the applState to set
     */
    public void setApplState(String applState) {
        this.applState = applState;
    }
    /**
     * @return the cnDate
     */
    public String getCnDate() {
        return cnDate;
    }
    /**
     * @param cnDate the cnDate to set
     */
    public void setCnDate(String cnDate) {
        this.cnDate = cnDate;
    }
    /**
     * @return the cnBranchNo
     */
    public String getCnBranchNo() {
        return cnBranchNo;
    }
    /**
     * @param cnBranchNo the cnBranchNo to set
     */
    public void setCnBranchNo(String cnBranchNo) {
        this.cnBranchNo = cnBranchNo;
    }
    /**
     * @return the cnNo
     */
    public String getCnNo() {
        return cnNo;
    }
    /**
     * @param cnNo the cnNo to set
     */
    public void setCnNo(String cnNo) {
        this.cnNo = cnNo;
    }
    /**
     * @return the cnName
     */
    public String getCnName() {
        return cnName;
    }
    /**
     * @param cnName the cnName to set
     */
    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContractStateBackVo [salesBranchNo=" + salesBranchNo + ", applNo=" + applNo + ", applName=" + applName
                + ", preApplState=" + preApplState + ", cnBackReason=" + cnBackReason + ", applState=" + applState
                + ", cnDate=" + cnDate + ", cnBranchNo=" + cnBranchNo + ", cnNo=" + cnNo + ", cnName=" + cnName + "]";
    }

}
