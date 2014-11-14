package xingu.node.commons.signal;

import xingu.node.commons.signal.impl.SignalSupport;

public class MyResponse<T>
	extends SignalSupport<T>
{

	public MyResponse(String id)
	{
		this.signalId = id;
	}
}