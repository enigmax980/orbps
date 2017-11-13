package com.newcore.orbps.service.impl;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.halo.core.batch.annotation.Schedule;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.ProcPlnmioRecRecordService;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;

/**
 * 产生应收数据操作定时任务
 * @author JCC
 * 2016年12月2日 19:45:43
 * 每隔一分钟分钟执行一次。
 */
@Schedule(cron = "0 0/1 * * * ?")    
@Service("ProcPlnmioRecRecordQuartz")
@DisallowConcurrentExecution
public class ProcPlnmioRecRecordJob implements Job{
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcPlnmioRecRecordJob.class);
	
	@Autowired
	ProcPlnmioRecRecordService procPlnmioRecRecordService;
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("执行方法[产生应收数据操作定时任务]:开始运行！");
		// 1.从产生应收付流水任务表查询STATUS==N 新建的数据
		SqlRowSet row = plnmioCommonDao.queryProcPlnmioTaskQueue("N");
		while(row.next()){
			String taskId=row.getString(1);		 //任务ID
			String businessKey=row.getString(2); //业务流水号
			try{
				logger.info("执行方法[产生应收数据操作定时任务]:任务ID="+taskId+">>>>业务流水号="+businessKey);
				//锁定当前数据
				plnmioCommonDao.updateProcPlnmioTaskQueue("K",taskId);
				//获取保单信息
				GrpInsurAppl grpInsurAppl =  procMioInfoDao.getGrpInsurAppl(businessKey);
				boolean isYes = procPlnmioRecRecordService.procPlnmioRecRecord(grpInsurAppl,taskId);
				logger.info("执行方法[产生应收数据操作定时任务]:生成应收数据操作,返回状态【"+isYes+"】");
				if(isYes){
					plnmioCommonDao.updateProcPlnmioTaskQueue("C",taskId);
				}else{
					logger.info("执行方法[产生应收数据操作定时任务]：生成应收数据失败！【"+taskId+"】【"+businessKey+"】");
					plnmioCommonDao.updateProcPlnmioTaskQueue("E",taskId);
				}
			}catch(Exception ex){
				logger.error("执行方法[产生应收数据操作定时任务]：【"+taskId+"】【"+businessKey+"】程序出现异常>>>>>>"+ex);
				plnmioCommonDao.updateProcPlnmioTaskQueue("E",taskId);
			}
		}
	}
}
