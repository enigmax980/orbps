package com.newcore.orbps.service.api;

import java.util.Map;
import com.newcore.orbps.models.para.RetInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author zhoushoubo
 * @date 2017年5月16日
 * 内容: 影像资料审核流程服务。
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface TaskImgAuditService {

	/**
	 * @author zhoushoubo
	 * @content 开始影像资料审核流程
	 * @param paraMap
	 */
	@POST
	@Path("/start")
	public RetInfo startTask(Map<String, Object> paraMap);
}
