package org.silentsoft.everywhere.server.fx.register.service;

import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.core.util.SystemUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.fx.search.service.SearchService;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
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
	
	private TbmSmUserDQM tbmSmUserDQM;
	
	private TbmSmUserDQM getTbmSmUserDQM() {
		if (tbmSmUserDQM == null) {
			tbmSmUserDQM = BeanUtil.getBean(TbmSmUserDQM.class);
		}
		
		return tbmSmUserDQM;
	}
	
	public int createUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		if (ObjectUtil.isNotEmpty(searchService.getUserInfo(tbmSmUserDVO))) {
			// if already exist user in database..
			return -1;
		}
		
		String uniqueSeq = SystemUtil.getUUID().replaceAll("-", "").concat(SecurityUtil.HASH_MD5(tbmSmUserDVO.getUserId()));
		tbmSmUserDVO.setUniqueSeq(uniqueSeq);
		
		return CrudUtil.create(tbmSmUserDVO);
	}
}
