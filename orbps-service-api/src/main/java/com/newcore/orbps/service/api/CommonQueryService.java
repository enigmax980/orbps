package com.newcore.orbps.service.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.BackResult;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.ManualApprovalList;
import com.newcore.orbps.models.service.bo.UndoResult;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalVo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;


/**
 * @author huanglong
 * @date 2017年2月14日
 * @content 用于公共查询服务的接口
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CommonQueryService {
	
	/**
	 * @author huanglong
	 * @date 2017年2月14日
	 * @param CommonQueryService
	 * @return PageData<ManualApprovalList>
	 * @content 人工审批清单 查询
	 */
	@POST
	@Path("/manualApproval")
	public PageData<ManualApprovalList> manualApproval(Map<String, Object> map);
	

	/**
	 * @author huanglong
	 * @date 2017年2月16日
	 * @param CommonQueryService
	 * @return PageData<CommonAgreement>
	 * @content  共保协议查询
	 */
	@POST
	@Path("/commAgrQuery")
	public PageData<CommonAgreement> commonAgreementQuery(Map<String, Object> map);
	
	/**
	 * @author wangxiao
	 * @date 2017年2月21日
	 * @param PageQuery<ApprovalVo>
	 * @return PageData<UndoResult>
	 * @content  契约撤销查询(分页)
	 */
	@POST
	@Path("/queryForUndo")
	public PageData<UndoResult> queryForUndo(PageQuery<ApprovalVo> pageQuery);
	/**
	 * @author wangxiao
	 * @date 2017年2月21日
	 * @param ApprovalVo
	 * @return List<UndoResult>
	 * @content  契约撤销查询
	 */
	@POST
	@Path("/queryAllForUndo")
	public List<UndoResult> queryAllForUndo(ApprovalVo approvalVo);
	/**
	 * @author wangxiao
	 * @date 2017年2月21日
	 * @param PageQuery<ApprovalVo>
	 * @return PageData<BackResult>
	 * @content  契约回退查询(分页)
	 */
	@POST
	@Path("/queryForBack")
	public PageData<BackResult> queryForBack(PageQuery<ApprovalVo> pageQuery);
	/**
	 * @author wangxiao
	 * @date 2017年2月21日
	 * @param ApprovalVo
	 * @return List<BackResult>
	 * @content  契约回退查询
	 */
	@POST
	@Path("/queryAllForBack")
	public List<BackResult> queryAllForBack(ApprovalVo approvalVo);
}
