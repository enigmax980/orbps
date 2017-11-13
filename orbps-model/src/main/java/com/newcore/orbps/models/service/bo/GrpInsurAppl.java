package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.service.bo.grpinsurappl.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.FundInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.HealthInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.supports.dicts.YES_NO_FLAG;
/**
 * 团体出单基本信息-落地前整合数据
 * 
 * @author lijifei 
 * 创建时间：2016年9月20日下午7:46:11
 */
public class GrpInsurAppl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2419853196207722665L;

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

	/* 字段名：管理机构号，长度：6，是否必填：是 */
	private String mgrBranchNo;

	/* 字段名：归档机构号，长度：6，是否必填：否*/
	private String arcBranchNo;

	// 个人汇交人信息，清汇特有
	private PsnListHolderInfo psnListHolderInfo;

	// 团体客户信息
	private GrpHolderInfo grpHolderInfo;

	// 销售信息
	private List<SalesInfo> salesInfoList;

	// 缴费相关
	private PaymentInfo paymentInfo;

	// 投保要约
	private ApplState applState;

	// 要约分组
	private List<IpsnStateGrp> ipsnStateGrpList;

	// 团体收费组
	private List<IpsnPayGrp> ipsnPayGrpList;

	// 组织架构树 团单特有
	private List<OrgTree> orgTreeList;

	// 建工险 团单特有
	private ConstructInsurInfo constructInsurInfo;

	// 健康险 团单特有
	private HealthInsurInfo healthInsurInfo;

	// 基金险 团单特有
	private FundInsurInfo fundInsurInfo;

	//特约
	private Conventions conventions;
	//任务流信息
	private TaskInfo taskInfo;

	/*
	 * 字段名：被保人清单打印方式,长度：2,是否必填：是 L 只打清单，V 打印清单凭证，N 只打印合同，P 只打印凭证，H 打印家庭险凭证，F 不打印凭证
	 */
	private String prtIpsnlstType;

	//团体保单一次性缴费多期缴费标准
	private List<GcLppPremRateBo> gcLppPremRateBoList;

	//应收付数据
	private List<PlnmioRec> plnmioReclist;



	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}



	public String getPrtIpsnlstType() {
		return prtIpsnlstType;
	}



	public void setPrtIpsnlstType(String prtIpsnlstType) {
		this.prtIpsnlstType = prtIpsnlstType;
	}



	public List<GcLppPremRateBo> getGcLppPremRateBoList() {
		return gcLppPremRateBoList;
	}



	public void setGcLppPremRateBoList(List<GcLppPremRateBo> gcLppPremRateBoList) {
		this.gcLppPremRateBoList = gcLppPremRateBoList;
	}



	public List<PlnmioRec> getPlnmioReclist() {
		return plnmioReclist;
	}



	public void setPlnmioReclist(List<PlnmioRec> plnmioReclist) {
		this.plnmioReclist = plnmioReclist;
	}



	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	/**
	 * @return the lstProcType
	 */
	public String getLstProcType() {
		return lstProcType;
	}

	/**
	 * @param lstProcType the lstProcType to set
	 */
	public void setLstProcType(String lstProcType) {
		this.lstProcType = lstProcType;
	}

	/**
	 * @return the insurProperty
	 */
	public String getInsurProperty() {
		return insurProperty;
	}

	/**
	 * @param insurProperty the insurProperty to set
	 */
	public void setInsurProperty(String insurProperty) {
		this.insurProperty = insurProperty;
	}

	/**
	 * @return the approNo
	 */
	public String getApproNo() {
		return approNo;
	}

	/**
	 * @param approNo the approNo to set
	 */
	public void setApproNo(String approNo) {
		this.approNo = approNo;
	}

	/**
	 * @return the accessSource
	 */
	public String getAccessSource() {
		return accessSource;
	}

	/**
	 * @param accessSource the accessSource to set
	 */
	public void setAccessSource(String accessSource) {
		this.accessSource = accessSource;
	}

	/**
	 * @return the grpBusiType
	 */
	public String getGrpBusiType() {
		return grpBusiType;
	}

	/**
	 * @param grpBusiType the grpBusiType to set
	 */
	public void setGrpBusiType(String grpBusiType) {
		this.grpBusiType = grpBusiType;
	}

	/**
	 * @return the applBusiType
	 */
	public String getApplBusiType() {
		return applBusiType;
	}

	/**
	 * @param applBusiType the applBusiType to set
	 */
	public void setApplBusiType(String applBusiType) {
		this.applBusiType = applBusiType;
	}

	/**
	 * @return the cntrPrintType
	 */
	public String getCntrPrintType() {
		return cntrPrintType;
	}

	/**
	 * @param cntrPrintType the cntrPrintType to set
	 */
	public void setCntrPrintType(String cntrPrintType) {
		this.cntrPrintType = cntrPrintType;
	}

	/**
	 * @return the listPrintType
	 */
	public String getListPrintType() {
		return listPrintType;
	}

	/**
	 * @param listPrintType the listPrintType to set
	 */
	public void setListPrintType(String listPrintType) {
		this.listPrintType = listPrintType;
	}

	/**
	 * @return the voucherPrintType
	 */
	public String getVoucherPrintType() {
		return voucherPrintType;
	}

	/**
	 * @param voucherPrintType the voucherPrintType to set
	 */
	public void setVoucherPrintType(String voucherPrintType) {
		this.voucherPrintType = voucherPrintType;
	}

	/**
	 * @return the salesDevelopFlag
	 */
	public String getSalesDevelopFlag() {
		return salesDevelopFlag;
	}

	/**
	 * @param salesDevelopFlag the salesDevelopFlag to set
	 */
	public void setSalesDevelopFlag(String salesDevelopFlag) {
		this.salesDevelopFlag = salesDevelopFlag;
	}

	/**
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}

	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	/**
	 * @return the cntrSendType
	 */
	public String getCntrSendType() {
		return cntrSendType;
	}

	/**
	 * @param cntrSendType the cntrSendType to set
	 */
	public void setCntrSendType(String cntrSendType) {
		this.cntrSendType = cntrSendType;
	}

	/**
	 * @return the entChannelFlag
	 */
	public String getEntChannelFlag() {
		return entChannelFlag;
	}

	/**
	 * @param entChannelFlag the entChannelFlag to set
	 */
	public void setEntChannelFlag(String entChannelFlag) {
		this.entChannelFlag = entChannelFlag;
	}


	public String getSgType() {
		return sgType;
	}



	public void setSgType(String sgType) {
		this.sgType = sgType;
	}



	/**
	 * @return the applDate
	 */
	public Date getApplDate() {
		return applDate;
	}

	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}

	/**
	 * @return the accessChannel
	 */
	public String getAccessChannel() {
		return accessChannel;
	}

	/**
	 * @param accessChannel the accessChannel to set
	 */
	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}

	/**
	 * @return the argueType
	 */
	public String getArgueType() {
		return argueType;
	}

	/**
	 * @param argueType the argueType to set
	 */
	public void setArgueType(String argueType) {
		this.argueType = argueType;
	}

	/**
	 * @return the arbitration
	 */
	public String getArbitration() {
		return arbitration;
	}

	/**
	 * @param arbitration the arbitration to set
	 */
	public void setArbitration(String arbitration) {
		this.arbitration = arbitration;
	}

	/**
	 * @return the renewedCgNo
	 */
	public String getRenewedCgNo() {
		return renewedCgNo;
	}

	/**
	 * @param renewedCgNo the renewedCgNo to set
	 */
	public void setRenewedCgNo(String renewedCgNo) {
		this.renewedCgNo = renewedCgNo;
	}

	/**
	 * @return the agreementNo
	 */
	public String getAgreementNo() {
		return agreementNo;
	}

	/**
	 * @param agreementNo the agreementNo to set
	 */
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	/**
	 * @return the giftFlag
	 */
	public String getGiftFlag() {
		return giftFlag;
	}

	/**
	 * @param giftFlag the giftFlag to set
	 */
	public void setGiftFlag(String giftFlag) {
		this.giftFlag = giftFlag;
	}

	/**
	 * @return the notificaStat
	 */
	public String getNotificaStat() {
		return notificaStat;
	}

	/**
	 * @param notificaStat the notificaStat to set
	 */
	public void setNotificaStat(String notificaStat) {
		this.notificaStat = notificaStat;
	}

	/**
	 * @return the cgNo
	 */
	public String getCgNo() {
		return cgNo;
	}

	/**
	 * @param cgNo the cgNo to set
	 */
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	/**
	 * @return the sgNo
	 */
	public String getSgNo() {
		return sgNo;
	}

	/**
	 * @param sgNo the sgNo to set
	 */
	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}

	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}

	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	/**
	 * @return the inForceDate
	 */
	public Date getInForceDate() {
		return inForceDate;
	}

	/**
	 * @param inForceDate the inForceDate to set
	 */
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}

	/**
	 * @return the renewTimes
	 */
	public Long getRenewTimes() {
		return renewTimes;
	}

	/**
	 * @param renewTimes the renewTimes to set
	 */
	public void setRenewTimes(Long renewTimes) {
		this.renewTimes = renewTimes;
	}

	/**
	 * @return the cntrExpiryDate
	 */
	public Date getCntrExpiryDate() {
		return cntrExpiryDate;
	}

	/**
	 * @param cntrExpiryDate the cntrExpiryDate to set
	 */
	public void setCntrExpiryDate(Date cntrExpiryDate) {
		this.cntrExpiryDate = cntrExpiryDate;
	}

	/**
	 * @return the firPolCode
	 */
	public String getFirPolCode() {
		return firPolCode;
	}

	/**
	 * @param firPolCode the firPolCode to set
	 */
	public void setFirPolCode(String firPolCode) {
		this.firPolCode = firPolCode;
	}

	/**
	 * @return the udwType
	 */
	public String getUdwType() {
		return udwType;
	}

	/**
	 * @param udwType the udwType to set
	 */
	public void setUdwType(String udwType) {
		this.udwType = udwType;
	}

	/**
	 * @return the srndAmntCmptType
	 */
	public String getSrndAmntCmptType() {
		return srndAmntCmptType;
	}

	/**
	 * @param srndAmntCmptType the srndAmntCmptType to set
	 */
	public void setSrndAmntCmptType(String srndAmntCmptType) {
		this.srndAmntCmptType = srndAmntCmptType;
	}

	/**
	 * @return the srdFeeRate
	 */
	public Double getSrdFeeRate() {
		return srdFeeRate;
	}

	/**
	 * @param srdFeeRate the srdFeeRate to set
	 */
	public void setSrdFeeRate(Double srdFeeRate) {
		this.srdFeeRate = srdFeeRate;
	}

	/**
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	/**
	 * @return the arcBranchNo
	 */
	public String getArcBranchNo() {
		return arcBranchNo;
	}

	/**
	 * @param arcBranchNo the arcBranchNo to set
	 */
	public void setArcBranchNo(String arcBranchNo) {
		this.arcBranchNo = arcBranchNo;
	}

	/**
	 * @return the psnListHolderInfo
	 */
	public PsnListHolderInfo getPsnListHolderInfo() {
		return psnListHolderInfo;
	}

	/**
	 * @param psnListHolderInfo the psnListHolderInfo to set
	 */
	public void setPsnListHolderInfo(PsnListHolderInfo psnListHolderInfo) {
		this.psnListHolderInfo = psnListHolderInfo;
	}

	/**
	 * @return the grpHolderInfo
	 */
	public GrpHolderInfo getGrpHolderInfo() {
		return grpHolderInfo;
	}

	/**
	 * @param grpHolderInfo the grpHolderInfo to set
	 */
	public void setGrpHolderInfo(GrpHolderInfo grpHolderInfo) {
		this.grpHolderInfo = grpHolderInfo;
	}

	/**
	 * @return the salesInfoList
	 */
	public List<SalesInfo> getSalesInfoList() {
		return salesInfoList;
	}

	/**
	 * @param salesInfoList the salesInfoList to set
	 */
	public void setSalesInfoList(List<SalesInfo> salesInfoList) {
		this.salesInfoList = salesInfoList;
	}

	/**
	 * @return the paymentInfo
	 */
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * @param paymentInfo the paymentInfo to set
	 */
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	/**
	 * @return the applState
	 */
	public ApplState getApplState() {
		return applState;
	}

	/**
	 * @param applState the applState to set
	 */
	public void setApplState(ApplState applState) {
		this.applState = applState;
	}

	/**
	 * @return the ipsnStateGrpList
	 */
	public List<IpsnStateGrp> getIpsnStateGrpList() {
		return ipsnStateGrpList;
	}

	/**
	 * @param ipsnStateGrpList the ipsnStateGrpList to set
	 */
	public void setIpsnStateGrpList(List<IpsnStateGrp> ipsnStateGrpList) {
		this.ipsnStateGrpList = ipsnStateGrpList;
	}

	/**
	 * @return the ipsnPayGrpList
	 */
	public List<IpsnPayGrp> getIpsnPayGrpList() {
		return ipsnPayGrpList;
	}

	/**
	 * @param ipsnPayGrpList the ipsnPayGrpList to set
	 */
	public void setIpsnPayGrpList(List<IpsnPayGrp> ipsnPayGrpList) {
		this.ipsnPayGrpList = ipsnPayGrpList;
	}

	/**
	 * @return the orgTreeList
	 */
	public List<OrgTree> getOrgTreeList() {
		return orgTreeList;
	}

	/**
	 * @param orgTreeList the orgTreeList to set
	 */
	public void setOrgTreeList(List<OrgTree> orgTreeList) {
		this.orgTreeList = orgTreeList;
	}

	/**
	 * @return the constructInsurInfo
	 */
	public ConstructInsurInfo getConstructInsurInfo() {
		return constructInsurInfo;
	}

	/**
	 * @param constructInsurInfo the constructInsurInfo to set
	 */
	public void setConstructInsurInfo(ConstructInsurInfo constructInsurInfo) {
		this.constructInsurInfo = constructInsurInfo;
	}

	/**
	 * @return the healthInsurInfo
	 */
	public HealthInsurInfo getHealthInsurInfo() {
		return healthInsurInfo;
	}

	/**
	 * @param healthInsurInfo the healthInsurInfo to set
	 */
	public void setHealthInsurInfo(HealthInsurInfo healthInsurInfo) {
		this.healthInsurInfo = healthInsurInfo;
	}

	/**
	 * @return the fundInsurInfo
	 */
	public FundInsurInfo getFundInsurInfo() {
		return fundInsurInfo;
	}

	/**
	 * @param fundInsurInfo the fundInsurInfo to set
	 */
	public void setFundInsurInfo(FundInsurInfo fundInsurInfo) {
		this.fundInsurInfo = fundInsurInfo;
	}

	/**
	 * @return the conventions
	 */
	public Conventions getConventions() {
		return conventions;
	}

	/**
	 * @param conventions the conventions to set
	 */
	public void setConventions(Conventions conventions) {
		this.conventions = conventions;
	}

	/**
	 * @return the taskInfo
	 */
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	/**
	 * @param taskInfo the taskInfo to set
	 */
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	/**
	 * @return the provBranchNo
	 */
	public String getProvBranchNo() {
		return provBranchNo;
	}

	/**
	 * @param provBranchNo the provBranchNo to set
	 */
	public void setProvBranchNo(String provBranchNo) {
		this.provBranchNo = provBranchNo;
	}

	public Boolean isManApprove(Map<String, Object> insurRuleMangerMap){
		// 如果查询结果为空，不人工审批
		if (insurRuleMangerMap == null) {
			return false;
		} else {
			String artificialApproveFlag = (String) insurRuleMangerMap.get("ARTIFICIAL_APPROVE_FLAG");
			Long beforeEffectiveDate = ((BigDecimal) insurRuleMangerMap.get("BEFORE_EFFECTIVE_DATE")).longValue();
			Long afterEffectiveDate = ((BigDecimal) insurRuleMangerMap.get("AFTER_EFFECTIVE_DATE")).longValue();
			Date effectiveDateBackAcross = (Date) insurRuleMangerMap.get("EFFECTIVE_DATE_BACK_ACROSS");
			// 如果查询结果为人工审批，契约类型为清汇，发起清汇人工审批
			if (StringUtils.equals(artificialApproveFlag, YES_NO_FLAG.YES.getKey())) {
				return true;

				// 如果查询结果为人工审批，契约类型为团单，发起团单人工审批
			} else if (this.getApplState().getDesignForceDate() != null) {
				String pattern = "yyyyMMdd";
				long time = Long.parseLong(DateFormatUtils.format(this.getApplState().getDesignForceDate(), pattern));
				long beforetime = time - beforeEffectiveDate;
				long aftertime = time - afterEffectiveDate;
				long backAcrosstime = Long.parseLong(DateFormatUtils.format(effectiveDateBackAcross, pattern));
				long nowtime = Long.parseLong(DateFormatUtils.format(new Date(), pattern));
				if ((nowtime < beforetime || nowtime > aftertime || beforetime < backAcrosstime)) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}
		}		
	}
}
