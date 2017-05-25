package Test.entity;

import java.util.List;

import nc.com.BaseModel;
import nc.com.utils.MD5;
import nc.com.utils.StringUtil;
import Test.common.AppConst;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class HL extends BaseModel<HL>{
	private static final long serialVersionUID = -6967680902549117464L;
	
	public static final HL dao = new HL();
	
	public static List<HL> list()
	{
		String sql ="select id, name, content, mark from HL where idDel = ?";
		return dao.find(sql, AppConst.DELETE_NO);
	}
	
	public static void delete(int id) 
	{
		dao.updateColumnBy("isDel", "id", AppConst.DELETE_YES, id);
	}
	

}
