package com.newcore.orbps.models.cntrprint;

/**
 * @author chenyc
 *
 */
public class RegInfoData {
	private static final String EMPTY = "";
	private String itfid = EMPTY;
	private String tplcode = EMPTY;
	private String tplversion = EMPTY;
	private String paramtype = EMPTY;

	public String getItfid() {
		return itfid;
	}

	public void setItfid(String itfid) {
		this.itfid = itfid;
	}

	public String getTplcode() {
		return tplcode;
	}

	public void setTplcode(String tplcode) {
		this.tplcode = tplcode;
	}

	public String getTplversion() {
		return tplversion;
	}

	public void setTplversion(String tplversion) {
		this.tplversion = tplversion;
	}

	public String getParamtype() {
		return paramtype;
	}

	public void setParamtype(String paramtype) {
		this.paramtype = paramtype;
	}

}
