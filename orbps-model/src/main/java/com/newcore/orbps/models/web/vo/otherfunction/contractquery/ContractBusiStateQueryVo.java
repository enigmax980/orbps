package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;
import java.util.List;

/**
 * 新契约业务状态跟踪清单
 * @author xiaoYe
 *
 */
public class ContractBusiStateQueryVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544579L;
	/** 字段名称:受理机构代码 */
	private String bizzBranchNo;
	/** 字段名称:是否包含下级受理机构代码 */
    private String isCotainJuniorBizzBranch;
    /** 字段名称:投保单号 */
    private String applNo;
    /** 字段名称:投保人汇交人姓名 */
    private String applName;
    /** 字段名称:受理员工号 */
    private String bizzNo;
    /** 字段名称:受理人员姓名 */
    private String bizzName;
    /** 字段名称:受理时间 */
    private String bizzDate;
    /** 字段名称:任务当前状态入参 */
    private List<String> taskPresentState;
    /** 字段名称:任务当前状态 */
    private String taskPresentStates;
    /** 字段名称:契约业务形式 */
    private String contractBusiForm;
    /** 字段名称:首个主险种代码 */
    private String polCode;
    /** 字段名称:总保费 */
    private String totalPremium;
    /** 字段名称:交费形式 */
    private String payForm;
	/** 字段名称:销售机构代码 */
    private String salesBranchNo;
    /** 字段名称:是否包含下级销售机构代码 */
    private String isCotainJuniorSaleBranch;
    /** 字段名称:销售渠道 */
    private String salesChannel;
    /** 字段名称:销售人员工号 */
    private String salesNo;
    /** 字段名称:销售人员姓名 */
    private String salesName;
    /** 字段名称: 业务类型 */
    private String busiType;
    /** 字段名称:受理起期 */
    private String startDate;
    /** 字段名称:受理止期 */
    private String endDate;
    /** 详细业务情况 */
    private String detailBusiCondition;
    /**
     * @return the bizzBranchNo
     */
    public String getBizzBranchNo() {
        return bizzBranchNo;
    }
    /**
     * @param bizzBranchNo the bizzBranchNo to set
     */
    public void setBizzBranchNo(String bizzBranchNo) {
        this.bizzBranchNo = bizzBranchNo;
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
     * @return the bizzNo
     */
    public String getBizzNo() {
        return bizzNo;
    }
    /**
     * @param bizzNo the bizzNo to set
     */
    public void setBizzNo(String bizzNo) {
        this.bizzNo = bizzNo;
    }
    /**
     * @return the bizzName
     */
    public String getBizzName() {
        return bizzName;
    }
    /**
     * @param bizzName the bizzName to set
     */
    public void setBizzName(String bizzName) {
        this.bizzName = bizzName;
    }
    /**
     * @return the bizzDate
     */
    public String getBizzDate() {
        return bizzDate;
    }
    /**
     * @param bizzDate the bizzDate to set
     */
    public void setBizzDate(String bizzDate) {
        this.bizzDate = bizzDate;
    }
    /**
     * @return the contractBusiForm
     */
    public String getContractBusiForm() {
        return contractBusiForm;
    }
    /**
     * @param contractBusiForm the contractBusiForm to set
     */
    public void setContractBusiForm(String contractBusiForm) {
        this.contractBusiForm = contractBusiForm;
    }
    /**
     * @return the polCode
     */
    public String getPolCode() {
        return polCode;
    }
    /**
     * @param polCode the polCode to set
     */
    public void setPolCode(String polCode) {
        this.polCode = polCode;
    }
    /**
     * @return the totalPremium
     */
    public String getTotalPremium() {
        return totalPremium;
    }
    /**
     * @param totalPremium the totalPremium to set
     */
    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }
    /**
     * @return the payForm
     */
    public String getPayForm() {
        return payForm;
    }
    /**
     * @param payForm the payForm to set
     */
    public void setPayForm(String payForm) {
        this.payForm = payForm;
    }
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
     * @return the salesChannel
     */
    public String getSalesChannel() {
        return salesChannel;
    }
    /**
     * @param salesChannel the salesChannel to set
     */
    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }
    /**
     * @return the salesNo
     */
    public String getSalesNo() {
        return salesNo;
    }
    /**
     * @param salesNo the salesNo to set
     */
    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }
    /**
     * @return the salesName
     */
    public String getSalesName() {
        return salesName;
    }
    /**
     * @param salesName the salesName to set
     */
    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
    /**
     * @return the busiType
     */
    public String getBusiType() {
        return busiType;
    }
    /**
     * @param busiType the busiType to set
     */
    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /**
     * @return the isCotainJuniorBizzBranch
     */
    public String getIsCotainJuniorBizzBranch() {
        return isCotainJuniorBizzBranch;
    }
    /**
     * @param isCotainJuniorBizzBranch the isCotainJuniorBizzBranch to set
     */
    public void setIsCotainJuniorBizzBranch(String isCotainJuniorBizzBranch) {
        this.isCotainJuniorBizzBranch = isCotainJuniorBizzBranch;
    }
    /**
     * @return the isCotainJuniorSaleBranch
     */
    public String getIsCotainJuniorSaleBranch() {
        return isCotainJuniorSaleBranch;
    }
    /**
     * @param isCotainJuniorSaleBranch the isCotainJuniorSaleBranch to set
     */
    public void setIsCotainJuniorSaleBranch(String isCotainJuniorSaleBranch) {
        this.isCotainJuniorSaleBranch = isCotainJuniorSaleBranch;
    }
    /**
     * @return the taskPresentState
     */
    public List<String> getTaskPresentState() {
        return taskPresentState;
    }
    /**
     * @param taskPresentState the taskPresentState to set
     */
    public void setTaskPresentState(List<String> taskPresentState) {
        this.taskPresentState = taskPresentState;
    }
    /**
     * @return the detailBusiCondition
     */
    public String getDetailBusiCondition() {
        return detailBusiCondition;
    }
    /**
     * @param detailBusiCondition the detailBusiCondition to set
     */
    public void setDetailBusiCondition(String detailBusiCondition) {
        this.detailBusiCondition = detailBusiCondition;
    }
    /**
     * @return the taskPresentStates
     */
    public String getTaskPresentStates() {
        return taskPresentStates;
    }
    /**
     * @param taskPresentStates the taskPresentStates to set
     */
    public void setTaskPresentStates(String taskPresentStates) {
        this.taskPresentStates = taskPresentStates;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContractBusiStateQueryVo [bizzBranchNo=" + bizzBranchNo + ", isCotainJuniorBizzBranch="
                + isCotainJuniorBizzBranch + ", applNo=" + applNo + ", applName=" + applName + ", bizzNo=" + bizzNo
                + ", bizzName=" + bizzName + ", bizzDate=" + bizzDate + ", taskPresentState=" + taskPresentState
                + ", taskPresentStates=" + taskPresentStates + ", contractBusiForm=" + contractBusiForm + ", polCode="
                + polCode + ", totalPremium=" + totalPremium + ", payForm=" + payForm + ", salesBranchNo="
                + salesBranchNo + ", isCotainJuniorSaleBranch=" + isCotainJuniorSaleBranch + ", salesChannel="
                + salesChannel + ", salesNo=" + salesNo + ", salesName=" + salesName + ", busiType=" + busiType
                + ", startDate=" + startDate + ", endDate=" + endDate + ", detailBusiCondition=" + detailBusiCondition
                + "]";
    }
    
}
