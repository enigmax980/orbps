package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.pcms.bo.GrpFaReturn;
import com.newcore.orbps.models.pcms.bo.RetInfo;


/**
 * 暂交费还回服务
 * restful webservice服务.
 * @author xushichao
 * @date 2016年11月4日
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface GrpFaReturnService {
	/**
	 * 暂交费还回
	 * @return
	 */
	@POST
	@Path("/updateFa")
	RetInfo faReturn(GrpFaReturn grpFaReturn);
}

