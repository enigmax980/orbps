package com.newcore.orbps.service.api;

import java.util.Map;

/**
 * 实收付流水落地清单告知
 *
 * @author lijifei
 * 创建时间：2016年11月1日15:24:11
 */
@FunctionalInterface
public interface MioLogDataWriteBack {
	
	public String writeMioLogDate(String applNo, Map<String, String> keySetMap, String mioType,String oclkBranchNo,String oclkClerkNo);
}
