package com.newcore.orbps.service.api;

/**
 * DASC.自动节点服务：产生应收
 * 
 * @author jcc 2016年8月31日 14:13:09
 */
@FunctionalInterface
public interface TaskProcPlnmioService {

	/**
	 * 产生应收方法
	 * 
	 * @param args
	 *            :rwh 任务号和 ywlsh 业务流水号 拼装值
	 * @param
	 * @return
	 */
	void procPlnmio(String args);
}