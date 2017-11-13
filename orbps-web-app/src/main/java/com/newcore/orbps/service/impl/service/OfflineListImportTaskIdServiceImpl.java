package com.newcore.orbps.service.impl.service;

import com.newcore.task.models.TaskJumpModel;
import com.newcore.task.service.api.TaskBusinessProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportBsInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportPageVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportSubPolVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.contractentry.OfflineListImportController;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 *  清单导入 从任务链接中打开页面带过来数据。
 * @author xiaoYe
 *         Created on 16-11-1
 */
@Service("offlineListImportTaskIdService")
@DependsOn("grpInsurApplServer")
public class OfflineListImportTaskIdServiceImpl implements TaskBusinessProcessService<Serializable> {
    @Autowired
    InsurApplServer grpInsurApplServer;
    @Override
    public ListImportPageVo invoker(TaskJumpModel taskJumpModel) {
    	TaskInfo taskInfo = new TaskInfo();
        taskInfo.setBizNo(taskJumpModel.getBusinessKey());//业务流水号 || 投保单号
        taskInfo.setTaskId(taskJumpModel.getTaskId());//任务ID
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        System.out.println("++++++++++++++++++++++++++controller test   taskJumpModel++++++++++++++++++++++++++"+taskInfo.toString());
        System.out.println("++++++++++++++++++++++++++taskInfo.getBizNo()++++++++++++++++++++++++++"+taskInfo.getBizNo());
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        //调用后台服务查询保单信息
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
        System.out.println("保单类型"+grpInsurApplBo.getGrpInsurAppl().getCntrType());;
        ListImportPageVo listImportPageVo = new ListImportPageVo();
        List<ListImportBsInfoVo> bsInfoVos = new ArrayList<ListImportBsInfoVo>();
        for (Policy policy:grpInsurApplBo.getGrpInsurAppl().getApplState().getPolicyList()) {
            List<ListImportSubPolVo> listImportSubPolVos = new ArrayList<ListImportSubPolVo>();
        	ListImportBsInfoVo bsInfoVo = new ListImportBsInfoVo();
        	bsInfoVo.setPolCode(policy.getPolCode());
        	bsInfoVo.setPolName(policy.getPolNameChn());
        	//责任信息
        	if(null != policy.getSubPolicyList()){
        		for (SubPolicy subPolicy:policy.getSubPolicyList()){
            		ListImportSubPolVo listImportSubPolVo = new  ListImportSubPolVo();
            		listImportSubPolVo.setSubPolCode(subPolicy.getSubPolCode());
            		listImportSubPolVo.setSubPolName(subPolicy.getSubPolName());
            		listImportSubPolVos.add(listImportSubPolVo);
    			}
        	}
        	bsInfoVo.setListImportSubPolVos(listImportSubPolVos);
        	bsInfoVos.add(bsInfoVo);
		}
        List<InsuredGroupModalVo> insuredGroupModalVos = new ArrayList<>();
        for(IpsnStateGrp ipsnStateGrp:grpInsurApplBo.getGrpInsurAppl().getIpsnStateGrpList()){
            InsuredGroupModalVo insuredGroupModalVo = new InsuredGroupModalVo();
            insuredGroupModalVo.setIpsnGrpName(ipsnStateGrp.getIpsnGrpName());
            insuredGroupModalVo.setIpsnGrpNo(ipsnStateGrp.getIpsnGrpNo());
            insuredGroupModalVo.setIpsnGrpNum(ipsnStateGrp.getIpsnGrpNum());
            insuredGroupModalVo.setGenderRadio(ipsnStateGrp.getGenderRadio());
            insuredGroupModalVo.setGsRate(ipsnStateGrp.getGsPct());
            insuredGroupModalVo.setSsRate(ipsnStateGrp.getSsPct());
            insuredGroupModalVo.setIpsnOccSubclsCode(ipsnStateGrp.getIpsnOccCode());
            insuredGroupModalVo.setOccClassCode(ipsnStateGrp.getOccClassCode());
            if(ipsnStateGrp.getGrpPolicyList()==null){
                continue;
            }
            List<InsuranceInfoVo> insuranceInfoVos = new ArrayList<>();
        	for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
        	    InsuranceInfoVo insuranceInfoVo = new InsuranceInfoVo();
        	    insuranceInfoVo.setFaceAmnt(grpPolicy.getFaceAmnt());
        	    insuranceInfoVo.setMrCode(grpPolicy.getMrCode());
        	    insuranceInfoVo.setPolCode(grpPolicy.getPolCode());
        	    insuranceInfoVo.setPremium(grpPolicy.getPremium());
        	    insuranceInfoVo.setPremRate(grpPolicy.getPremRate());
        	    insuranceInfoVo.setRecDisount(grpPolicy.getPremDiscount());
        	    insuranceInfoVo.setStdPremium(grpPolicy.getStdPremium());
        	    insuranceInfoVos.add(insuranceInfoVo);
        	}
        	insuredGroupModalVo.setInsuranceInfoVos(insuranceInfoVos);
        	insuredGroupModalVos.add(insuredGroupModalVo);
        }
        listImportPageVo.setInsuredGroupModalVos(insuredGroupModalVos);
        listImportPageVo.setBsInfoVo(bsInfoVos);
        listImportPageVo.setTaskInfo(taskInfo);
        listImportPageVo.setCntrType(grpInsurApplBo.getGrpInsurAppl().getCntrType());//保单类型
        listImportPageVo.setPremSource(grpInsurApplBo.getGrpInsurAppl().getPaymentInfo().getPremSource());//保费来源
        System.out.println(listImportPageVo.toString());
        return listImportPageVo;
    }
}