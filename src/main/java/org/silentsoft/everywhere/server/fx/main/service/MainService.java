package org.silentsoft.everywhere.server.fx.main.service;

import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.fx.main.vo.Notice002DVO;
import org.silentsoft.everywhere.context.model.table.TbpEwCloudDVO;
import org.silentsoft.everywhere.server.fx.main.biz.MainBiz;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainService.class);
	
	private MainBiz mainBiz;
	
	private MainBiz getMainBiz() {
		if (mainBiz == null) {
			mainBiz = BeanUtil.getBean(MainBiz.class);
		}
		
		return mainBiz;
	}
	
	public List<Notice002DVO> getNotices(Map<String, Object> inputMap) {
		return getMainBiz().getNotices(inputMap);
	}
	
	public int insertCloudInfo(TbpEwCloudDVO inputDVO) throws Exception {
		return getMainBiz().insertCloudInfo(inputDVO);
	}
}
