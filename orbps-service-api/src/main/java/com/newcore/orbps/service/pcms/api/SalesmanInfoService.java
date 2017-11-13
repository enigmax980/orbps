package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;


/**
 * 
 *<p>销售员查询服务</p>
 * @author zhanghui
 *
 * @date create on  2016年9月8日下午8:56:19
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface SalesmanInfoService {

	/**
	 * 销售员查询服务接口
	 * @param salesmanQueryInfo
	 * @return
	 * 2016年9月10日
	 * zhanghui
	 */
	@POST
    @Path("/qry")
	public SalesmanQueryReturnBo querySalesman(SalesmanQueryInfo salesmanQueryInfo);
}
