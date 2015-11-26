package org.silentsoft.everywhere.server.core;

import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.io.memory.SharedThreadMemory;

public class AbstractTest {

	public AbstractTest() {
		BeanUtil.setApplicationContextForJUnitTest();
		
		SharedThreadMemory.create();
		SharedThreadMemory.put(BizConst.KEY_USER_ID, "JUNIT");
	}
	
}
