package nc.com.kits;




public class LazyLoadImgKit {

	
	private static String lazyAttr = "data-url";
	
	public static String encode(String html, String defautlImgUrl)
	{
		int index = -1;
		int fromIndex = 0;
		String pattern = "<img src=\"";
		while ((index = html.indexOf(pattern, fromIndex)) >= 0) 
		{
			int end = html.indexOf("\"", index + pattern.length() + 1);
			String srcImg = html.substring(index, end + 1);
			
			String lazyImg = srcImg.replaceFirst("src", lazyAttr);
			StringBuilder newImg = new StringBuilder(lazyImg);
			newImg.append(buildDefautImg(defautlImgUrl));
			
			html = html.replaceFirst(srcImg, newImg.toString());
			fromIndex = index + newImg.length();
		}
		return html;
	}
	
	public static String decode(String html, String defautlImgUrl)
	{
		String removeSrc = buildDefautImg(defautlImgUrl);
		html = html.replace(removeSrc, "");
		return html.replace(lazyAttr + "=", "src=");
	}
	
	private static String buildDefautImg(String defautlImgUrl)
	{
		return new StringBuilder().append(" src=\"").append(defautlImgUrl).append("\"").toString();
	}
	
	
	/*public static void main(String[] args) {
		String html = "<p style=\"text-align:center;\"><span><img src=\"http://192.168.31.248:8080/UBCIMG/newsImg/1429230800388_9218_3030_6175.jpg\" alt=\"\" /><br /></span></p><p style=\"text-align:center;\"><span><img src=\"http://192.168.31.248:8080/UBCIMG/newsImg/1429230800388_9218_3030_6175.jpg\" alt=\"\" /><br /></span></p>";
		String encode = encode(html, AppConst.DEFAULT_NEWS_DETAIL_IMG);
		System.out.println("格式化：" + encode);
		String decode = decode(encode, AppConst.DEFAULT_NEWS_DETAIL_IMG);
		System.out.println("还原：" + decode);
		System.out.println(html.equals(decode));
	}*/
}
