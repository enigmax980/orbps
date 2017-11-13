package com.newcore.orbps.web.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.web.vo.contractentry.modal.HealthInsurFlagVo;
import com.newcore.supports.models.Dict;

/**
 * combox工具
 * 
 * @author 靳聪
 *
 */
@Controller
@RequestMapping("/orbps/public/combox")
public class ComboxUtil {

    /**
     * 查询责任信息
     * 
     * @author xiaoYe
     * @param query
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/healthInsurFlag")
    public @ResponseMessage List<HealthInsurFlagVo> getResponseSummaryList() {
    	  List<HealthInsurFlagVo> list =  new ArrayList<>();
    	//第二个险种的配置是所有的险种
    	String[] healthInsurFlaga = PropertiesUtils.getProperty("healthInsurFlagOption2").split(",");
    	//循环遍历String数组
    	for(String string : healthInsurFlaga){
    		//获取到的字符串是以-的格式拼接的，所以在按-分割成数组，取到key和value，添加到list中
    		String strings[] = string.split("-");
    		HealthInsurFlagVo healthInsurFlagVo = new HealthInsurFlagVo();
    		healthInsurFlagVo.setKey(strings[0]);
    		healthInsurFlagVo.setDescription(strings[1]);
    		list.add(healthInsurFlagVo);
    	}
//        String jsonStr = "[{\"key\":\"00\",\"description\":\"非专项标识业务\"},{\"key\":\"A1\",\"description\":\"新农合\"},{\"key\":\"A2\",\"description\":\"新农合补充\"},{\"key\":\"A3\",\"description\":\"城镇职工基本医疗\"},{\"key\":\"A4\",\"description\":\"城镇职工补充医疗\"},{\"key\":\"A5\",\"description\":\"城镇居民基本医疗\"},{\"key\":\"A6\",\"description\":\"城镇居民补充医疗\"},{\"key\":\"A7\",\"description\":\"医疗救助\"},{\"key\":\"A8\",\"description\":\"企事业团体补充医疗\"},{\"key\":\"A9\",\"description\":\"其他委托管理业务\"}]";
//        List<HealthInsurFlagVo> list = JSON.parseArray(jsonStr, HealthInsurFlagVo.class);
        return list;
    }
}