package org.silentsoft.everywhere.server.fx.modify.biz;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.util.CrudUtil;

public class ModifyBiz {

	public int updateUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		return CrudUtil.update(tbmSmUserDVO);
	}
}
