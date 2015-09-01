package org.silentsoft.everywhere.server.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.server.PropertyKey;
import org.silentsoft.everywhere.server.core.MetaDAO;
import org.silentsoft.everywhere.server.core.type.DatabaseType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

public class CrudUtil {
	
	private static MetaDAO metaDao;
	
	private static MetaDAO getMetaDAO() throws Exception {
		if (metaDao == null) {
			metaDao = BeanUtil.getBean(MetaDAO.class);
		}
		
		return metaDao;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> int create(final T dvo) throws Exception {
		if (dvo == null) {
			return -1;
		}

		ObjectUtil.bindValue(dvo, "useYn", "Y");
		ObjectUtil.bindValue(dvo, "delYn", "N");

		String dbCurrentTime = getMetaDAO().getCurrentTime();
		ObjectUtil.bindValue(dvo, "fstRegDt", dbCurrentTime);
		ObjectUtil.bindValue(dvo, "fnlUpdDt", dbCurrentTime);
		
		String userId = SysUtil.getUserId();
		ObjectUtil.bindValue(dvo, "fstRegerId", userId);
		ObjectUtil.bindValue(dvo, "fnlUpderId", userId);

		Map<String, Object> map = ObjectUtil.toMap(dvo);
		
		String insertStatement = createInsertStatement(dvo);

		return getMetaDAO().update(insertStatement, map);
	}
	
	// TODO : must vertify this method.
	public static <T> int[] createBatch(final List<T> dvoList) throws Exception {
		if (dvoList == null || dvoList.size() < 1) {
			return new int[]{ -1 };
		}
		
		for (int i=0, j=dvoList.size(); i<j; i++) {
			T dvo = dvoList.get(i);
			
			ObjectUtil.bindValue(dvo, "useYn", "Y");
			ObjectUtil.bindValue(dvo, "delYn", "N");

			String dbCurrentTime = getMetaDAO().getCurrentTime();
			ObjectUtil.bindValue(dvo, "fstRegDt", dbCurrentTime);
			ObjectUtil.bindValue(dvo, "fnlUpdDt", dbCurrentTime);
			
			String userId = SysUtil.getUserId();
			ObjectUtil.bindValue(dvo, "fstRegerId", userId);
			ObjectUtil.bindValue(dvo, "fnlUpderId", userId);
			
			dvoList.set(i, dvo);
		}
		
		String insertStatement = createInsertStatement(dvoList.get(0));
		
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(dvoList.toArray());
		
		return getMetaDAO().batchUpdate(insertStatement, batch);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> int remove(final T dvo) throws Exception {
		Class<? extends Object> classType = dvo.getClass();
		
		// map객체가 비어있는 경우 종료
		if (ObjectUtil.isEmpty(dvo)) {
			throw new Exception("Parameter DVO cannot be empty !");
		}
		
		String tableName = getTableName(classType);

		// PK컬럼목록 반환.
		List<String> pkColumns = getMetaDAO().getPKColumns(tableName);

		Map<String, Object> map = ObjectUtil.toMap(dvo);

		// 진행처리가 가능한지 validation 체크
		if (checkPK(tableName, pkColumns, map)) {
			if (validateSchema(map, pkColumns)) {

				String deleteStatement = createDeleteStatement(tableName, pkColumns, map);
				
				return getMetaDAO().update(deleteStatement, map);
			}
		}
		
		return -1;
	}
	
	public static <T> int update(final T dvo) throws Exception {
		return update(dvo, true, true);
	}

	@SuppressWarnings("unchecked")
	private static <T> int update(final T dvo, boolean addUseYn, boolean addDelYn) throws Exception {
		Class<? extends Object> classType = dvo.getClass();
		
		// map객체가 비어있는 경우 종료
		if (ObjectUtil.isEmpty(dvo)) {
			throw new Exception("Parameter DVO cannot be empty !");
		}
		
		String tableName = getTableName(classType);

		// PK컬럼목록 반환.
		List<String> pkColumns = getMetaDAO().getPKColumns(tableName);

		// 진행처리가 가능한지 validation 체크
		if (checkPK(tableName, pkColumns, ObjectUtil.toMap(dvo))) {

			// DB에 저장된값
			T t = get(dvo);

			if (t != null) {
				// DB에 저장된값 기준으로 파라미터 입력DVO와 다른것은 UPDATE 대상으로 판단한다.
				T compare = objectMerge(dvo, t, pkColumns);
				
				// Data Map 반환.
				Map<String, Object> map = ObjectUtil.toMap(compare);

				if (validateSchema(map, pkColumns)) {
					String dbCurrentTime = getMetaDAO().getCurrentTime();
					ObjectUtil.bindMapValue(map, "fnlUpdDt", dbCurrentTime);

					String userId = SysUtil.getUserId();
					ObjectUtil.bindMapValue(map, "fnlUpderId", userId);
					
					String updateStatement = createUpdateStatement(tableName, map, pkColumns, addUseYn, addDelYn);
					
					return getMetaDAO().update(updateStatement, map);
				}

			}
		}
		
		return -1;
	}
	
	public static <T> int delete(final T dvo) throws Exception {
		return delete(dvo, false);
	}

	private static <T> int delete(final T dvo, boolean isPhysicalDelete) throws Exception {
		// 실제 삭제처리인 경우
		if (isPhysicalDelete) {
			return remove(dvo);
		} else {
			/* UPDATE DEL_YN , USE_YN 변경인경우 */
			ObjectUtil.bindValue(dvo, "useYn", "N");
			ObjectUtil.bindValue(dvo, "delYn", "Y");

			return update(dvo);
		}
	}
	
	public static <T> int save(final T dvo) throws Exception {
		return save(dvo, true, true);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> int save(final T dvo, boolean addUseYn, boolean addDelYn) throws Exception {
		Class<? extends Object> classType = dvo.getClass();
		
		// map객체가 비어있는 경우 종료
		if (ObjectUtil.isEmpty(dvo)) {
			throw new Exception("Parameter DVO cannot be empty !");
		}
		
		String tableName = getTableName(classType);

		// PK컬럼목록 반환.
		List<String> pkColumns = getMetaDAO().getPKColumns(tableName);

		// 진행처리가 가능한지 validation 체크
		if (checkPK(tableName, pkColumns, ObjectUtil.toMap(dvo))) {

			// DB에 저장된값
			T t = get(dvo);

			if (t == null) {
				// insert
				ObjectUtil.bindValue(dvo, "useYn", "Y");
				ObjectUtil.bindValue(dvo, "delYn", "N");
		
				String dbCurrentTime = getMetaDAO().getCurrentTime();
				ObjectUtil.bindValue(dvo, "fstRegDt", dbCurrentTime);
				ObjectUtil.bindValue(dvo, "fnlUpdDt", dbCurrentTime);
				
				String userId = SysUtil.getUserId();
				ObjectUtil.bindValue(dvo, "fstRegerId", userId);
				ObjectUtil.bindValue(dvo, "fnlUpderId", userId);
		
				Map<String, Object> map = ObjectUtil.toMap(dvo);
				
				String insertStatement = createInsertStatement(dvo);
		
				return getMetaDAO().update(insertStatement, map);
			} else {
				// update
				
				// DB에 저장된값 기준으로 파라미터 입력DVO와 다른것은 UPDATE 대상으로 판단한다.
				T compare = objectMerge(dvo, t, pkColumns);
				
				// Data Map 반환.
				Map<String, Object> map = ObjectUtil.toMap(compare);

				if (validateSchema(map, pkColumns)) {
					String dbCurrentTime = getMetaDAO().getCurrentTime();
					ObjectUtil.bindMapValue(map, "fnlUpdDt", dbCurrentTime);

					String userId = SysUtil.getUserId();
					ObjectUtil.bindMapValue(map, "fnlUpderId", userId);
					
					String updateStatement = createUpdateStatement(tableName, map, pkColumns, addUseYn, addDelYn);
					
					return getMetaDAO().update(updateStatement, map);
				}
			}
		}
		
		return -1;
	}
	
	private static String createSelectStatement(String tableName, List<String> pkColumns) throws Exception {
		if (ObjectUtil.isEmpty(tableName)) {
			throw new Exception("Table name cannot be empty !");
		}
		
		if (ObjectUtil.isEmpty(pkColumns)) {
			throw new Exception("PK columns cannot be empty !");
		}

		StringBuffer selectStatement = new StringBuffer();
		selectStatement.append("SELECT * FROM ").append(tableName).append(" \n");
		selectStatement.append("WHERE 1=1 \n");
		
		for (String where : pkColumns) {
			selectStatement.append("AND ").append(where).append(" = :").append(getPrefixLowerText(where)).append("\n");
		}

		return selectStatement.toString();
	}
	
	private static String createInsertStatement(final Object dvo) throws Exception {
		if (ObjectUtil.isEmpty(dvo)) {
			return "";
		}
		
		StringBuffer insertStatement = new StringBuffer();
		// 클래스명에 해당하는 테이블명이 존재하는 경우.
		String tableName = getTableName(dvo.getClass());

		if (ObjectUtil.isNotEmpty(tableName)) {
			List<String> columns = getMetaDAO().getColumns(tableName);

			if (ObjectUtil.isNotEmpty(columns)) {
				StringBuffer columnsBuffer = new StringBuffer();
				StringBuffer valuesBuffer = new StringBuffer();
				insertStatement.append("INSERT INTO ").append(tableName).append("\n");
				insertStatement.append("(\n");

				for (String column : columns) {
					Method declaredMethod = dvo.getClass().getDeclaredMethod("get".concat(getPrefixUpperText(column)));
					
					if (declaredMethod != null) {
						Object value = declaredMethod.invoke(dvo);
						
						if (ObjectUtil.isNotEmpty(value)) {
							columnsBuffer.append(column).append(",\n");
							valuesBuffer.append(":").append(getPrefixLowerText(column)).append("").append(",\n");
						}
					}
				}

				if (columnsBuffer.length() <= 0 || valuesBuffer.length() <= 0) {
					return "";
				}

				columnsBuffer.setLength(columnsBuffer.length() - 2);
				valuesBuffer.setLength(valuesBuffer.length() - 2);
				insertStatement.append(columnsBuffer.toString());

				insertStatement.append(") VALUES (\n");

				insertStatement.append(valuesBuffer.toString());

				insertStatement.append(")\n");
			} else {
				throw new Exception("Table is not exist !");
			}
		}
		return insertStatement.toString();
	}
	
	private static String createUpdateStatement(String tableName, Map<String, Object> dataMap, List<String> whereTableNameColumns,
			boolean addUseYn, boolean addDelYn) throws Exception {
		if (ObjectUtil.isEmpty(dataMap)) {
			throw new Exception("Parameter Map cannot be empty !");
		}
		
		if (ObjectUtil.isEmpty(whereTableNameColumns)) {
			throw new Exception("PK columns cannot be empty !");
		}

		boolean first = true;
		
		StringBuffer updateStatement = new StringBuffer();
		updateStatement.append("UPDATE ").append(tableName).append("\n SET ");
		
		Iterator<String> it = dataMap.keySet().iterator();
		while (it.hasNext()) {
			String fieldName = it.next();
			Object value = dataMap.get(fieldName);

			if (value == null) {
				continue;
			}

			String tableColumnName = toTableColumnName(fieldName);
			if (whereTableNameColumns.contains(tableColumnName)) {
				continue;
			}
			
			if (first) {
				first = false;
				updateStatement.append(tableColumnName).append(" = :").append(fieldName).append("\n");
			} else {
				updateStatement.append(",").append(tableColumnName).append(" = :").append(fieldName).append("\n");
			}
		}

		updateStatement.append("WHERE 1=1 \n");
		for (String pk : whereTableNameColumns) {
			updateStatement.append("AND ").append(pk).append(" = :").append(getPrefixLowerText(pk)).append("\n");
		}
		if (addUseYn) {
			updateStatement.append("AND USE_YN = 'Y'").append("\n");
		}
		if (addDelYn) {
			updateStatement.append("AND DEL_YN = 'N'").append("\n");
		}

		return updateStatement.toString();
	}
	
	private static String createDeleteStatement(String tableName, List<String> pkColumns, Map<String, Object> map) throws Exception {
		if (ObjectUtil.isEmpty(tableName)) {
			throw new Exception("Table name cannot be empty !");
		}
		
		if (ObjectUtil.isEmpty(pkColumns)) {
			throw new Exception("PK columns cannot be empty !");
		}

		StringBuffer deleteStatement = new StringBuffer();
		deleteStatement.append("DELETE FROM ").append(tableName).append(" \n");
		deleteStatement.append("WHERE 1=1 \n");
		
		for (String where : pkColumns) {
			deleteStatement.append("AND ").append(where).append(" = :").append(getPrefixLowerText(where)).append("\n");
		}

		return deleteStatement.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T get(final T dvo) throws Exception {
		final Class<T> classType = (Class<T>) dvo.getClass();

		// map객체가 비어있는 경우 종료
		if (ObjectUtil.isEmpty(dvo)) {
			throw new Exception("Parameter DVO cannot be empty !");
		}
		
		String tableName = getTableName(classType);

		// PK컬럼목록 반환.
		List<String> pkColumns = getMetaDAO().getPKColumns(tableName);

		// PK컬럼목록 반환.
		final List<String> columns = getMetaDAO().getColumns(tableName);

		// Data Map 반환.
		final Map<String, Object> map = ObjectUtil.toMap(dvo);

		// 진행처리가 가능한지 validation 체크
		if (checkPK(tableName, pkColumns, map)) {
			String selectStatement = createSelectStatement(tableName, pkColumns);

			return getMetaDAO().query(selectStatement, map, new ResultSetExtractor<T>() {
				
				public T extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					if (rs.next()) {
						T newInstance = null;
						try {
							newInstance = classType.newInstance();
							for (String column : columns) {
								String fieldName = "set".concat(getPrefixUpperText(column));

								Object valueObject = rs.getObject(column);

								if (valueObject == null) {
									continue;
								}
								
								Method declaredMethod = classType.getDeclaredMethod(fieldName, valueObject.getClass());
								if (ObjectUtil.isNotEmpty(declaredMethod)) {
									declaredMethod.invoke(newInstance, valueObject);
								}
							}
						}
						catch (Exception e) {
							throw new SQLException(e);
						}
						
						return newInstance;
					}
					
					return null;
				}
			});
		}
		return null;
	}
	
	/**
	 * pramValue와 dbValue를 비교하여 update 대상인 필드를 찾는다. paramValue가 새로 update 대상인
	 * 값이고 dbValue는 기준값이다.
	 * 
	 * @param paramValue
	 * @param dbValue
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static <T> T objectMerge(T paramValue, T dbValue) throws Exception {
		return objectMerge(paramValue, dbValue, null);
	}

	/**
	 * pramValue와 dbValue를 비교하여 update 대상인 필드를 찾는다. paramValue가 새로 update 대상인
	 * 값이고 dbValue는 기준값이다. excepts의 값은 merge에서 제외시킬 필드명을 기록한다. 보통 PK컬럼.
	 * 
	 * @param paramValue
	 *            merge할 대상 객체(update가 목적)
	 * @param dbValue
	 *            비교할 기준객체 (paramValue와 불일치한경우 update 대상 필드로 간주함.)
	 * @param excepts
	 *            merge에서 제외시킬 필드명
	 * @return
	 * @throws Exception
	 */
	private static <T> T objectMerge(final T paramValue, final T dbValue, final List<String> excepts) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(paramValue.getClass());
		
		// Iterate over all the attributes
		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

			// Only copy writable attributes
			Method writeMethod = descriptor.getWriteMethod();
			if (writeMethod == null) {
				continue;
			}

			Object originalValue = null;
			if(dbValue!=null)
			{
				descriptor.getReadMethod().invoke(dbValue);
			}
			
			Object newValue = descriptor.getReadMethod().invoke(paramValue);

			if (ObjectUtil.isEqual(originalValue, newValue)) {
				continue;
			}
			
			/* null인경우는 포함안함. 단 공백은 포함함 */
			else if (newValue == null) {
				continue;
			}

			String displayName = descriptor.getDisplayName();
			if (excepts != null) {
				if (!excepts.contains(displayName)) {
					writeMethod.invoke(paramValue, newValue);
				}
			} else {
				writeMethod.invoke(paramValue, newValue);
			}

		}
		return paramValue;
	}
	
	private static <T> boolean checkPK(final String tableName, List<String> pkColumns, final Map<String, Object> map) throws Exception {
		// 테이블명, 혹은 PK컬럼목록을 알수없으면 false
		if (ObjectUtil.isEmpty(tableName) || ObjectUtil.isEmpty(pkColumns)) {
			return false;
		}

		// 결과값
		boolean resultFlag = true;

		int dataPkCount = 0;
		
		// 진행처리가 가능한지 validation 체크
		if (ObjectUtil.isNotEmpty(pkColumns)) {
			for (String pkColumn : pkColumns) {
				// PK에 해당하는 컬럼이 map에 존재하지않으면 false
				if (ObjectUtil.isNotEmpty(map.get(getPrefixLowerText(pkColumn)))) {
					dataPkCount++;
				}
			}
		}

		if (!(dataPkCount == pkColumns.size())) {
			resultFlag = false;
		}
		
		return resultFlag;
	}
	
	private static boolean validateSchema(Map<String, Object> dataMap, List<String> pks) {
		Iterator<String> it = dataMap.keySet().iterator();

		int checkCount = 0;
		int pkSize = pks.size();
		
		while (it.hasNext()) {
			String next = it.next();
			
			if (pks.contains(toTableColumnName(next))) {
				checkCount++;
			} else if (dataMap.get(next) != null) {
				checkCount++;
			}

			if (checkCount > pkSize) {
				return true;
			}
		}

		return false;
	}
	
	private static String getTableName(Class<?> className)
	{
		StringBuffer sb = new StringBuffer();
		String simpleName = className.getSimpleName();
		
		if (simpleName.endsWith("DAO")) {
			simpleName = simpleName.substring(0, simpleName.indexOf("DAO"));
		}

		if (simpleName.endsWith("DVO")) {
			simpleName = simpleName.substring(0, simpleName.indexOf("DVO"));
		}

		sb.append(Character.toUpperCase(simpleName.charAt(0)));
		
		for (int i = 1; i < simpleName.length(); i++)
		{
			char charAt = simpleName.charAt(i);
			
			if (Character.isUpperCase(charAt)) {
				sb.append("_").append(charAt);
			} else if (Character.isLowerCase(charAt)) {
				sb.append(Character.toUpperCase(charAt));
			}

		}

		String tableName = sb.toString();
		
		DatabaseType databaseType = Enum.valueOf(DatabaseType.class, SysUtil.getProperty(PropertyKey.CACHE_DATABASE_TYPE));
		switch (databaseType) {
		case Oracle:
			tableName = tableName.toUpperCase();
			break;
		case PostgreSQL:
			tableName = tableName.toLowerCase();
			break;
		}
		
		return tableName;
	}
	
	private static String toTableColumnName(String fieldName) {
		StringBuffer sb = new StringBuffer();
		
		char[] charArray = fieldName.toCharArray();
		sb.append(Character.toUpperCase(charArray[0]));
		
		for (int i = 1, length = fieldName.length(); i < length; i++) {
			if (Character.isUpperCase(charArray[i])) {
				sb.append("_").append(charArray[i]);
			} else {
				sb.append(Character.toUpperCase(charArray[i]));
			}
		}
		
		return sb.toString();
	}
	
	private static String getPrefixUpperText(String str)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(str, "_");
		String nextElement = null;
		char[] charArray = null;
		String temp = "";
		
		while (stringTokenizer.hasMoreElements()) {
			nextElement = (String) stringTokenizer.nextElement();
			charArray = nextElement.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			
			for (int i = 1; i < charArray.length; i++) {
				charArray[i] = Character.toLowerCase(charArray[i]);
			}
			
			temp += String.valueOf(charArray);
		}
		
		return temp;
	}
	
	private static String getPrefixLowerText(String str)
	{
		char[] charArray = getPrefixUpperText(str).toCharArray();

		String lowerCase = String.valueOf(charArray[0]).toLowerCase();
		charArray[0] = lowerCase.charAt(0);

		return String.valueOf(charArray);
	}
}
