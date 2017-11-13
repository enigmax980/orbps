package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.CgPubExeAttrBo;
import com.newcore.orbps.models.pcms.bo.RetInfo;

/**
*  对保单公共属性扩展表CG_PUB_EXT_ATTR进行操作
* @author huangwei
* @date 2016年9月7日
*/
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CgPubExeAttrService {
	
	/*
	 * 对对保单公共属性扩展表CG_PUB_EXT_ATTR进行新增
	 */
	@POST
    @Path("/addCgPubExeAttr")
	RetInfo addCgPubExeAttr(CgPubExeAttrBo cgPubExeAttrBo);
	
	/**
	 * 
	* <p>根据组合保单号删除保单扩展属性 </p>
	* @author zhangyuan
	* @date 2016年9月13日
	 */
	@POST
	@Path("/delCgPubExeAttr")
	RetInfo delCgPubExeAttr(CgPubExeAttrBo cgPubExeAttrBo);
	
	
	boolean isgrpListTpTask(CgPubExeAttrBo cgPubExeAttrBo);

}
