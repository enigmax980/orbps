package com.newcore.orbps.model.service.para;

import java.io.Serializable;

import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

public class GrpInsurApplPara implements Serializable{

	private static final long serialVersionUID = -3998265936508097642L;
	
	GrpInsurAppl grpInsurAppl;
	String taskId;
	TraceNode traceNode;
	
	public GrpInsurAppl getGrpInsurAppl() {
		return grpInsurAppl;
	}
	public void setGrpInsurAppl(GrpInsurAppl grpInsurAppl) {
		this.grpInsurAppl = grpInsurAppl;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public TraceNode getTraceNode() {
		return traceNode;
	}
	public void setTraceNode(TraceNode traceNode) {
		this.traceNode = traceNode;
	}
	

}
