package com.newcore.orbps.service.uwbps.api;

import com.newcore.orbps.models.uwbps.AltBussStatInVO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Guojunjie
 *         Created on 16-9-19
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UnderWritingService {
    /**
	 * 更改新单状态
	 * @author yuhao
	 * @param altBussStatInVO
	 * @return
	 */
	@POST
	@Path("/updateNewApplStat")
	boolean updateNewApplStat(AltBussStatInVO altBussStatInVO);
}