package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.WebsiteQueryInfo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryReturnBo;

/**
 * <p>网点查询服务</p>
 *
 * @author zhanghui
 *
 * @date create on  2016年9月9日上午8:49:03
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface WebsiteInfoService {

	/**
	 * 查询网点信息服务接口
	 * @param websiteQueryInfo
	 * @return
	 * 2016年9月9日
	 * zhanghui
	 */
	@POST
    @Path("/qry")
	public WebsiteQueryReturnBo queryWebsite(WebsiteQueryInfo websiteQueryInfo);
}
