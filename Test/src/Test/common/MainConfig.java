package Test.common;


import Test.common.AppConst;
import Test.Controller.indexcontroller;
import Test.entity.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

import Test.Controller.indexcontroller;

public class MainConfig extends JFinalConfig {
	
	public MainConfig() {
		loadPropertyFile("config.txt");
	}
	

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setViewType(ViewType.JSP);

	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/", indexcontroller.class,AppConst.JSP_BASE_VIEW_PATH);
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
		arp.addMapping("user", User.class); 
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