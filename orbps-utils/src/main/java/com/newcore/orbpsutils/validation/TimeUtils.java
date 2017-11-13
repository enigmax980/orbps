package com.newcore.orbpsutils.validation;

import java.util.Date;

/**
 * 常用的计算方法
 * @author zhoushoubo
 * 
 */
public class TimeUtils {

	/**
	 * 计算两个日期的相差天数。
	 * @param 日期1
	 * @param 日期2
	 * @return 相差天数
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {

		if (null == fDate || null == oDate) {
			return -1;
		}

		long intervalMilli = oDate.getTime() - fDate.getTime();

		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}
}
