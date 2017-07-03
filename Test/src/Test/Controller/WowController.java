package Test.Controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Test.entity.Wow;
import nc.com.BaseController;
/*
 * 魔兽直播抓取
 */
public class WowController extends BaseController {

	public void index()
	{
		list();
		render("wow.jsp");
	}
	public void list()
	{
	    String	select = "select * ";
		String where = " from wow";
		page(select, where);
		render("list.jsp");
	}
	
	public void look()
	{
		int id =getParaToInt("id");
		
		setAttr("roomid",id);
		render("look.jsp");
	}
	public static void getWowlist() {
		// TODO Auto-generated method stub

		String url ="https://www.douyu.com/directory/game/WOW";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element wowlist = doc.getElementById("live-list-contentbox");
		Elements wowshowers = wowlist.getElementsByTag("li");
		for (Element wowshower:wowshowers)
		{
			int rid = Integer.parseInt(wowshower.attr("data-rid"));
			Element imgElement = wowshower.select("img").first();
			String title = wowshower.select("a").first().attr("title");
			String img = imgElement.attr("data-original");
			Wow wow = new Wow();
			wow.set("id", rid)
			   .set("rid", rid)
			   .set("title", title)
			   .set("img", img);
			System.out.println(wow);
			wow.save();
		}
	}
}
