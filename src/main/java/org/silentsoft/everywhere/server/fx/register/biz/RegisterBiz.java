package org.silentsoft.everywhere.server.fx.register.biz;

import org.silentsoft.core.util.SysUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.silentsoft.everywhere.server.util.TransactionUtil;
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
	
	public int createUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		String uniqueSeq = SysUtil.getUUID().replaceAll("-", "").concat(SecurityUtil.HASH_MD5(tbmSmUserDVO.getUserId()));
		tbmSmUserDVO.setUniqueSeq(uniqueSeq);
		
		int result = TransactionUtil.doScope(() -> {
			TbmSmUserDVO test = new TbmSmUserDVO();
			test.setUserId("transaction");
			test.setUserNm("transaction");
			test.setUserPwd("transaction");
			test.setSingleId("transaction");
			test.setUniqueSeq("transaction");
			CrudUtil.create(test);
			CrudUtil.create(test);
			return 1;
		}, () -> {
			return 2;
		});
		
		LOGGER.info("Transaction Util Test result is " + result);

		return CrudUtil.create(tbmSmUserDVO);
	}
}
