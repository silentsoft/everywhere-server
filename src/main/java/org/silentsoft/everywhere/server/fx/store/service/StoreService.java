package org.silentsoft.everywhere.server.fx.store.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.silentsoft.core.util.FileUtil;
import org.silentsoft.core.util.GenerateUtil;
import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.model.table.TbpSysFileStoreDVO;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.fx.store.dao.StoreDQM;
import org.silentsoft.everywhere.server.util.BeanUtil;
import org.silentsoft.everywhere.server.util.CrudUtil;
import org.silentsoft.everywhere.server.util.SysUtil;
import org.silentsoft.net.item.StoreItem;
import org.silentsoft.net.pojo.FilePOJO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoreService {

	private StoreDQM storeDQM;

	private StoreDQM getStoreDQM() {
		if (storeDQM == null) {
			storeDQM = BeanUtil.getBean(StoreDQM.class);
		}

		return storeDQM;
	}

	public StoreItem upload(StoreItem storeItem, MultipartFile[] multipartFiles)
			throws Exception {
		if (ObjectUtil.isEmpty(storeItem.getTag())) {
			storeItem.setTag(GenerateUtil.generateUUID());
		} else {
			// TODO : store group exist check logic
		}

		String storePath = SysUtil.getProperty(PropertyKey.CACHE_FILE_STORE_PATH);

		File destination = null;
		try {
			for (int i = 0, j = storeItem.size(); i < j; i++) {
				FilePOJO filePOJO = storeItem.get(i);
				filePOJO.setTag(GenerateUtil.generateUUID());

				String filePath = ((storeItem.getSender() == null || storeItem.getSender().length() <= 0) ? "unknown" : storeItem.getSender()).concat(File.separator);
				String encryptedName = GenerateUtil.generateUUID();

				destination = new File(storePath.concat(filePath).concat(encryptedName));
				if (destination.getParentFile().exists() == false) {
					destination.getParentFile().mkdirs();
				}

				if (destination.exists()) {
					destination.delete();
				}

				multipartFiles[i].transferTo(destination);

				TbpSysFileStoreDVO inputDVO = new TbpSysFileStoreDVO();
				inputDVO.setStoreGroup(storeItem.getTag());
				inputDVO.setStoreTag(filePOJO.getTag());
				inputDVO.setFilePath(filePath);
				inputDVO.setEncryptedName(encryptedName);
				inputDVO.setFileName(filePOJO.getNameWithExtension());
				inputDVO.setFileLength(new BigDecimal(filePOJO.getLength()));
				inputDVO.setFileSize(filePOJO.getSize());

				CrudUtil.save(inputDVO);
			}
		} catch (Exception e) {
			if (destination != null) {
				destination.delete();
			}
			
			throw e;
		}

		return storeItem;
	}

	@SuppressWarnings("unchecked")
	public StoreItem download(StoreItem storeItem) throws Exception {
		String storeGroup = storeItem.getTag();
		if (storeGroup != null && storeGroup.length() > 0) {
			// group download
			storeItem.clear();

			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("storeGroup", storeGroup);

			List<TbpSysFileStoreDVO> fileStoreDVOList = getStoreDQM().getByStoreGroup(inputMap);
			for (TbpSysFileStoreDVO fileStoreDVO : fileStoreDVOList) {
				FilePOJO filePOJO = new FilePOJO();
				setFilePOJOFromFileStoreDVO(filePOJO, fileStoreDVO);
				storeItem.add(filePOJO);
			}
		} else {
			// each download
			for (FilePOJO filePOJO : storeItem) {
				String storeTag = filePOJO.getTag();
				if (storeTag != null && storeTag.length() > 0) {
					Map<String, Object> inputMap = new HashMap<String, Object>();
					inputMap.put("storeTag", storeTag);

					List<TbpSysFileStoreDVO> fileStoreDVOList = getStoreDQM().getByStoreTag(inputMap);
					for (TbpSysFileStoreDVO fileStoreDVO : fileStoreDVOList) {
						setFilePOJOFromFileStoreDVO(filePOJO, fileStoreDVO);
						break;
					}
				}
			}
		}

		return storeItem;
	}

	private void setFilePOJOFromFileStoreDVO(FilePOJO filePOJO, TbpSysFileStoreDVO fileStoreDVO) throws Exception {
		String storePath = SysUtil.getProperty(PropertyKey.CACHE_FILE_STORE_PATH);

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(storePath.concat(fileStoreDVO.getFilePath()).concat(fileStoreDVO.getEncryptedName()));

			filePOJO.setName(FileUtil.getName(fileStoreDVO.getFileName()));
			filePOJO.setExtension(FileUtil.getExtension(fileStoreDVO.getFileName()));
			filePOJO.setLength(fileStoreDVO.getFileLength().longValue());
			filePOJO.setBytes(IOUtils.toByteArray(inputStream));
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
}
