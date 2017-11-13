package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.RetInfoObject;

/**
 * @author wangxiao
 * 创建时间：2016年9月3日下午3:42:25
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface CustNoSearchSrevice {
	@POST
	@Path("/search")
	public RetInfoObject search(Map<String, String> applNoMap);
}
