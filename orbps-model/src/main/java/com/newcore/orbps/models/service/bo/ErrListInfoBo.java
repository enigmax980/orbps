package com.newcore.orbps.models.service.bo;

import java.io.Serializable;


/**
 * 清单导入详细错误信息
 * lijifei
 */
public class ErrListInfoBo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4803807858276280492L;
	//保单号
	private  String cgNo;
	//批次号
	private  String batchNo;
	//被保人序号
	private  String ipsnNo;
	//修改标志（初始为0）
	private  String modifyFlag;
	//错误序号
	private  String errSeq;
	//错误代码-非必填字段
	private  String errCode;
	//错误描述-非必填字段
	private  String errDesc;
	public ErrListInfoBo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrListInfoBo(String cgNo, String batchNo, String ipsnNo, String modifyFlag, String errSeq, String errCode,
			String errDesc) {
		super();
		this.cgNo = cgNo;
		this.batchNo = batchNo;
		this.ipsnNo = ipsnNo;
		this.modifyFlag = modifyFlag;
		this.errSeq = errSeq;
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(String ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	public String getModifyFlag() {
		return modifyFlag;
	}
	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}
	public String getErrSeq() {
		return errSeq;
	}
	public void setErrSeq(String errSeq) {
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





}
