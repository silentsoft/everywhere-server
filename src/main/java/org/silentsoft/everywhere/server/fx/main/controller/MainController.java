package org.silentsoft.everywhere.server.fx.main.controller;

import java.io.File;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.core.SharedThreadMemory;
import org.silentsoft.everywhere.context.model.pojo.FilePOJO;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.fx.main.service.MainService;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/fx/main")
public class MainController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	@ResponseBody
	public void upload(@RequestParam("json") String json, @RequestParam("binary") MultipartFile[] files) throws Exception {
		//LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		FilePOJO filePOJO = null;
		
		try {
			filePOJO = JSONUtil.JSONToObject(json, FilePOJO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		String cloudRoot = SysUtil.getProperty(PropertyKey.CACHE_CLOUD_ROOT);
		File cloudRootDirectory = new File(cloudRoot);
		
		for (MultipartFile multipartFile : files) {
			if (!cloudRootDirectory.exists()) {
				cloudRootDirectory.mkdirs();
			}
			
			String filePath = cloudRoot + filePOJO.getName() + CommonConst.DOT + filePOJO.getExtension();
			File destination = new File(filePath);

			if (destination.exists()) {
				destination.delete();
			}
			
			destination.createNewFile();
			
			multipartFile.transferTo(destination);
		}
		
		LOGGER.debug("Successfully create a file !");
	}

}
