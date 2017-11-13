package com.newcore.orbps.service.api;

/**
 * 暂缴费还回
 *
 * @author lijifei
 * 创建时间：2016年10月28日16:24:11
 */
@FunctionalInterface
public interface ThawProviFeeInfo {
	
	public String temporarilyPayRevert(String applNo);
}
