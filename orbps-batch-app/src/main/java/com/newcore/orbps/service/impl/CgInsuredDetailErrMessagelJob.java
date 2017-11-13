package com.newcore.orbps.service.impl;

import java.util.List;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.bo.ErrBatchListBo;
import com.newcore.orbps.models.pcms.bo.ErrListInBo;
import com.newcore.orbps.models.pcms.bo.ErrListOutBo;
import com.newcore.orbps.service.pcms.api.ErrListQueryService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 定时任务-查询清单导入详细错误信息
 *
 * @author lijifei
 * @dete  2016-9-25 20 :16:18
 */
//@Schedule(cron = "0 0/1 * * * ?")
//@Service("cgInsuredDetailErrMessagelJob")
//@DisallowConcurrentExecution
public class CgInsuredDetailErrMessagelJob implements Job {


	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired 
	ErrListQueryService restfulerrListQueryServiceClient;

	private static Logger logger = LoggerFactory.getLogger(CgInsuredDetailErrMessagelJob.class);

	/*
	 * 4. 查询清单导入详细错误信息(CgInsuredDetailErrMessage)
	 *		1. 定时任务扫描保单落地信息控制表
	 *			1.1查询CNTR_EFFECT_CTRL表,状态字段为6的数据。
	 *		2.根据出错的保单号和批次号调用保单辅助系统服务，查询清单导入详细错误信息，并将错误信息更新到ERR_LIST_INFO表中。
	 *		3.更新CNTR_EFFECT_CTRL表,状态字段为2，清单中存在错误信息，待修正。
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("查询清单导入详细错误信息开始......");
		doCgInsuredDetailErrMessage();
		logger.info("查询清单导入详细错误信息结束......");
	}
	//查询服务保单落地信息控制表；调用保单辅助系统服务。
	public void doCgInsuredDetailErrMessage(){
		String	sql = "select CG_NO ,APPL_NO,BATCH_NO from CNTR_EFFECT_CTRL where PROC_STAT = '6'";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			logger.info("清单落地存在错误数据，开始处理......");
			//获得其中一条存在错误信息数据的 批次号，合同组号，投保单号
			String strBatchNo =	row.getString(3);
			String strCgNo =	row.getString(1);
			String strApplNo =	row.getString(2);
			//修改状态字段，锁定本条数据。
			String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
			jdbcTemplate.update(updatesql, "16" ,"清单错误查询-数据处理中！",strCgNo);
			ErrListInBo errListBo  = new ErrListInBo();
			errListBo.setApplNo(strApplNo);
			errListBo.setCgNo(strCgNo);
			errListBo.setBatNo(Long.valueOf(strBatchNo));
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//			headerInfo.setOrginSystem("ORBPS");					
//			headerInfo.getRouteInfo().setBranchNo("120000");
			HeaderInfoHolder.setOutboundHeader(headerInfo);	
			logger.info("调用pcms错误查询接口......");
			ErrListOutBo grpListCgErrLogRetInfoBo = restfulerrListQueryServiceClient.getGrpListErrLogListByCgnoAndBatno(errListBo);
			List<ErrBatchListBo> listErrBatchListBo  = grpListCgErrLogRetInfoBo.getErrBatchListBoList();
			//判断是否查出数据
			if(listErrBatchListBo.size() > 0){
				logger.info("将错误信息插入数据库！");
				//对查到的数据进行遍历
				for (ErrBatchListBo errBatchListBo : listErrBatchListBo) {
					//错误信息更新到ERR_LIST_INFO表中
					String INSERT_ERR_LIST_INFOD_SQL = "insert into ERR_LIST_INFO (CG_NO, BATCH_NO,IPSN_NO,MODIFY_FLAG,ERR_SEQ,ERR_CODE,ERR_DESC) values (?, ?, ?, ?, ?, ?, ?)";
					jdbcTemplate.update(INSERT_ERR_LIST_INFOD_SQL, 
							errBatchListBo.getCgNo(), 
							errBatchListBo.getBatNo(), 
							errBatchListBo.getIpsnNo(),
							"0",
							errBatchListBo.getErrSeq(),
							errBatchListBo.getErrCode(),
							errBatchListBo.getErrDesc());
				}
				//更新CNTR_EFFECT_CTRL表,状态字段为2，清单中存在错误信息，待修正
				String	updatesql2="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
				jdbcTemplate.update(updatesql2,"2" ,"清单中存在错误信息，待修正!",strCgNo);
			}else{
				//当没有查出数据时
				logger.error("该保单号与批次号没有对应的错误信息！");
			}
		}//end while--查询服务保单落地信息控制表-遍历结果
	}// end doCgInsuredDetailErrMessage


}
