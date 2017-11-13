package com.newcore.orbps.models.web.vo.contractentry.longinsuranceentry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 个人长险录入面向前端大Vo
 * @author wangyupeng
 *
 */
public class LongInsurEntry  implements Serializable{
	
	private static final long serialVersionUID = 1444454455L;
	
	
	/** 投保人信息 */
	private LongInsurApplInfoVo applInfoVo;
	/** 被保人信息 */
	private LongInsurInsurInfoVo insurInfoVo;
	/** 增值税信息 */
	private LongInsurAddTaxInfoVo addTaxInfoVo;
	/** 受益人信息 */
	private List<LongInsurBeneficiaryInfoVo> beneficiaryInfoVo= new ArrayList<LongInsurBeneficiaryInfoVo>() ;
	/** 要约信息 */
	private List<LongInsurOfferInfoVo> offerInfoVo = new ArrayList<LongInsurOfferInfoVo>();
	/** 被保人分组信息 */
	private List<InsuredGroupModalVo> InsuredGroupModalVo = new ArrayList<InsuredGroupModalVo>();
	/** 险种责任信息 */
	private List<ResponseVo> ResponseVo = new ArrayList<ResponseVo>();
	/**
	 * @return the applInfoVo
	 */
	public LongInsurApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}
	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(LongInsurApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}
	/**
	 * @return the insurInfoVo
	 */
	public LongInsurInsurInfoVo getInsurInfoVo() {
		return insurInfoVo;
	}
	/**
	 * @param insurInfoVo the insurInfoVo to set
	 */
	public void setInsurInfoVo(LongInsurInsurInfoVo insurInfoVo) {
		this.insurInfoVo = insurInfoVo;
	}
	/**
	 * @return the addTaxInfoVo
	 */
	public LongInsurAddTaxInfoVo getAddTaxInfoVo() {
		return addTaxInfoVo;
	}
	/**
	 * @param addTaxInfoVo the addTaxInfoVo to set
	 */
	public void setAddTaxInfoVo(LongInsurAddTaxInfoVo addTaxInfoVo) {
		this.addTaxInfoVo = addTaxInfoVo;
	}
	/**
	 * @return the beneficiaryInfoVo
	 */
	public List<LongInsurBeneficiaryInfoVo> getBeneficiaryInfoVo() {
		return beneficiaryInfoVo;
	}
	/**
	 * @param beneficiaryInfoVo the beneficiaryInfoVo to set
	 */
	public void setBeneficiaryInfoVo(List<LongInsurBeneficiaryInfoVo> beneficiaryInfoVo) {
		this.beneficiaryInfoVo = beneficiaryInfoVo;
	}
	/**
	 * @return the offerInfoVo
	 */
	public List<LongInsurOfferInfoVo> getOfferInfoVo() {
		return offerInfoVo;
	}
	/**
	 * @param offerInfoVo the offerInfoVo to set
	 */
	public void setOfferInfoVo(List<LongInsurOfferInfoVo> offerInfoVo) {
		this.offerInfoVo = offerInfoVo;
	}
	/**
	 * @return the InsuredGroupModalVo
	 */
	public List<InsuredGroupModalVo> getInsuredGroupModalVo() {
		return InsuredGroupModalVo;
	}
	/**
	 * @param InsuredGroupModalVo the InsuredGroupModalVo to set
	 */
	public void setInsuredGroupModalVo(List<InsuredGroupModalVo> InsuredGroupModalVo) {
		this.InsuredGroupModalVo = InsuredGroupModalVo;
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
		return "LongInsuranceEntry [applInfoVo=" + applInfoVo + ", insurInfoVo=" + insurInfoVo + ", addTaxInfoVo="
				+ addTaxInfoVo + ", beneficiaryInfoVo=" + beneficiaryInfoVo + ", offerInfoVo=" + offerInfoVo
				+ ", InsuredGroupModalVo=" + InsuredGroupModalVo + ", ResponseVo=" + ResponseVo + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addTaxInfoVo == null) ? 0 : addTaxInfoVo.hashCode());
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((InsuredGroupModalVo == null) ? 0 : InsuredGroupModalVo.hashCode());
		result = prime * result + ((beneficiaryInfoVo == null) ? 0 : beneficiaryInfoVo.hashCode());
		result = prime * result + ((insurInfoVo == null) ? 0 : insurInfoVo.hashCode());
		result = prime * result + ((ResponseVo == null) ? 0 : ResponseVo.hashCode());
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
		LongInsurEntry other = (LongInsurEntry) obj;
		if (addTaxInfoVo == null) {
			if (other.addTaxInfoVo != null)
				return false;
		} else if (!addTaxInfoVo.equals(other.addTaxInfoVo))
			return false;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (InsuredGroupModalVo == null) {
			if (other.InsuredGroupModalVo != null)
				return false;
		} else if (!InsuredGroupModalVo.equals(other.InsuredGroupModalVo))
			return false;
		if (beneficiaryInfoVo == null) {
			if (other.beneficiaryInfoVo != null)
				return false;
		} else if (!beneficiaryInfoVo.equals(other.beneficiaryInfoVo))
			return false;
		if (insurInfoVo == null) {
			if (other.insurInfoVo != null)
				return false;
		} else if (!insurInfoVo.equals(other.insurInfoVo))
			return false;
		if (ResponseVo == null) {
			if (other.ResponseVo != null)
				return false;
		} else if (!ResponseVo.equals(other.ResponseVo))
			return false;
		if (offerInfoVo == null) {
			if (other.offerInfoVo != null)
				return false;
		} else if (!offerInfoVo.equals(other.offerInfoVo))
			return false;
		return true;
	}

	
}
