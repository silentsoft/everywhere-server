package org.silentsoft.everywhere.server.fx.main.biz;

import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.fx.main.vo.Cloud002DVO;
import org.silentsoft.everywhere.context.fx.main.vo.Notice002DVO;
import org.silentsoft.everywhere.context.model.table.TbpEwCloudDVO;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainBiz {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainBiz.class);
	
	private MainDQM mainDQM;
	
	private MainDQM getMainDQM() {
		if (mainDQM == null) {
			mainDQM = BeanUtil.getBean(MainDQM.class);
		}
		
		return mainDQM;
	}
	
	@SuppressWarnings("unchecked")
	public List<Notice002DVO> getNotices(Map<String, Object> inputMap) {
		return getMainDQM().getNotices(inputMap);
	}
	
	public List<Cloud002DVO> getClouds(Map<String, Object> inputMap) {
		return getMainDQM().getClouds(inputMap);
	}
	
	public int saveCloud(TbpEwCloudDVO tbpEwCloudDVO) throws Exception {
		return CrudUtil.save(tbpEwCloudDVO);
	}
}
