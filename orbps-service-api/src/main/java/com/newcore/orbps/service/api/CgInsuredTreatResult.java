package com.newcore.orbps.service.api;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.newcore.orbps.models.service.bo.GrpInsuredBatchRecord;
import com.newcore.orbps.models.service.bo.RetInfo;



/**
 * 接受清单导入处理结果
 *
 * @author lijifei
 * 创建时间：2016年8月19日09:24:11
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface CgInsuredTreatResult {


	/**
	 * @param 
	 * @return
	 * @author lijifei
	 */
	@POST
    @Path("/get")
	public List<RetInfo> doCgInsuredTreatResult(List<GrpInsuredBatchRecord> grpInsuredList);



}
