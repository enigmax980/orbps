package com.newcore.orbps.models.pcms.bo;
import java.io.Serializable;

/**
 * CgPubExeAttrServiceImpl 的入参:直连保单辅助做 保单落地时使用，后期调ESB时将删除本类。
 */
public class CgPubExeAttrBo implements Serializable {

	
	private static final long serialVersionUID = -2647770876430703048L;
	
	/* 字段名：投保单号，长度：25，是否必填：是 */
	private String applNo;    
	
	/*
	 * 字段名：合同组号，长度：25，是否必填：是 契约平台自己填写
	 */
	private String cgNo;
	
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
	 * 字段名：团险方案审批号，长度：22，是否必填：否
	 */
	private String approNo;
	/*
	 * 字段名：契约业务类型，长度：2，是否必填：否
	 * 1.员福计划（统谈统签）；2：员福计划（统谈分签）；3：员福计划（普通业务）；4：老年保险；5：农村小额保险；6：建工保险；7：学生保险；8：
	 * 政保保险；9：计生保险
	 */
	private String applBusiType;
	
	/*
	 * 字段名：团单清单标志，长度：2，是否必填：是 L:普通清单，A：档案清单，M：事后补录，N：无清单
	 */
	private String lstProcType;
	
	/* 字段名：管理机构号，长度：6，是否必填：IF 柜面出单 THEN 必录 */
	private String mgrBranchNo;

	private String dataSource;
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
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
	public String getApproNo() {
		return approNo;
	}
	public void setApproNo(String approNo) {
		this.approNo = approNo;
	}
	public String getApplBusiType() {
		return applBusiType;
	}
	public void setApplBusiType(String applBusiType) {
		this.applBusiType = applBusiType;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getLstProcType() {
		return lstProcType;
	}
	public void setLstProcType(String lstProcType) {
		this.lstProcType = lstProcType;
	}
	
	

}

