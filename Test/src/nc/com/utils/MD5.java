package nc.com.utils;

import java.security.MessageDigest;

/**
 * MD5不可逆加密
 */
public class MD5 {
	
	/**
	 * 默认使用uft8字符集加密
	 */
	public final static String encrptMD5(String plainText) {
		 return encrptMD5(plainText, "utf8");
	 }
	
	 public final static String encrptMD5(String plainText,String charEncode) {
		 if(plainText == null){
			 return null;
		 }
		 char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
				    'A', 'B', 'C', 'D', 'E', 'F' };  
		  try {  
			   byte[] strTemp = plainText.getBytes(charEncode);  
			   MessageDigest mdTemp = MessageDigest.getInstance("MD5");  
			   mdTemp.update(strTemp);  
			   byte[] md = mdTemp.digest();  
			   int j = md.length;  
			   char str[] = new char[j * 2];  
			   int k = 0;  
			   for (int i = 0; i < j; i++) {  
			    byte byte0 = md[i];  
			    str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
			    str[k++] = hexDigits[byte0 & 0xf];  
			   }  
			   return new String(str);  
		  } catch (Exception e) {  
			  return null;  
		  }  
	 }
	 
}
