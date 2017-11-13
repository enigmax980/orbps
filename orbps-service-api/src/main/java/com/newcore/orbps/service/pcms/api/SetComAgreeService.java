package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.CommonAgreement;
import com.newcore.orbps.models.pcms.bo.RetInfo;

/**
 * 参与共保协议落地服务
 * @author xushichao
 * @date 2016年11月23日
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface SetComAgreeService {
	@POST
    @Path("/insAdd")
	RetInfo addCommonAgreement(CommonAgreement commonAgreement);
}
