/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint.vo;

import java.io.Serializable;

/**
 * @author chenyc Created on 16-8-13
 */
public class PrintVO implements Serializable {
	private static final long serialVersionUID = -8580058586901616278L;

	public static final String SALES_DEVELOP_FLAG = "销售人员是否共同展业标识不正确";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy年MM月dd日";
	public static final String DATE_FORMAT_MMDD = "MM月dd日";
	public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyy年MM月dd日 HH时mm分";
	public static final String DATE_FORMAT_SIMPLEYYYYMMDD = "yyyy/MM/dd";

	private EPrintVO ePrint;

	public EPrintVO getePrint() {
		return ePrint;
	}

	public void setePrint(EPrintVO ePrint) {
		this.ePrint = ePrint;
	}

}
