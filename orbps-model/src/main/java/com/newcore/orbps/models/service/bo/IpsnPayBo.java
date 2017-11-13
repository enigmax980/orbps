package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

public class IpsnPayBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1052801208497293036L;
	/*字段名：投保单号，长度：16*/
	private String applNo;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public IpsnPayBo() {
		super();
		// TODO Auto-generated constructor stub
	}


}
