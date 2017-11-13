package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 险种信息
 * @author xiaoye
 *
 */
public class GrpBusiPrdVo  implements Serializable{
	
	private static final long serialVersionUID = 1142318043L;

	/** 投保单ID */
	private String applId;
	
	/** 险种代码 */
	private String busiPrdCode;
	
	/** 险种名称 */
    private String busiPrdCodeName;
    
	/** 保险期间单位 */
	private String insurDurUnit;
	
	/** 保险期间 */
	private Integer insurDur;
	
	/** 承保人数 */
	private Long insuredNum;
	
	/** 保额*/
	private Double amount;

	/** 保费*/
	private Double premium;
	
	/** 健康险专项标识*/
	private String healthInsurFlag;

	/** 责任列表信息*/
	private List<ResponseVo> responseVos;

	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}

	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
	}

	/**
	 * @return the busiPrdCode
	 */
	public String getBusiPrdCode() {
		return busiPrdCode;
	}

	/**
	 * @param busiPrdCode the busiPrdCode to set
	 */
	public void setBusiPrdCode(String busiPrdCode) {
		this.busiPrdCode = busiPrdCode;
	}

	/**
	 * @return the busiPrdCodeName
	 */
	public String getBusiPrdCodeName() {
		return busiPrdCodeName;
	}

	/**
	 * @param busiPrdCodeName the busiPrdCodeName to set
	 */
	public void setBusiPrdCodeName(String busiPrdCodeName) {
		this.busiPrdCodeName = busiPrdCodeName;
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
	 * @return the insurDur
	 */
	public Integer getInsurDur() {
		return insurDur;
	}

	/**
	 * @param insurDur the insurDur to set
	 */
	public void setInsurDur(Integer insurDur) {
		this.insurDur = insurDur;
	}

	/**
	 * @return the insuredNum
	 */
	public Long getInsuredNum() {
		return insuredNum;
	}

	/**
	 * @param insuredNum the insuredNum to set
	 */
	public void setInsuredNum(Long insuredNum) {
		this.insuredNum = insuredNum;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the premium
	 */
	public Double getPremium() {
		return premium;
	}

	/**
	 * @param premium the premium to set
	 */
	public void setPremium(Double premium) {
		this.premium = premium;
	}

	/**
	 * @return the healthInsurFlag
	 */
	public String getHealthInsurFlag() {
		return healthInsurFlag;
	}

	/**
	 * @param healthInsurFlag the healthInsurFlag to set
	 */
	public void setHealthInsurFlag(String healthInsurFlag) {
		this.healthInsurFlag = healthInsurFlag;
	}

	/**
	 * @return the responseVos
	 */
	public List<ResponseVo> getResponseVos() {
		return responseVos;
	}

	/**
	 * @param responseVos the responseVos to set
	 */
	public void setResponseVos(List<ResponseVo> responseVos) {
		this.responseVos = responseVos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GrpBusiPrdVo [applId=" + applId + ", busiPrdCode=" + busiPrdCode + ", busiPrdCodeName="
				+ busiPrdCodeName + ", insurDurUnit=" + insurDurUnit + ", insurDur=" + insurDur + ", insuredNum="
				+ insuredNum + ", amount=" + amount + ", premium=" + premium + ", healthInsurFlag=" + healthInsurFlag
				+ ", responseVos=" + responseVos + "]";
	}

	
}
