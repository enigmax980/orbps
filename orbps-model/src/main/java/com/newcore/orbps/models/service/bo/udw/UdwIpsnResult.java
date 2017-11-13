package com.newcore.orbps.models.service.bo.udw;

import java.io.Serializable;
import java.util.List;

/**
 * @author huanglong
 * @date 2016年9月5日
 * 内容:被保人核保结论
 */
public class UdwIpsnResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9080676252443329905L;

	/*字段名称:被保人序号,长度:,是否必填:否*/
	private long ipsnNo;
	/*字段名称:被保人险种核保结论列表,长度:,是否必填:否*/
	private List<UdwIpsnPolResult> udwIpsnPolResults;
	
	public UdwIpsnResult() {
		super();
	}
	/**
	 * @return the ipsnNo
	 */
	public long getIpsnNo() {
		return ipsnNo;
	}
	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}

	/**
	 * @return the udwIpsnPolResults
	 */
	public List<UdwIpsnPolResult> getUdwIpsnPolResults() {
		return udwIpsnPolResults;
	}

	/**
	 * @param udwIpsnPolResults the udwIpsnPolResults to set
	 */
	public void setUdwIpsnPolResults(List<UdwIpsnPolResult> udwIpsnPolResults) {
		this.udwIpsnPolResults = udwIpsnPolResults;
	}
	
}
