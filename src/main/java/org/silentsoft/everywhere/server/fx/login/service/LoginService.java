package org.silentsoft.everywhere.server.fx.login.service;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.fx.login.biz.LoginBiz;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
			
	private LoginBiz loginBiz;
	
	private LoginBiz getLoginBiz() {
		if (loginBiz == null) {
			loginBiz = BeanUtil.getBean(LoginBiz.class);
		}
		
		return loginBiz;
	}
	
	public TbmSmUserDVO getTbmSmUserDVO(TbmSmUserDVO inputDVO) {
		TbmSmUserDVO user = getLoginBiz().getUserInfo(inputDVO);
		if (user == null) {
			LOGGER.debug("User <{}> not found !", new Object[]{inputDVO.getSingleId()});
			return null;
		}
		
		LOGGER.debug("in DB : <{}> , user typed : <{}>", new Object[]{user.getUserPwd(), inputDVO.getUserPwd()});
		if (SecurityUtil.comparePassword(user.getUserPwd(), inputDVO.getUserPwd())) {
			LOGGER.debug("Password validation pass !!");
			return user;
		} else {
			LOGGER.debug("Password is not match !");
		}
		
		return null;
	}
	
	
}
