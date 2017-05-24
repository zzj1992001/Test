package nc.com.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import jodd.util.RandomStringUtil;

public class StringUtil {

	public static final String RAND_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	public static final String RAND_NUM_STR = "1234567890";
	public static final String BLANK_STR = "";
	public static final String NULL_STR = "NULL";
	
	public static String randStr(int len)
	{
		return RandomStringUtil.random(len,RAND_STR);
	}
	
	public static String randNumStr(int len) 
	{
		return RandomStringUtil.random(len, RAND_NUM_STR);
	}
	
	public static String concat(String...strs)
	{
		StringBuilder sb = new StringBuilder();
		for (String str : strs) 
		{
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static boolean isBlankOrNull(String str){
		if(str == null){
			return true;
		}
		if(BLANK_STR.equals(str.trim())){
			return true;
		}
		if(NULL_STR.equalsIgnoreCase(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 友好时间串
	 * @param time
	 * @return
	 */
	public static String friendlyDate(Date time){
	    int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);
	    if(ct < 3600){
	    	return Math.max(ct / 60,1) + "分钟前";
	    }else if(ct >= 3600 && ct < 86400){
	    	return ct/3600 + "小时前";
	    }else if(ct >= 86400 && ct < 2592000){
	    	int day = ct / 86400 ;   
	    	return (day>1)? day + "天前" : "昨天";
	    }else{
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	return sdf.format(time);
	    }
	}
	
	/**
	 * 手机号规则验证
	 */
	public static boolean isMobileNumber(String mobiles) {
	    return Pattern
	            .compile("^((17[0-9])|(13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}")
	            .matcher(mobiles).matches();
	}

	/**
	 * 随机创建唯一字符串
	 */
	public static String randomUniqueStr() 
	{
		return UUID.randomUUID().toString() + new Random().nextInt(1000);
	}

	public static String uniqueStrWithDateTime()
	{
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		String conector = "_";
		String timeDesc = DateUtil.dateFormat("yyyyMMddHHmmss", new Date());
		sb.append(timeDesc)
		  .append(conector)
		  .append(System.currentTimeMillis())
		  .append(conector)
		  .append(random.nextInt(10000))
		  .append(conector)
		  .append(random.nextInt(10000))
		  .append(conector)
		  .append(random.nextInt(10000));
		return sb.toString();
	}
	
	/**
	 * 生成新的文件名
	 */
	public static String appendExtraToFileName(String fileName, String extra) 
	{
		int index = fileName.lastIndexOf(".");
		String name = fileName.substring(0, index);
		String ext = fileName.substring(index);
		return new StringBuilder().append(name).append(extra).append(ext).toString();
	}
	
	/**
	 * 给文件名添加随即字符串
	 */
	public static String generateNewName(String fileName) 
	{
		Random random = new Random();
		String subName = new StringBuffer().append(random.nextInt(1000))
										   .append("_")
										   .append(random.nextInt(1000))
										   .append("_")
										   .append(random.nextInt(1000))
										   .toString();
		return appendExtraToFileName(fileName, subName);
	}
	
	public static String join(Iterator<?> iter,String sep){
		if(iter == null){
			return "";
		}
		if(sep == null){
			sep = "";
		}
		StringBuilder sb = new StringBuilder();
		while(iter.hasNext()){
			sb.append(jodd.util.StringUtil.toSafeString(iter.next())).append(sep);
		}
		return sb.substring(0, sb.length()-sep.length());
	}
	
	public static double convertToDouble(String value,double defaultValue){
		if(jodd.util.StringUtil.isBlank(value)){
			return defaultValue;
		}
		double tmp = defaultValue;
		try{
			tmp = Double.valueOf(value);
		}catch(Exception e){
			tmp = defaultValue;
		}
		return tmp;
	}

	public static String setEmptyToNull(String str) 
	{
		if (str != null && str.trim().isEmpty()) 
		{
			return null;
		}
		return str;
	}

	public static String formatMoney(Double money) 
	{
		if (money == null || (money >= 0.0 && money <= 0.0)) 
		{
			return "0.00";
		}
		
		Long newMoney = (long)Math.floor(money * 100);
		String moneyStr = new StringBuilder(newMoney.toString()).reverse().toString();
		
		StringBuilder builder = new StringBuilder();
		
		while (moneyStr.length() < 3) 
		{
			moneyStr += "0";
		}
		for (int i = 0 ; i < moneyStr.length(); i++) 
		{
			builder.append(moneyStr.charAt(i));
			if (i == 1) 
			{
				builder.append(".");
			}
			if (i % 3 == 1 && i >= 4) {
				builder.append(",");
			}
		}
		builder = builder.reverse();
		String value = builder.toString();
		return value.startsWith(",") ? value.replaceFirst(",", "") : value;
	}
	
	public static String moneyToStr(double money){
		NumberFormat format = NumberFormat.getInstance();
		format.setRoundingMode(RoundingMode.FLOOR);
		format.setGroupingUsed(false);
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		return format.format(money);
	}
	
	public static void main(String[] args) {
		System.out.println(formatMoney(0.01));
		System.out.println(formatMoney(0.1));
		System.out.println(formatMoney(1.01));
		System.out.println(formatMoney(1000.01));
		System.out.println(formatMoney(0.00));
		System.out.println(formatMoney(1.00));
		System.out.println(formatMoney(10.00));
		System.out.println(formatMoney(100.00));
		System.out.println(formatMoney(1000.00));
		System.out.println(formatMoney(10000.00));
		System.out.println(formatMoney(100000.00));
		System.out.println(formatMoney(1000000.00));
		System.out.println(formatMoney(10000000.00));
		System.out.println(formatMoney(100000000.00));
	}

	/**
	 * @return str(length)...
	 */
	public static String sub(String str, int length) 
	{
		if (str == null || str.length() <= length) 
		{
			return str;
		}
		return str.substring(0, length - 1) + "...";
	}
	
	public static String dateFormat(Date date, String pattern) {
		if(date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date convertToDate(String date, String pattern) {
		if(jodd.util.StringUtil.isBlank(date)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date tDate = null;
		try {
			tDate = sdf.parse(date);
		} catch (ParseException e) {
		}
		return tDate;
	}
}
