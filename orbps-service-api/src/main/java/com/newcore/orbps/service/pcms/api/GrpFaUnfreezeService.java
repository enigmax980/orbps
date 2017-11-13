package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.pcms.bo.GrpFaUnfreezeBo;
import com.newcore.orbps.models.pcms.bo.RetInfo;
/**
 * restful webservice服务.
 *
 * @author maruifu
 * @date 2016-11-01
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface GrpFaUnfreezeService {
	
    /**
     * 实收冲正结果查询服务
     *
     */
    @POST
    @Path("/updateOperation")
    RetInfo operationGrpFaUnfreeze(GrpFaUnfreezeBo grpFaUnfreezeBo);
    
	
}