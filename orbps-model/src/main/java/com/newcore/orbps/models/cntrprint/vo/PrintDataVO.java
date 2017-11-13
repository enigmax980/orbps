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
public class PrintDataVO implements Serializable {
	private static final long serialVersionUID = 3845384294504493049L;

	@JSONField(ordinal = 1)
	private List<LabelDataVO> label;

	@JSONField(ordinal = 2)
	private List<TableListVO> tableList;

	public List<TableListVO> getTableList() {
		return tableList;
	}

	public void setTableList(List<TableListVO> tableList) {
		this.tableList = tableList;
	}

	public List<LabelDataVO> getLabel() {
		return label;
	}

	public void setLabel(List<LabelDataVO> label) {
		this.label = label;
	}

	public void addLabelData(LabelDataVO labelDataVO) {
		if (null == label) {
			label = new ArrayList<>();
		}
		label.add(labelDataVO);
	}

	public void addTableList(TableListVO so) {
		if (null == tableList) {
			tableList = new ArrayList<>();
		}
		tableList.add(so);
	}
}
