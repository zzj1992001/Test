package nc.com.push;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

public class XingePush {

	//account_env_userId
	private static final String ACCOUNT_FORMAT = "account_%d_%d";

	private static final Logger log = Logger.getLogger(XingePush.class);
	
	private static long ACCESS_ID_ANDROID;
	private static String SECRET_KEY_ANDROID;
	
	private static long ACCESS_ID_IOS;
	private static String SECRET_KEY_IOS;
	
	private static int IOS_ENV;// 1生产环境；2开发环境
	
	private static String IOS_SOUND = "beep.wav";
	private static int DEVICE_TYPE = 0;
	
	/**
		config.txt 配置
		<br>
		# 信鸽推送 1生产环境；2开发环境
		accessIdAndroid = 2100166040
		secretKeyAndroid = 7ec65fe7fbaf0fc23ff3b128bd4d9239
		accessIdIos = 2200159289
		secretKeyIos = 7ee84aff546ae4539d59891264b4112c
		iosEnv = 1
		iosSound = beep.wav
		pushTitle = 合作宝
		<br>
		AppConst.init() 调用
	 	<br>
		long accessIdAndroid = PropKit.getLong("accessIdAndroid");
		String secretKeyAndroid = PropKit.get("secretKeyAndroid");
		long accessIdIos = PropKit.getLong("accessIdIos");
		String secretKeyIos = PropKit.get("secretKeyIos");
		int iosEnv = PropKit.getInt("iosEnv");
		String iosSound = PropKit.get("iosSound");
		String pushTitle = PropKit.get("pushTitle");
		XingePush.init(accessIdAndroid, secretKeyAndroid, accessIdIos, secretKeyIos, iosEnv, iosSound, pushTitle);
	 */
	public static void init(long accessIdAndroid, String secretKeyAndroid, long accessIdIos, String secretKeyIos, int iosEnv, String iosSound)
	{
		ACCESS_ID_ANDROID = accessIdAndroid;
		SECRET_KEY_ANDROID = secretKeyAndroid;
		
		ACCESS_ID_IOS = accessIdIos;
		SECRET_KEY_IOS = secretKeyIos;
		IOS_ENV = iosEnv;
		IOS_SOUND = iosSound;
	}
	
	public static void push(int userId, String title, String content, Map<String, Object> customValues, String activity, long unReadNum) 
	{
		XingeApp xinge = new XingeApp(ACCESS_ID_ANDROID, SECRET_KEY_ANDROID);
		
		//需要设置隐藏消息内容
		Message message = new Message();
		
		message.setContent(content);
		message.setTitle(title);
		message.setType(Message.TYPE_NOTIFICATION);
		
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_ACTIVITY);
		action.setActivity(activity);
		message.setAction(action);
		
		Style style = new Style(1, 1, 0, 1, 0);
		message.setStyle(style);
		
		if (customValues != null) 
		{
			message.setCustom(customValues);
		}
		
		JSONObject result = xinge.pushSingleAccount(DEVICE_TYPE, getAccount(userId), message);
		log.info("安卓 push result：" + result.toString());
		
		//推送 iOS
		pushIos(userId, content, customValues, unReadNum);
	}
	
	public static void pushToAll(String title, String content, Map<String, Object> customValues, String activity) 
	{
		XingeApp xinge = new XingeApp(ACCESS_ID_ANDROID, SECRET_KEY_ANDROID);
		
		//需要设置消息内容
		Message message = new Message();
		
		message.setContent(content);
		message.setTitle(title);
		message.setType(Message.TYPE_NOTIFICATION);
		
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_ACTIVITY);
		action.setActivity(activity);
		message.setAction(action);
		
		Style style = new Style(1, 1, 0, 1, 0);
		message.setStyle(style);
		
		if (customValues != null) 
		{
			message.setCustom(customValues);
		}
		
		JSONObject result = xinge.pushAllDevice(DEVICE_TYPE, message);
		log.info("安卓 push result：" + result.toString());
		
		//推送 iOS
		pushToAllIos(content, customValues);
	}
	
	private static void pushIos(int userId, String content, Map<String, Object> customValues, long unReadNum) 
	{
		XingeApp xinge = new XingeApp(ACCESS_ID_IOS, SECRET_KEY_IOS);
		
		//推送 iOS
		MessageIOS messageIOS = new MessageIOS();
		messageIOS.setAlert(content);
		messageIOS.setBadge(((Long)unReadNum).intValue());
		messageIOS.setSound(IOS_SOUND);
		
		if (customValues != null) 
		{
			messageIOS.setCustom(customValues);
		}
		
		JSONObject resultIOS = xinge.pushSingleAccount(DEVICE_TYPE, getAccount(userId), messageIOS, IOS_ENV);
		log.info("iOS push result：" + resultIOS.toString());
	}
	
	private static void pushToAllIos(String content, Map<String, Object> customValues) 
	{
		XingeApp xinge = new XingeApp(ACCESS_ID_IOS, SECRET_KEY_IOS);
		
		//推送 iOS
		MessageIOS messageIOS = new MessageIOS();
		messageIOS.setAlert(content);
		messageIOS.setSound(IOS_SOUND);
		messageIOS.setBadge(1);
		
		if (customValues != null) 
		{
			messageIOS.setCustom(customValues);
		}
		
		JSONObject resultIOS = xinge.pushAllDevice(DEVICE_TYPE, messageIOS, IOS_ENV);
		log.info("iOS push result：" + resultIOS.toString());
	}
	
	/**
	 * 生成信鸽帐号
	 * <br>account_env_userId
	 */
	public static String getAccount(int userId) 
	{
		return String.format(ACCOUNT_FORMAT, IOS_ENV, userId);
	}
	
	/*public static void main(String[] args) {
		String account = "luke@ncthinker.com";
		String content = "mark 评论了你的动态";
		XingePush.push(account, "动态消息", content);
		
		//XingeApp.pushAccountAndroid(ACCESS_ID, SECRET_KEY, "消息", content, account);
	}*/
}
