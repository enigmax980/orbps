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
import com.newcore.orbps.models.pcms.bo.GrpMioUndoResultBo;
import com.newcore.orbps.models.pcms.bo.MioUndoRetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.GetCorrResult;
import com.newcore.orbps.service.pcms.api.GrpMioUndoResultQryService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 查询冲正结果通知
 * @author LJF
 * 2016年11月04日 09:50:26
 */
@Service("getCorrResult")
public class GetCorrResultServiceImpl implements GetCorrResult {

	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	GrpMioUndoResultQryService grpMioUndoResultQryServiceClient;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(GetCorrResultServiceImpl.class);

	/** 
	 * 方法说明：根据投保单号查询mongodb库中[管理机构号]与[第一险种]，将查出的值作为参数调用 保单辅助系统接口。
	 * @param applNo 投保单号
	 */
	@Override
	public String getCorrResultFromPcms(String applNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("applNo", applNo);
		//根据投保单号查询mongodb库
		GrpInsurAppl   grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		//非空判断
		if(null!=grpInsurAppl){
			//获得[管理机构号]与[第一险种]
			String mgrBranchNo =  grpInsurAppl.getMgrBranchNo();
			String polCode =  grpInsurAppl.getFirPolCode();
			//非空判断
			if(!StringUtils.isBlank(mgrBranchNo) && !StringUtils.isBlank(polCode)){
				//实收冲正结果
				GrpMioUndoResultBo grpMioUndoResultBo = new GrpMioUndoResultBo();
				grpMioUndoResultBo.setApplNo(applNo);
				grpMioUndoResultBo.setDataSource(null);
				grpMioUndoResultBo.setMgrBranchNo(mgrBranchNo);
				grpMioUndoResultBo.setPolCode(polCode);
				//消息头设置
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//				headerInfo.setOrginSystem("ORBPS");					
//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				//调用pcms查询冲正结果通知接口
				MioUndoRetInfo  mioUndoRetInfo  =	grpMioUndoResultQryServiceClient.queryGrpMioUndoResult(grpMioUndoResultBo);
				//根据调用保单辅助平台接口返回值，判断是否成功
				if("1".equals(mioUndoRetInfo.getRetCode())){
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
			logger.error("查询不到投保单号为："+applNo+"对应的数据！");
			return "N";
		}

	}

}
