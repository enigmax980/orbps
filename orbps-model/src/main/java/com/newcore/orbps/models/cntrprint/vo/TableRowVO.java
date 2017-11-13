/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyc Created on 16-8-13
 */
public class TableRowVO implements Serializable {
	private static final long serialVersionUID = -481454452294472558L;

	@JSONField(ordinal = 1)
	private List<LabelDataVO> label;

	public List<LabelDataVO> getLabel() {
		return label;
	}

	public void setLabel(List<LabelDataVO> label) {
		this.label = label;
	}

	public void addLabel(LabelDataVO labelDataVO) {
		if (null == label) {
			label = new ArrayList<>();
		}
		label.add(labelDataVO);
	}

}
