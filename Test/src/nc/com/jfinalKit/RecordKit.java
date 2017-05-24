package nc.com.jfinalKit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.com.utils.DateUtil;
import nc.com.utils.MapUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class RecordKit {

	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getToList(List<Record> rs, String column, Class<T> clazz) 
	{
		if(rs == null || rs.isEmpty())
		{
			return new ArrayList<T>();
		}
		
		List<T> list = new ArrayList<T>();
		for (Record r : rs) 
		{
			if (r.get(column) != null) 
			{
				list.add((T)(r.get(column)));
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	public static <K, V> Map<K, V> listToMap(List<Record> list, String columnKey, String columnVal, Class<K> keyType, Class<V> valueType)
	{
		if(list == null || list.isEmpty())
		{
			return new HashMap<K, V>();
		}
		Map<K, V> map = new HashMap<K, V>();
		for (Record t : list) 
		{
			Object key = t.get(columnKey);
			if(key != null)
			{
				map.put((K)key, (V)t.get(columnVal));
			}
		}
		return map;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static <K extends Model> List<Record> mergeColumnsTo(List<Record> toRecords, List<K> fromRecords, String eqToColumn, String eqFromColumn, String mergeColumns) 
	{
		if (toRecords == null || toRecords.isEmpty() || fromRecords == null || fromRecords.isEmpty()) 
		{
			return toRecords;
		}
		
		Map<Object, K> map = ModelKit.listToMap(fromRecords, eqFromColumn, Object.class);
		
		String[] mergeColumnArr = mergeColumns.split(",");
		
		for (Record a : toRecords) 
		{
			K k = map.get(a.get(eqToColumn));
			if(k != null)
			{
				for (String column : mergeColumnArr) 
				{
					String trimColumn = column.trim();
					a.set(trimColumn, k.get(trimColumn));
				}
			}
		}
		return toRecords;
	}
	
	
	public static <M extends Model<M>> List<Record> mergeColumnsTo(List<Record> toRecords, List<M> fromRecords, String eqToColumn, String eqFromColumn, String mergeColumns, String newColumnNames) 
	{
		if (toRecords == null || toRecords.isEmpty() || fromRecords == null || fromRecords.isEmpty()) 
		{
			return toRecords;
		}
		
		Map<Object, M> map = ModelKit.listToMap(fromRecords, eqFromColumn, Object.class);
		
		String[] mergeColumnArr = mergeColumns.split(",");
		String[] newColumnArr = newColumnNames.split(",");
		
		for (Record a : toRecords) 
		{
			M k = map.get(a.get(eqToColumn));
			if(k != null)
			{
				for (int i = 0; i < mergeColumnArr.length; i++) 
				{
					String column = mergeColumnArr[i].trim();
					String newColumn = newColumnArr[i].trim();
					
					a.set(newColumn, k.get(column));
				}
			}
		}
		return toRecords;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static <M extends Model> void mergeModelsTo(List<Record> toRecords, List<M> sourceModels, String eqToColumn, String eqSourceColumn, String newColumnName) 
	{
		if (toRecords == null || toRecords.isEmpty() || sourceModels == null || sourceModels.isEmpty()) 
		{
			return;
		}
		
		for (Record record : toRecords) 
		{
			Object recordVal = record.get(eqToColumn);
			for (M model : sourceModels) 
			{
				Object modelVal = model.get(eqSourceColumn);
				if (!recordVal.equals(modelVal)) 
				{
					continue;
				}
				
				if (record.get(newColumnName) == null) 
				{
					List<M> modelsOfRecord = new ArrayList<M>();
					record.set(newColumnName, modelsOfRecord);
				}
				List<M> modelsOfRecord = record.get(newColumnName);
				modelsOfRecord.add(model);
			}
		}
	}
	
	
	public static void addColumn(List<Record> list, String statusColumn, Object value) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (Record record : list) 
		{
			record.set(statusColumn, value);
		}
	}
	
	
	public static void addColumns(List<Record> list, String columns, Object... paras) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		String[] columnArr = columns.split(",");
		for (Record r : list) 
		{
			for (int i = 0; i < columnArr.length; i++) 
			{
				r.set(columnArr[i].trim(), paras[i]);
			}
		}
	}
	
	
	public static void removeColumn(List<Record> list, String... columns) 
	{
		if (columns == null || columns.length <= 0) 
		{
			return;
		}
		
		for (Record r : list) 
		{
			for (String column : columns) 
			{
				r.remove(column);
			}
		}
	}
	
	
	public static void modifyColumnName(List<Record> list, String oldColumn, String newColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		
		for (Record record : list) 
		{
			Object val = record.get(oldColumn);
			record.set(newColumn, val);
			record.remove(oldColumn);
		}
	}
	
	
	public static List<Record> putFriendlyTime(List<Record> list, String column, String newColunm) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<Record>();
		}
		for (Record record : list) 
		{
			Date date = record.getDate(column);
			if(date != null){
				record.set(newColunm, DateUtil.friendlyTime(date));
			}
		}
		return list;
	}
	
	public static List<Record> putSocialTime(List<Record> list, String column, String newColumn) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<Record>();
		}
		for (Record record : list) 
		{
			Date date = record.getDate(column);
			if(date != null){
				record.set(newColumn, DateUtil.socialTime(date));
			}
		}
		return list;
	}
	
	public static List<Record> putHHmm(List<Record> list, String column, String newColumn) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<Record>();
		}
		for (Record record : list) 
		{
			int millSeconds = record.getInt(column);
			record.set(newColumn, DateUtil.toHHmm(millSeconds));
		}
		return list;
	}
	
	public static List<Record> putDateStr(List<Record> list, String column, String newColumn, String datePattern) 
	{
		if(list == null || list.isEmpty())
		{
			return new ArrayList<Record>();
		}
		for (Record record : list) 
		{
			Date date = record.getDate(column);
			if (date != null) 
			{
				record.set(newColumn, DateUtil.dateFormat(datePattern, date));
			}else 
			{
				record.set(newColumn, "");
			}
		}
		return list;
	}
	
	public static List<Record> filterBy(List<Record> list, String column, Object value) 
	{
		if (list == null || list.isEmpty()) 
		{
			return new ArrayList<Record>();
		}
		
		List<Record> records = new ArrayList<Record>();
		for (Record r : list) 
		{
			if (r.get(column) != null && r.get(column).equals(value)) 
			{
				records.add(r);
			}
		}
		return records;
	}
	
	
	public static <M extends Model<M>> void mergeStatusColumnsTo(List<Record> toList, List<M> fromList, String eqToColumn, String eqFromColumn, String statusColumn, int statusYes, int statusNo) 
	{
		if (toList == null || toList.isEmpty()) 
		{
			return;
		}
		
		addColumn(toList, statusColumn, statusNo);
		
		if (fromList == null || fromList.isEmpty()) 
		{
			return;
		}
		
		for (M m : fromList) 
		{
			Object fromEqVal = m.get(eqFromColumn);
			for (Record r : toList)
			{
				Object toEqVal = r.get(eqToColumn);
				if (fromEqVal.equals(toEqVal)) 
				{
					r.set(statusColumn, statusYes);
					break;
				}
			}
		}
	}
	
	public static void setAttrStrVal(List<Record> records, Map<Integer, String> intToStrMap, String intValColumn, String strValColumn) 
	{
		if (records == null || records.isEmpty()) 
		{
			return;
		}
		for (Record record : records) 
		{
			setAttrStrVal(record, intToStrMap, intValColumn, strValColumn);
		}
	}
	
	public static void setAttrStrVal(Record record, Map<Integer, String> intToStrMap, String intValColumn, String strValColumn) 
	{
		if (record == null || intToStrMap == null) 
		{
			return;
		}
		Integer intVal = record.getInt(intValColumn);
		String strVal = intToStrMap.get(intVal);
		if (strVal == null) 
		{
			strVal = "未定义该类型";
		}
		record.set(strValColumn, strVal);
	}
	
	@SuppressWarnings("unchecked")
	public static void setAttrStrVals(List<Record> records, String intColumns, Object... maps) 
	{
		if (records == null || records.isEmpty()) 
		{
			return;
		}
		
		String[] columnArr = intColumns.replace(" ", "").split(",");
		
		for (Record record : records) 
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
				record.set(column + "Str", strVal);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void setAttrStrVals(Record record, String intColumns, Object... maps) 
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
			record.set(column + "Str", strVal);
		}
	}
	
	public static void prependColumnTo(List<Record> list, String fromColumn, String toColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (Record r : list) 
		{
			String value = r.get(fromColumn).toString() + r.get(toColumn).toString();
			r.set(toColumn, value);
		}
	}
	
	public static Record getFirstBy(List<Record> list, String filterColumn, Object value) 
	{
		if (list == null || list.isEmpty()) 
		{
			return null;
		}
		for (Record r : list) 
		{
			Object filterValue = r.get(filterColumn);
			if (filterValue != null && filterValue.equals(value)) 
			{
				return r;
			}
		}
		return null;
	}
	
	public static void setColumnVal(List<Record> list, String toColumn, String fromColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (Record m : list) 
		{
			Object value = m.get(fromColumn);
			m.set(toColumn, value);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void removeFrom(List<Record> list, String fromColumn, List removeValues) 
	{
		if (list == null || list.isEmpty() || removeValues == null || removeValues.isEmpty()) 
		{
			return;
		}
		Set<Object> valueSet = new HashSet<Object>();
		for (Object rmVal : removeValues) 
		{
			valueSet.add(rmVal);
		}
		
		List<Record> removeList = new ArrayList<Record>();
		for (Record m : list) 
		{
			Object value = m.get(fromColumn);
			if (valueSet.contains(value)) 
			{
				removeList.add(m);
			}
		}
		list.removeAll(removeList);
	}
	
	/**
	 * @return [{newColumn1, newColumn2}]
	 */
	public static List<Record> getToNewList(List<Record> list, String selectColumns, String newColumns) 
	{
		if (list == null || list.isEmpty()) 
		{
			return new ArrayList<Record>();
		}
		
		String[] columnArr = selectColumns.replaceAll(" ", "").split(",");
		String[] newColumnArr = newColumns.replaceAll(" ", "").split(",");
		
		List<Record> newList = new ArrayList<Record>();
		for (Record record : list) 
		{
			Record newR = new Record();
			for (int i = 0; i < columnArr.length; i++) 
			{
				newR.set(newColumnArr[i], record.get(columnArr[i]));
			}
			newList.add(newR);
		}
		return newList;
	}
	
	public static void sortList(List<Record> list, final String sortColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		Collections.sort(list, new Comparator<Record>() {
			@Override
			public int compare(Record r1, Record r2) {
				int v1 = r1.getInt(sortColumn);
				int v2 = r2.getInt(sortColumn);
				if (v1 == v2) 
				{
					return 0;
				}
				return v1 > v2 ? 1 : -1;
			}
		});
	}
	
	@SuppressWarnings({"unchecked" })
	public static <K> Map<K, Record> listToMap(List<Record> list, String columnKey, Class<K> keyType)
	{
		if(list == null || list.isEmpty())
		{
			return new HashMap<K, Record>();
		}
		Map<K, Record> map = new HashMap<K, Record>();
		for (Record t : list) 
		{
			Object val = t.get(columnKey);
			if(val != null)
			{
				map.put((K)val, t);
			}
		}
		return map;
	}
	
	public static void setValIfNull(List<Record> list, String column, Object defaultVal) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (Record record : list) 
		{
			if (record.get(column) == null) 
			{
				record.set(column, defaultVal);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <M extends Model<M>> void setAttrListToMap(List<Record> list, String listColumnName, String newMapColumnName, String listToMapKeyColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		for (Record record : list) 
		{
			List<M> details = record.get(listColumnName);
			Map<Integer, M> detailMap = ModelKit.listToMap(details, listToMapKeyColumn, Integer.class);
			Map<String, M> targetDetailMap = MapUtil.toStrKey(detailMap);
			record.set(newMapColumnName, targetDetailMap)
				  .remove(listColumnName);
		}
	}
	
	/**
	 * result = divisorA / divisorB
	 * @return result 
	 * <br>默认 0 BigDecimal
	 */
	public static void diviteIntAttrs(Record record, String divisorColumnA, String divisorColumnB, String resultColumn, int scale) 
	{
		if (record == null) 
		{
			return;
		}
		
		if (record.get(divisorColumnA) == null || record.get(divisorColumnB) == null || record.getInt(divisorColumnB) <= 0) 
		{
			record.set(resultColumn, new BigDecimal(0));
			return;
		}
		
		BigDecimal valueA = new BigDecimal(record.getInt(divisorColumnA));
		BigDecimal valueB = new BigDecimal(record.getInt(divisorColumnB));
		BigDecimal resultVal = valueA.divide(valueB, scale, BigDecimal.ROUND_HALF_UP);
		record.set(resultColumn, resultVal);
	}
	
	/**
	 * @return {sumColumnA, sumColumnB, ...}
	 */
	public static <M extends Model<M>> Record sumIntAttr(List<M> details, String sumColumns) 
	{
		String columns = sumColumns.replace(" ", "");
		String[] columnArr = columns.split(",");
		
		Record record = new Record();
		for (int i = 0; i < columnArr.length; i++) 
		{
			String column = columnArr[i];
			record.set(column, 0);
		}
			  
		if (details == null || details.isEmpty()) 
		{
			return record;
		}
		
		for (M detail : details) 
		{
			for (int i = 0; i < columnArr.length; i++) 
			{
				String column = columnArr[i];
				Integer value = detail.getInt(column);
				value = value == null ? 0 : value;
				Integer oldValue = record.getInt(column);
				record.set(column, oldValue + value);
			}
		}
		return record;
	}
	
	/**
	 * @return [{.., resultColumn}]
	 */
	public static void sumIntAttr(List<Record> list, String sumColumns, String resultColumn) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		String columns = sumColumns.replace(" ", "");
		String[] columnArr = columns.split(",");
		for (Record record : list) 
		{
			int sumValue = 0;
			for (int i = 0; i < columnArr.length; i++) 
			{
				String column = columnArr[i];
				Integer value = record.getInt(column);
				if (value != null) 
				{
					sumValue += value;
				}
			}
			record.set(resultColumn, sumValue);
		}
	}

	/**
	 * @return list [{.., calColumnASuffix, calColumnBSuffix, ...}]
	 */
	public static <M extends Model<M>> void sumSubListIntAttrTo(List<Record> list, String subListColumnName, String calColumns, String resultColumnSuffix) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		
		String columns = calColumns.replace(" ", "");
		String[] columnArr = columns.split(",");
		for (Record record : list) 
		{
			List<M> subList = record.get(subListColumnName);
			
			if (subList == null || subList.isEmpty()) 
			{
				for (int i = 0; i < columnArr.length; i++) 
				{
					String column = columnArr[i];
					record.set(column + resultColumnSuffix, 0);
				}
				continue;
			}
			
			for (int i = 0; i < columnArr.length; i++) 
			{
				String column = columnArr[i];
				int sumValue = 0;
				for (M model : subList) 
				{
					Integer value = model.getInt(column);
					if (value != null) 
					{
						sumValue += value;
					}
				}
				record.set(column + resultColumnSuffix, sumValue);
			}
		}
	}
	
	/**
	 * @return list [{.., calColumnASuffix, calColumnBSuffix, ...}]
	 */
	public static <M extends Model<M>> void sumSubListBDAttrTo(List<Record> list, String subListColumnName, String calColumns, String resultColumnSuffix) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		
		String columns = calColumns.replace(" ", "");
		String[] columnArr = columns.split(",");
		for (Record record : list) 
		{
			List<M> subList = record.get(subListColumnName);
			
			if (subList == null || subList.isEmpty()) 
			{
				for (int i = 0; i < columnArr.length; i++) 
				{
					String column = columnArr[i];
					record.set(column + resultColumnSuffix, 0);
				}
				continue;
			}
			
			for (int i = 0; i < columnArr.length; i++) 
			{
				String column = columnArr[i];
				BigDecimal sumValue = new BigDecimal(0);
				for (M model : subList) 
				{
					BigDecimal value = model.getBigDecimal(column);
					if (value != null) 
					{
						sumValue = sumValue.add(value);
					}
				}
				record.set(column + resultColumnSuffix, sumValue);
			}
		}
	}
	
	
	public static void mergeRecordsTo(List<Record> toRecords, List<Record> fromRecords, String eqToColumn, String eqSourceColumn, String newColumnName) 
	{
		if (toRecords == null || toRecords.isEmpty() || fromRecords == null || fromRecords.isEmpty()) 
		{
			return;
		}
		
		for (Record record : toRecords) 
		{
			Object recordVal = record.get(eqToColumn);
			for (Record model : fromRecords) 
			{
				Object modelVal = model.get(eqSourceColumn);
				if (!recordVal.equals(modelVal)) 
				{
					continue;
				}
				
				if (record.get(newColumnName) == null) 
				{
					List<Record> modelsOfRecord = new ArrayList<Record>();
					record.set(newColumnName, modelsOfRecord);
				}
				List<Record> modelsOfRecord = record.get(newColumnName);
				modelsOfRecord.add(model);
			}
		}
	}
	
	public static <M extends Model<M>>  List<M> getListAttrTo(List<Record> list, String listColumn, Class<M> clazz) 
	{
		if (list == null || list.isEmpty()) 
		{
			return new ArrayList<M>();
		}
		
		List<M> allList = new ArrayList<M>();
		for (Record record : list) 
		{
			List<M> attrList = record.get(listColumn);
			if (attrList != null && !attrList.isEmpty()) 
			{
				allList.addAll(attrList);
			}
		}
		
		return allList;
	}
	
	public static boolean isBlank(Record model, String columns) 
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
	
}
