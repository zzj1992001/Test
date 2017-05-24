package nc.com.kits;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;




public class HttpRequestKit {

	private static final String AJAX_HEADER_NAME = "x-requested-with";
	
	/**
	 * 判断ajax请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request)
	{
		String val = request.getHeader(AJAX_HEADER_NAME);
		return val != null && !val.equals("");
	}
	
	
	public static boolean isMutipartForm(HttpServletRequest request) 
	{
		return request != null 
				&& request.getContentType() != null 
				&& request.getContentType().contains("multipart/form-data");
	}
	
	
	public static boolean isUrlStartWith(HttpServletRequest request, String urlPrefix)
	{
		return request.getServletPath().startsWith(urlPrefix);
	}
	
	public static String currrentUrl(HttpServletRequest requst) 
	{
		if(requst.getQueryString() != null && !requst.getQueryString().equals(""))
		{
			return requst.getRequestURI() + "?" + requst.getQueryString();
		}
		
		return requst.getRequestURI();
	}
	
	public static String encodeUtf8(String currentUrl) 
	{
		try {
			return URLEncoder.encode(currentUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decodeUtf8(String url) 
	{
		try {
			return URLDecoder.decode(url, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String removeServletContexName(String currentUrl, HttpServletRequest request) 
	{
		return currentUrl.replaceFirst(request.getServletContext().getContextPath(), "");
	}
	
	public static String readStream(HttpServletRequest request)
	{
		byte[] bytes = new byte[1024 * 1024];
		try {
			InputStream is = request.getInputStream();
			int nRead = 1;
			int nTotalRead = 0;
			while (nRead > 0) 
			{
				nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
				if (nRead > 0)
				{
					nTotalRead = nTotalRead + nRead;
				}
			}
			return new String(bytes, 0, nTotalRead, "utf-8");	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void readUrlToFile(String fromUrl, String toFilePath) 
	{
		try {
			URL url = new URL(fromUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(15 * 1000);  
	        InputStream inStream = conn.getInputStream();  
	        byte[] data = readInputStream(inStream);  
	        File imageFile = new File(toFilePath);  
	        FileOutputStream outStream = new FileOutputStream(imageFile);  
	        outStream.write(data);  
	        outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception
	{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1)
        {  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();
        return outStream.toByteArray();  
    }
	
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
            +"|windows (phone|ce)|blackberry"    
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
            +"|laystation portable)|nokia|fennec|htc[-_]"    
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
      
    //移动设备正则匹配：手机端、平板  
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);    
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);    
        
    /** 
     * 检测是否是移动设备访问 
     */  
    public static boolean isMobile(String userAgent)
    {    
        if(null == userAgent)
        {    
            userAgent = "";    
        }else 
        {
			userAgent = userAgent.toLowerCase();
		}  
        // 匹配    
        return phonePat.matcher(userAgent).find() || tablePat.matcher(userAgent).find();    
    }  
	
    public static boolean isMobile(HttpServletRequest request)
    {
    	String userAgent = request.getHeader("USER-AGENT");
    	return isMobile(userAgent);
    }
}
