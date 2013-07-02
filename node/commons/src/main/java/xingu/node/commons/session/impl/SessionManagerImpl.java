package xingu.node.commons.session.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.session.Session;
import xingu.node.commons.session.SessionManager;

public class SessionManagerImpl
	implements SessionManager
{
	private volatile AtomicLong	count		= new AtomicLong(0);

	private List<SessionImpl>	sessions	= new ArrayList<SessionImpl>();
	
	@Override
	public synchronized Session newSession(Channel channel)
	{
		long        id      = count.incrementAndGet();
		SessionImpl session = new SessionImpl(id, channel);
		sessions.add(session);
		return session;
	}

	@Override
	public synchronized Session by(Channel channel)
	{
		for(SessionImpl session : sessions)
		{
			Channel c = session.getChannel();
			if(c.equals(channel))
			{
				return session;
			}
		}
		return null;
	}

	@Override
	public synchronized Session by(long id)
	{
		for(SessionImpl session : sessions)
		{
			long my = session.getId();
			if(my == id)
			{
				return session;
			}
		}
		return null;
	}
}

class SessionImpl
	implements Session
{
	private final long		id;

	private final Channel	channel;

	public SessionImpl(long id, Channel channel)
	{
		this.id      = id;
		this.channel = channel;
	}

	@Override
	public long getId()
	{
		return id;
	}

	 @Override
	 public Channel getChannel()
	 {
		 return channel;
	 }
}