package org.silentsoft.everywhere.server.fx.register.service;

import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.register.biz.RegisterBiz;
import org.silentsoft.everywhere.server.fx.search.biz.SearchBiz;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);
			
	private RegisterBiz registerBiz;
	
	private SearchBiz searchBiz;
	
	private RegisterBiz getRegisterBiz() {
		if (registerBiz == null) {
			registerBiz = BeanUtil.getBean(RegisterBiz.class);
		}
		
		return registerBiz;
	}
	
	private SearchBiz getSearchBiz() {
		if (searchBiz == null) {
			searchBiz = BeanUtil.getBean(SearchBiz.class);
		}
		
		return searchBiz;
	}
	
	public int createUserInfo(TbmSmUserDVO inputDVO) throws Exception {
		if (ObjectUtil.isNotEmpty(getSearchBiz().getUserInfo(inputDVO))) {
			// if already exist user in database..
			return -1;
		}
		
		return getRegisterBiz().createUserInfo(inputDVO);
	}
}
