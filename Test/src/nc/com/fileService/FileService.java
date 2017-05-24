package nc.com.fileService;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;
import nc.com.utils.StringUtil;

/**
 * 本地文件服务器存储服务 
 */
public class FileService {

	private static FileService service;

	private String fileServerDomain;
	private String fileServerPath;
	private String appName;
	
	private FileService(){}
	
	/**
	 * confix.txt 配置
	 * <br>
	   #uploaded file config
	   fileServerHostAndPort=http://192.168.31.248:8080
	   webAppsAbsolutePath=/Users/leigl/work/java/server/apache-tomcat-7.0.57/webapps
	   fileAppName=JFinalTmpIMG
	 * <br>
	    AppConst.init() 调用
	    <br>
		String fileServerHostAndPort = PropKit.get("fileServerHostAndPort");
		String webAppsAbsolutePath = PropKit.get("webAppsAbsolutePath");
		String fileAppName = PropKit.get("fileAppName");
		FileService.init(fileServerHostAndPort, webAppsAbsolutePath, fileAppName);
	 */
	public static void init(String fileServerHostAndPort, String webAppAbsolutePath, String appName)
	{
		if(service == null)
		{
			service = new FileService();
			service.fileServerDomain = fileServerHostAndPort;
			service.fileServerPath = webAppAbsolutePath;
			service.appName = appName;
			
			File appFolder = new File(service.fileServerPath + "/" + service.getAppName());
			if (!appFolder.exists()) 
			{
				appFolder.mkdir();
			}
		}
	}
	
	public static String getFileKey(SavedFile savedFile)
	{
		return getFileKey(savedFile.getDownloadLink());
	}
	
	
	/**
	 * @return folder/name
	 */
	private static String getFileKey(String downloadLink)
	{
		String serverPath = null;
		if (service.getFileServerDomain().endsWith("/")) 
		{
			serverPath = service.getFileServerDomain() + service.getAppName();
		}else 
		{
			serverPath = service.getFileServerDomain() + "/" + service.getAppName();
		}
		String key = downloadLink.replace(serverPath, "");
		return key.startsWith("/") ? key.replaceFirst("/", "") : key;
	}
	
	
	/**
	 * 将文件移动到文件服务器中
	 * @param saveFolder
	 * @param srcFile
	 * @return 下载链接
	 */
	public static SavedFile uploadTo(String saveFolder, File srcFile)
	{
		//创建保存目录
		String savePath = getFolderSavePath(saveFolder);
		
		if(srcFile == null || !srcFile.exists())
		{
			return null;
		}
		//生成新名称
		String destFilePath = savePath + generateNewName(srcFile.getName());
		//文件名称已存在，生成新的名称
		boolean fileExisted = true;
		File destFile = null;
		while (fileExisted) 
		{
			destFile = new File(destFilePath);
			if(destFile.exists())
			{
				destFilePath = savePath + generateNewName(srcFile.getName());
			}else {
				fileExisted = false;
			}
		}
		//移动文件
		try {
			FileUtil.move(srcFile, destFile);
			String relativeLink = generateRelativeLink(saveFolder, destFile.getName());
			return new SavedFile(getService().getFileServerDomain(), relativeLink, destFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @param fileExt  .jpg/jpg
	 */
	public static SavedFile gemerateSavedFile(String saveFolder, String fileExt) 
	{
		//创建保存目录
		String savePath = getFolderSavePath(saveFolder);
		fileExt = fileExt.startsWith(".") ? fileExt : "." + fileExt;
		String destFilePath = savePath + generateNewName(fileExt);
		
		//文件名称已存在，生成新的名称
		boolean fileExisted = true;
		File destFile = null;
		while (fileExisted) 
		{
			destFile = new File(destFilePath);
			if(destFile.exists())
			{
				destFilePath = savePath + generateNewName(fileExt);
			}else {
				fileExisted = false;
			}
		}
		String relativeLink = generateRelativeLink(saveFolder, destFile.getName());
		return new SavedFile(getService().getFileServerDomain(), relativeLink, destFile.getAbsolutePath());
	}
	
	public static String getFolderSavePath(String saveFolder) 
	{
		String savePath = new StringBuilder().append(getService().getFileServerPath())
											 .append("/")
											 .append(getService().getAppName())
											 .append("/")
											 .append(saveFolder)
											 .append("/")
											 .toString();
		File saveDir = new File(savePath);
		if(!saveDir.exists())
		{
			saveDir.mkdirs();
		}
		return savePath;
	}
	
	private static FileService getService()
	{
		if (service == null) 
		{
			try {
				throw new Exception("FileSaverService 尚未初始化，请先调用 init 方法进行初始化");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return service;
	}
	
	
	private static String generateRelativeLink(String saveFolder, String name) 
	{
		String link = "/" + getService().getAppName() + "/" + saveFolder + "/" + name;
		link = link.replace("\\","/");
		link = link.replaceAll("//", "/");
		return link;
	}
	
	
	public static String generateNewName(String fileName) 
	{
		String newName = StringUtil.uniqueStrWithDateTime();
		int index = fileName.lastIndexOf(".");
		String extName = fileName.substring(index);
		 
		return newName + extName;
	}
	
	
	public String getFileServerPath() {
		return fileServerPath;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getFileServerDomain() {
		return fileServerDomain;
	}
	
	public static void delete(String saveFolderName, File soft) 
	{
		File folder = getSaveFolder(saveFolderName);
		File destFile = new File(folder.getAbsolutePath() + "/" + soft.getName());
		if(destFile.exists())
		{
			destFile.delete();
		}
	}
	
	private static File getSaveFolder(String saveFolderName)
	{
		String savePath = new StringBuilder().append(getService().getFileServerPath())
											 .append("/")
											 .append(getService().getAppName())
											 .append("/")
											 .append(saveFolderName)
											 .toString();
		File saveDir = new File(savePath);
		if(!saveDir.exists())
		{
			saveDir.mkdir();
		}
		return saveDir;
	}
}
