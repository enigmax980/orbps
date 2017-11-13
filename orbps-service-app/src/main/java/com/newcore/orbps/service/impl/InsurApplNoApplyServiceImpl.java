package com.newcore.orbps.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.dao.api.UntilPolicyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.service.api.InsurApplNoApplyService;

/**
 * 投保单号申请接口实现
 * @author jiachenchen
 * 创建时间：2016年8月3日 10:03:47
 */
@Service("insurApplNoApplyService")
public class InsurApplNoApplyServiceImpl implements InsurApplNoApplyService {
	
	@Autowired
	UntilPolicyDao testUntilPolicy; 
	/**
	 * 生成投保单号 
	 * @param salesBranchNo 销售机构号
	 * @return appId:16位随机码(yyMMddHHmmssSSS+1位随机码)
	 * @throws Exception 
	 */
	@Override
	public RetInfo createInsurApplNo(Map<String, String> map){
		RetInfo retInfo=new RetInfo();
		String errCode="1";
		StringBuilder errMsg=new StringBuilder();
		String applNo=null;
		if(null==map||map.isEmpty()){
			errCode="0";
			 errMsg.append("传参不允许为空");
		}else{
			if(StringUtils.isBlank(map.get("cntrType"))){
				errCode="0";
				errMsg.append("契约类型不允许为空");
			}
			else if(StringUtils.isBlank(map.get("salesBarnchNo"))){
				errCode="0";
				errMsg.append("销售机构号不允许为空");
			}else{
				applNo=testUntilPolicy.cfsSetApplNo(map.get("cntrType"),map.get("salesBarnchNo"));

			}
		}
		retInfo.setApplNo(applNo);
		retInfo.setErrMsg(errMsg.toString());
		retInfo.setRetCode(errCode);
		return retInfo;
	}


}
