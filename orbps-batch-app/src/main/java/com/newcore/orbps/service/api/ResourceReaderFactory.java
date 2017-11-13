package com.newcore.orbps.service.api;

import java.io.BufferedReader;

/**
 * 读取resourceId 文件
 * @author liushuaifeng
 *
 * 创建时间：2016年9月20日上午8:41:28
 */
@FunctionalInterface
public interface ResourceReaderFactory {
	
	public BufferedReader create(String resourceId, String encoding);


}
