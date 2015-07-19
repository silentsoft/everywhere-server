package org.silentsoft.everywhere.server.fx.main.biz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silentsoft.everywhere.context.fx.main.vo.Notice002DVO;
import org.silentsoft.everywhere.server.core.AbstractDAO;
import org.springframework.jdbc.core.RowMapper;

public class MainDQM extends AbstractDAO {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getNotices(final Map inputMap) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT NOTICE_DT, \n");
		sql.append("       LANG_CODE, \n");
		sql.append("       TITLE, \n");
		sql.append("       CONT, \n");
		sql.append("       FST_REG_DT, \n");
		sql.append("       FST_REGER_ID, \n");
		sql.append("       FNL_UPD_DT, \n");
		sql.append("       FNL_UPDER_ID \n");
		sql.append("  FROM TBD_EW_NOTICE \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND LANG_CODE = :langCode \n");
		sql.append("   AND DEL_YN = 'N' \n");
		sql.append("   AND USE_YN = 'Y' \n");
		sql.append(" UNION ALL \n");
		sql.append("SELECT NOTICE_DT, \n");
		sql.append("       LANG_CODE, \n");
		sql.append("       TITLE, \n");
		sql.append("       CONT, \n");
		sql.append("       FST_REG_DT, \n");
		sql.append("       FST_REGER_ID, \n");
		sql.append("       FNL_UPD_DT, \n");
		sql.append("       FNL_UPDER_ID \n");
		sql.append("  FROM TBD_EW_NOTICE \n");
		sql.append(" WHERE 1=1 \n");
		sql.append("   AND LANG_CODE = 'en' \n");
		sql.append("   AND DEL_YN = 'N' \n");
		sql.append("   AND USE_YN = 'Y' \n");
		sql.append("   AND NOT EXISTS \n");
		sql.append("       ( \n");
		sql.append("        SELECT NOTICE_DT, \n");
		sql.append("               LANG_CODE, \n");
		sql.append("               TITLE, \n");
		sql.append("               CONT, \n");
		sql.append("               FST_REG_DT, \n");
		sql.append("               FST_REGER_ID, \n");
		sql.append("               FNL_UPD_DT, \n");
		sql.append("               FNL_UPDER_ID \n");
		sql.append("          FROM TBD_EW_NOTICE \n");
		sql.append("         WHERE 1=1 \n");
		sql.append("           AND LANG_CODE = :langCode \n");
		sql.append("           AND DEL_YN = 'N' \n");
		sql.append("           AND USE_YN = 'Y' \n");
		sql.append("       ) \n");
		sql.append(" ORDER BY NOTICE_DT DESC \n");
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (countMap(inputMap) > 0) {
			if (validKey(inputMap, "langCode")) {
				data.put("langCode", inputMap.get("langCode"));
			}
		}
		
		return query(sql.toString(), data, new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				Notice002DVO returnDVO = new Notice002DVO();
				returnDVO.setNoticeDt(resultSet.getString("NOTICE_DT"));
				returnDVO.setLangCode(resultSet.getString("LANG_CODE"));
				returnDVO.setTitle(resultSet.getString("TITLE"));
				returnDVO.setCont(resultSet.getString("CONT"));
				returnDVO.setFstRegDt(resultSet.getString("FST_REG_DT"));
				returnDVO.setFstRegerId(resultSet.getString("FST_REGER_ID"));
				returnDVO.setFnlUpdDt(resultSet.getString("FNL_UPD_DT"));
				returnDVO.setFnlUpderId(resultSet.getString("FNL_UPDER_ID"));
				
				return returnDVO;
			}
		});
	}
}