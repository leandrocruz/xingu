package br.com.ibnetwork.xingu.flow.engine;

public class StateChangeException 
	extends StateMachineException
{
	private static final long serialVersionUID = 1L;

	public StateChangeException(String message, Throwable cause) 
	{
		super(message, cause);
	}

	public StateChangeException(String message) 
	{
		super(message);
	}

}
