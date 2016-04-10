package org.silentsoft.everywhere.server.fx.store.controller;

import javax.servlet.annotation.MultipartConfig;

import org.silentsoft.core.item.StoreItem;
import org.silentsoft.core.util.JSONUtil;
import org.silentsoft.everywhere.server.fx.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/fx/sm/store")
@MultipartConfig
public class StoreController {
	@Autowired
	private StoreService storeService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public StoreItem upload(@RequestParam("json") String json, @RequestParam("binary") MultipartFile[] multipartFiles) throws Exception {
		return storeService.upload(JSONUtil.JSONToObject(json, StoreItem.class), multipartFiles);
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	@ResponseBody
	public StoreItem download(@RequestParam("json") String json) throws Exception {
		return storeService.download(JSONUtil.JSONToObject(json, StoreItem.class));
	}
}
