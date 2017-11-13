package com.newcore.orbps.models.service.bo.aa;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.service.bo.ApplState;
import com.newcore.orbps.models.service.bo.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.GcLppPremRateBo;
import com.newcore.orbps.models.service.bo.OperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.FundInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.HealthInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
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
	private static final long serialVersionUID = -8121692577743898083L;

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
	 * 字段名：被保人清单打印方式,长度：2,是否必填：是 L 只打清单，V 打印清单凭证，N 只打印合同，P 只打印凭证，H 打印家庭险凭证，F 不打印凭证
	 */
	private String prtIpsnlstType;

	/*
	 * 字段名：销售人员是否共同展业标识，长度：2，是否必填：否， 团单特有 0:否，1：是。（默认为0）
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
	 * 字段名：外包录入标志，长度：2，是否必填：否 1：是，0：否， （默认为0）
	 */
	private String entChannelFlag;

	/*
	 * 字段名：汇交人类型，长度：2，是否必填：C 清汇特有 1.个人汇交； 2.单位汇交
	 */
	private String listType;

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
	 * 字段名：赠送保险标志，长度：2，是否必填：否 0：否；1：是。（默认为0）
	 */
	private String giftFlag;

	/*
	 * 字段名：是否异常告知，长度：2，是否必填：是 0：否；1：是。
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
	 * 字段名：纳税人识别号，长度：15，是否必填：否
	 */
	private String taxpayerId;

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
	private Double srndChargePct;

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

	// 操作轨迹
	private OperTrace operTrace;

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

	//团体保单一次性缴费多期缴费标准
	private List<GcLppPremRateBo> gcLppPremRateBoList;

	//应收付数据
	private List<PlnmioRec> plnmioReclist;



	public GrpInsurAppl() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getApplNo() {
		return applNo;
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



	public String getListType() {
		return listType;
	}



	public void setListType(String listType) {
		this.listType = listType;
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



	public String getTaxpayerId() {
		return taxpayerId;
	}



	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
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



	public Double getSrndChargePct() {
		return srndChargePct;
	}



	public void setSrndChargePct(Double srndChargePct) {
		this.srndChargePct = srndChargePct;
	}



	public PsnListHolderInfo getPsnListHolderInfo() {
		return psnListHolderInfo;
	}



	public void setPsnListHolderInfo(PsnListHolderInfo psnListHolderInfo) {
		this.psnListHolderInfo = psnListHolderInfo;
	}



	public GrpHolderInfo getGrpHolderInfo() {
		return grpHolderInfo;
	}



	public void setGrpHolderInfo(GrpHolderInfo grpHolderInfo) {
		this.grpHolderInfo = grpHolderInfo;
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



	public List<IpsnPayGrp> getIpsnPayGrpList() {
		return ipsnPayGrpList;
	}



	public void setIpsnPayGrpList(List<IpsnPayGrp> ipsnPayGrpList) {
		this.ipsnPayGrpList = ipsnPayGrpList;
	}



	public OperTrace getOperTrace() {
		return operTrace;
	}



	public void setOperTrace(OperTrace operTrace) {
		this.operTrace = operTrace;
	}



	public List<OrgTree> getOrgTreeList() {
		return orgTreeList;
	}



	public void setOrgTreeList(List<OrgTree> orgTreeList) {
		this.orgTreeList = orgTreeList;
	}



	public ConstructInsurInfo getConstructInsurInfo() {
		return constructInsurInfo;
	}



	public void setConstructInsurInfo(ConstructInsurInfo constructInsurInfo) {
		this.constructInsurInfo = constructInsurInfo;
	}



	public HealthInsurInfo getHealthInsurInfo() {
		return healthInsurInfo;
	}



	public void setHealthInsurInfo(HealthInsurInfo healthInsurInfo) {
		this.healthInsurInfo = healthInsurInfo;
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



	public List<PlnmioRec> getPlnmioReclist() {
		return plnmioReclist;
	}



	public void setPlnmioReclist(List<PlnmioRec> plnmioReclist) {
		this.plnmioReclist = plnmioReclist;
	}










}


