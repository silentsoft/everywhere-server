package org.silentsoft.everywhere.server.fx.register.controller;

import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.register.service.RegisterService;
import org.silentsoft.everywhere.server.util.TransactionUtil;
import org.silentsoft.io.memory.SharedThreadMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fx/register")
public class RegisterController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping(value="/authentication", method=RequestMethod.POST)
	@ResponseBody
	public TbmSmUserDVO createTbmSmUserDVO(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSmUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSmUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		SharedThreadMemory.create();
		
		SharedThreadMemory.put(BizConst.KEY_USER_ID, inputDVO.getUserId());
		
		if (registerService.createUserInfo(inputDVO) != -1) {
			SharedThreadMemory.delete();
			return inputDVO;
		}
		
		SharedThreadMemory.delete();
		
		return null;
	}
}
