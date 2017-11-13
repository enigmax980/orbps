package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
/**
 * 新增险种信息 
 * @author wangyanjie 
 *
 */
public class SgGrpAddinsuranceVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000012L;
	
	/** 险种代码*/
	private String busiPrdCode;
	/** 险种名称*/
	private String busiPrdName;
	/** 保额*/
	private Double amount;
	/** 保费*/
	private Double premium;
	/** 承保人数*/
	private Long insuredNum;
	/** 保险期间*/
	private Integer validateDate;
	/**保险期类型*/
	private String insurDurUnit;
	/**健康险专项标识*/
	private String healthInsurFlag;
	/** 责任列表信息*/
    private List<ResponseVo> responseVos = new ArrayList<ResponseVo>();
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
	 * @return the busiPrdName
	 */
	public String getBusiPrdName() {
		return busiPrdName;
	}
	/**
	 * @param busiPrdName the busiPrdName to set
	 */
	public void setBusiPrdName(String busiPrdName) {
		this.busiPrdName = busiPrdName;
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
	 * @return the validateDate
	 */
	public Integer getValidateDate() {
		return validateDate;
	}
	/**
	 * @param validateDate the validateDate to set
	 */
	public void setValidateDate(Integer validateDate) {
		this.validateDate = validateDate;
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
		return "SgGrpAddinsuranceVo [busiPrdCode=" + busiPrdCode + ", busiPrdName=" + busiPrdName + ", amount=" + amount
				+ ", premium=" + premium + ", insuredNum=" + insuredNum + ", validateDate=" + validateDate
				+ ", insurDurUnit=" + insurDurUnit + ", healthInsurFlag=" + healthInsurFlag + ", responseVos="
				+ responseVos + "]";
	}
	
}
