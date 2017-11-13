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
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.ProPlnmioRecStepService;

/**
 * 生成分期应收数据定时任务
 * @author JCC
 * 2016年12月8日 09:05:24
 */
//@Schedule(cron = "0 0/1 * * * ?")    //每隔1分执行一次。
//@Service("ProPlnmioRecStepQuartz")
//@DisallowConcurrentExecution
public class ProPlnmioRecStepServiceJob implements Job{
	
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcPlnmioRecRecordJob.class);
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	ProPlnmioRecStepService proPlnmioRecStepService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//业务逻辑开始
		logger.info("执行方法[生成分期应收数据定时任务]:开始运行！");
		// 1.从生成分期应收队列表查询STATUS==N 新建的数据
		String sql = "select APPL_NO from PROC_STEP_PLNMIO_TASK_QUEUE where STATUS ='N'";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			String applNo=row.getString(1); //投保单号
			try{
				logger.info("执行方法[生成分期应收数据定时任务]:投保单号="+applNo);
				//锁定当前数据
				updateJDBC("K",applNo);
				GrpInsurAppl grpInsurAppl =  procMioInfoDao.getGrpInsurAppl(applNo);
				if(grpInsurAppl == null){
					logger.error("执行方法[生成分期应收数据定时任务]：根据投保单号【"+applNo+"】,未查询出对应的保单信息！");
					updateJDBC("E",applNo);
				}else{
					int updateRow=jdbcTemplate.update("update CNTR_EFFECT_CTRL set PROC_STAT='0' where  CG_NO='"+grpInsurAppl.getCgNo()+"' and appl_no='"+applNo+"' " );
					if(updateRow==0){
						logger.error("执行方法[生成分期应收数据定时任务]：根据投保单号【"+applNo+"】和合同组号【"+grpInsurAppl.getCgNo()+"】,修改控制表【CNTR_EFFECT_CTRL】操作失败！");
						updateJDBC("E",applNo);
					}else{
						boolean isTrue = proPlnmioRecStepService.proPlnmioRecStep(grpInsurAppl);
						if(isTrue){
							updateJDBC("C",applNo);
							logger.info("执行方法[生成分期应收数据定时任务]:投保单号:"+applNo+",生成分期应收数据定时任务完成！");
						}
					}
				}
			}catch(Exception ex){
				logger.error("执行方法[生成分期应收数据定时任务]：程序出现异常>>>>>>"+ex);
				updateJDBC("E",applNo);
			}
		}
	}

	/**
	 * 修改任务状态
	 * @param state 任务状态:N-新建 K-正在执行 C-完成 E-错误
	 * @param applNo 投保单号
	 * @return
	 */
	private int updateJDBC(String state, String applNo) {
		String sqlDate="";
		if("K".equals(state)){ 
			//开始执行时间
			sqlDate=" , START_TIME =sysdate ";
		}else if("C".equals(state)){
			//任务完成时间
			sqlDate=" , END_TIME =sysdate ";
		}
		return jdbcTemplate.update("update PROC_STEP_PLNMIO_TASK_QUEUE set STATUS='"+state+"' "+sqlDate+" where appl_no='"+applNo+"'");
	}
}
