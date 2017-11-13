package com.newcore.orbps.service.api;



import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
 * RESTFul webservice 服务.
 * 生成电子保单接口
 * @author jiangbomeng
 * 创建时间：2016年7月20日下午2:48:53
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/*
 * 当前注解用于单方法申明 多方法需去掉次注解 解决sona错误
 */
@FunctionalInterface       
public interface CreateGrpIsurApplService  {
	/**
	 * 保单查询并生成pdf
	 * @param CreateGrpIsurApplService
	 * @return
	 */
	@POST
    @Path("/single")
	public String SearchInsurAppl(Map<String, Object> applNoMap) throws Exception;
}
