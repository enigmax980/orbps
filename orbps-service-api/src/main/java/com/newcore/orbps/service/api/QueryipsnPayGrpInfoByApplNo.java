package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.service.bo.IpsnPayBo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 暂收费支取功能
 * @author LJF
 * 2017年2月22日 15:25:45
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QueryipsnPayGrpInfoByApplNo {

	/**
	 * 暂收费支取功能:根据投保单号查询该保单下面收费组信息
	 * @param applNo 投保单号
	 */
	@POST
    @Path("/get")
	PageData<ChargePayGroupModalVo>  getIpsnPayGrpList (PageQuery<IpsnPayBo> applNo);
}

