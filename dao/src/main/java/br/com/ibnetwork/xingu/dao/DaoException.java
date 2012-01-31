package br.com.ibnetwork.xingu.dao;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DaoException
    extends NestableRuntimeException
{
	private static final long serialVersionUID = 1L;

	public DaoException()
	{
		super();
	}

	public DaoException(String message, Throwable t)
	{
		super(message,t);
	}

	public DaoException(String message)
    {
		super(message);
    }

}
