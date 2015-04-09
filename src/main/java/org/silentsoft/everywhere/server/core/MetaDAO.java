package org.silentsoft.everywhere.server.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.core.util.DateUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MetaDAO extends AbstractDAO {
	
	final String query = "select column_name from ALL_TAB_COLUMNS where table_name = :tableName ";

	final String pkColumnsQuery = "select C.COLUMN_NAME from ALL_CONS_COLUMNS C, ALL_CONSTRAINTS S "
			+ " where C.CONSTRAINT_NAME = S.CONSTRAINT_NAME and S.CONSTRAINT_TYPE = 'P' AND C.TABLE_NAME = :tableName";
	
	public List<String> getColumns(String tableName) throws Exception {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tableName", tableName);
		
		return queryForList(query, hashMap, String.class);
	}

	public List<String> getPKColumns(String tableName) throws Exception {
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tableName", tableName);

		return queryForList(pkColumnsQuery, hashMap, String.class);
	}

	public String getCurrentTime() throws Exception {
		return getCurrentTime(DateUtil.DATEFORMAT_YYYYMMDDHH24MISS);
	}

	public String getCurrentTime(String format) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TO_CHAR(SYSDATE,'").append(format).append("') AS DT FROM DUAL\n");

		return query(sb.toString(), new ResultSetExtractor<String>() {

			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getString("DT");
				}
				return DateUtil.getSystemDateAsStr(DateUtil.DATEFORMAT_YYYYMMDDHHMMSS);
			}
		});
	}
}
