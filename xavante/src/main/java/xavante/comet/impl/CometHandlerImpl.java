package xavante.comet.impl;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public class CometHandlerImpl
	extends CometHandlerSupport
{
	@Override
	protected void verifyOwnership(Signal signal)
	{
	}

	@Override
	protected Identity findOwner(String token, Signal signal)
	{
		return null;
	}
}
