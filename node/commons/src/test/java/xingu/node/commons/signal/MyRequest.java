package xingu.node.commons.signal;

import xingu.node.commons.signal.impl.SignalSupport;

public class MyRequest<T>
	extends SignalSupport<T>
{

	public MyRequest()
	{}

	public MyRequest(String id)
	{
		this.signalId = id;
	}
}
