package nc.com.email;

import org.apache.log4j.Logger;

import nc.com.email.EmailAPI;

public class EmailService {

	private static final Logger log = Logger.getLogger(EmailService.class);
	
	public static void sendResetPassCode(String email, String code)
	{
		if (!EmailAPI.isEmail(email)) 
		{
			log.error("发送重置密码邮件，邮箱格式不正确：email = " + email + " code = " + code);
			return;
		}
		
		String subject = "重置密码邮件";
		String html = "重置密码的code为：" + code;
		
		Thread thread = new Thread(new EmailTask(subject, html, email));
		thread.start();
	}
	
}

class EmailTask implements Runnable{

	private String subject;
	private String html;
	private String to;
	
	public EmailTask(String subject, String html, String to) 
	{
		this.subject = subject;
		this.html = html;
		this.to = to;
	}
		
	@Override
	public void run() 
	{
		EmailAPI.sendHtmlMail(subject, html, to);
	}
}
