package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.RetInfoObject;

/**
 * @author huanglong
 * @date 2016年10月25日
 * 内容:共保服务接口
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CommonAgreementService {

	/**
	 * @author huanglong
	 * @content 共保 新增/修改 提交服务       参与共保提交服务
	 * @param commonAgreement
	 * @return RetInfo
	 */
	@POST
	@Path("/submit")
	public RetInfo comAgrSerSubmit(CommonAgreement commonAgreement);
	
	/**
	 * @author huanglong
	 * @content 共保 查询服务  
	 * @param CommonAgreement
	 * @return RetInfoObject<CommonAgreement>
	 */
	@POST
	@Path("/query")
	public RetInfoObject<CommonAgreement>  comAgrSerQuery(CommonAgreement commonAgreement);
	
	/**
	 * @author huanglong
	 * @content 共保 审核提交服务
	 * @param CommonAgreement
	 * @return RetInfo
	 */
	@POST
	@Path("/check")
	public RetInfo comAgrSerCheck(CommonAgreement commonAgreement);
	
}
