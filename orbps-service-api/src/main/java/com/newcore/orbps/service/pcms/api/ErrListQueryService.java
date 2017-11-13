package com.newcore.orbps.service.pcms.api;


import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.pcms.bo.ErrListInBo;
import com.newcore.orbps.models.pcms.bo.ErrListOutBo;



/**
 * restful webservice服务.
 *
 * @author xy
 * @date 2016-7-29 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ErrListQueryService {
	
     /**
     * 获取 GrpListErrLog.
     *
     */
	@GET
    @Path("/getGrpListErrLog")
	ErrListOutBo getGrpListErrLog(@QueryParam("cgNo")String cgno,@QueryParam("batNo")String batno);
	
	@POST
    @Path("/queryGrpListErrLog")
	ErrListOutBo queryGrpListErrLog(ErrListOutBo grplisterrlog);
    
    /**
     * 查询全列表.
     *
     * @return the user list
     */
	@GET
    @Path("/getGrpListErrLogList")
    List<ErrListOutBo> getGrpListErrLogList();

	@POST
    @Path("/qry")
	public ErrListOutBo getGrpListErrLogListByCgnoAndBatno(ErrListInBo errListBo) ;



	
}
