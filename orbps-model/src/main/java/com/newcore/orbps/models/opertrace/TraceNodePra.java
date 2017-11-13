package com.newcore.orbps.models.opertrace;

import java.io.Serializable;

/**
 * @author wangxiao
 * 创建时间：2016年11月2日上午9:43:20
 */
public class TraceNodePra implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1213684227755580223L;

	private String applNo;
	
	private TraceNode traceNode;
	
	private String taskId;
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
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
