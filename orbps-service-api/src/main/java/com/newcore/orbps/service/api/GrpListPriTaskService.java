package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.GrpListCgTaskBo;
import com.newcore.orbps.models.service.bo.RetInfo;

/**
 * 打印数据落地接口客户端
 * @author JCC
 * 2017年5月17日 15:08:17
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrpListPriTaskService {

	@POST
	@Path("/add")
	RetInfo add(GrpListCgTaskBo grpListCgTaskVo);
}
