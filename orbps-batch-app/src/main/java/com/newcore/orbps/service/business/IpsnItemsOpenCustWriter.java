package com.newcore.orbps.service.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskRetInfo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.CustomerAccountService;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class IpsnItemsOpenCustWriter<T> implements ItemWriter<T> {

	private static Logger logger = LoggerFactory.getLogger(IpsnItemsOpenCustWriter.class);
	
	private int openCustNoCount;
	@Autowired
	private CustomerAccountService customerAccountService;
	
	@Override
	public synchronized void write(List<? extends T> items) throws Exception {
		int maxAttempts = 3;
		List<Map<String, Object>> jsonObjects = new ArrayList<>();
		Map<Long, GrpInsured> grpInsuredMap = new HashMap<>();
		
		for (Map<GrpInsured,List<JSONObject>> item : (List<Map<GrpInsured,List<JSONObject>>>) items) {			
			for(GrpInsured grpInsured : item.keySet()){
				grpInsuredMap.put(grpInsured.getIpsnNo(), grpInsured);
				jsonObjects.addAll(item.get(grpInsured));
			}			
		}
		String applNo = (String) jsonObjects.get(0).get("APPL_NO");
		logger.info(applNo+ "size:"+ jsonObjects.size() +"：开始开户");
		//新增调用服务重试策略
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts); //设置重试次数
		retryTemplate.setRetryPolicy(retryPolicy);
		List<JSONObject> custNoList = retryTemplate.execute(
				new RetryCallback<List<JSONObject>, Exception>() {
					public List<JSONObject> doWithRetry(RetryContext context) throws Exception {
						//调用保单落地服务
						return customerAccountService.custaccount(jsonObjects);
					}
				}
		);

		logger.info(applNo+ "size: "+ jsonObjects.size() +"：开户完毕");
		logger.info(applNo+ "size: "+ jsonObjects.size() +"：开始客户号回写");
		customerAccountService.update(grpInsuredMap, custNoList);
		logger.info(applNo+ "size: "+ jsonObjects.size() +"：客户号回写完毕");
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *            the customerAccountService to set
	 */
	public void setCustomerAccountService(CustomerAccountService customerAccountService) {
		this.customerAccountService = customerAccountService;
	}

	/**
	 * @return the openCustNoCount
	 */
	public int getOpenCustNoCount() {
		return openCustNoCount;
	}

	/**
	 * @param openCustNoCount
	 *            the openCustNoCount to set
	 */
	public void setOpenCustNoCount(int openCustNoCount) {
		this.openCustNoCount = openCustNoCount;
	}

}
