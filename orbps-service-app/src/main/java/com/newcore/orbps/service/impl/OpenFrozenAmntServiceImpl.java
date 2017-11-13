package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.GrpFaUnfreezeBo;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.OpenFrozenAmntService;
import com.newcore.orbps.service.pcms.api.GrpFaUnfreezeService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 暂收费解冻服务
 *
 * @author lijifei
 * 创建时间：2016年11月03日13:25:11
 */

@Service("openFrozenAmntService")
public class OpenFrozenAmntServiceImpl implements OpenFrozenAmntService {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	GrpFaUnfreezeService grpFaUnfreezeServiceClient;
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(OpenFrozenAmntServiceImpl.class);
	/** 
	 * 方法说明：根据投保单号查询mongodb库中投保单集合[管理机构号]与[第一险种]，将查出的值作为参数调用 保单辅助系统接口。
	 * @param applNo    投保单号
	 */
	@Override
	public String openFrozenAmnt(String applNo) {
		Map<String, Object> map=new HashMap<>();
		map.put("applNo", applNo);
		//根据投保单号查询出管理机构号、主险种代码以及总保费
		GrpInsurAppl grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		//入参非空校验
		if(null!=grpInsurAppl){
			//获得 管理机构号和第一主险
			String mgrBranchNo=grpInsurAppl.getMgrBranchNo();
			String polCode=grpInsurAppl.getFirPolCode();
			//非空判断。如果为空直接返回N
			if(!StringUtils.isBlank(mgrBranchNo)&& !StringUtils.isBlank(polCode)){
				//实收冲正结果
				GrpFaUnfreezeBo  grpFaUnfreezeBo = new GrpFaUnfreezeBo();
				grpFaUnfreezeBo.setApplNo(applNo);
				grpFaUnfreezeBo.setDataSource(null);
				grpFaUnfreezeBo.setMgrBranchNo(mgrBranchNo);
				grpFaUnfreezeBo.setPolCode(polCode);
				//消息头设置
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//				headerInfo.setOrginSystem("ORBPS");					
//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				//调用保单辅助-暂收费解冻服务
				RetInfo retInfo = grpFaUnfreezeServiceClient.operationGrpFaUnfreeze(grpFaUnfreezeBo);
				//根据调用保单辅助平台接口返回值，判断是否成功
				if("1".equals(retInfo.getRetCode())){
					//如果retInfo.getRetCode()为1，即成功，则返回Y
					return "Y";
				}else{
					//否则返回N
					return "N";
				}
			}else{
				logger.error("查询不到投保单号为："+applNo+"下对应的[管理机构号]或[第一险种]！");
				return "N";
			}
		}else{
			logger.error("投保单号为："+applNo+"对应的[团单]查询不到信息！");
			return "N"; 
		}
	}

}
