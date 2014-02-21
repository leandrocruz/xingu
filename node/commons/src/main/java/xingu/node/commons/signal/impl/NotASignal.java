package xingu.node.commons.signal.impl;


public class NotASignal
	extends SignalSupport
{
	private Object	object;

	public NotASignal(Object msg)
	{
		this.object = msg;
	}

	public Object getObject(){return object;}
	public void setObject(Object object){this.object = object;}
}
