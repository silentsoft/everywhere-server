package org.silentsoft.everywhere.server.fx.modify.service;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.modify.biz.ModifyBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyService.class);
	
	private ModifyBiz modifyBiz;
	
	private ModifyBiz getModifyBiz() {
		if (modifyBiz == null) {
			modifyBiz = new ModifyBiz();
		}
		
		return modifyBiz;
	}
	
	public int modifyUserInfo(TbmSmUserDVO inputDVO) throws Exception {
		return getModifyBiz().updateUserInfo(inputDVO);
	}

}
