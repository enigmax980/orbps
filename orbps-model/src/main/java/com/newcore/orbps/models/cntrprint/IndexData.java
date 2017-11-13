package com.newcore.orbps.models.cntrprint;

/**
 * @author chenyc
 *
 */
public class IndexData {
	private static final String EMPTY = "";
	private String indexno = EMPTY;
	private String clerknote = EMPTY;
	private String applno = EMPTY;
	private String cgno = EMPTY;
	private String prdname = EMPTY;
	private String prdcode = EMPTY;

	public String getIndexno() {
		return indexno;
	}

	public void setIndexno(String indexno) {
		this.indexno = indexno;
	}

	public String getClerknote() {
		return clerknote;
	}

	public void setClerknote(String clerknote) {
		this.clerknote = clerknote;
	}

	public String getApplno() {
		return applno;
	}

	public void setApplno(String applno) {
		this.applno = applno;
	}

	public String getCgno() {
		return cgno;
	}

	public void setCgno(String cgno) {
		this.cgno = cgno;
	}

	public String getPrdname() {
		return prdname;
	}

	public void setPrdname(String prdname) {
		this.prdname = prdname;
	}

	public String getPrdcode() {
		return prdcode;
	}

	public void setPrdcode(String prdcode) {
		this.prdcode = prdcode;
	}

}
