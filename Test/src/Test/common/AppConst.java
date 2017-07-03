package Test.common;

public class AppConst  {
	//分页参数
	public static int PAGE_NUM_DEFAULT = 1;
	public static int PAGE_SIZE_DEFAULT = 20;
	public static int PAGE_SIZE_API_DEFAULT = 15;
	
	//服务端返回参数
	public static final String RESPONSE_KEY_CODE = "status_code";
	public static final String RESPONSE_KEY_MSG = "status_msg";
	//服务端返回参数值：200 请求成功；300 请求失败，发生异常；301 会话失效；302 没有访问权限；404 内容不见了
	public static final int RESPONSE_VALUE_CODE_SUCCESS = 200;
	public static final int RESPONSE_VALUE_CODE_FAIL = 300;
	public static final int RESPONSE_VALUE_CODE_UNAUTHENTICATION = 301;
	public static final int RESPONSE_VALUE_CODE_UNAUTHORITY = 302;
	public static final int RESPONSE_VALUE_CODE_404 = 404;
	public static final String RESPONSE_VALUE_DEFAULT_MSG = "";
	
	//JSP 常用页面
	public static final String JSP_BASE_VIEW_PATH = "/WEB-INF/web";
	public static final String URL_LOGIN = "/login.jsp";
	public static final String URL_INDEX_ADMIN = "/main/index.jsp";
	public static final String URL_RESULT = "/main/result.jsp";
	
	//常用HTTP请求参数
	public static final String PARAM_KEY_CURRENT_URL = "currentUrl";
	public static final String PARAM_API_KEY_USER_ID = "_userId";
	public static final String PARAM_API_KEY_TOKEN = "_token";
	
	//常用常量：0 未删除；1 已删除
	public static final int DELETE_NO = 0;
	public static final int DELETE_YES = 1;

}
