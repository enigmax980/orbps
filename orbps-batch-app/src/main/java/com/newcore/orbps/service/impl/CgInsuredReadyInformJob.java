package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.halo.core.common.SpringContextHolder;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskBo;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskRetInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.pcms.api.GrpListCgTaskService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;


/**
 * 定时任务-清单告知
 *
 * @author lijifei
 * @dete  2016-9-26 20 :16:18
 */
//@Schedule(cron = "0 0/1 * * * ?")
//@Service("cgInsuredReadyInformJob")
//@DisallowConcurrentExecution
public class CgInsuredReadyInformJob implements Job {//测试时需求确认 总保额  总保费  总人数（此时存在可能存在作废人数，故与团单里数据可能不一致）

	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	GrpListCgTaskService restfulgrpListCgTaskServiceClient;



	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CgInsuredReadyInformJob.class);

	/* 清单告知
	 * 流程：定时扫描CNTR_EFFECT_CTRL表PROC_STAT字段，若为8-->查询mongodb数据（人数、总保额、总保费）
	 * 若数据与表格中对应数据一致-->通知pcms，将保单落地控制表状态置5，同时将数据插入-清单批次信息记录表；
	 * */
	@AsynCall
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("清单告知开始......");
		//定时任务调取查询服务
		grpInsuredInformPcms();
		logger.info("清单告知结束......");
	}
	//查询服务保单落地信息控制表；调用保单辅助系统服务。
	public int grpInsuredInformPcms(){
		//组合保单号 
		String cgNoStr=null;
		//总人数  
		int ipsnNumInt=0;
		//总保额  
		Double sumFaceAmntDou;
		// 总保费    
		Double sumPremiumDou;
		//批次号
		String batchNoStr ;
		//投保单号
		String applNo ;
		//险种代码
		String  polCode;
		//管理机构号
		String mgrBranchNo ;
		//管理机构号
		String lstProcType ;
		//是否订正标识
		@SuppressWarnings("unused")
		String mio ;
		//编写sql查询语句
		String	sql = "select CG_NO,BATCH_NO,IPSN_NUM,SUM_FACE_AMNT,SUM_PREMIUM,"
				+ "APPL_NO,MG_BRANCH,POL_CODE, LST_PROC_TYPE,MIOLOG_DATE from CNTR_EFFECT_CTRL where PROC_STAT = '12'";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			logger.info("查询到数据，开始处理清单告知......");
			cgNoStr=row.getString(1);		 //任务ID
			batchNoStr=row.getString(2); //业务流水号
			ipsnNumInt=Integer.valueOf(row.getString(3));		 //任务ID
			sumFaceAmntDou=Double.valueOf(row.getString(4)); //业务流水号
			sumPremiumDou=Double.valueOf(row.getString(5));		 //任务ID
			applNo=row.getString(6); //业务流水号
			mgrBranchNo=row.getString(7);//任务ID
			polCode=row.getString(8); //业务流水
			lstProcType=row.getString(9); //业务流水
			mio = row.getString(10); //业务流水
			List<Object> listGrp = new ArrayList<>();
			//L:普通清单，A：档案清单，M：事后补录，N：无清单
			if("N".equals(lstProcType) || "A".equals(lstProcType) || "M".equals(lstProcType)){
				doLstProcTypeOfN(applNo,cgNoStr);
				//判断调用接口返回信息，若RetCode=1 即程序执行成功。反之则为失败！
				String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
				jdbcTemplate.update(updatesql, "1" ,"成功",cgNoStr);
			}else{
				logger.info("数据.......................................................调用清单告知");
				//根据保单落地控制表-每一条数据cgNo，batchNo查询mongodb
				Map<String, Object> map=new HashMap<>();
				map.put("cgNo",cgNoStr);
				map.put("batNo",batchNoStr);
				map.put("procStat","E");
				listGrp = (List<Object>) mongoBaseDao.find(GrpInsured.class, map);
				logger.info("数据.......................................................调用清单告知方法");
				getCgInsuredReadyInform(listGrp, applNo, cgNoStr, batchNoStr, ipsnNumInt, sumFaceAmntDou, sumPremiumDou, mgrBranchNo, polCode);
			}
		}//end for
		return ipsnNumInt;
	}
	public String getCgInsuredReadyInform(List<Object> listGrp,String applNo,String cgNoStr,String batchNoStr,int ipsnNumInt,
			Double sumFaceAmntDou,Double sumPremiumDou,String mgrBranchNo,String polCode){
		String result="";
		if(listGrp.size()>0){
			//修改状态字段，锁定本条数据。
			String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
			jdbcTemplate.update(updatesql, "16" ,"清单告知-数据处理中！",cgNoStr);
			//查询是否有该条数据，如果没有就插入一条
			String selectcounter = "select count(*) from LIST_BATCH_RECORD  WHERE CG_NO = ? and BATCH_NO= ?";
			int	counter = jdbcTemplate.queryForObject(selectcounter, int.class, cgNoStr ,batchNoStr);
			if(counter==0){
				//2.插入 清单批次信息记录表 一条对应信息GrpInsuredBatchRecord
				//编写sql插入语句
				String INSERT_LIST_BATCH_RECORD_SQL = "insert into LIST_BATCH_RECORD "
						+ "(CG_NO, BATCH_NO,BATCH_IPSN_NUM,BATCH_SUM_FACE_AMNT,BATCH_SUM_PREMIUM,"
						+ "SUCC_NUM,FAIL_NUM,CREATE_TIME,APPL_NO,POL_CODE,MG_BRANCH_NO) values (?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
				jdbcTemplate.update(INSERT_LIST_BATCH_RECORD_SQL, 
						cgNoStr, 
						batchNoStr, 
						ipsnNumInt,
						sumFaceAmntDou, 
						sumPremiumDou, 
						"0",
						"0", 
						new Date(),
						applNo,
						polCode,
						mgrBranchNo);
			}//end if
			//通知pcms方 数据已准备完毕
			GrpListCgTaskBo grpListCgTaskBo=new GrpListCgTaskBo();
			//赋值
			grpListCgTaskBo.setCgNo(cgNoStr);          					// 组合保单号
			grpListCgTaskBo.setBatNo(Long.valueOf(batchNoStr));		// 批次号
			grpListCgTaskBo.setBatchIpsnNum(Long.valueOf(ipsnNumInt));		// 批次总人数
			grpListCgTaskBo.setBatchSumFaceAmnt(sumFaceAmntDou);			// 批次总保额
			grpListCgTaskBo.setBatchSumPremium(sumPremiumDou);				// 批次总保费
			grpListCgTaskBo.setApplNo(applNo);							// 投保单号
			grpListCgTaskBo.setMgBranch(mgrBranchNo);					// 管理机构号
			grpListCgTaskBo.setPolCode(polCode);

			logger.info("调用pcms清单告知服务开始......"+cgNoStr);
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);	
			GrpListCgTaskRetInfo grpListCgTaskRetInfo =restfulgrpListCgTaskServiceClient.grpListCgTaskService(grpListCgTaskBo);
			logger.info("调用pcms清单告知服务结束......"+cgNoStr);
			//判断返回结果是否成功，若返回结果code为1 即成功。
			if(Integer.valueOf(grpListCgTaskRetInfo.getRetCode())==1){
				//3.更改-保单落地信息控制表-处理状态为5   清单信息发送成功
				String	updatesql2="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ? ";
				jdbcTemplate.update(updatesql2, "5" , "清单信息发送成功!", cgNoStr);
			}else{
				String	updatesql2="update CNTR_EFFECT_CTRL SET PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
				jdbcTemplate.update(updatesql2, grpListCgTaskRetInfo.getErrMsg(),cgNoStr);
			}
			result = "Y";
		}else{
			logger.error("没有清单告知数据！");
			result = "N";
		}
		return result;
	}

	/**
	 *无清单时处理逻辑：
	 *	1.
	 *	2.返回任务流ID，触发打印；
	 *	3.添加轨迹节点.
	 * */

	public void doLstProcTypeOfN(String applNo,String cgNo){
		//查询任务流ID
		String selectIdSql = "select ID from CNTR_EFFECT_CTRL WHERE CG_NO = ? ";
		String selectId = jdbcTemplate.queryForObject(selectIdSql, String.class, cgNo);
		//修改操作轨迹记录
		TraceNode traceNode= new TraceNode();
		traceNode.setProcDate(new Date());
		traceNode.setProcStat("16");
		mongoBaseDao.updateOperTrace(applNo,traceNode);
		//当都成功时，调用打印流程
		TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		taskProcessRequestBO.setTaskId(selectId);// 业务服务参数中获取的任务ID
		taskProcessService.completeTask(taskProcessRequestBO);// 进行任务完成操作
	}








}

