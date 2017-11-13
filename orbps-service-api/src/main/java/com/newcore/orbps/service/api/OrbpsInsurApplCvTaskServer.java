package com.newcore.orbps.service.api;

import java.text.ParseException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.InsurApplCvTask;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.CvInformVo;

/**
 * @author huanglong
 * @date 2016年8月24日
 * 内容:回执核销接口
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface OrbpsInsurApplCvTaskServer {	
	/**
	 * @author huanglong
	 * @content 回执核销
	 * @param  InsurApplCvTask
	 * @return RetInfo
	 */
	@POST
	@Path("/submit")
	public  RetInfo insurApplCvTask(InsurApplCvTask insurApplCvTask);
	
	/**
	 * @author huanglong
	 * @date 2017年2月8日
	 * @param Map<String, String> map
	 * @return RetInfoObject<RetInfo>
	 * @content 批量回执核销
	 */
	@POST
	@Path("/submits")
	public  RetInfoObject<RetInfo> insurApplCvTasks(Map<String, String> map);

	/**
	 * @param insurApplCvTask
	 * @return
	 * @throws ParseException 
	 */
	@POST
	@Path("/inForm")
	public  RetInfo inForm(CvInformVo cvInformVo);
}
