package com.newcore.orbps.service.pcms.api;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.GrpListMioTask;
import com.newcore.orbps.models.pcms.bo.RetInfo;


/**
 * 实收付流水任务告知服务
 * @author xushichao
 *
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface GrpMioNtTaskService {
	/**
	 * 创建实收付流水冲正批处理和实收付流水落地批处理的任务
	 * @param grpListMioTask
	 * @return
	 */
	@POST
	@Path("/addGrp")
	RetInfo grpListMioTaskCreate(GrpListMioTask grpListMioTask);
}
