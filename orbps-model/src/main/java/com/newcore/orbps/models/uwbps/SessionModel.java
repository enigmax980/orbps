package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

public class SessionModel implements Serializable {

    /** SerId */
    private static final long serialVersionUID = 614744216958642937L;
    /* 操作员工号 */
    private String clerkNo;
    /* 操作员姓名 */
    private String name;
    /* 机构代码 */
    private String branchNo;
    /* 机构名称 */
    private String branchName;
    /* 上级机构代码 */
    private String prioBranchNo;
    /* 邮政编码 */
    private String postCode;
    /* 固定电话 */
    private String tel;
    /* 通讯地址 */
    private String address;
    /* 电子邮箱 */
    private String email;
    /* 处理中心机构代码 */
    private String processingCenter;
    /* 归档机构代码 */
    private String archiveBranchNo;
    /* 出单机构印刷代码 */
    private String printBranchNo;
    /* 出单机构印刷名称 */
    private String printBranchName;
    /* 部门号 */
    private Integer deptNo;
    /* 部门名称 */
    private String deptName;
    /* 部门类别代码 */
    private String deptClass;

    /**
     * @return the prioBranchNo
     */
    public String getPrioBranchNo() {
        return prioBranchNo;
    }

    /**
     * @param prioBranchNo
     *            the prioBranchNo to set
     */
    public void setPrioBranchNo(String prioBranchNo) {
        this.prioBranchNo = prioBranchNo;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode
     *            the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     *            the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the processingCenter
     */
    public String getProcessingCenter() {
        return processingCenter;
    }

    /**
     * @param processingCenter
     *            the processingCenter to set
     */
    public void setProcessingCenter(String processingCenter) {
        this.processingCenter = processingCenter;
    }

    /**
     * @return the archiveBranchNo
     */
    public String getArchiveBranchNo() {
        return archiveBranchNo;
    }

    /**
     * @param archiveBranchNo
     *            the archiveBranchNo to set
     */
    public void setArchiveBranchNo(String archiveBranchNo) {
        this.archiveBranchNo = archiveBranchNo;
    }

    /**
     * @return the printBranchNo
     */
    public String getPrintBranchNo() {
        return printBranchNo;
    }

    /**
     * @param printBranchNo
     *            the printBranchNo to set
     */
    public void setPrintBranchNo(String printBranchNo) {
        this.printBranchNo = printBranchNo;
    }

    /**
     * @return the printBranchName
     */
    public String getPrintBranchName() {
        return printBranchName;
    }

    /**
     * @param printBranchName
     *            the printBranchName to set
     */
    public void setPrintBranchName(String printBranchName) {
        this.printBranchName = printBranchName;
    }
   
    /**
     * @return the deptNo
     */
    public Integer getDeptNo() {
        return deptNo;
    }

    /**
     * @param deptNo the deptNo to set
     */
    public void setDeptNo(Integer deptNo) {
        this.deptNo = deptNo;
    }

    /**
     * @return the deptName
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * @param deptName
     *            the deptName to set
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return the deptClass
     */
    public String getDeptClass() {
        return deptClass;
    }

    /**
     * @param deptClass
     *            the deptClass to set
     */
    public void setDeptClass(String deptClass) {
        this.deptClass = deptClass;
    }

    /**
     * @return the clerkNo
     */
    public String getClerkNo() {
        return clerkNo;
    }

    /**
     * @param clerkNo
     *            the clerkNo to set
     */
    public void setClerkNo(String clerkNo) {
        this.clerkNo = clerkNo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the branchNo
     */
    public String getBranchNo() {
        return branchNo;
    }

    /**
     * @param branchNo
     *            the branchNo to set
     */
    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName
     *            the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SessionModel [clerkNo=" + clerkNo + ", name=" + name + ", branchNo=" + branchNo + ", branchName="
                + branchName + ", prioBranchNo=" + prioBranchNo + ", postCode=" + postCode + ", tel=" + tel
                + ", address=" + address + ", email=" + email + ", processingCenter=" + processingCenter
                + ", archiveBranchNo=" + archiveBranchNo + ", printBranchNo=" + printBranchNo + ", printBranchName="
                + printBranchName + ", deptNo=" + deptNo + ", deptName=" + deptName + ", deptClass=" + deptClass + "]";
    }

}
