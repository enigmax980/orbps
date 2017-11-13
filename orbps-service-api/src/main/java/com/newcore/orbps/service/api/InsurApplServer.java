package com.newcore.orbps.service.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * RESTFul webservice 服务. 保单基本信息录入接口
 * 
 * @author wangxiao 创建时间：2016年7月20日下午2:48:53
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsurApplServer {
	/**
	 * 保单暂存
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/tsave")
	public RetInfo tempSave(GrpInsurApplPara grpInsurApplPara);
	
	/**
	 * 
	 * 保单复核
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/check")
	RetInfo infoCheck(GrpInsurApplPara grpInsurApplPara);
	
	/**
	 * 保单录入
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/add")
	public RetInfo addSubmit(GrpInsurAppl grpInsurAppl);

	/**
	 * 保单修正
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/updata")
	public RetInfo modifySubmit(GrpInsurAppl grpInsurAppl);

	/**
	 * 根据流水号查询
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/query")
	GrpInsurApplBo searchInsurApplByBusinessKey(String businessKey);

	/**
	 * 保单录入
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@POST
	@Path("/save")
	RetInfo save(GrpInsurApplPara grpInsurApplPara);
	
	/**
	 * 根据流水号查询
	 * 
	 * @param applNo
	 * @return
	 */
	@POST
	@Path("/queryAS")
	ApplState searchApplState(String applNo);
	
	/**
	 * @author huanglong
	 * @date 2016年12月13日
	 * @param 
	 * @return GrpInsurAppl
	 * @content 多条件查询保单信息
	 */
	@POST
	@Path("/Single")
	public GrpInsurApplPara searchGrpInsurAppl(Map<String, Object> map);
}
