package com.newcore.orbps.service.ipms.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.halo.core.fastjson.annotation.JsonConfig;

/**
 * 产品组调用-查询险种宽限期客户端配置
 * @author jiachenchen
 * 2017年3月14日 09:36:10
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PolicyBasicService {
	   @POST
	   @Path("/excute")
	public @JsonConfig(writeType = true)  Map<String , Object> excute(@JsonConfig(writeType = true) Map<String , Object> map);
}
