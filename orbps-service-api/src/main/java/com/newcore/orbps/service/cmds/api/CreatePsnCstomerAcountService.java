package com.newcore.orbps.service.cmds.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface CreatePsnCstomerAcountService {
	@POST
	@Path("/")
	public String createPsnCstomerAcount(List<Map<String, Object>> list);
}
