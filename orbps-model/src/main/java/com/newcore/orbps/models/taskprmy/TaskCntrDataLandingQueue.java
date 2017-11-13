package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;
import java.util.Date;

/**
 * 保单落地批作业监控表
 * Created by liushuaifeng on 2017/2/13 0013.
 */
public class TaskCntrDataLandingQueue implements Serializable {
    /**
     * 任务ID
     */
    private Long taskSeq;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 销售机构号
     */
    private String salesBranchNo;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 开始执行时间
     */
    private Date startTime;

    /**
     * 任务完成时间
     */
    private Date endTime;
    /**
     * 执行次数
     */
    private Integer askTimes;

    /**
     * 投保单号
     */
    private String applNo;

    /**
     * 合同号
     */
    private String cgNo;

    /**
     * spring batch实例运行ID
     */
    private Long jobInstanceId;

    /**
     * 契约类型
     */
    private String cntrType;

    /**
     * 清单类型
     */
    private String lstProcType;

    /**
     * 是否续期扣款
     */
    private String isRenew;

    /**
     * 是否多期暂交
     */
    private String isMultiPay;

    /**
     * 是否共保
     */
    private String isCommonAgreement;

    /**
     * 财务落地批次号
     */
    private Long finLandBatNo;

    /**
     * 财务落地标记FIN_LAND_FLAG
     */
    private String finLandFlag;

    /**
     * 被保人落地标记 IPSN_LAND_FLAG
     */
    private String ipsnLandFlag;

    /**
     * 保单基本信息落地标记:INSUR_APPL_LAND_FLAG
     */
    private String insurApplLandFlag;

    /**
     * 共保协议落地标记：COMMON_AGREEMENT_LAND_FLAG
     */
    private String commonAgreementLandFlag;

    /**
     * 备注
     */
    private String remark;

    private String extKey0;

    private String extKey1;

    private String extKey2;

    private String extKey3;

    private String extKey4;

    private String extKey5;
    
  private String plnLandFlag;   //分期应收 共保应付 
    
    private Long plnLandBatNo;  //应收落地批次号
    
    private Double sumPremium;   //总保费
    
    private String isStepPlnmio;  //是否分期
    

    public String getPlnLandFlag() {
		return plnLandFlag;
	}

	public void setPlnLandFlag(String plnLandFlag) {
		this.plnLandFlag = plnLandFlag;
	}

	public Long getPlnLandBatNo() {
		return plnLandBatNo;
	}

	public void setPlnLandBatNo(Long plnLandBatNo) {
		this.plnLandBatNo = plnLandBatNo;
	}

	public Double getSumPremium() {
		return sumPremium;
	}

	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}

	public String getIsStepPlnmio() {
		return isStepPlnmio;
	}

	public void setIsStepPlnmio(String isStepPlnmio) {
		this.isStepPlnmio = isStepPlnmio;
	}

	public Long getTaskSeq() {
        return taskSeq;
    }

    public void setTaskSeq(Long taskSeq) {
        this.taskSeq = taskSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalesBranchNo() {
        return salesBranchNo;
    }

    public void setSalesBranchNo(String salesBranchNo) {
        this.salesBranchNo = salesBranchNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getAskTimes() {
        return askTimes;
    }

    public void setAskTimes(Integer askTimes) {
        this.askTimes = askTimes;
    }

    public String getApplNo() {
        return applNo;
    }

    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    public String getCgNo() {
        return cgNo;
    }

    public void setCgNo(String cgNo) {
        this.cgNo = cgNo;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getCntrType() {
        return cntrType;
    }

    public void setCntrType(String cntrType) {
        this.cntrType = cntrType;
    }

    public String getLstProcType() {
        return lstProcType;
    }

    public void setLstProcType(String lstProcType) {
        this.lstProcType = lstProcType;
    }

    public String getIsRenew() {
        return isRenew;
    }

    public void setIsRenew(String isRenew) {
        this.isRenew = isRenew;
    }

    public String getIsMultiPay() {
        return isMultiPay;
    }

    public void setIsMultiPay(String isMultiPay) {
        this.isMultiPay = isMultiPay;
    }

    public String getIsCommonAgreement() {
        return isCommonAgreement;
    }

    public void setIsCommonAgreement(String isCommonAgreement) {
        this.isCommonAgreement = isCommonAgreement;
    }

    public Long getFinLandBatNo() {
        return finLandBatNo;
    }

    public void setFinLandBatNo(Long finLandBatNo) {
        this.finLandBatNo = finLandBatNo;
    }

    public String getFinLandFlag() {
        return finLandFlag;
    }

    public void setFinLandFlag(String finLandFlag) {
        this.finLandFlag = finLandFlag;
    }

    public String getIpsnLandFlag() {
        return ipsnLandFlag;
    }

    public void setIpsnLandFlag(String ipsnLandFlag) {
        this.ipsnLandFlag = ipsnLandFlag;
    }

    public String getInsurApplLandFlag() {
        return insurApplLandFlag;
    }

    public void setInsurApplLandFlag(String insurApplLandFlag) {
        this.insurApplLandFlag = insurApplLandFlag;
    }

    public String getCommonAgreementLandFlag() {
        return commonAgreementLandFlag;
    }

    public void setCommonAgreementLandFlag(String commonAgreementLandFlag) {
        this.commonAgreementLandFlag = commonAgreementLandFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtKey0() {
        return extKey0;
    }

    public void setExtKey0(String extKey0) {
        this.extKey0 = extKey0;
    }

    public String getExtKey1() {
        return extKey1;
    }

    public void setExtKey1(String extKey1) {
        this.extKey1 = extKey1;
    }

    public String getExtKey2() {
        return extKey2;
    }

    public void setExtKey2(String extKey2) {
        this.extKey2 = extKey2;
    }

    public String getExtKey3() {
        return extKey3;
    }

    public void setExtKey3(String extKey3) {
        this.extKey3 = extKey3;
    }

    public String getExtKey4() {
        return extKey4;
    }

    public void setExtKey4(String extKey4) {
        this.extKey4 = extKey4;
    }

    public String getExtKey5() {
        return extKey5;
    }

    public void setExtKey5(String extKey5) {
        this.extKey5 = extKey5;
    }
}

