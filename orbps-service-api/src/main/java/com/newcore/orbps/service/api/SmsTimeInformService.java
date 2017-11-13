package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;

/**
 * @author zhoushoubo
 * @date 2017年5月8日
 * 内容: 打印系统做成打印数据之后，调用CCS系统向客户发送短信，然后调用本服务记录短信发送时间。
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface SmsTimeInformService {

	/**
	 * @author zhoushoubo
	 * @content 设置sms短信发送时间到回执核销任务表
	 * @param applNo
	 * @param smsTime
	 * @return RetInfo
	 */
	@POST
	@Path("/upd")
	public RetInfo insSmsTime(Map<String, Object> paraMap);
}
