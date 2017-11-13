package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.cntrprint.CommonData;
import com.newcore.orbps.models.cntrprint.DataAdapter;
import com.newcore.orbps.models.cntrprint.PmsDataInfo;
import com.newcore.orbps.models.cntrprint.SgInstallmentInfo;
import com.newcore.orbps.models.cntrprint.SgMultiPayInfo;
import com.newcore.orbps.models.cntrprint.vo.EPrintVO;
import com.newcore.orbps.models.cntrprint.vo.LabelDataVO;
import com.newcore.orbps.models.cntrprint.vo.PrintDataVO;
import com.newcore.orbps.models.cntrprint.vo.PrintVO;
import com.newcore.orbps.models.cntrprint.vo.TableListVO;
import com.newcore.orbps.models.cntrprint.vo.TableRowVO;
import com.newcore.orbps.models.cntrprint.vo.TableSonVO;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.service.api.TaskCntrPrintService;
import com.newcore.orbps.service.ipms.api.PolicyBasicService;
import com.newcore.orbps.service.pms.api.ReceiveReqService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.BizParameterDao;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.DUR_UNIT;
import com.newcore.supports.dicts.GRP_ID_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.SEX;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
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
	/**
	 * 日志管理工具实例.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskCntrPrintServiceImpl.class);

	@Autowired
	TaskProcessService taskProcessServiceDascClient;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	DascInsurParaDao dascInsurParaDao;

	@Autowired
	ReceiveReqService receiveReqServiceClient;

	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	PolicyBasicService policyBasicService;
	
	@Autowired
	BranchService branchService;	
	
	@Autowired
	BizParameterDao bizParameterDao;
	
	@Autowired
	OldPrintTaskQueueDao oldPrintTaskQueueDao;
	
	@Override
	@AsynCall
	public String cntrPrint(String args) {
		String[] argsArr = args.split("_");
		// 业务逻辑开始
		LOGGER.info("执行方法:打印" + "业务流水号:" + argsArr[0] + ",任务ID:" + argsArr[1]);
		try {
			// 业务逻辑处理开始
			// 根据业务流水号查询契约形式
			String applNo = argsArr[0];

			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("applNo", applNo);
			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
			if (null == grpInsurAppl) {
				throw new BusinessException("0019", applNo);
			}
			String cntrType = grpInsurAppl.getCntrType();
			// 设置加费信息
			setAddPrem(grpInsurAppl);
			System.out.println("applNo:" + applNo + ",cntrType:" + cntrType);
			if (StringUtils.equals(cntrType, CNTR_TYPE.GRP_INSUR.getKey())) {
				cntrGrpPrint(applNo);
			} else if (StringUtils.equals(cntrType, CNTR_TYPE.LIST_INSUR.getKey())) {
				cntrLstPrint(applNo);
			} else {
				LOGGER.info("契约形式不正确");
			}

			// 插入操作轨迹
			TraceNode traceNode = new TraceNode();
			traceNode.setProcStat(NEW_APPL_STATE.PRINT.getKey());
			traceNode.setProcDate(new Date());
			mongoBaseDao.updateOperTrace(applNo, traceNode);

			// 调用任务管理平台的完成任务接口
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			Map<String, String> mapVar = new HashMap<>();// 选填-放入流程运转过程中必须的业务信息~
			mapVar.put("IS_RECEIPTVERIFICA", "1");
			taskProcessRequestBO.setProcessVar(mapVar);

			taskProcessRequestBO.setBusinessKey(applNo);
			taskProcessRequestBO.setTaskId(argsArr[1]);// 业务服务参数中获取的任务ID
			taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作
			LOGGER.info("TaskCntrPrintService调用任务管理平台的completeTask接口" + applNo);

			return null;
		} catch (Exception e) {
			LOGGER.info("TaskCntrPrintService Exception");
			throw e;
		}
	}
	
	@Override
	public void cntrGrpPrint(String applNo) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 格式化设置

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("applNo", applNo);

		// 根据投保单号获取团单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, param);
		if (grpInsurAppl == null) {
			LOGGER.error("保单基本信息不存在: " + applNo);
			throw new BusinessException("0016", applNo);
		}

		//新增业务逻辑开始
		doOldPrintTask(grpInsurAppl);
		//新增业务逻辑结束
		
		
		// 根据投保单号获取操作轨迹信息
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
				param);
		String applChkClerkName = "";
		String cntrCreateDate = ""; // 合同生成日期
		String premConfirmDate = ""; // 保费确认日期"
		String cntrFoundDate = ""; // 合同成立
		if (null != insurApplOperTrace) {
			for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {
				if (isReview(traceNode)) {
					applChkClerkName = traceNode.getPclkName();
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.POLICY_INFORCE.getKey())) {
					cntrCreateDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDDHHMM);
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PAY_CHECK.getKey())) {
					premConfirmDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDDHHMM);
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.UNDERWRITING.getKey())) {
					cntrFoundDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDD);
				}
			}
		} else {
			LOGGER.error("操作轨迹不存在: " + applNo);
			throw new BusinessException("0016", applNo);
		}

		EPrintVO ePrintVO = new EPrintVO();
		// regInfoData
		CommonData common = new CommonData();
		// 建工险
		if (grpInsurAppl.getConstructInsurInfo() != null) {
			common.getRegInfoData().setItfid("119");
			common.getRegInfoData().setTplcode("TPL00271");
		} else {
			common.getRegInfoData().setItfid("120");
			common.getRegInfoData().setTplcode("TPL00268");
		}

		common.getRegInfoData().setTplversion("1.0");
		common.getRegInfoData().setParamtype("3");

		// indexData
		common.getIndexData().setIndexno(grpInsurAppl.getCgNo());
		common.getIndexData().setApplno(grpInsurAppl.getApplNo());
		common.getIndexData().setCgno(grpInsurAppl.getCgNo());
		common.getIndexData().setPrdname(grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn());
		common.getIndexData().setPrdcode(grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());

		// pubData
		common.getPubData().setOrganization(grpInsurAppl.getProvBranchNo());
		common.getPubData().setPhdrcustno(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
		common.getPubData().setPhdrname(grpInsurAppl.getGrpHolderInfo().getContactName());
		if (grpInsurAppl.getGrpHolderInfo().getContactMobile() != null
				&& !grpInsurAppl.getGrpHolderInfo().getContactMobile().isEmpty()) {
			common.getPubData().setPhdrhomephone(grpInsurAppl.getGrpHolderInfo().getContactMobile());
		} else {
			common.getPubData().setPhdrhomephone(grpInsurAppl.getGrpHolderInfo().getContactTelephone());
		}
		common.getPubData().setPhdremail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
		common.getPubData().setManagebranch(grpInsurAppl.getMgrBranchNo());

		// 获取销售员信息
		SalesInfo salesInfo = new SalesInfo();
		if (StringUtils.equals(grpInsurAppl.getSalesDevelopFlag(), YES_NO_FLAG.NO.getKey())) {
			salesInfo = grpInsurAppl.getSalesInfoList().get(0);
		} else if (StringUtils.equals(grpInsurAppl.getSalesDevelopFlag(), YES_NO_FLAG.YES.getKey())) {
			for (SalesInfo salesInfoTmp : grpInsurAppl.getSalesInfoList()) {
				if (StringUtils.equals(salesInfoTmp.getDevelMainFlag(), DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey())) {
					salesInfo = salesInfoTmp;
				}
			}
		}

		common.getPubData().setSaleschannel(salesInfo.getSalesChannel());
		common.getPubData().setSyssource("4");
		
		// 建工险
		if (grpInsurAppl.getConstructInsurInfo() != null) {
			common.getPubData().setBillclass("007");
		}
		else
		{
			common.getPubData().setBillclass("009");	
		}
		common.getPubData().setCgno(grpInsurAppl.getCgNo());
		common.getPubData().setSgno(grpInsurAppl.getSgNo());

		ePrintVO.setCommonDatas(common.toPrintData());

		// printData
		PrintDataVO printData = new PrintDataVO();

		StringBuilder sb = new StringBuilder(grpInsurAppl.getCgNo());
		sb.insert(4, "-");
		sb.insert(11, "-");
		sb.insert(15, "-");
		sb.insert(24, "-");
		printData.addLabelData(new LabelDataVO("cggno", sb.toString()));

		// healthInsurInfo
		if (grpInsurAppl.getHealthInsurInfo() != null) {
			// 固定公共保额 需打印，公共保额和公共保费；公共保额=健康险中公共保额；公共保费=健康险中公共保费
			// 浮动公共保额 需打印，公共保额；公共保额=人均浮动保额*人数
			if (StringUtils.equals(COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey(),
					grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())) {
				printData.addLabelData(new LabelDataVO("public_insurance_amount",
						decimalFormat.format(grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt())));
				printData.addLabelData(new LabelDataVO("public_insurance_premium",
						decimalFormat.format(grpInsurAppl.getHealthInsurInfo().getComInsrPrem())));
			} else {
				printData.addLabelData(new LabelDataVO("public_insurance_amount",
						decimalFormat.format(grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()
								* grpInsurAppl.getApplState().getIpsnNum())));

				printData.addLabelData(new LabelDataVO("public_insurance_premium", "0.00"));
			}
		} else {
			printData.addLabelData(new LabelDataVO("public_insurance_amount", "0.00"));
			printData.addLabelData(new LabelDataVO("public_insurance_premium", "0.00"));
		}

		if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo())) // 销售网点及地址
		{
			printData.addLabelData(new LabelDataVO("salesNoAdd", salesInfo.getSalesDeptNo()));
		} else {
			printData.addLabelData(new LabelDataVO("salesNoAdd", ""));
		}

		if (grpInsurAppl.getConstructInsurInfo() != null) {
			printData.addLabelData(
					new LabelDataVO("construction_name", grpInsurAppl.getConstructInsurInfo().getIobjName()));
			printData.addLabelData(new LabelDataVO("construction_total",
					decimalFormat.format(grpInsurAppl.getConstructInsurInfo().getIobjCost())));
			printData.addLabelData(new LabelDataVO("construction_area",
					decimalFormat.format(grpInsurAppl.getConstructInsurInfo().getIobjSize())));
		}
		printData.addLabelData(new LabelDataVO("cntr_establish_date", cntrFoundDate));

		printData.addLabelData(new LabelDataVO("cntr_no", grpInsurAppl.getCgNo()));
		printData.addLabelData(new LabelDataVO("cntrtype", grpInsurAppl.getCntrType()));
		printData.addLabelData(new LabelDataVO("phdr_name", grpInsurAppl.getGrpHolderInfo().getGrpName()));
		
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Branch branch = branchService.findOne(grpInsurAppl.getProvBranchNo());
		printData.addLabelData(new LabelDataVO("sign_branch", branch.getPrintBranchName()));

		if (null != grpInsurAppl.getSignDate()) {
			printData.addLabelData(new LabelDataVO("sign_date",
					DateFormatUtils.format(grpInsurAppl.getSignDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		printData.addLabelData(new LabelDataVO("appl_no", grpInsurAppl.getApplNo()));
		printData.addLabelData(new LabelDataVO("phdr_cust_no", grpInsurAppl.getGrpHolderInfo().getGrpCustNo()));
		printData.addLabelData(new LabelDataVO("currency",
				CURRENCY_CODE.valueOfKey(grpInsurAppl.getPaymentInfo().getCurrencyCode()).getDescription() + "元"));

		if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			long ipsnNum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
					GrpInsured.class);
			printData.addLabelData(new LabelDataVO("ipsn_count", Long.toString(ipsnNum)));
		} else {
			if (null != grpInsurAppl.getGrpHolderInfo().getIpsnNum()) {
				printData.addLabelData(
						new LabelDataVO("ipsn_count", grpInsurAppl.getGrpHolderInfo().getIpsnNum().toString()));
			}
		}

		printData.addLabelData(new LabelDataVO("moneyin_itrvl",
				MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription()));

		if (null != grpInsurAppl.getInForceDate()) {
			printData.addLabelData(new LabelDataVO("in_force_date",
					DateFormatUtils.format(grpInsurAppl.getInForceDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
				Aggregation.unwind("subStateList"), Aggregation.group("$applNo").sum("subStateList.faceAmnt")
						.as("sumFaceAmnt").sum("subStateList.sumPremium").as("sumPremium"));
		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();

		if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			Double addPrem = setAddPremAllPol(grpInsurAppl);
			printData.addLabelData(new LabelDataVO("sumpremium",
					decimalFormat.format(jsonObjects.get(0).getDouble("sumPremium") + addPrem)));
			printData.addLabelData(
					new LabelDataVO("sumfaceamnt", decimalFormat.format(jsonObjects.get(0).getDouble("sumFaceAmnt"))));
		} else {

			printData.addLabelData(
					new LabelDataVO("sumpremium", decimalFormat.format(grpInsurAppl.getApplState().getSumPremium())));
			printData.addLabelData(
					new LabelDataVO("sumfaceamnt", decimalFormat.format(grpInsurAppl.getApplState().getSumFaceAmnt())));

		}

		if (null != grpInsurAppl.getCntrExpiryDate()) {
			printData.addLabelData(new LabelDataVO("cntr_term_date",
					DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		printData.addLabelData(new LabelDataVO("agent_branch_code", salesInfo.getSalesBranchNo()));

		printData.addLabelData(new LabelDataVO("agent_branch_name", salesInfo.getSalesBranchName()));
		printData.addLabelData(new LabelDataVO("agentno", salesInfo.getSalesNo()));
		printData.addLabelData(new LabelDataVO("agentname", salesInfo.getSalesName()));
		printData.addLabelData(new LabelDataVO("sign_addr", branch.getAddress()));

		printData.addLabelData(new LabelDataVO("post_code", salesInfo.getSalesBranchPostCode()));
		printData.addLabelData(new LabelDataVO("service_tel", "95519"));
		printData.addLabelData(new LabelDataVO("printname", "ePrint"));
		printData.addLabelData(new LabelDataVO("appl_chk_clerk_name", applChkClerkName));

		printData.addLabelData(new LabelDataVO("branch_no", salesInfo.getSalesBranchNo()));
		printData.addLabelData(new LabelDataVO("branch_name", salesInfo.getSalesBranchName()));
		printData.addLabelData(new LabelDataVO("cntr_create_date", cntrCreateDate));

		if (ProcMioInfoUtils.isBankTrans(grpInsurAppl)) {
			String sqlMioDate = "select min(mio_date) from mio_log "
					+ "where instr(cntr_no, ?)>0 and mio_item_code='FA' "
					+ "and mio_class=1 and mio_type='T' and mio_tx_class=0";
			Date mioDate = jdbcTemplate.queryForObject(sqlMioDate, Date.class, applNo);
			printData.addLabelData(
					new LabelDataVO("first_pay_date", DateFormatUtils.format(mioDate, PrintVO.DATE_FORMAT_YYYYMMDD)));
		} else {
			printData.addLabelData(new LabelDataVO("first_pay_date",
					DateFormatUtils.format(grpInsurAppl.getSignDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		printData.addLabelData(new LabelDataVO("prem_confirm_date", premConfirmDate)); // 保费确认日期
		printData.addLabelData(new LabelDataVO("cntrPrintType", grpInsurAppl.getCntrPrintType())); // 合同打印方式

		if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			printData.addLabelData(new LabelDataVO("listPrintType", grpInsurAppl.getListPrintType())); // 清单打印方式
			printData.addLabelData(new LabelDataVO("voucherPrintType", grpInsurAppl.getVoucherPrintType())); // 个人凭证类型
		} else {
			printData.addLabelData(new LabelDataVO("listPrintType", "n"));
			printData.addLabelData(new LabelDataVO("voucherPrintType", "n"));
		}

		if (!StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())) {
			printData.addLabelData(new LabelDataVO("moneyinType",
					MONEYIN_TYPE.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinType()).getDescription())); // 收款形式
		} else {
			printData.addLabelData(new LabelDataVO("moneyinType", "")); // 收款形式
		}

		printData.addLabelData(new LabelDataVO("operator_name", "")); // 经办人

		if (grpInsurAppl.getGrpHolderInfo().getGrpIdType().equals(GRP_ID_TYPE.TRADE_LICENSE.getKey())) {
			printData.addLabelData(new LabelDataVO("grpIdNo", grpInsurAppl.getGrpHolderInfo().getGrpIdNo())); // 工商执照号
		} else {
			printData.addLabelData(new LabelDataVO("grpIdNo", ""));
		}

		printData.addLabelData(new LabelDataVO("taxpayerId", grpInsurAppl.getGrpHolderInfo().getTaxpayerId())); // 纳税人识别号
		printData.addLabelData(new LabelDataVO("service_url", "http://www.e-chinalife.com")); // 公司网址
		printData.addLabelData(new LabelDataVO("cntr_no", grpInsurAppl.getCgNo())); // 保险合同号

		// tabelList
		// convention_lst
		TableListVO tableList1 = new TableListVO();
		tableList1.setTableName("convention_lst");
		TableSonVO son1 = new TableSonVO();
		tableList1.addTableSon(son1);
		TableRowVO row1 = new TableRowVO();
		row1.addLabel(new LabelDataVO("inputConv", ""));
		if (null != grpInsurAppl.getConventions()) {
			row1.addLabel(new LabelDataVO("orSetConv", grpInsurAppl.getConventions().getOrSetConv()));
			row1.addLabel(new LabelDataVO("udwConv", grpInsurAppl.getConventions().getUdwConv()));
			row1.addLabel(new LabelDataVO("minorConv", grpInsurAppl.getConventions().getMinorConv()));

			if (!StringUtils.isEmpty(grpInsurAppl.getAgreementNo())) {
				String conv = "本保险合同的承保、理赔等所有与保险合同有关的事项均由首席承保人负责，投保人、被保险人、受益人如需咨询、报案、理赔等所有与本保险合同有关的服务时，均应与首席承保人联系。\r\n";
				row1.addLabel(
						new LabelDataVO("polConv", conv + "        " + grpInsurAppl.getConventions().getPolConv()));
			} else {
				row1.addLabel(new LabelDataVO("polConv", grpInsurAppl.getConventions().getPolConv()));
			}
		}
		son1.addTableRow(row1);

		// pol_lst
		TableListVO tableList2 = new TableListVO();
		tableList2.setTableName("pol_lst");
		List<Policy> policyList = grpInsurAppl.getApplState().getPolicyList();
		for (Policy policy : policyList) {
			Aggregation aggregation2 = Aggregation
					.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
							Aggregation.unwind("subStateList"),
							Aggregation.match(
									Criteria.where("subStateList.polCode").regex(policy.getPolCode().substring(0, 3))),
							Aggregation.group("$applNo").sum("subStateList.faceAmnt").as("sumFaceAmnt")
									.sum("subStateList.sumPremium").as("sumPremium"));

			AggregationResults<JSONObject> aggregate2 = mongoTemplate.aggregate(aggregation2, "grpInsured",
					JSONObject.class);
			List<JSONObject> jsonObjects2 = aggregate2.getMappedResults();

			TableSonVO son2 = new TableSonVO();
			tableList2.addTableSon(son2);

			son2.addLabel(new LabelDataVO("pol_name", policy.getPolNameChn()));
			son2.addLabel(new LabelDataVO("pol_code", policy.getPolCode()));
			son2.addLabel(new LabelDataVO("pol_ipsn_num", policy.getPolIpsnNum().toString())); // 已经不使用

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				son2.addLabel(new LabelDataVO("pol_face_amnt",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumFaceAmnt"))));
			} else {
				son2.addLabel(new LabelDataVO("pol_face_amnt", decimalFormat.format(policy.getFaceAmnt())));
			}

			son2.addLabel(new LabelDataVO("insur_dur",
					policy.getInsurDur().toString() + DUR_UNIT.valueOfKey(policy.getInsurDurUnit()).getDescription()));

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				Double addPremSinglePol = setAddPremSinglePol(grpInsurAppl, policy.getPolCode());
				son2.addLabel(new LabelDataVO("pol_std_prem",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumPremium") + addPremSinglePol)));
			} else {
				son2.addLabel(new LabelDataVO("pol_std_prem", decimalFormat.format(policy.getStdPremium())));
			}

			List<SubPolicy> subPolicyList = policy.getSubPolicyList();
			if (subPolicyList != null) {
				for (SubPolicy subPolicy : subPolicyList) {
					Aggregation aggregation3 = Aggregation.newAggregation(
							Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
							Aggregation.unwind("subStateList"),
							Aggregation.match(Criteria.where("subStateList.polCode").is(subPolicy.getSubPolCode())),
							Aggregation.group("$applNo").sum("subStateList.faceAmnt").as("sumFaceAmnt")
									.sum("subStateList.sumPremium").as("sumPremium"));
					AggregationResults<JSONObject> aggregate3 = mongoTemplate.aggregate(aggregation3, "grpInsured",
							JSONObject.class);
					List<JSONObject> jsonObjects3 = aggregate3.getMappedResults();

					TableRowVO row2 = new TableRowVO();
					row2.addLabel(new LabelDataVO("sub_pol_code", subPolicy.getSubPolCode()));
					row2.addLabel(new LabelDataVO("sub_pol_name", subPolicy.getSubPolName()));

					if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
						row2.addLabel(new LabelDataVO("sub_pol_face_amnt",
								decimalFormat.format(jsonObjects3.get(0).getDouble("sumFaceAmnt"))));
						// Double addPremSinglePol =
						// setAddPremSingleSubPol(grpInsurAppl,
						// subPolicy.getSubPolCode());
						row2.addLabel(new LabelDataVO("sub_pol_std_prem ",
								decimalFormat.format(jsonObjects3.get(0).getDouble("sumPremium"))));
					} else {
						row2.addLabel(
								new LabelDataVO("sub_pol_face_amnt", decimalFormat.format(subPolicy.getSubPolAmnt())));
						row2.addLabel(new LabelDataVO("sub_pol_std_prem ",
								decimalFormat.format(subPolicy.getSubStdPremium())));
					}

					son2.addTableRow(row2);
				}
			}
		}

		// receipt_list
		TableListVO tableList4 = new TableListVO();
		tableList4.setTableName("receipt_list"); // 团单收据表格

		List<Policy> policyList2 = grpInsurAppl.getApplState().getPolicyList();
		for (Policy policy : policyList2) {
			Aggregation aggregation2 = Aggregation
					.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
							Aggregation.unwind("subStateList"),
							Aggregation.match(
									Criteria.where("subStateList.polCode").regex(policy.getPolCode().substring(0, 3))),
							Aggregation.group("$applNo").sum("subStateList.sumPremium").as("sumPremium"));
			AggregationResults<JSONObject> aggregate2 = mongoTemplate.aggregate(aggregation2, "grpInsured",
					JSONObject.class);
			List<JSONObject> jsonObjects2 = aggregate2.getMappedResults();

			TableSonVO son4 = new TableSonVO();
			tableList4.addTableSon(son4);
			TableRowVO row4 = new TableRowVO();

			row4.addLabel(new LabelDataVO("cgno", grpInsurAppl.getCgNo()));
			row4.addLabel(new LabelDataVO("pol_name", policy.getPolNameChn()));
			row4.addLabel(new LabelDataVO("moneyin_itrvl",
					MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription())); // 缴费方式

			row4.addLabel(new LabelDataVO("payments_no", "-")); // 已缴费期数
			row4.addLabel(new LabelDataVO("payments_items", "款项")); // 缴费项目
			row4.addLabel(new LabelDataVO("moneyin_dur", // 缴费期间
					grpInsurAppl.getPaymentInfo().getMoneyinDur().toString()
							+ DUR_UNIT.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit()).getDescription()));

			String payments_dates = "";
			if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinType(), MONEYIN_ITRVL.SINGLE_PAY.getKey())) {
				payments_dates = DateFormatUtils.format(grpInsurAppl.getInForceDate(),
						PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD) + "-"
						+ DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD);
			} else {
				String sqlPlnmioDate = "select to_char(min(plnmio_date)-1, 'yyyy/mm/dd') from plnmio_rec "
						+ "where cg_no=? and mtn_item_code=29 and mio_item_code='PS'";
				String plnmioDate = jdbcTemplate.queryForObject(sqlPlnmioDate, String.class, grpInsurAppl.getCgNo());
				if (StringUtils.isEmpty(plnmioDate)) {
					plnmioDate = DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(),
							PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD);
				}
				payments_dates = DateFormatUtils.format(grpInsurAppl.getInForceDate(),
						PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD) + "-" + plnmioDate;
			}
			row4.addLabel(new LabelDataVO("payments_dates", payments_dates)); // 本次缴费期间起止日

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				Double addPremSinglePol = setAddPremSinglePol(grpInsurAppl, policy.getPolCode());
				row4.addLabel(new LabelDataVO("pol_prem",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumPremium") + addPremSinglePol)));
			} else {
				row4.addLabel(new LabelDataVO("pol_prem", decimalFormat.format(policy.getStdPremium())));
			}

			son4.addTableRow(row4);
		}

		printData.addTableList(tableList1);
		printData.addTableList(tableList2);
		printData.addTableList(tableList4);
		ePrintVO.setPrintData(printData);
		PrintVO print = new PrintVO();
		print.setePrint(ePrintVO);

		System.out.println("json" + JSON.toJSONString(print));

		String result = "";
		CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo2);
		result = receiveReqServiceClient.excute(print);
		LOGGER.info("result" + result);
		// System.out.println("result" + result);
	}

	// 清单打印
	@Override
	public void cntrLstPrint(String applNo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("applNo", applNo);

		// 根据投保单号获取团单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, param);
		if (grpInsurAppl == null) {
			LOGGER.error("保单基本信息不存在: " + applNo);
			throw new BusinessException("0016", applNo);
		}

		//新增业务逻辑开始
		doOldPrintTask(grpInsurAppl);
		//新增业务逻辑结束
		
		// 根据投保单号获取操作轨迹信息
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
				param);
		String applChkClerkName = "";
		String applEntClerkName = "";
		String cntrFoundDate = "";
		String cntrCreateDate = ""; // 合同生成日期
		String premConfirmDate = ""; // 保费确认日期"
		if (null != insurApplOperTrace) {
			for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {
				// 获取复核人员
				if (isReview(traceNode)) {
					applChkClerkName = traceNode.getPclkName();
				}
				// 获取录入人员
				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_ENTRY.getKey())
						|| StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_ENTRY.getKey())) {
					applEntClerkName = traceNode.getPclkName();
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.UNDERWRITING.getKey())) {
					cntrFoundDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDD);
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.POLICY_INFORCE.getKey())) {
					cntrCreateDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDDHHMM);
				}

				if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PAY_CHECK.getKey())) {
					premConfirmDate = DateFormatUtils.format(traceNode.getProcDate(), PrintVO.DATE_FORMAT_YYYYMMDDHHMM);
				}
			}
		}

		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 格式化设置

		EPrintVO ePrintVO = new EPrintVO();
		// regInfoData
		CommonData common = new CommonData();
		common.getRegInfoData().setItfid("110");
		common.getRegInfoData().setTplcode("TPL00255");
		common.getRegInfoData().setTplversion("1.0");
		common.getRegInfoData().setParamtype("3");

		// indexData
		common.getIndexData().setIndexno(grpInsurAppl.getSgNo());
		common.getIndexData().setApplno(grpInsurAppl.getApplNo());
		common.getIndexData().setCgno(grpInsurAppl.getCgNo());
		common.getIndexData().setPrdname(grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn());
		common.getIndexData().setPrdcode(grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());

		// pubData
		common.getPubData().setOrganization(grpInsurAppl.getProvBranchNo());
		if (grpInsurAppl.getSgType().equals("P")) {
			common.getPubData().setPhdrcustno(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
			common.getPubData().setPhdrname(grpInsurAppl.getPsnListHolderInfo().getSgName());
			common.getPubData()
					.setPhdrsex(SEX.valueOfKey(grpInsurAppl.getPsnListHolderInfo().getSgSex()).getDescription());
			common.getPubData().setPhdremail(grpInsurAppl.getPsnListHolderInfo().getSgEmail());
			if (grpInsurAppl.getPsnListHolderInfo().getSgMobile() != null
					&& !grpInsurAppl.getPsnListHolderInfo().getSgMobile().equals("")) {
				common.getPubData().setPhdrhomephone(grpInsurAppl.getPsnListHolderInfo().getSgMobile());
			} else {
				common.getPubData().setPhdrhomephone(grpInsurAppl.getPsnListHolderInfo().getSgTelephone());
			}
		} else {
			common.getPubData().setPhdrcustno(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
			common.getPubData().setPhdrname(grpInsurAppl.getGrpHolderInfo().getContactName());
			common.getPubData().setPhdremail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
			if (grpInsurAppl.getGrpHolderInfo().getContactMobile() != null
					&& !grpInsurAppl.getGrpHolderInfo().getContactMobile().equals("")) {
				common.getPubData().setPhdrhomephone(grpInsurAppl.getGrpHolderInfo().getContactMobile());
			} else {
				common.getPubData().setPhdrhomephone(grpInsurAppl.getGrpHolderInfo().getContactTelephone());
			}
		}

		common.getPubData().setManagebranch(grpInsurAppl.getMgrBranchNo());
		
		// 获取销售员信息
		SalesInfo salesInfo = new SalesInfo();
		if (StringUtils.equals(grpInsurAppl.getSalesDevelopFlag(), YES_NO_FLAG.NO.getKey())) {
			salesInfo = grpInsurAppl.getSalesInfoList().get(0);
		} else if (StringUtils.equals(grpInsurAppl.getSalesDevelopFlag(), YES_NO_FLAG.YES.getKey())) {
			for (SalesInfo salesInfoTmp : grpInsurAppl.getSalesInfoList()) {
				if (StringUtils.equals(salesInfoTmp.getDevelMainFlag(), DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey())) {
					salesInfo = salesInfoTmp;
				}
			}
		}

		common.getPubData().setSaleschannel(salesInfo.getSalesChannel());
		common.getPubData().setSyssource("4");
		common.getPubData().setBillclass("012");
		common.getPubData().setCgno(grpInsurAppl.getCgNo());
		common.getPubData().setSgno(grpInsurAppl.getSgNo());
		
		CxfHeader headerInfoBranch = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfoBranch);
		Branch branch = branchService.findOne(grpInsurAppl.getProvBranchNo());
//		System.out.println("test" + branch.getPrintBranchName());
		common.getPubData().setSignbranch(branch.getPrintBranchName());
		
		ePrintVO.setCommonDatas(common.toPrintData());

		// printData
		PrintDataVO printData = new PrintDataVO();

		// StringBuilder sb = new StringBuilder(grpInsurAppl.getCgNo());
		// sb.insert(4, "-");
		// sb.insert(11, "-");
		// sb.insert(15, "-");
		// sb.insert(24, "-");
		// printData.addLabelData(new LabelDataVO("cggno", sb.toString()));

		if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo())) {
			printData.addLabelData(new LabelDataVO("salesNoAdd", salesInfo.getSalesDeptNo()));
		} else {
			printData.addLabelData(new LabelDataVO("salesNoAdd", ""));
		}

		Double sum_prem = 0.00;
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
				Aggregation.unwind("subStateList"), Aggregation.group("$applNo").sum("subStateList.faceAmnt")
						.as("sumFaceAmnt").sum("subStateList.sumPremium").as("sumPremium"));
		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();

		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			if (jsonObjects.size() > 0) {
				Double addPrem = setAddPremAllPol(grpInsurAppl);
				sum_prem = jsonObjects.get(0).getDouble("sumPremium");
				printData.addLabelData(new LabelDataVO("sum_prem",
						decimalFormat.format(jsonObjects.get(0).getDouble("sumPremium") + addPrem)));
				printData.addLabelData(new LabelDataVO("face_amnt",
						decimalFormat.format(jsonObjects.get(0).getDouble("sumFaceAmnt"))));
			} else {
				printData.addLabelData(new LabelDataVO("sum_prem", "0.00"));
				printData.addLabelData(new LabelDataVO("face_amnt", "0.00"));
			}

		} else {
			sum_prem = grpInsurAppl.getApplState().getSumPremium();
			printData.addLabelData(
					new LabelDataVO("sum_prem", decimalFormat.format(grpInsurAppl.getApplState().getSumPremium())));
			printData.addLabelData(
					new LabelDataVO("face_amnt", decimalFormat.format(grpInsurAppl.getApplState().getSumFaceAmnt())));
		}

		printData.addLabelData(new LabelDataVO("temporary_pay_need", grpInsurAppl.getPaymentInfo().getIsMultiPay())); // 是否多期暂交
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getIsMultiPay(), YES_NO_FLAG.YES.getKey())) {
			printData.addLabelData(
					new LabelDataVO("temporary_pay_years", grpInsurAppl.getPaymentInfo().getMutipayTimes().toString())); // 多期暂交年数

			String selectAmntSql = "SELECT amnt FROM plnmio_rec WHERE CNTR_NO = ? ";
			Double damnt = null;
			try
			{
				damnt = jdbcTemplate.queryForObject(selectAmntSql, Double.class, applNo);
			}
			catch(Exception e)
			{
				System.out.println("");
				throw new BusinessException("没有应收记录");
			}

			printData.addLabelData(new LabelDataVO("temporary_pay_Total", decimalFormat.format(damnt - sum_prem))); // 多期暂交合计
			
			DataAdapter dataAdapter = new DataAdapter();
			PmsDataInfo pmsDataInfo = new PmsDataInfo();
			SgMultiPayInfo sgMultiPayInfo = new SgMultiPayInfo();
			
			dataAdapter.setApplNo(grpInsurAppl.getApplNo());
			dataAdapter.setCgNo(grpInsurAppl.getCgNo());
			dataAdapter.setUpdateTimestamp(new Date());
			sgMultiPayInfo.setMultiPayTimes(grpInsurAppl.getPaymentInfo().getMutipayTimes());
			sgMultiPayInfo.setMultiPayPrem(damnt - sum_prem);
			sgMultiPayInfo.setCntrExpiryDate(DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_MMDD));
			sgMultiPayInfo.setRenewTimes(grpInsurAppl.getPaymentInfo().getMutipayTimes());
			pmsDataInfo.setSgMultiPayInfo(sgMultiPayInfo);
			dataAdapter.setPmsDataInfo(pmsDataInfo);
			
			Query query = new Query();
			Criteria cri = new Criteria();
			cri.and("applNo").is(grpInsurAppl.getApplNo());
			query.addCriteria(cri);
			if(mongoTemplate.exists(query, DataAdapter.class))
			{
				mongoTemplate.remove(query, DataAdapter.class);
			}
			mongoTemplate.insert(dataAdapter);
		} else {
			printData.addLabelData(new LabelDataVO("temporary_pay_years", ""));
			printData.addLabelData(new LabelDataVO("temporary_pay_Total", "")); // 多期暂交合计
		}

		printData.addLabelData(new LabelDataVO("temporary_pay_data",
				DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_MMDD))); // 有期限截止月日

		if (getAmorFlag(grpInsurAppl.getApplState().getInsurDurUnit(), grpInsurAppl.getApplState().getInsurDur(),
				grpInsurAppl.getPaymentInfo().getMoneyinItrvl()) > 1) {
			printData.addLabelData(new LabelDataVO("installment_need", "Y")); // 是否分期交费
		} else {
			printData.addLabelData(new LabelDataVO("installment_need", "N")); // 是否分期交费
		}

		printData.addLabelData(new LabelDataVO("moneyin_itrvl",
				MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription())); // 缴费方式
		
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		
		Map<String , Object> map = new HashMap<>();
		map.put("polCode", grpInsurAppl.getApplState().getPolicyList().get(0).getPolCode());

		Map<String , Object> resultMap=policyBasicService.excute(map);
		JSONObject json = (JSONObject) JSONObject.toJSON(resultMap.get("policyBasicBo"));
		//需要相加的数值
		String premExtAmnt =json.getString("premExtAmnt"); 
		//保费宽限期单位  Y-年 M-月 D-天
		String premExtUnitDesc = json.getString("premExtUnitDesc");
	
		printData.addLabelData(new LabelDataVO("moneyin_itrvl_date", premExtAmnt + premExtUnitDesc)); // 交费宽限期

		// 分期缴费
		if(getAmorFlag(grpInsurAppl.getApplState().getInsurDurUnit(), grpInsurAppl.getApplState().getInsurDur(),
				grpInsurAppl.getPaymentInfo().getMoneyinItrvl()) > 1){
			DataAdapter dataAdapter = new DataAdapter();
			PmsDataInfo pmsDataInfo = new PmsDataInfo();
			SgInstallmentInfo sgInstallmentInfo = new SgInstallmentInfo();
			
			dataAdapter.setApplNo(grpInsurAppl.getApplNo());
			dataAdapter.setCgNo(grpInsurAppl.getCgNo());
			dataAdapter.setUpdateTimestamp(new Date());
			pmsDataInfo.setSgInstallmentInfo(sgInstallmentInfo);
			sgInstallmentInfo.setMoneyinItrvlDesc(MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription());
			sgInstallmentInfo.setPremExtAmnt(Integer.parseInt(premExtAmnt));
			sgInstallmentInfo.setPremExtUnitDesc(premExtUnitDesc);
			dataAdapter.setPmsDataInfo(pmsDataInfo);
			
			Query query = new Query();
			Criteria cri = new Criteria();
			cri.and("applNo").is(grpInsurAppl.getApplNo());
			query.addCriteria(cri);
			if(mongoTemplate.exists(query, DataAdapter.class))
			{
				mongoTemplate.remove(query, DataAdapter.class);
			}
			mongoTemplate.insert(dataAdapter);
		}
		
		if (StringUtils.equals(LIST_TYPE.GRP_SG.getKey(), grpInsurAppl.getSgType())) {
			printData.addLabelData(new LabelDataVO("sname", grpInsurAppl.getGrpHolderInfo().getGrpName()));
		} else {
			printData.addLabelData(new LabelDataVO("sname", grpInsurAppl.getPsnListHolderInfo().getSgName()));
		}

		printData.addLabelData(new LabelDataVO("cntrtype", grpInsurAppl.getCntrType()));
		printData.addLabelData(new LabelDataVO("sg_appl_no", grpInsurAppl.getApplNo()));
		printData.addLabelData(new LabelDataVO("sg_no", grpInsurAppl.getSgNo()));

		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			long ipsnNum = mongoTemplate.count(Query.query(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
					GrpInsured.class);
			printData.addLabelData(new LabelDataVO("ipsn_count", Long.toString(ipsnNum)));
		} else {
			printData.addLabelData(new LabelDataVO("ipsn_count", grpInsurAppl.getApplState().getIpsnNum().toString()));
		}

		printData.addLabelData(new LabelDataVO("insur_dur", grpInsurAppl.getApplState().getInsurDur().toString()
				+ DUR_UNIT.valueOfKey(grpInsurAppl.getApplState().getInsurDurUnit()).getDescription()));
		if (null != grpInsurAppl.getInForceDate()) // 生效日期
		{
			printData.addLabelData(new LabelDataVO("in_force_date",
					DateFormatUtils.format(grpInsurAppl.getInForceDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}
		if (null != grpInsurAppl.getCntrExpiryDate()) // 生效至日期
		{
			printData.addLabelData(new LabelDataVO("cntr_term_date",
					DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}
		printData.addLabelData(new LabelDataVO("currency",
				CURRENCY_CODE.valueOfKey(grpInsurAppl.getPaymentInfo().getCurrencyCode()).getDescription() + "元"));

		printData.addLabelData(new LabelDataVO("agentname", salesInfo.getSalesName()));
		printData.addLabelData(new LabelDataVO("appl_chk_clerk_name", applChkClerkName));
		printData.addLabelData(new LabelDataVO("appl_ent_clerk_name", applEntClerkName));

		if (StringUtils.equals(LIST_TYPE.GRP_SG.getKey(), grpInsurAppl.getSgType())) {
			printData.addLabelData(new LabelDataVO("phdr_name", grpInsurAppl.getGrpHolderInfo().getGrpName()));
		} else {
			printData.addLabelData(new LabelDataVO("phdr_name", grpInsurAppl.getPsnListHolderInfo().getSgName()));
		}
		printData.addLabelData(new LabelDataVO("agent_branch_code", salesInfo.getSalesBranchNo()));
		printData.addLabelData(new LabelDataVO("agent_branch_name", salesInfo.getSalesBranchName()));
		printData.addLabelData(new LabelDataVO("agentno", salesInfo.getSalesNo()));
		printData.addLabelData(new LabelDataVO("sign_addr", branch.getAddress()));
		printData.addLabelData(new LabelDataVO("cntr_create_date", cntrCreateDate)); // 合同生成日期
		printData.addLabelData(new LabelDataVO("prem_confirm_date", premConfirmDate)); // 保费确认日期
		printData.addLabelData(new LabelDataVO("cgno", grpInsurAppl.getCgNo()));
		printData.addLabelData(new LabelDataVO("policyflg", grpInsurAppl.getVoucherPrintType())); // 个人凭证类型
		printData.addLabelData(new LabelDataVO("cntr_establish_date", cntrFoundDate)); // 合同成立日期
		printData.addLabelData(new LabelDataVO("cntrPrintType", grpInsurAppl.getCntrPrintType())); // 合同打印方式

		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			printData.addLabelData(new LabelDataVO("listPrintType", grpInsurAppl.getListPrintType())); // 清单打印方式
			printData.addLabelData(new LabelDataVO("voucherPrintType", grpInsurAppl.getVoucherPrintType())); // 个人凭证类型
		} else {
			printData.addLabelData(new LabelDataVO("listPrintType", "n")); // 清单打印方式
			printData.addLabelData(new LabelDataVO("voucherPrintType", "n")); // 个人凭证类型
		}

		if (null != grpInsurAppl.getSignDate()) {
			printData.addLabelData(new LabelDataVO("sign_date",
					DateFormatUtils.format(grpInsurAppl.getSignDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		if (!StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())) {
			printData.addLabelData(new LabelDataVO("moneyinType",
					MONEYIN_TYPE.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinType()).getDescription())); // 收款形式
		} else {
			printData.addLabelData(new LabelDataVO("moneyinType", "")); // 收款形式
		}

		if (ProcMioInfoUtils.isBankTrans(grpInsurAppl)) {
			String sqlMioDate = "select min(mio_date) from mio_log "
					+ "where instr(cntr_no, ?)>0 and mio_item_code='FA' "
					+ "and mio_class=1 and mio_type='T' and mio_tx_class=0";
			Date mioDate = jdbcTemplate.queryForObject(sqlMioDate, Date.class, applNo);
			if(mioDate != null)
			{
				printData.addLabelData(
						new LabelDataVO("first_pay_date", DateFormatUtils.format(mioDate, PrintVO.DATE_FORMAT_YYYYMMDD)));
			}
		} else {
			printData.addLabelData(new LabelDataVO("first_pay_date",
					DateFormatUtils.format(grpInsurAppl.getSignDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
		}

		printData.addLabelData(new LabelDataVO("operator_name", "")); // 经办人

		if (StringUtils.equals(LIST_TYPE.GRP_SG.getKey(), grpInsurAppl.getSgType()) && StringUtils
				.equals(grpInsurAppl.getGrpHolderInfo().getGrpIdType(), GRP_ID_TYPE.TRADE_LICENSE.getKey())) {
			printData.addLabelData(new LabelDataVO("grpIdNo", grpInsurAppl.getGrpHolderInfo().getGrpIdNo())); // 工商执照号
			printData.addLabelData(new LabelDataVO("taxpayerId", "grpInsurAppl.getGrpHolderInfo().getTaxpayerId())")); // 纳税人识别号
		} else {
			printData.addLabelData(new LabelDataVO("grpIdNo", ""));
			printData.addLabelData(new LabelDataVO("taxpayerId", "")); // 纳税人识别号

		}

		printData.addLabelData(new LabelDataVO("service_tel", "95519"));
		printData.addLabelData(new LabelDataVO("service_url", "http://www.e-chinalife.com")); // 公司网址
		printData.addLabelData(new LabelDataVO("cntr_no", grpInsurAppl.getCgNo())); // 保险合同号

		// convention_lst
		TableListVO tableList1 = new TableListVO();
		tableList1.setTableName("convention_lst");
		TableSonVO son1 = new TableSonVO();
		tableList1.addTableSon(son1);
		TableRowVO row1 = new TableRowVO();
		row1.addLabel(new LabelDataVO("inputConv", ""));
		if (null != grpInsurAppl.getConventions()) {
			row1.addLabel(new LabelDataVO("orSetConv", grpInsurAppl.getConventions().getOrSetConv()));
			row1.addLabel(new LabelDataVO("udwConv", grpInsurAppl.getConventions().getUdwConv()));
			row1.addLabel(new LabelDataVO("minorConv", grpInsurAppl.getConventions().getMinorConv()));
			row1.addLabel(new LabelDataVO("polConv", grpInsurAppl.getConventions().getPolConv()));
		}
		son1.addTableRow(row1);

		TableListVO tableList2 = new TableListVO();
		tableList2.setTableName("pol_lst");

		List<Policy> policyList = grpInsurAppl.getApplState().getPolicyList();
		for (Policy policy : policyList) {
			Aggregation aggregation2 = Aggregation
					.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
							Aggregation.unwind("subStateList"),
							Aggregation.match(
									Criteria.where("subStateList.polCode").regex(policy.getPolCode().substring(0, 3))),
							Aggregation.group("$applNo").sum("subStateList.faceAmnt").as("sumFaceAmnt")
									.sum("subStateList.sumPremium").as("sumPremium"));

			AggregationResults<JSONObject> aggregate2 = mongoTemplate.aggregate(aggregation2, "grpInsured",
					JSONObject.class);
			List<JSONObject> jsonObjects2 = aggregate2.getMappedResults();

			TableSonVO son2 = new TableSonVO();
			tableList2.addTableSon(son2);

			son2.addLabel(new LabelDataVO("pol_name", policy.getPolNameChn()));
			son2.addLabel(new LabelDataVO("pol_code", policy.getPolCode()));
			son2.addLabel(new LabelDataVO("pol_ipsn_num", policy.getPolIpsnNum().toString()));

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				son2.addLabel(new LabelDataVO("pol_face_amnt",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumFaceAmnt"))));
			} else {
				son2.addLabel(new LabelDataVO("pol_face_amnt", decimalFormat.format(policy.getFaceAmnt())));
			}

			son2.addLabel(new LabelDataVO("insur_dur",
					policy.getInsurDur().toString() + DUR_UNIT.valueOfKey(policy.getInsurDurUnit()).getDescription()));

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				Double addPremSinglePol = setAddPremSinglePol(grpInsurAppl, policy.getPolCode());
				son2.addLabel(new LabelDataVO("pol_std_prem",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumPremium") + addPremSinglePol)));
			} else {
				son2.addLabel(new LabelDataVO("pol_std_prem", decimalFormat.format(policy.getStdPremium())));
			}

			if (null != grpInsurAppl.getInForceDate()) // 生效日期
			{
				son2.addLabel(new LabelDataVO("in_force_date",
						DateFormatUtils.format(grpInsurAppl.getInForceDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
			} else {
				son2.addLabel(new LabelDataVO("in_force_date", ""));
			}
			if (null != grpInsurAppl.getCntrExpiryDate()) // 生效至日期
			{
				son2.addLabel(new LabelDataVO("cntr_term_date",
						DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_YYYYMMDD)));
			} else {
				son2.addLabel(new LabelDataVO("cntr_term_date", ""));
			}
		}

		// receipt_list
		TableListVO tableList3 = new TableListVO();
		tableList3.setTableName("receipt_list"); // 团单收据表格

		List<Policy> policyList2 = grpInsurAppl.getApplState().getPolicyList();
		for (Policy policy : policyList2) {
			Aggregation aggregation2 = Aggregation
					.newAggregation(Aggregation.match(Criteria.where("applNo").is(applNo).and("procStat").is("E")),
							Aggregation.unwind("subStateList"),
							Aggregation.match(
									Criteria.where("subStateList.polCode").regex(policy.getPolCode().substring(0, 3))),
							Aggregation.group("$applNo").sum("subStateList.sumPremium").as("sumPremium"));
			AggregationResults<JSONObject> aggregate2 = mongoTemplate.aggregate(aggregation2, "grpInsured",
					JSONObject.class);
			List<JSONObject> jsonObjects2 = aggregate2.getMappedResults();

			TableSonVO son3 = new TableSonVO();
			tableList3.addTableSon(son3);
			TableRowVO row3 = new TableRowVO();

			row3.addLabel(new LabelDataVO("cgno", grpInsurAppl.getCgNo()));
			row3.addLabel(new LabelDataVO("pol_name", policy.getPolNameChn()));
			row3.addLabel(new LabelDataVO("moneyin_itrvl",
					MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription())); // 缴费方式

			row3.addLabel(new LabelDataVO("payments_no", "")); // 已缴费期数
			row3.addLabel(new LabelDataVO("payments_items", "款项")); // 缴费项目
			row3.addLabel(new LabelDataVO("moneyin_dur", // 缴费期间
					grpInsurAppl.getPaymentInfo().getMoneyinDur().toString()
							+ DUR_UNIT.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit()).getDescription()));

			String payments_dates = "";
			if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getMoneyinType(), MONEYIN_ITRVL.SINGLE_PAY.getKey())) {
				payments_dates = DateFormatUtils.format(grpInsurAppl.getInForceDate(),
						PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD) + "-"
						+ DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(), PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD);
			} else {
				String sqlPlnmioDate = "select to_char(min(plnmio_date)-1, 'yyyy/mm/dd') from plnmio_rec "
						+ "where cg_no=? and mtn_item_code=29 and mio_item_code='PS'";
				String plnmioDate = jdbcTemplate.queryForObject(sqlPlnmioDate, String.class, grpInsurAppl.getCgNo());
				if (StringUtils.isEmpty(plnmioDate)) {
					plnmioDate = DateFormatUtils.format(grpInsurAppl.getCntrExpiryDate(),
							PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD);
				}
				payments_dates = DateFormatUtils.format(grpInsurAppl.getInForceDate(),
						PrintVO.DATE_FORMAT_SIMPLEYYYYMMDD) + "-" + plnmioDate;
			}
			row3.addLabel(new LabelDataVO("payments_dates", payments_dates)); // 本次缴费期间起止日

			if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				Double addPremSinglePol = setAddPremSinglePol(grpInsurAppl, policy.getPolCode());
				row3.addLabel(new LabelDataVO("pol_prem",
						decimalFormat.format(jsonObjects2.get(0).getDouble("sumPremium") + addPremSinglePol)));
			} else {
				row3.addLabel(new LabelDataVO("pol_prem", decimalFormat.format(policy.getStdPremium())));
			}

			son3.addTableRow(row3);
		}

		printData.addTableList(tableList1);
		printData.addTableList(tableList2);
		printData.addTableList(tableList3);
		ePrintVO.setPrintData(printData);
		PrintVO print = new PrintVO();
		print.setePrint(ePrintVO);

		System.out.println("json" + JSON.toJSONString(print));

		CxfHeader headerInfoPrint = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfoPrint);
		String result = receiveReqServiceClient.excute(print);
		LOGGER.info("result" + result);
	}

	private boolean isReview(TraceNode traceNode) {
		if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_REVIEW.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_REVIEW.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_CHECK_SUC.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_SUC.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_CHECK_FAL.getKey())) {
			return true;
		} else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_FAL.getKey())) {
			return true;
		} else {
			return false;
		}
	}

	private void setAddPrem(GrpInsurAppl grpInsurAppl) {

		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());
		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			return;
		}

		List<UdwPolResult> udwPolResults = applUdwResult.getUdwPolResults();

		if (null == udwPolResults || udwPolResults.isEmpty()) {
			return;
		}

		ApplState applState = grpInsurAppl.getApplState();

		for (UdwPolResult udwPolResult : udwPolResults) {
			for (Policy policy : applState.getPolicyList()) {
				if (StringUtils.equals(udwPolResult.getPolCode(), policy.getPolCode())) {
					policy.setPremium(policy.getPremium() + udwPolResult.getAddFeeAmnt());
					applState.setSumPremium(applState.getSumPremium() + udwPolResult.getAddFeeAmnt());
				}
			}
		}

		if (grpInsurAppl.getConventions() != null) {
			grpInsurAppl.getConventions().setUdwConv(applUdwResult.getUdwConv());
		}

	}

	private Double setAddPremAllPol(GrpInsurAppl grpInsurAppl) {
		Double addPremAllPol = 0.00;
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());
		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			return addPremAllPol;
		}

		List<UdwPolResult> udwPolResults = applUdwResult.getUdwPolResults();

		if (null == udwPolResults || udwPolResults.isEmpty()) {
			return addPremAllPol;
		}

		for (UdwPolResult udwPolResult : udwPolResults) {
			addPremAllPol += udwPolResult.getAddFeeAmnt();
		}

		return addPremAllPol;
	}

	private Double setAddPremSinglePol(GrpInsurAppl grpInsurAppl, String polCode) {
		Double addPremPol = 0.00;
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());
		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			return addPremPol;
		}

		List<UdwPolResult> udwPolResults = applUdwResult.getUdwPolResults();

		if (null == udwPolResults || udwPolResults.isEmpty()) {
			return addPremPol;
		}

		for (UdwPolResult udwPolResult : udwPolResults) {
			if (StringUtils.equals(udwPolResult.getPolCode(), polCode)) {
				addPremPol += udwPolResult.getAddFeeAmnt();
			}
		}

		return addPremPol;
	}

	// private Double setAddPremSingleSubPol(GrpInsurAppl grpInsurAppl, String
	// polCode) {
	// Double addPremPol = 0.00;
	// Map<String, Object> queryMap = new HashMap<>();
	// queryMap.put("businessKey", grpInsurAppl.getApplNo());
	// ApplUdwResult applUdwResult = (ApplUdwResult)
	// mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
	// if (null == applUdwResult) {
	// return addPremPol;
	// }
	//
	// List<UdwPolResult> udwPolResults = applUdwResult.getUdwPolResults();
	//
	// if (null == udwPolResults || udwPolResults.isEmpty()) {
	// return addPremPol;
	// }
	//
	// for (UdwPolResult udwPolResult : udwPolResults) {
	// if (StringUtils.equals(udwPolResult.getPolCode(), polCode)) {
	// addPremPol += udwPolResult.getAddFeeAmnt();
	// }
	// }
	//
	// return addPremPol;
	// }

	/**
	 * 判断是否无清单分期应收
	 *
	 * @param insurDurDnit
	 * @param insurDur
	 * @param moneyinItrvl
	 * @return
	 */
	public int getAmorFlag(String insurDurDnit, double insurDur, String moneyinItrvl) {
		double moneyinDur = 0.0;
		// 保险期间
		BigDecimal b1 = new BigDecimal(Double.toString(insurDur));
		// 1
		BigDecimal b2 = new BigDecimal(Double.toString(1));
		// 4
		BigDecimal b3 = new BigDecimal(Double.toString(4));
		// 6
		BigDecimal b4 = new BigDecimal(Double.toString(6));
		// 12
		BigDecimal b5 = new BigDecimal(Double.toString(12));
		// 保险期间乘以12
		double subtr = b1.multiply(b5).doubleValue();
		// 保险期间乘以12
		BigDecimal b6 = new BigDecimal(Double.toString(subtr));
		// 判断缴费方式
		if ("O".equals(moneyinItrvl) || "W".equals(moneyinItrvl) || "Y".equals(moneyinItrvl)
				|| "X".equals(moneyinItrvl)) {
			moneyinDur = 0.0;
		} else {// 保险期类型
			if ("M".equals(insurDurDnit)) {
				switch (moneyinItrvl)// 缴费方式
				{
				case "M":// 保险期间除以1
					moneyinDur = b1.divide(b2).doubleValue();
					break;
				case "Q":// 保险期间除以4
					moneyinDur = b1.divide(b3).doubleValue();
					break;
				case "H":// 保险期间除以6
					moneyinDur = b1.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
				}
			} // 保险期类型
			if ("Y".equals(insurDurDnit)) {
				switch (moneyinItrvl)// 缴费方式
				{
				case "M":// 保险期间乘以12 在除以1
					moneyinDur = b6.divide(b2).doubleValue();
					break;
				case "Q":// 保险期间乘以12 在除以4
					moneyinDur = b6.divide(b3).doubleValue();
					break;
				case "H":// 保险期间乘以12 在除以6
					moneyinDur = b6.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
					break;
				}
			} // end if
		} // end if
		return (int) moneyinDur;
	}
	
	
	/**
	 * 处理纸质打印业务
	 * @param grpInsurAppl
	 */
	public void doOldPrintTask(GrpInsurAppl grpInsurAppl){
		String applNo = grpInsurAppl.getApplNo();
		LOGGER.info("处理纸质打印业务开始:【"+applNo+"】");
		//合同打印方式   0：电子保单；1：纸质保单
		String cntrPrintType = grpInsurAppl.getCntrPrintType(); 
		if("1".equals(cntrPrintType)){
			//是否调用老打印系统开关:1-开   0-关
			String bizParamValue = bizParameterDao.findBizParam("OLD_PRINT_FLAG");
			if("1".equals(bizParamValue)){
				int rowid=oldPrintTaskQueueDao.findCountyApplNo(applNo);
				if(rowid==1){
					oldPrintTaskQueueDao.updateDateByApplNo(applNo, "N");
				}else if(rowid==0){
					//往打印任务控制表(old_print_task_queue)里面插入一条数据
					oldPrintTaskQueueDao.insertData("打印",applNo);
					LOGGER.info("打印任务控制表【OLD_PRINT_TASK_QUEUE】：入库完成 【"+applNo+"】");
				}
			}
		}
		LOGGER.info("处理纸质打印业务结束:【"+applNo+"】");
	}

}
