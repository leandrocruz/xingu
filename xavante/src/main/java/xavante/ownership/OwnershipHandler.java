package xavante.ownership;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public interface OwnershipHandler
{
	boolean verifyOwnership(Signal signal);

	Identity<?> findOwner(String token, Signal signal);

	void setOwner(String token, Identity<?> identity);
}