package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.vo.GrpInsurApplStateVo;



/** 
* @ClassName: QueryGrpInsurApplStateService 
* @Description:  根据投保单号查询保单进度 给前台使用
* @author  jiangbomeng
* @date 2016年9月26日 上午9:03:01 
*  
*/
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/*
 * 当前注解用于单方法申明 多方法需去掉次注解 解决sona错误
 */
@FunctionalInterface     
public interface QueryGrpInsurApplStateService {
	/** 
	* @Title: getSalesInfo 
	* @Description: 根据投保单号查询保单进度
	* @param @param salesInfo
	* @param @return    设定文件 
	* @return SalesInfoVo    返回类型 
	* @throws 
	*/ 
	@POST
	@Path("/query")
	GrpInsurApplStateVo getGrpInsurApplState(Map<String, Object> map);

}
