package com.newcore.orbps.web.otherfunction;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.service.api.BatchJobControlService;
import com.newcore.orbps.web.contractentry.OfflineListImportController;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 机构权限控制
 * 
 * @author 靳聪
 *
 */
@Controller
@RequestMapping("/orbps/otherFunction/branchControl")
public class BatchControl {
	/**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(OfflineListImportController.class);
	
	/**
     * 批作业控制服务
     */
    @Autowired
    BatchJobControlService batchJobControlService;
	
    /**
     * 批量重启批作业服务
     * 
     * @author jincong
     * @param session
     * @param str
     * @return
     */
    @RequestMapping(value = "/batchRestart")
    public @ResponseMessage RetInfo batchRestart(@CurrentSession Session session) {
    	RetInfo retInfo = new RetInfo();
	    try{
	    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
	        HeaderInfoHolder.setOutboundHeader(headerInfo);
	    	batchJobControlService.restartStoppedBatJob();
	    }catch(Exception e){
	        logger.info(e.getMessage(), e);
	        retInfo.setRetCode("0");
	        retInfo.setErrMsg("批作业启动失败！");
	    }
        return retInfo;
    }
    
    /**
     * 批量停止批作业服务
     * 
     * @author jincong
     * @param session
     * @param str
     * @return
     */
    @RequestMapping(value = "/batchStop")
    public @ResponseMessage RetInfo batchStop(@CurrentSession Session session) {
    	RetInfo retInfo = new RetInfo();
	    try{
	    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
	        HeaderInfoHolder.setOutboundHeader(headerInfo);
	    	batchJobControlService.stopMultiBatchJob();
	    }catch(Exception e){
	        logger.info(e.getMessage(), e);
	        retInfo.setRetCode("0");
	        retInfo.setErrMsg("批作业停止失败！");
	    }
        return retInfo;
    }
    
    /**
     * 批作业启停结果查询
     * 
     * @author jincong
     * @param session
     * @param str
     * @return
     */
    @RequestMapping(value = "/batchStatQuery")
    public @ResponseMessage RetInfo batchStatQuery(@CurrentSession Session sessionss) {
    	RetInfo retInfo = new RetInfo();
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
	    try{
	    	retInfo = batchJobControlService.getIsBatchStopped();
	    }catch(Exception e){
	        logger.info(e.getMessage(), e);
	        retInfo.setRetCode("0");
	        retInfo.setErrMsg("批作业查询失败！");
	    }
        return retInfo;
    }
}