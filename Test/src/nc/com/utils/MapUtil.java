package nc.com.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class MapUtil <T> {
	
	/**
	 * 添加一个键值对到所有的Map中
	 * @param maps
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map> addAttributeToMaps(List<Map> maps, String key, String value)
	{
		for (Map map : maps) 
		{
			map.put(key, value);
		}
		return maps;
	}
	
	/**
	 * 从所有的 Map 中提取属性为 key 的值
	 * @param maps
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List extractAttributeFromMaps(List<Map> maps, String key)
	{
		List attributes = new ArrayList();
		for (Map map : maps) {
			attributes.add(map.get(key));
		}
		
		return attributes;
	}
	
	/**
	 * 改变Map的键名
	 * @param maps
	 * @param sourceKey
	 * @param targetKey
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map> changeKeyOfMaps(List<Map> maps,
			String sourceKey, String targetKey) {
		for (Map map : maps) 
		{
			if(map.containsKey(sourceKey))
			{
				map.put(targetKey, map.get(sourceKey));
				map.remove(sourceKey);
			}
		}
		return maps;
	}
	/**
	 * map 转化为 list
	 * @param map
	 * @return
	 */
	public static <K, V> List<V> toList(Map<K, V> map) 
	{
		List<V> list = new ArrayList<V>();
		
		Set<K> keys = map.keySet();
		for (K key : keys) 
		{
			list.add(map.get(key));
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public static <K, V> void replaceKeys(Map<K, V> map,
			String[] oldKeys, String[] newKeys) 
	{
		if(map == null || oldKeys == null || newKeys == null)
		{
			return;
		}
		
		for (int i = 0; i < oldKeys.length; i++) 
		{
			if(i + 1 > newKeys.length)
			{
				//防止newKeys数组越界
				return;
			}
			String oldKey = oldKeys[i];
			
			if(map.containsKey(oldKey))
			{
				V value = map.get(oldKey);
				map.put((K)newKeys[i], value);
				map.remove(oldKey);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map toStrKey(Map originMap) 
	{
		if (originMap == null || originMap.isEmpty()) 
		{
			return new HashMap();
		}
		Map newMap = new HashMap();
		for (Object key : originMap.keySet()) 
		{
			newMap.put(key.toString(), originMap.get(key));
		}
		return newMap;
	}
	
}