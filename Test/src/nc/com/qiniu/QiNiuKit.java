package nc.com.qiniu;

import jodd.util.StringUtil;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;


public class QiNiuKit {
	
	public static String DOMAIN; //七牛访问域名，比如：http://7ryl6g.com1.z0.glb.clouddn.com
	private static String ACCESS_KEY;
	private static String SECRET_KEY;
	private static String BUCKET; //七牛子空间
	
	/**
	 	config.txt 加入
	  	# 七牛云存储参数
	  	qiniu_access_key = vOxpCx1nM4mKT8cnO5Azz8LGLQzSvxbq8aEr-Vbj
	  	qiniu_secret_key = Tt1RJx26fYtbv7P3_3bw7ugbc_xE72_Jk78OB1n2
		qiniu_domain = http://7xo4wx.com1.z0.glb.clouddn.com
		qiniu_bucket = water
		<br>
	  	AppConst.init() 调用
	 	//七牛云存储
		String qiniuAccessKey = PropKit.get("qiniu_access_key");
		String qiniuSecretKey = PropKit.get("qiniu_secret_key");
		String qiniuDomain = PropKit.get("qiniu_domain");
		String qiniuBucket = PropKit.get("qiniu_bucket");
		QiNiuKit.init(qiniuAccessKey, qiniuSecretKey, qiniuBucket, qiniuDomain);
	 * 
	 */
	public static void init(String accessKey, String secretKey, String buchet, String domain)
	{
		ACCESS_KEY = accessKey;
		SECRET_KEY = secretKey;
		BUCKET = buchet;
		if (domain.endsWith("/")) 
		{
			DOMAIN = domain;
		}else 
		{
			DOMAIN = domain + "/";
		}
	}
	
	public static String getToken()
	{
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String token = auth.uploadToken(BUCKET);
		return token;
	}
	
	public static String upload(String filePath, String fileKey)
	{
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String token = auth.uploadToken(BUCKET);
		
		if (fileKey.startsWith("/")) 
		{
			fileKey = fileKey.substring(1);
		}
		
		UploadManager manager = new UploadManager();
		try {
			Response res = manager.put(filePath, fileKey, token);
			if(res.isOK())
			{
				DefaultPutRet ret = res.jsonToObject(DefaultPutRet.class);
				return DOMAIN + ret.key;
			}
		} catch (QiniuException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deleteByUrl(String url)
	{
		String key = url.replace(DOMAIN, "");
		delete(key);
	}
	
	public static void delete(String key)
	{
		if (key.startsWith("/")) 
		{
			key = key.substring(1);
		}
		
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		BucketManager bucketManager = new BucketManager(auth);
		try {
			bucketManager.delete(BUCKET, key);
		} catch (QiniuException e) {
			e.printStackTrace();
		}
	}
	
	public static String getThumbUrl(String imgUrl,int width,int height){
		if(StringUtil.isNotBlank(imgUrl) 
				&& !imgUrl.contains("?imageView2/1/w")){
			imgUrl = imgUrl + "?imageView2/1/w/"+width+"/h/"+height;
		}
		return imgUrl;
	}
	
	public static void main(String[] args) {
		
		String qiniuAccessKey = "vOxpCx1nM4mKT8cnO5Azz8LGLQzSvxbq8aEr-Vbj";
		String qiniuSecretKey = "Tt1RJx26fYtbv7P3_3bw7ugbc_xE72_Jk78OB1n2";
		String qiniuDomain = "http://om0vxzcgx.bkt.clouddn.com";
		String qiniuBucket = "teadev";
		init(qiniuAccessKey, qiniuSecretKey, qiniuBucket, qiniuDomain);
		upload("/Users/leigl/Desktop/c_01.jpeg", "test/002.jpg");
		//delete("/user/photo/user_test_image_5.jpg");
	}
}
