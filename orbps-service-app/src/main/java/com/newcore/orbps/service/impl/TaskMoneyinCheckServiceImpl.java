package com.newcore.orbps.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.service.api.TaskMoneyinCheckService;

/**
 * @ClassName: TaskMoneyinCheckServiceImpl
 * @Description: 收费检查任务流程
 * @author JCC
 * @date 2016年9月1日 上午11:06:50
 * 
 */
@Service("taskMoneyinCheckService")
public class TaskMoneyinCheckServiceImpl implements TaskMoneyinCheckService {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(TaskMoneyinCheckServiceImpl.class);

	@Autowired
	JdbcOperations jdbcTemplate;

	@Override
	@AsynCall
	public void moneyinCheck(String args) {
		String[] argsArr = args.split("_");

		// 业务逻辑开始
		logger.info("执行方法:收费检查" + argsArr[0] + "_" + argsArr[1]);
		try {
			//从传入参数里获取任务ID和业务流水号
			String businessKey = argsArr[0];//业务流水号
			String taskId = argsArr[1]; 	//任务ID
			logger.info("++++++业务流水号是+++++++：" + businessKey);
			jdbcTemplate.update("delete MONEY_IN_CHECK_TASK_QUEUE where BUSINESS_KEY='"+businessKey+"'");	
			//产生应收付流水任务表	
			String	insertsql="insert into MONEY_IN_CHECK_TASK_QUEUE(TASK_SEQ,STATUS,CREATE_TIME,TASK_ID,BUSINESS_KEY,APPL_NO)"
					+"values(S_MONEY_IN_CHECK_TASK_QUEUE.NEXTVAL,'N',sysdate,'"+taskId+"','"+businessKey+"','"+businessKey+"')";
			jdbcTemplate.update(insertsql);	
			logger.info("收费检查流水任务表【MONEY_IN_CHECK_TASK_QUEUE】：入库完成,"+businessKey+"!");
			return;
		} catch (Exception e) {
			/**
			 * 业务系统异常处理逻辑在这里增加
			 * 
			 */
			throw e;
		}
	}
}
