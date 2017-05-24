package nc.com.testKit;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class ApiDocsKit {

	
	public static List<Record> createList(String columns, Object... values) 
	{
		List<Record> list = new ArrayList<Record>();
		Record r = createRecord(columns, values);
		list.add(r);
		return list;
	}
	
	
	public static Record createRecord(String columns, Object... values) 
	{
		Record r = new Record();
		String[] colArr = columns.replace(" ", "").split(",");
		for (int i = 0; i < colArr.length; i++) 
		{
			String column = colArr[i];
			r.set(column, values[i]);
		}
		return r;
	}
	
}
