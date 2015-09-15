package org.silentsoft.everywhere.server.util;

import org.silentsoft.everywhere.server.scope.IErrorScope;
import org.silentsoft.everywhere.server.scope.ITransactionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public final class TransactionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionUtil.class);
	
	private static TransactionTemplate transactionTemplate;
	
	private static TransactionTemplate getTransactionTemplate() {
		if (transactionTemplate == null) {
			transactionTemplate = BeanUtil.get("transactionTemplate", TransactionTemplate.class);
		}
		
		return transactionTemplate;
	}
	
	public static <T> T doScope(ITransactionScope<T> iTransactionScope) {
		return getTransactionTemplate().execute(new TransactionCallback<T>() {
			@Override
			public T doInTransaction(TransactionStatus transactionStatus) {
				T result = null;
				
				try {
					result = iTransactionScope.doScope();
				} catch (Throwable e) {
					transactionStatus.setRollbackOnly();
					LOGGER.error(e.toString());
				}
				
				return result;
			}
		});
	}
	
	public static <T> T doScope(ITransactionScope<T> iTransactionScope, IErrorScope<T> iErrorScope) {
		return getTransactionTemplate().execute(new TransactionCallback<T>() {
			@Override
			public T doInTransaction(TransactionStatus transactionStatus) {
				T result = null;
				
				try {
					result = iTransactionScope.doScope();
				} catch (Throwable e) {
					transactionStatus.setRollbackOnly();
					result = iErrorScope.doScope();
				}
				
				return result;
			}
		});
	}
}
