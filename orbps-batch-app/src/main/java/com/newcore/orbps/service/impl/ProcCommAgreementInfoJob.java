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
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.ProcCommAgreementInfoService;

/**
 * 共保协议落地服务定时任务
 * @author JCC
 * 2016年12月8日 09:05:24
 * 每隔1分执行一次。
 */
//@Schedule(cron = "0 0/1 * * * ?")   
//@Service("ProcCommAgreementInfoQuartz")
//@DisallowConcurrentExecution
public class ProcCommAgreementInfoJob implements Job{
	
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcCommAgreementInfoJob.class);
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	ProcCommAgreementInfoService procCommAgreementInfoService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("执行方法[共保协议落地服务定时任务]:开始运行！");
	    // 1.查询保单落地信息控制表状态为8
		String sql = "select APPL_NO,CG_NO,is_Common_Agreement from CNTR_EFFECT_CTRL where PROC_STAT = '8'";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			String applNo=row.getString(1); //投保单号
			String cgNo=row.getString(2);   //组合保单号
			String isGB=row.getString(3);   //是否共保标识
			try{
				logger.info("执行方法[共保协议落地服务定时任务]:投保单号："+applNo+">>>>>组合保单号："+cgNo+">>>>>>是否共保标识[Y-共保，N-非共保]:"+isGB);
				updateJDBC("16",cgNo); //处理中
				//如果不是共保获取，则直接把状态改为12，并返回
				if("N".equals(isGB)){
					updateJDBC("12",cgNo);
					logger.info("执行方法[共保协议落地服务定时任务]:根据投保单号【"+applNo+"】和合同组号【"+cgNo+"】,任务完成！");
				}else if("Y".equals(isGB)){
					GrpInsurAppl grpInsurAppl =  procMioInfoDao.getGrpInsurAppl(applNo);
					if(grpInsurAppl == null){
						updateJDBC("8",cgNo);
						logger.error("执行方法[共保协议落地服务定时任务]：根据投保单号【"+applNo+"】,未查询出对应的保单信息！");
					}else{
						RetInfo retInfo = procCommAgreementInfoService.commonAgreement(grpInsurAppl);
						if("1".equals(retInfo.getRetCode())){
							updateJDBC("12",cgNo);
							logger.info("执行方法[共保协议落地服务定时任务]:根据投保单号【"+applNo+"】和合同组号【"+cgNo+"】,任务完成！");
						}else{
							updateJDBC("8",cgNo);
							logger.info("执行方法[共保协议落地服务定时任务]:投保单号【"+applNo+"】和合同组号【"+cgNo+"】,操作失败！");
						}
					}
				}
			}catch(Exception ex){
				logger.error("执行方法[共保协议落地服务定时任务]：根据投保单号【"+applNo+"】和合同组号【"+cgNo+"】程序出现异常>>>>>>"+ex);
				updateJDBC("8",cgNo);
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
