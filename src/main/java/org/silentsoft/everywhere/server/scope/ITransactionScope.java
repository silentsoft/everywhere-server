package org.silentsoft.everywhere.server.scope;

public interface ITransactionScope<T> {
	
	public T doScope() throws Throwable;
	
}
