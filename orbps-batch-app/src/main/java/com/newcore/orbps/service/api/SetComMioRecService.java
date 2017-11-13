package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.FinQueueData;
import com.newcore.orbps.models.pcms.bo.RetInfo;


/**
 * 首席共保财务与财务队列落地服务
 *
 * @author zhanghui
 *
 * @date create on  2016年11月24日下午8:20:38
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface SetComMioRecService {

	/**
	 * 首席共保财务与财务队列落地服务
	 * @param finQueueData
	 * @return
	 * 2016年11月24日
	 * zhanghui
	 */
	@POST
	@Path("/setCom")
	public RetInfo setComMioRecService(FinQueueData finQueueData);
}
