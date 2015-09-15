package org.silentsoft.everywhere.server.util;

import org.junit.Assert;
import org.junit.Test;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.core.AbstractTest;

public class TransactionUtilTest extends AbstractTest {
	
	@Test
	public void commitTest() {
		boolean result = TransactionUtil.doScope(() -> {
			{
				TbmSmUserDVO user = new TbmSmUserDVO();
				user.setUserId("commit");
				user.setUserPwd("commit");
				user.setUserNm("commit");
				user.setSingleId("commit");
				user.setUniqueSeq("commit");
				CrudUtil.remove(user);
				CrudUtil.create(user);
			}
			
			return true;
		});
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void rollbackTest() {
		boolean result = TransactionUtil.doScope(() -> {
			{
				TbmSmUserDVO user = new TbmSmUserDVO();
				user.setUserId("rollback");
				user.setUserPwd("rollback");
				user.setUserNm("rollback");
				user.setSingleId("rollback");
				user.setUniqueSeq("rollback");
				CrudUtil.remove(user);
				CrudUtil.create(user);
			}
			{
				TbmSmUserDVO user = new TbmSmUserDVO();
				user.setUserId("rollback");
				user.setUserPwd("rollback");
				user.setUserNm("rollback");
				user.setSingleId("rollback");
				user.setUniqueSeq("rollback");
				CrudUtil.create(user);
			}
			
			return true;
		}, () -> {
			return false;
		});
		
		Assert.assertFalse(result);
	}
}
