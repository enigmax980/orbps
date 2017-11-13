package com.newcore.orbpsutils.math;

import com.halo.core.exception.BusinessException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liushuaifeng on 2017/2/13 0013.
 */
public class DateUtils {

    private final static Integer PASS_COUNT_LENGTH = 9;

    /**
     * 功能描述:计算任意一天,经过指定的月/日/年数后的日期。
     *
     * @param lstrBeginDate :起始日期，格式为"YYYY-MM-DD"
     * @param llPassCount   :经过的年月日数
     * @param lstrFlag      :标志 年-"Y"、月-"M"、日-"D"
     * @return 返回的日期
     */
    public static Date computeDate(Date lstrBeginDate, int llPassCount, String lstrFlag) {

        //判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
        //日历
        Calendar c = Calendar.getInstance();
        //设置时间为 输入的时间
        c.setTime(lstrBeginDate);

        //判断输入数字的长度
        Integer numLen = llPassCount;
        if (numLen.toString().length() <= PASS_COUNT_LENGTH) {
            //判断标志，判断输入数据是 年或是 月 或是日。年-"Y"、月-"M"、日-"D"。
            if ("Y".equals(lstrFlag)) {
                c.add(Calendar.YEAR, llPassCount); // 目前時間加
                return computeDate(c.getTime(), 0, "D");
            } else if ("M".equals(lstrFlag)) {
                c.add(Calendar.MONTH, llPassCount); // 目前時間加
                return c.getTime();
            } else if ("D".equals(lstrFlag)) {
                c.add(Calendar.DAY_OF_WEEK, llPassCount); // 目前時間加
                return c.getTime();
            } else {
                throw new BusinessException(new Throwable("标志输入错误，请重新输入！"));
            }
        } else {
            throw new BusinessException(new Throwable("输入跨越条数的长度超过" + PASS_COUNT_LENGTH + "位"));
        }
    }

    /**
     * @param lstrBeginDate 输入日期
     * @param count         相对天数
     * @return 返回的日期
     */
    public static Date getRelativeDate(Date lstrBeginDate, int count) {
        //判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
        //格式化日期
        Calendar c = Calendar.getInstance();
        //设置时间为 输入的时间
        c.setTime(lstrBeginDate);

        //判断输入数字的长度
        Integer a1 = count;
        if (a1.toString().length() <= PASS_COUNT_LENGTH) {
            c.add(Calendar.DAY_OF_WEEK, count); // 目前時間加
            return c.getTime();
        } else {
            throw new BusinessException(new Throwable("输入跨越条数的长度超过" + PASS_COUNT_LENGTH + "位"));
        }
    }

    
    /**
     * 获得新日期
     * @param 
     * 	date 日期
     * @param 
     * 	num 数值
     * @param 
     * 	unit 单位：Y-年，M-月，D-天
     * @return
     *  NEW DATE
     */
    public static Date getNewDate(Date date,int num ,String unit){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (unit) {
		case "Y":
			calendar.add(Calendar.YEAR,num);
			break;
		case "M":
			calendar.add(Calendar.MONTH,num);
			break;
		default:
		    calendar.add(Calendar.DATE,num);
			break;
		}
        return calendar.getTime();
    }
   
    /**
     * 获取日期年份
     * @param date 日期
     * @return YYYY
     */
    public static int getYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }


    /**
     * 格式化日期格式
     * @return
     *   yy-MM-dd HH:mm:ss
     */
    public static String getFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
    
    /**
	 * 日期格式转换
	 * @param 
	 * java.util.Date
	 * @return
	 * java.sql.Timestamp
	 */
    public static Timestamp getTimestamp(Date utilDate){
    	if(utilDate == null){
			return null;
		}
		Timestamp tmp =  new Timestamp(utilDate.getTime());
		return tmp;
	}

    /**
     * 日期格式转换
     * @param 
     * java.util.Date
     * @return
     * java.sql.Date
     */
	public static java.sql.Date getDate(Date utilDate) {
		if(utilDate == null){
			return null;
		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String fDate = format.format(utilDate);
		java.sql.Date date = java.sql.Date.valueOf(fDate);
		return date;
	}
}
