package com.newcore.orbps.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.cntrprint.IndexData;
import com.newcore.orbps.models.cntrprint.PubData;
import com.newcore.orbps.models.cntrprint.RegInfoData;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.TaskCntrPrintService;
import com.newcore.orbps.service.pms.api.ReceiveReqService;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.DUR_UNIT;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * @ClassName: TaskCntrPrintServiceImpl
 * @Description: 打印
 * @author chenyongcai
 * @date 2016年9月1日 上午11:06:50
 *
 */
@Service("taskCntrPrintService")
public class TaskCntrPrintServiceImpl implements TaskCntrPrintService {

	@Override
	public void cntrGrpPrint(String applNo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cntrLstPrint(String applNo) {
		// TODO Auto-generated method stub
		
	}
	
}
