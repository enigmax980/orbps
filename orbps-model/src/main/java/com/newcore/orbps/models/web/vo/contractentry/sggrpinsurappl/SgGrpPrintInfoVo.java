package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;

/**
 * 打印信息
 * @author wangyanjie
 *
 */
public class SgGrpPrintInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 11454554453L;
	
	/** 合同打印方式 */
	private String underNoticeType;
	/** 清单打印 */
	private String listPrint;
	/** 个人凭证打印 */
	private String personalIdPrint;
	/** 清单标记 */
	private String listFlag;
	/** 赠送险标记 */
	private String giftInsFlag;
	/** 保单性质 */
	private String polProperty;
	/** 是否人工核保 */
	private String manualUwFlag;
	/** 是否异常告知 */
	private String unusualFlag;
	/** 被保人分组类型 */
	private String groupType;
	/** 投保资料影像/档案清单文件上传 */
	private String imageListUpload;
	/**
	 * @return the underNoticeType
	 */
	public String getUnderNoticeType() {
		return underNoticeType;
	}
	/**
	 * @param underNoticeType the underNoticeType to set
	 */
	public void setUnderNoticeType(String underNoticeType) {
		this.underNoticeType = underNoticeType;
	}
	/**
	 * @return the listPrint
	 */
	public String getListPrint() {
		return listPrint;
	}
	/**
	 * @param listPrint the listPrint to set
	 */
	public void setListPrint(String listPrint) {
		this.listPrint = listPrint;
	}
	/**
	 * @return the personalIdPrint
	 */
	public String getPersonalIdPrint() {
		return personalIdPrint;
	}
	/**
	 * @param personalIdPrint the personalIdPrint to set
	 */
	public void setPersonalIdPrint(String personalIdPrint) {
		this.personalIdPrint = personalIdPrint;
	}
	/**
	 * @return the listFlag
	 */
	public String getListFlag() {
		return listFlag;
	}
	/**
	 * @param listFlag the listFlag to set
	 */
	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}
	/**
	 * @return the giftInsFlag
	 */
	public String getGiftInsFlag() {
		return giftInsFlag;
	}
	/**
	 * @param giftInsFlag the giftInsFlag to set
	 */
	public void setGiftInsFlag(String giftInsFlag) {
		this.giftInsFlag = giftInsFlag;
	}
	/**
	 * @return the polProperty
	 */
	public String getPolProperty() {
		return polProperty;
	}
	/**
	 * @param polProperty the polProperty to set
	 */
	public void setPolProperty(String polProperty) {
		this.polProperty = polProperty;
	}
	/**
	 * @return the manualUwFlag
	 */
	public String getManualUwFlag() {
		return manualUwFlag;
	}
	/**
	 * @param manualUwFlag the manualUwFlag to set
	 */
	public void setManualUwFlag(String manualUwFlag) {
		this.manualUwFlag = manualUwFlag;
	}
	/**
	 * @return the unusualFlag
	 */
	public String getUnusualFlag() {
		return unusualFlag;
	}
	/**
	 * @param unusualFlag the unusualFlag to set
	 */
	public void setUnusualFlag(String unusualFlag) {
		this.unusualFlag = unusualFlag;
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
	 * @return the imageListUpload
	 */
	public String getImageListUpload() {
		return imageListUpload;
	}
	/**
	 * @param imageListUpload the imageListUpload to set
	 */
	public void setImageListUpload(String imageListUpload) {
		this.imageListUpload = imageListUpload;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgGrpPrintInfoVo [underNoticeType=" + underNoticeType + ", listPrint=" + listPrint
				+ ", personalIdPrint=" + personalIdPrint + ", listFlag=" + listFlag + ", giftInsFlag=" + giftInsFlag
				+ ", polProperty=" + polProperty + ", manualUwFlag=" + manualUwFlag + ", unusualFlag=" + unusualFlag
				+ ", groupType=" + groupType + ", imageListUpload=" + imageListUpload + "]";
	}
	
}
