package Test.Controller;

import nc.com.BaseController;

public class WelcomeController extends BaseController {
	public void index(){
		render("Welcome.jsp");
	}
	
	public void login(){
		render("/main/login.jsp");
	}
	
	public void welcome(){
		renderSuccess("");
	}
}
