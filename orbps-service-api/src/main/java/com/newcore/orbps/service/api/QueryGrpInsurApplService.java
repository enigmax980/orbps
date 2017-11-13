package com.newcore.orbps.service.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;


/**
 * @author huanglong
 * @date 2016年9月6日
 * 内容:保单查询服务
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QueryGrpInsurApplService {
	
	/**
	 * @author huanglong
	 * @content 保单查询服务 暂时没有启用
	 * @param map
	 * @return List<GrpInsurAppl>
	 */
	@POST
	@Path("/query")
	public List<GrpInsurAppl> getGrpInsurAppls(Map<String, Object> map);

}
