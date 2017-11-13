package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 机构审查Vo
 * @author xiaoYe
 *
 */
public class CheckQueryVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544571L;
	
	/** 投保单号 */
	private String applNo;
	/** 生效起始日期 */
	private Date bgForceDate;
	/** 生效结束日期 */
	private Date edForceDate;
	/** 新单状态 */
	private String newApplState;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构号 */
	private String salesBranchNo;
	/** 销售员代码 */
	private String salesNo;
	
	private List<MgrBranchListVo> mgrBranchListVos = new ArrayList<MgrBranchListVo>();

	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}

	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	/**
	 * @return the bgForceDate
	 */
	public Date getBgForceDate() {
		return bgForceDate;
	}

	/**
	 * @param bgForceDate the bgForceDate to set
	 */
	public void setBgForceDate(Date bgForceDate) {
		this.bgForceDate = bgForceDate;
	}

	/**
	 * @return the edForceDate
	 */
	public Date getEdForceDate() {
		return edForceDate;
	}

	/**
	 * @param edForceDate the edForceDate to set
	 */
	public void setEdForceDate(Date edForceDate) {
		this.edForceDate = edForceDate;
	}

	/**
	 * @return the newApplState
	 */
	public String getNewApplState() {
		return newApplState;
	}

	/**
	 * @param newApplState the newApplState to set
	 */
	public void setNewApplState(String newApplState) {
		this.newApplState = newApplState;
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}

	/**
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}

	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	/**
	 * @return the mgrBranchListVos
	 */
	public List<MgrBranchListVo> getMgrBranchListVos() {
		return mgrBranchListVos;
	}

	/**
	 * @param mgrBranchListVos the mgrBranchListVos to set
	 */
	public void setMgrBranchListVos(List<MgrBranchListVo> mgrBranchListVos) {
		this.mgrBranchListVos = mgrBranchListVos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CheckQueryVo [applNo=" + applNo + ", bgForceDate=" + bgForceDate + ", edForceDate=" + edForceDate
				+ ", newApplState=" + newApplState + ", salesChannel=" + salesChannel + ", salesBranchNo="
				+ salesBranchNo + ", salesNo=" + salesNo + ", mgrBranchListVos=" + mgrBranchListVos + "]";
	}


	
}
