package com.newcore.orbps.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.types.resources.selectors.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.Base64;
import com.itextpdf.text.DocumentException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.CreateGrpIsurApplService;
import com.newcore.orbps.service.business.InsurApplProcessorFactory;
import com.newcore.orbps.util.CreateGrpIsurAppl;

/** 
* @ClassName: CreateGrpIsurApplServiceImpl 
* @Description: 根据投保单号查询投保单并生成电子投保单
* @author  jiangbomeng
* @date 2016年8月17日 上午9:21:59 
*  
*/
@Service("createGrpIsurApplService")
public class CreateGrpIsurApplServiceImpl implements CreateGrpIsurApplService{
	InsurApplProcessorFactory insurApplProcessorFactory = InsurApplProcessorFactory.getInsurApplProcessorFactory();
	InputStream inputStream;
	RetInfo retInfo = new RetInfo();
	@Autowired
	MongoBaseDao mongoBaseDao;
	/**
     * 日志管理工具实例.
     */
    static Logger logger = LoggerFactory.getLogger(CreateGrpIsurApplServiceImpl.class);
    
	
	/**
   	 * 保单查询
   	 * @param InsurApplString
	 * @throws IOException 
	 * @throws DocumentException 
   	 * @returnz
	 * @throws Exception 
   	 */
	@Override
	public String SearchInsurAppl(Map<String, Object> applNoMap) throws Exception {
		ByteArrayOutputStream out;
		String str="";
		StringBuilder errMsg=new StringBuilder();
		if(null==applNoMap||applNoMap.isEmpty()||StringUtils.isBlank((String)applNoMap.get("applNo"))){
			errMsg.append("投保单号不允许空值");
		}else{
				GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class,applNoMap);	
				if(null!=grpInsurAppl){
					String cntrType=StringUtils.isBlank(grpInsurAppl.getCntrType())?"G":grpInsurAppl.getCntrType();
					 out = new ByteArrayOutputStream();
					if ("G".equals(cntrType)&&null==grpInsurAppl.getConstructInsurInfo()) {
							CreateGrpIsurAppl.createGroup(out, grpInsurAppl);
						//CreateGrpIsurAppl.createAccident(out, grpInsurAppl);//测试建工险	
					}
					else if("G".equals(cntrType)&&null!=grpInsurAppl.getConstructInsurInfo()){
						CreateGrpIsurAppl.createAccident(out, grpInsurAppl);
					}
					else if ("L".equals(cntrType)) {
						Double moneyinCount=null;
						 if(StringUtils.equals("Y",grpInsurAppl.getPaymentInfo().getIsMultiPay())){
							 Object mioPlnmioInfo = mongoBaseDao.findOne(MioPlnmioInfo.class,applNoMap);
							 if(null!=mioPlnmioInfo){
								 MioPlnmioInfo plnmioInfo=(MioPlnmioInfo) mioPlnmioInfo;
								 moneyinCount=plnmioInfo.getSumPremium();
							 }
						 }
						 CreateGrpIsurAppl.createConcurrent(out, grpInsurAppl,moneyinCount);
						  
						  
					}
					inputStream = new ByteArrayInputStream(out.toByteArray());
					out.close();
					str=Base64.byteArrayToBase64(input2byte(inputStream));
				}else{
					errMsg.append("当前投保单号所查团单信息为空");
				}
			}
		return str;
	}


	  public static final byte[] input2byte(InputStream inStream)  
	            throws IOException {  
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
	        byte[] buff = new byte[1024];  
	        int rc;  
	        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        byte[] in2b = swapStream.toByteArray();  
	        return in2b;  
	    }  
	
	  
	  
	
	  
	  
}
