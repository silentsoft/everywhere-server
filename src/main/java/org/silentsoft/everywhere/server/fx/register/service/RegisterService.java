package org.silentsoft.everywhere.server.fx.register.service;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.fx.login.biz.LoginBiz;
import org.silentsoft.everywhere.server.fx.register.biz.RegisterBiz;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);
			
	private RegisterBiz registerBiz;
	
	private RegisterBiz getRegisterBiz() {
		if (registerBiz == null) {
			registerBiz = BeanUtil.getBean(RegisterBiz.class);
		}
		
		return registerBiz;
	}
	
	public int createUserInfo(TbmSmUserDVO inputDVO) throws Exception {
		return getRegisterBiz().createUserInfo(inputDVO);
	}
}
