package org.silentsoft.everywhere.server.fx.search.service;

import java.util.List;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.model.table.TbmSmUserDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
	
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
		
//		return (returnDVO.size() > CommonConst.SIZE_EMPTY) ? returnDVO.get(CommonConst.FIRST_INDEX) : tbmSmUserDVO;
		return (returnDVO.size() > CommonConst.SIZE_EMPTY) ? returnDVO.get(CommonConst.FIRST_INDEX) : null;
	}

}
