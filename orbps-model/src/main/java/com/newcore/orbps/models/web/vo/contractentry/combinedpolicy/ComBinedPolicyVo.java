package com.newcore.orbps.models.web.vo.contractentry.combinedpolicy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 组合保单面向前端大Vo  
 * @author wangyanjie
 *
 */
public class ComBinedPolicyVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000005L;
	/** 投保单信息 */
	private ComBinedInsurVo  insurInfoVo;
	/** 投保人基本信息 */
	private ComBinedPolicyHolderVo policyHolderInfoVo;
	/** 被保人信息 */
	private ComBinedRecognizeelVo recognizeelVo;
	/** 受益人信息 */
	private List<ComBinedBeneficiaryVo> beneficiaryVo = new ArrayList<ComBinedBeneficiaryVo>();
	/** 缴费信息 */
	private ComBinedPayVo payInfoVo;
	/**
	 * @return the insurInfoVo
	 */
	public ComBinedInsurVo getInsurInfoVo() {
		return insurInfoVo;
	}
	/**
	 * @param insurInfoVo the insurInfoVo to set
	 */
	public void setInsurInfoVo(ComBinedInsurVo insurInfoVo) {
		this.insurInfoVo = insurInfoVo;
	}
	/**
	 * @return the policyHolderInfoVo
	 */
	public ComBinedPolicyHolderVo getPolicyHolderInfoVo() {
		return policyHolderInfoVo;
	}
	/**
	 * @param policyHolderInfoVo the policyHolderInfoVo to set
	 */
	public void setPolicyHolderInfoVo(ComBinedPolicyHolderVo policyHolderInfoVo) {
		this.policyHolderInfoVo = policyHolderInfoVo;
	}
	/**
	 * @return the recognizeelVo
	 */
	public ComBinedRecognizeelVo getRecognizeelVo() {
		return recognizeelVo;
	}
	/**
	 * @param recognizeelVo the recognizeelVo to set
	 */
	public void setRecognizeelVo(ComBinedRecognizeelVo recognizeelVo) {
		this.recognizeelVo = recognizeelVo;
	}
	/**
	 * @return the beneficiaryVo
	 */
	public List<ComBinedBeneficiaryVo> getBeneficiaryVo() {
		return beneficiaryVo;
	}
	/**
	 * @param beneficiaryVo the beneficiaryVo to set
	 */
	public void setBeneficiaryVo(List<ComBinedBeneficiaryVo> beneficiaryVo) {
		this.beneficiaryVo = beneficiaryVo;
	}
	/**
	 * @return the payInfoVo
	 */
	public ComBinedPayVo getPayInfoVo() {
		return payInfoVo;
	}
	/**
	 * @param payInfoVo the payInfoVo to set
	 */
	public void setPayInfoVo(ComBinedPayVo payInfoVo) {
		this.payInfoVo = payInfoVo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CombinedPolicyVo [insurInfoVo=" + insurInfoVo + ", policyHolderInfoVo=" + policyHolderInfoVo
				+ ", recognizeelVo=" + recognizeelVo + ", beneficiaryVo=" + beneficiaryVo + ", payInfoVo=" + payInfoVo
				+ "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beneficiaryVo == null) ? 0 : beneficiaryVo.hashCode());
		result = prime * result + ((insurInfoVo == null) ? 0 : insurInfoVo.hashCode());
		result = prime * result + ((payInfoVo == null) ? 0 : payInfoVo.hashCode());
		result = prime * result + ((policyHolderInfoVo == null) ? 0 : policyHolderInfoVo.hashCode());
		result = prime * result + ((recognizeelVo == null) ? 0 : recognizeelVo.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComBinedPolicyVo other = (ComBinedPolicyVo) obj;
		if (beneficiaryVo == null) {
			if (other.beneficiaryVo != null)
				return false;
		} else if (!beneficiaryVo.equals(other.beneficiaryVo))
			return false;
		if (insurInfoVo == null) {
			if (other.insurInfoVo != null)
				return false;
		} else if (!insurInfoVo.equals(other.insurInfoVo))
			return false;
		if (payInfoVo == null) {
			if (other.payInfoVo != null)
				return false;
		} else if (!payInfoVo.equals(other.payInfoVo))
			return false;
		if (policyHolderInfoVo == null) {
			if (other.policyHolderInfoVo != null)
				return false;
		} else if (!policyHolderInfoVo.equals(other.policyHolderInfoVo))
			return false;
		if (recognizeelVo == null) {
			if (other.recognizeelVo != null)
				return false;
		} else if (!recognizeelVo.equals(other.recognizeelVo))
			return false;
		return true;
	}
	
}
