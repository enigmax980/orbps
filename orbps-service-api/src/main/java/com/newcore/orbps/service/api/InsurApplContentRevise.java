package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.model.service.para.GrpInsuredPara;
import com.newcore.orbps.models.para.RetInfo;

/**
 *保单内容修正
 * @author liushuaifeng
 *
 * 创建时间：2016年11月4日下午3:14:08
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsurApplContentRevise {
	
	/**
	 * 修正团体客户信息或汇交人客户信息
	 */
	@POST 
	@Path("/reviseGrpInfo")
    public RetInfo reviseGrpBasicContent(GrpInsurApplPara grpInsurApplPara);
	
	/**
	 * 修改团体被保人信息
	 * 
	 * @param grpInsured	
	 *         被保人信息
	 * @return RetInfoObject
	 * @throws Exception
	 */
	@POST 
	@Path("/reviseGrpInsured")
	public RetInfo reviseGrpInsuredContent(GrpInsuredPara grpInsuredPara);

}
