package org.silentsoft.everywhere.server.fx.register.biz;

import org.silentsoft.core.util.SysUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterBiz {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBiz.class);
			
	private TbmSmUserDQM tbmSmUserDQM;
	
	private TbmSmUserDQM getTbmSmUserDQM() {
		if (tbmSmUserDQM == null) {
			tbmSmUserDQM = BeanUtil.getBean(TbmSmUserDQM.class);
		}
		
		return tbmSmUserDQM;
	}
	
	@SuppressWarnings("unchecked")
	public int createUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		String uniqueSeq = SysUtil.getUUID().replaceAll("-", "").concat(SecurityUtil.HASH_MD5(tbmSmUserDVO.getUserId()));
		tbmSmUserDVO.setUniqueSeq(uniqueSeq);
		
		return CrudUtil.create(tbmSmUserDVO);
	}
}
