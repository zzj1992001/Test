package nc.com;

import javax.servlet.http.HttpSession;


/**
 * Session 属性信息
 */
public class AppSession {

	private static final String ATTR_USER_ID_ADMIN = "userId_admin";
	private static final String ATTR_USER_ID = "userId";
	private static final String ATTR_USER = "user";
	
	public static Integer getUserId(HttpSession session)
	{
		Object val = session.getAttribute(ATTR_USER_ID);
		
		return val == null ? null : (Integer)val;
	}

	public static void setUserId(HttpSession session, int userId) 
	{
		session.setAttribute(ATTR_USER_ID, userId);
	}
	
	public static Integer getAdminUserId(HttpSession session)
	{
		Object val = session.getAttribute(ATTR_USER_ID_ADMIN);
		return val == null ? null : (Integer)val;
	}
	
	public static void setAdminUserId(HttpSession session, int userId) 
	{
		session.setAttribute(ATTR_USER_ID_ADMIN, userId);
	}
	
	public static boolean isAdminLogin(HttpSession session) 
	{
		return session.getAttribute(ATTR_USER_ID_ADMIN) != null;
	}

	/**
	 * 判断是否存在登录session
	 */
	public static boolean isLogin(HttpSession session) 
	{
		return session.getAttribute(ATTR_USER_ID) != null;
	}

	/**
	 * 清楚登录session
	 * @param session
	 */
	public static void clear(HttpSession session) 
	{
		session.removeAttribute(ATTR_USER_ID);
		session.removeAttribute(ATTR_USER);
		session.removeAttribute(ATTR_USER_ID_ADMIN);
		session.invalidate();
	}
	
}
