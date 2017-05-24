package nc.com.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用日期函数工具类
 */
public class DateUtil {
	
	public final static String REG_YYMMDD = "yyyy-MM-dd";
	public final static String REG_YYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public final static String REG_YYMMDDMM = "yyyy-MM-dd HH:mm";
	
	public static final Integer MAX_HOUR = 23;
	public static final Integer MAX_MINUTE = 59;
	public static final Integer MAX_SECOND = 59;
	public static final Integer MAX_MILLSECOND = 999;
	
	public static final Integer MIN_HOUR = 0;
	public static final Integer MIN_MINUTE = 0;
	public static final Integer MIN_SECOND = 0;
	public static final Integer MIN_MILLSECOND = 0;
	
	public static final Map<Integer, String> SOCIAL_TIME_MAP;
	static{
		SOCIAL_TIME_MAP = new HashMap<Integer, String>();
		SOCIAL_TIME_MAP.put(0, "今天");
		SOCIAL_TIME_MAP.put(-1, "昨天");
		SOCIAL_TIME_MAP.put(-2, "前天");
	}
	
	/**
	 * 日期格式化
	 * @param reg   日期格式化参数
	 * @param date  日期
	 * @return
	 */
	public static String dateFormat(String reg,Date date){
		DateFormat sdf = getDateFormat(reg);
		return sdf.format(date);
	}
	/**
	 * 默认日期格式化  格式为 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date){
		if(date == null){
			return "";
		}
		return dateFormat(REG_YYMMDD,date);
	}
	
	
	public static String dateFormatmm(Date date){
		if(date == null){
			return "";
		}
		return dateFormat(REG_YYMMDDMM,date);
	}
	   
	public static Date firstDayOfMonth(int year, int month) 
	{     
		Calendar cal = Calendar.getInstance();     
		cal.set(Calendar.YEAR, year);     
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DATE));  
		return cal.getTime();  
	}
	  
	public static Date lastDayOfMonth(int year, int month) {     
		Calendar cal = Calendar.getInstance();     
		cal.set(Calendar.YEAR, year);     
		cal.set(Calendar.MONTH, month);  
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DATE));  
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}  
	   
	   
	 public  static List<Date> getDates(int year,int month){  
		        List<Date> dates = new ArrayList<Date>();  
		           
		         Calendar cal = Calendar.getInstance();  
		         cal.set(Calendar.YEAR, year);  
		        cal.set(Calendar.MONTH,  month-1);  
		         cal.set(Calendar.DATE, 1);  
		           
		          
		        while(cal.get(Calendar.YEAR) == year &&   
		                cal.get(Calendar.MONTH) < month){  
		            //int day = cal.get(Calendar.DAY_OF_WEEK);  
		               
		            
		                dates.add((Date)cal.getTime().clone());  
		           
		           cal.add(Calendar.DATE, 1);  
		         }  
		        return dates;  
		   
		     } 
	
	public static String dateFormatForTime(Date date){
		return dateFormat(REG_YYMMDD_HHMMSS, date);
	}
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendlyTime(Date time) {
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = getDateFormat();
		//判断是否是同一天
		String curDate = dateFormat.format(cal.getTime());
		String paramDate = dateFormat.format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0){
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			}else{ 
				ftime = hour+"小时前";
			}
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0){
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			}else{ 
				ftime = hour+"小时前";
			}
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormat.format(time);
		}
		return ftime;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(Date time){
		boolean b = false;
		Date today = new Date();
		DateFormat dateFormat = getDateFormat();
		if(time != null){
			String nowDate = dateFormat.format(today);
			String timeDate = dateFormat.format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 获取日期格式化对象
	 * @return
	 */
	public static DateFormat getDateFormat(){
		return getDateFormat(REG_YYMMDD);
	}
	/**
	 * 获取日期格式化对象
	 * @param reg
	 * @return
	 */
	public static DateFormat getDateFormat(String reg){
		return new SimpleDateFormat(reg);
	}
	
	/**
	 * 获取毫秒
	 * @param reg
	 * @param date
	 * @return
	 */
	public static long getMilliseconds(String reg,String date){
		long milliseconds = 0;
		try {
			DateFormat df = getDateFormat(reg);
			Date time = df.parse(date);
			milliseconds = time.getTime();
		} catch (ParseException e) {
			milliseconds = 0;
		}
		return milliseconds;
	}
	/**
	 * 获取秒
	 * @param reg
	 * @param date
	 * @return
	 */
	public static int getSeconds(String reg,String date){
		int seconds = 0;
		if(date == null || "".equals(date.trim())){
			return seconds;
		}
		try {
			DateFormat df = getDateFormat(reg);
			Date time = df.parse(date);
			seconds = new Long(time.getTime()/1000).intValue();
		} catch (ParseException e) {
			seconds = 0;
		}
		return seconds;
	}
	public static int getSeconds(String reg,Date date){
		int seconds = 0;
		if(date == null){
			return seconds;
		}
		seconds = new Long(date.getTime()/1000).intValue();
		return seconds;
	}
	/**
	 * 获取当前日期
	 * @param reg
	 * @return
	 */
	public static String getNowTimeFormat(String reg){
		return dateFormat(reg, new Date());
	}
	public static int getNowSeconds(){
		return getSeconds(REG_YYMMDD_HHMMSS, new Date());
	}
	public static Object secondsToDate(int seconds,String reg) {
		return dateFormat(reg, new Date(seconds*1000L));
	}
	
	public static int compare(Date first, Date second) {
		if(first == null && second == null){
			return 0;
		}
		if(first == null && second != null){
			return -1;
		}
		if(first != null && second == null){
			return 1;
		}
		return first.compareTo(second);
	}
	
	public static TimeRange getTimeRangeOfDay(int afterDayNum) 
	{
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.add(Calendar.DAY_OF_YEAR, afterDayNum);
		startCalendar.set(Calendar.HOUR_OF_DAY, startCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		startCalendar.set(Calendar.MINUTE, startCalendar.getActualMinimum(Calendar.MINUTE));
		startCalendar.set(Calendar.SECOND, startCalendar.getActualMinimum(Calendar.SECOND));
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.add(Calendar.DAY_OF_YEAR, afterDayNum);
		endCalendar.set(Calendar.HOUR_OF_DAY, startCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, startCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, startCalendar.getActualMaximum(Calendar.SECOND));
		
		return new TimeRange(startCalendar.getTime(), endCalendar.getTime());
	}
	
	public static Date setTime(Date endDate, Integer hour,
			Integer minute, Integer second)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	
	public static Calendar getCalendar(Date date) 
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	
	/**
	 * @param month from 1 to 12
	 * @return
	 */
	public static List<Integer> getDaysBetweenOfMonth(Integer month, Date startDate, Date endDate) 
	{
		List<Integer> days = new ArrayList<Integer>();
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		int targetMonth = month - 1;
		
		if (startCal.get(Calendar.MONTH) <= targetMonth) 
		{
			while (startCal.get(Calendar.MONTH) <= targetMonth && !startCal.after(endCal)) 
			{
				if(startCal.get(Calendar.MONTH) == targetMonth)
				{
					days.add(startCal.get(Calendar.DAY_OF_MONTH));
				}
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		return days;
	}
	
	
	/**
	 * @param year
	 * @param month from 1 to 12
	 * @param dayOfMonth
	 */
	public static TimeRange getTimeRangeOfDate(Integer year, Integer month, Integer dayOfMonth) 
	{
		Calendar currentDay = Calendar.getInstance();
		currentDay.set(Calendar.YEAR, year);
		currentDay.set(Calendar.MONTH, month - 1);
		currentDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		currentDay.set(Calendar.HOUR_OF_DAY, currentDay.getActualMaximum(Calendar.HOUR_OF_DAY));
		currentDay.set(Calendar.MINUTE, currentDay.getActualMaximum(Calendar.MINUTE));
		currentDay.set(Calendar.SECOND, currentDay.getActualMaximum(Calendar.SECOND));
		
		Date endTime = currentDay.getTime();
		
		currentDay.set(Calendar.HOUR_OF_DAY, currentDay.getActualMinimum(Calendar.HOUR_OF_DAY));
		currentDay.set(Calendar.MINUTE, currentDay.getActualMinimum(Calendar.MINUTE));
		currentDay.set(Calendar.SECOND, currentDay.getActualMinimum(Calendar.SECOND));
		
		Date startTime = currentDay.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	public static TimeRange getTimeRangeOf(Date date) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal = setTimeToZero(cal);
		cal.set(Calendar.MILLISECOND, MIN_MILLSECOND);
		Date startTime = cal.getTime();
		
		cal = setTimeToMax(cal);
		cal.set(Calendar.MILLISECOND, MAX_MILLSECOND);
		Date endTime = cal.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	public static TimeRange getTimeRangeOfToday()
	{
		Calendar currentDay = Calendar.getInstance();
		currentDay.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		currentDay.set(Calendar.MINUTE, MIN_MINUTE);
		currentDay.set(Calendar.SECOND, MIN_SECOND);
		
		Date startTime = currentDay.getTime();
		
		currentDay.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		currentDay.set(Calendar.MINUTE, MAX_MINUTE);
		currentDay.set(Calendar.SECOND, MAX_SECOND);
		
		Date endTime = currentDay.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	
	/**
	 * @param month 1..12
	 * @return
	 */
	public static TimeRange getTimeRangeOfMonth(int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		
		Date startTime = cal.getTime();
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		cal.set(Calendar.MINUTE, MAX_MINUTE);
		cal.set(Calendar.SECOND, MAX_SECOND);
		
		Date endTime = cal.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	/**
	 * @param month 1..12
	 * @return
	 */
	public static TimeRange getTimeRangeOfMonth(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		
		Date startTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		cal.set(Calendar.MINUTE, MAX_MINUTE);
		cal.set(Calendar.SECOND, MAX_SECOND);
		
		Date endTime = cal.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	
	public static TimeRange getOfCurrentWeek() 
	{
		Calendar calendar = Calendar.getInstance();
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, 7 - dayOfWeek);
		calendar.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		calendar.set(Calendar.MINUTE, MAX_MINUTE);
		calendar.set(Calendar.SECOND, MAX_SECOND);
		Date end = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -6);
		calendar.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		calendar.set(Calendar.MINUTE, MIN_MINUTE);
		calendar.set(Calendar.SECOND, MIN_SECOND);
		Date start = calendar.getTime();
		return new TimeRange(start, end);
	}
	
	
	public static TimeRange timeRangeOfCurMonth()
	{
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		
		Date startTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		cal.set(Calendar.MINUTE, MAX_MINUTE);
		cal.set(Calendar.SECOND, MAX_SECOND);
		
		Date endTime = cal.getTime();
		
		return new TimeRange(startTime, endTime);
	}
	
	
	/**
	 * 获取当前月
	 */
	public static Integer getCurrentMonth() 
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}
	
	
	public static List<Integer> getMonths() {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			list.add(i);
		}
		return list;
	}
	
	/**
	 * 创建某个月的日历
	 * month 1..12
	 */
	public static List<Calendar> createCalsOf(int year, int month) 
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		
		int minDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<Calendar> cals = new ArrayList<Calendar>();
		for (int dayOfMonth = minDay; dayOfMonth <= maxDay; dayOfMonth++) 
		{
			Calendar newCal = (Calendar)cal.clone();
			newCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			cals.add(newCal);
		}
		return cals;
	}
	
	/**
	 * 字符串转化为Date
	 * dateStr 格式为： yyyy-MM-dd HH:mm:ss
	 */
	public static Date getDateOf(String dateStr) 
	{
		return getDateOf(dateStr, REG_YYMMDD_HHMMSS);
	}
	
	/**
	 * 字符串转化为Date
	 */
	public static Date getDateOf(String dateStr, String patterm) 
	{
		SimpleDateFormat format = new SimpleDateFormat(patterm);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取当天的时间
	 * @param hhmm  格式为，HH:mm，比如： 22:30
	 */
	public static Date getTodayTimeOf(String hhmm)
	{
		if(hhmm == null || "".equals(hhmm))
		{
			return null;
		}
		String[] pies = hhmm.split(":");
		if(pies.length != 2)
		{
			return null;
		}
		int hour = Integer.parseInt(pies[0]);
		int minite = Integer.parseInt(pies[1]);
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, hour);
		now.set(Calendar.MINUTE, minite);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}
	
	/**
	 * @return 
	 * 今年：MM-dd HH:mm
	 * 其它：yy年 MM-dd HH:mm
	 */
	public static String socialTime(Date date) 
	{
		Calendar cal = Calendar.getInstance();
		int currentDay = cal.get(Calendar.DAY_OF_YEAR);
		int currentYear = cal.get(Calendar.YEAR);
		
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		
		SimpleDateFormat format = new SimpleDateFormat();
		String yearStr = "";
		String dateStr = null;
		if (year == currentYear) 
		{
			int dayNum = day - currentDay;
			dateStr = SOCIAL_TIME_MAP.get(dayNum);
		}else {
			format.applyPattern("yy年 ");
			yearStr = format.format(date);
		}
		if (dateStr == null)
		{
			format.applyPattern("MM-dd");
			dateStr = format.format(date);
		}
		format.applyPattern(" HH:mm");
		String timeStr = format.format(date);

		return new StringBuilder().append(yearStr).append(dateStr).append(timeStr).toString();
	}
	
	public static String toHHmm(int millSeconds)
	{
		int minutes = millSeconds / (1000 * 60);
		
		Integer minute = minutes % 60;
		Integer hour = minutes / 60;
		String hourStr = hour < 10 ? "0" + hour.toString() : hour.toString();
		String minuteStr = minute < 10 ? "0" + minute.toString() : minute.toString();
		
		return String.format("%s:%s", hourStr, minuteStr);
	}
	
	/**
	 * @return 今天／昨天／前天／ yyyy-MM-dd
	 */
	public static String toDay(Date date) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		cal.add(Calendar.DAY_OF_MONTH, 15);
		
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		now.set(Calendar.MINUTE, MIN_MINUTE);
		now.set(Calendar.SECOND, MIN_SECOND);
		
		if (!now.after(now)) 
		{
			cal.add(Calendar.DAY_OF_MONTH, -15);
			long millis = now.getTimeInMillis() - cal.getTimeInMillis();
			long day = millis / (1000 * 60 * 60 * 24);
			
			if (day == 0) 
			{
				return "今天";
			}
			if (day == 1) 
			{
				return "昨天";
			}
			if (day == 2) 
			{
				return "前天";
			}
			if (day <= 15) 
			{
				return day + "天前";
			}
		}
		
		return dateFormat(date);
	}
	
	
	/**
	 * @return
	 * 三天内：今天/昨天/前天 HH:mm
	 * 今年：MM-dd HH:mm
	 * 其它：yy年 MM-dd HH:mm
	 */
	public static String matchTime(Date date) 
	{
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(" HH:mm");
		String hhmm = format.format(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal = setTimeToZero(cal);
		
		Calendar now = Calendar.getInstance();
		now = setTimeToZero(now);
		
		long days = daysBetween(now.getTime(), cal.getTime());
		String mmdd = "";
		if (days <= 0) 
		{
			mmdd = "今天";
		}else if (days <= 1) 
		{
			mmdd = "昨天";
		}else if (days <= 2) 
		{
			mmdd = "前天";
		}else 
		{
			format.applyPattern("MM-dd");
			mmdd = format.format(date);
		}
		
		String yy = "";
		if (now.get(Calendar.YEAR) != cal.get(Calendar.YEAR)) 
		{
			format.applyPattern("yyyy-");
			yy = format.format(date);
		}
		return new StringBuilder().append(yy).append(mmdd).append(hhmm).toString();
	}
	
	
	public static Integer calAge(Date birthday) 
	{
		Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException("出生时间大于当前时间!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) 
        {
            if (monthNow == monthBirth) 
            {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else 
            {
                age--;
            }
        }
        return age;
	}
	
	public static Calendar setTimeToZero(Date date) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		return cal;
	}
	
	private static Calendar setTimeToZero(Calendar cal) 
	{
		cal.set(Calendar.HOUR_OF_DAY, MIN_HOUR);
		cal.set(Calendar.MINUTE, MIN_MINUTE);
		cal.set(Calendar.SECOND, MIN_SECOND);
		return cal;
	}
	
	public static Calendar setTimeToMax(Calendar cal) 
	{
		cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		cal.set(Calendar.MINUTE, MAX_MINUTE);
		cal.set(Calendar.SECOND, MAX_SECOND);
		return cal;
	}
	
	public static Date setTimeToMax(Date date) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
		cal.set(Calendar.MINUTE, MAX_MINUTE);
		cal.set(Calendar.SECOND, MAX_SECOND);
		return cal.getTime();
	}
	
	/**
	 * @return dateA - dateB
	 */
	private static long daysBetween(Date dateA, Date dateB)
	{
		long millis = dateA.getTime() - dateB.getTime();
		
		long days = millis / (1000 * 60 * 60 * 24);
		
		return days;
	}
	
	public static Date getTodayTimeOf(String timeStr, String pattern) 
	{
		Date date = DateUtil.getDateOf(timeStr, pattern);
		
		Calendar setCal = Calendar.getInstance();
		setCal.setTime(date);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, setCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, setCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, setCal.get(Calendar.SECOND));
		return cal.getTime();
	}
	
}
