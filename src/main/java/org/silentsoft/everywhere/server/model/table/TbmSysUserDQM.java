package org.silentsoft.everywhere.server.model.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.model.table.TbmSysUserDVO;
import org.silentsoft.everywhere.server.core.AbstractDAO;
import org.springframework.jdbc.core.RowMapper;

public class TbmSysUserDQM extends AbstractDAO {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getUserById(final Map inputMap) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT USER_SEQ, \n");
		sql.append("       USER_ID, \n");
		sql.append("       USER_PWD, \n");
		sql.append("	   USER_NAME, \n");
		sql.append("	   EMAIL_ADDR, \n");
		sql.append("	   STORE_ROOT, \n");
		sql.append("	   FNL_ACCS_DT, \n");
		sql.append("	   FNL_ACCS_IP, \n");
		sql.append("	   ACCT_TYPE_CODE, \n");
		sql.append("	   ACCT_STATUS_CODE, \n");
		sql.append("	   ACCT_STATUS_UPD_DT, \n");
		sql.append("	   LANG_CODE, \n");
		sql.append("	   PWD_ERR_CNT, \n");
		sql.append("	   PWD_VALID_DT, \n");
		sql.append("	   DEL_YN, \n");
		sql.append("	   USE_YN \n");
		sql.append("  FROM TBM_SYS_USER \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND USER_ID = :userId \n");
		sql.append("   AND DEL_YN  = 'N' \n");
		sql.append("   AND USE_YN  = 'Y' \n");

		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (isValidKey(inputMap, "userId")) {
				data.put("userId", inputMap.get("userId"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				TbmSysUserDVO returnDVO = new TbmSysUserDVO();
				returnDVO.setUserSeq(resultSet.getString("USER_SEQ"));
				returnDVO.setUserId(resultSet.getString("USER_ID"));
				returnDVO.setUserPwd(resultSet.getString("USER_PWD"));
				returnDVO.setUserName(resultSet.getString("USER_NAME"));
				returnDVO.setEmailAddr(resultSet.getString("EMAIL_ADDR"));
				returnDVO.setStoreRoot(resultSet.getString("STORE_ROOT"));
				returnDVO.setFnlAccsDt(resultSet.getString("FNL_ACCS_DT"));
				returnDVO.setFnlAccsIp(resultSet.getString("FNL_ACCS_IP"));
				returnDVO.setAcctTypeCode(resultSet.getString("ACCT_TYPE_CODE"));
				returnDVO.setAcctStatusCode(resultSet.getString("ACCT_STATUS_CODE"));
				returnDVO.setAcctStatusUpdDt(resultSet.getString("ACCT_STATUS_UPD_DT"));
				returnDVO.setLangCode(resultSet.getString("LANG_CODE"));
				returnDVO.setPwdErrCnt(resultSet.getBigDecimal("PWD_ERR_CNT"));
				returnDVO.setPwdValidDt(resultSet.getString("PWD_VALID_DT"));
				returnDVO.setDelYn(resultSet.getString("DEL_YN"));
				returnDVO.setUseYn(resultSet.getString("USE_YN"));
				
				return returnDVO;
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getUserByEmail(final Map inputMap) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT USER_SEQ, \n");
		sql.append("       USER_ID, \n");
		sql.append("       USER_PWD, \n");
		sql.append("	   USER_NAME, \n");
		sql.append("	   EMAIL_ADDR, \n");
		sql.append("	   STORE_ROOT, \n");
		sql.append("	   FNL_ACCS_DT, \n");
		sql.append("	   FNL_ACCS_IP, \n");
		sql.append("	   ACCT_TYPE_CODE, \n");
		sql.append("	   ACCT_STATUS_CODE, \n");
		sql.append("	   ACCT_STATUS_UPD_DT, \n");
		sql.append("	   LANG_CODE, \n");
		sql.append("	   PWD_ERR_CNT, \n");
		sql.append("	   PWD_VALID_DT, \n");
		sql.append("	   DEL_YN, \n");
		sql.append("	   USE_YN \n");
		sql.append("  FROM TBM_SYS_USER \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND EMAIL_ADDR = :emailAddr \n");
		sql.append("   AND DEL_YN  = 'N' \n");
		sql.append("   AND USE_YN  = 'Y' \n");

		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (isValidKey(inputMap, "emailAddr")) {
				data.put("emailAddr", inputMap.get("emailAddr"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				TbmSysUserDVO returnDVO = new TbmSysUserDVO();
				returnDVO.setUserSeq(resultSet.getString("USER_SEQ"));
				returnDVO.setUserId(resultSet.getString("USER_ID"));
				returnDVO.setUserPwd(resultSet.getString("USER_PWD"));
				returnDVO.setUserName(resultSet.getString("USER_NAME"));
				returnDVO.setEmailAddr(resultSet.getString("EMAIL_ADDR"));
				returnDVO.setStoreRoot(resultSet.getString("STORE_ROOT"));
				returnDVO.setFnlAccsDt(resultSet.getString("FNL_ACCS_DT"));
				returnDVO.setFnlAccsIp(resultSet.getString("FNL_ACCS_IP"));
				returnDVO.setAcctTypeCode(resultSet.getString("ACCT_TYPE_CODE"));
				returnDVO.setAcctStatusCode(resultSet.getString("ACCT_STATUS_CODE"));
				returnDVO.setAcctStatusUpdDt(resultSet.getString("ACCT_STATUS_UPD_DT"));
				returnDVO.setLangCode(resultSet.getString("LANG_CODE"));
				returnDVO.setPwdErrCnt(resultSet.getBigDecimal("PWD_ERR_CNT"));
				returnDVO.setPwdValidDt(resultSet.getString("PWD_VALID_DT"));
				returnDVO.setDelYn(resultSet.getString("DEL_YN"));
				returnDVO.setUseYn(resultSet.getString("USE_YN"));
				
				return returnDVO;
			}
		});
	}
}
