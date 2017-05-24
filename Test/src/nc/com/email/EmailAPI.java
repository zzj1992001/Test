package nc.com.email;
import java.io.File;
import java.util.regex.Pattern;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SimpleAuthenticator;
import jodd.mail.SmtpServer;
import jodd.mail.att.FileAttachment;
/**
 * 邮件工具类
 *
 * jar依赖：
 * 需要引入1、jodd.jar
 *       2、javax.email包
 */
public class EmailAPI {
	//邮件正则表达式
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	
	private static String smtpUrl;
	private static String username;
	private static String password;
	private static SmtpServer smtpServer;
	
	public static void init(String smtpHost, String emailAccount, String emailPass)
	{
		smtpUrl = smtpHost;
		username = emailAccount;
		password = emailPass;  
		
		smtpServer = new SmtpServer(smtpUrl, new SimpleAuthenticator(username, password));
	}
	
	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	
	/**
	 * 发送普通邮件
	 * @param to        收件人地址
	 * @param subject   邮件主题
	 * @param content   邮件内容
	 */
	public static void sendPlaintTextMail(String subject,String content,String... to){
		SendMailSession session = null;
		try {
			Email email = genPlaintEmail(subject, content, "UTF-8", to);
			session = smtpServer.createSession();
			session.open();
			session.sendMail(email);
		} finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	
	/**
	 * 发送带附件邮件
	 * @param to          收件人
	 * @param subject     主题
	 * @param content     内容
	 * @param fileAttach  附件
	 */
	public static void sendPlaintTextMailWithFileAttach(String subject,String content,String fileAttach,String... to){
		SendMailSession session = null;
		try {
			Email email = genPlaintEmailWithFileAttachment(subject, content, "UTF-8",fileAttach,to);
			session = smtpServer.createSession();
			session.open();
			session.sendMail(email);
		} finally{
			if(session != null){
				session.close();
			}
		}
	}
	/**
	 * 发送html格式的邮件
	 * @param to       收件人地址
	 * @param subject  邮件主题
	 * @param content  邮件内容
	 */
	public static void sendHtmlMail(String subject,String html,String... to){
		SendMailSession session = null;
		try {
			Email email = genHtmlEmail(subject, html, "UTF-8",to);
			session = smtpServer.createSession();
			session.open();
			session.sendMail(email);
		} finally{
			if(session != null){
				session.close();
			}
		}
	}
	/**
	 * 发送带附件邮件
	 * @param to          收件人
	 * @param subject     主题
	 * @param content     内容
	 * @param fileAttach  附件
	 */
	public static void sendHtmlMailWithFileAttach(String subject,String content,String fileAttach,String... to){
		SendMailSession session = null;
		try {
			Email email = genHtmlEmailWithFileAttachment(subject, content, "UTF-8",fileAttach,to);
			session = smtpServer.createSession();
			session.open();
			session.sendMail(email);
		} finally{
			if(session != null){
				session.close();
			}
		}
	}
	private static Email genPlaintEmail(String subject,String content,String encoding,String... to){
		Email email = new Email();
		email.from(username).to(to).subject(subject)
			 .addText(content,encoding);
		return email;
	}
	//生成html邮件
	private static Email genHtmlEmail(String subject,String htmlContent,String encoding,String... to){
		Email email = new Email();
		email.from(username).to(to).subject(subject)
		     .addHtml(htmlContent,encoding);
		return email;
	}
	private static Email genPlaintEmailWithFileAttachment(String subject,String content,String encoding,String attachFile,String... to){
		Email email = genPlaintEmail(subject, content, encoding,to);
		File file = new File(attachFile);
		FileAttachment fileAttachment = new FileAttachment(new File(attachFile), file.getName(), file.getName());
		email.attach(fileAttachment);
		return email;
	}
	private static Email genHtmlEmailWithFileAttachment(String subject,String htmlContent,String encoding,String attachFile,String... to){
		Email email = genHtmlEmail(subject, htmlContent, encoding, to);
		File file = new File(attachFile);
		FileAttachment fileAttachment = new FileAttachment(new File(attachFile), file.getName(), file.getName());
		email.attach(fileAttachment);
		return email;
	}
}