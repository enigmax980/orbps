package com.newcore.orbps.models.banktrans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private DateHelper(){
		
	}
	/**
	 * yyyy-MM-dd
	 */
	public static final String PATTERN_1 = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String PATTERN_2 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final String PATTERN_3 = "yyyy-MM-dd HH:mm:ss,SSS";
	/**
	 * yyyyMMddHHmmssSSS
	 */
	public static final String PATTERN_4 = "yyyyMMddHHmmssSSS";

	/**
	 * 把指定时间格式化成指定格式
	 * 
	 * @param date
	 *            要格式化的时间
	 * @param pattern
	 *            格式
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null || pattern == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 把当前时间格式化成指定格式
	 * 
	 * @param pattern
	 *            格式
	 * @return 格式化后的字符串
	 */
	public static String formatNow(String pattern) {
		if (pattern == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}

	/**
	 * 把指定格式的时间字符串格式化成时间类型
	 * 
	 * @param source
	 *            字符串
	 * @param patten
	 *            格式
	 * @return 格式化后的时间
	 * @throws ParseException
	 */
	public static Date formatString(String source, String pattern)
			throws ParseException {
		if (source == null || pattern == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(source);
	}

	/**
	 * 把java.util.Date转换成java.sql.Date
	 * 
	 * @param date
	 *            java.util.Date
	 * @return java.sql.Date
	 */
	public static java.sql.Date toSqlDate(Date date) {
		if (date == null)
			return null;
		return new java.sql.Date(date.getTime());
	}
}