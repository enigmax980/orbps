package com.newcore.orbps.service.pms.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.cntrprint.vo.PrintVO;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReceiveReqService {
	@POST
    @Path("/")
	public String  excute(PrintVO printVO);
}
