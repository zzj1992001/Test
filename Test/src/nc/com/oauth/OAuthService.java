package nc.com.oauth;

import java.util.UUID;

import nc.com.utils.MD5;

/**
 * 开放授权服务，用户客户端身份验证
 * 1) 生成授权令牌
 * 2) 检查授权令牌是否合法
 * 3) 清除缓存令牌
 */
public class OAuthService {

	public static final String PARAM_USER_ID = "_userId";
	public static final String PARAM_TOKEN = "_token";
	
	/**
	 * 生成授权令牌
	 */
	public static String createToken(String userId)
	{
		String secret = UUID.randomUUID().toString() + "_" + userId;
		String token = MD5.encrptMD5(secret);
		
		OAuthCache.cache(token, userId);
		
		return token;
	}
	
	/**
	 * 检查授权令牌是否合法
	 */
	public static boolean checkLegal(String token, Object userId)
	{
		String cachedUserId = OAuthCache.get(token);
		if(userId == null)
		{
			return cachedUserId != null;
		}
		return cachedUserId != null && userId.equals(cachedUserId);
	}
	
	
	/*public static String refreshToken()
	{
		return null;
	}*/
	
	/**
	 * 清除缓存令牌
	 */
	public static void clear(String token)
	{
		OAuthCache.remove(token);
	}
}
