package nc.com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import jodd.util.Base64;


public class EncryptUtil {
	
	
	public static String encrypt(String data, String key) 
	{
		char[] contents = data.toCharArray();
		char[] keyChars = key.toCharArray();
		StringBuffer sb = new StringBuffer();
		
		for(int i=0,j=0; i<contents.length; i++,j++) 
		{
			if(j>=keyChars.length) 
			{
				j = 0;
			}
			sb.append((char)(contents[i]^keyChars[j]));
		}
		
		return sb.toString();
	}
	
	
	public static String decrypt(String data, String key) 
	{
		if(data == null || "".equals(data))
		{
			return "";
		}
		
		char[] encodeChars = data.toCharArray();
		char[] keyChars = key.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i = 0, j = 0; i < encodeChars.length; i++, j++) 
		{
			if(j >= keyChars.length) 
			{
				j = 0;
			}
			
			sb.append((char)(encodeChars[i]^keyChars[j]));
		}
		return sb.toString();
	}
	
	public static String urlEncode(String str)
	{
		try {
			return URLEncoder.encode(str, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String urlDecode(String str)
	{
		try {
			return URLDecoder.decode(str, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 简单异或加密
	 * @param code 325
	 */
	public static byte[] decode(String str, int code) 
	{
		byte[] charArray = null;
		try {		
			charArray = str.getBytes("UTF-8");
			for (int i = 0; i < charArray.length; i++) {
				charArray[i] = (byte) (charArray[i] ^ code);
			}
			return charArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decodeBase64(byte[] s)
	{
		byte[] encodeBase64 = Base64.decode(s);
		try {
			return new String(encodeBase64, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encodeBase64(byte[] data) 
	{
		return Base64.encodeToString(data);
	}
	
	public static String encodeBase64(String data) 
	{
		return Base64.encodeToString(data);
	}
}
