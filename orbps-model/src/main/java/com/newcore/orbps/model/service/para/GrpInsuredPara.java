package com.newcore.orbps.model.service.para;

import java.io.Serializable;

import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

public class GrpInsuredPara implements Serializable{
	
	
	private static final long serialVersionUID = 924899257016412005L;
	
	GrpInsured grpInsured;
	TraceNode traceNode;
	/**
	 * @return the grpInsured
	 */
	public GrpInsured getGrpInsured() {
		return grpInsured;
	}
	/**
	 * @param grpInsured the grpInsured to set
	 */
	public void setGrpInsured(GrpInsured grpInsured) {
		this.grpInsured = grpInsured;
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
