package xingu.node.commons.signal;

import xingu.node.commons.signal.impl.SignalSupport;

public class ErrorSignal<T>
	extends SignalSupport<T>
{
	protected boolean	error	= true;

	protected String	code;

	protected String	message;
	
	public ErrorSignal()
	{}
	
	public ErrorSignal(String code, String message)
	{
		this.message = message;
	}

	/* @formatter:off */
	public String getMessage(){return message;}
	public boolean isError(){return error;}
	public void setError(boolean error){this.error = error;}
	public String getCode(){return code;}
	public void setCode(String code){this.code = code;}
	public void setMessage(String message){this.message = message;}
	/* @formatter:on */
}
