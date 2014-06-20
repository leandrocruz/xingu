package xavante.comet;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public interface SessionManager
{
	CometSession newSession();

	CometSession byId(String sessionId);
	
	boolean verifyOwnership(Signal signal);

	void setOwner(String sessionId, Identity<?> identity);
}
