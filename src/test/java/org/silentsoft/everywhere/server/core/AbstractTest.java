package org.silentsoft.everywhere.server.core;

import org.junit.runner.RunWith;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.io.memory.SharedThreadMemory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/config/spring/application-context.xml", "classpath*:META-INF/config/springmvc/application-servlet.xml"})
public class AbstractTest {

	public AbstractTest() {
		SharedThreadMemory.create();
		SharedThreadMemory.put(BizConst.KEY_USER_ID, "JUNIT");
	}
	
}
