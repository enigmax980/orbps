package com.newcore.orbps.service.api;

/**
 * 暂收费解冻服务
 *
 * @author lijifei
 * 创建时间：2016年11月03日13:24:11
 */
@FunctionalInterface
public interface OpenFrozenAmntService {
	public String openFrozenAmnt(String applNo);
}
