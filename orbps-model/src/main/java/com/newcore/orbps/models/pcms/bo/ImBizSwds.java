package com.newcore.orbps.models.pcms.bo;

public class ImBizSwds implements java.io.Serializable {

	// Fields

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 6691022375058346477L;
	private Long tlstSeq;
	private Long taskSeq;
	private Short attachedFileType;
	private String sendWords;
	private Byte lineNo;
	
	private String dataSource;
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public Long getTlstSeq() {
		return tlstSeq;
	}
	public void setTlstSeq(Long tlstSeq) {
		this.tlstSeq = tlstSeq;
	}
	public Long getTaskSeq() {
		return taskSeq;
	}
	public void setTaskSeq(Long taskSeq) {
		this.taskSeq = taskSeq;
	}
	public Short getAttachedFileType() {
		return attachedFileType;
	}
	public void setAttachedFileType(Short attachedFileType) {
		this.attachedFileType = attachedFileType;
	}
	public String getSendWords() {
		return sendWords;
	}
	public void setSendWords(String sendWords) {
		this.sendWords = sendWords;
	}
	public Byte getLineNo() {
		return lineNo;
	}
	public void setLineNo(Byte lineNo) {
		this.lineNo = lineNo;
	}
	

}