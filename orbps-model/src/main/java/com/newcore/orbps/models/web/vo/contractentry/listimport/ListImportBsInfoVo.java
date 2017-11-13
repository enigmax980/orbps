package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.List;
/**
 * 要约信息Vo
 * @author jincong
 *
 */
public class ListImportBsInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080001L;
	
	/** 险种代码 */
	private String polCode;
	/** 险种名称 */
	private String polName;
	/** 责任信息 */
	private List<ListImportSubPolVo> listImportSubPolVos;
	/**
	 * @return the polCode
	 */
	public String getPolCode() {
		return polCode;
	}
	/**
	 * @param polCode the polCode to set
	 */
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	/**
	 * @return the polName
	 */
	public String getPolName() {
		return polName;
	}
	/**
	 * @param polName the polName to set
	 */
	public void setPolName(String polName) {
		this.polName = polName;
	}
	/**
	 * @return the listImportSubPolVos
	 */
	public List<ListImportSubPolVo> getListImportSubPolVos() {
		return listImportSubPolVos;
	}
	/**
	 * @param listImportSubPolVos the listImportSubPolVos to set
	 */
	public void setListImportSubPolVos(List<ListImportSubPolVo> listImportSubPolVos) {
		this.listImportSubPolVos = listImportSubPolVos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListImportBsInfoVo [polCode=" + polCode + ", polName=" + polName + ", listImportSubPolVos="
				+ listImportSubPolVos + "]";
	}
	
	
}
