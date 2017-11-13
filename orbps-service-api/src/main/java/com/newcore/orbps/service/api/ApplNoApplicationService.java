package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface ApplNoApplicationService {
	/**
	 * 获取投保单号
	 * 
	 * @param mgrBranchNo
	 * @return
	 */
	@POST
	@Path("/appiyapplno")
	public RetInfo applyApplNo(String mgrBranchNo);
}
