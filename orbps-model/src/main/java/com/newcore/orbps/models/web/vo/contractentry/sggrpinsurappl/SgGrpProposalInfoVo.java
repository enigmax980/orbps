package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 要约信息
 * @author wangyanjie
 *
 */
public class SgGrpProposalInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 9222465343L;
	
	/** 被保险人总数 */
	private Long insuredTotalNum;
	/** 保险期间 */
	private Integer dateType;
	/** 保费合计 */
	private Double sumPrem;
	/** 生效方式 */
	private String effectType;
	/** 指定生效日 */
	private Date speEffectDate;
	/** 频次是否生效 */
	private String frequencyEffectFlag;
	/** 生效频率 */
	private Integer effectFreq;
	/** 保险期间单位 */
	private String insurDurUnit;
	/** 首期扣款截止日期 */
	private Date firstChargeDate;
	/** 特别约定 */
	private String convention;
	/** 新增险种信息 */
	private List<SgGrpAddinsuranceVo> addinsuranceVos = new ArrayList<SgGrpAddinsuranceVo>();
	/**
	 * @return the insuredTotalNum
	 */
	public Long getInsuredTotalNum() {
		return insuredTotalNum;
	}
	/**
	 * @param insuredTotalNum the insuredTotalNum to set
	 */
	public void setInsuredTotalNum(Long insuredTotalNum) {
		this.insuredTotalNum = insuredTotalNum;
	}
	/**
	 * @return the dateType
	 */
	public Integer getDateType() {
		return dateType;
	}
	/**
	 * @param dateType the dateType to set
	 */
	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}
	/**
	 * @return the sumPrem
	 */
	public Double getSumPrem() {
		return sumPrem;
	}
	/**
	 * @param sumPrem the sumPrem to set
	 */
	public void setSumPrem(Double sumPrem) {
		this.sumPrem = sumPrem;
	}
	/**
	 * @return the effectType
	 */
	public String getEffectType() {
		return effectType;
	}
	/**
	 * @param effectType the effectType to set
	 */
	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}
	/**
	 * @return the speEffectDate
	 */
	public Date getSpeEffectDate() {
		return speEffectDate;
	}
	/**
	 * @param speEffectDate the speEffectDate to set
	 */
	public void setSpeEffectDate(Date speEffectDate) {
		this.speEffectDate = speEffectDate;
	}
	/**
	 * @return the frequencyEffectFlag
	 */
	public String getFrequencyEffectFlag() {
		return frequencyEffectFlag;
	}
	/**
	 * @param frequencyEffectFlag the frequencyEffectFlag to set
	 */
	public void setFrequencyEffectFlag(String frequencyEffectFlag) {
		this.frequencyEffectFlag = frequencyEffectFlag;
	}
	/**
	 * @return the effectFreq
	 */
	public Integer getEffectFreq() {
		return effectFreq;
	}
	/**
	 * @param effectFreq the effectFreq to set
	 */
	public void setEffectFreq(Integer effectFreq) {
		this.effectFreq = effectFreq;
	}
	/**
	 * @return the insurDurUnit
	 */
	public String getInsurDurUnit() {
		return insurDurUnit;
	}
	/**
	 * @param insurDurUnit the insurDurUnit to set
	 */
	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}
	/**
	 * @return the firstChargeDate
	 */
	public Date getFirstChargeDate() {
		return firstChargeDate;
	}
	/**
	 * @param firstChargeDate the firstChargeDate to set
	 */
	public void setFirstChargeDate(Date firstChargeDate) {
		this.firstChargeDate = firstChargeDate;
	}
	/**
	 * @return the convention
	 */
	public String getConvention() {
		return convention;
	}
	/**
	 * @param convention the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}
    /**
     * @return the addinsuranceVos
     */
    public List<SgGrpAddinsuranceVo> getAddinsuranceVos() {
        return addinsuranceVos;
    }
    /**
     * @param addinsuranceVos the addinsuranceVos to set
     */
    public void setAddinsuranceVos(List<SgGrpAddinsuranceVo> addinsuranceVos) {
        this.addinsuranceVos = addinsuranceVos;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SgGrpProposalInfoVo [insuredTotalNum=" + insuredTotalNum + ", dateType=" + dateType + ", sumPrem="
                + sumPrem + ", effectType=" + effectType + ", speEffectDate=" + speEffectDate + ", frequencyEffectFlag="
                + frequencyEffectFlag + ", effectFreq=" + effectFreq + ", insurDurUnit=" + insurDurUnit
                + ", firstChargeDate=" + firstChargeDate + ", convention=" + convention + ", addinsuranceVos="
                + addinsuranceVos + "]";
    }

}
