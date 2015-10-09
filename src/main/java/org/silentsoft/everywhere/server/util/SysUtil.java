package org.silentsoft.everywhere.server.util;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.data.DataMap;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.core.SharedThreadMemory;
import org.silentsoft.everywhere.server.core.MetaDAO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class SysUtil {
	
	private static MetaDAO metaDao;
	
	private static DataMap cacheMap;
	
	private static synchronized MetaDAO getMetaDAO() {
		if (metaDao == null) {
			metaDao = BeanUtil.getBean(MetaDAO.class);
		}
		
		return metaDao;
	}
	
	private static synchronized DataMap getCacheMap() {
		if (cacheMap == null) {
			cacheMap = new DataMap();
		}
		
		return cacheMap;
	}
	
	public static String getUserId() {
		return ObjectUtil.toString(SharedThreadMemory.get(BizConst.KEY_USER_ID), BizConst.DEFAULT_USER_ID);
	}
	
	public static String getUserUniqueSeq() {
		return ObjectUtil.toString(SharedThreadMemory.get(BizConst.KEY_USER_UNIQUE_SEQ));
	}
	
	public static String getCurrentTime() {
		String currentTime = "";
		
		try {
			currentTime = getMetaDAO().getCurrentTime();
		} catch (Exception e) {
			;
		}
		
		return currentTime;
	}
	
	public static String getCurrentTime(String format) {
		String currentTime = "";
		
		try {
			currentTime = getMetaDAO().getCurrentTime(format);
		} catch (Exception e) {
			;
		}
		
		return currentTime;
	}
	
	public static String getProperty(String key) {
		return getProperty(key, CommonConst.NULL_STR);
	}
	
	public static String getProperty(String key, String defaultValue) {
		String property = "";
		
		try {
			boolean putCache = false;
			if (key.startsWith("cache.")) {
				if (getCacheMap().containsKey(key)) {
					return ObjectUtil.toString(getCacheMap().get(key));
				} else {
					putCache = true;
				}
			}
			
			property = PropertiesLoaderUtils.loadProperties(new ClassPathResource("META-INF/config/properties/config.properties")).getProperty(key, defaultValue);
			
			if (putCache) {
				getCacheMap().put(key, property);
			}
		} catch (Exception e) {
			property = (defaultValue == null) ? CommonConst.NULL_STR : defaultValue;
		}
		
		return property;
	}
}
