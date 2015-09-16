package org.silentsoft.everywhere.server.fx.login.service;

import java.util.List;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
			
	private TbmSmUserDQM tbmSmUserDQM;
	
	private TbmSmUserDQM getTbmSmUserDQM() {
		if (tbmSmUserDQM == null) {
			tbmSmUserDQM = BeanUtil.getBean(TbmSmUserDQM.class);
		}
		
		return tbmSmUserDQM;
	}
	
	public TbmSmUserDVO getTbmSmUserDVO(TbmSmUserDVO inputDVO) {
		List<TbmSmUserDVO> tbmSmUserDVOList = getTbmSmUserDQM().getTbmSmUser(ObjectUtil.toMap(inputDVO));
		
		TbmSmUserDVO user = (tbmSmUserDVOList.size() > CommonConst.SIZE_EMPTY) ? tbmSmUserDVOList.get(CommonConst.FIRST_INDEX) : null;
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
	
	public int updateUserInfo(TbmSmUserDVO tbmSmUserDVO) throws Exception {
		return CrudUtil.update(tbmSmUserDVO);
	}
}
