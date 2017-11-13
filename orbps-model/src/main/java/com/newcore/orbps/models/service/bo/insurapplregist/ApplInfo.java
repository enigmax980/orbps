package com.newcore.orbps.models.service.bo.insurapplregist;

import java.io.Serializable;

/**
 * 投保资料
 * @author wangxiao
 * 创建时间：2016年10月24日下午1:33:46
 */
public class ApplInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2442152827654208608L;
	//投保资料类型,长度：4,如果非空,不能重复
	private String applInfoType;
	//投保资料数量
	private Integer applInfoNumber;
	/**
	 * @return the applInfoType
	 */
	public String getApplInfoType() {
		return applInfoType;
	}
	/**
	 * @param applInfoType the applInfoType to set
	 */
	public void setApplInfoType(String applInfoType) {
		this.applInfoType = applInfoType;
	}
	/**
	 * @return the applInfoNumber
	 */
	public Integer getApplInfoNumber() {
		return applInfoNumber;
	}
	/**
	 * @param applInfoNumber the applInfoNumber to set
	 */
	public void setApplInfoNumber(Integer applInfoNumber) {
		this.applInfoNumber = applInfoNumber;
	}
	
}
