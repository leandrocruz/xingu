package br.com.ibnetwork.xingu.store;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class StoreException
    extends NestableRuntimeException
{

	public StoreException() {
		super();
	}

	public StoreException(String message, Throwable e) {
		super(message, e);
	}

	public StoreException(String message) {
		super(message);
	}

	public StoreException(Throwable e) {
		super(e);
	}

	

}
