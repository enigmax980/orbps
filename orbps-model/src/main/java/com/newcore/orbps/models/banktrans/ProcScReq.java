package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 传输接口入参
 * @author jcc
 * 2016-08-16 14:23:41
 */
@SuppressWarnings("serial")
@XmlType(name = "REQUEST")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcScReq implements Serializable{
	
	@XmlElement(name="SERIALNUM")
	private String serialnum;
	
	@XmlElement(name="STOP_TASK_IN")
	private StopTaskIn stopTaskIn;
	
	@XmlElement(name="IO_MIOS_GEN_BANK_TRANS_SCXE")
	private BuildBanTransBean buildBanTransBean;

	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}	

	public StopTaskIn getStopTaskIn() {
		return stopTaskIn;
	}

	public void setStopTaskIn(StopTaskIn stopTaskIn) {
		this.stopTaskIn = stopTaskIn;
	}

	public BuildBanTransBean getBuildBanTransBean() {
		return buildBanTransBean;
	}

	public void setBuildBanTransBean(BuildBanTransBean buildBanTransBean) {
		this.buildBanTransBean = buildBanTransBean;
	}
	
}
