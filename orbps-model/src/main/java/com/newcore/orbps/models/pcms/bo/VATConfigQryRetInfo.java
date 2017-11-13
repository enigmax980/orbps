package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
/**
 * 营改增配置信息查询服务 返回对象
 * @author xushichao
 * 2016年11月28日
 */
public class VATConfigQryRetInfo implements Serializable {

	private static final long serialVersionUID = -3837969741284537289L;
	
	private String retCode;
	private String errMsg;
	private String mioItenCode;
	private String mioTypeCode;
	private String fiMioItemCode;
	private String polCode;
	private Double rate;
	private String successFlag;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getMioItenCode() {
		return mioItenCode;
	}
	public void setMioItenCode(String mioItenCode) {
		this.mioItenCode = mioItenCode;
	}
	public String getMioTypeCode() {
		return mioTypeCode;
	}
	public void setMioTypeCode(String mioTypeCode) {
		this.mioTypeCode = mioTypeCode;
	}
	public String getFiMioItemCode() {
		return fiMioItemCode;
	}
	public void setFiMioItemCode(String fiMioItemCode) {
		this.fiMioItemCode = fiMioItemCode;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	@Override
	public String toString() {
		return "VATConfigQryRetInfo [retCode=" + retCode + ", errMsg=" + errMsg + ", mioItenCode=" + mioItenCode
				+ ", mioTypeCode=" + mioTypeCode + ", fiMioItemCode=" + fiMioItemCode + ", rate=" + rate
				+ ", successFlag=" + successFlag + "]";
	}
}
