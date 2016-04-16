package org.silentsoft.everywhere.server.fx.login.service;

import java.util.List;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.silentsoft.everywhere.server.model.table.TbmSysUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
			
	private TbmSysUserDQM tbmSysUserDQM;
	
	private TbmSysUserDQM getTbmSmUserDQM() {
		if (tbmSysUserDQM == null) {
			tbmSysUserDQM = BeanUtil.getBean(TbmSysUserDQM.class);
		}
		
		return tbmSysUserDQM;
	}
	
	@SuppressWarnings("unchecked")
	public TbmSysUserDVO getUserById(TbmSysUserDVO inputDVO) {
		return getUser(inputDVO, getTbmSmUserDQM().getUserById(ObjectUtil.toMap(inputDVO)));
	}
	
	@SuppressWarnings("unchecked")
	public TbmSysUserDVO getUserByEmail(TbmSysUserDVO inputDVO) {
		return getUser(inputDVO, getTbmSmUserDQM().getUserByEmail(ObjectUtil.toMap(inputDVO)));
	}
	
	private TbmSysUserDVO getUser(TbmSysUserDVO inputDVO, List<TbmSysUserDVO> tbmSysUserDVOList) {
		TbmSysUserDVO user = (tbmSysUserDVOList.size() > CommonConst.SIZE_EMPTY) ? tbmSysUserDVOList.get(CommonConst.FIRST_INDEX) : null;
		if (user == null) {
			LOGGER.debug("User <{}> not found !", new Object[]{inputDVO.getUserId()});
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
	
	public int updateUserInfo(TbmSysUserDVO tbmSysUserDVO) throws Exception {
		return CrudUtil.update(tbmSysUserDVO);
	}
}
