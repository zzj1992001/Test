package nc.com.authentication;

import nc.com.kits.AESKit;

public class AppAuth {

	private static String SECRET_KEY = "simple_secret_key";
	private static int SESSION_MINUTES = 600;
	
	private static String split = "/=/";
	
	public static void init(String mobileSecretKey, int mobileSessionMinutes)
	{
		SECRET_KEY = mobileSecretKey;
		SESSION_MINUTES = mobileSessionMinutes;
	}
	
	public static String createToken(int userId)
	{
		long effectiveTime = System.currentTimeMillis() + SESSION_MINUTES * 60 * 1000;
		String data = tokenData(userId, effectiveTime);
		
		return AESKit.encrypt(data, SECRET_KEY);
	}
	
	
	public static boolean verifyToken(String token, Object userId)
	{
		String data = AESKit.decrypt(token, SECRET_KEY);
		String[] datas = data.split(split);
		
		String secretUserId = datas[0];
		if (!secretUserId.equals(userId.toString())) 
		{
			return false;
		}
		
		long effectiveTime = Long.valueOf(datas[1]);
		long now = System.currentTimeMillis();
		
		if (effectiveTime < now) 
		{
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * @return userId/-/effectiveTime
	 */
	private static String tokenData(int userId, long effectiveTime)
	{
		StringBuilder data = new StringBuilder().append(userId)
												.append(split)
												.append(effectiveTime);
		return data.toString();
	}
	
}
