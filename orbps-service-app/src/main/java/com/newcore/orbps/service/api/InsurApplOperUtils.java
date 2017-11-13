package com.newcore.orbps.service.api;

import com.newcore.orbps.models.para.RetInfo;

/**
 * 保单落地帮助工具接口
 * Created by liushuaifeng on 2017/2/24 0024.
 */

public interface InsurApplOperUtils {

    public RetInfo getIsInsurApplLanding(String applNo);

    public RetInfo getIsInsurApplBack(String applNo);

}
