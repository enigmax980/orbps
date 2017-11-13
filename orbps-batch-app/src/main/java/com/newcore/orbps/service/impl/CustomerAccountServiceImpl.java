package com.newcore.orbps.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.custaccount.BnfrCustNoVo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.HldrInfo;
import com.newcore.orbps.service.api.CustomerAccountService;
import com.newcore.orbps.service.cmds.api.CreatePsnCstomerAcountService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author wangxiao 创建时间：2016年8月24日下午4:45:23
 */
@Service("customerAccountService")
public class CustomerAccountServiceImpl implements CustomerAccountService {

	private static Logger logger = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	CreatePsnCstomerAcountService restfulBatchCreatePsn;

	/**
	 * 客户开户，获取客户号
	 */
	@Override
	public List<JSONObject> custaccount(List<Map<String, Object>> jsonObjects) throws RemoteException {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String result = restfulBatchCreatePsn.createPsnCstomerAcount(jsonObjects);
		logger.info("开户结束，返回参数=" + result);
		return JSON.parseArray(result, JSONObject.class);
	}

	/**
	 * 客户号回写
	 */
	@Override
	public void update(Map<Long, GrpInsured> grpInsuredMap, List<JSONObject> retJSONObjects) {
		List<BnfrCustNoVo> bnfrCustNoVos = new ArrayList<>();
		String procFlag = "";
		String remark = "";
		String ipsnCustNo = "";
		String hldrCustNo = "";
		String ipsnPartyId = "";
		String hldrPartyId = "";
		String applNo = (String) retJSONObjects.get(0).get("APPL_NO");
		List<GrpInsured> grpInsureds = new ArrayList<>();
		List<Long> ipsnNoList = new ArrayList<>();
		Map<String, Object> queryMap = new HashMap<>();

		for (int i = 0; i < retJSONObjects.size(); i++) {
			JSONObject jsonObject = retJSONObjects.get(i);
			Map<String, Object> map = new HashMap<>();
			applNo = (String) jsonObject.get("APPL_NO");
			String ipsnNo = (String) jsonObject.get("IPSN_NO");
			String role = (String) jsonObject.get("ROLE");
			if (i == 0 || !StringUtils.equals(ipsnNo, (String) retJSONObjects.get(i - 1).get("IPSN_NO"))) {
				ipsnCustNo = "";
				hldrCustNo = "";
				bnfrCustNoVos.clear();
				procFlag = "";
				remark = "";
				ipsnPartyId = "";
				hldrPartyId = "";
			}
			if (role == null || StringUtils.isEmpty(jsonObject.getString("PARTY_ID")) || StringUtils.isEmpty(jsonObject.getString("CUST_NO"))) {
				procFlag = "F";
				remark = (String) jsonObject.get("RelCode");
			} else if (StringUtils.equals(role, "2")) {
				ipsnPartyId = (String) jsonObject.get("PARTY_ID");
				ipsnCustNo = (String) jsonObject.get("CUST_NO");
			} else if (StringUtils.equals(role, "1")) {
				hldrPartyId = (String) jsonObject.get("PARTY_ID");
				hldrCustNo = (String) jsonObject.get("CUST_NO");
			} else if (StringUtils.equals(role, "3")) {
				BnfrCustNoVo bnfrCustNoVo = new BnfrCustNoVo();
				String bnfrPartyId = (String) jsonObject.get("PARTY_ID");
				String bnfrCustNo = (String) jsonObject.get("CUST_NO");
				String bnfrLevel = (String) jsonObject.get("BNFRLEVEL");
				bnfrCustNoVo.setBnfrPartyId(bnfrPartyId);
				bnfrCustNoVo.setBnfrCustNo(bnfrCustNo);
				bnfrCustNoVo.setBnfrLevel(StringUtils.isEmpty(bnfrLevel) ? null : Long.valueOf(bnfrLevel));
				bnfrCustNoVos.add(bnfrCustNoVo);
			}
			if ((i == (retJSONObjects.size() - 1)
					|| !StringUtils.equals(ipsnNo, (String) retJSONObjects.get(i + 1).get("IPSN_NO")))
					&& !StringUtils.isEmpty(applNo) && !StringUtils.isEmpty(ipsnNo)) {
				map.put("applNo", applNo);
				map.put("ipsnNo", Long.valueOf(ipsnNo));
				GrpInsured grpInsured = grpInsuredMap.get(Long.valueOf(ipsnNo));
				grpInsured.setIpsnPartyId(ipsnPartyId);
				grpInsured.setIpsnCustNo(ipsnCustNo);
				grpInsured.setProcFlag(StringUtils.equals(procFlag, "") ? "C" : "N");
				grpInsured.setRemark(remark);
				HldrInfo hldrInfo = grpInsured.getHldrInfo();
				if (hldrInfo != null) {
					hldrInfo.setHldrPartyId(hldrPartyId);
					hldrInfo.setHldrCustNo(hldrCustNo);
				}
				grpInsured.setHldrInfo(hldrInfo);
				List<BnfrInfo> bnfrInfos = grpInsured.getBnfrInfoList();
				if (bnfrInfos != null && !bnfrInfos.isEmpty()) {
					setBnfrInfos(bnfrCustNoVos, bnfrInfos);
				}

				grpInsureds.add(grpInsured);
				ipsnNoList.add(grpInsured.getIpsnNo());
			} else if (StringUtils.isEmpty(applNo)) {
				logger.error(JSON.toJSONString(retJSONObjects));
				return;
			}
		}

		queryMap.put("applNo", applNo);
		queryMap.put("ipsnNo", ipsnNoList);
		mongoBaseDao.removeByQuery(GrpInsured.class, queryMap);
		mongoBaseDao.insertAll(grpInsureds);
	}

	private void setBnfrInfos(List<BnfrCustNoVo> bnfrCustNoVos, List<BnfrInfo> bnfrInfos) {
		for (BnfrInfo bnfrInfo : bnfrInfos) {
			for (BnfrCustNoVo bnfrCustNoVo : bnfrCustNoVos) {
				if (bnfrInfo.getBnfrLevel().equals(bnfrCustNoVo.getBnfrLevel())) {
					bnfrInfo.setBnfrPartyId(bnfrCustNoVo.getBnfrPartyId());
					bnfrInfo.setBnfrCustNo(bnfrCustNoVo.getBnfrCustNo());
				}
			}
		}
	}
}
