package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.GuaranteeRateInfo;
import com.newcore.orbps.models.pcms.bo.GuaranteeRateReturnBo;


/**
 * 保单进度查询
 *
 * @author zhanghui
 *
 * @date create on  2016年9月7日下午2:57:37
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GuaranteeRateQueryService {

	/**
	 * 帽子落地
	 * @param guaranteeRateInfo
	 * @return
	 */
	@POST
    @Path("/qry")
	public  GuaranteeRateReturnBo  QueryGuaranteeRate(GuaranteeRateInfo guaranteeRateInfo);
	
	
}
