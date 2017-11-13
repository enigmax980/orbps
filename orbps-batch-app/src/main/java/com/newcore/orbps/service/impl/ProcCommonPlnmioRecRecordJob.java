package com.newcore.orbps.service.impl;

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
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.ProcCommonPlnmioRecRecordService;

/**
 * 产生首席共保应付功能服务定时任务
 * @author JCC
 * 2016年12月15日 11:07:05
 * 每隔一分钟执行一次
 */
//@Schedule(cron = "0 0/1 * * * ?")   
//@Service("ProcCommonPlnmioRecRecordQuartz")
//@DisallowConcurrentExecution
public class ProcCommonPlnmioRecRecordJob implements Job{

	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcCommonPlnmioRecRecordJob.class);
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	ProcCommonPlnmioRecRecordService procCommonPlnmioRecRecordService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//业务逻辑开始
		logger.info("执行方法[产生首席共保应付功能服务定时任务]:开始运行！");
		// 1.查询保单落地信息控制表:生成共保FA/S、GF/S、PS/S等数据查询表,状态为1并且是共保的进行转保费，转成功则把状态改为13
		String sql = "select APPL_NO,CG_NO from CNTR_EFFECT_CTRL where PROC_STAT = '1' and is_Common_Agreement='Y' ";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			String applNo=row.getString(1); //投保单号
			String cgNo=row.getString(2);   //组合保单号
			try{
				logger.info("执行方法[产生首席共保应付功能服务定时任务]:投保单号："+applNo+">>>>>组合保单号："+cgNo);
				updateJDBC("16",cgNo); //处理中
				GrpInsurAppl grpIn =  procMioInfoDao.getGrpInsurAppl(applNo);
				if(grpIn == null){
					updateJDBC("1",cgNo);
					logger.error("执行方法[产生首席共保应付功能服务定时任务]：根据投保单号【"+applNo+"】,未查询出对应的保单信息！");
				}else{
					boolean isTrue = procCommonPlnmioRecRecordService.procCommonPlnmioRecRecord(grpIn);
					if(isTrue){
						updateJDBC("13",cgNo);
						logger.info("执行方法[产生首席共保应付功能服务定时任务]:根据投保单号【"+applNo+"】和合同组号【"+cgNo+"】,任务完成！");
					}else{
						updateJDBC("1",cgNo);
						logger.info("执行方法[产生首席共保应付功能服务定时任务]:投保单号【"+applNo+"】和合同组号【"+cgNo+"】,操作失败！");
					}
				}
			}catch(Exception ex){
				logger.error("执行方法[产生首席共保应付功能服务定时任务]：根据投保单号【"+applNo+"】和合同组号【"+cgNo+"】程序出现异常>>>>>>"+ex);
				updateJDBC("1",cgNo);
			}
		}
	}

	/**
	 * 修改任务状态
	 * @param state 
	 * @param cgNo 合同组号
	 * @return
	 */
	private int updateJDBC(String state, String cgNo) {
		return jdbcTemplate.update("update CNTR_EFFECT_CTRL set PROC_STAT='"+state+"' where CG_NO='"+cgNo+"'" );
	}

}
