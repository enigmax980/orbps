package com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 共保新增录入面向前端大Vo 
 * @author wangyanjie
 *
 */
public class CoInsurAgreementEntryVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000001L;
	/** 共保协议信息 */
	private CoInsurAgreementInfVo agreementInfVo;
	/** 客户信息 */
	private CoInsurCustomerVo customerVo;
	/** 协议约定 */
	private String appAddrCountry;
	/** 共保公司信息 */
	private List<CoInsurComVo> insuranceComVos = new ArrayList<CoInsurComVo>();
	/** 险种信息 */
	private List<CoInsurVo> insuranceVos = new ArrayList<CoInsurVo>();
	/**
	 * @return the agreementInfVo
	 */
	public CoInsurAgreementInfVo getAgreementInfVo() {
		return agreementInfVo;
	}
	/**
	 * @param agreementInfVo the agreementInfVo to set
	 */
	public void setAgreementInfVo(CoInsurAgreementInfVo agreementInfVo) {
		this.agreementInfVo = agreementInfVo;
	}
	/**
	 * @return the customerVo
	 */
	public CoInsurCustomerVo getCustomerVo() {
		return customerVo;
	}
	/**
	 * @param customerVo the customerVo to set
	 */
	public void setCustomerVo(CoInsurCustomerVo customerVo) {
		this.customerVo = customerVo;
	}
	/**
	 * @return the appAddrCountry
	 */
	public String getAppAddrCountry() {
		return appAddrCountry;
	}
	/**
	 * @param appAddrCountry the appAddrCountry to set
	 */
	public void setAppAddrCountry(String appAddrCountry) {
		this.appAddrCountry = appAddrCountry;
	}
	/**
	 * @return the insuranceComVos
	 */
	public List<CoInsurComVo> getInsuranceComVos() {
		return insuranceComVos;
	}
	/**
	 * @param insuranceComVos the insuranceComVos to set
	 */
	public void setInsuranceComVos(List<CoInsurComVo> insuranceComVos) {
		this.insuranceComVos = insuranceComVos;
	}
	/**
	 * @return the insuranceVos
	 */
	public List<CoInsurVo> getInsuranceVos() {
		return insuranceVos;
	}
	/**
	 * @param insuranceVos the insuranceVos to set
	 */
	public void setInsuranceVos(List<CoInsurVo> insuranceVos) {
		this.insuranceVos = insuranceVos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoInsurAgreementEntryVo [agreementInfVo=" + agreementInfVo + ", customerVo=" + customerVo
				+ ", appAddrCountry=" + appAddrCountry + ", insuranceComVos=" + insuranceComVos + ", insuranceVos="
				+ insuranceVos + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agreementInfVo == null) ? 0 : agreementInfVo.hashCode());
		result = prime * result + ((appAddrCountry == null) ? 0 : appAddrCountry.hashCode());
		result = prime * result + ((customerVo == null) ? 0 : customerVo.hashCode());
		result = prime * result + ((insuranceComVos == null) ? 0 : insuranceComVos.hashCode());
		result = prime * result + ((insuranceVos == null) ? 0 : insuranceVos.hashCode());
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
		CoInsurAgreementEntryVo other = (CoInsurAgreementEntryVo) obj;
		if (agreementInfVo == null) {
			if (other.agreementInfVo != null)
				return false;
		} else if (!agreementInfVo.equals(other.agreementInfVo))
			return false;
		if (appAddrCountry == null) {
			if (other.appAddrCountry != null)
				return false;
		} else if (!appAddrCountry.equals(other.appAddrCountry))
			return false;
		if (customerVo == null) {
			if (other.customerVo != null)
				return false;
		} else if (!customerVo.equals(other.customerVo))
			return false;
		if (insuranceComVos == null) {
			if (other.insuranceComVos != null)
				return false;
		} else if (!insuranceComVos.equals(other.insuranceComVos))
			return false;
		if (insuranceVos == null) {
			if (other.insuranceVos != null)
				return false;
		} else if (!insuranceVos.equals(other.insuranceVos))
			return false;
		return true;
	}
}
