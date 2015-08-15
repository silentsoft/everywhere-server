package org.silentsoft.everywhere.server.core.controller;

import org.silentsoft.everywhere.context.host.EverywhereException;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.core.type.DatabaseType;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.springframework.stereotype.Controller;

@Controller
public final class ValidationController {
	
	protected ValidationController() throws Exception {
		String databaseType = SysUtil.getProperty(PropertyKey.CACHE_DATABASE_TYPE);
		try {
			switch (Enum.valueOf(DatabaseType.class, databaseType)) {
			case Oracle: break;
			case PostgreSQL: break;
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			throw new EverywhereException("Not support <{}> database type !", new Object[]{ databaseType });
		}
	}
}
