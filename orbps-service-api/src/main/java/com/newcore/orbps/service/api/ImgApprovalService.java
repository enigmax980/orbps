package com.newcore.orbps.service.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ImageAuditTrailVo;

/**
 * 影像审批结果信息接口
 * 
 * @author wangxiao
 * @date 2017-05-15
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ImgApprovalService {
	@POST
	@Path("/add")
	public RetInfo add(Map<String,String> map);
	@POST
	@Path("/search")
	public List<ImageAuditTrailVo> search(String applNo);
	
}
