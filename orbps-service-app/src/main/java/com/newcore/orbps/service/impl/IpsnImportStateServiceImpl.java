package com.newcore.orbps.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.exception.BusinessException;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.IpsnImportInfo;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.IpsnImportStateService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;

/**
 * @author wangxiao
 * 创建时间：2016年10月25日下午7:34:22
 */
@Service("ipsnImportStateService")
public class IpsnImportStateServiceImpl implements IpsnImportStateService {
	
	private static Logger logger = LoggerFactory.getLogger(IpsnImportStateServiceImpl.class);
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	BranchService branchService;
	@Override
	public RetInfoObject<IpsnImportInfo> GetIpsnImportResult(IpsnImportInfo ipsnImportInfo) {
		StringBuilder sql = new StringBuilder();
		StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM LIST_IMPORT_TASK_QUEUE WHERE 1=1");
		if(ipsnImportInfo==null){
			throw new BusinessException("2006");
		}
		if(ipsnImportInfo.getPageNum()==null
			||ipsnImportInfo.getPageNum()<=0){
			ipsnImportInfo.setPageNum(0);
		}
		if(ipsnImportInfo.getRowNum()==null
			||ipsnImportInfo.getRowNum()==0){
			ipsnImportInfo.setRowNum(15);
		}
		sql.append("SELECT * FROM (SELECT ROWNUM AS ROWNO,t.* FROM LIST_IMPORT_TASK_QUEUE t");
		sql.append(" WHERE ROWNUM <=");
		sql.append((ipsnImportInfo.getPageNum()+1)*ipsnImportInfo.getRowNum());
		if(!StringUtils.isEmpty(ipsnImportInfo.getApplNo())){
			sql.append(" AND APPL_NO = '");
			sql.append(ipsnImportInfo.getApplNo());
			sql.append("'");
			countSql.append(" AND APPL_NO = '");
			countSql.append(ipsnImportInfo.getApplNo());
			countSql.append("'");
		}
		List<String> branchNos = new ArrayList<>();
		if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo())){
			StringBuilder salesBranchNos = new StringBuilder();
			salesBranchNos.append(ipsnImportInfo.getSalesBranchNo());
			branchNos.add(ipsnImportInfo.getSalesBranchNo());
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),ipsnImportInfo.getFindSubBranchNoFlag())){
				Branch branch = branchService.findSubBranchAll(ipsnImportInfo.getSalesBranchNo());
				List<String> branchNos1 = BranchNoUtils.getAllSubBranchNo(branch);
				for(String branchNo:branchNos1){
					salesBranchNos.append("','");
					salesBranchNos.append(branchNo);
					branchNos.add(branchNo);
				}
			}
			sql.append(" AND SALES_BRANCH_NO IN ('");
			sql.append(salesBranchNos);
			sql.append("')");
			countSql.append(" AND SALES_BRANCH_NO IN ('");
			countSql.append(salesBranchNos);
			countSql.append("')");
		}		
		if(!StringUtils.isEmpty(ipsnImportInfo.getPclkNo())){
			sql.append(" AND EXT_KEY0 = '");
			sql.append(ipsnImportInfo.getPclkNo());
			sql.append("'");
			countSql.append(" AND EXT_KEY0 = '");
			countSql.append(ipsnImportInfo.getPclkNo());
			countSql.append("'");
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getPclkBranchNo())){
			sql.append(" AND EXT_KEY1 = '");
			sql.append(ipsnImportInfo.getPclkBranchNo());
			sql.append("'");
			countSql.append(" AND EXT_KEY1 = '");
			countSql.append(ipsnImportInfo.getPclkBranchNo());
			countSql.append("'");
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getStatus())){
			sql.append(" AND STATUS = '");
			sql.append(ipsnImportInfo.getStatus());
			sql.append("'");
			countSql.append(" AND STATUS = '");
			countSql.append(ipsnImportInfo.getStatus());
			countSql.append("'");
		}
		if(ipsnImportInfo.getStartTime()!=null){
			sql.append(" AND START_TIME >= TO_DATE('");
			sql.append(DateFormatUtils.format(ipsnImportInfo.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
			sql.append("','yyyy-mm-dd hh24:mi:ss')");
			countSql.append(" AND START_TIME >= TO_DATE('");
			countSql.append(DateFormatUtils.format(ipsnImportInfo.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
			countSql.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(ipsnImportInfo.getEndTime()!=null){
			sql.append(" AND END_TIME <= TO_DATE('");
			sql.append(DateFormatUtils.format(ipsnImportInfo.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
			sql.append("','yyyy-mm-dd hh24:mi:ss')");
			countSql.append(" AND END_TIME <= TO_DATE('");
			countSql.append(DateFormatUtils.format(ipsnImportInfo.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
			countSql.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		Criteria criteria = new Criteria();
		if(!StringUtils.isEmpty(ipsnImportInfo.getApplNo())){
			criteria.and("billNo").is(ipsnImportInfo.getApplNo());
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo()) || !StringUtils.isEmpty(ipsnImportInfo.getSalesNo())){
			Criteria criteria1 = new Criteria();
			if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo())){
				criteria1.and("salesBranchNo").in(branchNos);
			}
			if(!StringUtils.isEmpty(ipsnImportInfo.getSalesNo())){
				criteria1.and("salesNo").is(ipsnImportInfo.getSalesNo());
			}
			criteria.and("salesInfos").elemMatch(criteria1);
		}
		Aggregation elementsAggregation = Aggregation.newAggregation(
			Aggregation.match(criteria),
			Aggregation.group("billNo")
			);
		AggregationResults<JSONObject> mentsResult = mongoTemplate.aggregate(elementsAggregation, "insurApplRegist", JSONObject.class);
		List<JSONObject> results = mentsResult.getMappedResults();
		if(results!=null && !results.isEmpty()){
			sql.append(" AND (");
			countSql.append(" AND (");
			for(int i = 0;i<results.size();i++){
				if(i==0){
					sql.append("APPL_NO IN(");
					countSql.append("APPL_NO IN(");
				}else if(i%999==0){
					sql.append(" OR APPL_NO IN(");
					countSql.append(" OR APPL_NO IN(");
				}
				sql.append("'");
				sql.append(results.get(i).get("_id"));
				sql.append("',");
				countSql.append("'");
				countSql.append(results.get(i).get("_id"));
				countSql.append("',");
				if((i%999==998)||i==results.size()-1){
					sql.append("'')");
					countSql.append("'')");
				}
			}
		}
		if(results!=null && !results.isEmpty()){
			countSql.append(")");
			sql.append(")");
		}
		sql.append(") t1 WHERE T1.ROWNO >");
		sql.append(ipsnImportInfo.getPageNum()*ipsnImportInfo.getRowNum());
		sql.append(" ORDER BY CREATE_TIME DESC");
		int count = jdbcTemplate.queryForObject(countSql.toString(), int.class);
		List<TaskPrmyInfo> taskPrmyInfos = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<TaskPrmyInfo>(TaskPrmyInfo.class));
		List<IpsnImportInfo> ipsnImportInfos = new ArrayList<>();
		for(TaskPrmyInfo taskPrmyInfo:taskPrmyInfos){
			IpsnImportInfo newIpsnImportInfo = new IpsnImportInfo();
			newIpsnImportInfo.setApplNo(taskPrmyInfo.getApplNo());
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(taskPrmyInfo.getApplNo())), GrpInsurAppl.class);
			String hldrName = "";
			String hldrCustNo = "";
			if(grpInsurAppl==null){
				grpInsurAppl = new GrpInsurAppl();
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())){
				if(null != grpInsurAppl.getGrpHolderInfo()){
					hldrName = grpInsurAppl.getGrpHolderInfo().getGrpName();
					hldrCustNo = grpInsurAppl.getGrpHolderInfo().getGrpCustNo();
				}
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey())){
				if(null != grpInsurAppl.getGrpHolderInfo() && StringUtils.equals(grpInsurAppl.getSgNo(),LIST_TYPE.GRP_SG.getKey())){
					hldrName = grpInsurAppl.getGrpHolderInfo().getGrpName();
					hldrCustNo = grpInsurAppl.getGrpHolderInfo().getGrpCustNo();
				}else if(null != grpInsurAppl.getPsnListHolderInfo() && StringUtils.equals(grpInsurAppl.getSgNo(),LIST_TYPE.PSN_SG.getKey())){
					hldrName = grpInsurAppl.getPsnListHolderInfo().getSgName();
					hldrCustNo = grpInsurAppl.getPsnListHolderInfo().getSgCustNo();
				}
			}
			newIpsnImportInfo.setHldrName(hldrName);
			newIpsnImportInfo.setHldrCustNo(hldrCustNo);
			newIpsnImportInfo.setSalesBranchNo(taskPrmyInfo.getSalesBranchNo());
			if(grpInsurAppl.getSalesInfoList() != null){
				for(SalesInfo salesInfo:grpInsurAppl.getSalesInfoList()){
					if(StringUtils.equals(salesInfo.getSalesBranchNo(), taskPrmyInfo.getSalesBranchNo())){
						newIpsnImportInfo.setSalesNo(salesInfo.getSalesNo());
					}
				}
			}
			//投保人数
			if(null != grpInsurAppl.getApplState()){
				newIpsnImportInfo.setIpsnNum(grpInsurAppl.getApplState().getIpsnNum());
			}
			//本次导入总人数
			long thisIpsnNum = 0;
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey5()) && taskPrmyInfo.getExtKey5().matches("[0-9]+")){
				thisIpsnNum = Long.parseLong(taskPrmyInfo.getExtKey5());
			}
			newIpsnImportInfo.setThisIpsnNum(thisIpsnNum);
			Query query = new Query();
			query.addCriteria(Criteria.where("applNo").is(taskPrmyInfo.getApplNo()));
			long rightNum = mongoTemplate.count(query, GrpInsured.class);
			long errorNum = mongoTemplate.count(query, ErrorGrpInsured.class);
			//总导入正确数量
			newIpsnImportInfo.setImportNum(rightNum);
			//本次已导入的正确数量
			long thisImportNum = rightNum;
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey4()) && taskPrmyInfo.getExtKey4().matches("[0-9]+")){
				thisImportNum = thisImportNum - Long.parseLong(taskPrmyInfo.getExtKey4());
			}
			newIpsnImportInfo.setThisImportNum(thisImportNum);
			if(StringUtils.equals(taskPrmyInfo.getStatus(),"C")
				|| StringUtils.equals(taskPrmyInfo.getStatus(),"E")){
				newIpsnImportInfo.setImportProgress(1.0);
			}else{
				double progress = (double)((rightNum+errorNum)*10000/grpInsurAppl.getApplState().getIpsnNum())/10000;
				newIpsnImportInfo.setImportProgress(progress);
			}
			newIpsnImportInfo.setStatus(taskPrmyInfo.getStatus());
			newIpsnImportInfo.setErrorNum(errorNum);
			if(!StringUtils.isEmpty(taskPrmyInfo.getStartTime())){
				newIpsnImportInfo.setStartTime(stringToDate(taskPrmyInfo.getStartTime()));
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getEndTime())){
				newIpsnImportInfo.setEndTime(stringToDate(taskPrmyInfo.getEndTime()));
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey0())){
				newIpsnImportInfo.setPclkNo(taskPrmyInfo.getExtKey0());
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey1())){
				newIpsnImportInfo.setPclkBranchNo(taskPrmyInfo.getExtKey1());
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey2())){
				newIpsnImportInfo.setPclkName(taskPrmyInfo.getExtKey2());
			}
			newIpsnImportInfo.setSumPage(count);
			ipsnImportInfos.add(newIpsnImportInfo);
		}
		RetInfoObject<IpsnImportInfo> retInfoObject = new RetInfoObject<>();
		retInfoObject.setListObject(ipsnImportInfos);
		return retInfoObject;
	}
	@Override
	public RetInfoObject<IpsnImportInfo> getAllIpsnImportResult(IpsnImportInfo ipsnImportInfo) {
		StringBuilder sql = new StringBuilder("SELECT * FROM LIST_IMPORT_TASK_QUEUE WHERE 1=1");
		if(ipsnImportInfo==null){
			throw new BusinessException("2006");
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getApplNo())){
			sql.append(" AND APPL_NO = '");
			sql.append(ipsnImportInfo.getApplNo());
			sql.append("'");
		}
		List<String> branchNos = new ArrayList<>();
		if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo())){
			StringBuilder salesBranchNos = new StringBuilder();
			salesBranchNos.append(ipsnImportInfo.getSalesBranchNo());
			branchNos.add(ipsnImportInfo.getSalesBranchNo());
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),ipsnImportInfo.getFindSubBranchNoFlag())){
				Branch branch = branchService.findSubBranchAll(ipsnImportInfo.getSalesBranchNo());
				List<String> branchNos1 = BranchNoUtils.getAllSubBranchNo(branch);
				for(String branchNo:branchNos1){
					salesBranchNos.append("','");
					salesBranchNos.append(branchNo);
					branchNos.add(branchNo);
				}
			}
			sql.append(" AND SALES_BRANCH_NO IN ('");
			sql.append(salesBranchNos);
			sql.append("')");
		}		
		if(!StringUtils.isEmpty(ipsnImportInfo.getPclkNo())){
			sql.append(" AND EXT_KEY0 = '");
			sql.append(ipsnImportInfo.getPclkNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getPclkBranchNo())){
			sql.append(" AND EXT_KEY1 = '");
			sql.append(ipsnImportInfo.getPclkBranchNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getStatus())){
			sql.append(" AND STATUS = '");
			sql.append(ipsnImportInfo.getStatus());
			sql.append("'");
		}
		if(ipsnImportInfo.getStartTime()!=null){
			sql.append(" AND START_TIME >= TO_DATE('");
			sql.append(DateFormatUtils.format(ipsnImportInfo.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
			sql.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(ipsnImportInfo.getEndTime()!=null){
			sql.append(" AND END_TIME <= TO_DATE('");
			sql.append(DateFormatUtils.format(ipsnImportInfo.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
			sql.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		Criteria criteria = new Criteria();
		if(!StringUtils.isEmpty(ipsnImportInfo.getApplNo())){
			criteria.and("billNo").is(ipsnImportInfo.getApplNo());
		}
		if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo()) || !StringUtils.isEmpty(ipsnImportInfo.getSalesNo())){
			Criteria criteria1 = new Criteria();
			if(!StringUtils.isEmpty(ipsnImportInfo.getSalesBranchNo())){
				criteria1.and("salesBranchNo").in(branchNos);
			}
			if(!StringUtils.isEmpty(ipsnImportInfo.getSalesNo())){
				criteria1.and("salesNo").is(ipsnImportInfo.getSalesNo());
			}
			criteria.and("salesInfos").elemMatch(criteria1);
		}
		Aggregation elementsAggregation = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> mentsResult = mongoTemplate.aggregate(elementsAggregation, "insurApplRegist", JSONObject.class);
		List<JSONObject> results = mentsResult.getMappedResults();
		if(results!=null && !results.isEmpty()){
			sql.append(" AND (");
			for(int i = 0;i<results.size();i++){
				if(i==0){
					sql.append("APPL_NO IN(");
				}else if(i%999==0){
					sql.append(" OR APPL_NO IN(");
				}
				sql.append("'");
				sql.append(results.get(i).get("_id"));
				sql.append("',");
				if((i%999==998)||i==results.size()-1){
					sql.append("'')");
				}
			}
		}
		if(results!=null && !results.isEmpty()){
			sql.append(")");
		}
		sql.append(" ORDER BY CREATE_TIME DESC");
		System.err.println(sql);
		List<TaskPrmyInfo> taskPrmyInfos = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<TaskPrmyInfo>(TaskPrmyInfo.class));
		List<IpsnImportInfo> ipsnImportInfos = new ArrayList<>();
		for(TaskPrmyInfo taskPrmyInfo:taskPrmyInfos){
			IpsnImportInfo newIpsnImportInfo = new IpsnImportInfo();
			newIpsnImportInfo.setApplNo(taskPrmyInfo.getApplNo());
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(taskPrmyInfo.getApplNo())), GrpInsurAppl.class);
			String hldrName = "";
			String hldrCustNo = "";
			if(grpInsurAppl==null){
				grpInsurAppl = new GrpInsurAppl();
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())){
				if(null != grpInsurAppl.getGrpHolderInfo()){
					hldrName = grpInsurAppl.getGrpHolderInfo().getGrpName();
					hldrCustNo = grpInsurAppl.getGrpHolderInfo().getGrpCustNo();
				}
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey())){
				if(null != grpInsurAppl.getGrpHolderInfo() && StringUtils.equals(grpInsurAppl.getSgNo(),LIST_TYPE.GRP_SG.getKey())){
					hldrName = grpInsurAppl.getGrpHolderInfo().getGrpName();
					hldrCustNo = grpInsurAppl.getGrpHolderInfo().getGrpCustNo();
				}else if(null != grpInsurAppl.getPsnListHolderInfo() && StringUtils.equals(grpInsurAppl.getSgNo(),LIST_TYPE.PSN_SG.getKey())){
					hldrName = grpInsurAppl.getPsnListHolderInfo().getSgName();
					hldrCustNo = grpInsurAppl.getPsnListHolderInfo().getSgCustNo();
				}
			}
			newIpsnImportInfo.setHldrName(hldrName);
			newIpsnImportInfo.setHldrCustNo(hldrCustNo);
			newIpsnImportInfo.setSalesBranchNo(taskPrmyInfo.getSalesBranchNo());
			if(grpInsurAppl.getSalesInfoList() != null){
				for(SalesInfo salesInfo:grpInsurAppl.getSalesInfoList()){
					if(StringUtils.equals(salesInfo.getSalesBranchNo(), taskPrmyInfo.getSalesBranchNo())){
						newIpsnImportInfo.setSalesNo(salesInfo.getSalesNo());
					}
				}
			}
			//投保人数
			if(null != grpInsurAppl.getApplState()){
				newIpsnImportInfo.setIpsnNum(grpInsurAppl.getApplState().getIpsnNum());
			}
			//本次导入总人数
			long thisIpsnNum = 0;
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey5()) && taskPrmyInfo.getExtKey5().matches("[0-9]+")){
				thisIpsnNum = Long.parseLong(taskPrmyInfo.getExtKey5());
			}
			newIpsnImportInfo.setThisIpsnNum(thisIpsnNum);
			Query query = new Query();
			query.addCriteria(Criteria.where("applNo").is(taskPrmyInfo.getApplNo()));
			long rightNum = mongoTemplate.count(query, GrpInsured.class);
			long errorNum = mongoTemplate.count(query, ErrorGrpInsured.class);
			//总导入正确数量
			newIpsnImportInfo.setImportNum(rightNum);
			//本次已导入的正确数量
			long thisImportNum = rightNum;
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey4()) && taskPrmyInfo.getExtKey4().matches("[0-9]+")){
				thisImportNum = thisImportNum - Long.parseLong(taskPrmyInfo.getExtKey4());
			}
			newIpsnImportInfo.setThisImportNum(thisImportNum);
			if(StringUtils.equals(taskPrmyInfo.getStatus(),"C")
					|| StringUtils.equals(taskPrmyInfo.getStatus(),"E")){
				newIpsnImportInfo.setImportProgress(1.0);
			}else{
				double progress = (double)((rightNum+errorNum)*10000/grpInsurAppl.getApplState().getIpsnNum())/10000;
				newIpsnImportInfo.setImportProgress(progress);
			}
			newIpsnImportInfo.setStatus(taskPrmyInfo.getStatus());
			newIpsnImportInfo.setErrorNum(errorNum);
			if(!StringUtils.isEmpty(taskPrmyInfo.getStartTime())){
				newIpsnImportInfo.setStartTime(stringToDate(taskPrmyInfo.getStartTime()));
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getEndTime())){
				newIpsnImportInfo.setEndTime(stringToDate(taskPrmyInfo.getEndTime()));
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey0())){
				newIpsnImportInfo.setPclkNo(taskPrmyInfo.getExtKey0());
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey1())){
				newIpsnImportInfo.setPclkBranchNo(taskPrmyInfo.getExtKey1());
			}
			if(!StringUtils.isEmpty(taskPrmyInfo.getExtKey2())){
				newIpsnImportInfo.setPclkName(taskPrmyInfo.getExtKey2());
			}
			ipsnImportInfos.add(newIpsnImportInfo);
		}
		RetInfoObject<IpsnImportInfo> retInfoObject = new RetInfoObject<>();
		retInfoObject.setListObject(ipsnImportInfos);
		return retInfoObject;
	}
	
	private java.util.Date stringToDate(String stringDate){
		java.util.Date date = null;
		try {
			date = DateUtils.parseDate(stringDate,"yyyy-MM-dd HH:mm:ss.SSS");
		} catch (ParseException e) {
			logger.info(e.getMessage(),e);
		}
		return date;
	}
}
