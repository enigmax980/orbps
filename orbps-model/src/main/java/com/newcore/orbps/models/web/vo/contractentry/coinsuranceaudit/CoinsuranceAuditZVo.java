package com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 共保审批面向前端大Vo
 * @author wangyanjie
 *
 */
public class CoinsuranceAuditZVo  implements Serializable{
	
	private static final long serialVersionUID = 1434454455L;
	
	/** 共保审批基本信息 */
	private CoinsuranceInfoVo coinsuranceInfoVo;
	/** 共保审批险种信息 */
	private List<CoinsuranceInsuranceInfoVo> coinsuranceInsuranceInfoVos= new ArrayList<CoinsuranceInsuranceInfoVo>() ;
	/** 共保审批公司信息 */
	private List<CoInsurComInFoVo> coInsurComVos = new ArrayList<CoInsurComInFoVo>();
	/** 协议约定 */
	private String appAddrCountry;
    /** 审核结果 */
    private String auditFindings;
        /** 备注 */
    private String remark;
	/**
	 * @return the coinsuranceInfoVo
	 */
	public CoinsuranceInfoVo getCoinsuranceInfoVo() {
		return coinsuranceInfoVo;
	}
	/**
	 * @param coinsuranceInfoVo the coinsuranceInfoVo to set
	 */
	public void setCoinsuranceInfoVo(CoinsuranceInfoVo coinsuranceInfoVo) {
		this.coinsuranceInfoVo = coinsuranceInfoVo;
	}
	/**
	 * @return the coinsuranceInsuranceInfoVos
	 */
	public List<CoinsuranceInsuranceInfoVo> getCoinsuranceInsuranceInfoVos() {
		return coinsuranceInsuranceInfoVos;
	}
	/**
	 * @param coinsuranceInsuranceInfoVos the coinsuranceInsuranceInfoVos to set
	 */
	public void setCoinsuranceInsuranceInfoVos(List<CoinsuranceInsuranceInfoVo> coinsuranceInsuranceInfoVos) {
		this.coinsuranceInsuranceInfoVos = coinsuranceInsuranceInfoVos;
	}
	/**
	 * @return the coInsurComVos
	 */
	public List<CoInsurComInFoVo> getCoInsurComVos() {
		return coInsurComVos;
	}
	/**
	 * @param coInsurComVos the coInsurComVos to set
	 */
	public void setCoInsurComVos(List<CoInsurComInFoVo> coInsurComVos) {
		this.coInsurComVos = coInsurComVos;
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
	 * @return the auditFindings
	 */
	public String getAuditFindings() {
		return auditFindings;
	}
	/**
	 * @param auditFindings the auditFindings to set
	 */
	public void setAuditFindings(String auditFindings) {
		this.auditFindings = auditFindings;
	}
	
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoinsuranceAuditZVo [coinsuranceInfoVo=" + coinsuranceInfoVo + ", coinsuranceInsuranceInfoVos="
				+ coinsuranceInsuranceInfoVos + ", coInsurComVos=" + coInsurComVos + ", appAddrCountry="
				+ appAddrCountry + ", auditFindings=" + auditFindings + ", remark=" + remark + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appAddrCountry == null) ? 0 : appAddrCountry.hashCode());
		result = prime * result + ((auditFindings == null) ? 0 : auditFindings.hashCode());
		result = prime * result + ((coInsurComVos == null) ? 0 : coInsurComVos.hashCode());
		result = prime * result + ((coinsuranceInfoVo == null) ? 0 : coinsuranceInfoVo.hashCode());
		result = prime * result + ((coinsuranceInsuranceInfoVos == null) ? 0 : coinsuranceInsuranceInfoVos.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
		CoinsuranceAuditZVo other = (CoinsuranceAuditZVo) obj;
		if (appAddrCountry == null) {
			if (other.appAddrCountry != null)
				return false;
		} else if (!appAddrCountry.equals(other.appAddrCountry))
			return false;
		if (auditFindings == null) {
			if (other.auditFindings != null)
				return false;
		} else if (!auditFindings.equals(other.auditFindings))
			return false;
		if (coInsurComVos == null) {
			if (other.coInsurComVos != null)
				return false;
		} else if (!coInsurComVos.equals(other.coInsurComVos))
			return false;
		if (coinsuranceInfoVo == null) {
			if (other.coinsuranceInfoVo != null)
				return false;
		} else if (!coinsuranceInfoVo.equals(other.coinsuranceInfoVo))
			return false;
		if (coinsuranceInsuranceInfoVos == null) {
			if (other.coinsuranceInsuranceInfoVos != null)
				return false;
		} else if (!coinsuranceInsuranceInfoVos.equals(other.coinsuranceInsuranceInfoVos))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		return true;
	}
}
