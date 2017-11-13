package com.newcore.orbpsutils.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.PauseTransData;
import com.newcore.orbps.models.finance.QueryPauseTransInfoBean;
import com.newcore.orbpsutils.dao.api.MioNot2bankDao;
import com.newcore.supports.models.service.bo.NullValue;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.service.api.PageQueryService;

@Repository("mioNot2bankDao")
public class MioNot2bankDaoImpl implements MioNot2bankDao {
	@Autowired
    JdbcOperations jdbcTemplate;
	
	@Autowired
	PageQueryService pageQueryService;
	
	private Logger logger = LoggerFactory.getLogger(MioNot2bankDaoImpl.class);
	
	/**
	 * 表名
	 */
    private static final String TABLE_NAME = "MIOS_NOT2BANK";
    
	
	@Override
	public int queryMioNot2bankRow(MiosNotToBank mioToBank) {
		StringBuilder sbd = new StringBuilder();
		sbd.append(" select count(*) as counts from MIOS_NOT2BANK  ");
		sbd.append(" where re_trans_date <= to_date(to_char(sysdate,'yyyy/MM/dd'),'yyyy/MM/dd') ");
		sbd.append(" and invalid_stat='0' and plnmio_rec_id ='"+mioToBank.getPlnmioRecId()+"' ");
		
		return jdbcTemplate.queryForObject(sbd.toString(),Integer.class);
	}

	@Override
	public boolean updateMioNot2bank(MiosNotToBank mioToBank) {
		StringBuilder sbd = new StringBuilder();
		sbd.append(" update mios_not2bank set ");
		sbd.append(" invalid_stat='1',cancel_time=to_date(to_char(sysdate,'yyyy/MM/dd'),'yyyy/MM/dd'),");
		sbd.append(" cancel_reason='系统自动到期处理',cancel_flag='1' ");
		sbd.append(" where trans_flag not in ('2','4','5') and invalid_stat='0' ");
		sbd.append(" and re_trans_date <= to_date(to_char(sysdate,'yyyy/MM/dd'),'yyyy/MM/dd') ");
		sbd.append(" and plnmio_rec_id ='"+mioToBank.getPlnmioRecId()+"' ");
		int row = jdbcTemplate.update(sbd.toString());
		return row>0;
	}
	
	@Override
	public boolean insertMioNot2bank(MiosNotToBank mioToBank) {
        Assert.notNull(mioToBank);
        SimpleJdbcInsert insert = DaoUtils.createJdbcInsert();
        insert.setTableName(TABLE_NAME);
        int rows = insert.execute(DaoUtils.toColumnMap(mioToBank));
		return rows>0;
	}
	
	@Override
	public MiosNotToBank queryMioNot2bankInfo(PauseTransData pauseTransData) {
		Assert.notNull(pauseTransData);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from mios_not2bank where invalid_stat=0");
		sql.append(" and plnmio_rec_id=");
		sql.append(pauseTransData.getPlnmioRecId());
		sql.append(" and cntr_no='");
		sql.append(pauseTransData.getCntrNo());
		sql.append("' and plnmio_date=to_date('");
		sql.append(pauseTransData.getPlnmioDate());
		sql.append("','yyyy-mm-dd') and mio_item_code='");
		sql.append(pauseTransData.getMioItemCode());
		sql.append("'");
		try {
			MiosNotToBank mios =  jdbcTemplate.queryForObject(sql.toString(), new CustomBeanPropertyRowMapper<MiosNotToBank>(MiosNotToBank.class));
			return mios;
		} catch (EmptyResultDataAccessException e) {
			this.logger.info("查询不转账数据为空！"+e);
			return null;
		}
		
	
	}

	@Override
	public List<MiosNotToBank> queryHistoryNotTransInfo(QueryPauseTransInfoBean bean, PageQuery<NullValue> page) {
		Assert.notNull(bean);
		
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlWhere = new StringBuilder();
		sql.append("select * from mios_not2bank ");
		
		if(bean.getBankRegFlag()==2){
			sqlWhere.append(" invalid_stat=0 and   ( (mio_item_code!='FA' and exists (select 1 from plnmio_rec where plnmio_rec_id = mios_not2bank.plnmio_rec_id)) or mio_item_code='FA' ) ");
		}
		if(!"".equals(bean.getCntrNo()) && !(bean.getCntrNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cntr_no='");
			}else{
				sqlWhere.append(" and cntr_no='");
			}
			sqlWhere.append(bean.getCntrNo()+"'");
		}
		if(!"".equals(bean.getPlnmioDate()) && !(bean.getPlnmioDate() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" plnmio_date=to_date('");
			}else{
				sqlWhere.append(" and plnmio_date=to_date('");
			}
			sqlWhere.append(bean.getPlnmioDate()+"','yyyy-mm-dd')");
		}
		if(bean.getMioClass()!=0){
			if(sqlWhere.length()==0){
				sqlWhere.append(" mio_class=");
			}else{
				sqlWhere.append(" and mio_class=");
			}
			sqlWhere.append(bean.getMioClass());
		}
		if(!"".equals(bean.getBankAccNo()) && !(bean.getBankAccNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" bank_acc_no='");
			}else{
				sqlWhere.append(" and bank_acc_no='");
			}
			sqlWhere.append(bean.getBankAccNo()+"'");
		}
		if(!"".equals(bean.getCustNo()) && !(bean.getCustNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cust_no='");
			}else{
				sqlWhere.append(" and cust_no='");
			}
			sqlWhere.append(bean.getCustNo()+"'");
		}
		if(!"".equals(bean.getMioItemCode()) && !(bean.getMioItemCode() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" mio_item_code='");
			}else{
				sqlWhere.append(" and mio_item_code='");
			}
			sqlWhere.append(bean.getMioItemCode()+"'");
		}
		
		if(!"".equals(bean.getEnterSdate()) && !(bean.getEnterSdate() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" enter_time>=to_date('");
			}else{
				sqlWhere.append(" and enter_time>=to_date('");
			}
			sqlWhere.append(bean.getEnterSdate()+" 00:00:00','YYYY-MM-DD HH24:MI:SS')");
		}
		if(!"".equals(bean.getEnterEdate()) && !(bean.getEnterEdate() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" enter_time<=to_date('");
			}else{
				sqlWhere.append(" and enter_time<=to_date('");
			}
			sqlWhere.append(bean.getEnterEdate()+" 23:59:59','YYYY-MM-DD HH24:MI:SS')");
		}
		if(!"".equals(bean.getCancelSdate()) && !(bean.getCancelSdate() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cancel_time>=to_date('");
			}else{
				sqlWhere.append(" and cancel_time>=to_date('");
			}
			sqlWhere.append(bean.getCancelSdate()+" 00:00:00','YYYY-MM-DD HH24:MI:SS')");
		}
		if(!"".equals(bean.getCancelEdate()) && !(bean.getCancelEdate() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cancel_time<=to_date('");
			}else{
				sqlWhere.append(" and cancel_time<=to_date('");
			}
			sqlWhere.append(bean.getCancelEdate()+" 23:59:59','YYYY-MM-DD HH24:MI:SS')");
		}
		
		if(!"".equals(bean.getEnterBranchNo()) && !(bean.getEnterBranchNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" enter_branch_no='");
			}else{
				sqlWhere.append(" and enter_branch_no='");
			}
			sqlWhere.append(bean.getEnterBranchNo()+"'");
		}
		if(!"".equals(bean.getEnterClerkNo()) && !(bean.getEnterClerkNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" enter_clerk_no='");
			}else{
				sqlWhere.append(" and enter_clerk_no='");
			}
			sqlWhere.append(bean.getEnterClerkNo()+"'");
		}
		if(!"".equals(bean.getCancelBranchNo()) && !(bean.getCancelBranchNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cancel_branch_no='");
			}else{
				sqlWhere.append(" and cancel_branch_no='");
			}
			sqlWhere.append(bean.getCancelBranchNo()+"'");
		}
		if(!"".equals(bean.getCancelClerkNo()) && !(bean.getCancelClerkNo() == null)){
			if(sqlWhere.length()==0){
				sqlWhere.append(" cancel_clerk_no='");
			}else{
				sqlWhere.append(" and cancel_clerk_no='");
			}
			sqlWhere.append(bean.getCancelClerkNo()+"'");
		}
		if(bean.getBankRegFlag()==2){
			if(sqlWhere.length()==0){
				sqlWhere.append(" trans_flag=");
			}else{
				sqlWhere.append(" and trans_flag=");
			}
			sqlWhere.append(bean.getTransFlag());
		}
		if(sqlWhere.length()>0){
			sql.append(" where ");
			sql.append(sqlWhere);
		}
		sql.append(" order by cntr_no,enter_time,mio_class,mio_item_code,plnmio_date");
		
		String pageSql = pageQueryService.buildPageQuerySql(sql.toString(), page);
		this.logger.info("pageSql:"+pageSql);
		
		return jdbcTemplate.query(pageSql, new CustomBeanPropertyRowMapper<MiosNotToBank>(MiosNotToBank.class));
	}

	
}
