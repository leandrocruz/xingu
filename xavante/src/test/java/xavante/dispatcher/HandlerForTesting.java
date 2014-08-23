package xavante.dispatcher;

import xavante.XavanteRequest;
import xavante.dispatcher.impl.RequestHandlerSupport;

public class HandlerForTesting
	extends RequestHandlerSupport
{
	public boolean			executed	= false;

	public XavanteRequest	req;

	public HandlerForTesting(String path)
	{
		super(path);
	}

	@Override
	public void handle(XavanteRequest req)
		throws Exception
	{
		executed = true;
		this.req = req;
	}
}
