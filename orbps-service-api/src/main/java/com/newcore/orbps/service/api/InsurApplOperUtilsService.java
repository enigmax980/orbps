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
public interface InsurApplOperUtilsService {
	/**
     * 是否落地在途，即财务，被保人、保单基本信息、共保协议等是否落地完成
     * 1.未生效
     * 2.生效、落地在途
     * 3.已落地
     * @param applNo
     * @return
     */
	@POST
    @Path("/get")
	public RetInfo getIsInsurApplLanding(String applNo);

}
