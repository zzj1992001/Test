package nc.com.im;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;



public class IMService {
	
	/**
	 * 给记录添加IM的账号和密码
	 * @return [{IMAccount, IMPassword, ...}]
	 */
	public static List<Record> fillIMAccount(List<Record> rs, String columnName)
	{
		if(rs == null)
		{
			return null;
		}
		for (Record r : rs) 
		{
			r.set("IMAccount", IM.createImUserName(r.getInt(columnName)));
			r.set("IMPassword", IM.createImPass(r.getInt(columnName)));
		}
		return rs;
	}

}