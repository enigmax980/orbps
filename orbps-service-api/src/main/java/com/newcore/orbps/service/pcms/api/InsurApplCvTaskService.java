package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.InsurApplCvTaskBo;
import com.newcore.orbps.models.pcms.bo.RetInfo;


/**
 * restful webservice服务.
 *
 * @author xy
 * @date 2016-8-7 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface InsurApplCvTaskService {
	
    /**
     * 添加回执数据记录.
     *
     */
    @POST
    @Path("/add")
    RetInfo addInsurApplCvTask(InsurApplCvTaskBo insurapplcvtaskbo);
    
	
}