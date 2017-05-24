package nc.com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;

import com.jfinal.kit.JsonKit;

public class HttpsUtil {
	
	private static final String METHOD_POST = "POST";
	private static final String DEFAULT_CHARSET = "utf-8";
	private static final int DEFAULT_CONNECT_TIME_OUT = 15000;
	private static final int DEFAULT_READ_TIME_OUT = 15000;
	
	private static Logger log = Logger.getLogger(HttpsUtil.class.getName());
	
	private HttpsURLConnection conn;
	
	private String charset = DEFAULT_CHARSET;
	private byte[] bodyContent;
	
	private HttpsUtil(URL url, String method) throws Exception{
		SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);
        
		conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
        
        conn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
        
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent", "stargate");
		conn.setRequestProperty("Content-Type", "application/json;charset="+charset);
		
		conn.setConnectTimeout(DEFAULT_CONNECT_TIME_OUT);
		conn.setReadTimeout(DEFAULT_READ_TIME_OUT);
	}
	
	public static HttpsUtil post(String url){
		URL tmp = null;
		HttpsUtil utils = null;
		try {
			tmp = new URL(url);
			utils = new HttpsUtil(tmp, METHOD_POST);
		} catch (Exception e) {
			log.error("创建连接失败.", e);
		}
		return utils;
	}
	
	public HttpsUtil charset(String charset){
		this.charset = charset;
		conn.setRequestProperty("Content-Type", "application/json;charset="+charset);
		return this;
	}
	
	public HttpsUtil header(String key,String value){
		conn.addRequestProperty(key, value);
		return this;
	}
	
	public HttpsUtil body(String body){
		try {
			this.bodyContent = body.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			log.error("url创建失败.", e);
		}
		return this;
	}
	
	public HttpsUtil body(String body,String charset){
		try {
			this.bodyContent = body.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
		}
		return this;
	}
	
	public String send(){
		OutputStream out = null;
		String response = "";
		try{
			out = conn.getOutputStream();
			if(bodyContent != null){
				out.write(bodyContent);
			}
			String charset = getResponseCharset(conn.getContentType());
			InputStream es = conn.getErrorStream();
			if (es == null) {
				response = getStreamAsString(conn.getInputStream(), charset);
			} else {
				response = getStreamAsString(es, charset);
				if (StringUtil.isEmpty(response)) {
					response = conn.getResponseCode() + ":" + conn.getResponseMessage();
				}
			}
		}catch(IOException e){
			log.error("发送请求错误.", e);
		}finally{
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}
	
	

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[1024*256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}
			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;
		if (!StringUtil.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtil.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}
	
	public static void main(String[] args) {
		String url = "https://a1.easemob.com/ncthinker/dianjinwang/token";
		Map<String, String> request = new HashMap<String, String>();
		request.put("grant_type", "client_credentials");
		request.put("client_id", "YXA6euMekGPbEeSQo9-QX2RECQ");
		request.put("client_secret", "YXA6_KcCTzJGta1mgBOwUfs-EWPQ0AA");
		String response = HttpsUtil.post(url)
									.body(JsonKit.toJson(request))
									.send();
		System.out.println(response);
		
	}
}