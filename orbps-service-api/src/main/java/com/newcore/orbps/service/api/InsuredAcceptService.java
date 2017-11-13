package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.RegistInfo;

/**
 * @author wangxiao
 * 创建时间：2016年10月24日下午1:26:47
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsuredAcceptService {
	/**
	 * 投保受理
	 * @param InsurApplRegist
	 * @return RetInfo
	 */
	@POST
    @Path("/accept")
	public RetInfo accept(RegistInfo registInfo);
	/**
	 * 修改并保存
	 * @param InsurApplRegist
	 * @return RetInfo
	 */
	@POST
	@Path("/update")
	public RetInfo update(RegistInfo registInfo);
	/**
	 * 查询
	 * @param Map<String,String>
	 * @return InsurApplRegist
	 */
	@POST
	@Path("/search")
	public RegistInfo search(Map<String, String> map);
	
	/**
	 * 删除
	 * @param Map<String,String>
	 * @return InsurApplRegist
	 */
	@POST
	@Path("/delete")
	public RetInfo delete(Map<String, String> map);
	
}
