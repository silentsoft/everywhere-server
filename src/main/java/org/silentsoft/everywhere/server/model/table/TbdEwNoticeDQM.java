package org.silentsoft.everywhere.server.model.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.model.table.TbdEwNoticeDVO;
import org.silentsoft.everywhere.server.core.AbstractDAO;
import org.springframework.jdbc.core.RowMapper;

public class TbdEwNoticeDQM extends AbstractDAO {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTbdEwNotice(final Map inputMap) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT NOTICE_DT, \n");
		sql.append("       LANG_CODE, \n");
		sql.append("       TITLE, \n");
		sql.append("       CONT, \n");
		sql.append("       FST_REG_DT, \n");
		sql.append("       FST_REGER_ID, \n");
		sql.append("       FNL_UPD_DT, \n");
		sql.append("       FNL_UPDER_ID, \n");
		sql.append("       DEL_YN, \n");
		sql.append("       USE_YN \n");
		sql.append("  FROM TBD_EW_NOTICE \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND DEL_YN = 'N' \n");
		sql.append("   AND USE_YN = 'Y' \n");
		sql.append(" ORDER BY NOTICE_DT DESC \n");
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				TbdEwNoticeDVO returnDVO = new TbdEwNoticeDVO();
				returnDVO.setNoticeDt(resultSet.getString("NOTICE_DT"));
				returnDVO.setLangCode(resultSet.getString("LANG_CODE"));
				returnDVO.setTitle(resultSet.getString("TITLE"));
				returnDVO.setCont(resultSet.getString("CONT"));
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
