package com.newcore.orbps.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.EarnestAccInfo;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
/**
 * 处理收付费服务实现类
 * @author JCC
 * 2016年11月30日 11:26:52
 */
@Repository("procMioInfoDao")
public class ProcMioInfoDaoImpl implements ProcMioInfoDao{
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoBaseDao mongoBaseDao;

	@Override
	public GrpInsurAppl getGrpInsurAppl(String applNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl  grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		return grpInsurAppl;
	}
	
	@Override
	public MioPlnmioInfo getMioPlnmioInfo(String applNo) {
		Map<String,Object> maps = new HashMap<>();
		maps.put("applNo",applNo);
		MioPlnmioInfo  mioPlnmio = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class,maps);	
		return mioPlnmio;
	}

	@Override
	public int removeMioPlnmioByApplNo(String applNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("applNo",applNo);
		return mongoBaseDao.remove(MioPlnmioInfo.class, map);
	}

	@Override
	public Long getMaxPlnMioRecId() {
		long maxId;
		//获取应收集合表中最大的数据
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.unwind("plnmioRecList"),
				Aggregation.group("max").max("plnmioRecList.plnmioRecId").as("maxPlnmioRecId")
		);
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "mioPlnmioInfo", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		if(bdbl.isEmpty()){
			maxId=9000000000L;
		}else{
			BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
			maxId = obj.getLong("maxPlnmioRecId");
		}
		return maxId;
	}

	@Override
	public double getPolicyAddMoney(String applNo, String polCode) {
		Criteria cri = new Criteria();
		cri.and("businessKey").is(applNo);
		Aggregation agg;
		if("".equals(polCode)){
			 agg = Aggregation.newAggregation(
					Aggregation.match(cri),
					Aggregation.unwind("udwPolResults"),
					Aggregation.group("sumJe").sum("udwPolResults.addFeeAmnt").as("sumAddFeeAmnt")
			);
		}else{
			Criteria cri2 = new Criteria();
			cri2.and("udwPolResults.polCode").is(polCode);
			 agg = Aggregation.newAggregation(
					Aggregation.match(cri),
					Aggregation.unwind("udwPolResults"),
					Aggregation.match(cri2),
					Aggregation.group("sumJe").sum("udwPolResults.addFeeAmnt").as("sumAddFeeAmnt")
			);
		}
		AggregationResults<Object> resuAgg = mongoTemplate.aggregate(agg,"applUdwResult", Object.class);
		BasicDBList dbdList = (BasicDBList) resuAgg.getRawResults().get("result");
		if(dbdList.isEmpty()){
			return 0;
		}else{
			BasicDBObject bdb = (BasicDBObject) dbdList.get(0);
			return bdb.getDouble("sumAddFeeAmnt");
		}
	}
	
	@Override
	public double getPersonAddMoney(String applNo,List<Long> ipsnNoList) {
		Aggregation agg ;
		if(ipsnNoList == null || ipsnNoList.isEmpty()){
			agg = Aggregation.newAggregation(
					Aggregation.match(Criteria.where("businessKey").is(applNo)),
					Aggregation.unwind("udwIpsnResults"),				
					Aggregation.unwind("udwIpsnResults.udwIpsnPolResults"),
					Aggregation.group("sumJe").sum("udwIpsnResults.udwIpsnPolResults.addFeeAmnt").as("sumAddFeeAmnt")
			);
		}else{
			//添加被保人序号
	        BasicDBList ipsnNoDB = new BasicDBList();
	        for(long ipsnNo:ipsnNoList){
	        	ipsnNoDB.add(ipsnNo);
	        }
			agg = Aggregation.newAggregation(
					Aggregation.match(Criteria.where("businessKey").is(applNo)),
					Aggregation.unwind("udwIpsnResults"),				
					Aggregation.unwind("udwIpsnResults.udwIpsnPolResults"),
					Aggregation.match(Criteria.where("udwIpsnResults.ipsnNo").in(ipsnNoDB)),
					Aggregation.group("sumJe").sum("udwIpsnResults.udwIpsnPolResults.addFeeAmnt").as("sumAddFeeAmnt")
			);
		}
	
		AggregationResults<Object> resuAgg = mongoTemplate.aggregate(agg,"applUdwResult", Object.class);
		BasicDBList dbdList = (BasicDBList) resuAgg.getRawResults().get("result");
		if(dbdList.isEmpty()){
			return 0;
		}else{
			BasicDBObject bdb = (BasicDBObject) dbdList.get(0);
			return bdb.getDouble("sumAddFeeAmnt");
		}
	}

	@Override
	public InsurApplOperTrace getInsurApplOperTrace(String applNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		InsurApplOperTrace insurOper = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, map);
		return insurOper;
	}

	@Override
	public long getMaxBuildBankId() {
		//获取银行转账数据中transBatSeq最大的数据
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.unwind("bankTransList"),
				Aggregation.group("max").max("bankTransList.transBatSeq").as("maxTransBatSeq")
		);
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "mioPlnmioInfo", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		if(bdbl.isEmpty()){
			return 0L;
		}else{
			BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
			return obj.getLong("maxTransBatSeq");
		}
	}

	@Override
	public Long getMixMioLogId() {
		Aggregation queryAgg = Aggregation.newAggregation(
				Aggregation.unwind("mioLogList"),
				Aggregation.group("max").max("mioLogList.mioLogId").as("maxmioLogId")
		);
		AggregationResults<Object> queryAggRes = mongoTemplate.aggregate(queryAgg, "mioPlnmioInfo", Object.class);
		BasicDBList bdbList = (BasicDBList) queryAggRes.getRawResults().get("result");
		if (bdbList.isEmpty()) {
			return 9000000000L;
		} else {
			BasicDBObject bdbObject = (BasicDBObject) bdbList.get(0);
			return bdbObject.getLong("maxmioLogId");
		}
	}

	@Override
	public EarnestAccInfo getEarnestAccInfo(String accNo) {
		Map<String,Object> eAccMap = new HashMap<>();
		eAccMap.put("accNo", accNo);
		//根据帐号查询账户信息表
		EarnestAccInfo bigEarAcc = (EarnestAccInfo) mongoBaseDao.findOne(EarnestAccInfo.class, eAccMap);
		return bigEarAcc;
	}

	@Override
	public int removeEarnestAccInfo(String accNo) {
		Map<String,Object> eAccMap = new HashMap<>();
		eAccMap.put("accNo", accNo);
		return mongoBaseDao.remove(EarnestAccInfo.class, eAccMap);
	}

	@Override
	public double getPersonAddMoneyByPolCode(String applNo, String polCode) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("businessKey").is(applNo)),
			Aggregation.unwind("udwIpsnResults"),				
			Aggregation.unwind("udwIpsnResults.udwIpsnPolResults"),
			Aggregation.match(Criteria.where("udwIpsnResults.udwIpsnPolResults.polCode").regex(polCode)),
			Aggregation.group("sumJe").sum("udwIpsnResults.udwIpsnPolResults.addFeeAmnt").as("sumAddFeeAmnt")
		);
		AggregationResults<Object> resuAgg = mongoTemplate.aggregate(agg,"applUdwResult", Object.class);
		BasicDBList dbdList = (BasicDBList) resuAgg.getRawResults().get("result");
		if(dbdList.isEmpty()){
			return 0;
		}else{
			BasicDBObject bdb = (BasicDBObject) dbdList.get(0);
			return bdb.getDouble("sumAddFeeAmnt");
		}
	}

	@Override
	public double getPersonAddMoney(String applNo, String polCode, long ipsnNo) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("businessKey").is(applNo)),
			Aggregation.unwind("udwIpsnResults"),		
			Aggregation.match(Criteria.where("udwIpsnResults.ipsnNo").is(ipsnNo)),
			Aggregation.unwind("udwIpsnResults.udwIpsnPolResults"),
			Aggregation.match(Criteria.where("udwIpsnResults.udwIpsnPolResults.polCode").regex(polCode)),
			Aggregation.group("sumJe").sum("udwIpsnResults.udwIpsnPolResults.addFeeAmnt").as("sumAddFeeAmnt")
		);
		AggregationResults<Object> resuAgg = mongoTemplate.aggregate(agg,"applUdwResult", Object.class);
		BasicDBList dbdList = (BasicDBList) resuAgg.getRawResults().get("result");
		if(dbdList.isEmpty()){
			return 0;
		}else{
			BasicDBObject bdb = (BasicDBObject) dbdList.get(0);
			return bdb.getDouble("sumAddFeeAmnt");
		}
	}

	@Override
	public double getInvalidIpsnMoney(String applNo,List<String> levelList){ 
		BasicDBList level= new BasicDBList();
		for(String levelCode :levelList){
			level.add(levelCode);
		}
		double money;
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo)
						.and("procStat").is("O")
						.and("levelCode").in(level)),
				Aggregation.group("sumJe").sum("grpPayAmount").as("sumPay")
		);
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		if(bdbl.isEmpty()){
			money=0;
		}else{
			BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
			money = obj.getDouble("sumPay");
		}
		return money;
	}

	@Override
	public double getInvalidPolicyMoney(String applNo, String polCode) {
		double money;
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("O")),
				Aggregation.unwind("subStateList"),
				Aggregation.match(Criteria.where("subStateList.polCode").regex(polCode)),
				Aggregation.group("sumJe").sum("subStateList.premium").as("sumPay")
		);
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		if(bdbl.isEmpty()){
			money=0;
		}else{
			BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
			money = obj.getDouble("sumPay");
		}
		return money;

	}

	@Override
	public List<GrpInsured> getGrpInsuredList(String applNo, List<String> levelList) {
		BasicDBList dbd= new BasicDBList();
		dbd.add("N");
		dbd.add("E");
		Query query = new Query();
		Criteria cri = new Criteria();
		cri.and("applNo").is(applNo);
		cri.and("procStat").in(dbd);
		
		if(levelList !=null && !levelList.isEmpty()){
			BasicDBList level= new BasicDBList();
			for(String levelCode :levelList){
				level.add(levelCode);
			}
			cri.and("levelCode").in(level);
		}
		query.addCriteria(cri);
		List<GrpInsured> list = mongoTemplate.find(query, GrpInsured.class);
		return list;
	}
}
