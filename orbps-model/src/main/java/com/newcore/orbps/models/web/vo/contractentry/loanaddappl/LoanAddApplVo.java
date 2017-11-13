package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 小额贷款界面Vo
 * @author jincong
 *
 */
public class LoanAddApplVo  implements Serializable{
	
	private static final long serialVersionUID = 11460300012L;
	
	/** 投保信息 */
	private LoanAddApplInfoVo applInfoVo;
	/** 增值税信息 */
	private LoanAddVatInfoVo vatInfoVo;
	/** 投保人信息 */
	private List<LoanAddHldrInfoVo> ldrInfoVo = new ArrayList<LoanAddHldrInfoVo>();
	/** 被保人信息 */
	private LoanAddIpsnInfoVo ipsnInfoVo;
	/** 受益人信息 */
	private LoanAddBeneficiaryVo beneFiciaryVo;
	/** 要约信息 */
	private List<LoanAddBsInfoVo> bsInfoVo = new ArrayList<LoanAddBsInfoVo>();
	/** 交费信息 */
	private LoanAddChargeInfoVo chargeInfoVo;
	/** 仲裁信息 */
	private LoanAddArbitrationInfoVo arbitrationInfoVo;
	/** 险种责任信息 */
	private List<ResponseVo> responseVo = new ArrayList<ResponseVo>();
	/** 被保人分组信息 */
	private List<InsuredGroupModalVo> insuredGroupModalVo = new ArrayList<InsuredGroupModalVo>();
	/** 组织层级架构信息 */
	private List<OrganizaHierarModalVo> organizaHierarModalVo = new ArrayList<OrganizaHierarModalVo>();
	
	/**
	 * @return the applInfoVo
	 */
	public LoanAddApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}
	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(LoanAddApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}
	/**
	 * @return the vatInfoVo
	 */
	public LoanAddVatInfoVo getVatInfoVo() {
		return vatInfoVo;
	}
	/**
	 * @param vatInfoVo the vatInfoVo to set
	 */
	public void setVatInfoVo(LoanAddVatInfoVo vatInfoVo) {
		this.vatInfoVo = vatInfoVo;
	}
	/**
	 * @return the ldrInfoVo
	 */
	public List<LoanAddHldrInfoVo> getLdrInfoVo() {
		return ldrInfoVo;
	}
	/**
	 * @param ldrInfoVo the ldrInfoVo to set
	 */
	public void setLdrInfoVo(List<LoanAddHldrInfoVo> ldrInfoVo) {
		this.ldrInfoVo = ldrInfoVo;
	}
	/**
	 * @return the ipsnInfoVo
	 */
	public LoanAddIpsnInfoVo getIpsnInfoVo() {
		return ipsnInfoVo;
	}
	/**
	 * @param ipsnInfoVo the ipsnInfoVo to set
	 */
	public void setIpsnInfoVo(LoanAddIpsnInfoVo ipsnInfoVo) {
		this.ipsnInfoVo = ipsnInfoVo;
	}
	/**
	 * @return the beneFiciaryVo
	 */
	public LoanAddBeneficiaryVo getBeneFiciaryVo() {
		return beneFiciaryVo;
	}
	/**
	 * @param beneFiciaryVo the beneFiciaryVo to set
	 */
	public void setBeneFiciaryVo(LoanAddBeneficiaryVo beneFiciaryVo) {
		this.beneFiciaryVo = beneFiciaryVo;
	}
	/**
	 * @return the bsInfoVo
	 */
	public List<LoanAddBsInfoVo> getBsInfoVo() {
		return bsInfoVo;
	}
	/**
	 * @param bsInfoVo the bsInfoVo to set
	 */
	public void setBsInfoVo(List<LoanAddBsInfoVo> bsInfoVo) {
		this.bsInfoVo = bsInfoVo;
	}
	/**
	 * @return the chargeInfoVo
	 */
	public LoanAddChargeInfoVo getChargeInfoVo() {
		return chargeInfoVo;
	}
	/**
	 * @param chargeInfoVo the chargeInfoVo to set
	 */
	public void setChargeInfoVo(LoanAddChargeInfoVo chargeInfoVo) {
		this.chargeInfoVo = chargeInfoVo;
	}
	/**
	 * @return the arbitrationInfoVo
	 */
	public LoanAddArbitrationInfoVo getArbitrationInfoVo() {
		return arbitrationInfoVo;
	}
	/**
	 * @param arbitrationInfoVo the arbitrationInfoVo to set
	 */
	public void setArbitrationInfoVo(LoanAddArbitrationInfoVo arbitrationInfoVo) {
		this.arbitrationInfoVo = arbitrationInfoVo;
	}
	/**
	 * @return the responseVo
	 */
	public List<ResponseVo> getResponseVo() {
		return responseVo;
	}
	/**
	 * @param responseVo the responseVo to set
	 */
	public void setResponseVo(List<ResponseVo> responseVo) {
		this.responseVo = responseVo;
	}
	/**
	 * @return the insuredGroupModalVo
	 */
	public List<InsuredGroupModalVo> getInsuredGroupModalVo() {
		return insuredGroupModalVo;
	}
	/**
	 * @param insuredGroupModalVo the insuredGroupModalVo to set
	 */
	public void setInsuredGroupModalVo(List<InsuredGroupModalVo> insuredGroupModalVo) {
		this.insuredGroupModalVo = insuredGroupModalVo;
	}
	/**
	 * @return the organizaHierarModalVo
	 */
	public List<OrganizaHierarModalVo> getOrganizaHierarModalVo() {
		return organizaHierarModalVo;
	}
	/**
	 * @param organizaHierarModalVo the organizaHierarModalVo to set
	 */
	public void setOrganizaHierarModalVo(List<OrganizaHierarModalVo> organizaHierarModalVo) {
		this.organizaHierarModalVo = organizaHierarModalVo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoanAddApplVo [applInfoVo=" + applInfoVo + ", vatInfoVo=" + vatInfoVo + ", ldrInfoVo=" + ldrInfoVo
				+ ", ipsnInfoVo=" + ipsnInfoVo + ", beneFiciaryVo=" + beneFiciaryVo + ", bsInfoVo=" + bsInfoVo
				+ ", chargeInfoVo=" + chargeInfoVo + ", arbitrationInfoVo=" + arbitrationInfoVo + ", responseVo="
				+ responseVo + ", insuredGroupModalVo=" + insuredGroupModalVo + ", organizaHierarModalVo="
				+ organizaHierarModalVo + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((arbitrationInfoVo == null) ? 0 : arbitrationInfoVo.hashCode());
		result = prime * result + ((beneFiciaryVo == null) ? 0 : beneFiciaryVo.hashCode());
		result = prime * result + ((bsInfoVo == null) ? 0 : bsInfoVo.hashCode());
		result = prime * result + ((chargeInfoVo == null) ? 0 : chargeInfoVo.hashCode());
		result = prime * result + ((insuredGroupModalVo == null) ? 0 : insuredGroupModalVo.hashCode());
		result = prime * result + ((ipsnInfoVo == null) ? 0 : ipsnInfoVo.hashCode());
		result = prime * result + ((ldrInfoVo == null) ? 0 : ldrInfoVo.hashCode());
		result = prime * result + ((organizaHierarModalVo == null) ? 0 : organizaHierarModalVo.hashCode());
		result = prime * result + ((responseVo == null) ? 0 : responseVo.hashCode());
		result = prime * result + ((vatInfoVo == null) ? 0 : vatInfoVo.hashCode());
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
		LoanAddApplVo other = (LoanAddApplVo) obj;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (arbitrationInfoVo == null) {
			if (other.arbitrationInfoVo != null)
				return false;
		} else if (!arbitrationInfoVo.equals(other.arbitrationInfoVo))
			return false;
		if (beneFiciaryVo == null) {
			if (other.beneFiciaryVo != null)
				return false;
		} else if (!beneFiciaryVo.equals(other.beneFiciaryVo))
			return false;
		if (bsInfoVo == null) {
			if (other.bsInfoVo != null)
				return false;
		} else if (!bsInfoVo.equals(other.bsInfoVo))
			return false;
		if (chargeInfoVo == null) {
			if (other.chargeInfoVo != null)
				return false;
		} else if (!chargeInfoVo.equals(other.chargeInfoVo))
			return false;
		if (insuredGroupModalVo == null) {
			if (other.insuredGroupModalVo != null)
				return false;
		} else if (!insuredGroupModalVo.equals(other.insuredGroupModalVo))
			return false;
		if (ipsnInfoVo == null) {
			if (other.ipsnInfoVo != null)
				return false;
		} else if (!ipsnInfoVo.equals(other.ipsnInfoVo))
			return false;
		if (ldrInfoVo == null) {
			if (other.ldrInfoVo != null)
				return false;
		} else if (!ldrInfoVo.equals(other.ldrInfoVo))
			return false;
		if (organizaHierarModalVo == null) {
			if (other.organizaHierarModalVo != null)
				return false;
		} else if (!organizaHierarModalVo.equals(other.organizaHierarModalVo))
			return false;
		if (responseVo == null) {
			if (other.responseVo != null)
				return false;
		} else if (!responseVo.equals(other.responseVo))
			return false;
		if (vatInfoVo == null) {
			if (other.vatInfoVo != null)
				return false;
		} else if (!vatInfoVo.equals(other.vatInfoVo))
			return false;
		return true;
	}
	
}
