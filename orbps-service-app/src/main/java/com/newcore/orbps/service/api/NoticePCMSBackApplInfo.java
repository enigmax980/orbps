package com.newcore.orbps.service.api;

/**
 * 保单回退通知服务
 *
 * @author lijifei
 * 创建时间：2016年11月02日18:25:11
 */
@FunctionalInterface
public interface NoticePCMSBackApplInfo {
	public String findNoticePCMSBackApplInfo(String applNo,String isProcMio);
}
