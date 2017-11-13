package com.newcore.orbps.dao.api;


/**
 * 被保险人错误信息提交服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日16:24:11
 */
@FunctionalInterface
public interface CgInsuredErrCommitService {
	
	public int doCgInsuredErrCommitService(String cgNo,String modifyFlag);
	
}
