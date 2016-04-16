package org.silentsoft.everywhere.server.fx.modify.service;

import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyService.class);
	
	public int modifyUserInfo(TbmSysUserDVO tbmSysUserDVO) throws Exception {
		return CrudUtil.update(tbmSysUserDVO);
	}

}
