package org.silentsoft.everywhere.server.fx.modify.controller;

import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.modify.service.ModifyService;
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
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public TbmSmUserDVO modifyTbmSmUserDVO(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSmUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSmUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		if (modifyService.modifyUserInfo(inputDVO) != -1) {
			return inputDVO;
		}
		
		return null;
	}
}
