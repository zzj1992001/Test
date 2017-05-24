package nc.com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * 类型转换
 */
public class TypeConverter {
	
	private static final int timeStampLen = "2011-01-18 16:18:18".length();
	private static final String timeStampPattern = "yyyy-MM-dd HH:mm:ss";
	private static final String datePattern = "yyyy-MM-dd";
	
	
	public static final Object convert(Class<?> clazz, String s) throws ParseException {
		// mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
		if (clazz == String.class) {
			return ("".equals(s) ? null : s);	
		}
		s = s.trim();
		if ("".equals(s)) {	
			return null;
		}
		
		
		Object result = null;
		// mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
		if (clazz == Integer.class || clazz == int.class) {
			result = Integer.parseInt(s);
		}
		// mysql type: bigint
		else if (clazz == Long.class || clazz == long.class) {
			result = Long.parseLong(s);
		}
		else if (clazz == java.util.Date.class) {
        	if (s.length() >= timeStampLen) {	
        		// Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
        		// result = new java.util.Date(java.sql.Timestamp.valueOf(s).getTime());	// error under jdk 64bit(maybe)
        		result = new SimpleDateFormat(timeStampPattern).parse(s);
        	}
			else {
				// result = new java.util.Date(java.sql.Date.valueOf(s).getTime());	// error under jdk 64bit
				result = new SimpleDateFormat(datePattern).parse(s);
			}
        }
		// mysql type: date, year
        else if (clazz == java.sql.Date.class) {
        	if (s.length() >= timeStampLen) {	// if (x < timeStampLen) 鏀圭敤 datePattern 杞崲锛屾洿鏅鸿兘
        		// result = new java.sql.Date(java.sql.Timestamp.valueOf(s).getTime());	// error under jdk 64bit(maybe)
        		result = new java.sql.Date(new SimpleDateFormat(timeStampPattern).parse(s).getTime());
        	}
        	else {
        		// result = new java.sql.Date(java.sql.Date.valueOf(s).getTime());	// error under jdk 64bit
        		result = new java.sql.Date(new SimpleDateFormat(datePattern).parse(s).getTime());
        	}
        }
		// mysql type: time
        else if (clazz == java.sql.Time.class) {
        	result = java.sql.Time.valueOf(s);
		}
		// mysql type: timestamp, datetime
        else if (clazz == java.sql.Timestamp.class) {
        	result = java.sql.Timestamp.valueOf(s);
		}
		// mysql type: real, double
        else if (clazz == Double.class) {
        	result = Double.parseDouble(s);
		}
		// mysql type: float
        else if (clazz == Float.class) {
        	result = Float.parseFloat(s);
		}
		// mysql type: bit, tinyint(1)
        else if (clazz == Boolean.class) {
        	result = Boolean.parseBoolean(s) || "1".equals(s);
		}
		// mysql type: decimal, numeric
        else if (clazz == java.math.BigDecimal.class) {
        	result = new java.math.BigDecimal(s);
		}
		// mysql type: unsigned bigint
		else if (clazz == java.math.BigInteger.class) {
			result = new java.math.BigInteger(s);
		}
		// mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob. I have not finished the test.
        else if (clazz == byte[].class) {
			result = s.getBytes();
		}
		else {
			throw new RuntimeException(clazz.getName() + " can not be converted, please use other type of attributes in your model!");	
        }
		return result;
	}
}
