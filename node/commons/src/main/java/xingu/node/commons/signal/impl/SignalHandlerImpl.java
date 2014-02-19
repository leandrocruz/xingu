package xingu.node.commons.signal.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.node.commons.session.Session;
import xingu.node.commons.session.SessionManager;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalHandler;
import xingu.node.commons.signal.SignalWaiter;
import xingu.node.commons.signal.Waiter;
import xingu.node.commons.signal.processor.SignalProcessor;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.TimeUtils;

public class SignalHandlerImpl
	implements SignalHandler, Configurable
{
	@Inject
	private SignalProcessor			processor;
	
	@Inject
	protected SessionManager		sessions;

	protected long					queryTimeout;

	protected volatile AtomicLong	count	= new AtomicLong(0);

	protected List<Waiter<Signal>>	waiters	= Collections.synchronizedList(new ArrayList<Waiter<Signal>>());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String tm = conf.getChild("query").getAttribute("timeout", "1m");
		queryTimeout = TimeUtils.toMillis(tm);
	}

	public ChannelFuture deliver(Channel channel, Signal signal)
		throws Exception
	{
		touch(channel, signal);
		return channel.write(signal);
	}

	public Signal query(Channel channel, ChannelFutureListener onWrite, Signal signal)
		throws Exception
	{
		Signal reply = null;
		touch(channel, signal);
		Waiter<Signal> waiter = new SignalWaiter(signal);
		waiters.add(waiter);
		
		try
		{
			ChannelFuture future = deliver(channel, signal);
			future.addListener(onWrite);
			future.awaitUninterruptibly();
			reply = backFromTheFuture(future, signal, waiter); /* pretty cool don't you think? */
		}
		catch(Exception e)
		{
			return new ExceptionSignal(signal, e);
		}
		finally
		{
			waiters.remove(waiter);
		}

		return reply;
	}

	protected Signal backFromTheFuture(ChannelFuture future, Signal signal, Waiter<Signal> waiter)
	{
		boolean success = future.isSuccess();
		if(success)
		{
			Signal reply = waitForReply(signal, waiter);
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

	protected long touch(Channel channel, Signal signal)
	{
		long sid = signal.getSessionId();
		if(sid <= 0)
		{
			Session session = sessions.by(channel);
			sid = session.getId();
			signal.setSessionId(sid);
		}

		long id = signal.getSignalId();
		if(id <= 0)
		{
			id = count.incrementAndGet();
			signal.setSignalId(id);
		}
		return id;
	}
	
	protected Waiter<Signal> findWaiter(Signal signal)
	{
		for(Waiter<Signal> waiter : waiters)
		{
			if(waiter.waitingOn(signal))
			{
				return waiter;
			}
		}
		return null;
	}

	protected Signal waitForReply(Signal signal, Waiter<Signal> waiter)
	{
	    waiter.waitFor(queryTimeout);
	    return waiter.reply;
	}
	
	@Override
	public void on(Channel channel, Signal signal)
		throws Exception
	{
		Session session = sessions.by(channel);
		long    id      = session.getId();
		signal.setSessionId(id);

		Waiter<Signal> waiter = findWaiter(signal);
		if(waiter != null)
		{
			synchronized(waiter)
			{
				waiter.notify(signal);
			}
		}
		else
		{
			processor.process(signal, channel);
		}
	}
}