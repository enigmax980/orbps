package com.newcore.orbps.service.api;

import java.text.ParseException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.QueryReceipt;
import com.newcore.supports.models.service.bo.PageData;

/**
 * @author huanglong
 * @date 2016年9月7日
 * 内容:查询回执核销
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public  interface QueryInsurApplCvTaskServer {

	/**
	 * @author huanglong
	 * @content 回执核销
	 * @param map
	 * @return RetInfoObject<PageData<InsurApplCvTask>>
	 * @throws ParseException
	 */
	@POST
	@Path("/query")
	public PageData<QueryReceipt> queryInsurApplCvTask(Map<String, Object> map);
	
	/**
	 * @author huanglong
	 * @date 2017年2月10日
	 * @param QueryInsurApplCvTaskServer
	 * @return Map<String,Long>
	 * @content 查询批量核销执行状态
	 */
	@POST
	@Path("/state")
	public Map<String, Object> queryExecuteState(Map<String, String> map);
	
}
