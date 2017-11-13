package com.newcore.orbps.service.api;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 清单导入结果查询
 * 
 * @author liushuaifeng
 *
 * 创建时间：2016年7月28日下午5:04:57
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface IpsnImportResultService {

	//根据投保单号获取清单导入结果
	@POST
	@Path("/get")
	public RetInfo GetIpsnImportResult(Map<String, String> applMap) throws IOException;

}
