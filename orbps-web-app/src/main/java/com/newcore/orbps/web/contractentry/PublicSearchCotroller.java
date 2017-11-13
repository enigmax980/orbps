package com.newcore.orbps.web.contractentry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.session.Session;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplBaseInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPayInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPrintInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpProposalInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSalesListFormVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSpecialInsurAddInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpVatInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.SalesInfoVo;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryInfo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryReturnBo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpBusiPrdVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalListVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegAcceptanceVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegSalesListFormVo;
import com.newcore.orbps.models.web.vo.contractentry.registrationacceptance.RegUsualAcceptVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.cmds.api.CreateGrpCstomerAcountService;
import com.newcore.orbps.service.cmds.api.UniQueryWebServicePortType;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.service.pcms.api.WebsiteInfoService;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.orbps.web.util.SgGrpDataBoToVo;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.cxf.RouteInfo;
import com.newcore.supports.models.cxf.SecurityInfo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 公共的查询函数
 * 
 * @author 王鸿林
 *
 * @date 2016年11月16日 上午11:13:05
 * @TODO
 */
@Controller
@RequestMapping("/orbps/contractEntry/search")
public class PublicSearchCotroller {

    @Autowired
    PolicyQueryService resulpolicyQueryService;
    /**
     * 保单辅助销售人员查询服务
     */
    @Autowired
    SalesmanInfoService resulsalesmanInfoService;
    @Autowired
    WebsiteInfoService resulwebsiteInfoService;
    @Autowired
    CreateGrpCstomerAcountService restfulCreateGrpCstomerAcountService;
    /**
     * 保单基本信息查询
     */
    @Autowired
    InsurApplServer grpInsurApplServer;
    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    InsurApplServer insurApplServer;
    /**
     * 产品组 子险种查询
     */
    @Autowired
    SubPolicyService resulsubPolicyService;

    
    @Autowired
    UniQueryWebServicePortType uniQueryWebServicePortType;
    
    private static Logger logger = LoggerFactory.getLogger(PublicSearchCotroller.class);
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
        if (StringUtils.isBlank(responseVo.getBusiPrdCode())) {
            return "fail";
        }
        map.put("polCode", responseVo.getBusiPrdCode());
        Map<String, Object> maps = resulpolicyQueryService.excute(map);
        if (null == maps || maps.isEmpty() && StringUtils.equals("8888", (String) maps.get("errorCode"))) {
            return "fail";
        }
        String result = JSON.toJSONString(maps);
        String JSONpolicyBo = JSONObject.parseObject(result).getString("policyBo");
        JSONObject JSONpolicyBos = JSONObject.parseObject(JSONpolicyBo);
        String polNameChn = "";
        if (null != JSONpolicyBos) {
            polNameChn = JSONpolicyBos.getString("polNameChn");
        }
        return polNameChn == "" ? "fail" : polNameChn;
    }

    /**
     * 根据险种查询主付险标识
     * 
     * @author wanghonglin
     * @param busiCode
     * @return
     */
    @RequestMapping(value = "/searchMrCode")
    public @ResponseMessage String queryMrCode(@RequestBody String busiCode) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(busiCode)) {
            return "fail";
        }
        // 截取字符串
        busiCode = busiCode.substring(0, 3);
        map.put("polCode", busiCode);
        Map<String, Object> maps = resulpolicyQueryService.excute(map);
        if (null == maps || maps.isEmpty() && StringUtils.equals("8888", (String) maps.get("errorCode"))) {
            return "fail";
        }
        String result = JSON.toJSONString(maps);
        String JSONpolicyBo = JSONObject.parseObject(result).getString("policyBo");
        JSONObject JSONpolicyBos = JSONObject.parseObject(JSONpolicyBo);
        String mrCode = "";
        if (null != JSONpolicyBos) {
            mrCode = JSONpolicyBos.getString("mrCode");
        }
        return mrCode == "" ? "fail" : mrCode;

    }

    /**
     * 查询销售人员信息
     * 
     * @author jincong
     * @param grpApplInfoVo
     * @return
     */
    @RequestMapping(value = "/querySaleName")
    public @ResponseMessage String querySaleName(@RequestBody GrpApplInfoVo grpApplInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
        if (null != grpApplInfoVo) {
            salesmanQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
            salesmanQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
            salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
        } else {
            return "fail";
        }
        SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
        if ("1".equals(queryReturnBo.getRetCode())) {
        	if("0".equals(queryReturnBo.getSaleState())){
        		//可销售标识为0，表示该销售人员不能进行销售
        		return "fail";
        	}else{
        		return queryReturnBo.getSalesName();
        	}
        } else {
            return "fail";
        }
    }

    /**
     * 查询销售人员信息
     * 
     * @author jincong
     * @param grpApplInfoVo
     * @return
     */
    @RequestMapping(value = "/querySalesInfo")
    public @ResponseMessage SalesInfoVo querySalesInfo(@RequestBody GrpApplInfoVo grpApplInfoVo) {
    	SalesInfoVo salesInfoVo = new SalesInfoVo();
    	SalesmanQueryInfo salesmanQueryInfo=new SalesmanQueryInfo();
    	WebsiteQueryInfo websiteQueryInfo=new WebsiteQueryInfo();
    	resulsalesmanInfoService=SpringContextHolder.getBean("resulsalesmanInfoService");
    	resulwebsiteInfoService=SpringContextHolder.getBean("resulwebsiteInfoService");
    	if (null != grpApplInfoVo && !StringUtils.isEmpty(grpApplInfoVo.getSalesBranchNo()) && !StringUtils.isEmpty(grpApplInfoVo.getSalesChannel())) {
    		salesmanQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
    		websiteQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
    		salesmanQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
    		websiteQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
    	}else{
    		return salesInfoVo;
    	}
    	// 当销售信息与网点信息同时存在 以销售信息为主 网点信息只要网点name
    	if (!StringUtils.isEmpty(grpApplInfoVo.getWorksiteNo()) && !StringUtils.isEmpty(grpApplInfoVo.getSaleCode())) {
    		salesmanQueryInfo.setSalesChannel("BS");
    		salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
    		websiteQueryInfo.setSalesDeptNo(grpApplInfoVo.getWorksiteNo());
    		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
    		HeaderInfoHolder.setOutboundHeader(headerInfo);	
    		SalesmanQueryReturnBo queryReturnBo  = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
    		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
    		HeaderInfoHolder.setOutboundHeader(headerInfo1);	
    		WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
    		if (null!=websiteQuery && StringUtils.equals("1", websiteQuery.getRetCode())) {// 获得网点信息
    			salesInfoVo.setDeptName(websiteQuery.getDeptName());
    			salesInfoVo.setDeptState(websiteQuery.getDeptState());
    		}
    		if (null!=queryReturnBo && StringUtils.equals("1", queryReturnBo.getSaleState())) {// 销售员工信息
    			salesInfoVo.setProvBranchNo(queryReturnBo.getProvBranchNo());// 省级机构号
    			salesInfoVo.setMgrBranchNo(queryReturnBo.getMgrBranchNo());// 管理机构号
    			salesInfoVo.setArchiveBranchNo(queryReturnBo.getArcBranchNo());//
    			salesInfoVo.setSalesName(queryReturnBo.getSalesName()); // 销售员姓名(
    			salesInfoVo.setSalesBranchAddr(queryReturnBo.getSalesBranchAddr()); // 销售机构地址
    			salesInfoVo.setSalesBranchPostCode(queryReturnBo.getSalesBranchPostCode()); // 销售机构邮政编码
    			salesInfoVo.setCenterCode(queryReturnBo.getCenterCode()); // 成本中心(
    			salesInfoVo.setSalesBranchName(queryReturnBo.getSalesBranchName());
    			salesInfoVo.setSaleState(queryReturnBo.getSaleState());
    		}
    	// 如果网点不为空销售员工为空 信息以网点为主
    	} else if (!StringUtils.isEmpty(grpApplInfoVo.getWorksiteNo()) && StringUtils.isEmpty(grpApplInfoVo.getSaleCode())) {
    		websiteQueryInfo.setSalesDeptNo(grpApplInfoVo.getWorksiteNo());
    		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
    		HeaderInfoHolder.setOutboundHeader(headerInfo1);	
    		WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
    		if (null!=websiteQuery && StringUtils.equals("1", websiteQuery.getRetCode())) {// 获得网点信息
    			salesInfoVo.setDeptName(websiteQuery.getDeptName());
    			salesInfoVo.setProvBranchNo(websiteQuery.getProvBranchNo());// 省级机构号
    			salesInfoVo.setMgrBranchNo(websiteQuery.getMgrBranchNo());// 管理机构号
    			salesInfoVo.setSalesBranchName(websiteQuery.getSalesBranchName());
    			salesInfoVo.setSalesBranchAddr(websiteQuery.getSalesBranchAddr()); // 销售机构地址
    			salesInfoVo.setSalesBranchPostCode(websiteQuery.getSalesBranchPostCode()); // 销售机构邮政编码
    			salesInfoVo.setCenterCode(websiteQuery.getCenterCode()); // 成本中心(
    			salesInfoVo.setDeptState(websiteQuery.getDeptState());
    		}
    	} else if (StringUtils.isEmpty(grpApplInfoVo.getWorksiteNo()) && !StringUtils.isEmpty(grpApplInfoVo.getSaleCode())) {
    		salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
    		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
    		HeaderInfoHolder.setOutboundHeader(headerInfo);	
    		SalesmanQueryReturnBo queryReturnBo  = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
    		if (null!=queryReturnBo && StringUtils.equals("1", queryReturnBo.getSaleState())) {// 销售员工信息
    			salesInfoVo.setProvBranchNo(queryReturnBo.getProvBranchNo());// 省级机构号
    			salesInfoVo.setSalesName(queryReturnBo.getSalesName()); // 销售员姓名(
    			salesInfoVo.setMgrBranchNo(queryReturnBo.getMgrBranchNo());// 管理机构号
    			salesInfoVo.setArchiveBranchNo(queryReturnBo.getArcBranchNo());//
    			salesInfoVo.setSalesBranchAddr(queryReturnBo.getSalesBranchAddr()); // 销售机构地址
    			salesInfoVo.setSalesBranchPostCode(queryReturnBo.getSalesBranchPostCode()); // 销售机构邮政编码
    			salesInfoVo.setCenterCode(queryReturnBo.getCenterCode()); // 成本中心(
    			salesInfoVo.setSalesBranchName(queryReturnBo.getSalesBranchName());
    			salesInfoVo.setSaleState(queryReturnBo.getSaleState());
    		}
    	}
    	return salesInfoVo;
    }
    
    /**
     * 查询北分第二销售人员信息
     * 
     * @author jincong
     * @param grpApplInfoVo
     * @return
     */
    @RequestMapping(value = "/queryAgencyName")
    public @ResponseMessage String queryAgencyName(@RequestBody GrpApplInfoVo grpApplInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
        //判断查询条件是否为空
        if (null != grpApplInfoVo) {
            salesmanQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
            salesmanQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
            salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
            salesmanQueryInfo.setSalesDeptNo(grpApplInfoVo.getWorksiteNo());
        } else {
            return "fail";
        }
        //调用服务查询
        SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
        if ("1".equals(queryReturnBo.getRetCode())) {
        	if("0".equals(queryReturnBo.getSaleState())){
        		//可销售标识为0，表示该销售人员不能进行销售
        		return "fail";
        	}else{
        		return queryReturnBo.getSalesName();
        	}
        } else {
            return "fail";
        }
    }
    
    /**
     * 判断是否为北分环境
     * 
     * @author jincong
     * @param str
     * @return
     */
    @RequestMapping(value = "/searchClerk")
    public @ResponseMessage String searchClerk(@CurrentSession Session session, @RequestBody String str) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String result = "";
        //获取session用户信息，判断是否为北分操作员
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        str = sessionModel.getBranchNo().substring(0, 2);
        if("11".equals(str)){
        	result = "1";
        }
        return result;
    }

    /**
     * 查询网点信息
     * 
     * @author jincong
     * @param regSalesListFormVo
     * @return
     */
    @RequestMapping(value = "/queryWorksite")
    public @ResponseMessage String queryWorksite(@RequestBody RegSalesListFormVo regSalesListFormVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        WebsiteQueryInfo websiteQueryInfo = new WebsiteQueryInfo();
        //判断代理网点为自营还是中介机构，SalesAgencyFlag为Y表示中介，SalesAgencyFlag为N表示自营
        if(null != regSalesListFormVo){
        	if("Y".equals(regSalesListFormVo.getSalesAgencyFlag())){
        		//将中介网点信息作为查询条件
        		websiteQueryInfo.setSalesChannel(regSalesListFormVo.getSalesChannelAgency());
                websiteQueryInfo.setSalesBranchNo(regSalesListFormVo.getBranchNoAgency());
                websiteQueryInfo.setSalesDeptNo(regSalesListFormVo.getWorkSiteNoAgency());
            }else if("N".equals(regSalesListFormVo.getSalesAgencyFlag())){
                //将自营网点信息作为查询条件
            	websiteQueryInfo.setSalesChannel(regSalesListFormVo.getSalesChannel());
                websiteQueryInfo.setSalesBranchNo(regSalesListFormVo.getSalesBranchNo());
                websiteQueryInfo.setSalesDeptNo(regSalesListFormVo.getWorksiteNo());
            } 
        }else {
            return "fail";
        }
        
        WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
        if ("1".equals(websiteQuery.getRetCode())) {
            return websiteQuery.getDeptName();
        } else {
            return "fail";
        }
    }

    /**
     * 查询责任信息
     * 
     * @author xiaoYe
     * @param query
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/queryResponsibilityeInfo")
    public @ResponseMessage InsuredGroupModalListVo getResponseSummaryList(@RequestBody ResponseVo responseVo) {

        InsuredGroupModalListVo insuredGroupModalListVo = new InsuredGroupModalListVo();
        insuredGroupModalListVo.setErrCode("1");
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<ResponseVo> list = new ArrayList<ResponseVo>();
        PageData<ResponseVo> pageData = new PageData<ResponseVo>();
        if (responseVo.getBusiPrdCode() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("polCode", responseVo.getBusiPrdCode());
            Map<String, Object> subPolMap = resulsubPolicyService.excute(map);
            if (null != subPolMap && null == subPolMap.get("errorCode")
                    && !StringUtils.equals("8888", (String) subPolMap.get("errorCode"))) {
                JSONArray arr = (JSONArray) subPolMap.get("subPolicyBos");
                for (int i = 0; i < arr.size(); i++) {
                    ResponseVo responseVo2 = new ResponseVo();
                    String subPolCode = arr.getJSONObject(i).getString("subPolCode");
                    String subPolName = arr.getJSONObject(i).getString("subPolName");
                    Double subPolId = arr.getJSONObject(i).getDouble("subPolId");
                    responseVo2.setProductName(subPolName);
                    responseVo2.setProductCode(subPolCode);
                    responseVo2.setSubStdPremium(subPolId);
                    list.add(responseVo2);
                }
                pageData.setTotalCount(arr.size());
                pageData.setData(list);
                insuredGroupModalListVo.setInsuredGroupModalListVo(list);
            } else {
                insuredGroupModalListVo.setErrCode("0");
                insuredGroupModalListVo.setEccMsg("险种不存在");
            }

        }
        return insuredGroupModalListVo;
    }

    /**
     * 查询可编辑表格的险种信息
     * 
     * @author xiaoY
     * @param query
     * @return
     */
    @RequestMapping(value = "/queryEditPrdList")
    public @ResponseMessage DataTable<GrpBusiPrdVo> queryEditPrdList(QueryDt query, QueryinfVo queryinfVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<GrpBusiPrdVo> pageData = new PageData<GrpBusiPrdVo>();
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        String applNo = queryinfVo.getApplNo();
        GrpInsurApplVo grpInsurApplVo = new GrpInsurApplVo();
        GrpInsurApplBo grpInsurApplBo = insurApplServer.searchInsurApplByBusinessKey(applNo);

        if (grpInsurApplBo.getErrCode().equals("1")) {
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            grpInsurApplVo = this.grpinsurApplBoToVo(grpInsurAppl);
        }
        List<GrpBusiPrdVo> pagingList = new ArrayList<>();// 分页数据
        long page = query.getPage();// 第几页
        int size = query.getRows();// 每页条数
        if (null != grpInsurApplVo.getBusiPrdVos()) {
            for (int i = size * (int) page; i < size * (page + 1) && i < grpInsurApplVo.getBusiPrdVos().size(); i++) {
                pagingList.add(grpInsurApplVo.getBusiPrdVos().get(i));
            }

            // 当前页号的列表数据
            pageData.setData(pagingList);
            // 总记录数
            pageData.setTotalCount(grpInsurApplVo.getBusiPrdVos().size());
        }

        return pageQueryService.tranToDataTable(query.getRequestId(), pageData);
    }
    
    /**
     * 档案清单导入，查询投保信息
     * 
     * @author jincong
     * @param applNo
     * @return String
     */
    @RequestMapping(value = "/applQuery")
    public @ResponseMessage Map<String,String> applQuery(@RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        Map<String,String> map = new HashMap<String,String>();
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(applNo);
        if("1".equals(grpInsurApplBo.getErrCode())){
        	GrpInsurAppl grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
        	//获取保单性质
        	map.put("cntrType", grpInsurAppl.getCntrType());
        	if(grpInsurAppl.getCntrType().equals(CNTR_TYPE.GRP_INSUR.getKey())){
        		//获取团单投保单位名称
        		if(null != grpInsurAppl.getGrpHolderInfo()){
        			map.put("applName", grpInsurAppl.getGrpHolderInfo().getGrpName());
        		}
        	}else if(grpInsurAppl.getCntrType().equals(CNTR_TYPE.LIST_INSUR.getKey()) && grpInsurAppl.getSgType().equals(LIST_TYPE.GRP_SG.getKey())){
        		//获取团体汇交投保单位名称
        		if(null != grpInsurAppl.getGrpHolderInfo()){
        			map.put("applName", grpInsurAppl.getGrpHolderInfo().getGrpName());
        		}
        	}else{
        		//获取个人汇交汇交人名称
        		if(null != grpInsurAppl.getPsnListHolderInfo()){
        			map.put("applName", grpInsurAppl.getPsnListHolderInfo().getSgName());
        		}
        	}
        	//获取投保人数
    		if(null != grpInsurAppl.getApplState() && null != grpInsurAppl.getApplState().getIpsnNum()){
    			map.put("applNum", grpInsurAppl.getApplState().getIpsnNum().toString());
    		}
    		//获取生效日期	
    		if(null != grpInsurAppl.getInForceDate()){
    			map.put("applDate", DateFormatUtils.format(grpInsurAppl.getInForceDate(),"yyyy-MM-dd"));
    		}
    		//团单清单标记
    		map.put("lstProcType", grpInsurAppl.getLstProcType());
        }else{
        	map.put("errCode", grpInsurApplBo.getErrCode());
        	map.put("errMsg", grpInsurApplBo.getErrMsg());
        }
        return map;
    }
    
    private GrpInsurApplVo grpinsurApplBoToVo(GrpInsurAppl grpInsurAppl) {
        GrpInsurApplVo grpInsurApplVo = new GrpInsurApplVo();
        GrpApplInfoVo applInfoVo = new GrpApplInfoVo();
        GrpApplBaseInfoVo applBaseInfoVo = new GrpApplBaseInfoVo();
        GrpVatInfoVo vatInfoVo = new GrpVatInfoVo();
        GrpPrintInfoVo printInfoVo = new GrpPrintInfoVo();
        GrpPayInfoVo payInfoVo = new GrpPayInfoVo();
        GrpProposalInfoVo proposalInfoVo = new GrpProposalInfoVo();
        GrpSpecialInsurAddInfoVo specialInsurAddInfoVo = new GrpSpecialInsurAddInfoVo();
        List<InsuredGroupModalVo> insuredGroupModalVos = new ArrayList<>();
        List<OrganizaHierarModalVo> organizaHierarModalVos = new ArrayList<>();
        OrganizaHierarModalVo organizaHierarModalVo = new OrganizaHierarModalVo();
        List<GrpBusiPrdVo> busiPrdVos = new ArrayList<>();
        GrpBusiPrdVo busiPrdVo = new GrpBusiPrdVo();
        List<ResponseVo> responseVos = new ArrayList<>();
        ResponseVo responseVo = new ResponseVo();
        applInfoVo.setApplNo(grpInsurAppl.getApplNo());
        System.out.println(grpInsurAppl.getApplNo());
        applInfoVo.setApplDate(grpInsurAppl.getApplDate());
        applInfoVo.setAgreementNo(grpInsurAppl.getAgreementNo());
        applInfoVo.setOldApplNo(grpInsurAppl.getRenewedCgNo());
        applInfoVo.setQuotaEaNo(grpInsurAppl.getApproNo());
        applBaseInfoVo.setBusinessFlag(grpInsurAppl.getEntChannelFlag());
        applBaseInfoVo.setDisputePorcWay(grpInsurAppl.getArgueType());
        if (grpInsurAppl.getGrpHolderInfo() != null) {
            applBaseInfoVo.setCompanyName(grpInsurAppl.getGrpHolderInfo().getGrpName());
            applBaseInfoVo.setRegisterArea(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode());
            applBaseInfoVo.setIdType(grpInsurAppl.getGrpHolderInfo().getGrpIdType());
            applBaseInfoVo.setIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());
            applBaseInfoVo.setDeptType(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType());
            applBaseInfoVo.setApplNum(grpInsurAppl.getGrpHolderInfo().getIpsnNum());
            if (grpInsurAppl.getGrpHolderInfo().getAddress() != null) {
                applBaseInfoVo.setAppAddrProv(grpInsurAppl.getGrpHolderInfo().getAddress().getProvince());
                applBaseInfoVo.setAppAddrCity(grpInsurAppl.getGrpHolderInfo().getAddress().getCity());
                applBaseInfoVo.setAppAddrTown(grpInsurAppl.getGrpHolderInfo().getAddress().getCounty());
                applBaseInfoVo.setAppAddrCountry(grpInsurAppl.getGrpHolderInfo().getAddress().getTown());
                applBaseInfoVo.setAppAddrValige(grpInsurAppl.getGrpHolderInfo().getAddress().getVillage());
                applBaseInfoVo.setAppPost(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode());
            }
            applBaseInfoVo.setConnName(grpInsurAppl.getGrpHolderInfo().getContactName());
            applBaseInfoVo.setConnIdType(grpInsurAppl.getGrpHolderInfo().getContactIdType());
            applBaseInfoVo.setConnIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo());
            // applBaseInfoVo.setAppBirthday(grpInsurAppl.getGrpHolderInfo().getContactBirthDate());
            // applBaseInfoVo.setAppGender(grpInsurAppl.getGrpHolderInfo().getContactSex());
            applBaseInfoVo.setConnPhone(grpInsurAppl.getGrpHolderInfo().getContactMobile());
            applBaseInfoVo.setConnPostcode(grpInsurAppl.getGrpHolderInfo().getContactEmail());
            applBaseInfoVo.setAppHomeTel(grpInsurAppl.getGrpHolderInfo().getContactTelephone());
            applBaseInfoVo.setAppHomeFax(grpInsurAppl.getGrpHolderInfo().getFax());
            applBaseInfoVo.setOccDangerFactor(grpInsurAppl.getGrpHolderInfo().getOccClassCode());
        }
        vatInfoVo.setTaxpayerCode(grpInsurAppl.getGrpHolderInfo().getTaxpayerId());
        printInfoVo.setManualCheck(grpInsurAppl.getUdwType());
        printInfoVo.setCntrType(grpInsurAppl.getCntrType());
        printInfoVo.setPrtIpsnLstType(grpInsurAppl.getListPrintType());
        printInfoVo.setIpsnVoucherPrt(grpInsurAppl.getVoucherPrintType());
        printInfoVo.setGiftFlag(grpInsurAppl.getGiftFlag());
        printInfoVo.setExceptionInform(grpInsurAppl.getNotificaStat());
        printInfoVo.setIpsnlstId(grpInsurAppl.getLstProcType());
        printInfoVo.setApplProperty(grpInsurAppl.getInsurProperty());
        //展业信息
        List<GrpSalesListFormVo> grpSalesListFormVos = new ArrayList<GrpSalesListFormVo>();
        if (null != grpInsurAppl.getSalesInfoList()  && grpInsurAppl.getSalesInfoList() .size() > 0) {
            for (int i = 0; i < grpInsurAppl.getSalesInfoList() .size(); i++) {
                GrpSalesListFormVo grpSalesListFormVo = new GrpSalesListFormVo();
                // 销售渠道
                grpSalesListFormVo.setSalesChannel(grpInsurAppl.getSalesInfoList() .get(i).getSalesChannel());
                // 销售机构代码
                grpSalesListFormVo.setSalesBranchNo(grpInsurAppl.getSalesInfoList() .get(i).getSalesBranchNo());
                // 销售员姓名
                grpSalesListFormVo.setSaleName(grpInsurAppl.getSalesInfoList() .get(i).getSalesName());
                // 销售员工号
                grpSalesListFormVo.setSaleCode(grpInsurAppl.getSalesInfoList() .get(i).getSalesNo());
                grpSalesListFormVos.add(grpSalesListFormVo);
            }
            applInfoVo.setGrpSalesListFormVos(grpSalesListFormVos);
        }
        if (grpInsurAppl.getPaymentInfo() != null) {
            payInfoVo.setMoneyinItrvl(grpInsurAppl.getPaymentInfo().getMoneyinItrvl());
            payInfoVo.setMoneyinType(grpInsurAppl.getPaymentInfo().getMoneyinType());
            payInfoVo.setPremFrom(grpInsurAppl.getPaymentInfo().getPremSource());
            payInfoVo.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());
            payInfoVo.setBankName(grpInsurAppl.getPaymentInfo().getBankAccName());
            payInfoVo.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());
            payInfoVo.setStlType(grpInsurAppl.getPaymentInfo().getStlType());
            payInfoVo.setStlLimit(grpInsurAppl.getPaymentInfo().getStlAmnt());
            if (grpInsurAppl.getPaymentInfo().getStlDate() != null) {
                List<Date> dateList = new ArrayList<>();
                for (int i = 0; i < grpInsurAppl.getPaymentInfo().getStlDate().size(); i++) {
                    dateList.add(grpInsurAppl.getPaymentInfo().getStlDate().get(i));
                }
                payInfoVo.setSettlementDate(dateList);
            }
            proposalInfoVo.setEnstPremDeadline(grpInsurAppl.getPaymentInfo().getForeExpDate());
        }
        if (grpInsurAppl.getApplState() != null) {
            proposalInfoVo.setInForceDate(grpInsurAppl.getApplState().getDesignForceDate());
            proposalInfoVo.setIpsnNum(grpInsurAppl.getApplState().getIpsnNum());
            proposalInfoVo.setFrequenceEff(grpInsurAppl.getApplState().getIsFreForce());
            proposalInfoVo.setForceNum(grpInsurAppl.getApplState().getForceFre());
            proposalInfoVo.setForceType(grpInsurAppl.getApplState().getInforceDateType());
            proposalInfoVo.setSumPrem(grpInsurAppl.getApplState().getSumPremium());
            proposalInfoVo.setInsurDurUnit(grpInsurAppl.getApplState().getInsurDurUnit());
        }
        if (grpInsurAppl.getConventions() != null) {
            proposalInfoVo.setSpecialPro(grpInsurAppl.getConventions().getPolConv());
        }
        if (grpInsurAppl.getHealthInsurInfo() != null) {
            specialInsurAddInfoVo.setComInsurAmntUse(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse());
            specialInsurAddInfoVo.setComInsurAmntType(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType());
            specialInsurAddInfoVo.setCommPremium(grpInsurAppl.getHealthInsurInfo().getComInsrPrem());
            specialInsurAddInfoVo.setFixedComAmnt(grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt());
            specialInsurAddInfoVo.setIpsnFloatAmnt(grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt());
            specialInsurAddInfoVo.setIpsnFloatPct(grpInsurAppl.getHealthInsurInfo().getFloatInverse());
        }
        if (grpInsurAppl.getFundInsurInfo() != null) {
            specialInsurAddInfoVo.setAdminCalType(grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType());
            specialInsurAddInfoVo.setAdminPcent(grpInsurAppl.getFundInsurInfo().getAdminFeePct());
            specialInsurAddInfoVo.setIpsnAccAmnt(grpInsurAppl.getFundInsurInfo().getIpsnFundPremium());
            specialInsurAddInfoVo.setInclIpsnAccAmnt(grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt());
            specialInsurAddInfoVo.setSumPubAccAmnt(grpInsurAppl.getFundInsurInfo().getSumFundPremium());
            specialInsurAddInfoVo.setInclSumPubAccAmnt(grpInsurAppl.getFundInsurInfo().getSumFundAmnt());
        }
        if (grpInsurAppl.getConstructInsurInfo() != null) {
            specialInsurAddInfoVo.setProjectName(grpInsurAppl.getConstructInsurInfo().getIobjName());
            specialInsurAddInfoVo.setProjectAddr(grpInsurAppl.getConstructInsurInfo().getProjLoc());
            specialInsurAddInfoVo.setProjectType(grpInsurAppl.getConstructInsurInfo().getProjType());
            specialInsurAddInfoVo.setCpnstMioType(grpInsurAppl.getConstructInsurInfo().getPremCalType());
            specialInsurAddInfoVo.setTotalCost(grpInsurAppl.getConstructInsurInfo().getIobjCost());
            specialInsurAddInfoVo.setTotalArea(grpInsurAppl.getConstructInsurInfo().getIobjSize());
            specialInsurAddInfoVo.setConstructionDur(grpInsurAppl.getConstructInsurInfo().getConstructFrom());
            specialInsurAddInfoVo.setUntil(grpInsurAppl.getConstructInsurInfo().getConstructTo());
        }
        List<InsuranceInfoVo> insuranceInfoVos = new ArrayList<InsuranceInfoVo>();
        if (grpInsurAppl.getIpsnStateGrpList() != null) {
            for (int i = 0; i < grpInsurAppl.getIpsnStateGrpList().size(); i++) {
                InsuredGroupModalVo insuredGroupModalVo = new InsuredGroupModalVo();
                insuredGroupModalVo.setGenderRadio(grpInsurAppl.getIpsnStateGrpList().get(i).getGenderRadio());
                insuredGroupModalVo.setGsRate(grpInsurAppl.getIpsnStateGrpList().get(i).getGsPct());
                insuredGroupModalVo.setIpsnGrpName(grpInsurAppl.getIpsnStateGrpList().get(i).getIpsnGrpName());
                insuredGroupModalVo.setIpsnGrpNo(grpInsurAppl.getIpsnStateGrpList().get(i).getIpsnGrpNo());
                insuredGroupModalVo.setIpsnGrpNum(grpInsurAppl.getIpsnStateGrpList().get(i).getIpsnGrpNum());
                insuredGroupModalVo.setIpsnOccSubclsCode(grpInsurAppl.getIpsnStateGrpList().get(i).getOccClassCode());
                insuredGroupModalVo.setSsRate(grpInsurAppl.getIpsnStateGrpList().get(i).getSsPct());
                if (grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList() != null) {
                    for (int j = 0; j < grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().size(); j++) {
                        InsuranceInfoVo insuranceInfoVo = new InsuranceInfoVo();
                        insuranceInfoVo.setFaceAmnt(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getFaceAmnt());
                        insuranceInfoVo.setMrCode(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getMrCode());
                        insuranceInfoVo.setPolCode(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getPolCode());
                        insuranceInfoVo.setPremium(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getPremium());
                        insuranceInfoVo.setPremRate(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getPremRate());
                        insuranceInfoVo.setRecDisount(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getPremDiscount());
                        insuranceInfoVo.setStdPremium(
                                grpInsurAppl.getIpsnStateGrpList().get(i).getGrpPolicyList().get(j).getStdPremium());
                        insuranceInfoVos.add(insuranceInfoVo);
                    }
                }
                insuredGroupModalVo.setInsuranceInfoVos(insuranceInfoVos);
                insuredGroupModalVos.add(insuredGroupModalVo);
            }
        }
        if (grpInsurAppl.getOrgTreeList() != null) {
            for (int i = 0; i < grpInsurAppl.getOrgTreeList().size(); i++) {
                organizaHierarModalVo.setCustNo(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getGrpCustNo());
                organizaHierarModalVo
                        .setCompanyName(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getGrpName());
                organizaHierarModalVo
                        .setOldName(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getFormerGrpName());
                organizaHierarModalVo
                        .setUnitCharacter(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getNatureCode());
                organizaHierarModalVo.setIndustryClassification(
                        grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getOccClassCode());
                organizaHierarModalVo
                        .setTotalMembers(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getNumOfEnterprise());
                organizaHierarModalVo
                        .setDeptType(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getGrpIdType());
                organizaHierarModalVo.setIdCardNo(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getGrpIdNo());
                organizaHierarModalVo
                        .setOjEmpNum(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getOnjobStaffNum());
                organizaHierarModalVo.setApplNum(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getIpsnNum());
                organizaHierarModalVo.setProvince(
                        grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getProvince());
                organizaHierarModalVo
                        .setCity(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getCity());
                organizaHierarModalVo
                        .setCounty(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getCounty());
                organizaHierarModalVo
                        .setTown(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getTown());
                organizaHierarModalVo
                        .setVillage(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getVillage());
                organizaHierarModalVo.setDetailAddress(
                        grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getHomeAddress());
                organizaHierarModalVo.setPostCode(
                        grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getAddress().getPostCode());
                organizaHierarModalVo.setRegisteredNationality(
                        grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getGrpCountryCode());
                organizaHierarModalVo
                        .setContactName(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getContactName());
                organizaHierarModalVo
                        .setPhoneNum(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getContactMobile());
                organizaHierarModalVo
                        .setEmail(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getContactEmail());
                organizaHierarModalVo
                        .setFixedPhones(grpInsurAppl.getOrgTreeList().get(i).getGrpHolderInfo().getContactTelephone());
                organizaHierarModalVo.setPay(grpInsurAppl.getOrgTreeList().get(i).getIsPaid());
                organizaHierarModalVo.setSecurityOptions(grpInsurAppl.getOrgTreeList().get(i).getIsPaid());
                organizaHierarModalVo.setServiceAssignment(grpInsurAppl.getOrgTreeList().get(i).getServiceOpt());
                organizaHierarModalVo.setDeptType(grpInsurAppl.getOrgTreeList().get(i).getReceiptOpt());
                organizaHierarModalVos.add(organizaHierarModalVo);
            }
        }
        if (grpInsurAppl.getApplState() != null) {
            if (grpInsurAppl.getApplState().getPolicyList() != null) {
                for (int i = 0; i < grpInsurAppl.getApplState().getPolicyList().size(); i++) {
                    busiPrdVo.setBusiPrdCode(null);
                    busiPrdVo.setInsurDur(null);
                    busiPrdVo.setInsurDurUnit(null);
                    busiPrdVo.setInsuredNum(null);
                    busiPrdVo.setAmount(null);
                    busiPrdVo.setPremium(null);
                    busiPrdVo.setBusiPrdCode(grpInsurAppl.getApplState().getPolicyList().get(i).getPolCode());
                    busiPrdVo.setInsurDur(grpInsurAppl.getApplState().getPolicyList().get(i).getInsurDur());
                    busiPrdVo.setInsurDurUnit(grpInsurAppl.getApplState().getPolicyList().get(i).getInsurDurUnit());
                    busiPrdVo.setInsuredNum(grpInsurAppl.getApplState().getPolicyList().get(i).getPolIpsnNum());
                    busiPrdVo.setAmount(grpInsurAppl.getApplState().getPolicyList().get(i).getFaceAmnt());
                    busiPrdVo.setPremium(grpInsurAppl.getApplState().getPolicyList().get(i).getPremium());
                    busiPrdVos.add(busiPrdVo);
                    if (grpInsurAppl.getApplState().getPolicyList().get(i).getSubPolicyList() != null) {
                        for (int j = 0; j < grpInsurAppl.getApplState().getPolicyList().get(i).getSubPolicyList()
                                .size(); j++) {
                            responseVo.setBusiPrdCode(null);
                            responseVo.setProductCode(null);
                            responseVo.setProductNum(null);
                            responseVo.setProductPremium(null);
                            responseVo.setBusiPrdCode(grpInsurAppl.getApplState().getPolicyList().get(i).getPolCode());
                            responseVo.setProductCode(grpInsurAppl.getApplState().getPolicyList().get(i)
                                    .getSubPolicyList().get(j).getSubPolCode());
                            responseVo.setProductNum(grpInsurAppl.getApplState().getPolicyList().get(i)
                                    .getSubPolicyList().get(j).getSubPolAmnt());
                            responseVo.setProductPremium(grpInsurAppl.getApplState().getPolicyList().get(i)
                                    .getSubPolicyList().get(j).getSubPremium());
                            responseVos.add(responseVo);
                        }
                    }
                }
            }
        }
        grpInsurApplVo.setApplInfoVo(applInfoVo);
        grpInsurApplVo.setApplBaseInfoVo(applBaseInfoVo);
        grpInsurApplVo.setVatInfoVo(vatInfoVo);
        grpInsurApplVo.setPrintInfoVo(printInfoVo);
        grpInsurApplVo.setPayInfoVo(payInfoVo);
        grpInsurApplVo.setProposalInfoVo(proposalInfoVo);
        grpInsurApplVo.setSpecialInsurAddInfoVo(specialInsurAddInfoVo);
        grpInsurApplVo.setInsuredGroupModalVos(insuredGroupModalVos);
        grpInsurApplVo.setOrganizaHierarModalVos(organizaHierarModalVos);
        grpInsurApplVo.setBusiPrdVos(busiPrdVos);
        grpInsurApplVo.setResponseVos(responseVos);
        return grpInsurApplVo;
    }

    @RequestMapping(value = "/getCustNo")
    public @ResponseMessage GrpInsurApplVo getCustNo(@RequestBody GrpInsurApplVo grpInsurApplVo) {
        if (grpInsurApplVo == null) {
            throw new BusinessException("0018", "团单基本信息为空");
        }
        GrpApplInfoVo applInfoVo = grpInsurApplVo.getApplInfoVo();
        if (applInfoVo == null) {
            throw new BusinessException("0018", "投保单基本信息为空");
        }
        SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
        WebsiteQueryInfo websiteQueryInfo = new WebsiteQueryInfo();
        // websiteQueryInfo.setDeptNo(applInfoVo.getDeptNo());
        String provBranchNo = "";
        String mgrBranchNo = "";
        if (null != applInfoVo.getGrpSalesListFormVos()) {
            if(applInfoVo.getGrpSalesListFormVos().size()==1){
                salesmanQueryInfo.setSalesBranchNo(applInfoVo.getGrpSalesListFormVos().get(0).getSalesBranchNo());
                websiteQueryInfo.setSalesBranchNo(applInfoVo.getGrpSalesListFormVos().get(0).getSalesBranchNo());
                salesmanQueryInfo.setSalesChannel(applInfoVo.getGrpSalesListFormVos().get(0).getSalesChannel());
                websiteQueryInfo.setSalesChannel(applInfoVo.getGrpSalesListFormVos().get(0).getSalesChannel());
                salesmanQueryInfo.setSalesNo(applInfoVo.getGrpSalesListFormVos().get(0).getSaleCode());
                websiteQueryInfo.setSalesDeptNo(applInfoVo.getGrpSalesListFormVos().get(0).getWorksiteNo());
            }else{
                for (int i = 0; i < applInfoVo.getGrpSalesListFormVos().size(); i++) {
                    String jointFieldWorkFlag = applInfoVo.getGrpSalesListFormVos().get(i).getJointFieldWorkFlag();
                    if(jointFieldWorkFlag.equals("Y")){
                        salesmanQueryInfo.setSalesBranchNo(applInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
                        websiteQueryInfo.setSalesBranchNo(applInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
                        salesmanQueryInfo.setSalesChannel(applInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel());
                        websiteQueryInfo.setSalesChannel(applInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel());
                        salesmanQueryInfo.setSalesNo(applInfoVo.getGrpSalesListFormVos().get(i).getSaleCode());
                        websiteQueryInfo.setSalesDeptNo(applInfoVo.getGrpSalesListFormVos().get(0).getWorksiteNo());
                        break;
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(salesmanQueryInfo.getSalesNo())&&!StringUtils.isEmpty(websiteQueryInfo.getSalesDeptNo())){
        	salesmanQueryInfo.setSalesChannel("BS");
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
             HeaderInfoHolder.setOutboundHeader(headerInfo);
             SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
             if (queryReturnBo != null) {
                 provBranchNo = queryReturnBo.getProvBranchNo();// 省级机构号
                 mgrBranchNo = queryReturnBo.getMgrBranchNo();// 管理机构号
             }
        }else if (!StringUtils.isEmpty(salesmanQueryInfo.getSalesNo())) {
            CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
            if (queryReturnBo != null) {
                provBranchNo = queryReturnBo.getProvBranchNo();// 省级机构号
                mgrBranchNo = queryReturnBo.getMgrBranchNo();// 管理机构号
            }
        }else if(!StringUtils.isEmpty(websiteQueryInfo.getSalesDeptNo())){
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
			if (websiteQuery != null) {
				provBranchNo = websiteQuery.getProvBranchNo();// 省级机构号
				mgrBranchNo = websiteQuery.getMgrBranchNo();// 管理机构号
			}
         }
        if (StringUtils.isEmpty(provBranchNo) || StringUtils.isEmpty(mgrBranchNo)) {
            throw new BusinessException("0018", "省级机构号或管理机构号");
        }
        if (grpInsurApplVo.getOrganizaHierarModalVos() != null) {
            for (OrganizaHierarModalVo organizaHierarModalVo : grpInsurApplVo.getOrganizaHierarModalVos()) {
                Map<String, Object> json = new HashMap<>();
                json.put("CUST_NO", "");
                json.put("PROV_BRANCH_NO", provBranchNo);
                json.put("SRC_SYS", "ORBPS");// 系统来源
                json.put("CUST_OAC_BRANCH_NO", mgrBranchNo);// 管理机构
                json.put("NAME", organizaHierarModalVo.getCompanyName());
                json.put("OLD_NAME", "");
                json.put("ID_TYPE", organizaHierarModalVo.getDeptType());
                json.put("ID_NO", organizaHierarModalVo.getIdCardNo());
                json.put("LEGAL_CODE", "");
                json.put("NATURE_CODE", "");
                json.put("OCC_CLASS_CODE", organizaHierarModalVo.getIndustryClassification());
                json.put("CORP_REP", "");
                json.put("CONTACT_PSN", "");
                json.put("CONTACT_PSN_SEX", "");
                if (null == organizaHierarModalVo.getTotalMembers()) {
                    json.put("NUM_OF_EMP", "0");
                } else {
                    json.put("NUM_OF_EMP", organizaHierarModalVo.getTotalMembers().toString());
                }
                CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
                HeaderInfoHolder.setOutboundHeader(headerInfo);
                String retjson = restfulCreateGrpCstomerAcountService.createGrpCstomerAcount(json);
                if (retjson == null) {
                    throw new BusinessException("0018", "开户返回");
                }
                JSONObject jsonObject = JSON.parseObject(retjson);
                if (jsonObject == null) {
                    throw new BusinessException("0018", "开户返回");
                }
                String custNo = jsonObject.getString("CUST_NO");
                String partyId = jsonObject.getString("PARTY_ID");
                organizaHierarModalVo.setCustNo(custNo);
                organizaHierarModalVo.setPartyId(partyId);
            }
        }
        return grpInsurApplVo;
    }

    /**
     * 查询销售人员相关信息
     * 
     * @author chang
     * @param
     * @return
     */
    @RequestMapping(value = "/querySlaes")
    public @ResponseMessage RegAcceptanceVo getSalesQueryList(@RequestBody RegUsualAcceptVo regUsualAcceptVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String applNo = regUsualAcceptVo.getApplNo();
        // 调用后台服务
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(applNo);
        // 大Vo
        RegAcceptanceVo regAcceptanceVo = new RegAcceptanceVo();
        regAcceptanceVo = this.salesBoToVo(grpInsurApplBo);
        return regAcceptanceVo;
    }

    private RegAcceptanceVo salesBoToVo(GrpInsurApplBo grpInsurApplBo) {
        RegAcceptanceVo regAcceptanceVo = new RegAcceptanceVo();
        // 展业信息
        RegSalesListFormVo regSalesListFormVo = new RegSalesListFormVo();
        // 保存展业信息
        List<RegSalesListFormVo> salesInfo = new ArrayList<RegSalesListFormVo>();
        if(null != grpInsurApplBo.getGrpInsurAppl()){
            if (null != grpInsurApplBo.getGrpInsurAppl().getSalesInfoList()
                    && grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().size() >= 0) {
                for (int i = 0; i < grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().size(); i++) {
                    regSalesListFormVo = new RegSalesListFormVo();
                    // 展业比例
                    regSalesListFormVo
                            .setBusinessPct(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getDevelopRate());
                    // 销售渠道
                    regSalesListFormVo
                            .setSalesChannel(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getSalesChannel());
                    // 销售机构代码
                    regSalesListFormVo.setSalesBranchNo(
                            grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getSalesBranchNo());
                    // 销售员姓名
                    regSalesListFormVo
                            .setSalesName(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getSalesName());
                    // 销售员工号
                    regSalesListFormVo.setSalesNo(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getSalesNo());
                    // 代理网点号
                    regSalesListFormVo
                            .setWorksiteNo(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getSalesDeptNo());
                    // 网点名称
                    regSalesListFormVo
                            .setWorksiteName(grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getDeptName());
                    // 销售员主副标记
                    regSalesListFormVo.setJointFieldWorkFlag(StringUtils.equals("1",
                            grpInsurApplBo.getGrpInsurAppl().getSalesInfoList().get(i).getDevelMainFlag()) ? "Y" : "N");
                    // 展业比例多销售员需传
                    // Double businessPct =
                    // insurAppl.getGrpInsurAppl().getSalesInfoList().get(i).getDevelopRate()*100;
                    // regSalesListFormVo.setBusinessPct(businessPct);
                    salesInfo.add(regSalesListFormVo);
                }
                regAcceptanceVo.setSalesListFormVos(salesInfo);
            }
        }
        // 放入大Vo中
        return regAcceptanceVo;
    }
    
    @RequestMapping(value = "/searGrpInfo")
    public @ResponseMessage GrpInsurApplVo searGrpInfo(@RequestBody String applNo) {
    	 GrpInsurApplVo regrpInsurApplVo =null;
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
    	headerInfo.setOrginSystem("ORBPS");					
		headerInfo.setServiceCode("CMDS-1008");
		headerInfo.setServiceName("UniQueryWebService");	
		headerInfo.setServiceVersion("1.0");
		
		RouteInfo routeInfo = new RouteInfo();
		routeInfo.setBranchNo("000000");
		routeInfo.setDestSystem("CMDS");
		headerInfo.setRouteInfo(routeInfo);
		
		SecurityInfo securityInfo=new SecurityInfo();
		securityInfo.setSignature("10");
		headerInfo.setSecurityInfo(securityInfo);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String str="<ROOT>"+
				"<HEADER>"+
					"<TRANS_NO>111111111</TRANS_NO>"+
				"<SERV_CODE>301</SERV_CODE>"+
				"<USERNAME>huluwa</USERNAME>"+
					"<PASSWORD>tanglaoya</PASSWORD>"+
				"</HEADER>"+
				"<BODY>"+
					"<PARAM>"+
						"<ROUTER>"+
							"<SYS_NO>2</SYS_NO>"+
							"<BRANCH_NO>320000</BRANCH_NO>"+
						"</ROUTER>"+
						"<CONDITION>"+
							"<CNTR_NO>"+applNo+"</CNTR_NO>"+
							"<END>100</END>"+
							"<BEGIN>1</BEGIN>"+
						"</CONDITION>"+
					"</PARAM>"+
				"</BODY>"+
			"</ROOT>";
        String resultStr = uniQueryWebServicePortType.getQueryResult(str);
        if(StringUtils.isBlank(resultStr)){
         	throw new BusinessException("0018","查询数据为空");
         }
        GrpInsurAppl grpInsurAppl =readXml(resultStr);
        GrpDataBoToVo BoToVo = new GrpDataBoToVo();		
        regrpInsurApplVo = BoToVo.grpinsurApplBoToVo(grpInsurAppl);
		return regrpInsurApplVo;
    }
    
    
    @RequestMapping(value = "/searSgGrpInfo")
    public @ResponseMessage SgGrpInsurApplVo searSgGrpInfo(@RequestBody String applNo) {
    	 SgGrpInsurApplVo regrpInsurApplVo =null;
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
    	headerInfo.setOrginSystem("ORBPS");					
		headerInfo.setServiceCode("CMDS-1008");
		headerInfo.setServiceName("UniQueryWebService");	
		headerInfo.setServiceVersion("1.0");
		
		RouteInfo routeInfo = new RouteInfo();
		routeInfo.setBranchNo("000000");
		routeInfo.setDestSystem("CMDS");
		headerInfo.setRouteInfo(routeInfo);
		
		SecurityInfo securityInfo=new SecurityInfo();
		securityInfo.setSignature("10");
		headerInfo.setSecurityInfo(securityInfo);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String str="<ROOT>"+
				"<HEADER>"+
					"<TRANS_NO>111111111</TRANS_NO>"+
				"<SERV_CODE>301</SERV_CODE>"+
				"<USERNAME>huluwa</USERNAME>"+
					"<PASSWORD>tanglaoya</PASSWORD>"+
				"</HEADER>"+
				"<BODY>"+
					"<PARAM>"+
						"<ROUTER>"+
							"<SYS_NO>2</SYS_NO>"+
							"<BRANCH_NO>320000</BRANCH_NO>"+
						"</ROUTER>"+
						"<CONDITION>"+
							"<CNTR_NO>"+applNo+"</CNTR_NO>"+
							"<END>100</END>"+
							"<BEGIN>1</BEGIN>"+
						"</CONDITION>"+
					"</PARAM>"+
				"</BODY>"+
			"</ROOT>";
        String resultStr = uniQueryWebServicePortType.getQueryResult(str);
        if(StringUtils.isBlank(resultStr)){
         	throw new BusinessException("0018","查询数据为空");
         }
        GrpInsurAppl grpInsurAppl =readXml(resultStr);
        SgGrpDataBoToVo BoToVo = new SgGrpDataBoToVo();
		regrpInsurApplVo = BoToVo.sgGrpInsurApplBoToVo(grpInsurAppl);
		return regrpInsurApplVo;
    }
    
    public static GrpInsurAppl readXml(String resultStr){
    	Document document=null;
    	try {
			document = DocumentHelper.parseText(resultStr);
		} catch (DocumentException e) {
			logger.error("读取xml文件异常",e);
			throw new BusinessException("0023");
		}  
    	Element root = document.getRootElement();
		Element ResultBean=root.element("ResultBean");
		if(StringUtils.equals("false",ResultBean.element("STATUS").getText())){
			throw new BusinessException("0018","查询数据失败");
			}
		Element Data=ResultBean.element("Data");
		Element grpCntrStdContractList = Data.element("Grp_cntr_std_contract_list");
		//Element grCntrInsureds = Data.element("Grp_cntr_insured_list");
		Element grpCntrHolders = Data.element("Grp_cntr_holder_list");
		Element grpCntrSubStateSums = Data.element("Grp_cntr_sub_state_sum_list");
		Element grpCntrBasicStates = Data.element("Grp_cntr_basic_state_list");
		Element grpCntrSubStates = Data.element("Grp_cntr_sub_state_list");
		Element grpCntrInsuredSums = Data.element("Grp_cntr_insured_sum");
		GrpInsurAppl grpInsurAppl=new GrpInsurAppl();   //保存团单信息
		ApplState applState=new ApplState();      		//保存要约信息
		PaymentInfo paymentInfo=new PaymentInfo();      //交费相关
		List<Policy> policyList=new ArrayList<>();      //险种(子要约保存)
		List<SalesInfo> salesInfoList=new ArrayList<>();  //销售信息
		SalesInfo salesInfo=new SalesInfo();
		GrpHolderInfo grpHolderInfo=new GrpHolderInfo();
		PsnListHolderInfo psnListHolderInfo=new PsnListHolderInfo();
		//保单基本信息列表录入
		for(Iterator it=grpCntrStdContractList.elementIterator();it.hasNext();){
			 Element element = (Element) it.next();
			 for (Iterator j = element.elementIterator(); j.hasNext();) {
				 Element jele = (Element) j.next();
				 if(StringUtils.equals("CNTR_FROM",jele.getName()))grpInsurAppl.setAccessSource(StringUtils.isBlank(jele.getText())?"":jele.getText());//保单来源
				 if(StringUtils.equals("APPL_NO",jele.getName()))grpInsurAppl.setApplNo(StringUtils.isBlank(jele.getText())?"":jele.getText());	//保单号
				 if(StringUtils.equals("ORIGINAL_CNTR_NO",jele.getName()))grpInsurAppl.setRenewedCgNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//老保单号
				 if(StringUtils.equals("SG_NO",jele.getName()))grpInsurAppl.setSgNo(StringUtils.isBlank(StringUtils.isBlank(jele.getText())?"":jele.getText())?"":StringUtils.isBlank(jele.getText())?"":jele.getText());//汇缴件号
				 if(StringUtils.equals("CG_NO",jele.getName()))grpInsurAppl.setCgNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//合同组号
				 if(StringUtils.equals("CNTR_TYPE",jele.getName()))grpInsurAppl.setCntrType(StringUtils.isBlank(jele.getText())?"":jele.getText());//保单类型
				 if(StringUtils.equals("APPL_DATE",jele.getName()))grpInsurAppl.setApplDate(strToDate(StringUtils.isBlank(jele.getText())?"":jele.getText())); //投保日期
			     if(StringUtils.equals("IN_FORCE_DATE",jele.getName()))grpInsurAppl.setInForceDate(strToDate(StringUtils.isBlank(jele.getText())?"":jele.getText()));//生效日期
				 if(StringUtils.equals("SIGN_DATE",jele.getName()))grpInsurAppl.setSignDate(strToDate(StringUtils.isBlank(jele.getText())?"":jele.getText()));//签单日期
				 if(StringUtils.equals("CNTR_TERM_DATE",jele.getName()))grpInsurAppl.setCntrExpiryDate(strToDate(StringUtils.isBlank(jele.getText())?"":jele.getText()));//合同终止日期
				 if(StringUtils.equals("MGR_BRANCH_NO",jele.getName()))grpInsurAppl.setMgrBranchNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//管理机构
				 if(StringUtils.equals("RENEW_TIMES",jele.getName()))grpInsurAppl.setRenewTimes(StringUtils.isBlank(jele.getText())?null:Long.parseLong(jele.getText()));//续保次数
				 //要约相关Long.parseLong(str)
				 if(StringUtils.equals("SALES_CHANNEL",jele.getName()))salesInfo.setSalesChannel(StringUtils.isBlank(jele.getText())?"":jele.getText());  //销售渠道
				 if(StringUtils.equals("N_SALES_BRANCH_NO",jele.getName()))salesInfo.setSalesBranchNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//现代理人销售机构
				 if(StringUtils.equals("N_SALES_CODE",jele.getName()))salesInfo.setSalesNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//现销售人员
				 //缴费相关
				 if(StringUtils.equals("MONEYIN_ITRVL",jele.getName()))paymentInfo.setMoneyinItrvl(StringUtils.isBlank(jele.getText())?"":jele.getText());//缴费方式
				 if(StringUtils.equals("MONEYIN_TYPE",jele.getName()))paymentInfo.setMoneyinType(StringUtils.isBlank(jele.getText())?"":jele.getText());	//缴费类型
				 if(StringUtils.equals("CURRENCY_CODE",jele.getName()))paymentInfo.setCurrencyCode(StringUtils.isBlank(jele.getText())?"":jele.getText());	//币种
				 if(StringUtils.equals("BANK_CODE",jele.getName()))paymentInfo.setBankCode(StringUtils.isBlank(jele.getText())?"":jele.getText());//      银行代码
				 if(StringUtils.equals("BANK_NAME",jele.getName()))paymentInfo.setBankAccName(StringUtils.isBlank(jele.getText())?"":jele.getText());//   银行名称
				 if(StringUtils.equals("BANK_ACC_NO",jele.getName()))paymentInfo.setBankAccNo(StringUtils.isBlank(jele.getText())?"":jele.getText());//   银行帐号
			 }
		}
		 for(Iterator it=grpCntrHolders.elementIterator();it.hasNext();){
			 	Element element = (Element) it.next();
             for (Iterator j = element.elementIterator(); j.hasNext();) {
				Element jele = (Element) j.next();
				if(StringUtils.equals("NAME",jele.getName()))psnListHolderInfo.setSgName(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("ID_TYPE",jele.getName()))psnListHolderInfo.setSgIdType(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("ID_NO",jele.getName()))psnListHolderInfo.setSgIdNo(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("HLDR_CUST_NO",jele.getName()))grpHolderInfo.setGrpCustNo(StringUtils.isBlank(jele.getText())?"":jele.getText());
				if(StringUtils.equals("LEGAL_CODE",jele.getName()))grpHolderInfo.setLegalCode(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("NATURE_CODE",jele.getName()))grpHolderInfo.setNatureCode(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("CONTACT_PSN",jele.getName()))grpHolderInfo.setContactName(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
				if(StringUtils.equals("NUM_OF_EMP",jele.getName()))grpHolderInfo.setNumOfEnterprise(StringUtils.isBlank(jele.getText())?null:Long.parseLong(jele.getText())); 
				if(StringUtils.equals("CORP_REP",jele.getName()))grpHolderInfo.setCorpRep(StringUtils.isBlank(jele.getText())?"":jele.getText()); 
          }
		}
		
		 for(Iterator it=grpCntrSubStateSums.elementIterator();it.hasNext();){
			  Element element = (Element) it.next();
                for (Iterator j = element.elementIterator(); j.hasNext();) {
					Element jele = (Element) j.next();
					if(StringUtils.equals("SUM_PREMIUM",jele.getName())) applState.setSumPremium(StringUtils.isBlank(jele.getText())?null:Double.parseDouble(jele.getText()));
					if(StringUtils.equals("SUM_FACE_AMNT",jele.getName())) applState.setSumFaceAmnt(StringUtils.isBlank(jele.getText())?null:Double.parseDouble(jele.getText()));
                }
		  }
		 for(Iterator it=grpCntrBasicStates.elementIterator();it.hasNext();){
			  Element element = (Element) it.next();
                for (Iterator j = element.elementIterator(); j.hasNext();) {
					Element jele = (Element) j.next();
					if(StringUtils.equals("INSUR_DUR_UNIT",jele.getName()))applState.setInsurDurUnit(StringUtils.isBlank(jele.getText())?"":jele.getText());
					if(StringUtils.equals("INSUR_DUR",jele.getName()))applState.setInsurDur(StringUtils.isBlank(jele.getText())?null:Integer.parseInt(jele.getText()));
					if(StringUtils.equals("MONEYIN_DUR_UNIT",jele.getName()))paymentInfo.setMoneyinDurUnit(StringUtils.isBlank(jele.getText())?"":jele.getText());
					if(StringUtils.equals("MONEYIN_DUR",jele.getName()))paymentInfo.setMoneyinDur(StringUtils.isBlank(jele.getText())?null:Integer.parseInt(jele.getText()));
                }
		 }
		 for(Iterator it=grpCntrSubStates.elementIterator();it.hasNext();){
			 Policy policy=new Policy();
			  Element element = (Element) it.next();
                for (Iterator j = element.elementIterator(); j.hasNext();) {
					Element jele = (Element) j.next();
					if(StringUtils.equals("PREMIUM",jele.getName()))policy.setPremium(StringUtils.isBlank(jele.getText())?null:Double.parseDouble(jele.getText()));
					if(StringUtils.equals("STD_PREMIUM",jele.getName()))policy.setStdPremium(StringUtils.isBlank(jele.getText())?null:Double.parseDouble(jele.getText()));
					if(StringUtils.equals("POL_CODE",jele.getName()))policy.setPolCode(StringUtils.isBlank(jele.getText())?"":jele.getText());
					if(StringUtils.equals("POL_CODE_CHN",jele.getName()))policy.setPolNameChn(StringUtils.isBlank(jele.getText())?"":jele.getText());
                }
                policyList.add(policy);
		 }
		 Element insuredCount = grpCntrInsuredSums.element("INSURED_COUNT");
		 applState.setIpsnNum(StringUtils.isBlank(insuredCount.getText())?null:Long.parseLong(insuredCount.getText()));
		
			applState.setPolicyList(policyList);
			salesInfoList.add(salesInfo);
			grpInsurAppl.setApplState(applState);
			grpInsurAppl.setPaymentInfo(paymentInfo);
			grpInsurAppl.setSalesInfoList(salesInfoList);
			grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
		  return grpInsurAppl;
    	
    }
    private static Date strToDate(String str){
		Date date=null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringUtils.isBlank(str)){
				date=df.parse(str);
			}
		} catch (ParseException e) {
			logger.error("转换时间类型异常",e);
			throw new BusinessException("转换时间类型异常");
		}
		return date;
	}
    
}

