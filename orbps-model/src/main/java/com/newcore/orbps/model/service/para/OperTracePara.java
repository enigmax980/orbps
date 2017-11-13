package com.newcore.orbps.model.service.para;

import java.io.Serializable;
import java.util.Deque;

import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * @author wangxiao
 * 创建时间：2016年11月2日下午4:59:40
 */
public class OperTracePara implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6486472628349396844L;
	private GrpInsurAppl grpInsurAppl;
	private Deque<TraceNode> operTraceStack;
	/**
	 * @return the grpInsurAppl
	 */
	public GrpInsurAppl getGrpInsurAppl() {
		return grpInsurAppl;
	}
	/**
	 * @param grpInsurAppl the grpInsurAppl to set
	 */
	public void setGrpInsurAppl(GrpInsurAppl grpInsurAppl) {
		this.grpInsurAppl = grpInsurAppl;
	}
	/**
	 * @return the operTraceStack
	 */
	public Deque<TraceNode> getOperTraceStack() {
		return operTraceStack;
	}
	/**
	 * @param operTraceStack the operTraceStack to set
	 */
	public void setOperTraceStack(Deque<TraceNode> operTraceStack) {
		this.operTraceStack = operTraceStack;
	}
	
}
