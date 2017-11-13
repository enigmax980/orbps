package com.newcore.orbps.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.service.api.ApplNoApplicationService;

/**
 * 投保单号生成功能
 * 
 * @author niuzhihaoi 创建时间：
 */
@Service("applNoApplicationService")
public class ApplNoApplicationServiceImpl implements ApplNoApplicationService {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory
			.getLogger(ApplNoApplicationServiceImpl.class);

	RetInfo retInfo = new RetInfo();

	@Override
	public RetInfo applyApplNo(String mgrBranchNo) {
		return null;
	}

}
