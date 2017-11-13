package com.newcore.orbps.models.custaccount;

import java.io.Serializable;

/**
 * @author wangxiao
 * 创建时间：2016年9月3日下午3:59:12
 */
public class BnfrCustNoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -720705408131896295L;
	//受益人顺序
	private Long bnfrLevel;
	//受益人cmds客户号
	private String bnfrPartyId;
	//受益人客户号
	private String bnfrCustNo;
	
	/**
	 * @return the bnfrLevel
	 */
	public Long getBnfrLevel() {
		return bnfrLevel;
	}
	/**
	 * @param bnfrLevel the bnfrLevel to set
	 */
	public void setBnfrLevel(Long bnfrLevel) {
		this.bnfrLevel = bnfrLevel;
	}
	
	/**
	 * @return the bnfrPartyId
	 */
	public String getBnfrPartyId() {
		return bnfrPartyId;
	}
	/**
	 * @param bnfrPartyId the bnfrPartyId to set
	 */
	public void setBnfrPartyId(String bnfrPartyId) {
		this.bnfrPartyId = bnfrPartyId;
	}
	/**
	 * @return the bnfrCustNo
	 */
	public String getBnfrCustNo() {
		return bnfrCustNo;
	}
	/**
	 * @param bnfrCustNo the bnfrCustNo to set
	 */
	public void setBnfrCustNo(String bnfrCustNo) {
		this.bnfrCustNo = bnfrCustNo;
	}
	
}
