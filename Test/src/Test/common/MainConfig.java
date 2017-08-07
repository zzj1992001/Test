package Test.common;


import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

import Test.Controller.HLcontroller;
import Test.Controller.VueController;
import Test.Controller.WelcomeController;
import Test.Controller.WowController;
import Test.Controller.indexcontroller;
import Test.entity.HL;
import Test.entity.User;
import Test.entity.Wow;

public class MainConfig extends JFinalConfig {
	
	public MainConfig() {
		loadPropertyFile("config.txt");
	}
	

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setViewType(ViewType.JSP);
		me.setDevMode(true);


	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/main", indexcontroller.class,AppConst.JSP_BASE_VIEW_PATH+ "/main/");
		me.add("/main/HL",HLcontroller.class,AppConst.JSP_BASE_VIEW_PATH +"/main/HL");
		me.add("/",WelcomeController.class,AppConst.JSP_BASE_VIEW_PATH);
		me.add("/main/wow",WowController.class,AppConst.JSP_BASE_VIEW_PATH+ "/main/wow");
		me.add("/main/vue",VueController.class,AppConst.JSP_BASE_VIEW_PATH+ "/main/");
		
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		DruidPlugin dp = new DruidPlugin(getProperty("jdbc_url"),  getProperty("jdbc_user"), getProperty("jdbc_password"));
		me.add(dp);  
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.addMapping("user", User.class);  //user
		arp.addMapping("HL", HL.class); //HL
		arp.addMapping("wow", Wow.class);
		arp.setShowSql(true);
		me.add(arp); 


	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

}
