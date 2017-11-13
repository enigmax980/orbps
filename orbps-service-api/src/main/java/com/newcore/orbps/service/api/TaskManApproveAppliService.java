package com.newcore.orbps.service.api;

/** 
* @ClassName: TaskManApproveAppliService 
* @Description: 人工审核
* @author  jiangbomeng
* @date 2016年9月1日 上午11:06:50 
*  
*/
@FunctionalInterface
public interface TaskManApproveAppliService {
	
	String manualApprove(String args);
}
