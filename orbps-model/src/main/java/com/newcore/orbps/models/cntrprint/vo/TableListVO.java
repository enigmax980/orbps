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
public class TableListVO implements Serializable {
	private static final long serialVersionUID = -481454452294472558L;

	@JSONField(ordinal = 1)
	private String tableName;
	@JSONField(ordinal = 2)
	private List<TableSonVO> tableSon;

	public void addTableSon(TableSonVO tableSonVO) {
		if (null == tableSon) {
			tableSon = new ArrayList<>();
		}
		tableSon.add(tableSonVO);
	}

	public String getTableName() {
		return tableName;
	}

	public List<TableSonVO> getTableSon() {
		return tableSon;
	}

	public void setTableSon(List<TableSonVO> tableSon) {
		this.tableSon = tableSon;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
