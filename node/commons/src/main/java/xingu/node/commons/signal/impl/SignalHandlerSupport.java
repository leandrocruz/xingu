package xingu.node.commons.signal.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.idgenerator.Generator;
import xingu.idgenerator.impl.TimestampInMemoryGenerator;
import xingu.node.commons.session.Session;
import xingu.node.commons.session.SessionManager;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalWaiter;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.Waiters;
import xingu.utils.TimeUtils;

public class SignalHandlerSupport
	implements Configurable
{
	@Inject
	protected SessionManager		sessions;

	@Inject
	protected Factory				factory;

	protected volatile AtomicLong	count	= new AtomicLong(0);

	protected long					queryTimeout;

	protected Waiters<Signal>		waiters	= new Waiters<Signal>();

	protected Generator<String>		idGen;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String tm = conf.getChild("query").getAttribute("timeout", "5m");
		queryTimeout = TimeUtils.toMillis(tm);
		idGen        = factory.create(TimestampInMemoryGenerator.class, "signal-sequencer", 100);
	}

	public Signal query(Signal signal, Channel channel)
		throws Exception
	{
		return query(signal, null, channel);
	}
	
	public Signal query(Signal signal, ChannelFutureListener onWrite, Channel channel)
		throws Exception
	{
		Signal reply = null;
		touch(channel, signal);
		Waiter<Signal> waiter = new SignalWaiter(signal);
		waiters.add(waiter);

		try
		{
			ChannelFuture future = deliver(signal, channel);
			if(onWrite != null)
			{
				future.addListener(onWrite);
			}
			future.awaitUninterruptibly();
			reply = backFromTheFuture(future, signal, waiter); /* pretty cool don't you think? */
		}
		catch(InterruptedException e)
		{
			throw e;
		}
		catch(Throwable t)
		{
			return new ExceptionSignal(signal, t);
		}
		finally
		{
			waiters.remove(waiter);
		}

		return reply;
	}

	public ChannelFuture deliver(Signal signal, Channel channel)
		throws Exception
	{
		touch(channel, signal);
		return channel.write(signal);
	}

	protected Signal<?> backFromTheFuture(ChannelFuture future, Signal<?> signal, Waiter<Signal> waiter)
		throws InterruptedException
	{
		boolean success = future.isSuccess();
		if(success)
		{
			Signal<?> reply = waiters.waitForReply(waiter, queryTimeout); /* removed the waiter when not timed out */
			if(reply != null)
			{
				return reply;
			}
			return new TimeoutSignal(signal, queryTimeout);
		}
		else
		{
			Throwable cause = future.getCause();
			return new ExceptionSignal(signal, cause);
		}
	}

	protected String touch(Channel channel, Signal signal)
	{
		long sid = signal.getSessionId();
		if(sid <= 0)
		{
			Session session = sessions.by(channel);
			sid = session.getId();
			signal.setSessionId(sid);
		}
	
		String id = signal.getSignalId();
		if(StringUtils.isEmpty(id))
		{
			id = idGen.next(); //count.incrementAndGet();
			signal.setSignalId(id);
		}
		return id;
	}
}
