package com.newcore.orbps.models.web.vo.contractentry.cardentry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 卡折录入前端Vo
 * @author jincong
 *
 */
public class CardEntryVo  implements Serializable{
	
	private static final long serialVersionUID = 1146020000L;
	/** 投保信息 */
	private ApplInfoVo applInfoVo;
	/** 被保人信息 */
	private IpsnInfoVo ipsnInfoVo;
	/** 身故受益人信息 */
	private List<BnfrInfoVo> bnfrInfoVos = new ArrayList<BnfrInfoVo>();
	/** 要约信息 */
	private List<BsInfoVo> bsInfoVos = new ArrayList<BsInfoVo>();
	/** 账户及保费信息 */
	private AccInfoVo accInfoVo;
	
	/**
	 * @return the applInfoVo
	 */
	public ApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}
	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(ApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}
	/**
	 * @return the ipsnInfoVo
	 */
	public IpsnInfoVo getIpsnInfoVo() {
		return ipsnInfoVo;
	}
	/**
	 * @param ipsnInfoVo the ipsnInfoVo to set
	 */
	public void setIpsnInfoVo(IpsnInfoVo ipsnInfoVo) {
		this.ipsnInfoVo = ipsnInfoVo;
	}
	/**
	 * @return the bnfrInfoVos
	 */
	public List<BnfrInfoVo> getBnfrInfoVos() {
		return bnfrInfoVos;
	}
	/**
	 * @param bnfrInfoVos the bnfrInfoVos to set
	 */
	public void setBnfrInfoVos(List<BnfrInfoVo> bnfrInfoVos) {
		this.bnfrInfoVos = bnfrInfoVos;
	}
	/**
	 * @return the bsInfoVo
	 */
	public List<BsInfoVo> getBsInfoVos() {
		return bsInfoVos;
	}
	/**
	 * @param bsInfoVos the bsInfoVos to set
	 */
	public void setBsInfoVos(List<BsInfoVo> bsInfoVos) {
		this.bsInfoVos = bsInfoVos;
	}
	/**
	 * @return the accInfoVo
	 */
	public AccInfoVo getAccInfoVo() {
		return accInfoVo;
	}
	/**
	 * @param accInfoVo the accInfoVo to set
	 */
	public void setAccInfoVo(AccInfoVo accInfoVo) {
		this.accInfoVo = accInfoVo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "cardEntryVo [applInfoVo=" + applInfoVo + ", ipsnInfoVo=" + ipsnInfoVo + ", bnfrInfoVo=" + bnfrInfoVos
				+ ", bsInfoVo=" + bsInfoVos + ", accInfoVo=" + accInfoVo + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accInfoVo == null) ? 0 : accInfoVo.hashCode());
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((bnfrInfoVos == null) ? 0 : bnfrInfoVos.hashCode());
		result = prime * result + ((bsInfoVos == null) ? 0 : bsInfoVos.hashCode());
		result = prime * result + ((ipsnInfoVo == null) ? 0 : ipsnInfoVo.hashCode());
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
		CardEntryVo other = (CardEntryVo) obj;
		if (accInfoVo == null) {
			if (other.accInfoVo != null)
				return false;
		} else if (!accInfoVo.equals(other.accInfoVo))
			return false;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (bnfrInfoVos == null) {
			if (other.bnfrInfoVos != null)
				return false;
		} else if (!bnfrInfoVos.equals(other.bnfrInfoVos))
			return false;
		if (bsInfoVos == null) {
			if (other.bsInfoVos != null)
				return false;
		} else if (!bsInfoVos.equals(other.bsInfoVos))
			return false;
		if (ipsnInfoVo == null) {
			if (other.ipsnInfoVo != null)
				return false;
		} else if (!ipsnInfoVo.equals(other.ipsnInfoVo))
			return false;
		return true;
	}
}
