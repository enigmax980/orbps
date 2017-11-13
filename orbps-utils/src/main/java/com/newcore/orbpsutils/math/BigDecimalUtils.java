package com.newcore.orbpsutils.math;

import java.math.BigDecimal;

/**
 * Created by liushuaifeng on 2017/2/21 0021.
 */
public class BigDecimalUtils {
	
	
   /**
     * 保存精确度
     * @param number  数据
     * @param precision 保存的小数点位数
     * @return
     * Double 类型
     */
    public static Double keepPrecisionDouble(double number, int precision) {
        BigDecimal bg = BigDecimal.valueOf(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 保存精确度
     * @param number  数据
     * @param precision 保存的小数点位数
     * @return
     * BigDecimal 类型
     */
    public static BigDecimal keepPrecision(double number, int precision) {
        BigDecimal bg = BigDecimal.valueOf(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 保存精确度
     * @param 
     * 	number  数据
     * @param 
     * 	precision 保存的小数点位数
     * @return
     *  BigDecimal 类型
     */
    public static BigDecimal keepPrecision(BigDecimal number, int precision) {
        return number.setScale(precision, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 比较两个double类型数据大小
     * @param a
     * @param b
     * @return 
     * 	a与b相比：大于返回1,等于返回0,小于返回-1
     */
    public static int compareDouble(Double a,Double b){
    	BigDecimal bigA = BigDecimal.valueOf(a);
		BigDecimal bigB = BigDecimal.valueOf(b);
		return bigA.compareTo(bigB);
    }
    
    /**
     * 比较两个BigDecimal类型数据大小
     * @param bigA
     * @param bigB
     * @return 
     * 	bigA与bigB相比：大于返回1,等于返回0,小于返回-1
     */
    public static int compareBigDecimal(BigDecimal bigA,BigDecimal bigB){
        return bigA.compareTo(bigB);
    }  
}
