package com.newcore.orbps.models.custaccount;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangxiao
 * 创建时间：2016年9月3日下午3:47:53
 */
public class CustNoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6875188381190511499L;
	//投保单号
	private String applNo;
	//被保人编号
	private Long ipsnNo;
	//被保人客户号
	private String ipsnCustNo;
	//投保人客户号
	private String hldrCustNo;
	//受益人信息
	private List<BnfrCustNoVo> bnfrCustNoVos;
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the ipsnNo
	 */
	public Long getIpsnNo() {
		return ipsnNo;
	}
	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(Long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	/**
	 * @return the ipsnCustNo
	 */
	public String getIpsnCustNo() {
		return ipsnCustNo;
	}
	/**
	 * @param ipsnCustNo the ipsnCustNo to set
	 */
	public void setIpsnCustNo(String ipsnCustNo) {
		this.ipsnCustNo = ipsnCustNo;
	}
	/**
	 * @return the hldrCustNo
	 */
	public String getHldrCustNo() {
		return hldrCustNo;
	}
	/**
	 * @param hldrCustNo the hldrCustNo to set
	 */
	public void setHldrCustNo(String hldrCustNo) {
		this.hldrCustNo = hldrCustNo;
	}
	/**
	 * @return the bnfrCustNoVos
	 */
	public List<BnfrCustNoVo> getBnfrCustNoVos() {
		return bnfrCustNoVos;
	}
	/**
	 * @param bnfrCustNoVos the bnfrCustNoVos to set
	 */
	public void setBnfrCustNoVos(List<BnfrCustNoVo> bnfrCustNoVos) {
		this.bnfrCustNoVos = bnfrCustNoVos;
	}
	
}
