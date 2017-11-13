package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 投保单号申请接口
 * @author jiachenchen
 * 创建时间：2016年8月3日 10:03:54
 */
@FunctionalInterface
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsurApplNoApplyService {
	
	/**
	 * 生成投保单号 
	 * @param cntrType 类别
	 * @param salesBranchNo 销售机构号
	 * @return 16位 随机码
	 * @throws Exception 
	 */
	@POST
    @Path("/getInsurApplNo")
	public RetInfo createInsurApplNo(Map<String, String> map);

}
