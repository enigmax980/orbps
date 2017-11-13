package com.newcore.orbps.service.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.ErrListInfoBo;

/**
 * @author huanglong
 * @date 2016年8月24日
 * 内容:清单错误查询
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CgInsuredErrSelService {
     
	 @POST
	 @Path("/cgInsuredErrSelByCgNO")
	 public List<ErrListInfoBo> cgInsuredErrSelByCgNO(String cgNo) throws Exception;
	
}
