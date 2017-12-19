package com.svlada.common.utils.wx;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static final String FORMAT_YMD_HMS = "yyyyMMddHHmmss";
	public static final String FORMAT_YMD = "yyyyMMdd";
	public static final String FORMAT_YMD_HMS_ALL = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YM = "yyyy-MM";

	//根据格式获取当前时间
	public static String getCurrentTimeStr(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	
	public static String getTimeStr(Date date ,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	//当前时间戳（秒）
	//当前时间后多少分钟

	public static String getTimeStamp() {
		int time = (int) (System.currentTimeMillis() / 1000);
		return String.valueOf(time);
	}
	public static String getCurrentTimeNextMinute(int minute) {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, minute);
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_HMS);
		return sdf.format(nowTime.getTime());
	}
	

	public static Date getDate(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}
		return getDateStrTo(source,FORMAT_YMD_HMS_ALL);
	}

	// 字符串转日期
	public static Date getDateStrTo(String dateStr,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	// 获取当天下N个月时间
	public static Date getDateOfNextMonth(String dateStr, int serveral) {
		Date date = getDateStrTo(dateStr,FORMAT_YMD_HMS_ALL);
		return getDateOfNextMonth(date, serveral);
	}

	// 获取当天下N个月时间
	public static Date getDateOfNextMonth(Date date, int serveral) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, serveral);
		return c.getTime();
	}
	
	// 获取当天下N个天时间
	public static Date getDateOfNextDay(Date date, int serveral) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, serveral);
		return c.getTime();
	}

	
	//获取当前计费周期
	public static String getCycleStartDate(){
		Calendar calendar = Calendar.getInstance();
		int date = calendar.get(Calendar.DATE);
		Date currentDate = new Date();
		if (date > 26) {
			currentDate = getDateOfNextMonth(currentDate, 1);
		}
		return getTimeStr(currentDate,FORMAT_YM) + "-01";
	}
	
	//获取账单计费周期
	public static String getBillCycleStartDate(){
		Date currentDate = new Date();
		return getTimeStr(currentDate,FORMAT_YM) + "-01";
	}
	
}
