package Test.Controller;

import com.jfinal.core.Controller;

public class WelcomeController extends Controller {
	public void index(){
		render("Welcome.jsp");
	}
	
	public void login(){
		render("/main/login.jsp");
	}
}
