package nc.com.utils;

import java.io.File;
import java.util.List;

public class FfmpegUtil {

	

	/**
	 * @param ffmpegPath    "/Users/leigl/Desktop/ffmpeg/ffmpeg"
	 * @param videoPath     "/Users/leigl/Desktop/video/1.mp4"
	 * @param imageSize     "320x240"
	 * @return
	 */
	public static boolean catchImage(String ffmpegPath, String videoPath, String imagePath, String imageSize, Integer startSecond) 
	{
		File file = new File(videoPath);
		if (!file.exists()) 
		{
			throw new IllegalArgumentException("视频不存在：" + videoPath);
		}
		
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(videoPath);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add(startSecond.toString());
		commands.add("-t");
		commands.add("0.001");
		commands.add("-s");
		commands.add(imageSize);
		commands.add(imagePath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.redirectErrorStream(true);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		String ffmpegPath = "/Users/leigl/Desktop/ffmpeg/ffmpeg";
		String path = "/Users/leigl/Desktop/video/1.mp4";
		String imagePath = path.substring(0, path.lastIndexOf(".")) + ".jpg";
		String size = "100x100";
		Integer startSecond = 0;
		catchImage(ffmpegPath, path, imagePath, size, startSecond);
	}
}
