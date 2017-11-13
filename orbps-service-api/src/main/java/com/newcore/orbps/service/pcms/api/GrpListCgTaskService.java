package com.newcore.orbps.service.pcms.api;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.GrpListCgTaskBo;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskPo;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskRetInfo;




/**
 * 	清单摘要信息接受服务
 * restful webservice服务.
 * @author huangwei
 * @date 2016年8月24日
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrpListCgTaskService {

	/**
	 * <p> 将保单主要信息（个单团单通用）和团体公共信息（与个单不同）保存到核心合同库中 </p>
	 * @param grpInsurAppl
	 * @return
	 */
	@POST
	@Path("/add")
	GrpListCgTaskRetInfo grpListCgTaskService(GrpListCgTaskBo grpListCgTaskBo);
	
	/**
	 * <p> 测试使用，通过批次号与保单号更新状态 </p>
	 * @param grpInsurAppl
	 * @return
	 */
	@POST
	@Path("/updateListCgTaskServicebytest")
	GrpListCgTaskRetInfo updateListCgTaskServicebytest(GrpListCgTaskBo grpListCgTaskBo);
	
	/**
	 * 获取清单落地任务告知表数据
	 * pcms-service-api
	 * 2016年9月27日 上午10:10:10
	 * huangZhiZHi
	 */
	List<GrpListCgTaskPo> getGrpListCgTaskPoList(GrpListCgTaskPo grpListCgTask);
	GrpListCgTaskPo getGrpListCgTaskPo(GrpListCgTaskPo grpListCgTask);
	
	/**
	 * 更新清单落地任务告知表数据
	 * pcms-service-api
	 * 2016年10月10日 上午10:19
	 * huangZhiZHi
	 */
	boolean updateGrpListCgTaskPo(GrpListCgTaskPo grpListCgTaskPo);

}
