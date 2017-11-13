package com.newcore.orbps.service.oldpms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.oldpms.OldPrintCallBackRequst;
import com.newcore.orbps.models.service.bo.RetInfo;

/**
 * 保单辅助打印数据落地反馈服务
 * @author JCC
 * 2017年5月17日 09:42:56
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface OldPrintCallBackService {
	
	@POST
    @Path("/callBack")
	RetInfo callBack(OldPrintCallBackRequst oldPrint);
}
