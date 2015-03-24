package org.silentsoft.everywhere.server.fx.search.service;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.search.biz.SearchBiz;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
	private SearchBiz searchBiz;
	
	private SearchBiz getSearchBiz() {
		if (searchBiz == null) {
			searchBiz = BeanUtil.getBean(SearchBiz.class);
		}
		
		return searchBiz;
	}
	
	public TbmSmUserDVO getTbmSmUserDVO(TbmSmUserDVO inputDVO) {
		return getSearchBiz().getUserInfo(inputDVO);
	}
}
