package org.silentsoft.everywhere.server.fx.cloud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.core.util.SystemUtil;
import org.silentsoft.core.util.ZipUtil;
import org.silentsoft.everywhere.context.fx.cloud.vo.CloudSVO;
//import org.silentsoft.everywhere.context.model.pojo.FilePOJO;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.fx.cloud.service.CloudService;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/fx/cloud")
public class CloudController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudController.class);
	
	@Autowired
	private CloudService cloudService;
	
	@RequestMapping(value="/notice", method=RequestMethod.POST)
	@ResponseBody
	public CloudSVO getNotices(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		CloudSVO cloudSVO = null;
		
		try {
			cloudSVO = JSONUtil.JSONToObject(json, CloudSVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		return cloudService.getNotices(cloudSVO);
	}
	
	@RequestMapping(value="/cloudDirectory", method=RequestMethod.POST)
	@ResponseBody
	public CloudSVO getCloudDirectory(@RequestBody String json) throws Exception {
		LOGGER.debug("i got json string.. <{}>", new Object[]{json});
		
		CloudSVO cloudSVO = null;
		
		try {
			cloudSVO = JSONUtil.JSONToObject(json, CloudSVO.class);
		} catch (Exception e) {
			LOGGER.error("Failed parse json to object !", new Object[]{e});
		}
		
		return cloudService.getCloudDirectory(cloudSVO);
	}
	
//	@RequestMapping(value="/upload", method=RequestMethod.POST)
//	@ResponseBody
//	public void upload(@RequestParam("json") String json, @RequestParam("binary") MultipartFile[] files) throws Exception {
//		//LOGGER.debug("i got json string.. <{}>", new Object[]{json});
//		FilePOJO filePOJO = null;
//		
//		try {
//			filePOJO = JSONUtil.JSONToObject(json, FilePOJO.class);
//		} catch (Exception e) {
//			LOGGER.error("Failed parse json to object !", new Object[]{e});
//		}
//		
//		String cloudRoot = SysUtil.getProperty(PropertyKey.CACHE_CLOUD_ROOT) + SysUtil.getUserUniqueSeq();
//		String filePath = cloudRoot + File.separator + filePOJO.getPath();
//		File destination = new File(filePath);
//		
//		if (filePOJO.isDirectory()) {
//			if (destination.exists()) {
//				destination.delete();
//			}
//			
//			destination.mkdirs();
//		} else {
//			for (MultipartFile multipartFile : files) {
//				if (!destination.getParentFile().exists()) {
//					destination.getParentFile().mkdirs();
//				}
//
//				if (destination.exists()) {
//					destination.delete();
//				}
//				
//				multipartFile.transferTo(destination);
//				
//				break;
//			}
//		}
//		
//		LOGGER.debug("Successfully create a file !");
//		
//		mainService.saveCloud(filePOJO);
//	}
//	
//	@RequestMapping(value="/download", method=RequestMethod.POST)
//	@ResponseBody
//	public FilePOJO download(@RequestBody String json) throws Exception {
//		//LOGGER.debug("i got json string.. <{}>", new Object[]{json});
//		FilePOJO filePOJO = null;
//		
//		try {
//			filePOJO = JSONUtil.JSONToObject(json, FilePOJO.class);
//		} catch (Exception e) {
//			LOGGER.error("Failed parse json to object !", new Object[]{e});
//		}
//		
//		String cloudRoot = SysUtil.getProperty(PropertyKey.CACHE_CLOUD_ROOT) + SysUtil.getUserUniqueSeq();
//		String filePath = cloudRoot + filePOJO.getPath();
//		filePath = (filePOJO.getPath().equals(File.separator) ? filePath : filePath.concat(File.separator));
//		String zipFilePath = filePath.concat("~".concat(filePOJO.getName()).concat(".zip"));
//		filePath = filePath.concat(filePOJO.getName());
//		
//		new ZipUtil().doZip(filePath, zipFilePath, 3);
//		File downloadZipFile = new File(zipFilePath);
//		
//		FileInputStream fileInputStream = new FileInputStream(downloadZipFile);
//		filePOJO.setBytes(IOUtils.toByteArray(fileInputStream));
//		fileInputStream.close();
//
//		downloadZipFile.delete();
//		
//		return filePOJO;
//	}

}
