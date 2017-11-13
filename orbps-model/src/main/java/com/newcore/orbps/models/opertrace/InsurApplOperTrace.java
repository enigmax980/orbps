package com.newcore.orbps.models.opertrace;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;



public class InsurApplOperTrace implements Serializable{

	private static final long serialVersionUID = 4477149908021557359L;
	
	private String applNo;
	
	private ArrayDeque<TraceNode> operTraceDeque;
	
	private ArrayDeque<TraceNode> imgApprovalTraceDeque;
	/**
	 * 
	 */
	public InsurApplOperTrace() {
		super();
		operTraceDeque = new ArrayDeque<TraceNode>();
		imgApprovalTraceDeque = new ArrayDeque<TraceNode>();
	}

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
	 * @return the operTraceDeque
	 */
	public Deque<TraceNode> getOperTraceDeque() {
		return operTraceDeque;
	}

	/**
	 * @param operTraceDeque the operTraceDeque to set
	 */
	public void setOperTraceDeque(ArrayDeque<TraceNode> operTraceDeque) {
		this.operTraceDeque = operTraceDeque;
	}

	/**
	 * @param operTraceStack the operTraceStack to set
	 */
	public void setOperTraceStack(ArrayDeque<TraceNode> operTraceStack) {
		this.operTraceDeque = operTraceStack;
	}
	
	public void pushTraceNode(TraceNode traceNode) {
		operTraceDeque.push(traceNode);
	}
	
	public TraceNode getCurrentTraceNode() {		
		return operTraceDeque.peek();
	}

	/**
	 * @return the imgApprovalTraceDeque
	 */
	public Deque<TraceNode> getImgApprovalTraceDeque() {
		return imgApprovalTraceDeque;
	}

	/**
	 * @param imgApprovalTraceDeque the imgApprovalTraceDeque to set
	 */
	public void setImgApprovalTraceDeque(ArrayDeque<TraceNode> imgApprovalTraceDeque) {
		this.imgApprovalTraceDeque = imgApprovalTraceDeque;
	}

	public void pushImgApprovalTrace(TraceNode traceNode) {
		imgApprovalTraceDeque.push(traceNode);
	}
	
	public TraceNode getCurrentImgApprovalTrace() {		
		return imgApprovalTraceDeque.peek();
	}

}
