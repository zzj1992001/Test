package nc.com;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import nc.com.BaseModel;

public class AppImage extends BaseModel<AppImage>{
	
	private static final long serialVersionUID = -6967680902549117464L;
	
	public static final AppImage dao = new AppImage();
	
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_THUMB_URL = "thumbUrl";

	//1 用户照片
	public static final int TYPE_USER_PHOTO = 1;
	
	
	public String getUrl()
	{
		return getStr(COLUMN_URL);
	}
	
	public String getThumbUrl()
	{
		return getStr(COLUMN_THUMB_URL);
	}
	
	public static void saveAppImages(int type, int ownerId, List<AppImage> appImages) 
	{
		if (appImages == null || appImages.isEmpty()) 
		{
			return;
		}

		for (AppImage image : appImages) 
		{
			image.set("type", type).set("ownerId", ownerId);
		}
		AppImage.dao.saveList(appImages);
	}
	
	public static void save(int type, int ownerId, List<String> imageLinks) 
	{
		if (imageLinks == null || imageLinks.isEmpty()) 
		{
			return;
		}
		
		List<AppImage> images = new ArrayList<AppImage>();
		for (String link : imageLinks) 
		{
			AppImage image = new AppImage().set(COLUMN_URL, link)
										   .set("type", type)
										   .set("ownerId", ownerId);
			images.add(image);
		}
		AppImage.dao.saveList(images);
	}

	public static void delete(int type, int ownerId) 
	{
		dao.deleteByColumns("type, ownerId", type, ownerId);
	}

	public static AppImage create(String originImgUrl, String thumbImgUrl) 
	{
		return new AppImage().set(COLUMN_URL, originImgUrl).set(COLUMN_THUMB_URL, thumbImgUrl);
	}

	public static List<AppImage> findImagesOf(int type, int ownerId)
	{
		List<AppImage> images = dao.findByColumns("id, url, thumbUrl", "type, ownerId", type, ownerId);
		return images;
	}

	public static void mergeTo(List<Record> list, String eqRecordColumn, String appImageColumnName, int type) 
	{
		dao.mergeModelsTo(list, eqRecordColumn, "id, url, ownerId", "ownerId", appImageColumnName, "type", type);
	}
	
	public static <M extends Model<M>> void mergeToMs(List<M> list, String eqRecordColumn, String appImageColumnName, int type) 
	{
		dao.mergeModelsToMs(list, eqRecordColumn, "id, url, ownerId", "ownerId", appImageColumnName, "type", type);
	}
	
	public static List<AppImage> createImages(String imageUrls, String split) 
	{
		if (imageUrls == null || imageUrls.trim().equals("")) 
		{
			return new ArrayList<AppImage>();
		}
		
		List<AppImage> images = new ArrayList<AppImage>();
		
		String[] urls = imageUrls.split(split);
		for (String url : urls)
		{
			images.add(create(url, url));
		}
		return images;
	}
	
	public static List<AppImage> last50() {
		String sql = "select * from app_image order by id desc limit 50";
		return dao.find(sql);
	}
}
