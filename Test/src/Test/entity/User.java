package Test.entity;

import java.util.List;

import nc.com.BaseModel;
import nc.com.utils.MD5;
import nc.com.utils.StringUtil;
import Test.common.AppConst;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class User extends BaseModel<User>{
	
	private static final long serialVersionUID = -6967680902549117464L;
	
	public static final User dao = new User();

	/**
	 * 根据用户名查找用户 
	 * @return {id, account, password, salt, userId}
	 */
	public static User findBy(String account) 
	{
		String sql = "select id, account, password, salt, userId, isDel from user where account = ?";
		return dao.findFirst(sql, account);
	}
	
	/**
	 * 根据密码判断是否登录成功
	 */
	public boolean checkPassword(String password) 
	{
		return getStr("password").toLowerCase().equals(password.toLowerCase());
	}


	/**
	 * 修改密码，密码使用MD5加密
	 * @throws Exception ("新密码和确认密码不一样")
	 * @throws Exception ("旧密码错误")
	 */
	public static void modifyPass(int userId, String oldPass, String newPass, String confirmPass) throws Exception 
	{
		if (oldPass != null && oldPass.equals(newPass)) 
		{
			throw new Exception("新密码不能和旧密码一样");
		}
		if(confirmPass == null || !confirmPass.equals(newPass))
		{
			throw new Exception("新密码和确认密码不一样");
		}
		User user = dao.findByIdLoadColumns(userId, "id, password, salt");
		
		String encrptOldPass = encryptPass(MD5.encrptMD5(oldPass), user.getStr("salt"));
		
		if(oldPass != null && newPass != null && user.getStr("password").equalsIgnoreCase(encrptOldPass))
		{
			user.set("password", encryptPass(MD5.encrptMD5(newPass), user.getStr("salt"))).update();
			return;
		}
		throw new Exception("旧密码错误");
	}

	/**
	 * 检查用户名是否存在
	 */
	public static boolean exist(String account)
	{
		User user = findBy(account);
		return user != null;
	}

	public static String encryptPass(String inputPass, String salt) 
	{
		return MD5.encrptMD5(inputPass.toLowerCase() + salt).toLowerCase();
	}
	
	public static String generateSalt()
	{
		return StringUtil.randStr(6);
	}

	public static boolean checkPass(String passwordA, String passwordB) 
	{
		return passwordA != null && !passwordA.isEmpty() && passwordA.equalsIgnoreCase(passwordB);
	}

	/**
	 * @return [{.., account}]
	 */
	public static void mergeToRs(List<Record> list, String userIdColumn) 
	{
		dao.mergeColumnsToRs(list, userIdColumn, "userId", "userId, account", "account", null);
	}

	/**
	 * @return {salt, password}
	 * <br>password : MD5(MD5(password) + salt)
	 */
	public static void setEncryptPass(User user, boolean isPassMd5) 
	{
		String passMd5 = null;
		if (isPassMd5) 
		{
			passMd5 = user.getStr("password");
		}else 
		{
			passMd5 = MD5.encrptMD5(user.getStr("password"));
		}
		
		String salt = generateSalt();
		String encryptPass = encryptPass(passMd5, salt);
		user.set("password", encryptPass)
			.set("salt", salt);
	}

	public static void delete(int userId) 
	{
		String sql = "update user set account = concat(account, '_0', id), isDel = ? where userId = ?";
		Db.update(sql, AppConst.DELETE_YES, userId);
	}

	public static void resetPass(int userId, String newPass) 
	{
		User user = dao.findById(userId, "id, salt");
		if (user != null) 
		{
			String salt = user.getStr("salt");
			String encryptPass = encryptPass(MD5.encrptMD5(newPass), salt);
			dao.updateColumnBy("password", "id", encryptPass, userId);
		}
	}

}
