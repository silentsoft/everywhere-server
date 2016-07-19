package org.silentsoft.everywhere.server.core;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class AbstractDAO extends NamedParameterJdbcTemplate {
	
	public AbstractDAO() {
		this("businessDataSource");
	}
	
	public AbstractDAO(String dataSource) {
		super(BeanUtil.get(dataSource, DataSource.class));
	}
	
	@SuppressWarnings("rawtypes")
	protected int countMap(final Map sourceMap) {
		return sourceMap.size();
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean isValidKey(final Map sourceMap, String key) {
		return (sourceMap.get(key) != null && !"".equals(sourceMap.get(key)));
	}
	
	protected boolean isVelocity(String sql) {
		return ((sql.indexOf("#if") > -1 || sql.indexOf("#foreach") > -1) && sql.indexOf("#end") > -1);
	}
	
	@Override
	public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
		if (isVelocity(sql)) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(new VelocityContext(paramMap), writer, "AbstractDAO", sql);
			sql = writer.toString();
		}
		
		return super.query(sql, paramMap, rowMapper);
	}
}