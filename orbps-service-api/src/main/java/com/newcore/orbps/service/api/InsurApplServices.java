package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 录入接口
 * @author xiaoye
 * 创建时间： 2016年5月04日10:48:06
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsurApplServices {
	
	
	/**
	 * 查询投保单
	 * @param appNo
	 * @return
	 */
	@POST
    @Path("/page")
	PageData<ResponseVo> getProductSummaryList(PageQuery<ResponseVo> pageQuery);

	
}
