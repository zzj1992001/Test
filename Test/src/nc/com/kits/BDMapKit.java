package nc.com.kits;

import java.util.HashMap;
import java.util.Map;


public class BDMapKit {

	
	/**
	 * @return {longitude, latitude}
	 */
	public static Map<String, Double> gcj02ToBd09(double gg_lon, double gg_lat)
	{
		double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;  
		double bd_lat = z * Math.sin(theta) + 0.006;  

		Map<String, Double> map = new HashMap<String, Double>();
		map.put("longitude", bd_lon);
		map.put("latitude", bd_lat);

	    return map;
	}
	
}
