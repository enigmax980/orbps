package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.ComEarnestaccQryArgInfo;
import com.newcore.orbps.models.pcms.bo.ComEarnestaccQryRetBo;

/**
 * 参与共保暂交保费查询服务
 *
 * @author zhanghui
 *
 * @date create on  2016年11月28日下午4:01:46
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QryJoinComFAAccService {

	/**
	 *  参与共保暂交保费查询服务
	 * @param comEarnestaccQryArgInfo
	 * @return
	 * 2016年11月28日
	 * zhanghui
	 */
	@POST
	@Path("/qryJoin")
	public ComEarnestaccQryRetBo  qryJoinComFAAccService(ComEarnestaccQryArgInfo comEarnestaccQryArgInfo);
}
