package nc.com.jfinalKit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;
import nc.com.BaseModel;
import nc.com.utils.DateUtil;

import com.jfinal.plugin.activerecord.Model;

public class ModelKit {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getToList(List<? extends Model> rs, String column, Class<T> clazz) 
	{
		if(rs == null || rs.isEmpty())
		{
			return new ArrayList<T>();
		}
		
		List<T> list = new ArrayList<T>();
		for (Model r : rs) 
		{
			if (r.get(column) != null) 
			{
				list.add((T)(r.get(column)));
			}
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getUniqueToList(List<? extends Model> rs, String column, Class<T> clazz) 
	{
		if(rs == null || rs.isEmpty())
		{
			return new ArrayList<T>();
		}
		
		List<T> list = new ArrayList<T>();
		for (Model r : rs) 
		{
			if (r.get(column) != null && !list.contains(r.get(column))) 
			{
				list.add((T)(r.get(column)));
			}
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K,V> Map<K, V> getToMap(List<? extends Model> prices, String keyColumn, String valColumn) 
	{
		if(prices == null || prices.isEmpty())
		{
			return new HashMap<K, V>();
		}
		Map<K, V> map = new HashMap<K, V>();
		for (Model m : prices) 
		{
			K key = (K)m.get(keyColumn);
			V val = (V)m.get(valColumn);
			map.put(key, val);
		}
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K,V> Map<K, V> getToMap(List<? extends Model> prices, String keyColumn, String valColumn, Class<K> clazzK, Class<V> classV) 
	{
		if(prices == null || prices.isEmpty())
		{
			return new HashMap<K, V>();
		}
		Map<K, V> map = new HashMap<K, V>();
		for (Model m : prices) 
		{
			K key = (K)m.get(keyColumn);
			V val = (V)m.get(valColumn);
			map.put(key, val);
		}
		return map;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static <K, M extends Model<M>> Map<K, List<M>> listToMapList(List<M> ms, String keyColumn, Class<K> clazz) 
	{
		Map<K, List<M>> mapList = new HashMap<K, List<M>>();
		for (M m : ms) 
		{
			K key = (K)m.get(keyColumn);
			if (!mapList.containsKey(key)) 
			{
				List<M> list = new ArrayList<M>();
				mapList.put(key, list);
			}
			List<M> trainList = mapList.get(key);
			trainList.add(m);
		}
		return mapList;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public static <T extends Model> List<T> putFriendlyTime(List<T> list, String column, String newColunm) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<T>();
		}
		for (T record : list) 
		{
			Date date = record.getDate(column);
			if(date != null){
				record.put(newColunm, DateUtil.friendlyTime(date));
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static <T extends Model> void putFriendlyTime(T model, String column, String newColumn) 
	{
		Date date = model.getDate(column);
		if(date != null)
		{
			model.put(newColumn, DateUtil.friendlyTime(date));
		}
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Model> List<T> putSocialTime(List<T> list, String column, String newColumn) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<T>();
		}
		for (T t : list) 
		{
			Date date = t.getDate(column);
			if(date != null){
				t.put(newColumn, DateUtil.socialTime(date));
			}
		}
		return list;
	}
	
	public static <T extends Model<T>> T putSocialTime(T t, String column, String newColumn) 
	{
		if(t == null)
		{
			return t;
		}
		
		Date date = t.getDate(column);
		if(date != null)
		{
			t.put(newColumn, DateUtil.socialTime(date));
		}
		
		return t;
	}
	
	public static <T extends Model<T>> void putDateFormat(List<T> ts, String column, String newColumn, String pattern) 
	{
		if (ts == null || ts.isEmpty()) 
		{
			return;
		}
		for (T t : ts) 
		{
			putDateFormat(t, column, newColumn, pattern);
		}
	}
	
	public static <T extends Model<T>> T putDateFormat(T t, String column, String newColumn, String pattern) 
	{
		if(t == null)
		{
			return t;
		}
		
		Date date = t.getDate(column);
		if(date != null)
		{
			t.put(newColumn, DateUtil.dateFormat(pattern, date));
		}
		
		return t;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Model, K> Map<K, T> listToMap(List<T> list, String columnKey, Class<K> keyType)
	{
		if(list == null || list.isEmpty())
		{
			return new HashMap<K, T>();
		}
		Map<K, T> map = new HashMap<K, T>();
		for (T t : list) 
		{
			Object val = t.get(columnKey);
			if(val != null)
			{
				map.put((K)val, t);
			}
		}
		return map;
	}
 	
	@SuppressWarnings("rawtypes")
	public static <T extends Model, K extends Model> List<T> mergeColumnsTo(List<T> toModels, List<K> fromModels, String eqToColumn, String eqFromColumn, String mergeColumns) 
	{
		Map<Object, K> map = listToMap(fromModels, eqFromColumn, Object.class);
		String[] mergeColumnArr = mergeColumns.split(",");
		for (T a : toModels) 
		{
			K k = map.get(a.get(eqToColumn));
			if(k != null)
			{
				for (String column : mergeColumnArr) 
				{
					String trimColumn = column.trim();
					a.put(trimColumn, k.get(trimColumn));
				}
			}
		}
		return toModels;
	}
	
	
	public static <M extends Model<M>, K extends Model<K>> void mergeColumnsTo(M to, K from, String mergeColumns)
	{
		if (to == null || from == null) 
		{
			return;
		}
		String[] mergeColumnArr = mergeColumns.split(",");
		for (String mergeColumn : mergeColumnArr) 
		{
			String column = mergeColumn.trim();
			to.put(column, from.get(column));
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Model> void mergeModelsTo(List<T> toModels, List<T> sourceModels, String eqToColumn, String eqFromColumn, String newColunmName) 
	{
		if(toModels == null || sourceModels == null)
		{
			return;
		}
		for (T parent : toModels) 
		{
			for (T child : sourceModels) 
			{
				Object pVal = parent.get(eqToColumn);
				Object cVal = child.get(eqFromColumn);
				if(pVal != null && cVal != null && pVal.equals(cVal))
				{
					if(parent.get(newColunmName) == null)
					{
						List<T> children = new ArrayList<T>();
						parent.put(newColunmName, children);
					}
					List<T> childrens = (List<T>)parent.get(newColunmName);
					childrens.add(child);
				}
			}
		}
	}
	
	public static <M extends Model<M>, T extends Model<T>> void mergeModelsToMs(List<M> toRecords, List<T> sourceModels, String eqToColumn, String eqSourceColumn, String newColumnName) 
	{
		if (toRecords == null || toRecords.isEmpty() || sourceModels == null || sourceModels.isEmpty()) 
		{
			return;
		}
		
		for (M record : toRecords) 
		{
			Object recordVal = record.get(eqToColumn);
			for (T model : sourceModels) 
			{
				Object modelVal = model.get(eqSourceColumn);
				if (!recordVal.equals(modelVal)) 
				{
					continue;
				}
				
				if (record.get(newColumnName) == null) 
				{
					List<M> modelsOfRecord = new ArrayList<M>();
					record.put(newColumnName, modelsOfRecord);
				}
				List<T> modelsOfRecord = record.get(newColumnName);
				modelsOfRecord.add(model);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Model<T>, K> Set<K> getToSet(List<T> list, String column, Class K) 
	{
		if (list == null || list.isEmpty()) 
		{
			return new HashSet<K>();
		}
		Set<K> set = new HashSet<K>();
		for (T t : list) 
		{
			set.add((K)t.get(column));
		}
		return set;
	}
	
	
	/**
	 * @param columns "id, name,photo"
	 */
	public static <M extends BaseModel<?>> void mergeTo(M target, M source, String columns) 
	{
		if (target == null || source == null) 
		{
			return;
		}
		
		for (String columnStr : columns.split(",")) 
		{
			String column = columnStr.trim();
			target.put(column, source.get(column));
		}
	}
	
	
	public static <M extends Model<M>> void addColumn(List<M> list, String statusColumn, int value) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (M m : list) 
		{
			m.put(statusColumn, value);
		}
	}
	
	public static <M extends Model<M>> void addColumns(List<M> list, String columns, Object... paras) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		String[] columnArr = columns.split(",");
		for (M r : list) 
		{
			for (int i = 0; i < columnArr.length; i++) 
			{
				r.set(columnArr[i].trim(), paras[i]);
			}
		}
	}
	
	public static <M extends Model<M>> boolean isBlank(M model, String columns) 
	{
		if (model == null) 
		{
			return true;
		}
		
		String[] columnArr = columns.split(",");
		for (String column : columnArr) 
		{
			String realColumn = column.trim();
			if (model.get(realColumn) == null || model.getStr(realColumn).isEmpty()) 
			{
				return true;
			}
		}
		return false;
	}

	public static <M extends Model<M>> void setAttrStrVal(List<M> models, Map<Integer, String> levelMap, String intValColumn, String strValColumn) 
	{
		if (models == null || models.isEmpty()) 
		{
			return;
		}
		
		for (M m : models) 
		{
			setAttrStrVal(m, levelMap, intValColumn, strValColumn);
		}
	}

	public static <M extends Model<M>> void setAttrStrVal(M model, Map<Integer, String> levelMap, String intValColumn, String strValColumn) 
	{
		if (model == null || levelMap == null) 
		{
			return;
		}
		Integer intVal = model.getInt(intValColumn);
		String strVal = levelMap.get(intVal);
		if (strVal == null) 
		{
			strVal = "未定义该类型";
		}
		model.put(strValColumn, strVal);
	}
	
	@SuppressWarnings("unchecked")
	public static <M extends Model<M>> void setAttrStrVals(List<M> records, String intColumns, Object... maps) 
	{
		if (records == null || records.isEmpty()) 
		{
			return;
		}
		
		String[] columnArr = intColumns.replace(" ", "").split(",");
		
		for (M record : records) 
		{
			for (int i = 0; i < columnArr.length; i++) 
			{
				String column = columnArr[i];
				Map<Integer, String> intToStrMap = (Map<Integer, String>)maps[i];
				
				Integer intVal = record.getInt(column);
				String strVal = intToStrMap.get(intVal);
				if (strVal == null) 
				{
					strVal = "未定义该类型";
				}
				record.put(column + "Str", strVal);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <M extends Model<M>> void setAttrStrVals(M record, String intColumns, Object... maps) 
	{
		if (record == null) 
		{
			return;
		}
		
		String[] columnArr = intColumns.replace(" ", "").split(",");
		
		for (int i = 0; i < columnArr.length; i++) 
		{
			String column = columnArr[i];
			Map<Integer, String> intToStrMap = (Map<Integer, String>)maps[i];
			
			Integer intVal = record.getInt(column);
			String strVal = intToStrMap.get(intVal);
			if (strVal == null) 
			{
				strVal = "未定义该类型";
			}
			record.put(column + "Str", strVal);
		}
	}
	
	public static <M extends Model<M>, K extends Model<K>> boolean equals(M modelA, K modelB, String modelAColumn, String modelBColumn) 
	{
		if (modelA == null || modelB == null) 
		{
			return false;
		}
		
		Object valA = modelA.get(modelAColumn);
		Object valB = modelB.get(modelBColumn);
		
		return valA != null && valB != null && valA.equals(valB);
	}
	
	public static <M extends Model<M>> M getFirstBy(List<M> ms, String filterColumn, Object filterValue) 
	{
		if (ms == null || ms.isEmpty()) 
		{
			return null;
		}
		for (M m : ms) 
		{
			Object value = m.get(filterColumn);
			if (value != null && value.equals(filterValue)) 
			{
				return m;
			}
		}
		return null;
	}
	
	public static <T extends Model<T>> List<T> filterBy(List<T> list, String column, Object value) 
	{
		if (list == null || list.isEmpty()) 
		{
			return new ArrayList<T>();
		}
		
		List<T> records = new ArrayList<T>();
		for (T r : list) 
		{
			if (r.get(column) != null && r.get(column).equals(value)) 
			{
				records.add(r);
			}
		}
		return records;
	}
	
	
	public static <M extends Model<M>> List<M> treeToList(List<M> catTree, String childListColumn, String newColumnOfLevel)
	{
		if (catTree == null || catTree.isEmpty()) 
		{
			return new ArrayList<M>();
		}
		
		List<M> list = new ArrayList<M>();
		
		for (M cat : catTree) 
		{
			cat.put(newColumnOfLevel, 1);
			list.add(cat);
			
			List<M> secondCats = cat.get(childListColumn);
			if (secondCats == null || secondCats.isEmpty()) 
			{
				continue;
			}
			
			for (M secondCat : secondCats) 
			{
				secondCat.put(newColumnOfLevel, 2);
				list.add(secondCat);
				
				List<M> thirdCats = cat.get(childListColumn);
				if (thirdCats == null || thirdCats.isEmpty()) 
				{
					continue;
				}
				
				for (M thirdCat : thirdCats) 
				{
					thirdCat.put(newColumnOfLevel, 3);
					list.add(thirdCat);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * @param catTree [{.., childList}]
	 * <br> childList [{.., money}]
	 * @return [{.., money, childList}]
	 */
	public static <M extends Model<M>> void sumChildMoneyToParent(List<M> catTree, String childListColumn, String moneyColumn) 
	{
		if (catTree == null || catTree.isEmpty()) 
		{
			return ;
		}
		
		for (M topCat : catTree) 
		{
			List<M> secondCats = topCat.get(childListColumn);
			
			BigDecimal topMoney = new BigDecimal(0);
			for (M secondCat : secondCats) 
			{
				List<M> thirdCats = secondCat.get(childListColumn);
				
				BigDecimal secondMoney = new BigDecimal(0);
				for (M thirdCat : thirdCats) 
				{
					secondMoney.add(thirdCat.getBigDecimal(moneyColumn));
				}
				
				secondCat.put(moneyColumn, secondMoney);
				topMoney = topMoney.add(secondMoney);
			}
			
			topCat.put(moneyColumn, topMoney);
		}
	}
	
	public static <M extends Model<M>> void removeWithNullColumn(List<M> list, String nullColumn)
	{
		if (list == null || list.isEmpty()) 
		{
			return; 
		}
		
		List<M> removeList = new ArrayList<M>();
		for (M m : list) 
		{
			if (m.get(nullColumn) == null) 
			{
				removeList.add(m);
			}
		}
		
		if (!removeList.isEmpty()) 
		{
			list.removeAll(removeList);
		}
	}
	
	public static <M extends Model<M>> void modifyColumnName(List<M> list, String oldColumn, String newColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		
		for (M record : list) 
		{
			Object val = record.get(oldColumn);
			record.put(newColumn, val);
			record.remove(oldColumn);
		}
	}
	
	public static <M extends Model<M>> void sumIntAttr(M m, String attrA, String attrB, String newSumAttrName) 
	{
		if (m == null) 
		{
			return;
		}
		
		Integer valueA = m.getInt(attrA);
		Integer valueB = m.getInt(attrB);
		
		valueA = valueA == null ? 0 : valueA;
		valueB = valueB == null ? 0 : valueB;
		
		Integer sumValue = valueA + valueB;
		
		m.put(newSumAttrName, sumValue);
	}
	
	public static <M extends Model<M>> void sumIntAttrToColumn(M m, String attrA, String attrB, String newSumAttrName) 
	{
		if (m == null) 
		{
			return;
		}
		sumIntAttr(m, attrA, attrB, newSumAttrName);
		Integer sumValue = m.getInt(newSumAttrName);
		m.remove(newSumAttrName);
		m.set(newSumAttrName, sumValue);
	}
	

	public static String buildSqlIn(String sql,String reg, List<Integer> idList) {
		String ids = join(idList, ",");
		return StringUtil.replace(sql,reg,ids);
	}
	
	public static String buildSqlInStr(String sql,String reg, List<String> idList) {
		String ids = join(idList, ",");
		return StringUtil.replace(sql,reg,ids);
	}
	
	public static String buildSqlIn(String sql,String reg, Integer[] id) {
		String ids = join(Arrays.asList(id), ",");
		return StringUtil.replace(sql,reg,ids);
	}
	
	public static String join(List<?> list,String delimiter){
		StringBuilder sb = new StringBuilder();
		if(list == null || list.isEmpty()){
			return sb.toString();
		}
		Iterator<?> iter = list.iterator();
		while(iter.hasNext()){
			sb.append(StringUtil.toSafeString(iter.next()))
			  .append(StringUtil.toSafeString(delimiter));
		}
		return sb.substring(0, sb.length() - StringUtil.toSafeString(delimiter).length());
	}
	
}
