package com.newcore.orbps.models.web.vo.udw;

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
	private static final long serialVersionUID = -7119250457579742300L;
	/*
	 * 被保人序号
	 */
	private long ipsnNo;
	/*
	 * 被保人险种核保结论列表
	 */
	private List<UdwIpsnPolResult> udwIpsnPolResults;
	
	
	public UdwIpsnResult() {
		super();
	}

	public long getIpsnNo() {
		return ipsnNo;
	}
	public void setIpsnNo(long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	public List<UdwIpsnPolResult> getUdwIpsnResults() {
		return udwIpsnPolResults;
	}
	public void setUdwIpsnResults(List<UdwIpsnPolResult> udwIpsnPolResults) {
		this.udwIpsnPolResults = udwIpsnPolResults;
	}
	
	
}
