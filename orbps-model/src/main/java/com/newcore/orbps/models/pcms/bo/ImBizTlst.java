package com.newcore.orbps.models.pcms.bo;

import java.sql.Date;

public class ImBizTlst implements java.io.Serializable {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -6820454259793533287L;
	private Long tlstSeq;
	private Long taskSeq;
	private Short attachedFileType;
	private Date sendDate;
	private Date prtTime;
	private String respWord;
	private Date plnRespTime;
	private Date respTime;
	
	private String dataSource;

	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public Long getTaskSeq() {
		return taskSeq;
	}
	public void setTaskSeq(Long taskSeq) {
		this.taskSeq = taskSeq;
	}
	public Long getTlstSeq() {
		return tlstSeq;
	}
	public void setTlstSeq(Long tlstSeq) {
		this.tlstSeq = tlstSeq;
	}
	public Short getAttachedFileType() {
		return attachedFileType;
	}
	public void setAttachedFileType(Short attachedFileType) {
		this.attachedFileType = attachedFileType;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Date getPrtTime() {
		return prtTime;
	}
	public void setPrtTime(Date prtTime) {
		this.prtTime = prtTime;
	}
	public String getRespWord() {
		return respWord;
	}
	public void setRespWord(String respWord) {
		this.respWord = respWord;
	}
	public Date getPlnRespTime() {
		return plnRespTime;
	}
	public void setPlnRespTime(Date plnRespTime) {
		this.plnRespTime = plnRespTime;
	}
	public Date getRespTime() {
		return respTime;
	}
	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}
	

}