package com.newcore.orbps.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.GuaranteeRateInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplStateVo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.QueryGrpInsurApplStateService;
import com.newcore.orbps.service.pcms.api.GuaranteeRateQueryService;
import  com.newcore.orbps.models.pcms.bo.GuaranteeRateReturnBo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

@Service("queryGrpInsurApplStateService") 
public class QueryGrpInsurApplStateServiceImpl implements QueryGrpInsurApplStateService {

	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	GuaranteeRateQueryService resulguaranteeRateQueryService;
	/**
     * 日志管理工具实例.
     */
     static Logger logger = LoggerFactory.getLogger(QueryGrpInsurApplStateServiceImpl.class);
    
    
	@Override
	public GrpInsurApplStateVo getGrpInsurApplState(Map<String, Object> map) {
		String retCode="1";
		StringBuilder errBuilder=new StringBuilder();
		GrpInsurApplStateVo  grpInsurApplState=new GrpInsurApplStateVo();
		if(null!=map&&!StringUtils.isBlank((String)map.get("applNo"))){
			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(null!=grpInsurAppl){
			if(!StringUtils.isBlank(grpInsurAppl.getCgNo())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//				headerInfo.setOrginSystem("ORBPS");					//<ORISYS>SLBPS</ORISYS>
//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				GuaranteeRateInfo guaranteeRateInfo=new GuaranteeRateInfo();
				guaranteeRateInfo.setApplNo((String)map.get("applNo"));
				guaranteeRateInfo.setCgNo(grpInsurAppl.getCgNo());
				guaranteeRateInfo.setMgBranch(grpInsurAppl.getMgrBranchNo());
				guaranteeRateInfo.setPolCode(grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());
				GuaranteeRateReturnBo guaranteeRateReturnBo = resulguaranteeRateQueryService.QueryGuaranteeRate(guaranteeRateInfo);
			//判断是否返回正确
			if(StringUtils.equals(guaranteeRateReturnBo.getRetCode(), "1")){
				grpInsurApplState.setCgNo(guaranteeRateReturnBo.getCgNo());
				grpInsurApplState.setCntrFlag(guaranteeRateReturnBo.getCntrFlag());
				grpInsurApplState.setCvFlag(guaranteeRateReturnBo.getCvFlag());
				grpInsurApplState.setListFlag(guaranteeRateReturnBo.getListFlag());
				grpInsurApplState.setMioFlag(guaranteeRateReturnBo.getMioFlag());
				grpInsurApplState.setCareteTime(guaranteeRateReturnBo.getCreateTime());
				}else{
					retCode="0";
					errBuilder.append(guaranteeRateReturnBo.getErrMsg());
				}
			}else{
			retCode="0";
			errBuilder.append("|保单未生效,合同号为空|");
			}
			
		}else{
			retCode="0";
			errBuilder.append("|查询团单信息为空|");
		}
		
		}else{
			retCode="0";
			errBuilder.append("|投保单号为空|");
			
		}
		grpInsurApplState.setRetCode(retCode);
		grpInsurApplState.setErrMsg(errBuilder.toString());
		return grpInsurApplState;
	}

}
