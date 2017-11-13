package com.newcore.orbps.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.service.api.TaskProcPlnmioService;
import com.newcore.supports.dicts.NEW_APPL_STATE;

/**
 * @ClassName: TaskProcPlnmioServiceImpl
 * @Description: 生成应收任务流节点
 * @author JCC
 * @date 2016年9月1日 上午11:06:50
 * 
 */
@Service("taskProcPlnmioService")
public class TaskProcPlnmioServiceImpl implements TaskProcPlnmioService {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(TaskProcPlnmioServiceImpl.class);

	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Override
	@AsynCall
	public void procPlnmio(String args) {
		String[] argsArr = args.split("_");
		// 业务逻辑开始
		logger.info("执行方法:生成应收任务流节点" + argsArr[0] + "_" + argsArr[1]);
		try {
			/**
			 * 业务逻辑处理开始
			 */
			// 1.从传入参数里获取任务ID和业务流水号
			String businessKey = argsArr[0]; // 业务流水号
			String taskId = argsArr[1];		 // 任务ID
			logger.info("生成应收任务流节点：业务流水号" + businessKey);
			TraceNode traceNode=mongoBaseDao.getPeekTraceNode(businessKey);
			//只有当最新一条操作轨迹流水的状态为13 核保,才需要生成应收
			if(NEW_APPL_STATE.UNDERWRITING.getKey().equals(traceNode.getProcStat())){
				//确保投保在表中的唯一性
				jdbcTemplate.update("delete PROC_PLNMIO_TASK_QUEUE where APPL_NO='"+businessKey+"'");
				//产生应收付流水任务表	
				String	insertsql="insert into PROC_PLNMIO_TASK_QUEUE(TASK_SEQ,STATUS,CREATE_TIME,TASK_ID,BUSINESS_KEY,APPL_NO)"
						+"values(S_PROC_PLNMIO_TASK_QUEUE.NEXTVAL,'N',sysdate,'"+taskId+"','"+businessKey+"','"+businessKey+"')";
				jdbcTemplate.update(insertsql);	
				logger.info("产生应收付流水任务表【PROC_PLNMIO_TASK_QUEUE】：入库完成,"+businessKey+"!");
			}else{
				logger.info("生成应收任务流节点：业务流水号[" + businessKey+"]对应最新的操作轨迹状态为["+traceNode.getProcStat()+"],不产生应收数据！");
			}
		} catch (Exception e) {
			/**
			 * 业务系统异常处理逻辑在这里增加
			 * 
			 */
			throw e;
		}
	}
}
