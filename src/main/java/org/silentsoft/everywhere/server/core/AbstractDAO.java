package org.silentsoft.everywhere.server.core;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class AbstractDAO extends NamedParameterJdbcTemplate {

	@SuppressWarnings("resource")
	public AbstractDAO() {
		super((DataSource) new ClassPathXmlApplicationContext(
				"META-INF/config/spring/application-context.xml").getBean("businessDataSource"));
	}
	
	@SuppressWarnings("rawtypes")
	public static int countMap(final Map sourceMap) {
		return sourceMap.size();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean validKey(final Map sourceMap, String key) {
		return (sourceMap.get(key) != null && !"".equals(sourceMap.get(key)));
	}
}