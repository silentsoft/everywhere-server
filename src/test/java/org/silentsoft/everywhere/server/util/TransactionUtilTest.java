package org.silentsoft.everywhere.server.util;

import org.junit.Assert;
import org.junit.Test;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.core.AbstractTest;

public class TransactionUtilTest extends AbstractTest {
	
	@Test
	public void commitTest() {
		boolean result = TransactionUtil.doScope(() -> {
			{
				TbmSysUserDVO user = new TbmSysUserDVO();
				user.setUserSeq("commit");
				user.setUserId("commit");
				user.setUserPwd("commit");
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
				TbmSysUserDVO user = new TbmSysUserDVO();
				user.setUserSeq("rollback");
				user.setUserId("rollback");
				user.setUserPwd("rollback");
				CrudUtil.remove(user);
				CrudUtil.create(user);
			}
			{
				TbmSysUserDVO user = new TbmSysUserDVO();
				user.setUserSeq("rollback");
				user.setUserId("rollback");
				user.setUserPwd("rollback");
				CrudUtil.create(user);
			}
			
			return true;
		}, () -> {
			return false;
		});
		
		Assert.assertFalse(result);
	}
}
