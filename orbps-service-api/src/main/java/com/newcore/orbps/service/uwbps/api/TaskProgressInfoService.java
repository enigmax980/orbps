package com.newcore.orbps.service.uwbps.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.uwbps.taskVo;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TaskProgressInfoService {
	@POST
	@Path("/taskStatus")
	taskVo queryTaskStatus(Map<String,Object> map);
}
