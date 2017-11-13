package com.newcore.orbps.service.api;



import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ContractBusiStateQueryVo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 
 * @author nzh
 *  合同查询
 * 创建时间：2016年9月12日下午2:38:51
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContractQueryService {
	@POST
	@Path("/query")	
	public PageData<ContractBusiStateQueryVo> queryCheck(PageQuery<ContractBusiStateQueryVo> pageQuery);
	
	@POST
	@Path("/list")	
	public PageData<QueryinfVo> query(PageQuery<CorrectionVo> pageQuery);
	
	@POST
	@Path("/queryone")	
	public GrpInsurAppl query(GrpInsurAppl grpInsurAppl);

	/**
	 * ContractQueryService
	 * ContractBusiStateQueryVo
	 */
	@POST
	@Path("/queryAll")
	public List<ContractBusiStateQueryVo> queryAllCheck(ContractBusiStateQueryVo contractBusiStateQueryVo);

}
