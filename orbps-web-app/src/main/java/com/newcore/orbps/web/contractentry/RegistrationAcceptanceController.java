package com.newcore.orbps.web.contractentry;

import java.util.ArrayList;
import com.halo.core.web.annotation.CurrentSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryInfo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryReturnBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.ApplInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.service.bo.insurapplregist.RegistInfo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegAcceptanceVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegFilesListVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegSalesListFormVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegUsualAcceptVo;
import com.newcore.orbps.service.api.InsuredAcceptService;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.service.pcms.api.WebsiteInfoService;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 投保登记受理
 * 
 * @author chang
 *
 */

@Controller
@RequestMapping("/orbps/contractEntry/reg")
public class RegistrationAcceptanceController {

	@Autowired
	PolicyQueryService resulpolicyQueryService;

	/**
	 * 契约受理服务
	 */
	@Autowired
	InsuredAcceptService insuredAcceptService;
	/**
	 * 分页查询的支持服务
	 */
	@Autowired
	PageQueryService pageQueryService;
	/**
	 * 保单辅助销售人员查询服务
	 */
	@Autowired
	SalesmanInfoService resulsalesmanInfoService;
	/**
	 * 保单辅助网点信息查询服务
	 */
	@Autowired
	WebsiteInfoService resulwebsiteInfoService;
	/**
	 * 查询机构号服务
	 */
	@Autowired
	BranchService branchService;
	/**
	 * 查询险种名称
	 * 
	 * @author xiaoYe
	 * @param query
	 * @param productVo
	 * @return
	 */
	@RequestMapping(value = "/searchBusiName")
	public @ResponseMessage String getSearchBusiName(@RequestBody ResponseVo responseVo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);

		Map<String, Object> map = new HashMap<>();
		map.put("polCode", responseVo.getBusiPrdCode());
		Map<String, Object> maps = resulpolicyQueryService.excute(map);
		String result = JSON.toJSONString(maps);
		String JSONpolicyBo = JSONObject.parseObject(result).getString("policyBo");
		JSONObject JSONpolicyBos = JSONObject.parseObject(JSONpolicyBo);
		String polNameChn = JSONpolicyBos.getString("polNameChn");
		return polNameChn;
	}

	/**
	 * 受理提交
	 * 
	 * @author liuchang
	 * @param session
	 * @param regAcceptanceVo
	 * @return
	 */
	@RequestMapping(value = "/submit")
	public @ResponseMessage RetInfo submitReg(@CurrentSession Session session,
			@RequestBody RegAcceptanceVo regAcceptanceVo) {
		//获取销售信息
		List<RegSalesListFormVo> segSalesListFormVos = regAcceptanceVo.getSalesListFormVos();
		//判断销售信息是否为空
		if(segSalesListFormVos == null || segSalesListFormVos.isEmpty()){
			RetInfo retInfo = new RetInfo();
			retInfo.setErrMsg("销售信息为空");
			retInfo.setRetCode("0");
			return retInfo;
		}
		//取第一个销售信息的销售机构
		String salesBranchNo = segSalesListFormVos.get(0).getSalesBranchNo();
		//如果存在共同展业，取第一主销售机构
		for(RegSalesListFormVo regSalesListFormVo:segSalesListFormVos){
			if(StringUtils.equals(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey(), regSalesListFormVo.getJointFieldWorkFlag())){
				salesBranchNo = regSalesListFormVo.getSalesBranchNo();
				break;
			}
		}
		//获取销售机构的省级机构号
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo1);
		String salesProvinceBranchNo = branchService.findProvinceBranch(salesBranchNo);
		//获取session的省级机构号
		CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo2);
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
		//比较session与录入销售员的省级机构是否相同
		if(!StringUtils.equals(salesProvinceBranchNo, sessionProvinceBranchNo)){
			RetInfo retInfo = new RetInfo();
			retInfo.setErrMsg("操作员只能受理本省级机构下的保单");
			retInfo.setRetCode("0");
			return retInfo;
		}
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);

		RegistInfo registInfo = new RegistInfo();
		registInfo = this.registInfoVoToBo(regAcceptanceVo);

		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());
		traceNode.setPclkNo(sessionModel.getClerkNo());
		traceNode.setPclkName(sessionModel.getName());
		registInfo.setTraceNode(traceNode);
		// 获取后台服务
		return insuredAcceptService.accept(registInfo);
	}

	/**
	 * 受理暂存
	 * 
	 * @author jincong
	 * @param session
	 * @param regAcceptanceVo
	 * @return
	 */
	@RequestMapping(value = "/save")
	public @ResponseMessage RetInfo saveReg(@CurrentSession Session session,
			@RequestBody RegAcceptanceVo regAcceptanceVo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);

		RegistInfo registInfo = new RegistInfo();
		registInfo = this.registInfoVoToBo(regAcceptanceVo);

		SessionModel sessionModel = SessionUtils.getSessionModel(session);

		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());
		traceNode.setPclkNo(sessionModel.getClerkNo());
		traceNode.setPclkName(sessionModel.getName());
		registInfo.setTraceNode(traceNode);

		// 获取后台服务
		return insuredAcceptService.update(registInfo);
	}

	/**
	 * 查询信息
	 * 
	 * @author chang
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/query")
	// @RequiresPermissions("orbps:ruleQuery:query")
	public @ResponseMessage RegAcceptanceVo getQueryList(@CurrentSession Session session,@RequestBody RegUsualAcceptVo regUsualAcceptVo) {
		//获取session的省级机构号
		CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo2);
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String, String> map = new HashMap<String, String>();
		map.put("billNo", regUsualAcceptVo.getApplNo());
		// 调用后台服务
		RegistInfo registInfo = insuredAcceptService.search(map);
		//获取受理操作员的省级机构号
		TraceNode traceNode = registInfo.getTraceNode();
		CxfHeader headerInfo3 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo3);
		String pclkProvinceBranchNo = branchService.findProvinceBranch(traceNode.getPclkBranchNo());
		if(!StringUtils.equals(sessionProvinceBranchNo, pclkProvinceBranchNo)){
			throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省机构的保单");
		}
		// 大Vo
		RegAcceptanceVo regAcceptanceVo = new RegAcceptanceVo();
		regAcceptanceVo = this.regAcceptanceBoToVo(registInfo.getInsurApplRegist());
		return regAcceptanceVo;
	}

	/**
	 * 查询销售人员信息
	 * 
	 * @author jincong
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/querySaleName")
	public @ResponseMessage String querySaleName(@RequestBody RegSalesListFormVo regSalesListFormVo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
		if (regSalesListFormVo.getSalesBranchNo() != null && regSalesListFormVo.getSalesNo() != null
				&& regSalesListFormVo.getSalesChannel() != null) {
			salesmanQueryInfo.setSalesChannel(regSalesListFormVo.getSalesChannel());
			salesmanQueryInfo.setSalesBranchNo(regSalesListFormVo.getSalesBranchNo());
			salesmanQueryInfo.setSalesNo(regSalesListFormVo.getSalesNo());
		} else {
			return "fail";
		}
		SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
		if ("success".equals(queryReturnBo.getErrMsg())) {
			return queryReturnBo.getSalesName();
		} else {
			return "fail";
		}
	}

	/**
	 * 查询网点信息
	 * 
	 * @author jincong
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/queryWorksite")
	public @ResponseMessage String queryWorksite(@RequestBody RegSalesListFormVo regSalesListFormVo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		WebsiteQueryInfo websiteQueryInfo = new WebsiteQueryInfo();
		if (regSalesListFormVo.getSalesBranchNo() != null && regSalesListFormVo.getWorksiteNo() != null
				&& regSalesListFormVo.getSalesChannel() != null) {
			websiteQueryInfo.setSalesChannel(regSalesListFormVo.getSalesChannel());
			websiteQueryInfo.setSalesBranchNo(regSalesListFormVo.getSalesBranchNo());
			websiteQueryInfo.setSalesDeptNo(regSalesListFormVo.getWorksiteNo());
		} else {
			return "fail";
		}
		WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
		if ("success".equals(websiteQuery.getErrMsg())) {
			return websiteQuery.getDeptName();
		} else {
			return "fail";
		}
	}

	/**
	 * 受理删除
	 * 
	 * @author jincong
	 * @param session
	 * @param applNo
	 * @return
	 */
	@RequestMapping(value = "/regDelete")
	public @ResponseMessage RetInfo regDelete(@CurrentSession Session session, @RequestBody String applNo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);

		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());
		traceNode.setPclkNo(sessionModel.getClerkNo());
		traceNode.setPclkName(sessionModel.getName());
		Map<String, String> map = new HashMap<>();
		map.put("billNo", applNo);
		return insuredAcceptService.delete(map);
	}

	private RegAcceptanceVo regAcceptanceBoToVo(InsurApplRegist insurApplRegist) {
		RegAcceptanceVo regAcceptanceVo = new RegAcceptanceVo();

		RegUsualAcceptVo regUsualAcceptVos = new RegUsualAcceptVo();
		// 展业信息
		RegSalesListFormVo regSalesListFormVo = new RegSalesListFormVo();
		// 保存展业信息
		List<RegSalesListFormVo> regSalesList = new ArrayList<RegSalesListFormVo>();
		// 资料清单
		RegFilesListVo regFilesListVo = null;
		// 保存资料清单
		List<RegFilesListVo> regFilesList = new ArrayList<>();

		if (null != insurApplRegist) {
			// 团险方案审批号
			regUsualAcceptVos.setGroupPriceNo(insurApplRegist.getApproNo());
			// 保单类型后台没有字段
			// 外包录入标记
			regUsualAcceptVos.setEntChannelFlag(insurApplRegist.getEntChanelFlag());
			// 被保险人数
			regUsualAcceptVos.setInsuredNum(insurApplRegist.getIpsnNum());
			// 投保单数量
			regUsualAcceptVos.setApplNum(insurApplRegist.getBillNumber());
			// 契约形式
			regUsualAcceptVos.setCntrType(insurApplRegist.getCntrType());
			// 险种
			regUsualAcceptVos.setPolName(insurApplRegist.getPolNameChn());
			// 申请日期
			regUsualAcceptVos.setApplDate(insurApplRegist.getAcceptDate());
			// 保费合计
			regUsualAcceptVos.setSumPremium(insurApplRegist.getSumPremium());
			// 投保人/汇缴人
			regUsualAcceptVos.setHldrName(insurApplRegist.getHldrName());
			// 是否共保
			regUsualAcceptVos.setAgreementFlag(insurApplRegist.getIsCommonAgreement());
			// 险种代码
			regUsualAcceptVos.setPolCode(insurApplRegist.getPolCode());
			// 币种
			regUsualAcceptVos.setCurrencyCode(insurApplRegist.getCurrencyCode());
			// 共保协议号
			regUsualAcceptVos.setAgreementNum(insurApplRegist.getAgreementNo());
			// 共享客户信息
			regUsualAcceptVos.setParaVal(insurApplRegist.getShareCustFlag());
			// 共同展业
			regUsualAcceptVos.setSalesDevelopFlag(!StringUtils.isBlank(insurApplRegist.getSalesDevelopFlag())
					? insurApplRegist.getSalesDevelopFlag() : "");

			for (int i = 0; i < insurApplRegist.getSalesInfos().size(); i++) {
				regSalesListFormVo = new RegSalesListFormVo();
				// 判断是否用网点代理，做b转v的赋值
				if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesName())
						&& StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesNo())
						&& StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesDeptNo())
						&& StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getDeptName())) {
					// 销售渠道
					regSalesListFormVo.setSalesChannelAgency(insurApplRegist.getSalesInfos().get(i).getSalesChannel());
					// 销售机构代码
					regSalesListFormVo.setBranchNoAgency(insurApplRegist.getSalesInfos().get(i).getSalesBranchNo());
					// 销售员姓名
					regSalesListFormVo.setSalesNameAgency(insurApplRegist.getSalesInfos().get(i).getSalesName());
					// 销售员工号
					regSalesListFormVo.setSalesCodeAgency(insurApplRegist.getSalesInfos().get(i).getSalesNo());
					// 代理网点号
					regSalesListFormVo.setWorkSiteNoAgency(insurApplRegist.getSalesInfos().get(i).getSalesDeptNo());
					// 网点名称
					regSalesListFormVo.setWorkSiteNameAgency(insurApplRegist.getSalesInfos().get(i).getDeptName());
					// 代理员代码
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getCommnrPsnNo())) {
						regSalesListFormVo.setAgencyNo(insurApplRegist.getSalesInfos().get(i).getCommnrPsnNo());
					}
					// 代理员姓名
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getCommnrPsnName())) {
						regSalesListFormVo.setAgencyName(insurApplRegist.getSalesInfos().get(i).getCommnrPsnName());
					}
				} else {
					// 销售渠道
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesChannel())) {
						regSalesListFormVo.setSalesChannel(insurApplRegist.getSalesInfos().get(i).getSalesChannel());
					}
					// 销售机构代码
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesBranchNo())) {
						regSalesListFormVo.setSalesBranchNo(insurApplRegist.getSalesInfos().get(i).getSalesBranchNo());
					}
					// 销售员姓名
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesName())) {
						regSalesListFormVo.setSalesName(insurApplRegist.getSalesInfos().get(i).getSalesName());
					}
					// 销售员工号
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesNo())) {
						regSalesListFormVo.setSalesNo(insurApplRegist.getSalesInfos().get(i).getSalesNo());
					}
					// 代理网点号
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getSalesDeptNo())) {
						regSalesListFormVo.setWorksiteNo(insurApplRegist.getSalesInfos().get(i).getSalesDeptNo());
					}
					// 网点名称
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getDeptName())) {
						regSalesListFormVo.setWorksiteName(insurApplRegist.getSalesInfos().get(i).getDeptName());
					}
				}

				if (insurApplRegist.getSalesInfos().size() > 1) {
					// 销售员主副标记
					if (StringUtils.isNotEmpty(insurApplRegist.getSalesInfos().get(i).getDevelMainFlag())) {
						regSalesListFormVo.setJointFieldWorkFlag(
								StringUtils.equals("1", insurApplRegist.getSalesInfos().get(i).getDevelMainFlag()) ? "Y"
										: "N");
					}
					// 展业比例多销售员需传
					if (null != insurApplRegist.getSalesInfos().get(i).getDevelopRate()) {
						Double businessPct = insurApplRegist.getSalesInfos().get(i).getDevelopRate() * 100;
						regSalesListFormVo.setBusinessPct(businessPct);
					}
				}
				regSalesList.add(regSalesListFormVo);
			}
			if(insurApplRegist.getApplInfos()==null){
				insurApplRegist.setApplInfos(new ArrayList<>());
			}
			for (ApplInfo applInfos : insurApplRegist.getApplInfos()) {
				regFilesListVo = new RegFilesListVo();
				regFilesListVo.setFileNum(applInfos.getApplInfoNumber());
				regFilesListVo.setFileType(applInfos.getApplInfoType());
				regFilesList.add(regFilesListVo);
			}

		}

		// 放入大Vo中
		regAcceptanceVo.setUsualAcceptVo(regUsualAcceptVos);
		regAcceptanceVo.setSalesListFormVos(regSalesList);
		regAcceptanceVo.setFilesListVos(regFilesList);
		return regAcceptanceVo;
	}

	private RegistInfo registInfoVoToBo(RegAcceptanceVo regAcceptanceVo) {
		RegistInfo registInfo = new RegistInfo();
		InsurApplRegist insurApplRegist = new InsurApplRegist();
		List<SalesInfo> salesInfoList = new ArrayList<>();
		insurApplRegist.setSalesDevelopFlag(StringUtils.isBlank(regAcceptanceVo.getSalesDevelopFlag()) ? ""
				: regAcceptanceVo.getSalesDevelopFlag());
				// RegSgApplListVo regSgApplListVo =new RegSgApplListVo();

		// //投保单号起applNoBegin
		// regAcceptanceVo.getSgApplListVos().get(0).getApplNoBegin();
		// //投保单号止
		// regAcceptanceVo.getSgApplListVos().get(0).getApplNoEnd();
		// 投保单号
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getApplNo())) {
			insurApplRegist.setBillNo(regAcceptanceVo.getUsualAcceptVo().getApplNo());
		}
		// 申请日期
		if (regAcceptanceVo.getUsualAcceptVo().getApplDate() != null) {
			insurApplRegist.setAcceptDate(regAcceptanceVo.getUsualAcceptVo().getApplDate());
		}

		// 团险定价审批号
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getGroupPriceNo())) {
			insurApplRegist.setApproNo(regAcceptanceVo.getUsualAcceptVo().getGroupPriceNo());
		}

		// 外包录入标记
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getEntChannelFlag())) {
			insurApplRegist.setEntChanelFlag(regAcceptanceVo.getUsualAcceptVo().getEntChannelFlag());
		}

		// 被保险人数
		if (regAcceptanceVo.getUsualAcceptVo().getInsuredNum() != null) {
			insurApplRegist.setIpsnNum(regAcceptanceVo.getUsualAcceptVo().getInsuredNum());
		}

		// 投保单数量
		if (regAcceptanceVo.getUsualAcceptVo().getApplNum() != null) {
			insurApplRegist.setBillNumber(regAcceptanceVo.getUsualAcceptVo().getApplNum());

		}
		// 契约形式
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getPolicyType())
				&& regAcceptanceVo.getUsualAcceptVo().getPolicyType().equals("G")) {
			insurApplRegist.setCntrType(CNTR_TYPE.GRP_INSUR.getKey());
		} else {
			insurApplRegist.setCntrType(CNTR_TYPE.LIST_INSUR.getKey());
		}
		// 险种名称
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getPolName())) {
			insurApplRegist.setPolNameChn(regAcceptanceVo.getUsualAcceptVo().getPolName());

		}
		// 保费合计
		if (regAcceptanceVo.getUsualAcceptVo().getSumPremium() != null) {
			insurApplRegist.setSumPremium(regAcceptanceVo.getUsualAcceptVo().getSumPremium());
		}
		// 投保人/汇缴人
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getHldrName())) {
			insurApplRegist.setHldrName(regAcceptanceVo.getUsualAcceptVo().getHldrName());

		}
		// 是否共保
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getAgreementFlag())) {
			insurApplRegist.setIsCommonAgreement(regAcceptanceVo.getUsualAcceptVo().getAgreementFlag());
		}
		// 险种代码
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getPolCode())) {
			insurApplRegist.setPolCode(regAcceptanceVo.getUsualAcceptVo().getPolCode());
		}
		// 共保协议号
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getAgreementNum())) {
			insurApplRegist.setAgreementNo(regAcceptanceVo.getUsualAcceptVo().getAgreementNum());
		}
		// 币种
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getCurrencyCode())) {
			insurApplRegist.setCurrencyCode(regAcceptanceVo.getUsualAcceptVo().getCurrencyCode());
		}
		// 共享客户信息
		if (StringUtils.isNotEmpty(regAcceptanceVo.getUsualAcceptVo().getParaVal())) {
			insurApplRegist.setShareCustFlag(regAcceptanceVo.getUsualAcceptVo().getParaVal());
		}

		System.out.println("+++++++++++++++regAcceptanceVo.getSalesListFormVos().size()+++++++++++++++++"
				+ regAcceptanceVo.getSalesListFormVos().size());
		// 销售信息
		for (int i = 0; i < regAcceptanceVo.getSalesListFormVos().size(); i++) {
			SalesInfo salesInfo = new SalesInfo();
			// 有网点代理
			if (null != regAcceptanceVo.getSalesListFormVos().get(i)
					&& StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesChannelAgency())) {
				// 销售渠道
				salesInfo.setSalesChannel(regAcceptanceVo.getSalesListFormVos().get(i).getSalesChannelAgency());
				// 销售机构代码
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesBranchNoAgency())) {
					salesInfo.setSalesBranchNo(regAcceptanceVo.getSalesListFormVos().get(i).getSalesBranchNoAgency());
				}
				// 代理网点号
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getWorkSiteNoAgency())) {
					salesInfo.setSalesDeptNo(regAcceptanceVo.getSalesListFormVos().get(i).getWorkSiteNoAgency());
				}
				// 网点名称
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getWorkSiteNameAgency())) {
					salesInfo.setDeptName(regAcceptanceVo.getSalesListFormVos().get(i).getWorkSiteNameAgency());
				}
				// 销售人员代码
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesCodeAgency())) {
					salesInfo.setSalesNo(regAcceptanceVo.getSalesListFormVos().get(i).getSalesCodeAgency());
				}
				// 销售人员姓名
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesNameAgency())) {
					salesInfo.setSalesName(regAcceptanceVo.getSalesListFormVos().get(i).getSalesNameAgency());
				}
				// 代理人员代码
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getAgencyNo())) {
					salesInfo.setCommnrPsnNo(regAcceptanceVo.getSalesListFormVos().get(i).getAgencyNo());
				}
				// 代理人员姓名
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getAgencyName())) {
					salesInfo.setCommnrPsnName(regAcceptanceVo.getSalesListFormVos().get(i).getAgencyName());
				}
			}

			// 无网点代理
			if (null != regAcceptanceVo.getSalesListFormVos().get(i)
					&& StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesChannel())) {
				salesInfo.setSalesChannel(regAcceptanceVo.getSalesListFormVos().get(i).getSalesChannel());
				if ("OA".equals(regAcceptanceVo.getSalesListFormVos().get(i).getSalesChannel())) {
					// 代理网点号
					if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getWorksiteNo())) {
						salesInfo.setSalesDeptNo(regAcceptanceVo.getSalesListFormVos().get(i).getWorksiteNo());
					}
					// 网点名称
					if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getWorksiteName())) {
						salesInfo.setDeptName(regAcceptanceVo.getSalesListFormVos().get(i).getWorksiteName());
					}
				} else {
					// 销售人员代码
					if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesNo())) {
						salesInfo.setSalesNo(regAcceptanceVo.getSalesListFormVos().get(i).getSalesNo());
					}
					// 销售人员姓名
					if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesNo())) {
						salesInfo.setSalesName(regAcceptanceVo.getSalesListFormVos().get(i).getSalesName());
					}
				}
				// 销售机构代码
				if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getSalesBranchNo())) {
					salesInfo.setSalesBranchNo(regAcceptanceVo.getSalesListFormVos().get(i).getSalesBranchNo());
				}
			}

			// 销售员主副标记
			if (StringUtils.isNotEmpty(regAcceptanceVo.getSalesListFormVos().get(i).getJointFieldWorkFlag())) {
				salesInfo.setDevelMainFlag(
						StringUtils.equals("Y", regAcceptanceVo.getSalesListFormVos().get(i).getJointFieldWorkFlag())
								? "1" : "2");
			}
			// 展业比例多销售员需传
			if (null != regAcceptanceVo.getSalesListFormVos().get(i).getBusinessPct()) {
				Double developRate = regAcceptanceVo.getSalesListFormVos().get(i).getBusinessPct() / 100;
				salesInfo.setDevelopRate(developRate);
			}
			salesInfoList.add(salesInfo);
		}
		List<ApplInfo> applInfos = new ArrayList<ApplInfo>();
		if (null != regAcceptanceVo.getFilesListVos()) {
			for (int i = 0; i < regAcceptanceVo.getFilesListVos().size(); i++) {
				ApplInfo applInfo = new ApplInfo();
				applInfo.setApplInfoNumber(regAcceptanceVo.getFilesListVos().get(i).getFileNum());
				applInfo.setApplInfoType(regAcceptanceVo.getFilesListVos().get(i).getFileType());
				applInfos.add(applInfo);
			}
		}

		insurApplRegist.setSalesInfos(salesInfoList);
		System.out.println("销售员信息+++++++++++++++++++++++++++++" + salesInfoList.toString());
		insurApplRegist.setApplInfos(applInfos);
		registInfo.setInsurApplRegist(insurApplRegist);

		return registInfo;
	}
}