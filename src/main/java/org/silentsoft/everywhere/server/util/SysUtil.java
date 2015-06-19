package org.silentsoft.everywhere.server.util;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.data.DataMap;
import org.silentsoft.core.util.ObjectUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class SysUtil {
	
	private static DataMap cacheMap;
	
	private static synchronized DataMap getCacheMap() {
		if (cacheMap == null) {
			cacheMap = new DataMap();
		}
		
		return cacheMap;
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
