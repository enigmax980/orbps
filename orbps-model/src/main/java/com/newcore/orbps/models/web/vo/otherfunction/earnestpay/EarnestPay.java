package com.newcore.orbps.models.web.vo.otherfunction.earnestpay;

import java.io.Serializable;
import java.util.List;
/**
 * 暂交费支取vo对象 
 * @author jincong 
 *
 */

public class EarnestPay  implements Serializable{
	
	private static final long serialVersionUID = 13334560000012L;
	
	/** 查询信息 */
	private QueryInfoVo queryInfoVo;
	/** 账户信息 */
	private List<AccInfoVo> accInfoVos;
	/** 账户总余额 */
	private Double sumAmount;
	/** 是否全部支取 */
	private String payAllFlag;
	/**
	 * @return the queryInfoVo
	 */
	public QueryInfoVo getQueryInfoVo() {
		return queryInfoVo;
	}
	/**
	 * @param queryInfoVo the queryInfoVo to set
	 */
	public void setQueryInfoVo(QueryInfoVo queryInfoVo) {
		this.queryInfoVo = queryInfoVo;
	}
	/**
	 * @return the accInfoVos
	 */
	public List<AccInfoVo> getAccInfoVos() {
		return accInfoVos;
	}
	/**
	 * @param accInfoVos the accInfoVos to set
	 */
	public void setAccInfoVos(List<AccInfoVo> accInfoVos) {
		this.accInfoVos = accInfoVos;
	}
	/**
	 * @return the sumAmount
	 */
	public Double getSumAmount() {
		return sumAmount;
	}
	/**
	 * @param sumAmount the sumAmount to set
	 */
	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}
	/**
	 * @return the payAllFlag
	 */
	public String getPayAllFlag() {
		return payAllFlag;
	}
	/**
	 * @param payAllFlag the payAllFlag to set
	 */
	public void setPayAllFlag(String payAllFlag) {
		this.payAllFlag = payAllFlag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EarnestPay [queryInfoVo=" + queryInfoVo + ", accInfoVos=" + accInfoVos + ", sumAmount=" + sumAmount
				+ ", payAllFlag=" + payAllFlag + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accInfoVos == null) ? 0 : accInfoVos.hashCode());
		result = prime * result + ((payAllFlag == null) ? 0 : payAllFlag.hashCode());
		result = prime * result + ((queryInfoVo == null) ? 0 : queryInfoVo.hashCode());
		result = prime * result + ((sumAmount == null) ? 0 : sumAmount.hashCode());
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
		EarnestPay other = (EarnestPay) obj;
		if (accInfoVos == null) {
			if (other.accInfoVos != null)
				return false;
		} else if (!accInfoVos.equals(other.accInfoVos))
			return false;
		if (payAllFlag == null) {
			if (other.payAllFlag != null)
				return false;
		} else if (!payAllFlag.equals(other.payAllFlag))
			return false;
		if (queryInfoVo == null) {
			if (other.queryInfoVo != null)
				return false;
		} else if (!queryInfoVo.equals(other.queryInfoVo))
			return false;
		if (sumAmount == null) {
			if (other.sumAmount != null)
				return false;
		} else if (!sumAmount.equals(other.sumAmount))
			return false;
		return true;
	}
}
