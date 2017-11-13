package com.newcore.orbps.service.api;

/**
 * 查询冲正结果通知
 *
 * @author lijifei
 * 创建时间：2016年11月02日16:55:11
 */
@FunctionalInterface
public interface GetCorrResult {
	public String getCorrResultFromPcms(String applNo);
}
