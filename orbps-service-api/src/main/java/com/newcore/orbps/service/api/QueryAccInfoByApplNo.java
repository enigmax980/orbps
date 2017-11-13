package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.service.bo.QueryEarnestAccInfoBean;
import com.newcore.orbps.models.web.vo.otherfunction.earnestpay.AccInfoVo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 暂收费支取功能：
 * @author LJF
 * 2017年2月22日 19:25:45
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface QueryAccInfoByApplNo {

	/**
	 * 暂收费支取功能:查询账户信息
	 * @param queryEarnestAccInfoBean 查询账户信息实体类
	 */
	@POST
    @Path("/get")
	PageData<AccInfoVo> getEarnestAccInfo(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean);
}