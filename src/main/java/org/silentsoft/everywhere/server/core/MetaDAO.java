package org.silentsoft.everywhere.server.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.core.util.DateUtil;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.core.type.DatabaseType;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MetaDAO extends AbstractDAO {
	
	private enum Query {
		SELECT_COLUMNS,
		SELECT_PK_COLUMNS,
		SELECT_CURRENT_TIME
	}
	
	private String getQuery(Query query, String databaseType, String...params) {
		StringBuffer returnQuery = new StringBuffer();
		
		DatabaseType dbType = Enum.valueOf(DatabaseType.class, databaseType);
		
		switch(query) {
		case SELECT_COLUMNS:
			switch (dbType) {
			case Oracle:
				returnQuery.append("SELECT COLUMN_NAME \n");
				returnQuery.append("  FROM ALL_TAB_COLUMNS \n");
				returnQuery.append(" WHERE TABLE_NAME = :tableName \n");
				break;
			case PostgreSQL:
				returnQuery.append("SELECT COLUMN_NAME \n");
				returnQuery.append("  FROM INFORMATION_SCHEMA.COLUMNS \n");
				returnQuery.append(" WHERE TABLE_NAME = :tableName \n");
				break;
			}
			break;
		case SELECT_PK_COLUMNS:
			switch (dbType) {
			case Oracle:
				returnQuery.append("SELECT C.COLUMN_NAME \n");
				returnQuery.append("  FROM ALL_CONS_COLUMNS C, \n");
				returnQuery.append("       ALL_CONSTRAINTS S \n");
				returnQuery.append(" WHERE C.CONSTRAINT_NAME = S.CONSTRAINT_NAME \n");
				returnQuery.append("   AND S.CONSTRAINT_TYPE = 'P' \n");
				returnQuery.append("   AND C.TABLE_NAME = :tableName \n");
				break;
			case PostgreSQL:
				returnQuery.append("SELECT C.COLUMN_NAME \n");
				returnQuery.append("  FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE C, \n");
				returnQuery.append("       INFORMATION_SCHEMA.TABLE_CONSTRAINTS S \n");
				returnQuery.append(" WHERE C.CONSTRAINT_NAME = S.CONSTRAINT_NAME \n");
				returnQuery.append("   AND S.CONSTRAINT_TYPE = 'PRIMARY KEY' \n");
				returnQuery.append("   AND C.TABLE_NAME = :tableName \n");
				break;
			}
			break;
		case SELECT_CURRENT_TIME:
			switch (dbType) {
			case Oracle:
				returnQuery.append("SELECT TO_CHAR(SYSDATE,'").append(params[0]).append("') AS DT FROM DUAL \n");
				break;
			case PostgreSQL:
				returnQuery.append("SELECT TO_CHAR(NOW(),'").append(params[0]).append("') AS DT \n");
				break;
			}
			break;
		}
		
		return returnQuery.toString();
	}
	
	public List<String> getColumns(String tableName) throws Exception {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tableName", tableName);
		
		return queryForList(getQuery(Query.SELECT_COLUMNS, SysUtil.getProperty(PropertyKey.CACHE_DATABASE_TYPE)), hashMap, String.class);
	}

	public List<String> getPKColumns(String tableName) throws Exception {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tableName", tableName);

		return queryForList(getQuery(Query.SELECT_PK_COLUMNS, SysUtil.getProperty(PropertyKey.CACHE_DATABASE_TYPE)), hashMap, String.class);
	}

	public String getCurrentTime() throws Exception {
		return getCurrentTime(DateUtil.DATEFORMAT_YYYYMMDDHH24MISS);
	}

	public String getCurrentTime(String format) throws Exception {
		return query(getQuery(Query.SELECT_CURRENT_TIME, SysUtil.getProperty(PropertyKey.CACHE_DATABASE_TYPE), format), new ResultSetExtractor<String>() {

			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString("DT");
				}
				return DateUtil.getSystemDateAsStr(DateUtil.DATEFORMAT_YYYYMMDDHHMMSS);
			}
		});
	}
}
