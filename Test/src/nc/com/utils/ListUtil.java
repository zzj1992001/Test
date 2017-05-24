package nc.com.utils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.bean.BeanUtil;
import jodd.typeconverter.Convert;
import jodd.typeconverter.TypeConverterManager;
import jodd.util.StringUtil;
public class ListUtil {
	
	/**
	 * 查找整数List中的最小整数
	 * @param ints
	 * @return
	 */
	public static int minIntInList(List<Integer> ints)
	{
		int min = ints.get(0);
		for (Integer integer : ints) 
		{
			if(min > integer)
			{
				min = integer;
			}
		}
		return min;
	}
	
	@SuppressWarnings("unchecked")
	public static <T,K> List<K> extractUniquePropertyOfBeans(List<T> beans, String propertyName, Class<K> cls)
	{
		Set<K> set = new HashSet<K>();
		for (T bean : beans) 
		{
			K propertyValue = (K)BeanUtil.getPropertySilently(bean, propertyName);
			set.add(propertyValue);
		}
		List<K> list = new ArrayList<K>();
		for (K object : set) 
		{
			list.add(object);
		}
		return list;
	}
	
	public static <T> List<Object> extractUniquePropertyOfBeans(List<T> beans, String propertyName){
		Set<Object> set = new HashSet<Object>();
		for (T bean : beans) 
		{
			Object propertyValue = BeanUtil.getPropertySilently(bean, propertyName);
			set.add(propertyValue);
		}
		List<Object> list = new ArrayList<Object>();
		for (Object object : set) 
		{
			list.add(object);
		}
		return list;
	}
	
	/**
	 * 抽取某个属性
	 * @param beans
	 * @param property
	 * @param cls
	 * @return
	 */
	public static <T,K> List<K> extractPropertyOfBeans(List<T> beans, String property,Class<K> cls){
		List<K> list = new ArrayList<K>();
		for (T t : beans) {
			Object propertyValue = BeanUtil.getPropertySilently(t, property);
			K k = propertyValue == null ? null : TypeConverterManager.convertType(propertyValue, cls);
			list.add(k);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static <S,K,V> Map<K, V> extractPropertyFromListToMap(List<S> beans, String propertyNameOfKey, String propertyNameOfValue, Class<K> keyType, Class<V> valueType)
	{
		Map<K, V> map = new HashMap<K, V>();
		for (S s : beans) 
		{
			K key = ((K)BeanUtil.getPropertySilently(s, propertyNameOfKey));
			V value = (V)BeanUtil.getPropertySilently(s, propertyNameOfValue);
			map.put(key, value);
		}
		return map;
	}
	
	public static <T> Map<Object, T> listToMap(List<T> list, String propertyName)
	{
		Map<Object, T> map = new HashMap<Object, T>();
		for (T t : list) 
		{
			Object propertyValue = BeanUtil.getPropertySilently(t, propertyName);
			if(!map.containsKey(propertyValue))
			{
				map.put(propertyValue, t);
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static <T, K> Map<K, T> listToMap(List<T> list, String propertyName, Class<K> classOfMapKey)
	{
		Map<K, T> map = new HashMap<K, T>();
		for (T t : list) 
		{
			K propertyValue = ((K)BeanUtil.getPropertySilently(t, propertyName));
			if(!map.containsKey(propertyValue))
			{
				map.put(propertyValue, t);
			}
		}
		return map;
	}
	
	/**
	 * 根据某个属性分组
	 * @param list
	 * @param propertyName
	 * @return
	 */
	public static <T> Map<Object, List<T>> groupByProperties(List<T> list, String propertyName){
		Map<Object, List<T>> map = new HashMap<Object, List<T>>();
		for (T t : list) {
			Object propertyValue = BeanUtil.getPropertySilently(t, propertyName);
			if(map.containsKey(propertyValue)){
				map.get(propertyValue).add(t);
			}else{
				List<T> tmpList = new ArrayList<T>();
				tmpList.add(t);
				map.put(propertyValue, tmpList);
			}
		}
		return map;
	}
	
	/**
	 * 按两个属性分组
	 * @param list
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> Map<Object, Map<Object, T>> uniqueGroupByTwoProperties(List<T> list, String first,String second){
		Map<Object, Map<Object, T>> map = new HashMap<Object, Map<Object, T>>();
		for (T t : list) {
			Object firstProperty = BeanUtil.getPropertySilently(t, first);
			Object secondProperty = BeanUtil.getPropertySilently(t, second);
			if(map.containsKey(firstProperty)){
				map.get(firstProperty).put(secondProperty,t);
			}else{
				Map<Object, T> tmp = new HashMap<Object, T>();
				tmp.put(secondProperty, t);
				map.put(firstProperty, tmp);
			}
		}
		return map;
	}
	/**
	 * 按两个属性分组
	 * @param list
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> Map<Object, Map<Object, List<T>>> groupByTwoProperties(List<T> list, String first,String second){
		Map<Object, Map<Object, List<T>>> map = new HashMap<Object, Map<Object, List<T>>>();
		for (T t : list) {
			Object firstProperty = BeanUtil.getPropertySilently(t, first);
			Object secondProperty = BeanUtil.getPropertySilently(t, second);
			if(map.containsKey(firstProperty)  && map.get(firstProperty).containsKey(secondProperty)){
					map.get(firstProperty).get(secondProperty).add(t);
			}else if(map.containsKey(firstProperty)){
				List<T> tmp = new ArrayList<T>();
				tmp.add(t);
				map.get(firstProperty).put(secondProperty, tmp);
			}else{
				Map<Object, List<T>> tmpMap = new HashMap<Object, List<T>>();
				List<T> tmp = new ArrayList<T>();
				tmp.add(t);
				tmpMap.put(secondProperty, tmp);
				map.put(firstProperty, tmpMap);
			}
		}
		return map;
	}
	/**
	 * 求平均
	 * @param list
	 * @return
	 */
	public static <T> double avg(List<T> list){
		double avg = 0;
		for (T value : list) {
			double val = value == null ? 0 : Convert.toDouble(value, 0d);
			avg += val;
		}
		int length = list.size() == 0 ? 1 : list.size();
		return avg/length;
	}
	public static <T> double sum(List<T> list){
		double sum = 0;
		for (T value : list) {
			double val = value == null ? 0 : Convert.toDouble(value, 0d);
			sum += val;
		}
		return sum;
	}
	
	/**
	 * arr转list
	 * @param arr
	 * @return
	 */
	public static <T> List<T> arrayToList(T[] arr){
		List<T> tmp = new LinkedList<T>();
		if(arr == null){
			return tmp;
		}
		return Arrays.asList(arr);
	}
	public static List<Double> arrayToList(double[] arr){
		if(arr == null){
			return new ArrayList<Double>();
		}
		List<Double> tmp = new LinkedList<Double>();
		for (double value : arr) {
			tmp.add(value);
		}
		return tmp;
	}
	
	/**
	 * list转array
	 * @param list
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArr(List<T> list,Class<T> cls){
		if(list == null || list.isEmpty()){
			return null;
		}
		T[] tmp = (T[]) Array.newInstance(cls, list.size());
		return list.toArray(tmp);
	}
	
	public static <V> String joinToStr(List<V> strList, String seperator) 
	{
		if(strList == null || strList.size() <= 0)
		{
			return "";
		}
		StringBuilder strBuilder = new StringBuilder();
		for (V str : strList) 
		{
			strBuilder.append(str).append(seperator);
		}
		return strBuilder.substring(0, strBuilder.length() - seperator.length());
	}
	
	public static boolean contains(List<String> mainList, List<String> subList) 
	{
		if(mainList == null || subList == null || mainList.size() < subList.size())
		{
			return false;
		}
		
		for (int i = 0 ; i < subList.size(); i++) 
		{
			if(!mainList.get(i).equals(subList.get(i)))
			{
				return false;
			}
		}
		return true;
	}
	public static String[]  toStringArray(Collection<?> col) {
		if(col == null || col.size() == 0){
			return null;
		}
		String[] tmp = new String[col.size()];
		int tmpi = 0;
		for (Object obj : col) {
			tmp[tmpi++] = StringUtil.toSafeString(obj);
		}
		return tmp;
	}
	
	/**
	 * 给list中的对象属性赋值
	 * @param itemList 需要赋值的对象列表
	 * @param itemKey 赋值对象的标识属性
	 * @param itemProName 需要赋值的属性名称
	 * @param proValueList 携带值的对象列表
	 * @param getProName 携带值的属性名称
	 * @param valueKey 值对象的标识属性
	 */
	public static <T, K> void setListItemProperty(List<T> itemList, String itemKeyProName, String itemProName, List<K> proValueList, String valueKey, String getProName)
	{
		Map<Object, K> proValueMap = listToMap(proValueList, valueKey);
		for (T t : itemList) 
		{
			Object itemKey = BeanUtil.getDeclaredPropertySilently(t, itemKeyProName);
			K k = proValueMap.get(itemKey);
			Object itemProValue = k == null ? null : BeanUtil.getDeclaredPropertySilently(k, getProName);
			BeanUtil.setDeclaredPropertyForcedSilent(t, itemProName, itemProValue);
		}
	}
	
	/**
	 * 根据属性过滤 beans
	 */
	public static <T> List<T> filterByProperty(List<T> beans, String propertyName, Object propertyValue) 
	{
		if(beans == null || beans.isEmpty())
		{
			return new ArrayList<T>();
		}
		
		List<T> filterList = new ArrayList<T>();
		for (T bean : beans) 
		{
			Object value = BeanUtil.getPropertySilently(bean, propertyName);//BeanUtil.getDeclaredPropertySilently(bean, propertyName);
			if((value != null && value.equals(propertyValue)) || value == propertyValue)
			{
				filterList.add(bean);
			}
		}
		return filterList;
	}
	public static List<Integer> toIntegerList(String strToSeperate, String dilimer) 
	{
		if(strToSeperate == null || "".equals(strToSeperate))
		{
			return new ArrayList<Integer>();
		}
		return toIntegerList(strToSeperate.split(dilimer));
	}
	
	public static List<Integer> toIntegerList(String[] intElements) 
	{
		List<Integer> ints = new ArrayList<Integer>();
		for (String element : intElements) 
		{
			if(!"".equals(element))
			{
				ints.add(Integer.valueOf(element));
			}
		}
		return ints;
	}
	
	/**
	 * 移除所有大写的键
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> removeAllUperKeys(List<Map<String, Object>> list){
		List<Map<String, Object>> tmpList = new LinkedList<Map<String,Object>>();
		for (Map<String, Object> map : list) {
			Set<String> keys = map.keySet();
			Map<String, Object> tmp = new HashMap<String, Object>();
			for (String key : keys) {
				if(!StringUtil.equals(key, StringUtil.capitalize(key))){
					tmp.put(key, map.get(key));
				}
			}
			tmpList.add(tmp);
		}
		return tmpList;
	}
	
	public static List<Map<String, Object>> getWantedValueByKeys(
			List<? extends Object> list,
			String... wantedKeys){
		
		List<Map<String, Object>> tmp = new LinkedList<Map<String,Object>>();
		for (Object object : list) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			for (String key : wantedKeys) {
				tmpMap.put(key, BeanUtil.getPropertySilently(object, key));
			}
			tmp.add(tmpMap);
		}
		return tmp;
	}
	
	public static List<Map<String, Object>> getWantedValueByKeys(
			List<? extends Object> list,
			String[] wantedKeys,
			String[] alias){
		
		List<Map<String, Object>> tmp = new LinkedList<Map<String,Object>>();
		for (Object object : list) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			for (int i = 0; i < wantedKeys.length; i++) {
				String key = i < alias.length ? alias[i] : wantedKeys[i];
				tmpMap.put(key, BeanUtil.getPropertySilently(object, wantedKeys[i]));
			}
			tmp.add(tmpMap);
		}
		return tmp;
	}
	/**
	 * 向上转型
	 */
	public static <T> List<T> covertTypeUp(List<? extends T> beans, Class<T> targetType) 
	{
		List<T> newBeans = new ArrayList<T>();
		for (T evaluatorImported : beans) 
		{
			newBeans.add(evaluatorImported);
		}
		return newBeans;
	}
	
	/*public static<T> List<T> removeBy(List<T> list, String propertyName, Object... values) 
	{
		Map<Object, T> map = listToMap(list, propertyName);
		for (Object key : values) 
		{
			if(map.containsKey(key))
			{
				map.remove(key);
			}
		}
		return MapUtil.toList(map);
	}*/
	
	/*@SuppressWarnings("unchecked")
	public static <T, V> Map<String, V> extractPropertyToMap(List<T> beans, String valueName, Class<V> valueClass, MultipleKey multipleKey) 
	{
		Map<String, V> map = new HashMap<String, V>();
		for (T s : beans) 
		{
			List<String> nameList = new ArrayList<String>();
			for (String name : multipleKey.getNames()) 
			{
				nameList.add(BeanUtil.getPropertySilently(s, name).toString());
			}
			V value = (V)BeanUtil.getPropertySilently(s, valueName);
			map.put(joinToStr(nameList, multipleKey.getDilimer()), value);
		}
		return map;
	}*/
	
	/**
	 * both fromIndex and toIndex are included.
	 * @param greatCourses
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public static <T> List<T> sub(List<T> greatCourses, int fromIndex, int toIndex) 
	{
		if(greatCourses == null)
		{
			return new ArrayList<T>();
		}
		
		List<T> newList = new ArrayList<T>();
		for (int i = 0; i < greatCourses.size(); i++) 
		{
			if(i >= fromIndex && i <= toIndex)
			{
				newList.add(greatCourses.get(i));
			}
		}
		return newList;
	}
	public static boolean contains(List<String> list, String value)
	{
		for (String val : list) 
		{
			if(val.equals(value))
			{
				return true;
			}
		}
		return false;
	}
	
	public static List<Map<String, Object>> sortDesc(List<Map<String, Object>> trainees, String key) 
	{
		if(trainees == null)
		{
			return new ArrayList<Map<String,Object>>();
		}
		
		for (int i = 0; i < trainees.size() - 1; i++) 
		{
			if(trainees.get(i) == null)
			{
				continue;
			}
			
			for (int j = i + 1; j < trainees.size(); j++) 
			{
				if (trainees.get(j) == null) 
				{
					continue;
				}
				
				double current = Convert.toDouble(trainees.get(i).get(key));
				double next = Convert.toDouble(trainees.get(j).get(key));
				if(current < next)
				{
					Map<String, Object> temp = trainees.get(j);
					trainees.set(j, trainees.get(i));
					trainees.set(i, temp);
				}
			}
		}
		
		return trainees;
	}

	public static List<Integer> filterListInt(List<Integer> values, Set<Integer> excludeValueSet) 
	{
		List<Integer> newValues = new ArrayList<Integer>();
		for (Integer areaId : values) 
		{
			if (!excludeValueSet.contains(areaId)) 
			{
				newValues.add(areaId);
			}
		}
		return newValues;
	}
	
}