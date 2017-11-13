package com.newcore.orbps.models.ipbps;
import java.io.Serializable;
import java.util.List;

public class Branch implements Serializable {

    private static final long serialVersionUID = -2421467601060770228L;

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
    /* 机构状态 */
    private String branchStatus;
    /* 机构状态  描述 */
    private String branchStatusDesc;
    /* 子机构 */
    private List<Branch> children;

    /**
     * @return the children
     */
    public List<Branch> getChildren() {
        return children;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<Branch> children) {
        this.children = children;
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
     * @return the archiveBranch
     */
    public String getArchiveBranchNo() {
        return archiveBranchNo;
    }

    /**
     * @param archiveBranch
     *            the archiveBranch to set
     */
    public void setArchiveBranchNo(String archiveBranchNo) {
        this.archiveBranchNo = archiveBranchNo;
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
     * @return the branchStatus
     */
    public String getBranchStatus() {
        return branchStatus;
    }

    /**
     * @param branchStatus
     *            the branchStatus to set
     */
    public void setBranchStatus(String branchStatus) {
        this.branchStatus = branchStatus;
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
     * @return the branchStatusDesc
     */
    public String getBranchStatusDesc() {
        return branchStatusDesc;
    }

    /**
     * @param branchStatusDesc the branchStatusDesc to set
     */
    public void setBranchStatusDesc(String branchStatusDesc) {
        this.branchStatusDesc = branchStatusDesc;
    }

}
