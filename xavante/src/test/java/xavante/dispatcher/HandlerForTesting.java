package xavante.dispatcher;

import xavante.XavanteRequest;

public class HandlerForTesting
	implements RequestHandler
{
	public boolean executed = false;
	
	@Override
	public void handle(XavanteRequest req)
		throws Exception
	{
		executed = true;
	}
}
