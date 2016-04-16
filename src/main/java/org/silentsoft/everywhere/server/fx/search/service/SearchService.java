package org.silentsoft.everywhere.server.fx.search.service;

import java.util.List;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.model.table.TbmSysUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
	
	private TbmSysUserDQM tbmSysUserDQM;
	
	private TbmSysUserDQM getTbmSmUserDQM() {
		if (tbmSysUserDQM == null) {
			tbmSysUserDQM = BeanUtil.getBean(TbmSysUserDQM.class);
		}
		
		return tbmSysUserDQM;
	}
	
	@SuppressWarnings("unchecked")
	public TbmSysUserDVO getUserById(TbmSysUserDVO tbmSysUserDVO) {
		List<TbmSysUserDVO> returnDVO = getTbmSmUserDQM().getUserById(ObjectUtil.toMap(tbmSysUserDVO));
		
		return (returnDVO.size() > CommonConst.SIZE_EMPTY) ? returnDVO.get(CommonConst.FIRST_INDEX) : null;
	}

	@SuppressWarnings("unchecked")
	public TbmSysUserDVO getUserByEmail(TbmSysUserDVO tbmSysUserDVO) {
		List<TbmSysUserDVO> returnDVO = getTbmSmUserDQM().getUserByEmail(ObjectUtil.toMap(tbmSysUserDVO));
		
		return (returnDVO.size() > CommonConst.SIZE_EMPTY) ? returnDVO.get(CommonConst.FIRST_INDEX) : null;
	}
}
