package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.GrpCntrBackBO;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.NoticePCMSBackApplInfo;
import com.newcore.orbps.service.pcms.api.GrpCntrBackService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 保单回退通知服务
 *
 * @author lijifei
 * 创建时间：2016年11月02日13:15:18
 */

@Service("noticePCMSBackApplInfo")
public class NoticePCMSBackApplInfoServiceImpl implements NoticePCMSBackApplInfo {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	GrpCntrBackService grpCntrBackServiceClient;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(NoticePCMSBackApplInfoServiceImpl.class);
	/** 
	 * 方法说明：根据投保单号查询mongodb库中投保单集合[管理机构号]与[险种集合]，将查出的值与[处理财务数据标记]作为参数调用 保单辅助系统接口。
	 * @param applNo    投保单号
	 * 		  isProcMio 处理财务数据标记
	 */
	@Override
	public String findNoticePCMSBackApplInfo(String applNo,String isProcMio) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("applNo", applNo);
		GrpInsurAppl   grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		//入参非空校验。保单及处理财务数据标记 isProcMio
		if(null!=grpInsurAppl && !StringUtils.isBlank(isProcMio) ){
			//获得 管理机构号、 险种代码(数组)
			String mgrBranchNo =  grpInsurAppl.getMgrBranchNo();
			List<String> polCode = new ArrayList<>();
			for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
				polCode.add(policy.getPolCode());	//险种代码(数组)
			}
			//调用接口入参非空校验
			if(!StringUtils.isBlank(mgrBranchNo)&& !polCode.isEmpty()){
				//保单回退通知bo
				GrpCntrBackBO  grpCntrBackBO  =new GrpCntrBackBO();
				grpCntrBackBO.setApplNo(applNo);
				grpCntrBackBO.setDataSource(null);
				grpCntrBackBO.setIsProcMio(isProcMio);
				grpCntrBackBO.setMgrBranchNo(mgrBranchNo);
				grpCntrBackBO.setPolCode(polCode);
				//消息头设置
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//				headerInfo.setOrginSystem("ORBPS");					
//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				//调用pcms方-保单回退通知服务接口
				RetInfo  retInfo =	grpCntrBackServiceClient.setGrpCntrBack(grpCntrBackBO);
				if("1".equals(retInfo.getRetCode())){
					//如果retInfo.getRetCode()为1，即成功，则返回Y
					return "Y";
				}else{
					//否则返回N
					return "N";
				}
			}else{
				logger.error("查询不到投保单号为："+applNo+"下对应的[管理机构号]或[险种数组]！");
				return "N";
			}
		}else{
			logger.error("投保单号为："+applNo+"对应的[团单]查询不到信息或[处理财务数据标记]为空！");
			return "N"; 
		}
	}

}
