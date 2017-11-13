package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * FTP传输接口返回参数
 * @author jcc
 * 2016-08-16 14:23:41
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESPONSE")
public class ProcScRes implements Serializable {
	@XmlElement(name="SERIALNUM")
	private String SERIALNUM;
	
	@XmlElement(name="PUB_INFO")
	private PubInfo pubInfo;
	
	@XmlElement(name="STOP_TASK_OUT")
	private StopTaskOut stopTaskOut;
	
	public StopTaskOut getStopTaskOut() {
		return stopTaskOut;
	}
	public void setStopTaskOut(StopTaskOut stopTaskOut) {
		this.stopTaskOut = stopTaskOut;
	}
	public String getSERIALNUM() {
		return SERIALNUM;
	}
	public void setSERIALNUM(String sERIALNUM) {
		SERIALNUM = sERIALNUM;
	}
	public PubInfo getPubInfo() {
		return pubInfo;
	}
	public void setPubInfo(PubInfo pubInfo) {
		this.pubInfo = pubInfo;
	}
}
