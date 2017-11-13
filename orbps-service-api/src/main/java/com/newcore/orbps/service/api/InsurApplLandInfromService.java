package com.newcore.orbps.service.api;


import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.LandRetVo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 保单落地告知服务
 *
 * @author liushuaifeng
 * @date 2017-02-21
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface InsurApplLandInfromService {

    @POST
    @Path("/infrom")
    RetInfo infromLandResult(LandRetVo landRetVo);

}