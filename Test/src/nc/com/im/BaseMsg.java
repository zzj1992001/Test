package nc.com.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseMsg<T extends BaseMsg> {

	public static final String TYPE_TXT = "txt";
	public static final String TYPE_IMG = "img";
	
	protected String target_type;
	protected List<String> targets;
	protected Map<String, String> msg;
	protected String from;
	protected Map<String, String> ext;
	
	public BaseMsg()
	{
		this.target_type = "users";
		this.targets = new ArrayList<String>();
		this.msg = new HashMap<String, String>();
	}
	
	@SuppressWarnings("unchecked")
	public T addTarget(String...targets)
	{
		for (String t : targets) 
		{
			this.targets.add(t);
		}
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public T setType(String type)
	{
		msg.put("type", type);
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public T setFrom(String fromUser)
	{
		this.from = fromUser;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public T addExtAttr(String key, String value)
	{
		if(this.ext == null)
		{
			this.ext = new HashMap<String, String>();
		}
		this.ext.put(key, value);
		return (T)this;
	}

	protected Map<String, String> getMsg() {
		return msg;
	}
}
