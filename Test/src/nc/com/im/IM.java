package nc.com.im;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;
import nc.com.utils.HttpsUtil;
import nc.com.utils.MD5;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
/**
 * 环信IM功能封装
 */
public class IM {
	
	public static Logger logger = Logger.getLogger(IM.class);
	
	public static String EMTOKEN_CACHE = "emTokenCache";
	public static String EMTOKEN_KEY = "emTokenKey";
	
	public static final String WEB_SITE = "https://a1.easemob.com/";
	public static final String COMPANY_NAME = "ncthinker";
	public static final String APP_NAME = "oa";
	public static String BASE_URL = WEB_SITE + COMPANY_NAME + "/" + APP_NAME;
	

	/**
	 * 发送文本消息
	 * @throws Exception ("获取环信token失败")
	 * @throws Exception ("环信发送消息失败")
	 */
	public static void sendText(int fromUserId, String msg, int toUserId, Integer type) throws Exception
	{
		String fromUser = createImUserName(fromUserId);
		String toUser = createImUserName(toUserId);
		TextMsg txt = new TextMsg().setFrom(fromUser)
								   .addTarget(toUser)
								   .setMsg(msg)
								   .addExtAttr("type", type.toString());
		sendMsg(txt);
	}
	
	/**
	 * 发送消息
	 * @throws Exception ("获取环信token失败")
	 * @throws Exception ("环信发送消息失败")
	 */
	@SuppressWarnings("rawtypes")
	public static void sendMsg(BaseMsg msg) throws Exception
	{
		String token = getEMToken();
		if(StringUtil.isBlank(token))
		{
			throw new Exception("获取环信token失败");
		}
		
		String url = BASE_URL + "/messages";
		String response = HttpsUtil.post(url)
				  				   .header("Authorization", "Bearer " + token)
				  				   .body(JsonKit.toJson(msg))
				  				   .send();
		if(StringUtil.isBlank(response))
		{
			throw new Exception("环信发送消息失败");
		}
		logger.info("环信发送消息结果：" + response);
	}
	
	/**
	 * 注册环信帐号
	 * APP_NAME + userId 作为环信帐号
	 * MD5(userId + APP_NAME) 作为环信密码
	 */
	public static boolean register(Integer userId)
	{
		String userName = createImUserName(userId);
		String password = createImPass(userId);
		return register(userName, password);
	}

	/**
	 * 注册环信帐号
	 */
	public static boolean register(String username,String password){
		String token = getEMToken();
		if(StringUtil.isBlank(token)){
			return false;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		String url = BASE_URL + "/users";
		String response = HttpsUtil.post(url)
			  .header("Authorization", "Bearer "+token)
			  .body(JsonKit.toJson(map))
			  .send();
		if(StringUtil.isBlank(response)){
			return false;
		}
		
		logger.info("环信注册用户结果：" + response);
		
		Map<String, Object> json = parseData(response);
		Object error = json.get("error");
		if(StringUtil.equals("duplicate_unique_property_exists",StringUtil.toSafeString(error))){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据用户Id生成环信帐号
	 */
	public static String createImUserName(Integer userId) 
	{
		String userName = APP_NAME + userId;
		return userName;
	}
	
	/**
	 * 根据用户Id生成环信密码
	 */
	public static String createImPass(Integer userId) 
	{
		String password = MD5.encrptMD5(userId + APP_NAME);
		return password;
	}
	
	/**
	 * 解析环信账号，获取userId
	 */
	public static int getUserIdFrom(String imAccount) 
	{
		String userId = imAccount.replace(APP_NAME, "");
		return Integer.valueOf(userId);
	}
	
	public static String saveEMToken(String token){
		//登录成功，生成token加到缓存队列
		CacheKit.put(EMTOKEN_CACHE, EMTOKEN_KEY, token);
		return token;
	}
	
	public static String getEMToken(){
		String token = CacheKit.get(EMTOKEN_CACHE, EMTOKEN_KEY);
		if(StringUtil.isBlank(token)){
			token = getTokenFromServer();
			saveEMToken(token);
		}
		return token;
	}
	
	public static String getTokenFromServer(){
		String url = "https://a1.easemob.com/ncthinker/dianjinwang/token";
		Map<String, String> request = new HashMap<String, String>();
		request.put("grant_type", "client_credentials");
		request.put("client_id", "YXA6x4OWUIbAEeSys3_7LxBl3A");
		request.put("client_secret", "YXA6JVUmOHdEeyaaXt38Ji5RfEW_2EI");
		String response = HttpsUtil.post(url).body(JsonKit.toJson(request)).send();
		Map<String, Object> json = parseData(response);
		String token = "";
		if(json != null){
			Object tmp = json.get("access_token");
			token = tmp == null ? "" : tmp.toString();
		}
		return token;
	}
	
	 private  static Map<String, Object> parseData(String data){
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(data, new TypeToken<Map<String, Object>>() {}.getType()); 
        if(map == null) {
        	return new HashMap<String, Object>();
        }
        return map;
    }

	 public static void appendAccountTo(List<Record> list, String idColumn, String newAccountColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		
		for (Record record : list) 
		{
			record.set(newAccountColumn, IM.createImUserName(record.getInt(idColumn)));
		}
	}
}