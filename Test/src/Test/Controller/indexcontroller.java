package Test.Controller;

import com.jfinal.core.Controller;
import nc.com.BaseController;
import Test.Interceptor.AdminInterceptor;
import nc.com.AppSession;
import Test.entity.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;

@Before(AdminInterceptor.class)
public class indexcontroller extends BaseController {

	public void index()
	{
		render("index.jsp");
	}
	
	@Clear
	public void login() 
	{
		render("login.jsp");
	}
	@Clear
	public void ajaxLogin()
	{
		String account = getPara("account");
		String password = getPara("password", "").toLowerCase();
		
		User user = User.findBy(account);
		if(user == null || !user.checkPassword(User.encryptPass(password, user.getStr("salt"))))
		{
			renderFail("用户名或密码错误");
			return;
		}
		
		AppSession.setAdminUserId(getSession(), user.getInt("id"));
		renderSuccess("登录成功");
	}
	@Clear
	public void logout()
	{
		Integer userId = AppSession.getAdminUserId(getSession());
		if(userId != null)
		{
			AppSession.clear(getSession());
		}
		render("login.jsp");
	}
	public void passModifyEdit()
	{
		render("passModifyEdit.jsp");
	}
	
	public void passModify()
	{
		int userId = getSessionAdminUserId();
		String oldPass = getPara("oldPass");
		String newPass = getPara("newPass");
		String confirmPass = getPara("confirmPass");
		try {
			User.modifyPass(userId, oldPass, newPass, confirmPass);
		} catch (Exception e) {
			e.printStackTrace();
			renderFail(e.getMessage());
			return;
		}
		renderSuccess("修改成功，新密码已经生效。");
	}
	
}
