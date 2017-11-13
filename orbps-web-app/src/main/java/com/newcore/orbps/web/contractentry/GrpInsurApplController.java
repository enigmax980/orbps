package com.newcore.orbps.web.contractentry;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.orbps.web.util.GrpDataVoToBo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 团单控制
 * 
 * @author xiaoye
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/grp")
public class GrpInsurApplController {

    @Autowired
    InsurApplServices insurApplServices;

    @Autowired
    InsurApplServer grpInsurApplServer;
    
    @Autowired
    PolicyQueryService resulpolicyQueryService;
    
    /**
     * 柜面机构查询服务
     */
    @Autowired
    BranchService branchService;
    
    /**
     * 保单辅助销售人员查询服务
     */
    @Autowired
    SalesmanInfoService resulsalesmanInfoService;
    
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
    @RequestMapping(value = "/search", method=RequestMethod.POST)
    public @ResponseMessage List<ResponseVo> getResponseSummaryList(@RequestBody ResponseVo responseVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
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
                       responseVo2.setProductName(subPolName);   
                       responseVo2.setProductCode(subPolCode);
                       list.add(responseVo2); 
                   }
                   pageData.setTotalCount(arr.size());
                   pageData.setData(list);
            }else{
            	throw new BusinessException("险种不存在");
            }
  
        }
        return list;
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
        HeaderInfoHolder.setOutboundHeader(headerInfo);       
   
        Map<String, Object> map=new HashMap<>();
        map.put("polCode", responseVo.getBusiPrdCode());
        Map<String, Object> maps = resulpolicyQueryService.excute(map);
        String result=JSON.toJSONString(maps);
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
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(NEW_APPL_STATE.GRP_ENTRY.getKey());
        GrpDataVoToBo VotoBo = new GrpDataVoToBo();
        GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
        //公共Vo　ＴＯ　ＢＯ　
        grpInsurApplPara.setGrpInsurAppl(VotoBo.grpinsurApplVoToBo(grpInsurApplVo));
        grpInsurApplPara.setTaskId(grpInsurApplVo.getTaskInfo().getTaskId());
        grpInsurApplPara.setTraceNode(traceNode);
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        RetInfo result = grpInsurApplServer.save(grpInsurApplPara);	
        return result;
    }
    
    /**
     * 暂存团单信息
     * 
     * @author jiincong
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/tempSave")
    public @ResponseMessage RetInfo tempSave(@CurrentSession Session session,@RequestBody GrpInsurApplVo grpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
//        taskInfo.setBizNo(grpInsurApplVo.getApplInfoVo().getApplNo());
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(NEW_APPL_STATE.RECORD_TEMPORARILY.getKey());
        GrpDataVoToBo VotoBo = new GrpDataVoToBo();
        GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
        grpInsurApplPara.setGrpInsurAppl(VotoBo.grpinsurApplVoToBo(grpInsurApplVo));//公共Vo to Bo 
        grpInsurApplPara.setTraceNode(traceNode);
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        RetInfo result = grpInsurApplServer.tempSave(grpInsurApplPara); 
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
        GrpDataBoToVo BoToVo = new GrpDataBoToVo();
        if("1".equals(grpInsurApplBo.getErrCode())){
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
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
        SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
        if(grpApplInfoVo.getSalesBranchNo()!=null&&grpApplInfoVo.getSaleCode()!=null&&grpApplInfoVo.getSalesChannel()!=null){
            salesmanQueryInfo.setSalesChannel(grpApplInfoVo.getSalesChannel());
            salesmanQueryInfo.setSalesBranchNo(grpApplInfoVo.getSalesBranchNo());
            salesmanQueryInfo.setSalesNo(grpApplInfoVo.getSaleCode());
        }else{
            return "fail";
        }
        SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
        if("success".equals(queryReturnBo.getErrMsg())){
            return queryReturnBo.getSalesName();
        }else{
            return "fail";
        }
    }
    
    /**
     * 查询销售机构名称
     * 
     * @author jincong
     * @param query
     * @return
     */
    @RequestMapping(value = "/queryBranchName")
    public @ResponseMessage String queryBranchName(@RequestBody String branchNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        Branch branch = branchService.findOne(branchNo);
        if(null == branch){
            return "fail";
        }else{
            return branch.getBranchName();
        }
    }
}