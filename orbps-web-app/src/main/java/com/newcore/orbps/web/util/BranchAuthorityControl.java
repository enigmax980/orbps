package com.newcore.orbps.web.util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.service.api.CommonAgreementService;
import com.newcore.orbps.service.api.InsurApplOperUtilsService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 机构权限控制
 * 
 * @author 靳聪
 *
 */
@Controller
@RequestMapping("/orbps/public/branchControl")
public class BranchAuthorityControl {
	
	/**
     *	机构查询服务
     */
	@Autowired
    BranchService branchService;

	/**
     *	共保协议查询服务
     */
	@Autowired
	CommonAgreementService comAgrService;
	
	/**
     *	保单基本信息查询服务
     */
	@Autowired
	InsurApplServer grpInsurApplServer;
	
	/**
	 * 查询保单是否生效
	 */
	@Autowired
	InsurApplOperUtilsService insurApplOperUtilsService;
    /**
     * 省级机构对比
     * 
     * @author jincong
     * @param session
     * @param applNo
     * @return
     */
    @RequestMapping(value = "/cmpProvinceBranch")
    public @ResponseMessage RetInfo cmpProvinceBranch(@CurrentSession Session session,@RequestBody String applNo) {
    	RetInfo retInfo = new RetInfo();
    	String retCode="0";
    	StringBuffer errMsg= new StringBuffer();
    	String polProvinceBranch = "";
        //获取保单的管理机构号
        Map<String, Object> map =new HashMap<>();
        if(!StringUtils.isBlank(applNo)){
        	map.put("applNo", applNo);
        	//调用服务报文头
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            GrpInsurApplPara grpInsurApplPara = grpInsurApplServer.searchGrpInsurAppl(map);
            if(null != grpInsurApplPara){
            	polProvinceBranch = grpInsurApplPara.getGrpInsurAppl().getProvBranchNo();
            }else{
            	errMsg.append("|该投保单号查询不到数据!|");
            }
        }
        
		// 查询当前操作员管理机构号对应的省级机构号
		if (!StringUtils.isBlank(polProvinceBranch)) {
			// 获取session中当前操作员的管理机构号
			SessionModel sessionModel = SessionUtils.getSessionModel(session);
			String sessBrachNo = sessionModel.getBranchNo();
			// 调用服务报文头
			CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo2);
			// 查询session中当前操作员管理机构号对应的省级机构号
			String sessProvinceBranch = branchService.findProvinceBranch(sessBrachNo);
			// 权限控制：判断当前操作员对应的省级机构号和保单号对应的省级机构号是否一致
			if (polProvinceBranch.equals(sessProvinceBranch)) {
				retCode = "1";
			} else {
				errMsg.append("|当前操作员对应省级机构和该保单对应省级机构号不一致!|");
			}
		} else {
			errMsg.append("|该投保单对应的省级机构号为空!|");
		}
        retInfo.setRetCode(retCode);
        retInfo.setErrMsg(errMsg.toString());
        return retInfo;
    }
    /**
     * 本级及下级机构对比
     * 
     * @author wangxiao
     * @param session
     * @param applNo
     * @return
     */
    @RequestMapping(value = "/cmpBranchAndSubBranch")
    public @ResponseMessage RetInfo cmpBranchAndSubBranch(@CurrentSession Session session,@RequestBody String applNo) {
    	RetInfo retInfo = new RetInfo();
    	String retCode="0";
    	StringBuffer errMsg= new StringBuffer();
    	String polBranchNo = "";
        //获取保单的管理机构号
        Map<String, Object> map =new HashMap<>();
        if(!StringUtils.isBlank(applNo)){
        	if(applNo.length()!=16){
        		map.put("cgNo", applNo);
        	}else{
        		map.put("applNo", applNo);
        	}
        	//调用服务报文头
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            GrpInsurApplPara grpInsurApplPara = grpInsurApplServer.searchGrpInsurAppl(map);
            if(null != grpInsurApplPara){
            	polBranchNo = grpInsurApplPara.getGrpInsurAppl().getMgrBranchNo();
            }else{
            	errMsg.append("|该投保单号查询不到数据!|");
            }
        }
        
        //查询当前操作员管理机构号对应的省级机构号
        if(!StringUtils.isBlank(polBranchNo)){
        	SessionModel sessionModel = SessionUtils.getSessionModel(session);
        	String sessBrachNo = sessionModel.getBranchNo();
        	if(StringUtils.equals(polBranchNo, sessBrachNo)){
        		retCode = "1";
        		retInfo.setRetCode(retCode);
        		return retInfo;
        	}
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        	HeaderInfoHolder.setOutboundHeader(headerInfo);
        	Branch branch = branchService.findSubBranchAll(sessBrachNo);
        	List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
        	if(branch.getChildren()==null){
        		errMsg.append("|该操作员只能操作本机级下级机构的保单!|");
        		retInfo.setRetCode(retCode);
        	    retInfo.setErrMsg(errMsg.toString());
        	    return retInfo;
        	}
        	for(String branchNo:branchNos){
        		if(StringUtils.equals(polBranchNo, branchNo)){
        			retCode = "1";
            		retInfo.setRetCode(retCode);
            		return retInfo;
        		}
        	}
        	errMsg.append("|该操作员只能操作本机级下级机构的保单!|");
    		retInfo.setRetCode(retCode);
    	    retInfo.setErrMsg(errMsg.toString());
    	    return retInfo;
        }else{
        	errMsg.append("|该投保单对应的管理机构号为空!|");
        }
        retInfo.setRetCode(retCode);
        retInfo.setErrMsg(errMsg.toString());
        return retInfo;
    }

    /**
	 * 回退前根据保单是否已生效进行机构号控制
	 * 
	 * @author wangxiao
	 * @param applNo
	 * @return
	 */
    @RequestMapping(value = "/beforeFallback")
    public @ResponseMessage RetInfo getIsInsurApplLanding(@CurrentSession Session session,@RequestBody String applNo){
    	RetInfo result = new RetInfo();
    	//调用服务报文头
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
    	HeaderInfoHolder.setOutboundHeader(headerInfo);
    	String retCode = insurApplOperUtilsService.getIsInsurApplLanding(applNo).getRetCode();
    	if(StringUtils.equals("1",retCode)){
    		result = cmpProvinceBranch(session, applNo);
    	}else if(StringUtils.equals("3",retCode)){
    		result = cmpBranchAndSubBranch(session, applNo);
    	}
    	return result;
    }

	/**
	 * 共保协议号校验是否本机构
	 * 
	 * @author wangyanjie
	 * @param searchAgreement
	 * @param agreementNum
	 * @return
	 */
	@RequestMapping(value = "/searchAgreement")
	public @ResponseMessage RetInfo searchAgreement(@CurrentSession Session session,@RequestBody String agreementNum) {
		RetInfo result = new RetInfo();
		//调用服务报文头
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		// 获取session信息
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		//获取操作员省级机构
		String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
		CommonAgreement commonAgreement = new CommonAgreement();
		commonAgreement.setAgreementNo(agreementNum);
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo1);
		//获取共保协议号的管理机构
		RetInfoObject<CommonAgreement> retInfoObject = comAgrService.comAgrSerQuery(commonAgreement);
		//判断查询共保协议是否成功
		if("1".equals(retInfoObject.getRetCode())){
			CommonAgreement commonAgreementRet = retInfoObject.getListObject().get(0);
			//判断查询共保协议内容是否为NULL，如果为null，赋值为空
			String branchAgree = null==commonAgreementRet?"":commonAgreementRet.getMgrBranchNo();
			//判断操作员省级机构和共保协议号管理机构是否一致
			if (!StringUtils.equals(branchAgree,provinceBranchNo)) {
				result.setRetCode("0");
				result.setErrMsg("该操作员只能使用本省级的共保协议号！");
				return result;
			}
		}
		result.setRetCode(retInfoObject.getRetCode());
		result.setErrMsg(retInfoObject.getErrMsg());
		return result;
	}
}