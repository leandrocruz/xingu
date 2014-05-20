package xavante.ownership.impl;

import java.util.HashMap;
import java.util.Map;

import xavante.ownership.OwnershipHandler;
import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public class OwnershipHandlerImpl
	implements OwnershipHandler
{
	protected Map<String, Identity<?>> identityByToken = new HashMap<String, Identity<?>>();
	
	@Override
	public boolean verifyOwnership(Signal signal)
	{
		return true;
	}

	@Override
	public Identity<?> findOwner(String token, Signal signal)
	{
		return identityByToken.get(token);
	}

	@Override
	public void setOwner(String token, Identity<?> identity)
	{
		identityByToken.put(token, identity);
	}
}