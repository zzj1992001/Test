package nc.com.testKit;

import java.util.Map;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import nc.com.utils.JsonUtil;

import com.jfinal.kit.JsonKit;

public class HttpTestKit {

	public static final int HTTP_CODE_SUCCESS = 200;
	public static final String HTTP_CODE_KEY = "http_code";

	public static Map<String, Object> sendRequest(String url, Map<String, Object> paramMap)
	{
		String paramStr = JsonKit.toJson(paramMap);
		
		System.out.println("--------------------------------------------");
		System.out.println(String.format("url: %s", url));
		System.out.println(String.format("参数: %s", paramStr));
		
		String encryptText = paramStr;//new String(EncryptUtil.encodeBase64());
		
		HttpResponse response = HttpRequest.post(url).body(encryptText).send();
		
		String text = response.body();
		String decryptText = text;
		
		Map<String, Object> dataMap = JsonUtil.toMap(decryptText);
		
		int httpCode = response.statusCode();
		dataMap.put(HTTP_CODE_KEY, httpCode);
		
		response.close();
		
		System.out.println(String.format("返回：%s", decryptText));
		System.out.println("--------------------------------------------");
		
		return dataMap;
	}
}
