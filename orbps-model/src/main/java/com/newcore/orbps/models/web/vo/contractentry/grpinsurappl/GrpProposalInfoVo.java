package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 要约信息
 * @author xiaoye
 *
 */
public class GrpProposalInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 9222465343L;
	/** 指定生效日 */
	private Date inForceDate;
	/** 保险期间单位 */
	private String insurDurUnit;
	/** 首期扣款截止日期 */
	private Date enstPremDeadline;
	/** 被保险人总数 */
	private Long ipsnNum;
	/** 频次是否生效 */
	private String frequenceEff;
	/** 生效频率 */
	private Integer forceNum;
	/** 生效方式 */
	private String forceType;
	/** 保费合计 */
	private Double sumPrem;
	/** 特别约定 */
	private String specialPro;
	/**
	 * @return the inForceDate
	 */
	public Date getInForceDate() {
		return inForceDate;
	}
	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
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
	 * @return the enstPremDeadline
	 */
	public Date getEnstPremDeadline() {
		return enstPremDeadline;
	}
	/**
	 * @param enstPremDeadline the enstPremDeadline to set
	 */
	public void setEnstPremDeadline(Date enstPremDeadline) {
		this.enstPremDeadline = enstPremDeadline;
	}
	/**
	 * @return the ipsnNum
	 */
	public Long getIpsnNum() {
		return ipsnNum;
	}
	/**
	 * @param ipsnNum the ipsnNum to set
	 */
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}
	/**
	 * @return the frequenceEff
	 */
	public String getFrequenceEff() {
		return frequenceEff;
	}
	/**
	 * @param frequenceEff the frequenceEff to set
	 */
	public void setFrequenceEff(String frequenceEff) {
		this.frequenceEff = frequenceEff;
	}
	/**
	 * @return the forceNum
	 */
	public Integer getForceNum() {
		return forceNum;
	}
	/**
	 * @param forceNum the forceNum to set
	 */
	public void setForceNum(Integer forceNum) {
		this.forceNum = forceNum;
	}
	/**
	 * @return the forceType
	 */
	public String getForceType() {
		return forceType;
	}
	/**
	 * @param forceType the forceType to set
	 */
	public void setForceType(String forceType) {
		this.forceType = forceType;
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
	 * @return the specialPro
	 */
	public String getSpecialPro() {
		return specialPro;
	}
	/**
	 * @param specialPro the specialPro to set
	 */
	public void setSpecialPro(String specialPro) {
		this.specialPro = specialPro;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GrpProposalInfoVo [inForceDate=" + inForceDate + ", insurDurUnit=" + insurDurUnit
                + ", enstPremDeadline=" + enstPremDeadline + ", ipsnNum=" + ipsnNum + ", frequenceEff=" + frequenceEff
                + ", forceNum=" + forceNum + ", forceType=" + forceType + ", sumPrem=" + sumPrem + ", specialPro="
                + specialPro + "]";
    }
	
}
