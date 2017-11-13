package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;

/**
 * @author huanglong
 * @date 2016年9月5日
 * 内容: 人工核保结论服务
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface UdwResultService {

	/**
	 * @author huanglong
	 * @content 人工核保结论存储
	 * @param udwResult
	 * @return RetInfo
	 * @throws Exception
	 */
	@POST
	@Path("/submit")
	public RetInfo setUdwResult(ApplUdwResult udwResult);
}
