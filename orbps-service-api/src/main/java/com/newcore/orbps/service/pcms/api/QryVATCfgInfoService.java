package com.newcore.orbps.service.pcms.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.VATConfigQryInfo;
import com.newcore.orbps.models.pcms.bo.VATConfigQryRetInfo;


/**
 * 营改增配置信息查询服务
 * 外层服务
 * @author xushichao
 * @date 2016年11月28日
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QryVATCfgInfoService {
	@POST
    @Path("/qryQuery")
	List<VATConfigQryRetInfo> queryQryVATCfgInfo(VATConfigQryInfo vATConfigQryInfo);
}
