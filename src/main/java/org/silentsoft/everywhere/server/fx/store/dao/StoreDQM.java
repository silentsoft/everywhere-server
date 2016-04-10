package org.silentsoft.everywhere.server.fx.store.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.model.table.TbpSysFileStoreDVO;
import org.silentsoft.everywhere.server.core.AbstractDAO;
import org.springframework.jdbc.core.RowMapper;

public class StoreDQM extends AbstractDAO {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getByStoreGroup(final Map inputMap) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT STORE_GROUP, \n");
		sql.append("       STORE_TAG, \n");
		sql.append("       FILE_PATH, \n");
		sql.append("       ENCRYPTED_NAME, \n");
		sql.append("       FILE_NAME, \n");
		sql.append("       FILE_LENGTH, \n");
		sql.append("       FILE_SIZE, \n");
		sql.append("       FST_REG_DT, \n");
		sql.append("       FST_REGER_ID, \n");
		sql.append("       FNL_UPD_DT, \n");
		sql.append("       FNL_UPDER_ID, \n");
		sql.append("       DEL_YN, \n");
		sql.append("       USE_YN \n");
		sql.append("  FROM TBP_SYS_FILE_STORE \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND STORE_GROUP = :storeGroup \n");
		sql.append("   AND DEL_YN = 'N' \n");
		sql.append("   AND USE_YN = 'Y' \n");
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (isValidKey(inputMap, "storeGroup")) {
				data.put("storeGroup", inputMap.get("storeGroup"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				TbpSysFileStoreDVO returnDVO = new TbpSysFileStoreDVO();
				returnDVO.setStoreGroup(resultSet.getString("STORE_GROUP"));
				returnDVO.setStoreTag(resultSet.getString("STORE_TAG"));
				returnDVO.setFilePath(resultSet.getString("FILE_PATH"));
				returnDVO.setEncryptedName(resultSet.getString("ENCRYPTED_NAME"));
				returnDVO.setFileName(resultSet.getString("FILE_NAME"));
				returnDVO.setFileLength(resultSet.getBigDecimal("FILE_LENGTH"));
				returnDVO.setFileSize(resultSet.getString("FILE_SIZE"));
				returnDVO.setFstRegDt(resultSet.getString("FST_REG_DT"));
				returnDVO.setFstRegerId(resultSet.getString("FST_REGER_ID"));
				returnDVO.setFnlUpdDt(resultSet.getString("FNL_UPD_DT"));
				returnDVO.setFnlUpderId(resultSet.getString("FNL_UPDER_ID"));
				returnDVO.setDelYn(resultSet.getString("DEL_YN"));
				returnDVO.setUseYn(resultSet.getString("USE_YN"));
				
				return returnDVO;
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getByStoreTag(Map inputMap) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT STORE_GROUP, \n");
		sql.append("       STORE_TAG, \n");
		sql.append("       FILE_PATH, \n");
		sql.append("       ENCRYPTED_NAME, \n");
		sql.append("       FILE_NAME, \n");
		sql.append("       FILE_LENGTH, \n");
		sql.append("       FILE_SIZE, \n");
		sql.append("       FST_REG_DT, \n");
		sql.append("       FST_REGER_ID, \n");
		sql.append("       FNL_UPD_DT, \n");
		sql.append("       FNL_UPDER_ID, \n");
		sql.append("       DEL_YN, \n");
		sql.append("       USE_YN \n");
		sql.append("  FROM TBP_SYS_FILE_STORE \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND STORE_TAG = :storeTag \n");
		sql.append("   AND DEL_YN = 'N' \n");
		sql.append("   AND USE_YN = 'Y' \n");
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (isValidKey(inputMap, "storeTag")) {
				data.put("storeTag", inputMap.get("storeTag"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				TbpSysFileStoreDVO returnDVO = new TbpSysFileStoreDVO();
				returnDVO.setStoreGroup(resultSet.getString("STORE_GROUP"));
				returnDVO.setStoreTag(resultSet.getString("STORE_TAG"));
				returnDVO.setFilePath(resultSet.getString("FILE_PATH"));
				returnDVO.setEncryptedName(resultSet.getString("ENCRYPTED_NAME"));
				returnDVO.setFileName(resultSet.getString("FILE_NAME"));
				returnDVO.setFileLength(resultSet.getBigDecimal("FILE_LENGTH"));
				returnDVO.setFileSize(resultSet.getString("FILE_SIZE"));
				returnDVO.setFstRegDt(resultSet.getString("FST_REG_DT"));
				returnDVO.setFstRegerId(resultSet.getString("FST_REGER_ID"));
				returnDVO.setFnlUpdDt(resultSet.getString("FNL_UPD_DT"));
				returnDVO.setFnlUpderId(resultSet.getString("FNL_UPDER_ID"));
				returnDVO.setDelYn(resultSet.getString("DEL_YN"));
				returnDVO.setUseYn(resultSet.getString("USE_YN"));
				
				return returnDVO;
			}
		});
	}
}
