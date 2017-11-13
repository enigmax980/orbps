package com.newcore.orbps.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.web.vo.contractentry.modal.HealthInsurFlagVo;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 公共查询
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/read")
public class ReadProperties {
	@Autowired
	PolNatureService polNatureService;
    /**
     * 查询险种集合
     * @author xiaoYe
     * @param busiPrdCode
     * @return
     */
    @RequestMapping(value = "/queryPolCode")
    public @ResponseMessage String queryPolCodeIs(){
        String healthPublicAmountBusiprdCode = PropertiesUtils.getProperty("healthPublicAmountBusiprdCode");
        String constructionbBusiprdCode = PropertiesUtils.getProperty("constructionbBusiprdCode");
        String busiprdCodes = constructionbBusiprdCode +"-"+healthPublicAmountBusiprdCode;
        return busiprdCodes;
    }
    
    /**
     * 查询健康险专项标识
     * @author xiaoYe
     * @param busiPrdCode
     * @return
     */
    @RequestMapping(value = "/queryHealthInsurFlag")
    public @ResponseMessage String queryHealthInsurFlag(@RequestBody String busiPrdCode){
        //获取健康险的集合
        String[] healthInsurFlaga = PropertiesUtils.getProperty("healthInsurFlag1").split(",");
        String[] healthInsurFlagb = PropertiesUtils.getProperty("healthInsurFlag2").split(",");
        //获取健康险option的集合
        for (String string : healthInsurFlaga) {
            if(busiPrdCode.equals(string)){
            	return PropertiesUtils.getProperty("healthInsurFlag1");
            }
        }
        //如果是以下险种的话，option的值就没有变化，传个标识过去，便于前台增删option值。
        for(String string : healthInsurFlagb){
        	if(busiPrdCode.equals(string)){
        		return "2";
        	}
        }
        return "";
    }
    /**
     * 查询所有的健康险所有险种
     * @return string
     */
    @RequestMapping(value = "/queryAllHealthInsur")
    public @ResponseMessage String queryAllHealthInsur(){
    	//获取健康险的集合
    	String string = PropertiesUtils.getProperty("healthInsurFlag1")+","+PropertiesUtils.getProperty("healthInsurFlag2");
    	return string;
    	
    }
    /**
     * 查询当前险种是否是基金险
     * @return
     */
    @RequestMapping(value = "/queryFundInsurInfo")
    public @ResponseMessage String queryFundInsurInfo(@RequestBody String busiPrdCode){
    	String string = "2";
    	List<String> list = new ArrayList<String>();
    	if(busiPrdCode.indexOf(",")!= -1){
    		String[] strings = busiPrdCode.split(",");
    		list = Arrays.asList(strings);  
    	}else{
    		list.add(busiPrdCode);
    	}
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);//isFund
		List<JSONObject> lists = polNatureService.getPolNatureInfo(list);
		for (JSONObject jsonObject : lists) {
			if("Y".equals(jsonObject.get("isFund"))){
				string = "1";
			}
		}
		return string;
    	
    }
}
