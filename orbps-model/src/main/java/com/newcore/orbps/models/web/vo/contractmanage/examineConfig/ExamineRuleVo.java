package com.newcore.orbps.models.web.vo.contractmanage.examineConfig;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核
 * @author xiaoYe
 *
 */
public class ExamineRuleVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445441L;
	/** 销售机构代码 */
	private String branchCode;
	/** 销售渠道 */
	private String salesChannel;
	/** 业务场景 */
	private String scene;
	/** 契约形式 */
	private String cntrForm;
	/** 产品类型 */
	private String productType;
	  /** 生效日期 */
    private String validDate;
    /** 失效日期 */
    private String invalidDate;
	/** 是否自动审批 */
	private String autoApproveFlag;
	/** 人工审批 */
	private String artificialApproveFlag;
	/** 1、生效日往前追溯天数 */
	private Long beforeEffectiveDate;
	/** 2、生效日往后指定天数 */
	private Long afterEffectiveDate;
	/** 3、生效日往前追溯跨越日期 */
	private String effectiveDateBackAcross;
	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}
	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}
	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	/**
	 * @return the scene
	 */
	public String getScene() {
		return scene;
	}
	/**
	 * @param scene the scene to set
	 */
	public void setScene(String scene) {
		this.scene = scene;
	}
	/**
	 * @return the cntrForm
	 */
	public String getCntrForm() {
		return cntrForm;
	}
	/**
	 * @param cntrForm the cntrForm to set
	 */
	public void setCntrForm(String cntrForm) {
		this.cntrForm = cntrForm;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the validDate
	 */
	public String getValidDate() {
		return validDate;
	}
	/**
	 * @param validDate the validDate to set
	 */
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	/**
	 * @return the invalidDate
	 */
	public String getInvalidDate() {
		return invalidDate;
	}
	/**
	 * @param invalidDate the invalidDate to set
	 */
	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}
	/**
	 * @return the autoApproveFlag
	 */
	public String getAutoApproveFlag() {
		return autoApproveFlag;
	}
	/**
	 * @param autoApproveFlag the autoApproveFlag to set
	 */
	public void setAutoApproveFlag(String autoApproveFlag) {
		this.autoApproveFlag = autoApproveFlag;
	}
	/**
	 * @return the artificialApproveFlag
	 */
	public String getArtificialApproveFlag() {
		return artificialApproveFlag;
	}
	/**
	 * @param artificialApproveFlag the artificialApproveFlag to set
	 */
	public void setArtificialApproveFlag(String artificialApproveFlag) {
		this.artificialApproveFlag = artificialApproveFlag;
	}
	/**
	 * @return the beforeEffectiveDate
	 */
	public Long getBeforeEffectiveDate() {
		return beforeEffectiveDate;
	}
	/**
	 * @param beforeEffectiveDate the beforeEffectiveDate to set
	 */
	public void setBeforeEffectiveDate(Long beforeEffectiveDate) {
		this.beforeEffectiveDate = beforeEffectiveDate;
	}
	/**
	 * @return the afterEffectiveDate
	 */
	public Long getAfterEffectiveDate() {
		return afterEffectiveDate;
	}
	/**
	 * @param afterEffectiveDate the afterEffectiveDate to set
	 */
	public void setAfterEffectiveDate(Long afterEffectiveDate) {
		this.afterEffectiveDate = afterEffectiveDate;
	}
	/**
	 * @return the effectiveDateBackAcross
	 */
	public String getEffectiveDateBackAcross() {
		return effectiveDateBackAcross;
	}
	/**
	 * @param effectiveDateBackAcross the effectiveDateBackAcross to set
	 */
	public void setEffectiveDateBackAcross(String effectiveDateBackAcross) {
		this.effectiveDateBackAcross = effectiveDateBackAcross;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExamineRuleVo [branchCode=" + branchCode + ", salesChannel=" + salesChannel + ", scene=" + scene
				+ ", cntrForm=" + cntrForm + ", productType=" + productType + ", validDate=" + validDate
				+ ", invalidDate=" + invalidDate + ", autoApproveFlag=" + autoApproveFlag + ", artificialApproveFlag="
				+ artificialApproveFlag + ", beforeEffectiveDate=" + beforeEffectiveDate + ", afterEffectiveDate="
				+ afterEffectiveDate + ", effectiveDateBackAcross=" + effectiveDateBackAcross + "]";
	}
	
}
