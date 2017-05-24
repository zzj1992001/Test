package nc.com.im;


public class TextMsg extends BaseMsg<TextMsg>{

	public TextMsg()
	{
		super();
		setType(TYPE_TXT);
	}
	
	public TextMsg setMsg(String msg)
	{
		getMsg().put("msg", msg);
		return this;
	}
}
