package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author huanglong
 * @date 2017年4月6日
 * @content 档案清单监控服务
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ArchiveListMonitor {
	
	/**
	 * @author huanglong
	 * @date 2017年4月6日
	 * @param ArchiveListMonitor
	 * @return 
	 * @content 发送二次落地服务
	 */
	@POST
	@Path("/send")
	public void send(String applNo);
	
	/**
	 * @author huanglong
	 * @date 2017年4月6日
	 * @param ArchiveListMonitor
	 * @return String  档案清单已开户，第一次落地完毕时-A,二次落地成功-S,二次落地失败-F,无二次落地-N,正在开户中-K,异常-"E"
	 * @content 查询档案清单状态
	 */
	@POST
	@Path("/query")
	public String query(String  applNo);
}
