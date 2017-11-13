package com.newcore.orbps.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.mongodb.BasicDBList;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.QueryAccInfoDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.service.bo.QueryEarnestAccInfoBean;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 查询账户信息
 * @author LJF
 * 2017年2月22日 20:19:55
 */
@Repository("queryAccInfoDaoService")
public class QueryAccInfoDaoImpl implements QueryAccInfoDao {

	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	private static Logger logger = LoggerFactory.getLogger(QueryAccInfoDaoImpl.class);

	public static final String  TASK_STATUS_NEW  = "N";

	private static final String INSERT_MIO_ACC_INFO_LOG = "insert into MIO_ACC_INFO_LOG";

	private static final String SELECT_S_MIO_ACC_INFO_LOG_SQL = "select S_MIO_ACC_INFO_LOG.nextval from dual";

	@Override
	public Long getIpsnNoList(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean) {

		Long ipsnNo = null ;
		Query queryGrpInsured = new Query();
		Criteria criteriaGrpInsured = new Criteria();
		BasicDBList dbd = new BasicDBList();
		for (Long  feeGrpNo : queryEarnestAccInfoBean.getCondition().getFeeGrpNoList()) {
			dbd.add(feeGrpNo);
		}
		//根据被保人5要素
		criteriaGrpInsured.and("applNo").is(queryEarnestAccInfoBean.getCondition().getApplNo());
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnName())){
			criteriaGrpInsured.and("ipsnName").is(queryEarnestAccInfoBean.getCondition().getIpsnName());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnSex())){
			criteriaGrpInsured.and("ipsnSex").is(queryEarnestAccInfoBean.getCondition().getIpsnSex());
		}
		if(null != queryEarnestAccInfoBean.getCondition().getIpsnBirthDate()){
			criteriaGrpInsured.and("ipsnBirthDate").is(queryEarnestAccInfoBean.getCondition().getIpsnBirthDate());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdType())){
			criteriaGrpInsured.and("ipsnIdType").is(queryEarnestAccInfoBean.getCondition().getIpsnIdType());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdNo())){
			criteriaGrpInsured.and("ipsnIdNo").is(queryEarnestAccInfoBean.getCondition().getIpsnIdNo());
		}

		criteriaGrpInsured.and("feeGrpNo").in(dbd);
		queryGrpInsured.addCriteria(criteriaGrpInsured);

		GrpInsured grpInsured =	mongoTemplate.findOne(queryGrpInsured, GrpInsured.class);

		if(null!=grpInsured){
			ipsnNo = grpInsured.getIpsnNo();
		}

		return ipsnNo;
	}

	@Override
	public Long getIpsnNoListBy(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean, List<String> levelCodeList) {
		Long ipsnNo = null ;
		Query queryGrpInsured = new Query();
		Criteria criteriaGrpInsured = new Criteria();
		BasicDBList dbd = new BasicDBList();
		for (String  levelCode : levelCodeList) {
			dbd.add(levelCode);
		}
		//根据被保人5要素
		criteriaGrpInsured.and("applNo").is(queryEarnestAccInfoBean.getCondition().getApplNo());
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnName())){
			criteriaGrpInsured.and("ipsnName").is(queryEarnestAccInfoBean.getCondition().getIpsnName());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnSex())){
			criteriaGrpInsured.and("ipsnSex").is(queryEarnestAccInfoBean.getCondition().getIpsnSex());
		}
		if(null != queryEarnestAccInfoBean.getCondition().getIpsnBirthDate()){
			criteriaGrpInsured.and("ipsnBirthDate").is(queryEarnestAccInfoBean.getCondition().getIpsnBirthDate());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdType())){
			criteriaGrpInsured.and("ipsnIdType").is(queryEarnestAccInfoBean.getCondition().getIpsnIdType());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdNo())){
			criteriaGrpInsured.and("ipsnIdNo").is(queryEarnestAccInfoBean.getCondition().getIpsnIdNo());
		}

		criteriaGrpInsured.and("levelCode").in(dbd);
		queryGrpInsured.addCriteria(criteriaGrpInsured);

		GrpInsured grpInsured =	mongoTemplate.findOne(queryGrpInsured, GrpInsured.class);

		if(null!=grpInsured){
			ipsnNo = grpInsured.getIpsnNo();
		}
		return ipsnNo;
	}

	public List<Long> getIpsnNoListByQueryEarnestAccInfoBean(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean) {
		//【收费组】为空，【组织架构树节点单位名称】不为空时 ,根据levelCodeList查询 被保人序号信息
		List<Long> ipsnNoListAll = new ArrayList<>();
		//根据被保人5要素
		Query queryGrpInsured = new Query();
		Criteria criteriaGrpInsured = new Criteria();

		//根据被保人5要素
		criteriaGrpInsured.and("applNo").is(queryEarnestAccInfoBean.getCondition().getApplNo());
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnName())){
			criteriaGrpInsured.and("ipsnName").is(queryEarnestAccInfoBean.getCondition().getIpsnName());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnSex())){
			criteriaGrpInsured.and("ipsnSex").is(queryEarnestAccInfoBean.getCondition().getIpsnSex());
		}
		if(null != queryEarnestAccInfoBean.getCondition().getIpsnBirthDate()){
			criteriaGrpInsured.and("ipsnBirthDate").is(queryEarnestAccInfoBean.getCondition().getIpsnBirthDate());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdType())){
			criteriaGrpInsured.and("ipsnIdType").is(queryEarnestAccInfoBean.getCondition().getIpsnIdType());
		}
		if(!StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdNo())){
			criteriaGrpInsured.and("ipsnIdNo").is(queryEarnestAccInfoBean.getCondition().getIpsnIdNo());
		}
		queryGrpInsured.addCriteria(criteriaGrpInsured);

		List<GrpInsured> grpInsuredlist = new ArrayList<>();
		grpInsuredlist  =	mongoTemplate.find(queryGrpInsured, GrpInsured.class);
		if (!grpInsuredlist.isEmpty()) {
			for (GrpInsured grpInsured : grpInsuredlist) {
				ipsnNoListAll.add(grpInsured.getIpsnNo());
			}
		}
		return ipsnNoListAll;
	}


	@Override
	public List<EarnestAccInfo> queryEarnestAccInfo(List<String> accNoList , Integer pageSize , long pageStartNo) {

		pageStartNo = pageStartNo+1;
		StringBuilder strAccNoList = new StringBuilder();
		//遍历账号accNoList
		for (String strAccNo : accNoList) {
			strAccNoList.append(" '");
			strAccNoList.append(strAccNo);
			strAccNoList.append("' ,");
		}
		String	str = strAccNoList.substring(0,strAccNoList.length()-1);
		StringBuilder sql = new StringBuilder();

		sql.append("select * from (select t1.*, rownum rn from (select * from EARNEST_ACC_INFO t2");
		sql.append(" WHERE t2.ACC_NO in (");
		sql.append(str);
		sql.append(")");
		sql.append(")");
		sql.append(" t1 where rownum< '");
		sql.append(pageStartNo+pageSize);
		sql.append("')");
		sql.append(" where rn>= '");
		sql.append(pageStartNo);
		sql.append("'");
		List<EarnestAccInfo> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		return list;
	}

	@Override
	public List<EarnestAccInfo> queryEarnestAccInfoByApplNo(String applNo , Integer pageSize , long pageStartNo) {

		pageStartNo = pageStartNo+1;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from (select t1.*, rownum rn from (select * from EARNEST_ACC_INFO t2");
		sql.append(" WHERE t2.ACC_NO like '");
		sql.append(applNo+"%");
		sql.append("')");
		sql.append(" t1 where rownum< '");
		sql.append(pageStartNo+pageSize);
		sql.append("')");
		sql.append(" where rn>= '");
		sql.append(pageStartNo);
		sql.append("'");
		List<EarnestAccInfo> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		return list;
	}

	@Override
	public BigDecimal querySumBalanceEarnestAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(balance) from EARNEST_ACC_INFO ");
		sql.append(" WHERE ACC_NO like'");
		sql.append(applNo+"%");
		sql.append("'");
		return (BigDecimal) jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
	}
	@Override
	public BigDecimal queryNumAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from EARNEST_ACC_INFO ");
		sql.append(" WHERE ACC_NO like'");
		sql.append(applNo+"%");
		sql.append("'");
		return (BigDecimal) jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
	}

	@Override
	public boolean getProcStat(String cntrNo, String lockFlag, String procStat) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from PLNMIO_REC");
		sql.append(" WHERE PROC_STAT='");
		sql.append(procStat);
		sql.append("'");
		sql.append(" AND LOCK_FLAG ='");
		sql.append(lockFlag);
		sql.append("'");
		sql.append(" AND CNTR_NO like '");
		sql.append(cntrNo);
		sql.append("%'");
		List<PlnmioRec> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		return list.size()>0;
	}

	@Override
	public boolean updateEarnestAccInfo(String accNo , Double amnt){
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE EARNEST_ACC_INFO SET BALANCE = '"); 
		sql.append(amnt);
		sql.append("'  WHERE ACC_NO='");
		sql.append(accNo);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}

	@Override
	public boolean insertEarnestAccInfo(MioAccInfoLog mioAccInfoLog) {
		StringBuffer sql = new StringBuffer();
		sql.append(INSERT_MIO_ACC_INFO_LOG);
		sql.append(" values(?,?,?,?,?,?,?)");
		int row=jdbcTemplate.update(sql.toString(),	
				mioAccInfoLog.getAccLogId(),mioAccInfoLog.getAccId(),mioAccInfoLog.getCreateTime(),mioAccInfoLog.getMioItemCode(),	
				mioAccInfoLog.getMioClass(),mioAccInfoLog.getAnmt(),mioAccInfoLog.getRemark());
		return row>0;
	}

	@Override
	public BigDecimal getMioLog(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag) {
		BigDecimal sunAmnt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(AMNT) from mio_log");
		sql.append(" WHERE BANK_CODE='");
		sql.append(bankCode);
		sql.append("'");
		sql.append(" AND BANK_ACC_NO ='");
		sql.append(bankAccNo);
		sql.append("'");
		if(StringUtils.equals("I", elementFlag)){
			sql.append(" AND IPSN_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else if(StringUtils.equals("P", elementFlag)){
			sql.append(" AND FEE_GRP_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else{
			sql.append(" AND LEVEL_CODE ='");
			sql.append(element);
			sql.append("'");
		}
		sql.append(" AND  INSTR(CNTR_NO, '");
		sql.append(cntrNo);
		sql.append("')>0");
		sql.append(" AND mio_class = 1 ");
		sql.append(" AND mio_item_code='FA' ");
		try {
			sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return sunAmnt;
		}
		return sunAmnt;
	}

	@Override
	public EarnestAccInfo queryOneEarnestAccInfoByApplNo(String applNo) {
		EarnestAccInfo earnestAccInfo = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM EARNEST_ACC_INFO");
		sql.append(" WHERE ACC_NO = '");
		sql.append(applNo);
		sql.append("'");
		try {
			earnestAccInfo = jdbcTemplate.queryForObject(sql.toString(), 
					new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return earnestAccInfo;
		}
		return earnestAccInfo;
	}

	@Override
	public BigDecimal querySumFrozenBalanceEarnestAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(frozen_balance) from EARNEST_ACC_INFO ");
		sql.append(" WHERE ACC_NO like'");
		sql.append(applNo+"%");
		sql.append("'");
		return (BigDecimal) jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
	}

	@Override
	public long selectMioAccInfoLog() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SELECT_S_MIO_ACC_INFO_LOG_SQL, long.class);
	}

	@Override
	public boolean insertProcEarnestPayTask(String applNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into PROC_EARNEST_PAY_TASK (task_seq , status ,create_time , appl_no)");
		sql.append(" values(S_PROC_EARNEST_PAY_TASK.nextval,?,?,?)");
		int row=jdbcTemplate.update(sql.toString(),	TASK_STATUS_NEW, new Date() , applNo);
		return row>0;
	}

	@Override
	public int findProcEarnestPayTask(String applNo) {
		int count = 0;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM PROC_EARNEST_PAY_TASK");
		sql.append(" WHERE APPL_NO = '");
		sql.append(applNo);
		sql.append("'");
		sql.append(" AND STATUS in ('N' , 'K' , 'E')");
		try {
			count =  jdbcTemplate.queryForObject(sql.toString(), int.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return count;
		}
		return count;
	}

	@Override
	public int queryCountEarnestAccInfoByApplNo(String applNo) {
		int count = 0 ;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM EARNEST_ACC_INFO");
		sql.append(" WHERE ACC_NO like '");
		sql.append(applNo+"%");
		sql.append("'");
		sql.append(" AND BALANCE > 0 ");
		try {
			count = jdbcTemplate.queryForObject(sql.toString(),  int.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return count;
		}
		return count;
	}

	@Override
	public BigDecimal getPlnmioRec(String cntrNo, String bankAccNo, String bankCode, String element,
			String elementFlag) {
		BigDecimal sunAmnt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(AMNT) from PLNMIO_REC");
		sql.append(" WHERE BANK_CODE='");
		sql.append(bankCode);
		sql.append("'");
		sql.append(" AND BANK_ACC_NO ='");
		sql.append(bankAccNo);
		sql.append("'");
		if(StringUtils.equals("I", elementFlag)){
			sql.append(" AND IPSN_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else if(StringUtils.equals("P", elementFlag)){
			sql.append(" AND FEE_GRP_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else{
			sql.append(" AND LEVEL_CODE ='");
			sql.append(element);
			sql.append("'");
		}
		sql.append(" AND CNTR_NO = '");
		sql.append(cntrNo);
		sql.append("'");
		sql.append(" AND mio_class =-1 ");
		sql.append(" AND mio_item_code = 'FA' ");
		sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		return sunAmnt;
	}

}
