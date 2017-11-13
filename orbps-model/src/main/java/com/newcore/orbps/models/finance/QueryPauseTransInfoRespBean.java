package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 暂停送划相关查询返回bo
 * @author LiSK
 *2017-02-22
 */
@SuppressWarnings(value = "serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RESPONSE")
public class QueryPauseTransInfoRespBean implements Serializable {
	/**
	 * 返回条数
	 */
	@XmlElement(name = "NB_NUM")
	long nbNum;
	/**
	 * 暂停送划原因
	 */
	@XmlElement(name = "REASON")
	String reason;
	/**
	 * 不可送划日期
	 */
	@XmlElement(name = "STOP_TRANS_DATE")
	String stopTransDate;
	/**
	 * 恢复送划日期
	 */
	@XmlElement(name = "RE_TRANS_DATE")
	String reTransDate;
	/**
	 * 暂停送划明细数据
	 */
	@XmlElement(name = "BANK_NOT2TRANS_SCXE")
	List<PauseTransData> pauseTransDataList;
	
	
	public long getNbNum() {
		return nbNum;
	}
	public void setNbNum(long nbNum) {
		this.nbNum = nbNum;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStopTransDate() {
		return stopTransDate;
	}
	public void setStopTransDate(String stopTransDate) {
		this.stopTransDate = stopTransDate;
	}
	public String getReTransDate() {
		return reTransDate;
	}
	public void setReTransDate(String reTransDate) {
		this.reTransDate = reTransDate;
	}
	public List<PauseTransData> getPauseTransDataList() {
		return pauseTransDataList;
	}
	public void setPauseTransDataList(List<PauseTransData> pauseTransDataList) {
		this.pauseTransDataList = pauseTransDataList;
	}
	
	
}
