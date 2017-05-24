package nc.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.com.jfinalKit.ModelKit;
import nc.com.jfinalKit.RecordKit;
import nc.com.utils.TypeConverter;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * 继承Jfinal Model，添加一些通用功能
 */
@SuppressWarnings("rawtypes")
public class BaseModel<M extends BaseModel> extends Model<M>{
	
	private static Logger logger = Logger.getLogger(BaseModel.class);
	
	private static final long serialVersionUID = 6748529767690950203L;
	
	private static final int COLUMN_STATUS_YES = 1;
	private static final int COLUMN_STATUS_NO = 0;

	private Map<String, Class<?>> modelAttrs;
	
	
	/*
	 ***********************
	 ****    属性赋值   *****            
	 ***********************
	 */
	
	/**
	 * 从request中给bean注入值
	 */
	@SuppressWarnings("unchecked")
	public M setAttrsFrom(Controller ctrl, String columns)
	{
		String[] columnArr = columns.split(",");
		
		Map<String, Class<?>> attrs = getModelAttrs();
		
		try {
			for (String columnStr : columnArr) 
			{
				String column = columnStr.trim();
				if(!attrs.containsKey(column))
				{
					continue;
				}
				
				if(ctrl.getPara(column) == null)
				{
					set(column, null);
					continue;
				}
				
				Class<?> type = attrs.get(column);
				if("".equals(ctrl.getPara(column)))
				{
					if(type == String.class)
					{
						set(column, ctrl.getPara(column));
						continue;
					}
					set(column, null);
					continue;
				}
				
				Object val = TypeConverter.convert(type, ctrl.getPara(column));
				set(column, val);
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return (M)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Model<T>> M putAttrs(T m, String columns)
	{
		if (m == null || columns == null || columns.isEmpty()) 
		{
			return (M)this;
		}
		
		String[] colomnArr = columns.split(",");
		for (String column : colomnArr) 
		{
			if (m.get(column) != null) 
			{
				put(column, m.get(column));
			}
		}
		return (M)this;
	}
	
	@Override
	public M put(String key, Object value) 
	{
		if (getTable().hasColumnLabel(key)) 
		{
			return super.set(key, value);
		}
		return super.put(key, value);
	}
	
	/*
	 ***********************
	 ****   属性格式化   ****            
	 ***********************
	 */
	
	/**
	 * 将日期类型转化为长整型
	 * @param attrs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public M formatToMillSecond(String... attrs)
	{
		for (String attr : attrs) 
		{
			Date date = getDate(attr);
			if(date != null)
			{
				set(attr, date.getTime());
			}
		}
		return (M)this;
	}
	
	
	/*
	 ***********************
	 ****    属性操作   *****            
	 ***********************
	 */
	/**
	 * 保留bean实体中attrs属性，其它的删掉
	 */
	@SuppressWarnings("unchecked")
	public M returnAttrs(String... attrs)
	{
		Set<String> set = new HashSet<String>();
		for (String attr : attrs) 
		{
			set.add(attr);
		}
		
		for (String attr : _getAttrNames()) 
		{
			if(!set.contains(attr))
			{
				this.remove(attr);
			}
		}
		return (M)this;
	}
	
	
	/*
	 ***********************
	 ****    批量添加   *****            
	 ***********************
	 */
	@SuppressWarnings("unchecked")
	public void saveList(List<M> beanList) 
	{
		if(beanList != null && beanList.size() > 0) 
		{
			Config config = getConfig();
		    Table table = getTable();
		     
		    try {
		    	String sql = null;
		    	List<Object[]> paramsList = new ArrayList<Object[]>();
		    	for(M bean: beanList) 
		    	{
			        StringBuilder sb = new StringBuilder();
			        List<Object> paras = new ArrayList<Object>();
			        config.getDialect().forModelSave(table, bean.getAttrs(), sb, paras);
			        if(sql == null) 
			        {
			          sql = sb.toString();
			        }
			        paramsList.add(paras.toArray());
		    	}
		    	Object[][] paramss = new Object[paramsList.size()][];
			    int i = 0;
			    for(Object[] oo : paramsList) 
			    {
			        paramss[i++] = oo;
			    }
		       
			    batchExecUpdate(sql, paramss);
		    } catch (Exception e) {
		    	throw new ActiveRecordException(e);
		    }
		}
	}
	
	/**
	 * JDBC 构建批量插入 SQL 
	 */
	private int[] batchExecUpdate(String sql, Object[]... paramss) throws SQLException 
	{
		Config config = getConfig();
		Table table = getTable();
		if (paramss == null) {
		    paramss = new Object[0][0];
		}
	 
		Connection conn = null;
		PreparedStatement pst = null;
		int[] rows = null;
		try {
		    conn = config.getConnection();
		    if (config.getDialect().isOracle()) {
		      pst = conn.prepareStatement(sql.toString(), table.getPrimaryKey());
		    } else {
		      pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		    }
	 
		    for (Object[] params : paramss) {
		      config.getDialect().fillStatement(pst, params);
		      pst.addBatch();
		    }
		    rows = pst.executeBatch();
	 
		} finally {
			config.close(pst, conn);
		}
	  return rows;
	}
	

	/*
	 ***********************
	 ****    删除操作   *****            
	 ***********************
	 */
	public void deleteByIds(List<Integer> ids)
	{
		if(ids == null || ids.isEmpty())
		{
			return;
		}
		String sql = "delete from `" + getTable().getName() + "` where id in (??)";
		sql = buildSqlHasIn(sql, ids);
		Db.update(sql);
	}
	
	public int deleteByColumns(String columns, Object...vals)
	{
		String where = buildWhereBlock(columns);
		String sql = "delete from `" + getTable().getName() + "` where " + where;
		return Db.update(sql, vals);
	}
	

	/*
	 ***********************
	 ****    更新操作   *****            
	 ***********************
	 */
	public int updateColumnBy(String column, String whereColumns, Object...values)
	{
		String where = buildWhereBlock(whereColumns);
		StringBuilder sql = new StringBuilder("update `").append(getTable().getName())
														 .append("` set ")
														 .append(column)
														 .append(" = ? where ")
														 .append(where);
		return Db.update(sql.toString(), values);
	}
	
	public void updateColumnByIds(String column, Object val, List<Integer> ids) 
	{
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		StringBuilder sqlBuilder = new StringBuilder("update `")
										.append(getTable().getName())
										.append("` set ")
										.append(column)
										.append(" = ? where id in (??)");
		String sql = buildSqlHasIn(sqlBuilder.toString(), ids);
		Db.update(sql, val);
	}
	
	public void plusNumById(Object id, String column)
	{
		String sql = "update `" + getTable().getName() + "` set " + column + " = " + column + " + 1 where id = ?";
		Db.update(sql, id);
	}
	
	public void plusNumByColumns(String plusColumn, String whereColumns, Object... paras)
	{
		String sql = String.format("update `%s` set %s = %s + 1 where %s", getTable().getName(), plusColumn, plusColumn, buildWhereBlock(whereColumns));
		Db.update(sql, paras);
	}
	
	public void minusNumById(Object id, String column)
	{
		String sql = "update `" + getTable().getName() + "` set " + column + " = " + column + " - 1 where id = ?";
		Db.update(sql, id);
	}
	
	public void minusNumByColumns(String minusColumn, String whereColumns, Object... paras)
	{
		String sql = String.format("update `%s` set %s = %s - 1 where %s", getTable().getName(), minusColumn, minusColumn, buildWhereBlock(whereColumns));
		Db.update(sql, paras);
	}
	
	public int setColumnStatus(int id, String ownerColumn, Object ownerValue, String statusColumn, Object onValue, Object offValue) 
	{
		M m = findByIdLoadColumns(id, "id, " + ownerColumn + ", " + statusColumn);
		if (m == null || !m.get(ownerColumn).equals(ownerValue)) 
		{
			return 0;
		}
		
		if (m.get(statusColumn).equals(onValue)) 
		{
			m.set(statusColumn, offValue);
			m.update();
			return -1;
		}
		
		m.set(statusColumn, onValue);
		m.update();
		return 1;
	}
	
	public int setColumnStatus(int id, String statusColumn, Object onValue, Object offValue) 
	{
		return setColumnStatus(id, "id", id, statusColumn, onValue, offValue);
	}
	
	
	/*
	 ***********************
	 ****    查询操作   *****            
	 ***********************
	 */
	public boolean exist(String columns, Object...values) 
	{
		return countByColumns(columns, values) > 0;
	}
	
	public long count()
	{
		String sql = "select count(*) as count from `" + getTable().getName() + "`";
		return findFirst(sql).getLong("count");
	}
	
	public long countByColumns(String columns, Object...values)
	{
		String where = buildWhereBlock(columns);
		String sql = "select count(*) as num from `" + getTable().getName() + "` where " + where;
		M m = findFirst(sql, values);
		return m.getLong("num");
	}
	
	public Integer getMax(String column)
	{
		String sql = "select max(" + column + ") as maxNum from `" + getTable().getName() + "`";
		M m = findFirst(sql);
		if (m == null) 
		{
			return 0;
		}
		return m.get("maxNum") == null ? 0 : m.getInt("maxNum"); 
	}
	
	public Integer getMaxBy(String maxColumn, String whereColumns, Object... paras)
	{
		String where = buildWhereBlock(whereColumns);
		String sql = "select max(" + maxColumn + ") as maxNum from `" + getTable().getName() + "` where " + where;
		M m = findFirst(sql, paras);
		if (m == null) 
		{
			return 0;
		}
		return m.get("maxNum") == null ? 0 : m.getInt("maxNum"); 
	}
	
	
	public List<M> findByIds(List<Integer> ids, String columns)
	{
		if (ids == null || ids.isEmpty()) 
		{
			return new ArrayList<M>();
		}
		
		String sql = "select " + columns + " from `" + getTable().getName() + "` where id in (??)";
		sql = buildSqlHasIn(sql, ids);
		return find(sql);
	}
	
	public M findFirstByColumns(String selectColumns, String whereColumns, Object...vals)
	{
		String where = buildWhereBlock(whereColumns);
		String sql = "select " + selectColumns +" from `" + getTable().getName() + "` where " + where;
		return findFirst(sql, vals);
	}
	
	public List<M> findByColumns(String selectColumns, String whereColumns, Object...vals)
	{
		String where = buildWhereBlock(whereColumns);
		String sql = "select " + selectColumns +" from `" + getTable().getName() + "` where " + where;
		return find(sql, vals);
	}
	
	public List<M> findIn(String sql , List<Integer> vals, Object...params)
	{
		if(vals == null || vals.isEmpty())
		{
			return new ArrayList<M>();
		}
		sql = buildSqlHasIn(sql, vals);
		return this.find(sql, params);
	}
	
	
	
	
	
	/*
	 ***********************
	 **  查询 & 合并操作  ***            
	 ***********************
	 */
	
	public <T extends Model> void mergeToById(T target, String eqToColumn, String selectColumnNames)
	{
		mergeToById(target, eqToColumn, selectColumnNames, selectColumnNames);
	}
	
	
	public <T extends Model> void mergeToById(T target, String eqToColumn, String selectColumnNames, String mergeColumnNames)
	{
		if (target == null) 
		{
			return;
		}
		
		M data = findByIdLoadColumns(target.get(eqToColumn), selectColumnNames);
		mergeTo(target, data, selectColumnNames, mergeColumnNames);
	}
	
	public <T extends Model> void mergeToByColumns(T target, String eqToColumn, String selectColumnNames, String whereColumns, Object... paras)
	{
		mergeToByColumns(target, eqToColumn, selectColumnNames, selectColumnNames, whereColumns, paras);
	}
	
	public <T extends Model> void mergeToByColumns(T target, String eqToColumn, String selectColumnNames, String mergeColumnNames, String whereColumns, Object... paras)
	{
		if (target == null) 
		{
			return;
		}
		
		String where = buildWhereBlock(whereColumns);
		String sql = "select " + selectColumnNames + " from `" + getTable().getName() + "` where " + where;
		M data = findFirst(sql, paras);
		
		mergeTo(target, data, selectColumnNames, mergeColumnNames);
	}
	
	private  <T extends Model> void mergeTo(T target, M data, String selectColumnNames, String mergeColumnNames)
	{
		if (data == null) 
		{
			return;
		}
		String[] selectColumns = selectColumnNames.split(",");
		String[] mergeColumns = mergeColumnNames.split(",");
		for (int i = 0; i < selectColumns.length; i++) 
		{
			target.put(mergeColumns[i].trim(), data.get(selectColumns[i].trim()));
		}
	}
	
	
	
	public void mergeColumnsToRs(List<Record> toRecords, String eqToColumn, String mergeColumns)
	{
		mergeColumnsToRs(toRecords, eqToColumn, mergeColumns, mergeColumns);
	}
	
	public void mergeColumnsToRs(List<Record> toRecords, String eqToColumn, String selectColumns, String mergeColumns)
	{
		if(toRecords.isEmpty())
		{
			return;
		}
		
		List<Integer> ids = RecordKit.getToList(toRecords, eqToColumn, Integer.class);
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		List<M> models = findByIds(ids, "id, " + selectColumns);

		if (models.isEmpty()) 
		{
			return;
		}
		
		Map<Object, M> map = ModelKit.listToMap(models, "id", Object.class);
		String[] columnArr = mergeColumns.split(",");
		for (Record a : toRecords) 
		{
			M k = map.get(a.get(eqToColumn));
			if(k != null)
			{
				for (String column : columnArr) 
				{
					if (!column.trim().equals("id")) 
					{
						a.set(column.trim(), k.get(column.trim()));
					}
				}
			}
		}
	}
	
	
	public void mergeColumnsToRs(List<Record> toRecords, String eqToColumn, String eqFromColumn, String selectColumns, String mergeColumns, String whereColumns, Object... paras)
	{
		if(toRecords.isEmpty())
		{
			return;
		}
		
		List<Integer> ids = RecordKit.getToList(toRecords, eqToColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(selectColumns)
				  .append(" from `")
				  .append(getTable().getName())
				  .append("` where ");
		if (whereColumns != null) 
		{
			String where = buildWhereBlock(whereColumns);
			sqlBuilder.append(where)
					  .append(" and ");
		}
		sqlBuilder.append(eqFromColumn)
				  .append(" in (??) ");
		String sql = buildSqlHasIn(sqlBuilder.toString(), ids);
		
		List<M> models = find(sql, paras);
		
		if (models.isEmpty()) 
		{
			return;
		}
		
		Map<Object, M> fromModelMap = ModelKit.listToMap(models, eqFromColumn, Object.class);
		String[] mergeColumnArr = mergeColumns.split(",");
		for (Record a : toRecords) 
		{
			M fromModel = fromModelMap.get(a.get(eqToColumn));
			if(fromModel != null)
			{
				for (String mergeColumn : mergeColumnArr) 
				{
					if (!mergeColumn.trim().equals(eqFromColumn)) 
					{
						a.set(mergeColumn.trim(), fromModel.get(mergeColumn.trim()));
					}
				}
			}
		}
	}
	
	
	public <K extends BaseModel> void mergeColumnsToMs(List<K> toRecords, String eqToColumn, String mergeColumns)
	{
		mergeColumnsToMs(toRecords, eqToColumn, mergeColumns, mergeColumns);
	}
	
	public <K extends BaseModel> void mergeColumnsToMs(List<K> toRecords, String eqToColumn, String selectColumns, String mergeColumns)
	{
		if(toRecords.isEmpty())
		{
			return;
		}
		
		List<Integer> ids = ModelKit.getToList(toRecords, eqToColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		List<M> models = findByIds(ids, "id, " + selectColumns);

		if (models.isEmpty()) 
		{
			return;
		}
		
		Map<Object, M> map = ModelKit.listToMap(models, "id", Object.class);
		String[] columnArr = mergeColumns.split(",");
		for (K k : toRecords) 
		{
			M m = map.get(k.get(eqToColumn));
			if(m != null)
			{
				for (String column : columnArr) 
				{
					if (!column.trim().equals("id")) 
					{
						k.put(column.trim(), m.get(column.trim()));
					}
				}
			}
		}
	}
	
	public void mergeCountNumToRs(List<Record> list, String toEqColumn, String fromEqColumn, String newColumn) 
	{
		mergeCountNumToRs(list, toEqColumn, fromEqColumn, newColumn, null);
	}
	
	public void mergeCountNumToRs(List<Record> list, String toEqColumn, String fromEqColumn, String newColumn, String whereColumns, Object... values) 
	{
		if (list == null || list.isEmpty()) 
		{
			return;
		}
		RecordKit.addColumn(list, newColumn, 0L);
		
		List<Integer> ids = RecordKit.getToList(list, toEqColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		//sql： "select " + fromEqColumn + ", count(id) as " + newColumn + " from " + getTable().getName() + " where " + fromEqColumn + " in (??) group by " + fromEqColumn;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(fromEqColumn)
				  .append(", count(id) as ")
				  .append(newColumn)
				  .append(" from ")
				  .append(getTable().getName())
				  .append(" where ");
		if (whereColumns != null) 
		{
			String where = buildWhereBlock(whereColumns);
			sqlBuilder.append(where)
					  .append(" and ");
		}
		sqlBuilder.append(fromEqColumn)
				  .append(" in (??) ")
				  .append(" group by ")
				  .append(fromEqColumn);
		
		String sql = buildSqlHasIn(sqlBuilder.toString(), ids);
		
		List<M> replyNums = find(sql, values);
		if (replyNums.isEmpty()) 
		{
			return;
		}
		RecordKit.mergeColumnsTo(list, replyNums, toEqColumn, fromEqColumn, newColumn);
	}
	
	public <K extends Model<K>> void mergeCountNumTo(K m, String newColumnName, String whereColumns, Object...values) 
	{
	 	long countNum = countByColumns(whereColumns, values);
		m.put(newColumnName, countNum);
	}
	
	public void mergeStatusColumnTo(List<Record> list, String toEqColumn, String fromEqColumn, String newColumn) 
	{
		mergeStatusColumnTo(list, toEqColumn, fromEqColumn, newColumn, null);
	}
	
	public void mergeStatusColumnTo(List<Record> list, String toEqColumn, String fromEqColumn, String newColumn, String whereColumns, Object...values) 
	{
		if (list != null && list.isEmpty()) 
		{
			return;
		}
		
		RecordKit.addColumn(list, newColumn, COLUMN_STATUS_NO);
		
		List<Integer> ids = RecordKit.getToList(list, toEqColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(fromEqColumn)
				  .append(" from ")
				  .append(getTable().getName())
				  .append(" where ");
		if (whereColumns != null) 
		{
			String where = buildWhereBlock(whereColumns);
			sqlBuilder.append(where)
					  .append(" and ");
		}
		sqlBuilder.append(fromEqColumn)
				  .append(" in (??) ");
		
		String sql = buildSqlHasIn(sqlBuilder.toString(), ids);
		
		List<M> mList = find(sql, values);
		if (mList.isEmpty()) 
		{
			return;
		}
		for (M m : mList) 
		{
			Object fromEqVal = m.get(fromEqColumn);
			for (Record r : list)
			{
				Object toEqVal = r.get(toEqColumn);
				if (fromEqVal.equals(toEqVal)) 
				{
					r.set(newColumn, COLUMN_STATUS_YES);
				}
			}
		}
	}
	
	public <T extends Model<T>> void mergeStatusColumnTo(T t, String newColumnName, String whereColumns, Object...values) 
	{
		long count = countByColumns(whereColumns, values);
		if (count > 0) 
		{
			t.put(newColumnName, COLUMN_STATUS_YES);
		}else 
		{
			t.put(newColumnName, COLUMN_STATUS_NO);
		}
	}
	
	public void mergeModelsTo(List<Record> toRecords, String eqToColumn, String mergeColumns, String eqFromColumn, String mergedColumnName) 
	{
		if (toRecords == null || toRecords.isEmpty()) 
		{
			return;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(mergeColumns)
				  .append(" from `")
				  .append(getTable().getName())
				  .append("` where ")
				  .append(eqFromColumn)
				  .append(" in (??) ");
		appendModelsTo(toRecords, eqToColumn, eqFromColumn, mergedColumnName, sqlBuilder.toString());
	}
	
	public void mergeModelsTo(List<Record> toRecords, String eqToColumn, String mergeColumns, String eqFromColumn, String mergedColumnName, String whereColumns, Object...params) 
	{
		if (toRecords == null || toRecords.isEmpty()) 
		{
			return;
		}
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(mergeColumns)
				  .append(" from `")
				  .append(getTable().getName())
				  .append("` where ")
				  .append(eqFromColumn)
				  .append(" in (??) ");
		String[] otherWhereColumnArr = whereColumns.split(",");
		for (String where : otherWhereColumnArr) 
		{
			sqlBuilder.append(" and ")
					  .append(where)
					  .append(" = ? ");
		}
		
		appendModelsTo(toRecords, eqToColumn, eqFromColumn, mergedColumnName, sqlBuilder.toString(), params);
	}
	
	
	public <T extends Model<T>> void mergeModelsToMs(List<T> toRecords, String eqToColumn, String mergeColumns, String eqFromColumn, String mergedColumnName, String whereColumns, Object...params) 
	{
		if (toRecords == null || toRecords.isEmpty()) 
		{
			return;
		}
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ")
				  .append(mergeColumns)
				  .append(" from `")
				  .append(getTable().getName())
				  .append("` where ")
				  .append(eqFromColumn)
				  .append(" in (??) ");
		String[] otherWhereColumnArr = whereColumns.split(",");
		for (String where : otherWhereColumnArr) 
		{
			sqlBuilder.append(" and ")
					  .append(where)
					  .append(" = ? ");
		}
		
		appendModelsToMs(toRecords, eqToColumn, eqFromColumn, mergedColumnName, sqlBuilder.toString(), params);
	}
	
	public <T extends Model<T>> void mergeModelsTo(T tweet, String selectColumns, String newColumnName, String whereColumns, Object...values) 
	{
		if (tweet == null) 
		{
			return;
		}
		List<M> images = findByColumns(selectColumns, whereColumns, values);
		tweet.put(newColumnName, images);
	}

	private void appendModelsTo(List<Record> toRecords, String eqToColumn, String eqSourceColumn, String newColumnName, String sql, Object...params) 
	{
		logger.info(sql);
		List<Integer> ids = RecordKit.getToList(toRecords, eqToColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		List<M> models = findIn(sql, ids, params);
		
		RecordKit.mergeModelsTo(toRecords, models, eqToColumn, eqSourceColumn, newColumnName);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Model<T>> void appendModelsToMs(List<T> toRecords, String eqToColumn, String eqSourceColumn, String newColumnName, String sql, Object...params) 
	{
		List<Integer> ids = ModelKit.getToList(toRecords, eqToColumn, Integer.class);
		
		if (ids == null || ids.isEmpty()) 
		{
			return;
		}
		
		List<M> models = findIn(sql, ids, params);
		
		ModelKit.mergeModelsToMs(toRecords, models, eqToColumn, eqSourceColumn, newColumnName);
	}
	
	
	/*
	 ***********************
	 *****  SQL 操作  ******            
	 ***********************
	 */
	
	public String buildSqlHasIn(String sql, List<Integer> vals) 
	{
		if (vals == null || vals.isEmpty()) 
		{
			return sql.replace("(??)", "(-1)");
		}
		
		StringBuilder valStr = new StringBuilder();
		for (int val : vals) 
		{
			valStr.append(val).append(",");
		}
		String inVals = valStr.substring(0, valStr.length() - 1);
		sql = sql.replace("(??)", "(" + inVals + ")");
		return sql;
	}
	
	private String buildWhereBlock(String columns) 
	{
		String[] cols = columns.split(",");
		
		StringBuilder where = new StringBuilder();
		for (int i = 0; i < cols.length; i++) 
		{
			where.append(cols[i]).append(" = ? ");
			if(i < cols.length - 1)
			{
				where.append(" and ");
			}
		}
		return where.toString();
	}
	
	
	
	/*
	 ***********************
	 ***  Model 基础操作 ***            
	 ***********************
	 */
	
	/*private Config getConfig() {
		return DbKit.getConfig(getClass());
	}*/
	
	private Table getTable() {
		return TableMapping.me().getTable(getClass());
	}
	
	private Map<String, Class<?>> getModelAttrs()
	{
		if (modelAttrs == null) {
			Table table = TableMapping.me().getTable(getClass());
			modelAttrs = table.getColumnTypeMap();
		}
		return modelAttrs;
	}
}
