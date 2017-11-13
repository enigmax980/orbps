package com.newcore.orbps.dao.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.newcore.orbps.dao.api.ProcPSInfoDao;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.OperateBankTransInBean;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;


@Repository("procPSInfoDao")
public class ProcPSInfoDaoImpl implements ProcPSInfoDao {


	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoTemplate mongoTemplate;

	private static final String DEFAULT_UALUE_MINUS_ONR = "-1";

	private static final String DEFAULT_UALUE_ONR = "1";


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
	public List<GrpInsured> getGrpInsuredList(String applNo, List<String> levelList) {
		BasicDBList dbd= new BasicDBList();
		dbd.add("N");
		dbd.add("E");
		Query query = new Query();
		Criteria cri = new Criteria();
		cri.and("applNo").is(applNo);
		cri.and("procStat").in(dbd);

		if(null != levelList && !levelList.isEmpty()){
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
	public double getPlnmioRecAmntBankTrans(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT AMNT FROM PLNMIO_REC");
		sql.append(" WHERE CNTR_NO like '");
		sql.append(applNo+"%");
		sql.append("'");
		sql.append(" AND MIO_CLASS = ");
		sql.append(1);
		sql.append(" AND MIO_ITEM_CODE ='");
		sql.append(MIO_ITEM_CODE.FA.getKey());
		sql.append("'");
		sql.append(" AND MIO_TYPE ='");
		sql.append(MIO_TYPE.T.getKey());
		sql.append("'");
		return jdbcTemplate.queryForObject(sql.toString(), Double.class);
	}

	@Override
	public List<BankTrans> getListBankTrans(OperateBankTransInBean operateBankTransInBean) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM bank_trans");
		sql.append(" WHERE trans_bat_seq = '");
		sql.append(-1);
		sql.append("'");
		sql.append(" AND bank_code = '");
		sql.append(operateBankTransInBean.getBankCode());
		sql.append("'");
		if(!StringUtils.isBlank(operateBankTransInBean.getMgrBranchNo())){
			sql.append(" AND mgr_branch_no = '");
			sql.append(operateBankTransInBean.getMgrBranchNo());
			sql.append("'");
		}
		if(StringUtils.equals(DEFAULT_UALUE_MINUS_ONR, operateBankTransInBean.getMioClass()) 
				|| StringUtils.equals(DEFAULT_UALUE_ONR, operateBankTransInBean.getMioClass())){
			sql.append(" AND mio_class = '");
			sql.append(operateBankTransInBean.getMioClass());
			sql.append("'");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenSdate())){
			String str = operateBankTransInBean.getGenSdate().replace("/", "-");
			sql.append(" AND create_date  >= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenEdate())){
			String str = operateBankTransInBean.getGenEdate().replace("/", "-");
			sql.append(" AND create_date <= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		List<BankTrans> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<BankTrans>(BankTrans.class));
		return list;
	}

	@Override
	public Double getListBankTransAmnt(OperateBankTransInBean operateBankTransInBean) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(trans_amnt) FROM bank_trans");
		sql.append(" WHERE trans_bat_seq = '");
		sql.append(-1);
		sql.append("'");
		sql.append(" AND bank_code = '");
		sql.append(operateBankTransInBean.getBankCode());
		sql.append("'");
		if(!StringUtils.isBlank(operateBankTransInBean.getMgrBranchNo())){
			sql.append(" AND mgr_branch_no = '");
			sql.append(operateBankTransInBean.getMgrBranchNo());
			sql.append("'");
		}
		if(StringUtils.equals(DEFAULT_UALUE_MINUS_ONR, operateBankTransInBean.getMioClass()) 
				|| StringUtils.equals(DEFAULT_UALUE_ONR, operateBankTransInBean.getMioClass())){
			sql.append(" AND mio_class = '");
			sql.append(operateBankTransInBean.getMioClass());
			sql.append("'");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenSdate())){
			String str = operateBankTransInBean.getGenSdate().replace("/", "-");
			sql.append(" AND create_date  >= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenEdate())){
			String str = operateBankTransInBean.getGenEdate().replace("/", "-");
			sql.append(" AND create_date <= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		return jdbcTemplate.queryForObject(sql.toString(), Double.class);
	}

	@Override
	public String getListBankTransMaxCreateDate(OperateBankTransInBean operateBankTransInBean) {
		StringBuilder sql = new StringBuilder();
		sql.append("select create_date from ( SELECT * FROM bank_trans");
		sql.append(" WHERE trans_bat_seq = '");
		sql.append(-1);
		sql.append("'");
		sql.append(" AND bank_code = '");
		sql.append(operateBankTransInBean.getBankCode());
		sql.append("'");
		if(!StringUtils.isBlank(operateBankTransInBean.getMgrBranchNo())){
			sql.append(" AND mgr_branch_no = '");
			sql.append(operateBankTransInBean.getMgrBranchNo());
			sql.append("'");
		}
		if(StringUtils.equals(DEFAULT_UALUE_MINUS_ONR, operateBankTransInBean.getMioClass()) 
				|| StringUtils.equals(DEFAULT_UALUE_ONR, operateBankTransInBean.getMioClass())){
			sql.append(" AND mio_class = '");
			sql.append(operateBankTransInBean.getMioClass());
			sql.append("'");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenSdate())){
			String str = operateBankTransInBean.getGenSdate().replace("/", "-");
			sql.append(" AND create_date  >= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenEdate())){
			String str = operateBankTransInBean.getGenEdate().replace("/", "-");
			sql.append(" AND create_date <= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(" order by create_date desc)");
		sql.append(" where rownum=1 ");
		return jdbcTemplate.queryForObject(sql.toString(), String.class);
	}

	@Override
	public String getListBankTransMinCreateDate(OperateBankTransInBean operateBankTransInBean) {
		StringBuilder sql = new StringBuilder();
		sql.append("select create_date from ( SELECT * FROM bank_trans");
		sql.append(" WHERE trans_bat_seq = '");
		sql.append(-1);
		sql.append("'");
		sql.append(" AND bank_code = '");
		sql.append(operateBankTransInBean.getBankCode());
		sql.append("'");
		if(!StringUtils.isBlank(operateBankTransInBean.getMgrBranchNo())){
			sql.append(" AND mgr_branch_no = '");
			sql.append(operateBankTransInBean.getMgrBranchNo());
			sql.append("'");
		}
		if(StringUtils.equals(DEFAULT_UALUE_MINUS_ONR, operateBankTransInBean.getMioClass()) 
				|| StringUtils.equals(DEFAULT_UALUE_ONR, operateBankTransInBean.getMioClass())){
			sql.append(" AND mio_class = '");
			sql.append(operateBankTransInBean.getMioClass());
			sql.append("'");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenSdate())){
			String str = operateBankTransInBean.getGenSdate().replace("/", "-");
			sql.append(" AND create_date  >= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(!StringUtils.isBlank(operateBankTransInBean.getGenEdate())){
			String str = operateBankTransInBean.getGenEdate().replace("/", "-");
			sql.append(" AND create_date <= to_date('");
			sql.append(str);
			sql.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(" order by create_date asc)");
		sql.append(" where rownum=1 ");
		return jdbcTemplate.queryForObject(sql.toString(), String.class);
	}

}
