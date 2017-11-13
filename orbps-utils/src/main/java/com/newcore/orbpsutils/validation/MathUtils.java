package com.newcore.orbpsutils.validation;

import java.math.BigDecimal;

/**
 * 常用的计算方法
 * @author zhenjw
 * on 2016-11-16
 */
public class MathUtils {

	/**
	 * 把两个double类型的数据转换为BigDecimal类型的数据进行加法操作，然后返回计算结果。
	 * @param d1 要参与运算的数据
	 * @param d2 要参与运算的数据
	 * @return 转为BigDecimal类型后进行加法运算的结果值
	 */
	public static double add(double d1,double d2)
	{
		BigDecimal bd1=BigDecimal.valueOf(d1);
		return bd1.add(BigDecimal.valueOf(d2)).doubleValue();
	}
	
	/**
	 * 把两个double类型的数据转换为BigDecimal类型的数据进行减法法操作，然后返回d1-d2的计算结果。
	 * @param d1 要参与运算的数据
	 * @param d2 要参与运算的数据
	 * @return 转为BigDecimal类型后进行减法运算的结果值(d1-d2)
	 */
	public static double substract(double d1,double d2)
	{
		BigDecimal bd1=BigDecimal.valueOf(d1);
		return bd1.subtract(BigDecimal.valueOf(d2)).doubleValue();
	}
}
