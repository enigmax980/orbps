package com.newcore.orbps.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.dao.api.InsurApplDao;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

@Service("insurApplServices")
public class InsurApplServiceImpl implements InsurApplServices{

	@Autowired
	InsurApplDao insurApplDao;
	
	/**
     * 日志管理工具实例.
     */
    private static Logger logger = LoggerFactory.getLogger(InsurApplServiceImpl.class);
    
	@Override
    public PageData<ResponseVo> getProductSummaryList(PageQuery<ResponseVo> pageQuery) {
        logger.info("执行方法:查询投保单号下的信息列表！");
        logger.info(pageQuery.getCondition().getBusiPrdCode());
        PageData<ResponseVo> pageData = new PageData<ResponseVo>();
        try {
            pageData.setData(insurApplDao.getProductSummaryList(pageQuery));
            pageData.setTotalCount(insurApplDao.getProductSummaryCount(pageQuery.getCondition()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return pageData;
    }
	
}
