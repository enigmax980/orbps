package com.newcore.orbps.models.web.vo.contractentry.cardentry;

import java.io.Serializable;

/**
 * 身故受益人信息Vo
 * @author jincong
 *
 */
public class BsInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146020004L;
	/** 险种代码 */
	private String polCode;
	/** 保险金额 */
	private String insureAmount;
	/** 保险期间 */
	private Double insurDuration;
	/** 保费 */
	private String premium;
	
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
	 * @return the insureAmount
	 */
	public String getInsureAmount() {
		return insureAmount;
	}
	/**
	 * @param insureAmount the insureAmount to set
	 */
	public void setInsureAmount(String insureAmount) {
		this.insureAmount = insureAmount;
	}
	/**
	 * @return the insurDuration
	 */
	public Double getInsurDuration() {
		return insurDuration;
	}
	/**
	 * @param insurDuration the insurDuration to set
	 */
	public void setInsurDuration(Double insurDuration) {
		this.insurDuration = insurDuration;
	}
	/**
	 * @return the premium
	 */
	public String getPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	public void setPremium(String premium) {
		this.premium = premium;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "bsInfoVo [polCode=" + polCode + ", insureAmount=" + insureAmount + ", insurDuration=" + insurDuration
				+ ", premium=" + premium + "]";
	}
	
	
}
