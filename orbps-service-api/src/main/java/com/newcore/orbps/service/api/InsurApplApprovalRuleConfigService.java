package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.InsurApplApprovalRule;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InsurApplApprovalRuleConfigService {
	@POST
	@Path("/add")
	public RetInfo addRule(InsurApplApprovalRule insurApplApprovalRule);

	@POST
	@Path("/query")
	public InsurApplApprovalRule queryRule(InsurApplApprovalRule insurApplRule);

	@POST
	@Path("/update")
	public RetInfo updateRule(InsurApplApprovalRule insurApplApprovalRule);

	@POST
	@Path("/page")
	public PageData<InsurApplApprovalRule> listPage(
			PageQuery<InsurApplApprovalRule> pageQuery);
}
