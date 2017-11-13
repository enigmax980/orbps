package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 团体清单转合同错误日志表
 */
public class ErrBatchListBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1596167760282881557L;
	//组合保单号  长度25
	private String cgNo;
	//批次号
	private long batNo;
	//被保人序号
	private long ipsnNo;
	//错误序号
	private long errSeq;
	//错误代码 4
	private String errCode;
	//错误描述 225
	private String errDesc;
	//生成时间
	private Date createTime;
	public ErrBatchListBo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	public long getBatNo() {
		return batNo;
	}
	public void setBatNo(long batNo) {
		this.batNo = batNo;
	}
	public long getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	public long getErrSeq() {
		return errSeq;
	}
	public void setErrSeq(long errSeq) {
		this.errSeq = errSeq;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrDesc() {
		return errDesc;
	}
	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}






}
