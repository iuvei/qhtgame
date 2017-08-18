package com.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/*
	 * 获取当前时间戮
	 * @return	Timestamp
	 */
	public static Timestamp getCurTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static Date getDate(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			//System.out.println(e.getMessage());
			return null;
		}
		
		return date;
	}
	
	/*
	 * 时间戮 ts 按 format格式转换成字符串
	 */
	public static String TimestampToString(Timestamp ts,String format) {
		String result = null;
		DateFormat sdf = new SimpleDateFormat(format);
		try {
			result = sdf.format(ts);
		} catch (Exception e) {   
			return null;
		}
		
		return result;
	}
	
	/*
	 * string转换成Timestamp
	 */
	public static Timestamp StringToTimestamp(String str) {
		Timestamp ts = null;
		try {
			ts = Timestamp.valueOf(str);
		} catch(Exception e) {
			ts = null;
		}
		return ts;
	}
	

	/*
	 * 得到某天的开始时间戮
	 * @param	date(int) 	YYYYMMDD
	 */
	public static Timestamp getStartTimestamp(int date) {
		int year = date/10000;
		int month = (date/100)%100;
		int day = date%100;
		
		String strDate = year+"-"+month+"-"+day+" 00:00:00.000";
		return Timestamp.valueOf(strDate);
	}
	
	/*
	 * 得到某天的结束时间戮
	 * @param	date(int) 	YYYYMMDD
	 */
	public static Timestamp getEndTimestamp(int date) {
		int year = date/10000;
		int month = (date/100)%100;
		int day = date%100;
		
		String strDate = year+"-"+month+"-"+day+" 23:59:59.999";
		return Timestamp.valueOf(strDate);
	}
	
	/*
	 * 根据时间戮得到日期
	 * @param	t(Timestamp)
	 * @return	day(int)	YYYYMMDD
	 */
	@SuppressWarnings("deprecation")
	public static int getDay(Timestamp t) {
		int year = 1900+t.getYear();
		int month = t.getMonth()+1;
		int day = t.getDate();
		
		return year*10000+month*100+day;
	}
	
	/*
	 * 根据时间戮得到日期
	 * @param	t(Timestamp)
	 * @return	day(String)	YYYY-MM-DD
	 */
	@SuppressWarnings("deprecation")
	public static String getDayString(Timestamp t) {
		int year = 1900+t.getYear();
		int month = t.getMonth()+1;
		int day = t.getDate();
		
		return year+"-"+month+"-"+day;
	}
	
	/*
	 * 比较两个时间戮的大小，按日期比较
	 */
	@SuppressWarnings("deprecation")
	public static int comparamDate(Timestamp t1, Timestamp t2) {
		int year1 = t1.getYear();
		int month1 = t1.getMonth();
		int day1 = t1.getDate();
		
		int year2 = t2.getYear();
		int month2 = t2.getMonth();
		int day2 = t2.getDate();
		
		if (year1<year2)
			return -1;
		else if (year1>year2)
			return 1;
		
		if (month1<month2)
			return -1;
		else if (month1>month2)
			return 1;
		
		if (day1<day2)
			return -1;
		else if (day1>day2)
			return 1;
		
		return 0;
	}
}

