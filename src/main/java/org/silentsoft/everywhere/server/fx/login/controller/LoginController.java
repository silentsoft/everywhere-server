package org.silentsoft.everywhere.server.fx.login.controller;

import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.fx.login.service.LoginService;
import org.silentsoft.everywhere.server.util.SysUtil;
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
@RequestMapping("/fx/login")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="/authentication", method=RequestMethod.POST)
	@ResponseBody
	public TbmSysUserDVO getTbmSmUserDVO(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		TbmSysUserDVO inputDVO = null;
		
		try {
			inputDVO = JSONUtil.JSONToObject(json, TbmSysUserDVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		TbmSysUserDVO outputDVO = null;
		if (ObjectUtil.isNotEmpty(inputDVO.getUserId())) {
			outputDVO = loginService.getUserById(inputDVO);
		} else if (ObjectUtil.isNotEmpty(inputDVO.getEmailAddr())) {
			outputDVO = loginService.getUserByEmail(inputDVO);
		}
		
		if (ObjectUtil.isNotEmpty(outputDVO)) {
			SharedThreadMemory.create();
			
			SharedThreadMemory.put(BizConst.KEY_USER_ID, outputDVO.getUserId());
//			SharedThreadMemory.put(BizConst.KEY_USER_NM, outputDVO.getUserNm());
			
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
			
			SharedThreadMemory.delete();
		}
		
		return outputDVO;
	}
}
