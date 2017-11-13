package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.halo.core.exception.FileAccessException;
import com.newcore.orbps.models.para.RetInfo;

/**
 * @author wangxiao
 * 创建时间：2016年10月31日下午4:33:14
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface IpsnImpAppliForInterService {
	/**
	 * IpsnImportAppliService
	 * RetInfo
	 * @throws FileAccessException 
	 */
	@POST
	@Path("/apply")
	RetInfo applyIpsnImport(Map<String, String> filemap) throws FileAccessException;
}
