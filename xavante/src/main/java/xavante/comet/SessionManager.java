package xavante.comet;

import java.util.List;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public interface SessionManager
{
	CometSession newSession();

	CometSession byId(String sessionId);

	List<CometSession> byOwner(Identity<?> identity);
	
	boolean verifyOwnership(Signal signal);

	void setOwner(String sessionId, Identity<?> identity);
}
