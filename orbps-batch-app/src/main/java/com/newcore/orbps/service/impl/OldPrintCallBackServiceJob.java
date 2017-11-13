package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.header.HeaderInfoHolder;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbps.service.oldpms.api.Dyhbpsqyapisrv;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.VOUCHER_PRINT_TYPE;
import com.newcore.supports.models.cxf.AuthInfo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.cxf.PageInfo;
import com.newcore.supports.models.cxf.ProcessInfo;
import com.newcore.supports.models.cxf.RouteInfo;
import com.newcore.supports.models.cxf.SecurityInfo;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 保单辅助打印数据落地反馈服务定时任务
 * @author JCC
 * 2017年5月18日 20:29:22
 * 每隔1分执行一次。
 */
@Schedule(cron = "0 0/1 * * * ?")   
@Service("OldPrintCallBackServiceQuartz")
@DisallowConcurrentExecution
public class OldPrintCallBackServiceJob implements Job {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(OldPrintCallBackServiceJob.class);

	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	OldPrintTaskQueueDao oldPrintTaskQueueDao;
	
	@Autowired
	Dyhbpsqyapisrv dyhbpsqyapisrvClient;
	
	@Autowired
	MioLogDao mioLogDao;
	
	@Autowired
	SalesmanInfoService salesmanInfoService;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
    PolNatureService polNatureService;
	
	@Autowired
	BranchService branchService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//STATUS: N=新建告知，K=告知处理中，E=告知失败，C=告知成功，D=需要调用老打印，S=老打印处理成功成功，W=老打印处理成功处理中，F=老打印处理成功失败
		logger.info("执行方法[保单辅助打印数据落地反馈服务定时任务]:开始运行！");
		String [] status = {"D","F"};
		SqlRowSet row = oldPrintTaskQueueDao.queryOldPrintQueue(status);
		while(row.next()){
			String applNo=row.getString(1); //投保单号
			try{
				logger.info("执行方法[保单辅助打印数据落地反馈服务定时任务]:开始业务处理 ！["+applNo+"]");
				//锁定当然处理的投保单 status改成K
				oldPrintTaskQueueDao.updateDateByApplNo(applNo, "W");
				boolean isSusscc = callBack(applNo);
				if(isSusscc){
					logger.info("执行方法[保单辅助打印数据落地反馈服务定时任务]:业务处理完成 ！["+applNo+"]");
					oldPrintTaskQueueDao.updateDateByApplNo(applNo, "S");
				}else{
					logger.info("执行方法[保单辅助打印数据落地反馈服务定时任务]:业务处理失败 ！["+applNo+"]");
					oldPrintTaskQueueDao.updateDateByApplNo(applNo, "F");
				}
			}catch(Exception ex){
				logger.info("执行方法[保单辅助打印数据落地反馈服务定时任务]操作失败！["+applNo+"]"+ex);
				oldPrintTaskQueueDao.updateDateByApplNo(applNo, "F");
			}
		}
	}

	/**
	 * 调用老打印功能
	 * @param applNo
	 * @return
	 */
	public boolean callBack(String applNo){ 
		logger.info("开始执行——保单辅助打印数据落地反馈服务实现类["+applNo+"]");
		//保单信息
		GrpInsurAppl appl = procMioInfoDao.getGrpInsurAppl(applNo);
		String requestXml = getRequestXML(appl);
		logger.info("["+applNo+"]调用短险打印服务接口请求报文体:"+requestXml);
		//定义报文头
		setOutboundHeader(appl.getProvBranchNo());
		String result = dyhbpsqyapisrvClient.srvhbpsqyapi(requestXml);
		logger.info("["+applNo+"]调用短险打印服务接口返回报文体:"+result);
		
		boolean isSusscc=false;
		if(result.indexOf("<RetCode>0</RetCode>") > -1){
			isSusscc=true;
		}
		return isSusscc;
	}
	
	/**
	 * 封装请求报文体
	 * @return
	 */
	private String getRequestXML(GrpInsurAppl appl) {
		String applNo = appl.getApplNo();
		String polCode = getMainPolCode(appl); 	//主险代码
		String billClass="012";					//单正类别代码
		String subClass="004";					//单正类别子代码
		String batchNo ="";						//批次号
		String endFlag ="";						//传输结束标志
		SalesInfo sales = getSalesInfo(appl);	//销售员信息：是否共同展业销售人员	
		SalesmanQueryReturnBo salesman=getSalesmanQuery(sales);//销售员信息
		Branch branch = getBranch(applNo); 
		
		String tagInfo = "1";	//个人保单打印类型
		String policyFlg=getPolicyFlg(appl); 	//凭证标志
		//合同生成日期
		String cntrCreateDate= getFormatDate(mioLogDao.getMioLogUpdTime(applNo,"APPL"));
		//保费确认日期
		String premConfirmDate= getFormatDate(mioLogDao.getMioLogUpdTime(appl.getCgNo(),"CGNO"));
		
		String batchDataFlag = "2"; 	//批次数据状态
		SalesInfo salesOA = getSalesInfoOA(appl);
		int policyType = getPolicyType(appl);   //险种类别
		switch (policyType) {
			case 1:
				batchNo ="0";
				endFlag ="0";	
				break;
			case 2:
				batchNo ="1";
				endFlag ="1";	
				break;
			case 3:
			case 4:
				billClass="009";
				subClass=getSubClass(appl);	
				batchNo ="1";
				endFlag ="1";	
				tagInfo="c";
				batchDataFlag="3";
				break;
			case 5:
				billClass="007";
				break;
		}
		if(policyType==4){
			//基金险与团单非基金险的区别就是billClass赋值为010，非基金险是009
			billClass="010";
		}

		StringBuilder sbd = new StringBuilder();
		sbd.append("<BODY><REQUEST><SERIALNUM>1</SERIALNUM><SG_ISSUE_NOTICE><Request>");	
		sbd.append("<SysSource>004</SysSource>");				//系统号
		sbd.append("<PrintType>0</PrintType>");					//清单类型
		sbd.append("<IPAddress>192.168.1.20</IPAddress>");		//终端IP
		sbd.append("<PrevewServerIP>1</PrevewServerIP>");		//JETFORM服务器IP
		sbd.append("<TtyNo></TtyNo>");							//终端号码
		sbd.append("<PrdCode>"+polCode+"</PrdCode>");			//主险代码
		sbd.append("<BillClass>"+billClass+"</BillClass>");		//单正类别代码
		sbd.append("<SubClass>"+subClass+"</SubClass>");		//单正类别子代码
		sbd.append("<FormCode></FormCode>");					//表单代码
		sbd.append("<PolicyNo>"+appl.getCgNo()+"</PolicyNo>");	//保单号
				
		sbd.append("<BillNo>"+appl.getCgNo()+"</BillNo>");		//单证号
		sbd.append("<BatchNo>"+batchNo+"</BatchNo>");			//批次号
		sbd.append("<EndFlag>"+endFlag+"</EndFlag>");			//传输结束标志
		sbd.append("<ManageBranch>"+appl.getMgrBranchNo()+"</ManageBranch>");	//管理机构号
		sbd.append("<AgentBranch>"+sales.getSalesBranchNo()+"</AgentBranch>");  //销售机构代码
		sbd.append("<AgentLocation></AgentLocation>");						//销售职场代码
		sbd.append("<AgentNo>"+sales.getSalesNo()+"</AgentNo>");			//销售员工号
		sbd.append("<AgentName>"+salesman.getSalesName()+"</AgentName>");	//代理人姓名
		sbd.append("<AgentTel>"+salesman.getSalesMobile()+"</AgentTel>");	//销售人员电话
		sbd.append("<AcceptBranch>"+branch.getBranchNo()+"</AcceptBranch>");//受理机构
				
		sbd.append("<AcceptNode>2</AcceptNode>");			//受理网点
		sbd.append("<OperateBranch>"+appl.getMgrBranchNo()+"</OperateBranch>");//操作机构号
		sbd.append("<SignBranch>"+appl.getMgrBranchNo()+"</SignBranch>");	   //签单机构
		sbd.append("<ArchiveBranch></ArchiveBranch>");		//归档机构号
		sbd.append("<CenterCode>"+salesman.getCenterCode()+"</CenterCode>");	//核算单元
		sbd.append("<WorkplaceCode></WorkplaceCode>");		//职场代码
		sbd.append("<WorkplaceName></WorkplaceName>");		//职场名称
		sbd.append("<Tag_Info>"+tagInfo+"</Tag_Info>");		//个人保单打印类型
		sbd.append("<POLICY_FLG>"+policyFlg+"</POLICY_FLG>");//凭证标志
		sbd.append("<Tag_Flag>0</Tag_Flag>");				//清汇保单打印状态
				
		sbd.append("<CNTR_CREATE_DATE>"+cntrCreateDate+"</CNTR_CREATE_DATE>");	 //合同生成日期
		sbd.append("<PREM_CONFIRM_DATE>"+premConfirmDate+"</PREM_CONFIRM_DATE>");//保费确认日期
		sbd.append("<IPSN_COUNT>"+findIpsnNum(applNo)+"</IPSN_COUNT>");			 //承保人数
		sbd.append("<Batch_Data_Flag>"+batchDataFlag+"</Batch_Data_Flag>");		 //批数据状态
		sbd.append("<GmisBranchNo>"+salesOA.getSalesBranchNo()+"</GmisBranchNo>");	//代理机构号
		sbd.append("<GmisBranchName>"+salesOA.getDeptName()+"</GmisBranchName>");	//代理机构名称
		sbd.append("<GmisSalesNo>"+salesOA.getCommnrPsnNo()+"</GmisSalesNo>");		//代理销售员工号
		sbd.append("<GmisSalesName>"+salesOA.getCommnrPsnName()+"</GmisSalesName>");//代理机构销售人员姓名
		sbd.append("<ElectronFlag>0</ElectronFlag>");		//电子保单标识
		sbd.append("<R_PRINT>0</R_PRINT>");					//补换发标志
				
		sbd.append("</Request><Response>");
		sbd.append("<Proc_Flag>0</Proc_Flag>");
		sbd.append("<Error_Info>0</Error_Info>");
		sbd.append("</Response></SG_ISSUE_NOTICE></REQUEST></BODY>");
		return sbd.toString();
	}
	
	/**
	 * 获取销售员信息
	 * @param sales
	 * @return
	 */
	private SalesmanQueryReturnBo getSalesmanQuery(SalesInfo sales) {
		SalesmanQueryInfo queryInfo = new SalesmanQueryInfo();
		queryInfo.setSalesChannel(sales.getSalesChannel());	  //销售渠道
		queryInfo.setSalesBranchNo(sales.getSalesBranchNo()); //销售机构号
		queryInfo.setSalesNo(sales.getSalesNo());			  //销售员代码
		//消息头设置
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
		SalesmanQueryReturnBo salesman=salesmanInfoService.querySalesman(queryInfo);
		return salesman;
	}


	/**
	 * 根据机构号，查询本机构信息.
	 * @param applNo
	 * @return
	 */
	private Branch getBranch(String applNo) {
		String pclkBranchNo = getAcceptBranch(applNo);
		//消息头设置
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
		Branch branch =  branchService.findOne(pclkBranchNo); 
		return branch;
	}

	/**
	 * 获取凭证标识
	 * @param appl
	 * @return
	 */
	private String getPolicyFlg(GrpInsurAppl appl) {
		String policyFlg="";
		if("1".equals(appl.getListPrintType()) &&"0".equals(appl.getVoucherPrintType())){
			policyFlg="L";
		}else if("1".equals(appl.getListPrintType()) &&"1".equals(appl.getVoucherPrintType())){
			policyFlg="V";
		}else if("0".equals(appl.getListPrintType()) &&"0".equals(appl.getVoucherPrintType())){
			policyFlg="P";
		}
		return policyFlg;
	}

	/**
	 * 获取销售渠道为OA的销售员
	 * @param appl
	 * @return
	 */
	private SalesInfo getSalesInfoOA(GrpInsurAppl appl) {
		SalesInfo salesInfo = new SalesInfo();
		salesInfo.setSalesBranchNo("");
		salesInfo.setDeptName("");
		salesInfo.setCommnrPsnNo("");
		salesInfo.setCommnrPsnName("");
		for(SalesInfo sales:appl.getSalesInfoList()){
			//共同展业主副标记  1：主销售员；2：附销售员
			if("OA".equals(sales.getSalesChannel())){
				if(!"".equals(sales.getSalesNo()) && !"".equals(sales.getSalesDeptNo())){
					salesInfo =sales;
					break;
				}
			}
		}
		return salesInfo;
	}

	/**
	 * 获取有效被保人数量
	 * @param applNo
	 * @return
	 */
	private long findIpsnNum(String applNo) {
		//组织发送报文
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		queryMap.put("procStat", "E");
		return mongoBaseDao.count(GrpInsured.class, queryMap);
	}
	
	/**
	 * 获取受理机构
	 * @param applNo
	 * @return
	 */
	private String getAcceptBranch(String applNo) {
		//查询操作轨迹中受理轨迹的操作员机构号
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo)),
				Aggregation.unwind("operTraceDeque"),
				Aggregation.match(Criteria.where("operTraceDeque.procStat").is("00"))
		);
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "insurApplOperTrace", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
		JSONObject json = (JSONObject) JSONObject.toJSON(obj.get("operTraceDeque"));
		return json.getString("pclkBranchNo");
	}

	/**
	 * 获取销售员信息
	 * @param appl 保单信息
	 * @return
	 */
	private SalesInfo getSalesInfo(GrpInsurAppl appl) {
		SalesInfo salesInfo = new SalesInfo();
		//销售人员是否共同展业标识: N:否，Y：是 ; 若是共同展业取主销售员，不是取列表中的第一个。SalesInfo.salesBranchNo
		if("N".equals(appl.getSalesDevelopFlag())){
			salesInfo = appl.getSalesInfoList().get(0);
		}else if("Y".equals(appl.getSalesDevelopFlag())){
			for(SalesInfo sales:appl.getSalesInfoList()){
				//共同展业主副标记  1：主销售员；2：附销售员
				if("1".equals(sales.getDevelMainFlag())){
					salesInfo = sales;
					break;
				}
			}
		}
		return salesInfo;
	}

	/**
	 * 获取单正类别子代码
	 * @param appl
	 * @return
	 */
	private String getSubClass(GrpInsurAppl appl) {
		String subClass="";
		if("1".equals(appl.getListPrintType()) && "0".equals(appl.getVoucherPrintType())){
			subClass="001";
		}else if("1".equals(appl.getListPrintType()) && "1".equals(appl.getVoucherPrintType())){
		    subClass="002";
		}else if("0".equals(appl.getListPrintType()) && "0".equals(appl.getVoucherPrintType())){
		    subClass="003";
		}
		return subClass;
	}

	/**
	 * 获取第一主险代码
	 * @param appl
	 * @return
	 */
	private String getMainPolCode(GrpInsurAppl appl) {
		String polCode = "";
		for (Policy policy : appl.getApplState().getPolicyList()) {
			if (MR_CODE.MASTER.getKey().equals(policy.getMrCode())) {
				polCode = policy.getPolCode();
				break;
			}
		}
		return polCode;
	}

	/**
	 * 获取险种类别
	 * @param appl 保单信息
	 * @return
	 * 险种类别   policyType: 
	 * 1-清汇非家庭险,2-清汇家庭险,3-团单非基金险,4-团单基金险,5-建工险
	 */
	private int getPolicyType(GrpInsurAppl appl) {
		//契约形式    G：团单；L：清汇;
		String cntrType = appl.getCntrType();
		int policyType=0;
		if("L".equals(cntrType)){
			//个人凭证类型， 0：电子个人凭证；1：纸制个人凭证；  TODO
			if(StringUtils.equals(VOUCHER_PRINT_TYPE.FAMILY_ELEC_CREDENTIALS.getKey(),appl.getVoucherPrintType()) || 
					StringUtils.equals(VOUCHER_PRINT_TYPE.FAMILY_PAPER_CREDENTIALS.getKey(),appl.getVoucherPrintType())){
				//清汇家庭险
				policyType =2;
			}else{//普通清汇
				policyType =1;
			}
		}else if("G".equals(cntrType)){
			//判断是否是建工险
			if(appl.getConstructInsurInfo()!=null){
				policyType=5;
			}else{
				policyType=3;
				//基金险功模块业务：在生效的时候，需要计算公共与个人账户金额以及管理费金额
		        boolean isFund = checkPolCodeIsFund(appl.getApplState().getPolicyList());
		        if(isFund){
		        	policyType = 4;
		        }
	        }
		}
		return policyType;
	}

   /**
     * 判断险种是否是基金险
     * @param policyList 险种
     * @return 
     * 	包含基金险-true
     *  无基金险 -false
     */
	private boolean checkPolCodeIsFund(List<Policy> policyList) {
    	List<String> polCodes = new ArrayList<>();
        for(Policy policy:policyList){
        	polCodes.add(policy.getPolCode());
        }
        //消息头设置
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<JSONObject> polNatureList=polNatureService.getPolNatureInfo(polCodes);
        boolean isTrue = false;
        for(JSONObject json:polNatureList){
        	String isFund =json.getString("isFund");     //是否基金险
        	if("Y".equals(isFund)){
        		isTrue = true;
        		break;
        	}
        }
        return isTrue;
	}
	
	/**
	 * 自定义报文头
	 * @param provBranchNo 省级机构号
	 * @return
	 */
	private void setOutboundHeader(String provBranchNo) {
		//消息头设置
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");					
		headerInfo.setServiceCode("PMS-0012");
		headerInfo.setServiceName("Dyhbpsqyapisrv");	
		headerInfo.setServiceVersion("1.0");
		//登录认证信息部分
		AuthInfo authInfo= new AuthInfo();
		authInfo.setBranchNo("");
		authInfo.setTokenId("");
		authInfo.setUserId("");
		headerInfo.setAuthInfo(authInfo);
		//安全信息部分
		SecurityInfo security = new SecurityInfo();
		security.setSignature("10");
		headerInfo.setSecurityInfo(security);
		//路由信息
		RouteInfo routeInfo = new RouteInfo();
		routeInfo.setBranchNo(provBranchNo);
		routeInfo.setDestSystem("PMS");
		headerInfo.setRouteInfo(routeInfo);
		//控制信息
		ProcessInfo  processInfo = new ProcessInfo();
		processInfo.setPageInfo(new PageInfo());
		processInfo.setProcessMode("S");
		processInfo.setProcessNum("1");
		headerInfo.setProcessInfo(processInfo);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	private String getFormatDate(Date date) {
		if(date ==  null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		return sdf.format(date);
	}
}

