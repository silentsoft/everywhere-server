package org.silentsoft.everywhere.server.fx.modify.controller;

import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.fx.modify.service.ModifyService;
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
@RequestMapping("/fx/modify")
public class ModifyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyController.class);
	
	@Autowired
	private ModifyService modifyService;
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public TbmSysUserDVO modifyTbmSmUserDVO(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSysUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSysUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		if (modifyService.modifyUserInfo(inputDVO) != -1) {
			return inputDVO;
		}
		
		return null;
	}
	
	@RequestMapping(value="/check", method=RequestMethod.POST)
	@ResponseBody
	public boolean checkPassword(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSysUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSysUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		return searchService.checkUserByPassword(inputDVO);
	}
}
