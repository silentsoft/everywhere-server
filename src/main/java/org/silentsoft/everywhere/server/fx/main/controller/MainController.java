package org.silentsoft.everywhere.server.fx.main.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.fx.main.vo.Cloud002DVO;
import org.silentsoft.everywhere.context.fx.main.vo.MainSVO;
import org.silentsoft.everywhere.context.fx.main.vo.Notice002DVO;
import org.silentsoft.everywhere.context.model.pojo.FilePOJO;
import org.silentsoft.everywhere.context.model.table.TbpEwCloudDVO;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.fx.main.service.MainService;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value="/notice", method=RequestMethod.POST)
	@ResponseBody
	public MainSVO getNotices(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		MainSVO mainSVO = null;
		
		try {
			mainSVO = JSONUtil.JSONToObject(json, MainSVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		Map inputMap = ObjectUtil.toMap(mainSVO.getNotice001DVO());
		List<Notice002DVO> notice002DVOList = mainService.getNotices(inputMap);
		
		mainSVO.setNotice002DVOList(notice002DVOList);
		
		return mainSVO;
	}
	
	@RequestMapping(value="/cloud", method=RequestMethod.POST)
	@ResponseBody
	public MainSVO getClouds(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		MainSVO mainSVO = null;
		
		try {
			mainSVO = JSONUtil.JSONToObject(json, MainSVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		Map inputMap = ObjectUtil.toMap(mainSVO.getCloud001DVO());
		List<Cloud002DVO> cloud002DVOList = mainService.getClouds(inputMap);
		
		mainSVO.setCloud002DVOList(cloud002DVOList);
		
		return mainSVO;
		
	}
	
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
		
		if (filePOJO.isDirectory()) {
			
		} else {
			String cloudRoot = SysUtil.getProperty(PropertyKey.CACHE_CLOUD_ROOT) + filePOJO.getUserUniqueSeq();
			File cloudRootDirectory = new File(cloudRoot);
			
			for (MultipartFile multipartFile : files) {
				if (!cloudRootDirectory.exists()) {
					cloudRootDirectory.mkdirs();
				}
				
				String filePath = cloudRoot + File.separator + filePOJO.getPath();
				File destination = new File(filePath);
				
				if (!destination.getParentFile().exists()) {
					destination.getParentFile().mkdirs();
				}
	
				if (destination.exists()) {
					destination.delete();
				}
				
				multipartFile.transferTo(destination);
				
				break;
			}
			
			LOGGER.debug("Successfully create a file !");
		}
		
		TbpEwCloudDVO inputDVO = new TbpEwCloudDVO();
		inputDVO.setUserId(SysUtil.getUserId());
		inputDVO.setDirectoryYn((filePOJO.isDirectory() == true ? "Y" : "N"));
		if (ObjectUtil.isEmpty(filePOJO.getExtension())) {
			inputDVO.setFileName(filePOJO.getName());
		} else {
			inputDVO.setFileName(filePOJO.getName() + CommonConst.DOT + filePOJO.getExtension());
		}
		String filePath = File.separator.concat(filePOJO.getPath());
		filePath = filePath.substring(0, filePath.length()-inputDVO.getFileName().length());
		filePath = (filePath.length() > 1 && filePath.endsWith(File.separator)) ? filePath.substring(0, filePath.length()-1) : filePath;
		inputDVO.setFilePath(filePath);
		inputDVO.setFileSize(filePOJO.getSize());
		
		mainService.saveCloud(inputDVO);
	}

}
