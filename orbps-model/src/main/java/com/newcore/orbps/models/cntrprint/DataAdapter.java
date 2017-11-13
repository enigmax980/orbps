package com.newcore.orbps.models.cntrprint;

import java.io.Serializable;
import java.util.Date;

public class DataAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9050060528786990065L;

	private String applNo;
	private String cgNo;
	private Date updateTimestamp;
	private PmsDataInfo pmsDataInfo;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public PmsDataInfo getPmsDataInfo() {
		return pmsDataInfo;
	}

	public void setPmsDataInfo(PmsDataInfo pmsDataInfo) {
		this.pmsDataInfo = pmsDataInfo;
	}
	
	@Override
	public String toString() {
		return "DataAdapter [applNo=" + applNo + ", cgNo=" + cgNo + ", updateTimestamp=" + updateTimestamp
				+ ", pmsDataInfo=" + pmsDataInfo + "]";
	}

	
}
