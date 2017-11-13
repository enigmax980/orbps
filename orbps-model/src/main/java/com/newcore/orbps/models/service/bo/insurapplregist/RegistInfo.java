package com.newcore.orbps.models.service.bo.insurapplregist;

import java.io.Serializable;

import com.newcore.orbps.models.opertrace.TraceNode;

/**
 * @author wangxiao
 * 创建时间：2016年10月24日下午6:24:53
 */
public class RegistInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4032422228642759707L;
	
	private InsurApplRegist insurApplRegist;
	
	private TraceNode traceNode;

	/**
	 * @return the insurApplRegist
	 */
	public InsurApplRegist getInsurApplRegist() {
		return insurApplRegist;
	}

	/**
	 * @param insurApplRegist the insurApplRegist to set
	 */
	public void setInsurApplRegist(InsurApplRegist insurApplRegist) {
		this.insurApplRegist = insurApplRegist;
	}

	/**
	 * @return the traceNode
	 */
	public TraceNode getTraceNode() {
		return traceNode;
	}

	/**
	 * @param traceNode the traceNode to set
	 */
	public void setTraceNode(TraceNode traceNode) {
		this.traceNode = traceNode;
	}

}
