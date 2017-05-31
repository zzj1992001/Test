package Test.Controller;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import nc.com.BaseController;
import nc.com.fileService.FileService;
import nc.com.fileService.SavedFile;
import nc.com.qiniu.QiNiuKit;
import Test.Interceptor.AdminInterceptor;
import Test.common.AppConst;
import Test.entity.HL;

@Before(AdminInterceptor.class)
public class HLcontroller extends BaseController {
	public void index()
	{
		list();
		render("index.jsp");
	}
	public void list()
	{
		String select ="select id, name, content, mark";
		String where ="from HL where isDel = ?";
		
		page(select,where,AppConst.DELETE_NO);

		render("list.jsp");
	}
	public void Add()
	{
		render("Edit.jsp");
	}
	public void update()
	{
		HL hl =getModel(HL.class,"hl");
		hl.update();
		
		renderSuccess("修改成功");
		
	}
	public void modifyEdit()
	{
		Integer id=getParaToInt(0);
		if(id!=null&&id>0){
			setAttr("hl", HL.dao.findById(id));
		}
		render("Edit.jsp");
	}
	public void submit() 
	{

		HL hl =getModel(HL.class,"hl");
		hl.set("createtime", new Date());
		hl.save();
		renderSuccess("添加成功");
		
	}
	
	public void delete()
	{
		int id = getParaToInt("id");
		
		HL.delete(id);
		
		renderSuccess("删除成功");
	}
	
	
}
