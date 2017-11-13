package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.EarnestPayList;
import com.newcore.orbps.models.service.bo.RetInfo;

/**
 *暂收费支取功能
 * 
 * @author lijifei 
 * 创建时间：2017年2月23日上午9:49:11
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface ProcEarnestPayPlnmioRecInfo {

	/**
	 * 暂收费支取功能:查询账户信息
	 * @param queryEarnestAccInfoBean 查询账户信息实体类
	 */
	@POST
    @Path("/get")
	RetInfo drawEarnestAccInfo(EarnestPayList earnestPayList);
}