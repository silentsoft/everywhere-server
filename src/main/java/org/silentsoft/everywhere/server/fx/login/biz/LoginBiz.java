package org.silentsoft.everywhere.server.fx.login.biz;

import java.util.List;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginBiz {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginBiz.class);
			
	private TbmSmUserDQM tbmSmUserDQM;
	
	private TbmSmUserDQM getTbmSmUserDQM() {
		if (tbmSmUserDQM == null) {
			tbmSmUserDQM = BeanUtil.getBean(TbmSmUserDQM.class);
		}
		
		return tbmSmUserDQM;
	}
	
	@SuppressWarnings("unchecked")
	public TbmSmUserDVO getUserInfo(TbmSmUserDVO tbmSmUserDVO) {
		List<TbmSmUserDVO> returnDVO = getTbmSmUserDQM().getTbmSmUser(ObjectUtil.toMap(tbmSmUserDVO));
		
		return (returnDVO.size() > CommonConst.SIZE_EMPTY) ? returnDVO.get(CommonConst.FIRST_INDEX) : null;
	}
	
	public int updateUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		return CrudUtil.update(tbmSmUserDVO);
	}
}
