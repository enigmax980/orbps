package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;

/**
 * 契约撤销清单Vo
 * 
 * @author xiaoYe
 *
 */
public class ContractRevokeVo implements Serializable {

    private static final long serialVersionUID = 14344544594L;

    /** 投保单号 */
    private String applNo;
    /** 机构号 */
    private String salesBranchNo;
    /** 投保人（汇交人）姓名 */
    private String applName;
    /** 契约撤销操作人员机构号 */
    private String pclkBranchNo;
    /** 契约撤销人员工号 */
    private String pclkNo;
    /** 契约撤销人员姓名 */
    private String pclkName;
    /** 契约撤销时间 */
    private String pclkDate;
    /** 撤销前保单任务环节（保单状态） */
    private String applState;

    /**
     * @return the applNo
     */
    public String getApplNo() {
        return applNo;
    }

    /**
     * @param applNo
     *            the applNo to set
     */
    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    /**
     * @return the salesBranchNo
     */
    public String getSalesBranchNo() {
        return salesBranchNo;
    }

    /**
     * @param salesBranchNo
     *            the salesBranchNo to set
     */
    public void setSalesBranchNo(String salesBranchNo) {
        this.salesBranchNo = salesBranchNo;
    }

    /**
     * @return the applName
     */
    public String getApplName() {
        return applName;
    }

    /**
     * @param applName
     *            the applName to set
     */
    public void setApplName(String applName) {
        this.applName = applName;
    }

    /**
     * @return the pclkBranchNo
     */
    public String getPclkBranchNo() {
        return pclkBranchNo;
    }

    /**
     * @param pclkBranchNo
     *            the pclkBranchNo to set
     */
    public void setPclkBranchNo(String pclkBranchNo) {
        this.pclkBranchNo = pclkBranchNo;
    }

    /**
     * @return the pclkNo
     */
    public String getPclkNo() {
        return pclkNo;
    }

    /**
     * @param pclkNo
     *            the pclkNo to set
     */
    public void setPclkNo(String pclkNo) {
        this.pclkNo = pclkNo;
    }

    /**
     * @return the pclkName
     */
    public String getPclkName() {
        return pclkName;
    }

    /**
     * @param pclkName
     *            the pclkName to set
     */
    public void setPclkName(String pclkName) {
        this.pclkName = pclkName;
    }

    /**
     * @return the pclkDate
     */
    public String getPclkDate() {
        return pclkDate;
    }

    /**
     * @param pclkDate the pclkDate to set
     */
    public void setPclkDate(String pclkDate) {
        this.pclkDate = pclkDate;
    }

    /**
     * @return the applState
     */
    public String getApplState() {
        return applState;
    }

    /**
     * @param applState
     *            the applState to set
     */
    public void setApplState(String applState) {
        this.applState = applState;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContractRevokeVo [applNo=" + applNo + ", salesBranchNo=" + salesBranchNo + ", applName=" + applName
                + ", pclkBranchNo=" + pclkBranchNo + ", pclkNo=" + pclkNo + ", pclkName=" + pclkName + ", pclkDate="
                + pclkDate + ", applState=" + applState + "]";
    }


}
