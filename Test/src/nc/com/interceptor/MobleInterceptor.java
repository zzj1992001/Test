package nc.com.interceptor;

import nc.com.authentication.AppAuth;
import Test.common.AppConst;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class MobleInterceptor implements Interceptor{


	@Override
	public void intercept(Invocation ai) 
	{
		Controller ctrl = ai.getController();
		String token = ctrl.getPara(AppConst.PARAM_API_KEY_TOKEN);
		int userId = ctrl.getParaToInt(AppConst.PARAM_API_KEY_USER_ID);
		
		if (AppAuth.verifyToken(token, userId))
		{
			ai.invoke();
			return;
		}
		
		ctrl.setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_UNAUTHENTICATION);
		ctrl.renderJson();
	}
	
}
