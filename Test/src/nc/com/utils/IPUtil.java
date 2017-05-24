package nc.com.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;

public class IPUtil {
	
	public static String getIpAddr(ServletRequest request) {
		HttpServletRequest localRequest=(HttpServletRequest) request;
		String ip = localRequest.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
		{
			ip = localRequest.getHeader("X-Forwarded-For");
		}
		if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) 
		{
			String[] ips = ip.split(",");
			ip = ips[0].trim();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = localRequest.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = localRequest.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String [] getDomainIP(String domain) throws UnknownHostException {
		InetAddress [] array= InetAddress.getAllByName(domain);
		String ips []=new String[array.length];
		for (int i = 0; i < array.length; i++) {
			ips[i]=array[i].getHostAddress();
		}
		return ips;
	}
	
	public static String getUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String parmas = request.getQueryString();
        if (StrKit.notBlank(parmas)) {
            url = url + "?" + parmas;
        }
        return url;
    }
}
