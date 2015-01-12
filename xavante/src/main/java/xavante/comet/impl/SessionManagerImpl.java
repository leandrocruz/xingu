package xavante.comet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.commons.lang3.RandomStringUtils;

import xavante.comet.CometSession;
import xavante.comet.MessageFactory;
import xavante.comet.SessionManager;
import xingu.container.Inject;
import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;

public class SessionManagerImpl
	implements SessionManager, Initializable
{
	@Inject
	private MessageFactory				factory;

	private Map<String, CometSession>	sessionById	= new HashMap<String, CometSession>();

	private int							idLength;

	@Override
	public void initialize()
		throws Exception
	{
		idLength = factory.getMessageIdLength();
	}

	private synchronized String nextId()
	{
		
		String id = RandomStringUtils.randomAlphanumeric(idLength);
		if(!sessionById.containsKey(id))
		{
			return id;
		}
		return nextId();
	}

	@Override
	public CometSession byId(String sessionId)
	{
		return sessionById.get(sessionId);
	}

	@Override
	public CometSession newSession()
	{
		String       id      = nextId();
		CometSession session = new CometSession(id);
		sessionById.put(id, session);
		return session;
	}
	
	@Override
	public boolean verifyOwnership(Signal signal)
	{
		return true;
	}

	@Override
	public void setOwner(String sessionId, Identity<?> identity)
	{
		CometSession session = byId(sessionId);
		session.setIdentity(identity);
	}

	@Override
	public List<CometSession> byOwner(Identity<?> identity)
	{
		List<CometSession> result = new ArrayList<CometSession>();
		Set<String>        keys   = sessionById.keySet();
		for(String key : keys)
		{
			CometSession session = sessionById.get(key);
			Identity<?>  id      = session.getIdentity();
			if(id != null && id.equals(identity))
			{
				result.add(session);
			}
		}
		return result;
	}
}