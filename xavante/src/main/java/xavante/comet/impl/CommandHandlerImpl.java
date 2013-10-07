package xavante.comet.impl;

import xavante.comet.CometHandler;
import xavante.comet.CometMessage;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class CommandHandlerImpl
	implements CometHandler
{
	@Override
	public String onMessage(CometMessage msg)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public String onError(Throwable t)
	{
		throw new NotImplementedYet();
	}
}
