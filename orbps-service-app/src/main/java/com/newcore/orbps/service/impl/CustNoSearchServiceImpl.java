package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.custaccount.BnfrCustNoVo;
import com.newcore.orbps.models.custaccount.CustNoVo;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.CustNoSearchSrevice;

/**
 * @author wangxiao
 * 创建时间：2016年9月3日下午4:16:21
 */
@Service("custNoSearchSrevice")
public class CustNoSearchServiceImpl implements CustNoSearchSrevice {
	@Autowired
	private MongoBaseDao mongoBaseDao;
	@Override
	public RetInfoObject<CustNoVo> search(Map<String, String> applNoMap) {
		String applNo = applNoMap.get("applNo");
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		List<GrpInsured> grpInsureds = (List<GrpInsured>) mongoBaseDao.find(GrpInsured.class, map);
		ArrayList<CustNoVo> custNoVos = new ArrayList<>();
		List<BnfrCustNoVo> bnfrCustNoVos = new ArrayList<>();
		for(GrpInsured grpInsured : grpInsureds){
			bnfrCustNoVos.clear();
			CustNoVo custNoVo = new CustNoVo();
			custNoVo.setApplNo(grpInsured.getApplNo());
			custNoVo.setIpsnNo(grpInsured.getIpsnNo());
			custNoVo.setIpsnCustNo(grpInsured.getIpsnCustNo());
			if(null != grpInsured.getHldrInfo()){
				custNoVo.setHldrCustNo(grpInsured.getHldrInfo().getHldrCustNo());
			}
			if(null != grpInsured.getBnfrInfoList()){
				for(BnfrInfo bnfrInfo : grpInsured.getBnfrInfoList()){
					BnfrCustNoVo bnfrCustNoVo = new BnfrCustNoVo();
					bnfrCustNoVo.setBnfrLevel(bnfrInfo.getBnfrLevel());
					bnfrCustNoVo.setBnfrCustNo(bnfrInfo.getBnfrCustNo());
					bnfrCustNoVos.add(bnfrCustNoVo);
				}
			}
			custNoVo.setBnfrCustNoVos(bnfrCustNoVos);
			custNoVos.add(custNoVo);
		}
		RetInfoObject<CustNoVo> retInfoObject = new RetInfoObject<>();
		retInfoObject.setApplNo(applNo);
		retInfoObject.setListObject(custNoVos);
		if(custNoVos.isEmpty()){
			retInfoObject.setRetCode("0");
		}else{
			retInfoObject.setRetCode("1");
		}
		return retInfoObject;
	}

}
