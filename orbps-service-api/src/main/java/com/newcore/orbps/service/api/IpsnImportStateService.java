package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsured.IpsnImportInfo;

/**
 * @author wangxiao
 * 创建时间：2016年10月25日下午6:42:20
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IpsnImportStateService {
	//根据投保单号获取清单导入结果
	@POST
	@Path("/search")
	public RetInfoObject<IpsnImportInfo> GetIpsnImportResult(IpsnImportInfo ipsnImportInfo);

	/**
	 * IpsnImportStateService
	 * RetInfoObject<IpsnImportInfo>
	 */
	@POST
	@Path("/searchAll")
	RetInfoObject<IpsnImportInfo> getAllIpsnImportResult(IpsnImportInfo ipsnImportInfo);
}
