package nc.com.kits;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class HttpKit {

	public static boolean download(String fileUrl, String savePath) 
	{  
		InputStream is = null;
		OutputStream os = null;
		try {
			URL url = new URL(fileUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(20 * 1000);  
	        is = con.getInputStream();
	        byte[] bs = new byte[1024];  
	        int len;  
	        os = new FileOutputStream(savePath);  
	        while ((len = is.read(bs)) != -1) 
	        {  
	        	os.write(bs, 0, len);  
	        }
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				if (os != null) 
				{
					os.close();
				}
				if (is != null) 
				{
					is.close();
				}
			} catch (Exception e2){}
		}
    }
	
	public static void download(String fileUrl, String filename, String savePath) throws Exception 
	{  
		URL url = new URL(fileUrl);  
        URLConnection con = url.openConnection();  
        con.setConnectTimeout(20 * 1000);  
        InputStream is = con.getInputStream();  
      
        byte[] bs = new byte[1024];  
        int len;  
        File sf=new File(savePath);  
        if(!sf.exists())
        {
        	sf.mkdirs();  
        }  
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        while ((len = is.read(bs)) != -1) 
        {  
        	os.write(bs, 0, len);  
        }  
        os.close();  
        is.close();  
    }
    
}
