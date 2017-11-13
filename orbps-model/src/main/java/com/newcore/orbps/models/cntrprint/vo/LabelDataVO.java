/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author chenyc Created on 16-8-13
 */
public class LabelDataVO implements Serializable {
	private static final long serialVersionUID = -2315328789262470489L;

	@JSONField(ordinal = 1)
	private String labelName;

	@JSONField(ordinal = 2)
	private String labelValue;

	public LabelDataVO() {
		// use for auto instance
	}

	public LabelDataVO(String labelName, String labelValue) {
		this.labelName = labelName;

		if (null != labelValue) {
			this.labelValue = labelValue;

		} else {
			this.labelValue = "";
		}
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelValue() {
		return labelValue;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}
}
