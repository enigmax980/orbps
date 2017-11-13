package com.newcore.orbps.web.errgrpinsureddown;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.web.annotation.ResponseMessage;

/**
 * @author wangxiao
 * 创建时间：2016年8月31日上午10:10:14
 */
@Controller
@RequestMapping("/exp/file")
public class ErrorGrpInsuredDownloadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorGrpInsuredDownloadController.class);
	
	@RequestMapping(value = "/download/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte []> download(@PathVariable("id") String applNo) throws IOException {    

		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
        HttpHeaders headers = new HttpHeaders();
        byte[] body = null;
        HttpStatus httpState = HttpStatus.NOT_FOUND;
        
		File file = new File(sb+File.separator+applNo+".xls");
		if(file.exists()){
	        //处理显示中文文件名的问题
	        String fileName=new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
	        //设置请求头内容,告诉浏览器代开下载窗口
	        body=FileUtils.readFileToByteArray(file);
	        httpState=HttpStatus.OK;
	        headers.setContentDispositionFormData("attachment",fileName );
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}else{
            LOGGER.error("差错文件["+applNo+".xls]不存在!");
		}
        return new ResponseEntity<>(body,headers,httpState);    
    }
	//判断文件是否存在
	@RequestMapping(value = "/exists")
	public @ResponseMessage String fileExistsOrNo(@RequestBody String fileName) {
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
		File file = new File(sb+File.separator+fileName+".xls");
		String rtn;
		if(file.exists()){
			rtn="1";
		}else{
			rtn="0";
		}
		return rtn;
	}
}
