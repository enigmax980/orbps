package com.newcore.orbps.models.web.vo.sendgrpinsurappl;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

import com.alibaba.druid.util.StringUtils;

import com.newcore.orbps.models.service.bo.ApplState;
import com.newcore.orbps.models.service.bo.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.GcLppPremRateBo;
import com.newcore.orbps.models.service.bo.OperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.*;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.supports.dicts.CNTR_PRINT_TYPE;
import com.newcore.supports.dicts.IPSN_GRP_TYPE;
import com.newcore.supports.dicts.LIST_PRINT_TYPE;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.VOUCHER_PRINT_TYPE;


/**
 * 团体出单基本信息
 *
 * @author lijifei
 *         创建时间：2016年9月12日下午7:46:11
 */
public class GrpInsurApplSendVo implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -979107895361031118L;

	/* 字段名：投保单号，长度：16，是否必填：是 */
	private String applNo;

	/*
	 * 字段名：团单清单标志，长度：2，是否必填：是 L:普通清单，A：档案清单，M：事后补录，N：无清单
	 */
	private String lstProcType;

	/*
	 * 字段名：保单性质，长度：4， 是否必填：否 0：新单投保，1：续期投保; (默认为0)
	 */
	private String insurProperty;

	/*
	 * 字段名：团险方案审批号，长度：22，是否必填：否
	 */
	private String approNo;

	/*
	 * 字段名：接入来源，长度：2， 是否必填：否   系统英文缩写
	 */
	private String accessSource;

	/*
	 * 字段名：团体业务分类，长度：2， 是否必填：否 1.E门店方案；2：E企购方案；3：E支持_团单方案；4：e支持_汇交件方案；
	 */
	private String grpBusiType;

	/*
	 * 字段名：契约业务类型，长度：2，是否必填：否
	 * 1.员福计划（统谈统签）；2：员福计划（统谈分签）；3：员福计划（普通业务）；4：老年保险；5：农村小额保险；6：建工保险；7：学生保险；8：
	 * 政保保险；9：计生保险
	 */
	private String applBusiType;

	/*
	 * 字段名：合同打印方式,长度：2,是否必填：是 0：电子保单；1：纸质保单；（默认为0单）
	 */
	private String cntrPrintType;

	/*
	 * 字段名：清单打印方式,长度：2,是否必填：是 0：电子清单，1：纸质清单。（默认为0）
	 */
	private String listPrintType;

	/*
	 * 字段名：个人凭证类型，长度：2，是否必填：是 0：电子个人凭证；1：纸制个人凭证；
	 */
	private String voucherPrintType;

	/*
	 * 字段名：销售人员是否共同展业标识，长度：2，是否必填：否， 团单特有 N:否，Y：是。（默认为N）
	 */
	private String salesDevelopFlag;

	/*
	 * 字段名：契约形式，长度：2，是否必填：是 G：团单；L：清汇；
	 */
	private String cntrType;

	/*
	 * 字段名：保单送达方式，长度：2，是否必填：IF 客户线上自助 && insurType == 2 THEN 必填 1：客户门店自取，2：邮寄；
	 */
	private String cntrSendType;

	/*
	 * 字段名：外包录入标志，长度：2，是否必填：否 Y：是，N：否， （默认为N）
	 */
	private String entChannelFlag;

	/*
	 * 字段名：汇交人类型，长度：2，是否必填：C 清汇特有 1.个人汇交； 2.单位汇交
	 */
	private String sgType;

	/* 字段名：投保日期，长度：2，是否必填：是 */
	private Date applDate;

	/* 字段名：接入通道，长度：4，是否必填：是 */
	private String accessChannel;

	/*
	 * 字段名：争议处理方式，长度：2，是否必填：是 1:诉讼；2：仲裁
	 */
	private String argueType;

	/* 字段名：仲裁委员会名称，长度：64，是否必填：IF argueType == 2 THEN 必填 */
	private String arbitration;

	/* 字段名：上期保单号，长度：26，是否必填：否 */
	private String renewedCgNo;

	/* 字段名：共保协议号，长度：20，是否必填：否 */
	private String agreementNo;

	/*
	 * 字段名：赠送保险标志，长度：2，是否必填：否 N：否；Y：是。（默认为N）
	 */
	private String giftFlag;

	/*
	 * 字段名：是否异常告知，长度：2，是否必填：是N：否；Y：是。
	 */
	private String notificaStat;

	/*
	 * 字段名：合同组号，长度：25，是否必填：是 契约平台自己填写
	 */
	private String cgNo;

	/*
	 * 字段名：汇缴件号，长度：25，是否必填：否 清汇：applNo，团单：NULL
	 */
	private String sgNo;

	/*
	 * 字段名：签单日期，是否必填：是
	 */
	private Date signDate;

	/*
	 * 字段名：生效日期，是否必填：是
	 */
	private Date inForceDate;

	/*
	 * 字段名：续保次数，是否必填：否
	 */
	private Long renewTimes;

	/*
	 * 字段名：合同预计满期日期，是否必填：是
	 */
	private Date cntrExpiryDate;

	/*
	 * 字段名：第一主险，长度：8，是否必填：是    后台自填
	 */
	private String firPolCode;

	/*
	 * 字段名：人工核保标志，长度：2，是否必填：否
	 */
	private String udwType;

	/*
	 * 字段名：指定退保公式，长度：2，是否必填：否	S:公式法；N：按照保费比例；D：按照日公式计算；M：按照月公式计算
	 */
	private String srndAmntCmptType;

	/*
	 * 字段名：退保手续费比例，是否必填：否
	 */
	private Double srdFeeRate;

	/* 字段名：省级机构号，长度：6，是否必填：否 */
	private String provBranchNo;

	//	/* 字段名：管理机构号，长度：6，是否必填：是 */
	//	private String mgrBranchNo;
	//
	//	/* 字段名：归档机构号，长度：6，是否必填：否*/
	//	private String arcBranchNo;

	// 个人汇交人信息，清汇特有
	private PsnListHolderInfo psnListHolderInfo;

	// 团体客户信息
	private GrpHolderInfoVo grpHolderInfo;

	// 销售信息
	private List<SalesInfo> salesInfoList;

	// 缴费相关
	private PaymentInfo paymentInfo;

	// 投保要约
	private ApplState applState;

	// 要约分组
	private List<IpsnStateGrp> ipsnStateGrpList;

	// 团体收费组
	private List<IpsnPayGrpVo> ipsnPayGrpList;

	// 组织架构树 团单特有
	private List<OrgTreeVo> orgTreeList;

	// 建工险 团单特有
	private ConstructInsurInfo constructInsurInfo;

	// 健康险 团单特有
	private HealthInsurInfoVo healthInsurInfo;

	// 基金险 团单特有
	private FundInsurInfo fundInsurInfo;

	//特约
	private Conventions conventions;
	//任务流信息

	private TaskInfo taskInfo;

	// 操作轨迹
	private OperTrace operTrace;

	//团体保单一次性缴费多期缴费标准
	private List<GcLppPremRateBo> gcLppPremRateBoList;

	//应收付数据
	private List<PlnmioRecVo> plnmioRecList;


	/*
	 * 字段名：被保人清单打印方式,长度：2,是否必填：是 L 只打清单，V 打印清单凭证，N 只打印合同，P 只打印凭证，H 打印家庭险凭证，F 不打印凭证
	 */
	private String prtIpsnlstType;



	public GrpHolderInfoVo getGrpHolderInfo() {
		return grpHolderInfo;
	}


	public void setGrpHolderInfo(GrpHolderInfoVo grpHolderInfo) {
		this.grpHolderInfo = grpHolderInfo;
	}


	public List<IpsnPayGrpVo> getIpsnPayGrpList() {
		return ipsnPayGrpList;
	}


	public void setIpsnPayGrpList(List<IpsnPayGrpVo> ipsnPayGrpList) {
		this.ipsnPayGrpList = ipsnPayGrpList;
	}


	public List<OrgTreeVo> getOrgTreeList() {
		return orgTreeList;
	}


	public void setOrgTreeList(List<OrgTreeVo> orgTreeList) {
		this.orgTreeList = orgTreeList;
	}


	public String getSgType() {
		return sgType;
	}


	public void setSgType(String sgType) {
		this.sgType = sgType;
	}


	public Double getSrdFeeRate() {
		return srdFeeRate;
	}


	public void setSrdFeeRate(Double srdFeeRate) {
		this.srdFeeRate = srdFeeRate;
	}


	public String getProvBranchNo() {
		return provBranchNo;
	}


	public void setProvBranchNo(String provBranchNo) {
		this.provBranchNo = provBranchNo;
	}


	public TaskInfo getTaskInfo() {
		return taskInfo;
	}


	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}


	public String getApplNo() {
		return applNo;
	}


	public OperTrace getOperTrace() {
		return operTrace;
	}

	public void setOperTrace(OperTrace operTrace) {
		this.operTrace = operTrace;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getLstProcType() {
		return lstProcType;
	}

	public void setLstProcType(String lstProcType) {
		this.lstProcType = lstProcType;
	}

	public String getInsurProperty() {
		return insurProperty;
	}

	public void setInsurProperty(String insurProperty) {
		this.insurProperty = insurProperty;
	}

	public String getApproNo() {
		return approNo;
	}

	public void setApproNo(String approNo) {
		this.approNo = approNo;
	}

	public String getAccessSource() {
		return accessSource;
	}

	public void setAccessSource(String accessSource) {
		this.accessSource = accessSource;
	}

	public String getGrpBusiType() {
		return grpBusiType;
	}

	public void setGrpBusiType(String grpBusiType) {
		this.grpBusiType = grpBusiType;
	}

	public String getApplBusiType() {
		return applBusiType;
	}

	public void setApplBusiType(String applBusiType) {
		this.applBusiType = applBusiType;
	}

	public String getCntrPrintType() {
		return cntrPrintType;
	}

	public void setCntrPrintType(String cntrPrintType) {
		this.cntrPrintType = cntrPrintType;
	}

	public String getListPrintType() {
		return listPrintType;
	}

	public void setListPrintType(String listPrintType) {
		this.listPrintType = listPrintType;
	}

	public String getVoucherPrintType() {
		return voucherPrintType;
	}

	public void setVoucherPrintType(String voucherPrintType) {
		this.voucherPrintType = voucherPrintType;
	}

	public String getPrtIpsnlstType() {
		return prtIpsnlstType;
	}

	public void setPrtIpsnlstType(String prtIpsnlstType) {
		this.prtIpsnlstType = prtIpsnlstType;
	}

	public String getSalesDevelopFlag() {
		return salesDevelopFlag;
	}

	public void setSalesDevelopFlag(String salesDevelopFlag) {
		this.salesDevelopFlag = salesDevelopFlag;
	}

	public String getCntrType() {
		return cntrType;
	}

	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public HealthInsurInfoVo getHealthInsurInfo() {
		return healthInsurInfo;
	}


	public void setHealthInsurInfo(HealthInsurInfoVo healthInsurInfo) {
		this.healthInsurInfo = healthInsurInfo;
	}

	public String getCntrSendType() {
		return cntrSendType;
	}

	public void setCntrSendType(String cntrSendType) {
		this.cntrSendType = cntrSendType;
	}

	public String getEntChannelFlag() {
		return entChannelFlag;
	}

	public void setEntChannelFlag(String entChannelFlag) {
		this.entChannelFlag = entChannelFlag;
	}

	public Date getApplDate() {
		return applDate;
	}

	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}

	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}

	public String getArgueType() {
		return argueType;
	}

	public void setArgueType(String argueType) {
		this.argueType = argueType;
	}

	public String getArbitration() {
		return arbitration;
	}

	public void setArbitration(String arbitration) {
		this.arbitration = arbitration;
	}

	public String getRenewedCgNo() {
		return renewedCgNo;
	}

	public void setRenewedCgNo(String renewedCgNo) {
		this.renewedCgNo = renewedCgNo;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public String getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(String giftFlag) {
		this.giftFlag = giftFlag;
	}

	public String getNotificaStat() {
		return notificaStat;
	}

	public void setNotificaStat(String notificaStat) {
		this.notificaStat = notificaStat;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getSgNo() {
		return sgNo;
	}

	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getInForceDate() {
		return inForceDate;
	}

	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}

	public Long getRenewTimes() {
		return renewTimes;
	}

	public void setRenewTimes(Long renewTimes) {
		this.renewTimes = renewTimes;
	}

	public Date getCntrExpiryDate() {
		return cntrExpiryDate;
	}

	public void setCntrExpiryDate(Date cntrExpiryDate) {
		this.cntrExpiryDate = cntrExpiryDate;
	}

	public String getFirPolCode() {
		return firPolCode;
	}

	public void setFirPolCode(String firPolCode) {
		this.firPolCode = firPolCode;
	}

	public String getUdwType() {
		return udwType;
	}

	public void setUdwType(String udwType) {
		this.udwType = udwType;
	}

	public String getSrndAmntCmptType() {
		return srndAmntCmptType;
	}

	public void setSrndAmntCmptType(String srndAmntCmptType) {
		this.srndAmntCmptType = srndAmntCmptType;
	}

	public PsnListHolderInfo getPsnListHolderInfo() {
		return psnListHolderInfo;
	}

	public void setPsnListHolderInfo(PsnListHolderInfo psnListHolderInfo) {
		this.psnListHolderInfo = psnListHolderInfo;
	}


	public List<SalesInfo> getSalesInfoList() {
		return salesInfoList;
	}

	public void setSalesInfoList(List<SalesInfo> salesInfoList) {
		this.salesInfoList = salesInfoList;
	}

	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public ApplState getApplState() {
		return applState;
	}

	public void setApplState(ApplState applState) {
		this.applState = applState;
	}

	public List<IpsnStateGrp> getIpsnStateGrpList() {
		return ipsnStateGrpList;
	}

	public void setIpsnStateGrpList(List<IpsnStateGrp> ipsnStateGrpList) {
		this.ipsnStateGrpList = ipsnStateGrpList;
	}


	public ConstructInsurInfo getConstructInsurInfo() {
		return constructInsurInfo;
	}

	public void setConstructInsurInfo(ConstructInsurInfo constructInsurInfo) {
		this.constructInsurInfo = constructInsurInfo;
	}

	public FundInsurInfo getFundInsurInfo() {
		return fundInsurInfo;
	}

	public void setFundInsurInfo(FundInsurInfo fundInsurInfo) {
		this.fundInsurInfo = fundInsurInfo;
	}

	public Conventions getConventions() {
		return conventions;
	}

	public void setConventions(Conventions conventions) {
		this.conventions = conventions;
	}

	public List<GcLppPremRateBo> getGcLppPremRateBoList() {
		return gcLppPremRateBoList;
	}

	public void setGcLppPremRateBoList(List<GcLppPremRateBo> gcLppPremRateBoList) {
		this.gcLppPremRateBoList = gcLppPremRateBoList;
	}

	public List<PlnmioRecVo> getPlnmioRecList() {
		return plnmioRecList;
	}

	public void setPlnmioRecList(List<PlnmioRecVo> plnmioRecList) {
		this.plnmioRecList = plnmioRecList;
	}

	public GrpInsurApplSendVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void transGrpInsurBoToVo(GrpInsurAppl grpInsurAppl) {
		this.applState = new ApplState();

		this.setApplNo(grpInsurAppl.getApplNo());
		this.setLstProcType(grpInsurAppl.getLstProcType());
		this.setInsurProperty(grpInsurAppl.getInsurProperty());
		this.setApproNo(grpInsurAppl.getApproNo());
		this.setAccessSource(grpInsurAppl.getAccessSource());
		this.setGrpBusiType(grpInsurAppl.getGrpBusiType());
		this.setApplBusiType(grpInsurAppl.getApplBusiType());
		this.setCntrPrintType(grpInsurAppl.getCntrPrintType());
		this.setListPrintType(grpInsurAppl.getListPrintType());
		this.setVoucherPrintType(grpInsurAppl.getVoucherPrintType());
		//打印方式转换
		if (StringUtils.equals(CNTR_PRINT_TYPE.ELEC_INSUR.getKey(), this.cntrPrintType)
				&& StringUtils.equals(LIST_PRINT_TYPE.PAPER_LIST.getKey(), this.listPrintType)
				&& StringUtils.equals(VOUCHER_PRINT_TYPE.ELEC_CREDENTIALS.getKey(), this.voucherPrintType)) {

			this.setPrtIpsnlstType("L");
		} else if (StringUtils.equals(CNTR_PRINT_TYPE.PAPER_INSUR.getKey(), this.cntrPrintType)
				&& StringUtils.equals(LIST_PRINT_TYPE.ELEC_LIST.getKey(), this.listPrintType)
				&& StringUtils.equals(VOUCHER_PRINT_TYPE.ELEC_CREDENTIALS.getKey(), this.voucherPrintType)) {

			this.setPrtIpsnlstType("N");
		} else if (StringUtils.equals(CNTR_PRINT_TYPE.ELEC_INSUR.getKey(), this.cntrPrintType)
				&& StringUtils.equals(LIST_PRINT_TYPE.ELEC_LIST.getKey(), this.listPrintType)
				&& StringUtils.equals(VOUCHER_PRINT_TYPE.PAPER_CREDENTIALS.getKey(), this.voucherPrintType)) {

			this.setPrtIpsnlstType("P");
		} else if (StringUtils.equals(CNTR_PRINT_TYPE.ELEC_INSUR.getKey(), this.cntrPrintType)
				&& StringUtils.equals(LIST_PRINT_TYPE.ELEC_LIST.getKey(), this.listPrintType)
				&& StringUtils.equals(VOUCHER_PRINT_TYPE.FAMILY_ELEC_CREDENTIALS.getKey(), this.voucherPrintType)) {

			this.setPrtIpsnlstType("H");
		} else {

			this.setPrtIpsnlstType("F");
		}
		this.setSalesDevelopFlag(grpInsurAppl.getSalesDevelopFlag());
		this.setCntrType(grpInsurAppl.getCntrType());
		this.setCntrSendType(grpInsurAppl.getCntrSendType());
		this.setEntChannelFlag(grpInsurAppl.getEntChannelFlag());
		this.setSgType(grpInsurAppl.getSgType());
		this.setApplDate(grpInsurAppl.getApplDate());
		this.setAccessChannel(grpInsurAppl.getAccessChannel());
		this.setArgueType(grpInsurAppl.getArgueType());
		this.setArbitration(grpInsurAppl.getArbitration());
		this.setRenewedCgNo(grpInsurAppl.getRenewedCgNo());
		this.setAgreementNo(grpInsurAppl.getAgreementNo());
		this.setGiftFlag(grpInsurAppl.getGiftFlag());
		this.setNotificaStat(grpInsurAppl.getNotificaStat());
		this.setCgNo(grpInsurAppl.getCgNo());
		this.setSgNo(grpInsurAppl.getSgNo());
		this.setSignDate(grpInsurAppl.getSignDate());
		this.setInForceDate(grpInsurAppl.getInForceDate());
		this.setRenewTimes(grpInsurAppl.getRenewTimes());
		this.setCntrExpiryDate(grpInsurAppl.getCntrExpiryDate());
		this.setFirPolCode(grpInsurAppl.getFirPolCode());
		this.setUdwType(grpInsurAppl.getUdwType());
		this.setSrndAmntCmptType(grpInsurAppl.getSrndAmntCmptType());
		this.setSrdFeeRate(grpInsurAppl.getSrdFeeRate());
		this.setPsnListHolderInfo(grpInsurAppl.getPsnListHolderInfo());

		//临时转换，保单辅助Fax问题
		GrpHolderInfoVo grpHolderInfoVo = new GrpHolderInfoVo();
		if(null != grpInsurAppl.getGrpHolderInfo()){
			grpHolderInfoVo.setAddress(grpInsurAppl.getGrpHolderInfo().getAddress());
			grpHolderInfoVo.setContactEmail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
			grpHolderInfoVo.setContactIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo());
			grpHolderInfoVo.setContactIdType(grpInsurAppl.getGrpHolderInfo().getContactIdType());
			grpHolderInfoVo.setContactMobile(grpInsurAppl.getGrpHolderInfo().getContactMobile());
			grpHolderInfoVo.setContactName(grpInsurAppl.getGrpHolderInfo().getContactName());
			grpHolderInfoVo.setContactTelephone(grpInsurAppl.getGrpHolderInfo().getContactTelephone());
			grpHolderInfoVo.setCorpRep(grpInsurAppl.getGrpHolderInfo().getCorpRep());
			grpHolderInfoVo.setFax(grpInsurAppl.getGrpHolderInfo().getFax());
			grpHolderInfoVo.setFormerGrpName(grpInsurAppl.getGrpHolderInfo().getFormerGrpName());
			grpHolderInfoVo.setGrpCountryCode(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode());
			grpHolderInfoVo.setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
			grpHolderInfoVo.setGrpIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());
			grpHolderInfoVo.setGrpIdType(grpInsurAppl.getGrpHolderInfo().getGrpIdType());
			grpHolderInfoVo.setGrpName(grpInsurAppl.getGrpHolderInfo().getGrpName());
			grpHolderInfoVo.setGrpPsnDeptType(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType());
			grpHolderInfoVo.setIpsnNum(grpInsurAppl.getGrpHolderInfo().getIpsnNum());
			grpHolderInfoVo.setLegalCode(grpInsurAppl.getGrpHolderInfo().getLegalCode());
			grpHolderInfoVo.setNatureCode(grpInsurAppl.getGrpHolderInfo().getNatureCode());
			grpHolderInfoVo.setNumOfEnterprise(grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise());
			grpHolderInfoVo.setOccClassCode(grpInsurAppl.getGrpHolderInfo().getOccClassCode());
			grpHolderInfoVo.setOnjobStaffNum(grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum());
			grpHolderInfoVo.setPartyId(grpInsurAppl.getGrpHolderInfo().getPartyId());
			grpHolderInfoVo.setTaxpayerId(grpInsurAppl.getGrpHolderInfo().getTaxpayerId());
		}
		this.setGrpHolderInfo(grpHolderInfoVo);

		this.setPaymentInfo(grpInsurAppl.getPaymentInfo());

		//因验收时间关系，临时赋值，后期需调整SalesInfoList赋值结构
		List<SalesInfo> salesInfoListNew = grpInsurAppl.getSalesInfoList();
		for (SalesInfo salesInfo : salesInfoListNew) {
			if((org.apache.commons.lang3.StringUtils.isBlank(salesInfo.getSalesNo()) 
					&& org.apache.commons.lang3.StringUtils.isBlank(salesInfo.getSalesName())) && (
							!org.apache.commons.lang3.StringUtils.isBlank(salesInfo.getSalesDeptNo()) 
							&& !org.apache.commons.lang3.StringUtils.isBlank(salesInfo.getDeptName()))){
				salesInfo.setSalesNo(salesInfo.getSalesDeptNo());
				salesInfo.setSalesName(salesInfo.getDeptName());
			}
		}
		this.setSalesInfoList(salesInfoListNew);


		if(org.apache.commons.lang3.StringUtils.isBlank(this.getPaymentInfo().getMoneyinType())){
			this.getPaymentInfo().setMoneyinType(MONEYIN_TYPE.TRANSFER.getKey());
		}

		//给要约赋值
		this.applState.setPolicyList(grpInsurAppl.getApplState().getPolicyList());
		this.applState.setIpsnNum(grpInsurAppl.getApplState().getIpsnNum());
		this.applState.setSumFaceAmnt(grpInsurAppl.getApplState().getSumFaceAmnt());
		this.applState.setSumPremium(grpInsurAppl.getApplState().getSumPremium());
		this.applState.setInsurDur(grpInsurAppl.getApplState().getInsurDur());
		this.applState.setInsurDurUnit(grpInsurAppl.getApplState().getInsurDurUnit());
		this.applState.setInforceDateType(grpInsurAppl.getApplState().getInforceDateType());
		this.applState.setIsPrePrint(grpInsurAppl.getApplState().getIsPrePrint());
		this.applState.setIsFreForce(grpInsurAppl.getApplState().getIsFreForce());

		//交费间隔月
		String	moneyinItrvlStr =grpInsurAppl.getPaymentInfo().getMoneyinItrvl();
		//缴费方式：M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期
		int moneyinItrvlMonInt;
		if("M".equals(moneyinItrvlStr)){
			moneyinItrvlMonInt=1;
		}else if("Q".equals(moneyinItrvlStr)){
			moneyinItrvlMonInt=3;
		}else if("H".equals(moneyinItrvlStr)){
			moneyinItrvlMonInt=6;
		}else if("Y".equals(moneyinItrvlStr)){
			moneyinItrvlMonInt=12;
		}else if("W".equals(moneyinItrvlStr)){
			moneyinItrvlMonInt=12;
		}else{
			moneyinItrvlMonInt=1;
		}
		for(Policy policy : this.getApplState().getPolicyList()){
			policy.setMoneyinItrvlMon(moneyinItrvlMonInt);
		}

		//给要约分组赋值
		this.setIpsnStateGrpList(getGrpIpsnStateGrpList(grpInsurAppl));

		//收费分组
		List<IpsnPayGrpVo> ipsnPayGrpList = new ArrayList<>();
		IpsnPayGrpVo ipsnPayGrpVo  = new IpsnPayGrpVo();
		if(null != grpInsurAppl.getIpsnPayGrpList() && !grpInsurAppl.getIpsnPayGrpList().isEmpty()){
			for (IpsnPayGrp ipsnPayGrp : grpInsurAppl.getIpsnPayGrpList()) {
				ipsnPayGrpVo.setBankAccNo(ipsnPayGrp.getBankaccNo());
				ipsnPayGrpVo.setBankAccName(ipsnPayGrp.getBankaccName());
				ipsnPayGrpVo.setBankChequeNo(ipsnPayGrp.getBankChequeNo());
				ipsnPayGrpVo.setBankCode(ipsnPayGrp.getBankCode());
				ipsnPayGrpVo.setFeeGrpName(ipsnPayGrp.getFeeGrpName());
				ipsnPayGrpVo.setFeeGrpNo(ipsnPayGrp.getFeeGrpNo());
				ipsnPayGrpVo.setIpsnGrpNum(ipsnPayGrp.getIpsnGrpNum());
				ipsnPayGrpVo.setMoneyinType(ipsnPayGrp.getMoneyinType());
				ipsnPayGrpList.add(ipsnPayGrpVo);
			}
		}
		this.setIpsnPayGrpList(ipsnPayGrpList);

		//组织架构树

		List<OrgTreeVo> orgTreeList = new ArrayList<>();
		if(null != grpInsurAppl.getOrgTreeList() && !grpInsurAppl.getOrgTreeList().isEmpty()){
			for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
				OrgTreeVo orgTreeVo = new OrgTreeVo();
				orgTreeVo.setBankAccName(orgTree.getBankaccName());
				orgTreeVo.setBankAccNo(orgTree.getBankaccNo());
				orgTreeVo.setBankChequeNo(orgTree.getBankChequeNo());
				orgTreeVo.setBankCode(orgTree.getBankCode());

				GrpHolderInfoVo grpHolderInfoVo2 = new GrpHolderInfoVo();
				if(null != orgTree.getGrpHolderInfo()){
					grpHolderInfoVo2.setAddress(orgTree.getGrpHolderInfo().getAddress());
					grpHolderInfoVo2.setContactEmail(orgTree.getGrpHolderInfo().getContactEmail());
					grpHolderInfoVo2.setContactIdNo(orgTree.getGrpHolderInfo().getContactIdNo());
					grpHolderInfoVo2.setContactIdType(orgTree.getGrpHolderInfo().getContactIdType());
					grpHolderInfoVo2.setContactMobile(orgTree.getGrpHolderInfo().getContactMobile());
					grpHolderInfoVo2.setContactName(orgTree.getGrpHolderInfo().getContactName());
					grpHolderInfoVo2.setContactTelephone(orgTree.getGrpHolderInfo().getContactTelephone());
					grpHolderInfoVo2.setCorpRep(orgTree.getGrpHolderInfo().getCorpRep());
					grpHolderInfoVo2.setFax(orgTree.getGrpHolderInfo().getFax());
					grpHolderInfoVo2.setFormerGrpName(orgTree.getGrpHolderInfo().getFormerGrpName());
					grpHolderInfoVo2.setGrpCountryCode(orgTree.getGrpHolderInfo().getGrpCountryCode());
					grpHolderInfoVo2.setGrpCustNo(orgTree.getGrpHolderInfo().getGrpCustNo());
					grpHolderInfoVo2.setGrpIdNo(orgTree.getGrpHolderInfo().getGrpIdNo());
					grpHolderInfoVo2.setGrpIdType(orgTree.getGrpHolderInfo().getGrpIdType());
					grpHolderInfoVo2.setGrpName(orgTree.getGrpHolderInfo().getGrpName());
					grpHolderInfoVo2.setGrpPsnDeptType(orgTree.getGrpHolderInfo().getGrpPsnDeptType());
					grpHolderInfoVo2.setIpsnNum(orgTree.getGrpHolderInfo().getIpsnNum());
					grpHolderInfoVo2.setLegalCode(orgTree.getGrpHolderInfo().getLegalCode());
					grpHolderInfoVo2.setNatureCode(orgTree.getGrpHolderInfo().getNatureCode());
					grpHolderInfoVo2.setNumOfEnterprise(orgTree.getGrpHolderInfo().getNumOfEnterprise());
					grpHolderInfoVo2.setOccClassCode(orgTree.getGrpHolderInfo().getOccClassCode());
					grpHolderInfoVo2.setOnjobStaffNum(orgTree.getGrpHolderInfo().getOnjobStaffNum());
					grpHolderInfoVo2.setPartyId(orgTree.getGrpHolderInfo().getPartyId());
					grpHolderInfoVo2.setTaxpayerId(orgTree.getGrpHolderInfo().getTaxpayerId());
				}
				orgTreeVo.setGrpHolderInfo(grpHolderInfoVo2);
				orgTreeVo.setIsPaid(orgTree.getIsPaid());
				orgTreeVo.setIsRoot(orgTree.getIsRoot());
				orgTreeVo.setLevelCode(orgTree.getLevelCode());
				orgTreeVo.setMoneyinType(orgTree.getMoneyinType());
				orgTreeVo.setMtnOpt(orgTree.getMtnOpt());
				orgTreeVo.setNodePayAmnt(orgTree.getNodePayAmnt());
				orgTreeVo.setNodeType(orgTree.getNodeType());
				orgTreeVo.setPrioLevelCode(orgTree.getPrioLevelCode());
				orgTreeVo.setReceiptOpt(orgTree.getReceiptOpt());
				orgTreeVo.setServiceOpt(orgTree.getServiceOpt());
				orgTreeVo.setTaxpayerId(orgTree.getTaxpayerId());
				orgTreeList.add(orgTreeVo);
			}
		}
		this.setOrgTreeList(orgTreeList);
		//操作轨迹里赋值管理机构
		this.operTrace = new OperTrace();
		this.operTrace.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());
		//建工险赋值
		if (null != grpInsurAppl.getConstructInsurInfo()) {
			ConstructInsurInfo constructInsurInfo = new ConstructInsurInfo();
			com.newcore.orbps.models.service.bo.grpinsurappl.ConstructInsurInfo constructInsurInfoOld = grpInsurAppl.getConstructInsurInfo();
			constructInsurInfo.setAcdntDeathNums(constructInsurInfoOld.getAcdntDeathNums());
			constructInsurInfo.setAcdntDisableNums(constructInsurInfoOld.getAcdntDisableNums());
			constructInsurInfo.setAwardGrade(constructInsurInfoOld.getAwardGrade());
			constructInsurInfo.setConstructFrom(constructInsurInfoOld.getConstructFrom());
			constructInsurInfo.setConstructTo(constructInsurInfoOld.getConstructTo());

			constructInsurInfo.setDiseaDeathNums(constructInsurInfoOld.getDiseaDeathNums());
			constructInsurInfo.setDiseaDisableNums(constructInsurInfoOld.getDiseaDisableNums());
			constructInsurInfo.setEnterpriseLicence(constructInsurInfoOld.getEnterpriseLicence());
			constructInsurInfo.setFloorHeight(constructInsurInfoOld.getFloorHeight());
			constructInsurInfo.setIobjCost(constructInsurInfoOld.getIobjCost());
			constructInsurInfo.setIobjName(constructInsurInfoOld.getIobjName());
			constructInsurInfo.setIobjNo(1L);

			constructInsurInfo.setIobjSize(constructInsurInfoOld.getIobjSize());
			constructInsurInfo.setPremCalType(constructInsurInfoOld.getPremCalType());
			constructInsurInfo.setProjLoc(constructInsurInfoOld.getProjLoc());
			constructInsurInfo.setProjLocType(constructInsurInfoOld.getProjLocType());
			constructInsurInfo.setProjType(constructInsurInfoOld.getProjType());
			constructInsurInfo.setSafetyFlag(constructInsurInfoOld.getSafetyFlag());
			constructInsurInfo.setSaftyAcdntFlag(constructInsurInfoOld.getSaftyAcdntFlag());
			constructInsurInfo.setSafetyFlag(constructInsurInfoOld.getSafetyFlag());
			this.setConstructInsurInfo(constructInsurInfo);
		}
		//基金险赋值
		this.setFundInsurInfo(grpInsurAppl.getFundInsurInfo());
		//赋值特约
		this.setConventions(grpInsurAppl.getConventions());

		if (null != this.conventions && !StringUtils.isEmpty(this.conventions.getInputConv())) {
			this.conventions.setInputConv("");
		}

	}


	/**
	 * 要约分组-合并其中的分组。
	 * 逻辑：遍历要约分组集合，获得其中的一个分组，遍历该分组的险种集合，把险种集合里的险种代码做截取前3位操作，之后放入map1集合中，
	 * 险种放入map1集合时初始值为1，如果有重复每次加一。遍历完险种之后，调用getIpsnStateGrp方法，获得合并后的一个分组，放入新建的要约分组集合listIpsnStateGrp中。
	 * 按照上面的逻辑遍历剩下的要约分组，最后把新建的listIpsnStateGrp返回，
	 */
	public List<IpsnStateGrp> getGrpIpsnStateGrpList(GrpInsurAppl grpInsurAppl) {
		List<IpsnStateGrp> listIpsnStateGrp = new ArrayList<>();
		List<IpsnStateGrp> listIpsnStateGrpOld = grpInsurAppl.getIpsnStateGrpList();
		if (listIpsnStateGrpOld == null || listIpsnStateGrpOld.isEmpty()) {
			return null;
		}

		//遍历分组
		for (IpsnStateGrp ipsnStateGrp : listIpsnStateGrpOld) {

			Map<String, Integer> map1 = new HashMap<>();
			IpsnStateGrp ipsnStateGrpNew = null;

			if(org.apache.commons.lang3.StringUtils.equals(IPSN_GRP_TYPE.PAY_SAME_PROPORT.getKey(), ipsnStateGrp.getIpsnGrpType())){//

				//遍历分组的险种信息，获得险种
				for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
					String pol = grpPolicy.getPolCode().substring(0, 3);
					if (map1.containsKey(pol)) {
						//将险种集合中的险种放入map1集合
						map1.put(pol, map1.get(pol) + 1);
					} else {
						map1.put(pol, 1);
					}
				}//end for

				List<GrpPolicy> grpPolicyList = new ArrayList<>();
				for (String pol  : map1.keySet()) {
					GrpPolicy grpPolicy = new GrpPolicy();
					grpPolicy.setPolCode(pol);
					grpPolicyList.add(grpPolicy);
				}
				ipsnStateGrp.setGrpPolicyList(grpPolicyList);
				ipsnStateGrpNew = ipsnStateGrp;
			}else{
				//遍历分组的险种信息，获得险种
				for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
					String pol = grpPolicy.getPolCode().substring(0, 3);
					if (map1.containsKey(pol)) {
						//将险种集合中的险种放入map1集合
						map1.put(pol, map1.get(pol) + 1);
					} else {
						map1.put(pol, 1);
					}
				}//end for
				//遍历分组-得到其中某一个分组---判断该分组是否有需要合并，如果需要就合并，不需要就直接返回。
				ipsnStateGrpNew = getIpsnStateGrp(ipsnStateGrp, map1);
			}//end if

			listIpsnStateGrp.add(ipsnStateGrpNew);
		}//end for-遍历分组


		return listIpsnStateGrp;
	}

	/**
	 * 要约分组-得到其中某一个分组
	 * 逻辑：首先判断该分组是否有需要合并，如果不需要就遍历该条要约分组的险种集合，把险种做截取前3位操作，之后返回该条要约分组；
	 * 如果需要合并，首先遍历map1集合，把需要合并的险种放到list中，把不需要分组的放入list2中，之后遍历list遍历险种把需要合并的险种进行合并，放入新建的险种集合grpPolicyList中，
	 * 遍历list2遍历险种，把险种做截取前3位操作，放入新建的险种集合grpPolicyList中，最后返回该条需要合并的要约分组。
	 */
	public IpsnStateGrp getIpsnStateGrp(IpsnStateGrp ipsnStateGrp, Map<String, Integer> map1) {
		List<GrpPolicy> grpPolicyList = new ArrayList<>();
		List<String> list = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		Double faceAmnt = 0d;
		Double premium = 0d;
		Double stdPremium = 0d;
		//对[承保费率][费率浮动幅度]做精度限定
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		//判断是否有需要合并的险种数组。
		if (map1.size() != ipsnStateGrp.getGrpPolicyList().size()) {
			//此时说明有需要合并的数组，遍历map获得需要合并的险种代码
			for (Map.Entry<String, Integer> entry : map1.entrySet()) {
				int intValue = Integer.valueOf(entry.getValue());
				//获取需要合并的险种代码
				if (intValue > 1) {
					list.add(String.valueOf(entry.getKey()));
				} else {
					list2.add(String.valueOf(entry.getKey()));
				}
			}//遍历map1集合
			for (int i = 0; i < list.size(); i++) {
				String mrCode = "";
				//遍历分组的险种信息，进行合并
				for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
					String pol = grpPolicy.getPolCode().substring(0, 3);
					if (list.get(i).equals(pol)) {
						faceAmnt += grpPolicy.getFaceAmnt();
						premium += grpPolicy.getPremium();
						stdPremium += grpPolicy.getStdPremium();
						mrCode = grpPolicy.getMrCode();
					}//需要合并的某一条数据
				}//end for 需要合并的多条数据
				GrpPolicy grpPolicyNew = new GrpPolicy();
				grpPolicyNew.setFaceAmnt(faceAmnt);
				grpPolicyNew.setMrCode(mrCode);
				grpPolicyNew.setPolCode(list.get(i));
				grpPolicyNew.setPremium(premium);
				grpPolicyNew.setStdPremium(stdPremium);
				//获得[承保费率]值，并赋值。
				String PremRate = dcmFmt.format((premium / faceAmnt) * 1000);
				grpPolicyNew.setPremRate(Double.valueOf(PremRate));
				//				//获得[费率浮动幅度]值，并赋值。
				//				String PremDiscount = dcmFmt.format(premium/stdPremium);
				//				grpPolicyNew.setPremDiscount(Double.valueOf(PremDiscount));
				grpPolicyList.add(grpPolicyNew);
			}
			for (int i = 0; i < list2.size(); i++) {
				//遍历分组的险种信息，获得不需要合并的数据
				for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
					String pol = grpPolicy.getPolCode().substring(0, 3);
					if (list2.get(i).equals(pol)) {
						grpPolicy.setPolCode(pol);
						faceAmnt = grpPolicy.getFaceAmnt();
						premium = grpPolicy.getPremium();
						//获得[承保费率]值，并赋值。
						String PremRate = dcmFmt.format((premium / faceAmnt) * 1000);
						grpPolicy.setPremRate(Double.valueOf(PremRate));
						//将不需要合并的数据放入新的集合中
						grpPolicyList.add(grpPolicy);
					}//end if
				}//end for 不需要合并的多条数据
			}
			ipsnStateGrp.setGrpPolicyList(grpPolicyList);
			return ipsnStateGrp;
		} else {
			//遍历分组的险种信息，	对不需要合并的把子险种代码改为险种代码
			for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
				String pol = grpPolicy.getPolCode().substring(0, 3);
				grpPolicy.setPolCode(pol);
				faceAmnt = grpPolicy.getFaceAmnt();
				premium = grpPolicy.getPremium();
				//获得[承保费率]值，并赋值。
				String PremRate = dcmFmt.format((premium / faceAmnt) * 1000);
				grpPolicy.setPremRate(Double.valueOf(PremRate));
				grpPolicyList.add(grpPolicy);
			}//end for 需要合并的多条数据
			ipsnStateGrp.setGrpPolicyList(grpPolicyList);
			return ipsnStateGrp;
		}
	}



}
