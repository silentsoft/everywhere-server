package org.silentsoft.everywhere.server.model.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.model.table.TbmSmUserDVO;
import org.silentsoft.everywhere.server.core.AbstractDAO;
import org.springframework.jdbc.core.RowMapper;

public class TbmSmUserDQM extends AbstractDAO {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTbmSmUser(final Map inputMap) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT USER_ID, \n");
		sql.append("       USER_PWD, \n");
		sql.append("	   USER_NM, \n");
		sql.append("	   ENG_USER_NM, \n");
		sql.append("	   EMP_NO, \n");
		sql.append("	   EMAIL_ADDR, \n");
		sql.append("	   SINGLE_ID, \n");
		sql.append("	   DEPT_CODE, \n");
		sql.append("	   DEPT_NM, \n");
		sql.append("	   ENG_DEPT_NM, \n");
		sql.append("	   MOBILE_TEL, \n");
		sql.append("	   UNIQUE_SEQ, \n");
		sql.append("	   DEL_YN, \n");
		sql.append("	   USE_YN \n");
		sql.append("  FROM TBM_SM_USER \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND SINGLE_ID = :singleId \n");
		sql.append("   AND DEL_YN  = 'N' \n");
		sql.append("   AND USE_YN  = 'Y' \n");

		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (validKey(inputMap, "singleId")) {
				data.put("singleId", inputMap.get("singleId"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				
				TbmSmUserDVO returnDVO = new TbmSmUserDVO();
				returnDVO.setUserId(resultSet.getString("USER_ID"));
				returnDVO.setUserPwd(resultSet.getString("USER_PWD"));
				returnDVO.setUserNm(resultSet.getString("USER_NM"));
				returnDVO.setEngUserNm(resultSet.getString("ENG_USER_NM"));
				returnDVO.setEmpNo(resultSet.getString("EMP_NO"));
				returnDVO.setEmailAddr(resultSet.getString("EMAIL_ADDR"));
				returnDVO.setSingleId(resultSet.getString("SINGLE_ID"));
				returnDVO.setDeptCode(resultSet.getString("DEPT_CODE"));
				returnDVO.setDeptNm(resultSet.getString("DEPT_NM"));
				returnDVO.setMobileTel(resultSet.getString("MOBILE_TEL"));
				returnDVO.setUniqueSeq(resultSet.getString("UNIQUE_SEQ"));
				returnDVO.setDelYn(resultSet.getString("DEL_YN"));
				returnDVO.setUseYn(resultSet.getString("USE_YN"));
				
				return returnDVO;
			}
		});
	}
}
