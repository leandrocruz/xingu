package xavante.dispatcher.impl;

import xavante.XavanteRequest;
import xavante.dispatcher.RequestHandler;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class RequestHandlerSupport
	implements RequestHandler
{
	protected final String	configuredPath;

	public RequestHandlerSupport(String path)
	{
		this.configuredPath = path;
	}

	@Override
	public String getConfiguredPath()
	{
		return configuredPath;
	}

	@Override
	public void handle(XavanteRequest req)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
