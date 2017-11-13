package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.RetInfoObject;

/**
 * @author huanglong
 * @date 2016年9月24日
 * 内容:保单操作轨迹查询
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QueryGrpInsurApplOperTraceService {	
	/**
	 * @author huanglong
	 * @content 保单操作轨迹查询
	 * @param map
	 * @return RetInfoObject<InsurApplOperTrace>
	 */
	@POST
	@Path("/querys")
	public RetInfoObject<InsurApplOperTrace> queryGrpInsurApplOperTrace(Map<String, Object> map);		
}
