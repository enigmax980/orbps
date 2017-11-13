package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.web.vo.sendgrpinsurappl.GrpInsurApplSendVo;

/**	
 * 保单落地
   @auther   lijifei
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MissionLandingService {

	@POST
    @Path("/")
	RetInfo missionLandingCreate(GrpInsurApplSendVo grpInsurApplSendVo);
}
