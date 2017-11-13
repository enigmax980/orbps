package com.newcore.orbps.service.api;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 
 * @Description 
 * @author zhoushoubo
 * @date 2016年12月15日下午4:32:36
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsuredFileCreatService {
	/**
	 * InsuredFileCreatService
	 * void
	 * @throws IOException 
	 */
	@POST
    @Path("/creFile")
	public RetInfo creatExcelFile(String applNo) throws IOException;
}
