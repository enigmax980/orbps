package com.newcore.orbps.service.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;



public class IpsnItemOpenCustProcessor implements ItemProcessor<GrpInsured, Map<GrpInsured,List<JSONObject>>>{
	@Autowired
	private MongoBaseDao mongoBaseDao;
	
	private GrpInsurAppl grpInsurAppl;
	
	private final static String PROC_FLAG_C = "C";
	
	
	@Override
	public Map<GrpInsured,List<JSONObject>> process(GrpInsured item) throws Exception {
		
		/*如果执行完成，则跳过此行*/
//		if(StringUtils.equals(item.getProcFlag(), PROC_FLAG_C)){
//			throw new IpsnOpenCustCompleteException();
//		}
		Map<GrpInsured,List<JSONObject>> grpInsuredMap = new HashMap<GrpInsured, List<JSONObject>>();
		
		Map<String, Object> map = new HashMap<>();
		map.put("applNo",item.getApplNo());

		if(grpInsurAppl==null || !StringUtils.equals(grpInsurAppl.getApplNo(),item.getApplNo())){
			grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		}
		
		List<JSONObject> jsonObjects = new ArrayList<>();
		if(isHldrInfoComplete(item)){
			JSONObject hldrJSONObject = getHldrJSONObject(grpInsurAppl, item);
			jsonObjects.add(hldrJSONObject);
		}
		if(!StringUtils.isEmpty(item.getIpsnName())
			&& !StringUtils.isEmpty(item.getIpsnSex())
			&& !StringUtils.isEmpty(item.getIpsnIdType())
			&& !StringUtils.isEmpty(item.getIpsnIdNo())
			&& item.getIpsnBirthDate()!=null
		){
			JSONObject ipsnJSONObject = getIpsnJSONObject(grpInsurAppl, item);
			jsonObjects.add(ipsnJSONObject);
		}
		if(item.getBnfrInfoList()!=null	&& !item.getBnfrInfoList().isEmpty()){
			List<JSONObject> bnfrJSONObject = getBnfrJSONObject(grpInsurAppl, item);
			if(!bnfrJSONObject.isEmpty())
				jsonObjects.addAll(bnfrJSONObject);
		}
		grpInsuredMap.put(item, jsonObjects);
		return grpInsuredMap;
	}

	/**
	 * @return the mongoBaseDao
	 */
	public MongoBaseDao getMongoBaseDao() {
		return mongoBaseDao;
	}

	/**
	 * @param mongoBaseDao the mongoBaseDao to set
	 */
	public void setMongoBaseDao(MongoBaseDao mongoBaseDao) {
		this.mongoBaseDao = mongoBaseDao;
	}
	private JSONObject getIpsnJSONObject(GrpInsurAppl grpInsurAppl,GrpInsured item){
		JSONObject json = new JSONObject();
		if(grpInsurAppl!=null && grpInsurAppl.getSalesInfoList()!=null){
			String provBranchNo = grpInsurAppl.getProvBranchNo();
			json.put("PROV_BRANCH_NO",provBranchNo==null?"120000":provBranchNo);
		}else{
			json.put("PROV_BRANCH_NO","120000");// 省机构号
		}
		json.put("SRC_SYS","ORBPS");// 系统来源
		if(grpInsurAppl!=null){
			String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
			json.put("CUST_OAC_BRANCH_NO",mgrBranchNo==null?"120000":mgrBranchNo);//管理机构
		}else{
			json.put("CUST_OAC_BRANCH_NO","120000");
		}
		json.put("APPL_NO", isEmpty(item.getApplNo()));
		json.put("ROLE","2");
		json.put("IPSN_NO",item.getIpsnNo()==null?"":item.getIpsnNo());
		json.put("BNFRLEVEL","");
		json.put("NAME", isEmpty(item.getIpsnName()));
		json.put("ID_TYPE",isEmpty(item.getIpsnIdType()));
		json.put("ID_NO",isEmpty(item.getIpsnIdNo()));
		json.put("SEX",isEmpty(item.getIpsnSex()));
		json.put("BIRTH_DATE",item.getIpsnBirthDate()==null?"":DateFormatUtils.format(item.getIpsnBirthDate(),"yyyy-MM-dd"));
		return json;
	}
	private JSONObject getHldrJSONObject(GrpInsurAppl grpInsurAppl,GrpInsured item){
		JSONObject json = new JSONObject();
		if(grpInsurAppl!=null && grpInsurAppl.getSalesInfoList()!=null){
			String provBranchNo = grpInsurAppl.getProvBranchNo();
			json.put("PROV_BRANCH_NO",provBranchNo==null?"120000":provBranchNo);
		}else{
			json.put("PROV_BRANCH_NO","120000");// 省机构号
		}
		json.put("SRC_SYS","ORBPS");// 系统来源
		if(grpInsurAppl!=null){
			String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
			json.put("CUST_OAC_BRANCH_NO",mgrBranchNo==null?"120000":mgrBranchNo);//管理机构
		}else{
			json.put("CUST_OAC_BRANCH_NO","120000");
		}
		json.put("APPL_NO", isEmpty(item.getApplNo()));
		json.put("ROLE","1");
		json.put("IPSN_NO",item.getIpsnNo()==null?"":item.getIpsnNo());
		json.put("BNFRLEVEL","");
		json.put("NAME", isEmpty(item.getHldrInfo().getHldrName()));
		json.put("ID_TYPE",isEmpty(item.getHldrInfo().getHldrIdType()));
		json.put("ID_NO",isEmpty(item.getHldrInfo().getHldrIdNo()));
		json.put("SEX",isEmpty(item.getHldrInfo().getHldrSex()));
		json.put("BIRTH_DATE",item.getHldrInfo().getHldrBirthDate()==null?"":DateFormatUtils.format(item.getHldrInfo().getHldrBirthDate(),"yyyy-MM-dd"));
		return json;
	}
	private List<JSONObject> getBnfrJSONObject(GrpInsurAppl grpInsurAppl,GrpInsured item){
		List<JSONObject> jsonObjects = new ArrayList<>();
		List<BnfrInfo> bnfrInfos = item.getBnfrInfoList();
		for (BnfrInfo bnfrInfo : bnfrInfos) {
			if(!StringUtils.isEmpty(bnfrInfo.getBnfrName())
				&& !StringUtils.isEmpty(bnfrInfo.getBnfrSex())
				&& !StringUtils.isEmpty(bnfrInfo.getBnfrIdType())
				&& !StringUtils.isEmpty(bnfrInfo.getBnfrIdNo())
				&& bnfrInfo.getBnfrBirthDate()!=null
			){
				JSONObject json = new JSONObject();
				if(grpInsurAppl!=null && grpInsurAppl.getSalesInfoList()!=null){
					String provBranchNo = grpInsurAppl.getProvBranchNo();
					json.put("PROV_BRANCH_NO",provBranchNo==null?"120000":provBranchNo);
				}else{
					json.put("PROV_BRANCH_NO","120000");// 省机构号
				}
				json.put("SRC_SYS","ORBPS");// 系统来源
				if(grpInsurAppl!=null){
					String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
					json.put("CUST_OAC_BRANCH_NO",mgrBranchNo==null?"120000":mgrBranchNo);//管理机构
				}else{
					json.put("CUST_OAC_BRANCH_NO","120000");
				}
				json.put("APPL_NO", isEmpty(item.getApplNo()));
				json.put("ROLE","3");
				json.put("IPSN_NO",item.getIpsnNo()==null?"":item.getIpsnNo());
				json.put("BNFRLEVEL",bnfrInfo.getBnfrLevel()==null?"":bnfrInfo.getBnfrLevel());
				json.put("NAME", isEmpty(bnfrInfo.getBnfrName()));
				json.put("ID_TYPE",isEmpty(bnfrInfo.getBnfrIdType()));
				json.put("ID_NO",isEmpty(bnfrInfo.getBnfrIdNo()));
				json.put("SEX",isEmpty(bnfrInfo.getBnfrSex()));
				json.put("BIRTH_DATE",bnfrInfo.getBnfrBirthDate()==null?"":DateFormatUtils.format(bnfrInfo.getBnfrBirthDate(),"yyyy-MM-dd"));
				jsonObjects.add(json);
			}
		}
		return jsonObjects;
	}
	private static String isEmpty(String str){
		return StringUtils.isBlank(str)?"":str.trim();
	}
	private boolean isHldrInfoComplete(GrpInsured item){
		if(item.getHldrInfo()==null){
			return false;
		}
		if(StringUtils.isEmpty(item.getHldrInfo().getHldrName())){
			return false;
		}
		if(StringUtils.isEmpty(item.getHldrInfo().getHldrSex())){
			return false;
		}
		if(StringUtils.isEmpty(item.getHldrInfo().getHldrIdType())){
			return false;
		}
		if(StringUtils.isEmpty(item.getHldrInfo().getHldrIdNo())){
			return false;
		}
		if(item.getHldrInfo().getHldrBirthDate()==null){
			return false;
		}
		return true;
	}
}
