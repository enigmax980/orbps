package com.newcore.orbps.service.pcms.api;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.pcms.bo.CntrBackTrackPo;
import com.newcore.orbps.models.pcms.bo.GrpCntrBackBO;
import com.newcore.orbps.models.pcms.bo.RetInfo;

/*
 * 保单回退服务，修改COMBINED_CNTR组合保单(合同组)和STD_CONTRACT保险合同状态为C
   @auther   huangwei
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrpCntrBackService {
	@POST
    @Path("/setGrp")
	RetInfo setGrpCntrBack(GrpCntrBackBO grpCntrBackBO);
	
	List<CntrBackTrackPo> getListByApplNoOrderSeqNo(CntrBackTrackPo cntrBackTrack);
 
}
