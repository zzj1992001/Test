package nc.com;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.com.exception.BizzException;
import nc.com.fileService.FileService;
import nc.com.fileService.SavedFile;
import nc.com.kits.HttpRequestKit;
import nc.com.utils.DateUtil;
import nc.com.utils.StringUtil;
import nc.com.utils.Thumbnail;
import nc.com.utils.TypeConverter;
import Test.common.AppConst;
import nc.com.AppImage;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class BaseController extends Controller{

	/**
	 * 读取session中的userId
	 */
	public int getSessionAdminUserId()
	{
		Integer userId = AppSession.getAdminUserId(getSession());
		return userId == null ? 0 : userId;
	}
	
	/**
	 * 读取session中的userId
	 */
	public int getSessionUserId()
	{
		Integer userId = AppSession.getUserId(getSession());
		//return userId == null ? getParaToInt(TestKit.KEY_DEBUG_SESSION_USER_ID, 7) : userId;
		return userId == null ? 0 : userId;
	}
	
	/**
	 * 读取移动端传输的userId
	 */
	/*public int getMobileUserId() 
	{
		return getParaToInt("_userId");
	}*/
	
	
	/**
	 * 将参数转为Date
	 * 默认格式为 yyyy-MM-dd HH:mm:ss
	 */
	public Date getParaToDateTime(String paraName)
	{
		return getParaToDateTime(paraName, DateUtil.REG_YYMMDD_HHMMSS);
	}
	
	/**
	 * 将参数转为Date
	 */
	public Date getParaToDateTime(String paraName, String patterm)
	{
		String date = getPara(paraName);
		if(date != null && !"".equals(date))
		{
			return DateUtil.getDateOf(date, patterm);
		}
		return null;
	}
	
	/**
	 * 将日期＋时间参数转为Date
	 * 默认格式为 yyyy-MM-ddHH:mm
	 */
	public Date getParasToDate(String paramDate, String paramTime)
	{
		return getParasToDate(paramDate, paramTime, "yyyy-MM-ddHH:mm");
	}
	
	public Date getParasToDate(String paramDate, String paramTime, String format)
	{
		String dateStr = getPara(paramDate);
		String timeStr = getPara(paramTime);
		Date dateTime = DateUtil.getDateOf(dateStr + timeStr, format);
		return dateTime;
	}
	
	
	
	public double getParaToDouble(String paraName)
	{
        String value = getPara(paraName);
        return Double.valueOf(value);
    }
    
    public double getParaToDouble(String paraName, double defaultValue)
    {
        String value = getPara(paraName);
        if(value == null || "".equals(value.trim()))
        {
            return defaultValue;
        }
        double tmp = defaultValue;
        try{
            tmp = Double.valueOf(value);
        }catch(Exception e){
            tmp = defaultValue;
        }
        return tmp;
    }
    
    public BigDecimal getParaToBigDecimal(String paraName)
    {
        String value = getPara(paraName);
        if (value == null || value.isEmpty()) 
        {
			return null;
		}
        try {
			return (BigDecimal)TypeConverter.convert(BigDecimal.class, value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public BigDecimal getParaToBigDecimal(String paraName, int defaultValue)
    {
        String value = getPara(paraName);
        if(value == null || "".equals(value.trim()))
        {
            return new BigDecimal(defaultValue);
        }
        try {
			return (BigDecimal)TypeConverter.convert(BigDecimal.class, value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return new BigDecimal(defaultValue);
    }
    
    /**
	 * 返回保存文件的网络访问地址
	 */
	public List<String> getUploadFiles(String folder) 
	{
		List<UploadFile> images = getFiles();
		 
		if(images == null || images.isEmpty())
		{
			return new ArrayList<String>();
		}
		
		List<String> imageLinks = new ArrayList<String>();
		for (UploadFile image : images) 
		{
			SavedFile savedImg = FileService.uploadTo(folder, image.getFile());
			imageLinks.add(savedImg.getDownloadLink());
		}
		return imageLinks;
	}
	
	public List<AppImage> getAndThumbUploadImages(String folder, int thumbWidth, int thumbHeight)
	{
		if (isNormalForm()) 
		{
			return new ArrayList<AppImage>();
		}
		
		List<UploadFile> images = getFiles();
		 
		if(images == null || images.isEmpty())
		{
			return new ArrayList<AppImage>();
		}
		
		List<AppImage> appImages = new ArrayList<AppImage>();
		for (UploadFile image : images) 
		{
			File thumbImage = Thumbnail.thumb(image.getFile(), thumbWidth, thumbHeight);
			
			SavedFile savedOriginImg = FileService.uploadTo(folder, image.getFile());
			SavedFile savedThumbImg = FileService.uploadTo(folder, thumbImage);
			
			AppImage appImage = AppImage.create(savedOriginImg.getDownloadLink(), savedThumbImg.getDownloadLink());
			appImages.add(appImage);
		}
		return appImages;
	}
	
	public AppImage getAndThumbUploadImage(String imageParam, String folder, int thumbWidth, int thumbHeight)
	{
		if (isNormalForm()) 
		{
			return null;
		}
		
		UploadFile image = getFile(imageParam);
		 
		if(image == null)
		{
			return null;
		}
		
		File thumbImage = Thumbnail.thumb(image.getFile(), thumbWidth, thumbHeight);
		
		SavedFile savedOriginImg = FileService.uploadTo(folder, image.getFile());
		SavedFile savedThumbImg = FileService.uploadTo(folder, thumbImage);
		
		return AppImage.create(savedOriginImg.getDownloadLink(), savedThumbImg.getDownloadLink());
	}
    
	
	public List<AppImage> getUploadImages(String folder)
	{
		if (isNormalForm()) 
		{
			return new ArrayList<AppImage>();
		}
		
		List<UploadFile> images = getFiles();
		 
		if(images == null || images.isEmpty())
		{
			return new ArrayList<AppImage>();
		}
		
		List<AppImage> appImages = new ArrayList<AppImage>();
		for (UploadFile image : images) 
		{
			SavedFile savedOriginImg = FileService.uploadTo(folder, image.getFile());
			AppImage appImage = AppImage.create(savedOriginImg.getDownloadLink(), savedOriginImg.getDownloadLink());
			appImages.add(appImage);
		}
		return appImages;
	}
	
	public SavedFile getUploadFile(String fileParamName, String folder)
	{
		if (isNormalForm()) 
		{
			return null;
		}
		
		UploadFile image = getFile(fileParamName);
		 
		if(image == null)
		{
			return null;
		}
		SavedFile savedFile = FileService.uploadTo(folder, image.getFile());
		return savedFile;
	}
	
	
	public boolean checkImage(SavedFile image, String errorJSP) 
	{
		if (image == null) 
		{
			renderHtmlFail("图片不能为空", errorJSP);
			return false;
		}
		if (!image.getFilePath().endsWith(".png") && !image.getFilePath().endsWith(".jpg")) 
		{
			renderHtmlFail("图片格式只能是 .png / .jpg", errorJSP);
			return false;
		}
		return true;
	}
	
	public boolean checkMp4(SavedFile video, String errorJSP) 
	{
		if (video == null) 
		{
			renderHtmlFail("视频不能为空", errorJSP);
			return false;
		}
		
		if (!video.getFilePath().endsWith(".mp4")) 
		{
			renderHtmlFail("视频格式只能是 .mp4", errorJSP);
			return false;
		}
		return true;
	}
	
	public boolean checkMp3(SavedFile video, String errorJSP) 
	{
		if (video == null) 
		{
			renderHtmlFail("音频不能为空", errorJSP);
			return false;
		}
		
		if (!video.getFilePath().endsWith(".mp3")) 
		{
			renderHtmlFail("音频格式只能是 .mp3", errorJSP);
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 返回请求成功信息
	 */
	public void renderSuccess()
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_SUCCESS);
		setAttr(AppConst.RESPONSE_KEY_MSG, AppConst.RESPONSE_VALUE_DEFAULT_MSG);
		renderJson();
	}
	
	/**
	 * 返回请求成功信息
	 */
	public void renderSuccess(String msg)
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_SUCCESS);
		setAttr(AppConst.RESPONSE_KEY_MSG, msg != null ? msg : AppConst.RESPONSE_VALUE_DEFAULT_MSG);
		renderJson();
	}
	
	/**
	 * 返回请求失败信息
	 */
	public void renderFail()
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_FAIL);
		setAttr(AppConst.RESPONSE_KEY_MSG, AppConst.RESPONSE_VALUE_DEFAULT_MSG);
		renderJson();
	}
	
	/**
	 * 返回请求失败信息
	 */
	public void renderFail(String msg)
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_FAIL);
		setAttr(AppConst.RESPONSE_KEY_MSG, msg != null ? msg : AppConst.RESPONSE_VALUE_DEFAULT_MSG);
		renderJson();
	}
	
	public void renderUnAuth()
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_UNAUTHENTICATION);
		setAttr(AppConst.RESPONSE_KEY_MSG, "当前会话失效，请重新登录");
		renderJson();
	}
	
	
	public void renderHtmlSuccess(String msg, String html) 
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_SUCCESS);
		setAttr(AppConst.RESPONSE_KEY_MSG, msg);
		render(html);
	}
	
	public void renderHtmlFail(String msg, String html) 
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_FAIL);
		setAttr(AppConst.RESPONSE_KEY_MSG, msg);
		render(html);
	}
	
	public void render404()
	{
		setAttr(AppConst.RESPONSE_KEY_CODE, AppConst.RESPONSE_VALUE_CODE_404);
		setAttr(AppConst.RESPONSE_KEY_MSG, AppConst.RESPONSE_VALUE_DEFAULT_MSG);
		renderJson();
	}
	
	/**
	 * 重写父类方法，返回当前请求URL
	 */
	public void render(String view)
	{
		HttpServletRequest requst = getRequest();
		
		String currentUrl = HttpRequestKit.currrentUrl(requst);
		requst.setAttribute(AppConst.PARAM_KEY_CURRENT_URL, currentUrl);
		
		super.render(view);
	}
	
	public void handleExpWithTx(Exception e) 
	{
		if (!(e instanceof BizzException)) 
		{
			e.printStackTrace();
		}
		renderFail(e.getMessage());
		throw new RuntimeException(e.getMessage());
	}
	
	public void handleExp(Exception e) 
	{
		if (!(e instanceof BizzException)) 
		{
			e.printStackTrace();
		}
		renderFail(e.getMessage());
	}
	
	
    public boolean isNormalForm() 
	{
    	return !HttpRequestKit.isMutipartForm(getRequest());
	}
    
	
	
	/**
	 * 分页查询
	 */
	public Page<Record> page(String select, String where)
	{
		return page(select, where, new Object[0]);
	}
	
	/**
	 * 分页查询
	 */
	public Page<Record> page(String select, String where, Object...params)
	{
		Integer pageNumber = getParaToInt("pageNum");
		pageNumber = pageNumber == null ? AppConst.PAGE_NUM_DEFAULT : pageNumber;
		
		Integer pageSize = getParaToInt("pageSize");
		pageSize = pageSize == null ? AppConst.PAGE_SIZE_DEFAULT : pageSize;
		
		Page<Record> page = Db.paginate(pageNumber, pageSize, select, where, params);
		
		setAttr("list", page.getList());
		setAttr("pageNum", page.getPageNumber());
		setAttr("pageSize", page.getPageSize());
		setAttr("totalRow", page.getTotalRow());
		setAttr("totalPage", page.getTotalPage());
		setPageUrl();
		return page;
	}
	
	private void setPageUrl() 
	{
		String pageUrl = getRequest().getRequestURL().toString();
		String params = getRequest().getQueryString();
		if(params != null && params.trim().length() > 0)
		{
			pageUrl = StringUtil.concat(pageUrl, "?", params);
		}
		setAttr("page_url", pageUrl);
	}
	
	
	
	/**
     * 另一种分页方法
     */
	public List<Record> nextPageDesc(String sql, Object[] params)
	{
		int nextId = Integer.MAX_VALUE;
		return nextPage(sql, nextId, params);
	}
	
	public List<Record> loadMoreDesc(String sql, Object... params)
	{
		int nextId = Integer.MAX_VALUE;
		return nextPage(sql, nextId, params);
	}
	
	public List<Record> nextPageAsc(String sql, Object[] params)
	{
		int nextId = Integer.MIN_VALUE;
		return nextPage(sql, nextId, params);
	}
	
	public List<Record> loadMoreAsc(String sql, Object...params)
	{
		int nextId = Integer.MIN_VALUE;
		return nextPage(sql, nextId, params);
	}
	
	/**
	 * 加载下一页
	 */
	private List<Record> nextPage(String sql, Integer nextId, Object...params)
	{
		if(getPara("nextId") != null)
		{
			nextId = getParaToInt("nextId");
		}
		int pageSize = AppConst.PAGE_SIZE_API_DEFAULT;
		if (getPara("pageSize") != null) 
		{
			pageSize = getParaToInt("pageSize");
		}
		
		sql = sql.replace("??", nextId.toString());
		StringBuilder sqlBuilder = new StringBuilder(sql);
		sqlBuilder.append(" limit ").append(pageSize);
		
		List<Record> list = Db.find(sqlBuilder.toString(), params);
		
		if (list != null && !list.isEmpty()) 
		{
			nextId = list.get(list.size() - 1).getInt("id");
		}
		setAttr("nextId", nextId);
		setAttr("pageSize", pageSize);
		setAttr("list", list);
		return list;
	}
	
	public String buildWhere(String whereSql, Map<String, Object> whereParams, String[] whereConditions, List<Object> buildedParams,  Object...originParams)
	{
		
		if (originParams != null && originParams.length > 0) 
		{
			for (Object param : originParams) 
			{
				buildedParams.add(param);
			}
		}
		
		StringBuilder whereBuilder = new StringBuilder().append(whereSql);
		if(whereParams != null && !whereParams.isEmpty())
		{
			for(int i = 0; i < whereConditions.length; i++)
			{
				if(whereParams.containsKey(whereConditions[i]) && whereParams.get(whereConditions[i]) != null && !whereParams.get(whereConditions[i]).equals(""))
				{
					whereBuilder.append(" ").append(whereConditions[i]).append(" ");
					buildedParams.add(whereParams.get(whereConditions[i]));
				}
			}
		}
		whereSql = whereBuilder.toString();
		return whereSql;
	}
	
}
