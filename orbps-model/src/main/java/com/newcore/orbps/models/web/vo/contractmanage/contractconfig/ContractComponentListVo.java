package com.newcore.orbps.models.web.vo.contractmanage.contractconfig;

import java.io.Serializable;

/**
 * 构件基本信息
 * @author xiaoye
 *
 */
public class ContractComponentListVo  implements Serializable{
	
	private static final long serialVersionUID = 11464445436L;
	/** 构件序号 */
	private String applId;
	/** 构件名称 */
	private String componentName;
	/** 构件顺序 */
	private String componentNo;
	/** 构件属性 */
	private String componentProperty;
	/** 构件模板 */
	private String componentTemplate;
	/** 构件修改 */
	private String componentUpdate;
	/** 预览 */
	private String componentPreview;
	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}
	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
	}
	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}
	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	/**
	 * @return the componentNo
	 */
	public String getComponentNo() {
		return componentNo;
	}
	/**
	 * @param componentNo the componentNo to set
	 */
	public void setComponentNo(String componentNo) {
		this.componentNo = componentNo;
	}
	/**
	 * @return the componentProperty
	 */
	public String getComponentProperty() {
		return componentProperty;
	}
	/**
	 * @param componentProperty the componentProperty to set
	 */
	public void setComponentProperty(String componentProperty) {
		this.componentProperty = componentProperty;
	}
	/**
	 * @return the componentTemplate
	 */
	public String getComponentTemplate() {
		return componentTemplate;
	}
	/**
	 * @param componentTemplate the componentTemplate to set
	 */
	public void setComponentTemplate(String componentTemplate) {
		this.componentTemplate = componentTemplate;
	}
	/**
	 * @return the componentUpdate
	 */
	public String getComponentUpdate() {
		return componentUpdate;
	}
	/**
	 * @param componentUpdate the componentUpdate to set
	 */
	public void setComponentUpdate(String componentUpdate) {
		this.componentUpdate = componentUpdate;
	}
	/**
	 * @return the componentPreview
	 */
	public String getComponentPreview() {
		return componentPreview;
	}
	/**
	 * @param componentPreview the componentPreview to set
	 */
	public void setComponentPreview(String componentPreview) {
		this.componentPreview = componentPreview;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CntrComponentListVo [applId=" + applId + ", componentName=" + componentName + ", componentNo="
				+ componentNo + ", componentProperty=" + componentProperty + ", componentTemplate=" + componentTemplate
				+ ", componentUpdate=" + componentUpdate + ", componentPreview=" + componentPreview + "]";
	}


	
}
