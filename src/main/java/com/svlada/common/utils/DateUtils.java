package com.svlada.common.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 */
public class DateUtils {

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_FORMAT_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String PART_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PART_DATE_FORMAT_CN = "yyyy年MM月dd日";
    public static final String YEAR_DATE_FORMAT = "yyyy";
    public static final String MONTH_DATE_FORMAT = "MM";
    public static final String DAY_DATE_FORMAT = "dd";
    public static final String WEEK_DATE_FORMAT = "week";
    public static final String FULL_DATE_FORMAT_ALL = "yyyyMMddHHmmss";

    /**
     * 将日期类型转换为字符串
     *
     * @param date    日期
     * @param xFormat 格式
     * @return
     */
    public static String getFormatDate(Date date, String xFormat) {
        date = date == null ? new Date() : date;
        xFormat = !StringUtils.isEmpty(xFormat) == true ? xFormat : FULL_DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
        return sdf.format(date);
    }

    /**
     * 获取当天的开始时间
     * @return
     */
    public static Date getStartTimeOfToday(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, ca.getActualMinimum(Calendar.HOUR_OF_DAY));
        ca.set(Calendar.MINUTE, ca.getActualMinimum(Calendar.MINUTE));
        ca.set(Calendar.SECOND, ca.getActualMinimum(Calendar.SECOND));
        Date time = ca.getTime();
        return time;
    }

    /**
     * 获取当天的结束时间
     * @return
     */
    public static Date getEndTimeOfToday(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, ca.getActualMaximum(Calendar.HOUR_OF_DAY));
        ca.set(Calendar.MINUTE, ca.getActualMaximum(Calendar.MINUTE));
        ca.set(Calendar.SECOND, ca.getActualMaximum(Calendar.SECOND));
        Date time = ca.getTime();
        return time;
    }


    /**
     * 获取当月的第一天
     * @return
     */
    public static Date getStartTimeOfMonth(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMinimum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, ca.getActualMinimum(Calendar.HOUR_OF_DAY));
        ca.set(Calendar.MINUTE, ca.getActualMinimum(Calendar.MINUTE));
        ca.set(Calendar.SECOND, ca.getActualMinimum(Calendar.SECOND));
        Date time = ca.getTime();
        return time;
    }

    /**
     * 获取当月的最后一天
     * @return
     */
    public static Date getEndTimeOfMonth(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, ca.getActualMaximum(Calendar.HOUR_OF_DAY));
        ca.set(Calendar.MINUTE, ca.getActualMaximum(Calendar.MINUTE));
        ca.set(Calendar.SECOND, ca.getActualMaximum(Calendar.SECOND));
        Date time = ca.getTime();
        return time;
    }

    /**
     * 日期运算
     * @param source
     * type: Calendar.YEAR Calendar.MONTH Calendar.DAY_OF_YEAR
     * @return
     */
    public static Date arithmetic(Date source,int type,int number){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(source);
        rightNow.add(type,number);
        Date dt1=rightNow.getTime();
        return dt1;
    }


}
