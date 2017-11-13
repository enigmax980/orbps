package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.MioFirstPremChkBo;
import com.newcore.orbps.models.pcms.bo.MioFirstPremchkRetInfo;


/**
 * 	短险保单收费检查客户端
 * restful webservice服务.
 * @author JCC
 * @date 2016年8月30日
 */
@FunctionalInterface
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MioFirstPremchkService {
	/*
	 *短险保费检查接口
	 * 
	 */
	@POST
	@Path("/chk")
	MioFirstPremchkRetInfo chMioFirstPremchkService(MioFirstPremChkBo  mioFirstPremChkBo);
}
