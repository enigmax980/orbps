package com.newcore.orbpsutils.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.models.banktrans.BuildBanTransBean;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.finance.PlnmioRecGroupBean;
import com.newcore.orbps.models.finance.QueryPauseTransInfoBean;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.math.DateUtils;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.PLNMIO_STATE;
import com.newcore.supports.dicts.TRAN_STATE;
import com.newcore.supports.service.api.PageQueryService;

@Repository("plnmioRecDao")
public class PlnmioRecDaoImpl implements PlnmioRecDao {
	
	@Autowired
	JdbcOperations jdbcTemplate;
	/**
	 * 分页辅助工具
	 */
	@Autowired
	PageQueryService pageQueryService;

	/**
	 * 数据库表名
	 */
	private static final String TABLE_NAME = "PLNMIO_REC";

	/**
	 * 查询所以字段SQL
	 */
	private static final String SELECT_ALL_DATA_SQL = "select * from PLNMIO_REC";
	/**
	 * 插入SQL
	 */
	private static final String INSERT_DATA_SQL = "insert into PLNMIO_REC";

	private static final String SELECT_PLNMIO_REC_SEQ_SQL = "select S_PLNMIO_REC.nextval from dual";
	
	private static final String  SELECT_PLNMIO_REC_BAT_SQL= "select S_PLNMIO_REC_BAT_NO.nextval from dual";


	@Override
	public PlnmioRec queryPlnmioRec(Long plnmioRecId) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT_ALL_DATA_SQL);
		sql.append(" WHERE plnmio_rec_id='");
		sql.append(plnmioRecId);
		sql.append("'");
		PlnmioRec plnmioRec = (PlnmioRec)jdbcTemplate.queryForObject(sql.toString(),
				new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		return plnmioRec;
	}

	@Override
	public List<PlnmioRec> queryPlnmioRecList(String plnmioRecIds) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT_ALL_DATA_SQL);
		sql.append(" WHERE plnmio_rec_id in (");
		sql.append(plnmioRecIds);
		sql.append(")");
		List<PlnmioRec> list = jdbcTemplate.query(sql.toString(),
				new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		return list;
	}

	@Override
	public List<PlnmioRec> queryPlnmioRecList(Map<String,String> param) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT_ALL_DATA_SQL);
		sql.append(" WHERE ");
		int index =0;
		for(Entry<String, String> entry:param.entrySet()){
			String key = entry.getKey();
			String val = entry.getValue();
			if(index>0){
				//第一次遍历的时候 查询条件不需要 and
				sql.append(" and ");
			}
			sql.append( key +" = '"+ val +"'");
			index++;
		}
		List<PlnmioRec> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		return list;
	}

	@Override
	public boolean insertPlnmioRec(List<PlnmioRec> plnMioList) {
		StringBuilder sql = new StringBuilder();
		sql.append(INSERT_DATA_SQL); 
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//表中共54个字段
		int[][] rows=jdbcTemplate.batchUpdate(sql.toString(), plnMioList, plnMioList.size(), 
				new ParameterizedPreparedStatementSetter<PlnmioRec>(){
			@Override
			public void setValues(PreparedStatement ps, PlnmioRec pln) throws SQLException {
				ps.setLong(1,pln.getPlnmioRecId());
				ps.setString(2,pln.getCntrType());		//合同类型
				ps.setString(3,pln.getSgNo());			//汇缴事件号
				ps.setString(4,pln.getCgNo());			//合同组号=applNo+第1险种
				ps.setString(5,pln.getArcBranchNo());	//(路由)归档机构
				ps.setInt(6,pln.getSignYear());			//(路由)签单年度
				ps.setString(7,pln.getPolCode());		//险种代码
				ps.setString(8,pln.getCntrNo()); 		//保单号/投保单号/帐号
				ps.setString(9,pln.getCurrencyCode());	//保单币种
				ps.setLong(10,pln.getMtnId());			//保全批改流水号:默认为0
				ps.setString(11,pln.getMtnItemCode());  //批改保全项目:默认为0	
				ps.setLong(12,pln.getIpsnNo());		    //被保人序号:默认为0
				ps.setString(13,pln.getMioCustNo());	//领款人/交款人客户号
				ps.setString(14,pln.getMioCustName()); //领款人/交款人姓名 
				ps.setInt(15,pln.getMioClass());		//收付类型:0-应收付；  1-应收； -1-应付
				ps.setDate(16,DateUtils.getDate(pln.getPlnmioDate()));	 	//应收付日期：暂时取保单生效日期
				ps.setDate(17,DateUtils.getDate(pln.getPremDeadlineDate())); 	//保费缴费宽限截止日期 
				ps.setString(18,pln.getMioItemCode());		//收付项目代码:首期暂收 FA
				ps.setString(19,pln.getMioType());			//收付款形式代码		
				ps.setString(20,pln.getMgrBranchNo()); 		//管理机构
				ps.setString(21,pln.getSalesChannel());		//销售渠道		
				ps.setString(22,pln.getSalesBranchNo());	//销售机构号
				ps.setString(23,pln.getSalesNo());			//销售员号		
				ps.setBigDecimal(24,pln.getAmnt()); 		//金额  【注：根据不同的保费来源获计算应缴金额】
				ps.setString(25,pln.getLockFlag());			//锁标志:默认为0  银行转账在途则位1
				ps.setString(26,pln.getBankCode());			//银行代码 【注：根据不同的保费来源获取对应的银行信息】
				ps.setString(27,pln.getBankAccName());      //帐户所有人名称
				ps.setString(28,pln.getAccCustIdType()); 	//帐户所有人证件类别
				ps.setString(29,pln.getAccCustIdNo()); 		//帐户所有人证件号
				ps.setString(30,pln.getBankAccNo());		//银行账号 【注：根据不同的保费来源获取对应的银行信息】
				ps.setString(31,pln.getHoldFlag());			//待转帐标志:默认为1	
				ps.setString(32,pln.getVoucherNo());		//核销凭证号
				ps.setDate(33,DateUtils.getDate(pln.getFinPlnmioDate()));	//财务应收付日期
				ps.setString(34,pln.getClearingMioTxNo());   //清算交易号(收据号)
				ps.setString(35,pln.getMioProcFlag());		//是否收付处理标记:默认是'1'
				ps.setString(36,pln.getRouterNo()); 		//路由号:默认是‘0’
				ps.setLong(37,pln.getAccId());				//关联帐户标识:默认0
				ps.setString(38,pln.getIpsnName());			//被保人姓名
				ps.setString(39,pln.getIpsnSex());			//被保人性别
				ps.setDate(40,DateUtils.getDate(pln.getIpsnBirthDate()));	//被保人出生日期
				ps.setString(41,pln.getIpsnIdType());	//被保人证件类别
				ps.setString(42,pln.getIpsnIdNo());		//被保人证件号
				ps.setString(43,pln.getRemark());			//备注
				ps.setDate(44,DateUtils.getDate(pln.getPlnmioCreateTime()));//生成应收记录时间
				ps.setString(45,pln.getTransStat());	     //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
				ps.setString(46,pln.getProcStat()); 		 //应收状态：N 未收 ,D 作废 ,S 实收，T 在途
				ps.setString(47,pln.getMultiPayAccType());	//账号所属人类别
				ps.setString(48,pln.getLevelCode());	//组织层次代码
				ps.setLong(49,pln.getFeeGrpNo());		//收费组:默认为0
				ps.setString(50,pln.getExtKey1());		//扩展健1
				ps.setString(51,pln.getExtKey2());		//扩展健2
				ps.setString(52,pln.getExtKey3());		//扩展健3
				ps.setString(53,pln.getExtKey4());		//扩展健4
				ps.setString(54,pln.getExtKey5());		//扩展健5
			}
		});
		return rows.length>0;
	}

	@Override
	public boolean updatePlnmioRecByIds(String plnmioRecIds,Map<String,String> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE "); 
		sql.append(TABLE_NAME);
		sql.append(" SET ");
		//参数个数
		int num=paramMap.size();
		int index=0;
		for(Map.Entry<String,String> entry : paramMap.entrySet()){
			index++;
			String key = entry.getKey();
			String value = entry.getValue();
			sql.append(key+" = '"+value+"' ");
			if(num>index){
				sql.append(",");
			}
		}
		
		//处理查询条件，超过1000个后，用OR拼接
		String [] plnmioId = plnmioRecIds.split(",");
		for(int i=0;i<plnmioId.length;i++){
			if(i==0){
				sql.append(" WHERE plnmio_rec_id in (");
			}else if(i%999==0){
				sql.append(" OR plnmio_rec_id IN(");
			}
			sql.append(plnmioId[i]);
			if((i%999==998) || i==plnmioId.length-1){
				sql.append(")");
			}else{
				sql.append(",");
			}
		}
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}

	@Override
	public boolean updatePlnmioRecByCntrNo(String applNo, String pracStat) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE PLNMIO_REC SET PROC_STAT=? WHERE cntr_No='"); 
		sql.append(applNo);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString(), pracStat);
		return row>0;
	}

	@Override
	public boolean updatePlnmioRecInforce(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE PLNMIO_REC SET PROC_STAT='N' WHERE PROC_STAT='D' AND cntr_No='"); 
		sql.append(applNo);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}
	
	@Override
	public boolean updatePlnmioRecProcStatById(long plnmioRecId, String pracStat) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE PLNMIO_REC SET PROC_STAT=? WHERE plnmio_Rec_Id='"); 
		sql.append(plnmioRecId);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString(), pracStat);
		return row>0;
	}

	@Override
	public List<PlnmioRecGroupBean> getPlnmioRecList(BuildBanTransBean bean) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT wm_concat(plnmio_rec_id) as plnmio_rec_id ,cntr_no,mio_class,plnmio_date,mio_item_code,mgr_branch_no,bank_code,bank_Acc_No,bankAcc_Name,sum(AMNT) AMNTS,count(PLNMIO_REC_ID) as count FROM ");
		//lock_Flag=锁标识[未锁定:0]; hold_Flag=待转账标识[待转账:0];  mio_Proc_Flag=是否收付处理标记[未处理:1]
		sql.append( TABLE_NAME );
		sql.append(" WHERE lock_Flag='0' and hold_Flag='0' and mio_Proc_Flag='1' ");
		sql.append(" and trans_Stat in ('U','"+TRAN_STATE.FAILURE.getKey()+"') ");//转账状态:[空] U,[失败] F
		if(bean.getSbFlag() ==0 ){
			//不包含下属机构号
			if(bean.getBranchNo()!= null && !"".equals(bean.getBranchNo())){
				sql.append(" and mgr_Branch_No ='"+bean.getBranchNo()+"'");  //管理机构号
			}
		}else{	
			//包含下属机构号
			sql.append(" and mgr_Branch_No in ("+bean.getBranchNo()+")");  //管理机构号
		}
		
		if(bean.getBankCode()!= null && !"".equals(bean.getBankCode())){
			sql.append(" and bank_Code='"+bean.getBankCode()+"'"); 	//银行代码
		}
		if(bean.getProcDate()!= null && !"".equals(bean.getProcDate())){
			sql.append(" and plnmio_Date <=to_date('"+bean.getProcDate()+"','yyyy/mm/dd')"); //开始处理日期
		}
		//首期续期表记 [0首期] [1续期]
		if(bean.getFcFlag()==0){
			sql.append(" and mio_Item_Code ='FA'"); //首期数据
		}else if(bean.getFcFlag()==1){
			sql.append(" and mio_Item_Code ='PS'"); //分期数据
		}
		sql.append(" and MIO_CLASS ='"+bean.getRpFlag()+"' ");					//收付类型 [1收] [-1付]			
		sql.append(" and mio_Type ='"+MONEYIN_TYPE.TRANSFER.getKey()+"'");		//缴费形式:T银行转账
		sql.append(" and proc_Stat='"+PLNMIO_STATE.UNCOLLECTED.getKey()+"' ");	//应收状态:未收N
		sql.append(" group by cntr_no,mio_class,plnmio_date,mio_item_code,mgr_branch_no,bank_code,bank_Acc_No,bankAcc_Name order by cntr_no,bank_code");
		List<PlnmioRecGroupBean> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<PlnmioRecGroupBean>(PlnmioRecGroupBean.class));
		return list;
	}

	@Override
	public long getPlnmioRecId() {
		return jdbcTemplate.queryForObject(SELECT_PLNMIO_REC_SEQ_SQL, long.class);
	}
	
	@Override
	public boolean getLockFlagByApplNo(String applNo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from plnmio_rec where lock_flag = '1' and cntr_no ='");
		sql.append(applNo);
		sql.append("'");
		
		int i = jdbcTemplate.queryForObject(sql.toString(),Integer.class);
		if(i>0){
			return true;
		}
		return false;
	}
	
	@Override
	public Double getPlnmioRecAmnt(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT AMNT FROM PLNMIO_REC");
		sql.append(" WHERE CNTR_NO = '");
		sql.append(applNo);
		sql.append("'");
		sql.append(" AND MIO_CLASS = ");
		sql.append(1);
		sql.append(" AND MIO_ITEM_CODE ='");
		sql.append(MIO_ITEM_CODE.FA.getKey());
		sql.append("'");
		sql.append(" AND MIO_TYPE !='");
		sql.append(MIO_TYPE.T.getKey());
		sql.append("'");
		return jdbcTemplate.queryForObject(sql.toString(), Double.class);

	}
	
	@Override
	public List<PlnmioRec> queryPlnmioInfoWithPageData(QueryPauseTransInfoBean bean) {
		Assert.notNull(bean);
		StringBuilder sql = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		sql.append("select * from plnmio_rec");
		sql.append(" where mio_type='T' and proc_stat in ('N','T')");
		if(!"".equals(bean.getCntrNo()) && !(bean.getCntrNo() == null)){
			sql.append(" and cntr_no='");
			sql.append(bean.getCntrNo()+"'");
		}
		if(!"".equals(bean.getPlnmioDate()) && !(bean.getPlnmioDate() == null)){
			sql.append(" and plnmio_date='");
			sql.append(bean.getPlnmioDate()+"'");
		}
		if(bean.getMioClass()!=0){
			sql.append(" and mio_class=");
			sql.append(bean.getMioClass());
		}
		if(!"".equals(bean.getBankAccNo()) && !(bean.getBankAccNo() == null)){
			sql.append(" and bank_acc_no='");
			sql.append(bean.getBankAccNo()+"'");
		}
		if(!"".equals(bean.getCustNo()) && !(bean.getCustNo() == null)){
			sql.append(" and mio_cust_no='");
			sql.append(bean.getCustNo()+"'");
		}
		if(!"".equals(bean.getMioItemCode()) && !(bean.getMioItemCode() == null)){
			sql.append(" and mio_item_code='");
			sql.append(bean.getMioItemCode()+"'");
		}
		sql.append(" order by cntr_no, mio_class,mio_item_code,plnmio_date");
		sql2.append("select * from (");
		sql2.append(sql.toString());
		sql2.append(") a where rownum<151");
		
		List<PlnmioRec> plnList = jdbcTemplate.query(sql2.toString(), new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		
		return plnList;
	}

	@Override
	public double getPlnmioRecAmntBankTrans(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(AMNT) as amnts FROM PLNMIO_REC");
		sql.append(" WHERE CNTR_NO = '");
		sql.append(applNo);
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
	public int getPlnmioRecSize(String cntrNo,String type,String key) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from plnmio_rec where cntr_No = '");
		sql.append(cntrNo);
		sql.append("' and  multi_Pay_Acc_Type = '");
		sql.append(type);
		if("O".equals(type)) {
			sql.append("' and level_code = '");
		}else if("I".equals(type)){
			sql.append("' and ipsn_no = '");
		}else if("P".equals(type)){
			sql.append("' and fee_grp_no = '");
		}
		sql.append(key);
		sql.append("'");
		int size = jdbcTemplate.queryForObject(sql.toString(),Integer.class);
		return size;
	}

	@Override
	public boolean deletePlnmioRec(String cntrNo, String type, String key) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete plnmio_rec where cntr_No = '");
		sql.append(cntrNo);
		sql.append("' and  multi_Pay_Acc_Type = '");
		sql.append(type);
		if("O".equals(type)) {
			sql.append("' and level_code = '");
		}else if("I".equals(type)){
			sql.append("' and ipsn_no = '");
		}else if("P".equals(type)){
			sql.append("' and fee_grp_no = '");
		}
		sql.append(key);
		sql.append("'");
		int size = jdbcTemplate.update(sql.toString());
		return size>0;
	}

	@Override
	public boolean deletePlnmioRecByCntrNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete plnmio_rec where cntr_No = '");
		sql.append(applNo);
		sql.append("'");
		int size = jdbcTemplate.update(sql.toString());
		return size>0;
	}

	@Override
	public long getPlnmioRecBatNId() {
		return jdbcTemplate.queryForObject(SELECT_PLNMIO_REC_BAT_SQL, long.class);
	}
}
