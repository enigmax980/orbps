package com.newcore.orbps.service.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangxiao
 * @date 2017年4月11日
 * 内容:险种性质接口
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PolNatureService {
	@POST
    @Path("/search")
	public List<JSONObject> getPolNatureInfo(List<String> polCodes);
}
