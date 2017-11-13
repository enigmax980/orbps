package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;

/**
 * 打印信息
 * @author xiaoye
 *
 */
public class GrpPrintInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 11454554453L;
	/** 保单类型 */
	private String cntrType;
	/** 清单打印 */
	private String prtIpsnLstType;
	/** 个人凭证打印 */
	private String ipsnVoucherPrt;
	/** 清单标记 */
	private String ipsnlstId;
	/** 赠送险标记 */
	private String giftFlag;
	/** 保单性质 */
	private String applProperty;
	/** 人工核保 */
	private String manualCheck;
	/** 异常告知 */
	private String exceptionInform;
	/** 被保人分组类型 */
	private String groupType;
	/** 投保资料影像-档案文件上传 */
	private String file;
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
	 * @return the prtIpsnLstType
	 */
	public String getPrtIpsnLstType() {
		return prtIpsnLstType;
	}
	/**
	 * @param prtIpsnLstType the prtIpsnLstType to set
	 */
	public void setPrtIpsnLstType(String prtIpsnLstType) {
		this.prtIpsnLstType = prtIpsnLstType;
	}
	/**
	 * @return the ipsnVoucherPrt
	 */
	public String getIpsnVoucherPrt() {
		return ipsnVoucherPrt;
	}
	/**
	 * @param ipsnVoucherPrt the ipsnVoucherPrt to set
	 */
	public void setIpsnVoucherPrt(String ipsnVoucherPrt) {
		this.ipsnVoucherPrt = ipsnVoucherPrt;
	}
	/**
	 * @return the ipsnlstId
	 */
	public String getIpsnlstId() {
		return ipsnlstId;
	}
	/**
	 * @param ipsnlstId the ipsnlstId to set
	 */
	public void setIpsnlstId(String ipsnlstId) {
		this.ipsnlstId = ipsnlstId;
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
	 * @return the applProperty
	 */
	public String getApplProperty() {
		return applProperty;
	}
	/**
	 * @param applProperty the applProperty to set
	 */
	public void setApplProperty(String applProperty) {
		this.applProperty = applProperty;
	}
	/**
	 * @return the manualCheck
	 */
	public String getManualCheck() {
		return manualCheck;
	}
	/**
	 * @param manualCheck the manualCheck to set
	 */
	public void setManualCheck(String manualCheck) {
		this.manualCheck = manualCheck;
	}
	/**
	 * @return the exceptionInform
	 */
	public String getExceptionInform() {
		return exceptionInform;
	}
	/**
	 * @param exceptionInform the exceptionInform to set
	 */
	public void setExceptionInform(String exceptionInform) {
		this.exceptionInform = exceptionInform;
	}
	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}
	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GrpPrintInfoVo [cntrType=" + cntrType + ", prtIpsnLstType=" + prtIpsnLstType + ", ipsnVoucherPrt="
				+ ipsnVoucherPrt + ", ipsnlstId=" + ipsnlstId + ", giftFlag=" + giftFlag + ", applProperty="
				+ applProperty + ", manualCheck=" + manualCheck + ", exceptionInform=" + exceptionInform
				+ ", groupType=" + groupType + ", file=" + file + "]";
	}
}
