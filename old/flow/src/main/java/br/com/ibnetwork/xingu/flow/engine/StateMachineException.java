package br.com.ibnetwork.xingu.flow.engine;

public class StateMachineException 
	extends Exception
{
	private static final long serialVersionUID = 1L;

	public StateMachineException(String message, Throwable cause) 
	{
		super(message, cause);
	}

	public StateMachineException(String message) 
	{
		super(message);
	}
}
