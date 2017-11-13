package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.supports.models.service.bo.PageQuery;

import java.util.List;

/**
 * 投保单接口
 *
 * @author liuxiaoye
 *         创建时间：2016年3月21日14:24:11
 */
public interface InsurApplDao {


    /**
     * @param pageQuery
     * @return
     * @author liuxiaoye
     * @since 2016-05-09
     */
    List<ResponseVo> getProductSummaryList(PageQuery<ResponseVo> pageQuery);

    long getProductSummaryCount(ResponseVo queryCondition);

}
