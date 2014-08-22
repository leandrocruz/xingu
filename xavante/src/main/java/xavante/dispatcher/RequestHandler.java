package xavante.dispatcher;

import xavante.XavanteRequest;

public interface RequestHandler
{
	String getConfiguredPath();
	
	void handle(XavanteRequest req)
		throws Exception;
}
