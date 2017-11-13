package com.newcore.orbps.models.web.vo.contractentry.frequencyeffect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 频次生效界面Vo
 * @author jincong
 *
 */
public class FrequencyEfVo  implements Serializable{
	
	private static final long serialVersionUID = 1146040003L;

	/** 界面录入信息 */
	private List<FrequencyEfEntryVo> applId = new ArrayList<FrequencyEfEntryVo>();
	
	/** 险种责任信息 */
	private List<ResponseVo> productCode = new ArrayList<ResponseVo>();

	/**
	 * @return the applId
	 */
	public List<FrequencyEfEntryVo> getApplId() {
		return applId;
	}

	/**
	 * @param applId the applId to set
	 */
	public void setApplId(List<FrequencyEfEntryVo> applId) {
		this.applId = applId;
	}

	/**
	 * @return the productCode
	 */
	public List<ResponseVo> getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(List<ResponseVo> productCode) {
		this.productCode = productCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FrequencyEffectVo [applId=" + applId + ", productCode=" + productCode + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applId == null) ? 0 : applId.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
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
		FrequencyEfVo other = (FrequencyEfVo) obj;
		if (applId == null) {
			if (other.applId != null)
				return false;
		} else if (!applId.equals(other.applId))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		return true;
	}
	
}
