package nc.com.oauth;

import com.jfinal.plugin.ehcache.CacheKit;

public class OAuthCache {

	
	private static String cacheName;
	
	public static void init(String cacheName)
	{
		OAuthCache.cacheName = cacheName;
	}
	
	public static void cache(String token, String userId) 
	{
		CacheKit.put(cacheName, token, userId);
	}

	public static String get(String token) 
	{
		return CacheKit.get(cacheName, token);
	}

	public static void remove(String token) {
		CacheKit.remove(cacheName, token);
	}
}