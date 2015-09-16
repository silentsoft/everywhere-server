package org.silentsoft.everywhere.server.fx.search.controller;

import org.silentsoft.everywhere.context.host.EverywhereException;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.server.fx.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fx/search")
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value="/user", method=RequestMethod.POST)
	@ResponseBody
	public TbmSmUserDVO getTbmSmUserDVO(@RequestBody String json) {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSmUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSmUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		return searchService.getUserInfo(inputDVO);
	}
}
