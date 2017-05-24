package nc.com.kits;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jodd.util.Base64;

public class AESKit {

	private static final String AES_TYPE = "AES/ECB/PKCS5Padding";
	private static Cipher encryptCipher;
	private static Cipher decryptCipher;
	
	static{
		try {
			encryptCipher = Cipher.getInstance(AES_TYPE);
			decryptCipher = Cipher.getInstance(AES_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String encrypt(String data, String encryptKey) 
	{
		Key key = generateKey(encryptKey);
		
		try {
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptData = encryptCipher.doFinal(data.getBytes());
			return new String(Base64.encodeToByte(encryptData));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String decrypt(String data, String decryptKey) 
	{
		Key key = generateKey(decryptKey);
		
		try {
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptData = decryptCipher.doFinal(Base64.decode(data));
			return new String(decryptData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Key generateKey(String password) 
	{
		SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "AES");
		return keySpec;
	}

	
	public static void main(String[] args) 
	{
		String content = "2015/-/1234554321987";  
		String password = "1234567812345678";  
		
		long time = System.currentTimeMillis();
		String encText = encrypt(content, password);
		System.out.println(encText);
		
		String decText = decrypt(encText, password);
		System.out.println(decText);
		
		long cost = System.currentTimeMillis() - time;
		System.out.println("耗时：" + cost);
	}
}
