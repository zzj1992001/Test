package nc.com.sns;



public class SnsKit {
	
	private static String account = "N5408348";  // 发送短信的账号(非登录账号) (示例:N987654) 
	private static String pswd = "szN5408348";  // 发送短信的密码(非登录密码)
	private static String url = "http://sms.253.com/msg/send";  // "http://sms.253.com/msg/send";应用地址 (无特殊情况时无需修改)
	private static String sign = "253云通讯";  //短信签名【xxxx】
	
	/**
	 * <br>1，config.txt 加入：
	 * #创蓝短信
	   sns_account = N5408348
	   sns_password = szN5408348
	   sns_url = http://sms.253.com/msg/send
	   sns_sign = 茶趣里
	 * <br>2，Appconst.java 调用：
	 * //创蓝短信
		String snsAccount = PropKit.get("sns_account");
		String snsPassword = PropKit.get("sns_password");
		String snsUrl = PropKit.get("sns_url");
		String snsSign = PropKit.get("sns_sign");
		SnsKit.init(snsAccount, snsPassword, snsUrl, snsSign);
	 */
	public static void init(String snsAccount, String snsPassword, String snsUrl, String snsSign)
	{
		account = snsAccount;
		pswd = snsPassword;
		url = snsUrl;
		sign = snsSign;
	}
	
	/**
	 * 发送短信验证码
	 * <br>修改资料（手机号）
	 * <br>【茶趣里】您的验证码是：1234（打死都不能告诉其他人）。
	 */
	public static void sendModifyTelCaptcha(String tel, String captcha) 
	{
		String msgText = "您的验证码是：" + captcha + "（打死都不能告诉其他人）。";
		sendMessage(tel, msgText);
	}
	
	/**
	 * 发起邀约，通知被邀请人
	 * <br>【茶趣里】亲爱的xxx，有人想约您一起喝茶，想知道Ta是谁，请打开茶趣里公众号。
	 */
	public static void sendOnMakingDate(String tel, String nickName) 
	{
		String msgText = "亲爱的" + nickName + "，有人想约您一起喝茶，想知道Ta是谁，请打开茶趣里公众号查看邀约记录。";
		sendMessage(tel, msgText);
	}
	
	/**
	 * 接受邀约，通知发起邀请人
	 * <br>【茶趣里】亲爱的xxx，用户xxx已经接受您发起的邀约，赶快联系她吧。
	 */
	public static void sendOnAcceptingDate(String tel, String nickName, String friendTel) 
	{
		String msgText = "太棒啦！您邀约的" + nickName + "已经接受邀请，赶快联系Ta吧！联系方式为 " + friendTel;
		sendMessage(tel, msgText);
	}
	
	/**
	 * 接受邀约，通知发起邀请人
	 * <br>【茶趣里】亲爱的xxx，您的书已经被人换走，想知道Ta是谁，请打开茶趣里公众号。
	 */
	public static void sendOnExchangeBook(String tel, String nickName) 
	{
		String msgText = "亲爱的" + nickName + "，您的书已经被人换走，想知道Ta是谁，请打开茶趣里公众号查看我的书架。";
		sendMessage(tel, msgText);
	}
	
	
	private static void sendMessage(String phone, String msgText)
	{
		String msg = "【" + sign + "】" + msgText; //您的签名+短信内容 
		
		String extno = null;     	// 扩展码(可选参数,可自定义)
		String rd="1";				// 是否需要状态报告(需要:1,不需要:0)
		try {
			    String returnString2 = HttpSender.SendPost(url,account, pswd,msg.toString(),phone,rd,extno); 
			    System.out.println("短信发送结果：" + returnString2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) 
	{
		String msgText = "测试短信，您的验证码是123456";
		sendMessage("13428790254", msgText);
	}*/
}
