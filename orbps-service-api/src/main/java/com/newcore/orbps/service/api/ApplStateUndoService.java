package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;


/**
 * 要约撤销
 * @author wangxiao
 * 创建时间：2016年9月24日上午10:36:28
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface ApplStateUndoService {
	@POST
    @Path("/undo")
	public RetInfo undo(TraceNodePra traceNodePra);
}
