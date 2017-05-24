package nc.com.exception;

/**
 * 业务异常
 */
public class BizzException extends Exception {

	public BizzException(String errorMsg) 
	{
		super(errorMsg);
	}

	private static final long serialVersionUID = 1L;
	
}
