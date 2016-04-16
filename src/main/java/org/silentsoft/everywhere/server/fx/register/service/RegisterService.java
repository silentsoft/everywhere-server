package org.silentsoft.everywhere.server.fx.register.service;

import org.silentsoft.core.util.GenerateUtil;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.fx.search.service.SearchService;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);
			
	@Autowired
	private SearchService searchService;
	
	public int createUserInfo(TbmSysUserDVO tbmSysUserDVO) throws Exception {
		if (ObjectUtil.isNotEmpty(searchService.getUserById(tbmSysUserDVO))) {
			// if already exist id in database..
			return -1;
		}
		
		if (ObjectUtil.isNotEmpty(searchService.getUserByEmail(tbmSysUserDVO))) {
			// if already exist email in database..
			return -1;
		}
		
		tbmSysUserDVO.setUserSeq(GenerateUtil.generateUUID().concat(GenerateUtil.generateUUID()));
		tbmSysUserDVO.setStoreRoot(GenerateUtil.generateUUID());
		
		return CrudUtil.create(tbmSysUserDVO);
	}
}
