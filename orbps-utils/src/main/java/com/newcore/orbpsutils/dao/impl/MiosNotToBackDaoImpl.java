package com.newcore.orbpsutils.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.NotTransInfoVo;
import com.newcore.orbpsutils.dao.api.MiosNotToBackDao;

/**
 * @author wangxiao
 * 创建时间：2017年2月27日下午4:31:06
 */
@Repository("miosNotToBackDao")
public class MiosNotToBackDaoImpl implements MiosNotToBackDao {
	
	private final String FIND_ALL_SQL = "SELECT * FROM MIOS_NOT2BANK WHERE 1=1";
	private final String UPD_MIOS_SQL = "UPDATE mios_not2bank SET invalid_stat=?,trans_flag=?,"
			+ " stop_trans_date=to_date(?,'yyyy-mm-dd'), re_trans_date=to_date(?,'yyyy-mm-dd'),cancel_branch_no=?,cancel_clerk_no=?,"
			+ " cancel_time =to_date(?,'YYYY-MM-DD HH24:MI:SS'),cancel_flag=?,cancel_reason=?"
			+ " WHERE plnmio_rec_id=? AND cntr_no=? AND plnmio_date=to_date(?,'yyyy-mm-dd') AND mio_item_code=? AND"
			+ " invalid_stat=0";
	
    private static Logger logger = LoggerFactory.getLogger(MiosNotToBackDaoImpl.class);
    
	@Autowired
	JdbcOperations jdbcTemplate;
	@Override
	public List<MiosNotToBank> getMiosNotToBanks(NotTransInfoVo notTransInfoVo) {
		List<MiosNotToBank> miosNotToBanks = new ArrayList<>();
		if(null == notTransInfoVo || StringUtils.isEmpty(notTransInfoVo.getBeginDate())){
			return miosNotToBanks;
		}
		String [] ss = notTransInfoVo.getBeginDate().split("\\|");
		if(3!=ss.length){
			return miosNotToBanks;
		}
		StringBuilder sql = new StringBuilder(FIND_ALL_SQL);
		sql.append(" AND PLNMIO_REC_ID NOT IN (SELECT PLNMIO_REC_ID FROM PLNMIO_REC WHERE PROC_STAT='S')");
		//查询类别为1，则直接返回条数为0的数据
		if(StringUtils.equals("1",ss[0])){
			return miosNotToBanks;
		//查询类别为2
		}else if(StringUtils.equals("2",ss[0])){
			sql.append(" AND INVALID_STAT = 0 AND TRANS_FLAG = 2");
		//查询类别为3
		}else if(StringUtils.equals("3",ss[0])){
			sql.append(" AND INVALID_STAT = 0 AND TRANS_FLAG = 0");
			sql.append(" AND RE_TRANS_DATE >= TO_DATE('");
			sql.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
			sql.append("','yyyy-mm-dd')");
		//查询类别为4
		}else if(StringUtils.equals("4",ss[0])){
			sql.append(" AND INVALID_STAT = 1 AND TRANS_FLAG = 3");
		//查询类别为5
		}else if(StringUtils.equals("5",ss[0])){
			sql.append(" AND INVALID_STAT = 0 AND TRANS_FLAG = 4");
		}else{
			return miosNotToBanks;
		}
		
		if(!StringUtils.isEmpty(notTransInfoVo.getCntrNo())){
			sql.append(" AND CNTR_NO = '");
			sql.append(notTransInfoVo.getCntrNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getPlnmioSdate())){
			sql.append(" AND PLNMIO_DATE >=TO_DATE('");
			sql.append(notTransInfoVo.getPlnmioSdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getPlnmioEdate())){
			sql.append(" AND PLNMIO_DATE <=TO_DATE('");
			sql.append(notTransInfoVo.getPlnmioEdate());
			sql.append("','yyyy-mm-dd')");
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getSysNo())){
			sql.append(" AND SYS_NO = '");
			sql.append(notTransInfoVo.getSysNo());
			sql.append("'");
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getMioClass())){
			sql.append(" AND MIO_CLASS = ");
			sql.append(notTransInfoVo.getMioClass());
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getFaFlag())
			&& StringUtils.equals("1", notTransInfoVo.getFaFlag())){
			sql.append(" AND MIO_ITEM_CODE = 'FA'");
		}else if(!StringUtils.isEmpty(notTransInfoVo.getFaFlag())
			&& StringUtils.equals("0", notTransInfoVo.getFaFlag())){
			return miosNotToBanks;
		}
		if(!StringUtils.isEmpty(notTransInfoVo.getMioItemCode())){
			sql.append(" AND MIO_ITEM_CODE = '");
			sql.append(notTransInfoVo.getMioItemCode());
			sql.append("'");
		}
		if(ss[2].matches("[0-9]+")){
			sql.append(" AND ROWNUM>=");
			sql.append(ss[2]);
		}else{
			ss[2] = "1";
			sql.append(" AND ROWNUM>=");
			sql.append("1");
		}
		if(ss[1].matches("[0-9]+")){
			long l = Long.valueOf(ss[2])+Long.valueOf(ss[1])*10;
			sql.append(" AND ROWNUM < ");
			sql.append(String.valueOf(l));
		}else if(StringUtils.equals(ss[1], "*")){
			long l = Long.valueOf(ss[2])+150;
			sql.append(" AND ROWNUM <= ");
			sql.append(String.valueOf(l));
		}
		sql.append(" ORDER BY PLNMIO_REC_ID ASC");
		logger.info(sql.toString());
		miosNotToBanks = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<MiosNotToBank>(MiosNotToBank.class));
		if(null==miosNotToBanks){
			return new ArrayList<>();
		}
		return miosNotToBanks;
	}
	@Override
	public boolean revoMiosNotToBanks(MiosNotToBank miosNotToBank) {
		
		Object [] objArray = {miosNotToBank.getInvalidStat(),miosNotToBank.getTransFlag(),new SimpleDateFormat("yyyy-MM-dd").format(miosNotToBank.getStopTransDate()),new SimpleDateFormat("yyyy-MM-dd").format(miosNotToBank.getReTransDate()),
				miosNotToBank.getCancelBranchNo(),miosNotToBank.getCancelClerkNo(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(miosNotToBank.getCancelTime()),miosNotToBank.getCancelFlag(),
				miosNotToBank.getCancelReason(),miosNotToBank.getPlnmioRecId(),miosNotToBank.getCntrNo(),new SimpleDateFormat("yyyy-MM-dd").format(miosNotToBank.getPlnmioDate()),
				miosNotToBank.getMioItemCode()};
		try {
			return 0 <= jdbcTemplate.update(UPD_MIOS_SQL, objArray)?true:false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002", "撤销暂停送划登记提交 更新数据库异常");
		}
	}
	@Override
	public boolean insMiosNotToBanks(MiosNotToBank miosNotToBank) {
		// TODO Auto-generated method stub
		return false;
	}
}
