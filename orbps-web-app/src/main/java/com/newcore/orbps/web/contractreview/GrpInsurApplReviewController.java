package com.newcore.orbps.web.contractreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
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
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.orbps.web.util.GrpDataVoToBo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;

/**
 * 团单控制
 * 
 * @author xiaoye
 *
 */
@Controller
@RequestMapping("/orbps/contractReview/grp")
public class GrpInsurApplReviewController {

	@Autowired
	PolicyQueryService resulpolicyQueryService;
    @Autowired
    InsurApplServices insurApplServices;
	@Autowired
	SalesmanInfoService resulsalesmanInfoService;
    @Autowired
    InsurApplServer grpInsurApplServer;
    /**
     * 产品组 子险种查询
     */
    @Autowired
    SubPolicyService resulsubPolicyService;
    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;
    
    /**
     * 查询责任信息
     * 
     * @author xiaoYe
     * @param query
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/search")
    public @ResponseMessage DataTable<ResponseVo> getResponseSummaryList(QueryDt queryDt,ResponseVo responseVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<ResponseVo> list = new ArrayList<ResponseVo>();
        PageData<ResponseVo> pageData = new PageData<ResponseVo>();
        if(responseVo.getBusiPrdCode()!=null){
            Map<String, Object> map=new HashMap<>();
            map.put("polCode", responseVo.getBusiPrdCode());
            Map<String, Object> subPolMap = resulsubPolicyService.excute(map);
            if(null!=subPolMap&&!subPolMap.isEmpty()&&null==map.get("errorCode")&&!StringUtils.equals("8888",(String)map.get("errorCode"))){
            	   JSONArray arr = (JSONArray)subPolMap.get("subPolicyBos");
                   for(int i=0;i<arr.size();i++){
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
            }else{
            	throw new BusinessException("险种不存在");
            }
  
        }
        return pageQueryService.tranToDataTable(6, pageData);
    }
    
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
         headerInfo.setOrginSystem("ORBPS");
         headerInfo.getRouteInfo().setBranchNo("1");
         HeaderInfoHolder.setOutboundHeader(headerInfo);       
    
         Map<String, Object> map=new HashMap<>();
         map.put("polCode", responseVo.getBusiPrdCode());
         Map<String, Object> maps = resulpolicyQueryService.excute(map);
         String result=JSON.toJSONString(maps);
         JSONObject  objSubPol = JSONObject.parseObject(result);      
         String JSONpolicyBo = JSONObject.parseObject(result).getString("policyBo");        
         JSONObject JSONpolicyBos = JSONObject.parseObject(JSONpolicyBo);        
         String polNameChn = JSONpolicyBos.getString("polNameChn");       
         return polNameChn;
    }
    
    /**
     * 提交团单信息
     * 
     * @author xiaoye
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo grpInsurAppl(@CurrentSession Session session,@RequestBody GrpInsurApplVo grpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        grpInsurApplVo.setAccessChannel("ORBPS");
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        grpInsurApplVo.getTaskInfo().setBizNo(grpInsurApplVo.getApplInfoVo().getApplNo());
        grpInsurApplVo.getTaskInfo().setOperBranchNo(sessionModel.getBranchNo());
        grpInsurApplVo.getTaskInfo().setOperClerkName(sessionModel.getName());
        grpInsurApplVo.getTaskInfo().setOperClerkNo(sessionModel.getClerkNo());
        grpInsurApplVo.getTaskInfo().setOperState(grpInsurApplVo.getApprovalState());
        GrpDataVoToBo VoToBo = new GrpDataVoToBo();
        GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
        grpInsurApplPara.setGrpInsurAppl(VoToBo.grpinsurApplVoToBo(grpInsurApplVo));
        grpInsurApplPara.setTaskId(grpInsurApplVo.getTaskInfo().getTaskId());
        TraceNode traceNode = new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(grpInsurApplVo.getApprovalState());//团单复合通过
        grpInsurApplPara.setTraceNode(traceNode);
        RetInfo result = grpInsurApplServer.infoCheck(grpInsurApplPara);	
        System.out.println();
        return result;
    }

    /**
     * 查询投保单信息
     * 
     * @author xiaoye
     * @param query
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/query")
    public @ResponseMessage GrpInsurApplVo search(@RequestBody TaskInfo taskInfo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        grpInsurApplServer = SpringContextHolder.getBean("grpInsurApplServer");
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
        GrpInsurApplVo regrpInsurApplVo = new GrpInsurApplVo();
        if("1".equals(grpInsurApplBo.getErrCode())){
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            GrpDataBoToVo BoToVo = new GrpDataBoToVo();
            regrpInsurApplVo = BoToVo.grpinsurApplBoToVo(grpInsurAppl);
            regrpInsurApplVo.setTaskInfo(taskInfo);
        }
        return regrpInsurApplVo;
    }
    
    /**
     * 查询销售人员信息
     * 
     * @author jincong
     * @param query
     * @return
     */
    @RequestMapping(value = "/querySaleName")
    public @ResponseMessage String querySaleName(@RequestBody GrpApplInfoVo grpApplInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SalesmanQueryInfo salesmanQueryInfo=null;
        String salesmanStr="";      //保存销售人员返回json串
        String salesmanString="";   //保存销售人员入参json串
        if(grpApplInfoVo.getSalesBranchNo()!=null&&grpApplInfoVo.getSaleCode()!=null&&grpApplInfoVo.getSalesChannel()!=null){
        	salesmanQueryInfo=new SalesmanQueryInfo();
        	salesmanQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
        	salesmanQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
        	salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
        }else{
            return "fail";
        }
        SalesmanQueryReturnBo queryReturnBo  = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
       if("success".equals(queryReturnBo.getErrMsg())){
            return queryReturnBo.getSalesName();
        }else{
            return "fail";
        }
    }
}