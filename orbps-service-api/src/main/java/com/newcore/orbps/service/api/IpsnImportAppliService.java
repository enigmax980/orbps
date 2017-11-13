package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 清单导入请求接口
 * 
 * @author liushuaifeng
 *
 * 创建时间：2016年7月28日下午7:31:47
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface IpsnImportAppliService {
	/**
	 * IpsnImportAppliService
	 * RetInfo
	 */
	@POST
    @Path("/single")
	RetInfo applyIpsnImport(Map<String, String> filemap);
	
}
