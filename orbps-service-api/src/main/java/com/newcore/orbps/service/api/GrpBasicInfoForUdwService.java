package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.uwbps.WebGroupVO;





/**
 * 核保平台接口
 * @author liuchang
 * 创建时间：2016年8月1日 19:22:21
 */
@FunctionalInterface
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrpBasicInfoForUdwService {
	/**
	 * 获取保单相关数据
	 * @param appNo
	 * @return
	 */
	@POST
    @Path("/get")
	public WebGroupVO getGrpBasicInfo(Map<String, Object> applNo);
}
