package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

public class PolCode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3150327018720583817L;
	/**
	 * 险种
	 */
	private String polCode;
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
}
