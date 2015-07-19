package org.silentsoft.everywhere.server.fx.login.controller;

import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.core.SharedThreadMemory;
import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.fx.login.service.LoginService;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fx/login")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="/authentication", method=RequestMethod.POST)
	@ResponseBody
	public TbmSmUserDVO getTbmSmUserDVO(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSmUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSmUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		TbmSmUserDVO outputDVO = loginService.getTbmSmUserDVO(inputDVO);
		if (ObjectUtil.isNotEmpty(outputDVO)) {
			SharedThreadMemory.put(BizConst.KEY_USER_ID, outputDVO.getUserId());
			SharedThreadMemory.put(BizConst.KEY_USER_NM, outputDVO.getUserNm());
			
			/**
			 * if want show to past logged in time
			 * 
			 *  String prevFnlAccsDt = outputDVO.getFnlAccsDt();
			
				outputDVO.setFnlAccsDt(SysUtil.getCurrentTime());
				loginService.updateUserInfo(outputDVO);
				
				outputDVO.setFnlAccsDt(prevFnlAccsDt);
			 */
			outputDVO.setFnlAccsDt(SysUtil.getCurrentTime());
			outputDVO.setFnlAccsIp(inputDVO.getFnlAccsIp());
			outputDVO.setLangCode(inputDVO.getLangCode());
			loginService.updateUserInfo(outputDVO);
		}
		
		return outputDVO;
	}
}
