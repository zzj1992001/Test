package nc.com.utils;

import java.util.HashMap;
import java.util.Map;

import jodd.json.JsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * json工具类
 * @author 罗季晖
 *
 */
public class JsonUtil {
	/**
	 * bean转json
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj){
		if(obj == null){
			return null;
		}
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(obj);
	}


	/**
	 * 根据json字符串返回Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String json) {
		return new JsonParser().parse(json, HashMap.class);
	}

	
	
}
