package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 个人长险录入面向前端大Vo
 * @author wangyupeng
 *
 */
public class PerInsurAppl  implements Serializable{
	
	private static final long serialVersionUID = 1444454455L;
	
	/** 投保单信息 */
	private PerInsurApplInfoVo applInfoVo;
	/** 被保人信息 */
	private PerInsurInsuredInfoVo insuredInfoVo;
	/** 增值税信息 */
	private PerInsurAddedTaxInfoVo addedTaxInfoVo;
	/** 受益人信息 */
	private List<PerInsurBeneficiaryInfoVo> beneficiaryInfoVo= new ArrayList<PerInsurBeneficiaryInfoVo>() ;
	/** 要约信息 */
	private List<PerInsurOfferInfoVo> offerInfoVo = new ArrayList<PerInsurOfferInfoVo>();
	
	private List<ResponseVo> ResponseVo = new ArrayList<ResponseVo>();

	/**
	 * @return the applInfoVo
	 */
	public PerInsurApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}

	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(PerInsurApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}

	/**
	 * @return the insuredInfoVo
	 */
	public PerInsurInsuredInfoVo getInsuredInfoVo() {
		return insuredInfoVo;
	}

	/**
	 * @param insuredInfoVo the insuredInfoVo to set
	 */
	public void setInsuredInfoVo(PerInsurInsuredInfoVo insuredInfoVo) {
		this.insuredInfoVo = insuredInfoVo;
	}

	/**
	 * @return the addedTaxInfoVo
	 */
	public PerInsurAddedTaxInfoVo getAddedTaxInfoVo() {
		return addedTaxInfoVo;
	}

	/**
	 * @param addedTaxInfoVo the addedTaxInfoVo to set
	 */
	public void setAddedTaxInfoVo(PerInsurAddedTaxInfoVo addedTaxInfoVo) {
		this.addedTaxInfoVo = addedTaxInfoVo;
	}

	/**
	 * @return the beneficiaryInfoVo
	 */
	public List<PerInsurBeneficiaryInfoVo> getBeneficiaryInfoVo() {
		return beneficiaryInfoVo;
	}

	/**
	 * @param beneficiaryInfoVo the beneficiaryInfoVo to set
	 */
	public void setBeneficiaryInfoVo(List<PerInsurBeneficiaryInfoVo> beneficiaryInfoVo) {
		this.beneficiaryInfoVo = beneficiaryInfoVo;
	}

	/**
	 * @return the offerInfoVo
	 */
	public List<PerInsurOfferInfoVo> getOfferInfoVo() {
		return offerInfoVo;
	}

	/**
	 * @param offerInfoVo the offerInfoVo to set
	 */
	public void setOfferInfoVo(List<PerInsurOfferInfoVo> offerInfoVo) {
		this.offerInfoVo = offerInfoVo;
	}

	/**
	 * @return the ResponseVo
	 */
	public List<ResponseVo> getResponseVo() {
		return ResponseVo;
	}

	/**
	 * @param ResponseVo the ResponseVo to set
	 */
	public void setResponseVo(List<ResponseVo> ResponseVo) {
		this.ResponseVo = ResponseVo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerInsurAppl [applInfoVo=" + applInfoVo + ", insuredInfoVo=" + insuredInfoVo + ", addedTaxInfoVo="
				+ addedTaxInfoVo + ", beneficiaryInfoVo=" + beneficiaryInfoVo + ", offerInfoVo=" + offerInfoVo
				+ ", ResponseVo=" + ResponseVo + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addedTaxInfoVo == null) ? 0 : addedTaxInfoVo.hashCode());
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((beneficiaryInfoVo == null) ? 0 : beneficiaryInfoVo.hashCode());
		result = prime * result + ((ResponseVo == null) ? 0 : ResponseVo.hashCode());
		result = prime * result + ((insuredInfoVo == null) ? 0 : insuredInfoVo.hashCode());
		result = prime * result + ((offerInfoVo == null) ? 0 : offerInfoVo.hashCode());
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
		PerInsurAppl other = (PerInsurAppl) obj;
		if (addedTaxInfoVo == null) {
			if (other.addedTaxInfoVo != null)
				return false;
		} else if (!addedTaxInfoVo.equals(other.addedTaxInfoVo))
			return false;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (beneficiaryInfoVo == null) {
			if (other.beneficiaryInfoVo != null)
				return false;
		} else if (!beneficiaryInfoVo.equals(other.beneficiaryInfoVo))
			return false;
		if (ResponseVo == null) {
			if (other.ResponseVo != null)
				return false;
		} else if (!ResponseVo.equals(other.ResponseVo))
			return false;
		if (insuredInfoVo == null) {
			if (other.insuredInfoVo != null)
				return false;
		} else if (!insuredInfoVo.equals(other.insuredInfoVo))
			return false;
		if (offerInfoVo == null) {
			if (other.offerInfoVo != null)
				return false;
		} else if (!offerInfoVo.equals(other.offerInfoVo))
			return false;
		return true;
	}
	
}
