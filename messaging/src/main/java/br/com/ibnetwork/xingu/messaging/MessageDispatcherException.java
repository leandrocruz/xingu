package br.com.ibnetwork.xingu.messaging;

public class MessageDispatcherException 
	extends RuntimeException
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