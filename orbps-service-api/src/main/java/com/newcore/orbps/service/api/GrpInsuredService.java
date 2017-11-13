package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 被保人信息Service层接口
 * 
 * @author jinmeina
 * @date 2016-10-25 10:25
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrpInsuredService {

	/**
	 * 增加被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST
	@Path("/add")
	public RetInfo addGrpInsured(GrpInsured grpInsured);
	
	/**
	 * 修改被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST 
	@Path("/update")
	public RetInfo updateGrpInsured(GrpInsured grpInsured);
	
	/**
	 * 删除被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST 
	@Path("/remove")
	public RetInfo removeGrpInsured(Map<String,Object> paraMap);
	
	/**
	 * 批增被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST
	@Path("/addAll")
	public RetInfo addAllGrpInsured(TraceNodePra traceNodePra);
	
	/**
	 * 查询被保人信息
	 * 
	 * @param grpInsured
	 *            被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST
	@Path("/query")
	public GrpInsured queryGrpInsured(GrpInsured grpInsured);

	/**
	 * GrpInsuredService
	 * GrpInsured
	 */
	@POST
	@Path("/queryPreOrNext")
	GrpInsured queryPreOrNextGrpInsured(Map<String, Object> map);
}
