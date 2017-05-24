package nc.com.fileService;

public class SavedFile {
	
	private String server;//服务器访问地址：http://host:80
	private String filePath;//文件相对服务器根目录路径
	private String absolutePath;//文件的磁盘路径
	
	public SavedFile(String server, String filePath, String absolutePath) 
	{
		this.server = server;
		this.filePath = filePath;
		this.absolutePath = absolutePath;
	}
	public String getServer() {
		return server;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getDownloadLink() {
		return server + filePath;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	
	
}
