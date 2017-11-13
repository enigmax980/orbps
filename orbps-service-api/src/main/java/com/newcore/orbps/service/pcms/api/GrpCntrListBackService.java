package com.newcore.orbps.service.pcms.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.CntrBackInBO;
import com.newcore.orbps.models.pcms.bo.CntrBackOutBO;
/**
 * 回退/订正/要约撤销
 *
 * @author lijife
 *
 * @date create on  2017年2月27日下午2:47:49
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface GrpCntrListBackService {

	/**
	 * 回退、订正、要约撤销接口
	 * @param CntrListBackBo
	 * @return
	 * 2017年2月27日
	 * lijifei
	 */
	@POST
	@Path("/set")
	public CntrBackOutBO setGrpCntrListBack(CntrBackInBO cntrBackInBO);
}

