package org.silentsoft.everywhere.server.fx.main.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.fx.main.vo.CloudDirectoryOutDVO;
import org.silentsoft.everywhere.context.fx.main.vo.MainSVO;
import org.silentsoft.everywhere.context.fx.main.vo.NoticeOutDVO;
import org.silentsoft.everywhere.context.model.pojo.FilePOJO;
import org.silentsoft.everywhere.context.model.table.TbpEwCloudDVO;
import org.silentsoft.everywhere.server.fx.main.dao.MainDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainService.class);
	
	private MainDQM mainDQM;
	
	private MainDQM getMainDQM() {
		if (mainDQM == null) {
			mainDQM = BeanUtil.getBean(MainDQM.class);
		}
		
		return mainDQM;
	}
	
	public MainSVO getNotices(MainSVO mainSVO) throws Exception {
		Map inputMap = ObjectUtil.toMap(mainSVO.getNoticeInDVO());
		List<NoticeOutDVO> noticeOutDVOList = getMainDQM().getNotices(inputMap);
		
		mainSVO.setNoticeOutDVOList(noticeOutDVOList);
		
		return mainSVO;
	}
	
	public MainSVO getCloudDirectory(MainSVO mainSVO) throws Exception {
		mainSVO.getCloudDirectoryInDVO().setUserId(SysUtil.getUserId());
		
		Map inputMap = ObjectUtil.toMap(mainSVO.getCloudDirectoryInDVO());
		List<CloudDirectoryOutDVO> cloudDirectoryOutDVOList = getMainDQM().getCloudDirectory(inputMap);
		
		mainSVO.setCloudDirectoryOutDVOList(cloudDirectoryOutDVOList);
		
		return mainSVO;
	}
	
	public void saveCloud(FilePOJO filePOJO) throws Exception {
		
		TbpEwCloudDVO tbpEwCloudDVO = new TbpEwCloudDVO();
		tbpEwCloudDVO.setUserId(SysUtil.getUserId());
		tbpEwCloudDVO.setDirectoryYn((filePOJO.isDirectory() == true ? "Y" : "N"));
		if (ObjectUtil.isEmpty(filePOJO.getExtension())) {
			tbpEwCloudDVO.setFileName(filePOJO.getName());
		} else {
			tbpEwCloudDVO.setFileName(filePOJO.getName() + CommonConst.DOT + filePOJO.getExtension());
		}
		String filePath = filePOJO.getPath().startsWith(File.separator) ? filePOJO.getPath() : File.separator.concat(filePOJO.getPath());
		filePath = filePath.substring(0, filePath.length()-tbpEwCloudDVO.getFileName().length());
		filePath = (filePath.length() > 1 && filePath.endsWith(File.separator)) ? filePath.substring(0, filePath.length()-1) : filePath;
		tbpEwCloudDVO.setFilePath(filePath);
		tbpEwCloudDVO.setFileSize(filePOJO.getSize());
		
		CrudUtil.save(tbpEwCloudDVO);
	}
	
}
