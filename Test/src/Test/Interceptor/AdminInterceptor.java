package Test.Interceptor;

import nc.com.AppSession;
import nc.com.BaseController;
import nc.com.kits.HttpRequestKit;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class AdminInterceptor implements Interceptor{
	
	//private static final Logger log = Logger.getLogger(WeixinApiInterceptor.class);

	@Override
	public void intercept(Invocation inv) 
	{
		BaseController ctrl = (BaseController)inv.getController();
		
		if (!AppSession.isAdminLogin(inv.getController().getSession())) 
		{
			if (HttpRequestKit.isAjaxRequest(ctrl.getRequest())) 
			{
				ctrl.renderUnAuth();
			}else 
			{
				ctrl.redirect("/login");
			}
			return;
		}
		
		inv.invoke();
	}

}
