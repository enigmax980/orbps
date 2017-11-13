package com.newcore.orbps.web.otherfunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.CommonAgreementRuleVo;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.CommonAgreementVo;
import com.newcore.orbps.service.api.CommonQueryService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
@Controller
@RequestMapping("/orbps/otherfunction/poolQuery")
public class CommonAgreementController {
	
	@Autowired
	CommonQueryService commonQueryService;
	@Autowired
	PageQueryService pageQueryService;
	@Autowired
	BranchService branchService;
	
	@RequestMapping(value = "/search")
	public @ResponseMessage DataTable<CommonAgreementVo> getCommonAgreementResult(
	        @CurrentSession Session session,QueryDt query,  CommonAgreementRuleVo commonAgreementRuleVo) throws Exception {
		
		String branchStr2 = "";
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String branchStr1 = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = commonAgreementRuleVo.getSalesBranchNo();
        Map<String, Object> map = new HashMap<String, Object>();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isBlank(mgrBranchNo)){
        	map.put("mgrBranchNo", branchStr1);
        }else{
            //销售机构条件控制
            CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            branchStr2 = branchService.findProvinceBranch(mgrBranchNo);
            if (!branchStr1.equals(branchStr2)) {
                throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省机构的保单");
            }
            map.put("mgrBranchNo", mgrBranchNo);
        }
        if(StringUtils.isBlank(commonAgreementRuleVo.getFindSubBranchNoFlag())){
        	map.put("findSubBranchNoFlag", "Y");
        }
		
		// 投保单号
		if (!StringUtils.isBlank(commonAgreementRuleVo.getApplNo())) {
			map.put("applNo", commonAgreementRuleVo.getApplNo());
		}
		// 协议号
		if (!StringUtils.isBlank(commonAgreementRuleVo.getAgreementNo())) {
			map.put("agreementNo", commonAgreementRuleVo.getAgreementNo());
		}
		// 协议名称
		if (!StringUtils.isBlank(commonAgreementRuleVo.getAgreementName())) {
			map.put("agreementName", commonAgreementRuleVo.getAgreementName());
		}
		// 协议创建起期
		//将后台的日期字符串转成日期送给后台。
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isBlank(commonAgreementRuleVo.getInForceDate() )) {
			map.put("inForceDate", sdf.parse(commonAgreementRuleVo.getInForceDate()));
		}
		// 协议创建止期
		if (!StringUtils.isBlank(commonAgreementRuleVo.getTermDate())) {
			map.put("termDate", sdf.parse(commonAgreementRuleVo.getTermDate()));
		}
			int page = Integer.valueOf(String.valueOf(query.getPage()));
			int size = query.getRows();
			page = (page/size)+1;
			map.put("page",page);
			map.put("size", query.getRows());

		// 调用后台commonQueryService.commonAgreementQuery方法。
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
		PageData<CommonAgreement> CommonAgreementList = commonQueryService.commonAgreementQuery(map);
		// 创建PageData。
		PageData<CommonAgreementVo> pageData = new PageData<CommonAgreementVo>();
		if ( null != CommonAgreementList && CommonAgreementList.getTotalCount() != 0) {
			// 创建arrayList，接收数据。
			List<CommonAgreementVo> arrayList = new ArrayList<>();
			// yyyy-MM-dd HH:mm
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//yyyy-MM-dd
			SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
			for (CommonAgreement  CommonAgreement:CommonAgreementList.getData()) {
				CommonAgreementVo commonAgreementVo = new CommonAgreementVo();
				//机构代码
				commonAgreementVo.setPclkBranchNo(CommonAgreement.getPclkBranchNo());
				//协议号
				commonAgreementVo.setAgreementNo(CommonAgreement.getAgreementNo());
				//协议号名称
				commonAgreementVo.setAgreementName(CommonAgreement.getAgreementName());
				//取到共保协议公司基本信息组信息，遍历 判断是否是本公司标志，是就把公司名字放到返回Vo里
				List<ComCompany> list = CommonAgreement.getComCompanies();
				if(list != null){
					for(ComCompany comCompany : list){
						if("Y".equals(comCompany.getCompanyFlag())){
							//本公司名字
							commonAgreementVo.setCompanyName(comCompany.getCompanyName());
							//本公司共保身份
							commonAgreementVo.setCoinsurType(comCompany.getCoinsurType());
							//本公司共保份额
							commonAgreementVo.setCoinsurResponsePct(comCompany.getCoinsurAmntPct());
						}
					}
				}
				//协议有效起期
				if(CommonAgreement.getInForceDate() != null){
					commonAgreementVo.setInForceDate(simpleDateFormats.format(CommonAgreement.getInForceDate()));
				}
				//协议有效止期
				if(CommonAgreement.getTermDate() != null){
					commonAgreementVo.setTermDate(simpleDateFormats.format(CommonAgreement.getTermDate()));
				}
				//协议审核日期
				if(CommonAgreement.getSignDate() != null){
					commonAgreementVo.setSignDate(simpleDateFormat.format(CommonAgreement.getSignDate()));
				}
				//协议创建人
				commonAgreementVo.setClerkNo(CommonAgreement.getClerkNo());
				//协议备注
				commonAgreementVo.setRemark(CommonAgreement.getRemark());
				//协议审核员工号
				commonAgreementVo.setVclkNo(CommonAgreement.getVclkNo());
				//协议基本信息
				//共保承保信息
				arrayList.add(commonAgreementVo);
			}
			// 当前页号的列表数据
			pageData.setData(arrayList);
			// 总记录数
			pageData.setTotalCount(CommonAgreementList.getTotalCount());
		}
		CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo2);
		return pageQueryService.tranToDataTable(query.getRequestId(), pageData);
	}
	@RequestMapping(value = "/outExcel")
	public @ResponseMessage List<CommonAgreementVo>  OutExcel(@CurrentSession Session session,@RequestBody CommonAgreementRuleVo commonAgreementRuleVo) throws ParseException{
		String branchStr2 = "";
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String branchStr1 = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = commonAgreementRuleVo.getSalesBranchNo();
		Map<String, Object> map = new HashMap<String, Object>();
		// 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isBlank(mgrBranchNo)){
        	map.put("mgrBranchNo", branchStr1);
        }else{
            //销售机构条件控制
            CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            branchStr2 = branchService.findProvinceBranch(mgrBranchNo);
            if (!branchStr1.equals(branchStr2)) {
                throw new BusinessException("0004", "导出失败，该操作员只能查看并操作本省机构的保单");
            }
            map.put("mgrBranchNo", mgrBranchNo);
        }
        if(StringUtils.isBlank(commonAgreementRuleVo.getFindSubBranchNoFlag())){
        	map.put("findSubBranchNoFlag", "Y");
        }
		// 投保单号
		if (!StringUtils.isBlank(commonAgreementRuleVo.getApplNo())) {
			map.put("applNo", commonAgreementRuleVo.getApplNo());
		}
		// 协议号
		if (!StringUtils.isBlank(commonAgreementRuleVo.getAgreementNo())) {
			map.put("agreementNo", commonAgreementRuleVo.getAgreementNo());
		}
		// 协议名称
		if (!StringUtils.isBlank(commonAgreementRuleVo.getAgreementName())) {
			map.put("agreementName", commonAgreementRuleVo.getAgreementName());
		}
		// 协议创建起期
		//将后台的日期字符串转成日期送给后台。
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isBlank(commonAgreementRuleVo.getInForceDate() )) {
			map.put("inForceDate", sdf.parse(commonAgreementRuleVo.getInForceDate()));
		}
		// 协议创建止期
		if (!StringUtils.isBlank(commonAgreementRuleVo.getTermDate())) {
			map.put("termDate", sdf.parse(commonAgreementRuleVo.getTermDate()));
		}
		// 调用后台commonQueryService.commonAgreementQuery方法。
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo1);
		PageData<CommonAgreement> CommonAgreementList = commonQueryService.commonAgreementQuery(map);
		// 创建arrayList，接收数据。
		List<CommonAgreementVo> arrayList = new ArrayList<>();
		if ( null != CommonAgreementList && CommonAgreementList.getTotalCount() != 0) {
			// yyyy-MM-dd HH:mm
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//yyyy-MM-dd
			SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
			for (CommonAgreement  CommonAgreement:CommonAgreementList.getData()) {
				CommonAgreementVo commonAgreementVo = new CommonAgreementVo();
				//机构代码
				commonAgreementVo.setPclkBranchNo(CommonAgreement.getPclkBranchNo());
				//协议号
				commonAgreementVo.setAgreementNo(CommonAgreement.getAgreementNo());
				//协议号名称
				commonAgreementVo.setAgreementName(CommonAgreement.getAgreementName());
				//取到共保协议公司基本信息组信息，遍历 判断是否是本公司标志，是就把公司名字放到返回Vo里
				List<ComCompany> list = CommonAgreement.getComCompanies();
				if(list != null){
					for(ComCompany comCompany : list){
						if("Y".equals(comCompany.getCompanyFlag())){
							//本公司名字
							commonAgreementVo.setCompanyName(comCompany.getCompanyName());
							//本公司共保身份
							commonAgreementVo.setCoinsurType(comCompany.getCoinsurType());
							//本公司共保份额
							commonAgreementVo.setCoinsurResponsePct(comCompany.getCoinsurAmntPct());
						}
					}
				}
				//协议有效起期
				if(CommonAgreement.getInForceDate() != null){
					commonAgreementVo.setInForceDate(simpleDateFormats.format(CommonAgreement.getInForceDate()));
				}
				//协议有效止期
				if(CommonAgreement.getTermDate() != null){
					commonAgreementVo.setTermDate(simpleDateFormats.format(CommonAgreement.getTermDate()));
				}
				//协议审核日期
				if(CommonAgreement.getSignDate() != null){
					commonAgreementVo.setSignDate(simpleDateFormat.format(CommonAgreement.getSignDate()));
				}
				//协议创建人
				commonAgreementVo.setClerkNo(CommonAgreement.getClerkNo());
				//协议备注
				commonAgreementVo.setRemark(CommonAgreement.getRemark());
				//协议审核员工号
				commonAgreementVo.setVclkNo(CommonAgreement.getVclkNo());
				//协议基本信息
				//共保承保信息
				arrayList.add(commonAgreementVo);
			}
		}
		return arrayList;
	}
}
