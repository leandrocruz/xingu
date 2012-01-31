package br.com.ibnetwork.xingu.messaging;

import org.apache.commons.lang.exception.NestableException;

public class MessageDispatcherException 
	extends NestableException
{
	public MessageDispatcherException()
	{
		super();
	}

	public MessageDispatcherException(String message)
	{
		super(message);
	}

	public MessageDispatcherException(Throwable t)
	{
		super(t);
	}

	public MessageDispatcherException(String message, Throwable t)
	{
		super(message, t);
	}

}