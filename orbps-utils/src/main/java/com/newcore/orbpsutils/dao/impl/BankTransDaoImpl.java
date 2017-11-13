package com.newcore.orbpsutils.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.models.banktrans.ExtractBankTransBean;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.BankTransInfoDetailsVo;
import com.newcore.orbps.models.finance.BankTransingInfoVo;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.orbpsutils.math.DateUtils;
import com.newcore.supports.dicts.TRAN_STATE;

@Repository("bankTransDao")
public class BankTransDaoImpl implements BankTransDao {
	 /**
     * JDBC操作工具
     */
    @Autowired
    JdbcOperations jdbcTemplate;

	@Override
	public boolean insertBankTrans(List<BankTrans> btList) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into BANK_TRANS "); 
		sql.append(" values(S_BANK_TRANS.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//表中共54个字段，主键设置了触发器，插入数据时，自增，因此下面只写了53个需要插入的字段
		int[][] rows=jdbcTemplate.batchUpdate(sql.toString(), btList, btList.size(), new ParameterizedPreparedStatementSetter<BankTrans>(){
			@Override
			public void setValues(PreparedStatement ps, BankTrans bank) throws SQLException {
				ps.setString(1,bank.getTransStat());		//处理状态(保险公司标准)	
				ps.setLong(2,bank.getPlnmioRecId());		//应收付记录标识
				ps.setString(3,bank.getPlnmioRecIdList());	//多个应收主键 id1,id2..
				ps.setLong(4,bank.getTransBatSeq());	 	//转帐批号
				ps.setString(5,bank.getBankCode());			//银行代码
				ps.setString(6,bank.getBranchBankAccNo());	//保险公司银行帐号
				ps.setString(7,bank.getAccCustName()); 		//帐户所有人名称
				ps.setString(8,bank.getAccCustIdType());	//帐户所有人证件类别
				ps.setString(9,bank.getAccCustIdNo()); ;	//帐户所有人证件号
				ps.setString(10,bank.getBankAccNo()); ;		//银行帐号
				ps.setString(11,bank.getMgrBranchNo()); ;  //管理机构
				ps.setString(12,bank.getCntrNo()); ;  		//合同号
				ps.setLong(13,bank.getIpsnNo()); 		 	//被保人顺序号
				ps.setString(14,bank.getTransClass());		//转帐类型
				ps.setInt(15,bank.getMioClass()); 			//收付类型
				ps.setString(16,bank.getMioItemCode());		//收付项目代码
				ps.setDate(17,DateUtils.getDate(bank.getPlnmioDate()));//应收/付日期
				ps.setDate(18,DateUtils.getDate(bank.getMioDate()));	//实收/付日期
				ps.setBigDecimal(19,bank.getTransAmnt());		//金额
				ps.setString(20,bank.getCustNo());				//客户号
				ps.setDate(21,DateUtils.getDate(bank.getCreateDate()));//生成日期	
				ps.setLong(22,bank.getBtMioTxNo());		//转账交易号
			}
		});
		return rows.length>0;
	}

	@Override
	public boolean updateBankTransTransStat(String transCode,String transStat,long transBatSeq) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update bank_trans set TRANS_STAT =?,TRANS_BAT_SEQ =? ");
		//处理查询条件，超过1000个后，用OR拼接
		String [] code = transCode.split(",");
		for(int i=0;i<code.length;i++){
			if(i==0){
				sql.append(" WHERE TRANS_CODE in (");
			}else if(i%999==0){
				sql.append(" OR TRANS_CODE IN(");
			}
			sql.append(code[i]);
			if((i%999==998) || i==code.length-1){
				sql.append(")");
			}else{
				sql.append(",");
			}
		}
		int rows = jdbcTemplate.update(sql.toString(), transStat,transBatSeq);
		return rows>0;
	}

	@Override
	public List<BankTrans> queryBankTransList(ExtractBankTransBean bean) {
		StringBuilder sbr = new StringBuilder();
		sbr.append(" select * from bank_trans ");
		sbr.append(" where trans_stat ='"+TRAN_STATE.NEW.getKey()+"'") ; //处理状态
		if(!"".equals(bean.getBankCode())){ //银行代码
			sbr.append(" and bank_code ='"+bean.getBankCode()+"'") ; 
		}
		if(!"".equals(bean.getMioClass())){
			sbr.append(" and mio_class ='"+bean.getMioClass()+"'") ; 
		}
		if(bean.getFcFlag() ==0){
			//不包含下属机构号
			if(!"".equals(bean.getBranchNo())){
				sbr.append(" and mgr_branch_no ='"+bean.getBranchNo()+"'") ;   
			}
		}else{
			//包含下属机构号
			sbr.append(" and mgr_Branch_No in ("+bean.getBranchNo()+")"); 
		}
		sbr.append(" and rownum <= 300 ");
		List<BankTrans> list = jdbcTemplate.query(sbr.toString(), new CustomBeanPropertyRowMapper<BankTrans>(BankTrans.class));
		return list;
	}

	@Override
	public BankTrans queryBankTransByTransCode(Long transCode) {
		String sql = "select * from bank_trans where trans_code="+transCode;
		BankTrans bank = jdbcTemplate.queryForObject(sql, new CustomBeanPropertyRowMapper<BankTrans>(BankTrans.class));
		return bank;
	}

	@Override
	public boolean deleteBankTrans(Long transCode) {
		String sql = "delete bank_trans where trans_code=?";
		int rows = jdbcTemplate.update(sql,transCode);
		return rows>0;
	}

	@Override
	public List<BankTrans> queryBankTransingInfo(BankTransingInfoVo bankTransingInfoVo) {
		StringBuilder sql = new StringBuilder("SELECT * FROM BANK_TRANS WHERE 1=1");
		if(bankTransingInfoVo == null){
			return new ArrayList<>();
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getCntrNo())){
			sql.append(" AND instr(cntr_No,'");
			sql.append(bankTransingInfoVo.getCntrNo());
			sql.append("') >0 ");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getAccCustName())){
			sql.append(" AND ACC_CUST_NAME = '");
			sql.append(bankTransingInfoVo.getAccCustName());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getBankaccIdNo())){
			sql.append(" AND ACC_CUST_ID_NO = '");
			sql.append(bankTransingInfoVo.getBankaccIdNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getBankAccNo())){
			sql.append(" AND BANK_ACC_NO = '");
			sql.append(bankTransingInfoVo.getBankAccNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getBankCode())){
			sql.append(" AND BANK_CODE = '");
			sql.append(bankTransingInfoVo.getBankCode());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getMioItemCode())){
			sql.append(" AND MIO_ITEM_CODE = '");
			sql.append(bankTransingInfoVo.getMioItemCode());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getPlnmioSdate())){
			sql.append(" AND PLNMIO_DATE >= TO_DATE('");
			sql.append(bankTransingInfoVo.getPlnmioSdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getPlnmioEdate())){
			sql.append(" AND PLNMIO_DATE <= TO_DATE('");
			sql.append(bankTransingInfoVo.getPlnmioEdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getMioSdate())){
			sql.append(" AND MIO_DATE >= TO_DATE('");
			sql.append(bankTransingInfoVo.getMioSdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getMioEdate())){
			sql.append(" AND MIO_DATE <= TO_DATE('");
			sql.append(bankTransingInfoVo.getMioEdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(bankTransingInfoVo.getMioClass()) && !StringUtils.equals(bankTransingInfoVo.getMioClass(),"0")){
			sql.append(" AND MIO_CLASS = ");
			sql.append(bankTransingInfoVo.getMioClass());
		}
		sql.append(" AND TRANS_BAT_SEQ = -1");
		List<BankTrans> bankTransList = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<BankTrans>(BankTrans.class));
		if(null==bankTransList){
			return new ArrayList<>();
		}
		return bankTransList;
	}

	@Override
	public List<BankTrans> queryOperateBankTransInfoDetails(BankTransInfoDetailsVo bankTransInfoDetailsVo) {
		StringBuilder sql = new StringBuilder("SELECT * FROM BANK_TRANS WHERE 1=1");
		if(!StringUtils.isEmpty(bankTransInfoDetailsVo.getBankCode())){
			sql.append(" AND BANK_CODE = '");
			sql.append(bankTransInfoDetailsVo.getBankCode());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(bankTransInfoDetailsVo.getMioClass())){
			sql.append(" AND MIO_CLASS = ");
			sql.append(bankTransInfoDetailsVo.getMioClass());
		}
		if(!StringUtils.isEmpty(bankTransInfoDetailsVo.getMinGenDate())){
			sql.append(" AND CREATE_DATE >= TO_DATE('");
			sql.append(bankTransInfoDetailsVo.getMinGenDate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(bankTransInfoDetailsVo.getMaxGenDate())){
			sql.append(" AND CREATE_DATE <= TO_DATE('");
			sql.append(bankTransInfoDetailsVo.getMaxGenDate());
			sql.append("','yyyy-mm-dd')");
		}
		if(bankTransInfoDetailsVo.getMgrBranchNo()!=null && !bankTransInfoDetailsVo.getMgrBranchNo().isEmpty()){
			sql.append(" AND (");
			for(int i=0;i<bankTransInfoDetailsVo.getMgrBranchNo().size();i++){
				if(i==0){
					sql.append("MGR_BRANCH_NO IN(");
				}else if(i%999==0){
					sql.append(" OR MGR_BRANCH_NO IN(");
				}
				sql.append("'");
				sql.append(bankTransInfoDetailsVo.getMgrBranchNo().get(i));
				sql.append("',");
				if((i%999==998)||i==bankTransInfoDetailsVo.getMgrBranchNo().size()-1){
					sql.append("'')");
				}
			}
			sql.append(")");
		}
		List<BankTrans> bankTransList = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<BankTrans>(BankTrans.class));
		if(null==bankTransList){
			return new ArrayList<>();
		}
		return bankTransList;
	}

	@Override
	public long getTransBatSeq() {
		String sql= "select S_TRANS_BAT_SEQ.nextval from dual";
		return jdbcTemplate.queryForObject(sql, long.class);
	}

}
