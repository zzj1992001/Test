package nc.com.utils;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class Thumbnail{
	
	public static File thumb(File img, int width, int height)
	{
		if(img == null || !img.exists())
		{
			System.err.println("输入图片不存在");
			return null;
		}
		String targetImgPath = appendExtraToFileName(img.getAbsolutePath(), "_thumb");
		try {
			Thumbnails.of(img).size(width, height).toFile(targetImgPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new File(targetImgPath);
	}
	
	public static File thumb(String srcImg, int width, int height)
	{
		File img = new File(srcImg);
		return thumb(img, width, height);
	}
	
	/**
	 * 生成新的文件名
	 */
	public static String appendExtraToFileName(String fileName, String extra) 
	{
		int index = fileName.lastIndexOf(".");
		String name = fileName.substring(0, index);
		String ext = fileName.substring(index);
		return new StringBuilder().append(name).append(extra).append(ext).toString();
	}
	
	
	public static void main(String[] args) {
		String srcImg = "E://移动端2.jpg";
		thumb(srcImg, 640, 640);
	}
}
