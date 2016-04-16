package org.silentsoft.everywhere.server.fx.cloud.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.fx.cloud.vo.CloudDirectoryOutDVO;
import org.silentsoft.everywhere.context.fx.cloud.vo.CloudSVO;
import org.silentsoft.everywhere.context.fx.cloud.vo.NoticeOutDVO;
//import org.silentsoft.everywhere.context.model.pojo.FilePOJO;
import org.silentsoft.everywhere.context.model.table.TbpEwCloudDVO;
import org.silentsoft.everywhere.server.fx.cloud.dao.CloudDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CloudService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudService.class);
	
	private CloudDQM cloudDQM;
	
	private CloudDQM getCloudDQM() {
		if (cloudDQM == null) {
			cloudDQM = BeanUtil.getBean(CloudDQM.class);
		}
		
		return cloudDQM;
	}
	
	public CloudSVO getNotices(CloudSVO cloudSVO) throws Exception {
		Map inputMap = ObjectUtil.toMap(cloudSVO.getNoticeInDVO());
		List<NoticeOutDVO> noticeOutDVOList = getCloudDQM().getNotices(inputMap);
		
		cloudSVO.setNoticeOutDVOList(noticeOutDVOList);
		
		return cloudSVO;
	}
	
	public CloudSVO getCloudDirectory(CloudSVO cloudSVO) throws Exception {
		cloudSVO.getCloudDirectoryInDVO().setUserId(SysUtil.getUserId());
		
		Map inputMap = ObjectUtil.toMap(cloudSVO.getCloudDirectoryInDVO());
		List<CloudDirectoryOutDVO> cloudDirectoryOutDVOList = getCloudDQM().getCloudDirectory(inputMap);
		
		cloudSVO.setCloudDirectoryOutDVOList(cloudDirectoryOutDVOList);
		
		return cloudSVO;
	}
	
//	public void saveCloud(FilePOJO filePOJO) throws Exception {
//		
//		TbpEwCloudDVO tbpEwCloudDVO = new TbpEwCloudDVO();
//		tbpEwCloudDVO.setUserId(SysUtil.getUserId());
//		tbpEwCloudDVO.setDirectoryYn((filePOJO.isDirectory() == true ? "Y" : "N"));
//		if (ObjectUtil.isEmpty(filePOJO.getExtension())) {
//			tbpEwCloudDVO.setFileName(filePOJO.getName());
//		} else {
//			tbpEwCloudDVO.setFileName(filePOJO.getName() + CommonConst.DOT + filePOJO.getExtension());
//		}
//		String filePath = filePOJO.getPath().startsWith(File.separator) ? filePOJO.getPath() : File.separator.concat(filePOJO.getPath());
//		filePath = filePath.substring(0, filePath.length()-tbpEwCloudDVO.getFileName().length());
//		filePath = (filePath.length() > 1 && filePath.endsWith(File.separator)) ? filePath.substring(0, filePath.length()-1) : filePath;
//		tbpEwCloudDVO.setFilePath(filePath);
//		tbpEwCloudDVO.setFileSize(filePOJO.getSize());
//		
//		CrudUtil.save(tbpEwCloudDVO);
//	}
	
}
