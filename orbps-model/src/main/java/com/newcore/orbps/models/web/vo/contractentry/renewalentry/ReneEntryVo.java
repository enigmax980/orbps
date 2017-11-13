package com.newcore.orbps.models.web.vo.contractentry.renewalentry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 续保录入面向前端大Vo
 * @author wangyupeng
 *
 */
public class ReneEntryVo  implements Serializable{
	
	private static final long serialVersionUID = 1444454465L;
	
	/** 投保单信息 */
	private ReneApplInfoVo applInfoVo;
	/** 其他信息**/
	private ReneOtherInfoVo otherInfoVo ;
	/** 要约信息**/
	private List<ReneOfferInfoVo> offerInfoVo = new ArrayList<ReneOfferInfoVo>();
	/** 险种责任信息 **/
	private List<ResponseVo> responseVo = new ArrayList<ResponseVo>();

	/**
	 * @return the applInfoVo
	 */
	public ReneApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}

	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(ReneApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}

	/**
	 * @return the otherInfoVo
	 */
	public ReneOtherInfoVo getOtherInfoVo() {
		return otherInfoVo;
	}

	/**
	 * @param otherInfoVo the otherInfoVo to set
	 */
	public void setOtherInfoVo(ReneOtherInfoVo otherInfoVo) {
		this.otherInfoVo = otherInfoVo;
	}

	/**
	 * @return the offerInfoVo
	 */
	public List<ReneOfferInfoVo> getOfferInfoVo() {
		return offerInfoVo;
	}

	/**
	 * @param offerInfoVo the offerInfoVo to set
	 */
	public void setOfferInfoVo(List<ReneOfferInfoVo> offerInfoVo) {
		this.offerInfoVo = offerInfoVo;
	}

	/**
	 * @return the RenewalEntry
	 */
	public List<ResponseVo> getResponseVo() {
		return responseVo;
	}

	/**
	 * @param ReneEntryVo the RenewalEntry to set
	 */
	public void setResponseVo(List<ResponseVo> responseVo) {
		this.responseVo = responseVo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RenewalEntryForm [applInfoVo=" + applInfoVo + ", otherInfoVo=" + otherInfoVo + ", offerInfoVo="
				+ offerInfoVo + ", responseVo=" + responseVo + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((offerInfoVo == null) ? 0 : offerInfoVo.hashCode());
		result = prime * result + ((otherInfoVo == null) ? 0 : otherInfoVo.hashCode());
		result = prime * result + ((responseVo == null) ? 0 : responseVo.hashCode());
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
		ReneEntryVo other = (ReneEntryVo) obj;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (offerInfoVo == null) {
			if (other.offerInfoVo != null)
				return false;
		} else if (!offerInfoVo.equals(other.offerInfoVo))
			return false;
		if (otherInfoVo == null) {
			if (other.otherInfoVo != null)
				return false;
		} else if (!otherInfoVo.equals(other.otherInfoVo))
			return false;
		if (responseVo == null) {
			if (other.responseVo != null)
				return false;
		} else if (!responseVo.equals(other.responseVo))
			return false;
		return true;
	}
	
}
